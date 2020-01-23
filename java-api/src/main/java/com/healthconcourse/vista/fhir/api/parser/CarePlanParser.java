/* Created by Perspecta http://www.perspecta.com */
/*
        Licensed to the Apache Software Foundation (ASF) under one
        or more contributor license agreements.  See the NOTICE file
        distributed with this work for additional information
        regarding copyright ownership.  The ASF licenses this file
        to you under the Apache License, Version 2.0 (the
        "License"); you may not use this file except in compliance
        with the License.  You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
        Unless required by applicable law or agreed to in writing,
        software distributed under the License is distributed on an
        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
        KIND, either express or implied.  See the License for the
        specific language governing permissions and limitations
        under the License.
*/
package com.healthconcourse.vista.fhir.api.parser;

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;

import com.nfeld.jsonpathlite.JsonPath;
import com.nfeld.jsonpathlite.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.dstu3.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CarePlanParser {

    private static final Logger LOG = LoggerFactory.getLogger(CarePlanParser.class);
    private static final String CURRENT = "CURRENT";
    private static final String ACTIVE = "ACTIVE";
    private static final String MET = "MET";
    private static final String DISCONTINUED = "DISCONTINUED";
    private static final String PROGRESS = "IN-PROGRESS";

    public List<CarePlan> parseCarePlan(String httpData, String icn) {
        List<CarePlan> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return results;
        }

        JsonResult parsedJson = JsonPath.parseOrNull(httpData);
        if (parsedJson == null) {
            LOG.warn("Unable to parse Care Plan JSON");
            return results;
        }
        JSONArray allPlans = parsedJson.read("$.CarePlan");

        if (allPlans != null) {
            for (int i = 0; i < allPlans.length(); i++) {
                JSONObject visitJson = allPlans.getJSONObject(i);
                JSONArray visits = visitJson.getJSONArray("Visit");
                CarePlan carePlan = new CarePlan();
                List<CarePlan.CarePlanActivityComponent> activities = new ArrayList<>();
                // A visit can be either a new care plan or an activity under the last care plan
                //  so activities need to accumulate before making a new plan
                for (int visitCounter = 0; visitCounter < visits.length(); visitCounter++) {
                    JSONObject json = visits.getJSONObject(visitCounter).getJSONObject("HealthFactor");
                    String cpType = json.getString("cpType");
                    if (cpType.toLowerCase().equals("careplan")) {
                        carePlan = new CarePlan();
                        activities = new ArrayList<>();
                        carePlan.setMeta(ResourceHelper.getVistaMeta());
                        String resourceId = json.getString("resourceId");
                        carePlan.setId(resourceId);
                        String cpIntent = json.getString("cpIntent");
                        if (!StringUtils.isEmpty(cpIntent)) {
                            carePlan.setIntent(getIntent(cpIntent));
                        }
                        String cpState = json.getString("cpState");
                        if (!StringUtils.isEmpty(cpState)) {
                            carePlan.setStatus(getCarePlanStatus(cpState));
                        }
                        String cpStartFHIR = json.getString("cpStartFHIR");
                        if (!StringUtils.isEmpty(cpStartFHIR)) {
                            String cpEndFHIR = json.getString("cpEndFHIR");
                            Optional<Period> carePlanPeriod = getPlanPeriod(cpStartFHIR, cpEndFHIR);
                            if (carePlanPeriod.isPresent()) {
                                carePlan.setPeriod(carePlanPeriod.get());
                            }
                        }
                        String patientName = json.getString("patientName");
                        Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                        carePlan.setSubject(ref);

                        Long code = json.optLong("hlfNameSCT", HcConstants.MISSING_ID);
                        String snomedCode = code.longValue() != HcConstants.MISSING_ID ? code.toString() : "";
                        String snomedName = json.getString("hlfName");
                        carePlan.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.SNOMED_URN, snomedCode, snomedName));
                        carePlan.setTitle(snomedName);
                        carePlan.setDescription(snomedName);

                        String encounterId = json.getString("visitResId");
                        if (!StringUtils.isEmpty(encounterId)) {
                            carePlan.setContext(ResourceHelper.createReference(HcConstants.URN_VISTA_ENCOUNTER, encounterId, null, ResourceHelper.ReferenceType.Encounter));
                        }
                    } else if (cpType.toLowerCase().equals("activity")) {
                        activities.add(getActivity(json));
                    }

                    if (!activities.isEmpty()) {
                        carePlan.setActivity(activities);
                    }

                    results.add(carePlan);
                }
            }
        }
        return results;
    }

    private CarePlan.CarePlanActivityComponent getActivity(JSONObject json) {
        CarePlan.CarePlanActivityComponent activity = new CarePlan.CarePlanActivityComponent();
        CarePlan.CarePlanActivityDetailComponent detail = new CarePlan.CarePlanActivityDetailComponent();
        activity.setDetail(detail);

        String snomedName = json.getString("hlfName");
        Long code = json.optLong("hlfNameSCT", HcConstants.MISSING_ID);
        String snomedCode = code.longValue() != HcConstants.MISSING_ID ? code.toString() : "";
        detail.setDescription(snomedName);
        String cpState = json.getString("cpState");
        if (!cpState.equals("")) {
            detail.setStatus(getActivityStatus(cpState));
        }

        if (!snomedName.equals("")) {
            detail.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, snomedCode, snomedName));
            detail.setDescription(snomedName);
        }

        String encounterProvider = json.getString("encounterProvider");
        String encounterProviderId = json.getString("encounterProviderId");
        if (!encounterProvider.equals("")) {
            detail.setPerformer(ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_PRACTITIONER, encounterProviderId, encounterProvider, ResourceHelper.ReferenceType.Practitioner));
        }

        String eventDateTimeFHIR =json.getString("eventDateTimeFHIR");
        Optional<Period> detailPeriod = getPlanPeriod(eventDateTimeFHIR, "");
        if (detailPeriod.isPresent()) {
            detail.setScheduled(detailPeriod.get());
        }

        return activity;
    }

    private Optional<Period> getPlanPeriod(String planStart, String planEnd) {
        if (!planStart.equals("")) {
            Period planPeriod = new Period();
            Optional<Date> planStartDate = InputValidator.parseAnyDate(planStart);
            if (planStartDate.isPresent()) {
                planPeriod.setStart(planStartDate.get());
            }
            if (!planEnd.equals("")) {
                Optional<Date> planEndDate = InputValidator.parseAnyDate(planEnd);
                if (planEndDate.isPresent()) {
                    planPeriod.setEnd(planEndDate.get());
                }
            }
            return Optional.of(planPeriod);
        }
        return Optional.empty();
    }

    private static CarePlan.CarePlanIntent getIntent(String data) {
        CarePlan.CarePlanIntent intent;
        switch (data.toUpperCase()) {
            case "PLAN":
                intent = CarePlan.CarePlanIntent.PLAN;
                break;
            case "ORDER":
                intent = CarePlan.CarePlanIntent.ORDER;
                break;
            case "PROPOSAL":
                intent = CarePlan.CarePlanIntent.PROPOSAL;
                break;
                case "OPTION":
                intent = CarePlan.CarePlanIntent.OPTION;
                break;
            default:
                intent = CarePlan.CarePlanIntent.NULL;
                break;
        }

        return intent;
    }

    private static CarePlan.CarePlanActivityStatus getActivityStatus(String data) {
        CarePlan.CarePlanActivityStatus status;

        switch (data.toUpperCase()) {
            case PROGRESS:
            case CURRENT:
                status = CarePlan.CarePlanActivityStatus.INPROGRESS;
                break;
            case MET:
                status = CarePlan.CarePlanActivityStatus.COMPLETED;
                break;
            case DISCONTINUED:
                status = CarePlan.CarePlanActivityStatus.CANCELLED;
                break;
            default:
                status = CarePlan.CarePlanActivityStatus.UNKNOWN;
                break;
        }

        return status;
    }

    private static CarePlan.CarePlanStatus getCarePlanStatus(String data) {
        CarePlan.CarePlanStatus status;

        switch (data.toUpperCase()) {
            case ACTIVE:
            case CURRENT:
                status = CarePlan.CarePlanStatus.ACTIVE;
                break;
            case MET:
                status = CarePlan.CarePlanStatus.COMPLETED;
                break;
            case DISCONTINUED:
                status = CarePlan.CarePlanStatus.CANCELLED;
                break;
            default:
                status = CarePlan.CarePlanStatus.UNKNOWN;
                break;
        }

        return status;
    }
}
