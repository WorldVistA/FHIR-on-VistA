/* Created by Perspecta http://www.perspecta.com */
/*
(c) 2017-2019 Perspecta

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.healthconcourse.vista.fhir.api.parser;

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.r4.model.*;
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

        if (org.apache.commons.lang3.StringUtils.isEmpty(httpData)) {
            return results;
        }

        if (httpData.trim().equalsIgnoreCase("{}")) {
            return results;
        }

        ReadContext ctx = JsonPath.parse(httpData);

        try {
            Integer totalCarePlans = ctx.read("$.CarePlan.length()");
            Integer totalVisits = ctx.read("$.CarePlan[0].Visit.length()");

            for (int carePlanCounter = 0; carePlanCounter < totalCarePlans; carePlanCounter++) {
                CarePlan carePlan = new CarePlan();
                carePlan.setMeta(ResourceHelper.getVistaMeta());
                List<CarePlan.CarePlanActivityComponent> activities = new ArrayList<>();
                for (int visitCounter = 0; visitCounter < totalVisits; visitCounter++) {
                    String basePath = "$.CarePlan[" + carePlanCounter + "].Visit[" + visitCounter + "].HealthFactor.";
                    String cpType = ctx.read(basePath + "cpType");
                    if (cpType.toLowerCase().equals("careplan")) {
                        String resourceId = ctx.read(basePath + "resourceId");
                        carePlan.setId(resourceId);
                        String cpIntent = ctx.read(basePath + "cpIntent");
                        if (!StringUtils.isEmpty(cpIntent)) {
                            carePlan.setIntent(getIntent(cpIntent));
                        }
                        String cpState = ctx.read(basePath + "cpState");
                        if (!StringUtils.isEmpty(cpState)) {
                            carePlan.setStatus(getCarePlanStatus(cpState));
                        }
                        String cpStartFHIR = ctx.read(basePath + "cpStartFHIR");
                        if (!StringUtils.isEmpty(cpStartFHIR)) {
                            String cpEndFHIR = ctx.read(basePath + "cpEndFHIR");
                            Optional<Period> carePlanPeriod = getPlanPeriod(cpStartFHIR, cpEndFHIR);
                            if (carePlanPeriod.isPresent()) {
                                carePlan.setPeriod(carePlanPeriod.get());
                            }
                        }
                        String patientName = ctx.read(basePath + "patientName");
                        Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                        carePlan.setSubject(ref);

                        Integer snomedCode = ctx.read(basePath + "hlfNameSCT");
                        String snomedName = ctx.read(basePath + "hlfName");
                        carePlan.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.SNOMED_URN, snomedCode.toString(), snomedName));
                        carePlan.setTitle(snomedName);
                        carePlan.setDescription(snomedName);
                    } else if (cpType.toLowerCase().equals("activity")) {
                        activities.add(getActivity(ctx, basePath));
                    }

                    if (!activities.isEmpty()) {
                        carePlan.setActivity(activities);
                    }

                    results.add(carePlan);
                }
            }
        } catch (PathNotFoundException pex) {
            LOG.warn("Invalid JSON found", pex);
            LOG.warn(httpData);
        }
        return results;
    }

    private CarePlan.CarePlanActivityComponent getActivity(ReadContext ctx, String basePath) {

        CarePlan.CarePlanActivityComponent activity = new CarePlan.CarePlanActivityComponent();
        CarePlan.CarePlanActivityDetailComponent detail = new CarePlan.CarePlanActivityDetailComponent();
        activity.setDetail(detail);

        String snomedName = ctx.read(basePath + "hlfName");
        Integer snomedCode = ctx.read(basePath + "hlfNameSCT");

        detail.setDescription(snomedName);
        String cpState = ctx.read(basePath + "cpState");
        if (!cpState.equals("")) {
            detail.setStatus(getActivityStatus(cpState));
        }

        if (!snomedName.equals("")) {
            detail.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, snomedCode.toString(), snomedName));
            detail.setDescription(snomedName);
        }

        String encounterProvider = ctx.read(basePath + "encounterProvider");
        String encounterProviderId = ctx.read(basePath + "encounterProviderId");
        if (!encounterProvider.equals("")) {
            detail.setPerformer(ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_PRACTITIONER, encounterProviderId, encounterProvider, ResourceHelper.ReferenceType.Practitioner));
        }

        String eventDateTimeFHIR = ctx.read(basePath + "eventDateTimeFHIR");
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
                status = CarePlan.CarePlanStatus.REVOKED;
                break;
            default:
                status = CarePlan.CarePlanStatus.UNKNOWN;
                break;
        }

        return status;
    }
}
