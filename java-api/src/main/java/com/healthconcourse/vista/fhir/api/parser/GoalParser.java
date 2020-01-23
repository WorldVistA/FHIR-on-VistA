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
import org.hl7.fhir.dstu3.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GoalParser implements VistaParser<Goal> {

    private static final Logger LOG = LoggerFactory.getLogger(GoalParser.class);
    private static final String CURRENT = "CURRENT";
    private static final String MET = "MET";
    private static final String DISCONTINUED = "DISCONTINUED";

    @Override
    public List<Goal> parseList(String httpData) {
        List<Goal> result = new ArrayList<>();
        if (StringUtils.isEmpty(httpData)) {
            LOG.info("No goal data found for patient");
            return result;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson == null) {
            LOG.warn("Unable to parse Goal JSON");
            return result;
        }

        JSONArray carePlans = allJson.read("$.NCPS..NurseCarePlan");
        if (carePlans != null) {
            for (int i = 0; i < carePlans.length(); i++) {
                JSONObject json = carePlans.getJSONObject(i);

                List<Reference> addresses = new ArrayList<>();
                JSONObject problemList = json.optJSONObject("problemLists");
                if (problemList != null) {
                    JSONArray problems = problemList.getJSONArray("problemList");
                    for (int k = 0; k < problems.length(); k++) {
                        JSONObject problem = problems.getJSONObject(k);
                        String problemName = problem.getString("problem");
                        String problemId = problem.getString("resourceId");
                        addresses.add(ResourceHelper.createReference(HcConstants.URN_VISTA_CONDITION, problemId, problemName, ResourceHelper.ReferenceType.Condition));
                    }
                }
                JSONObject goalContainer = json.optJSONObject("targetDates");
                if (goalContainer != null) {
                    JSONArray goals = goalContainer.getJSONArray("targetDate");
                    for (int j = 0; j < goals.length(); j++) {
                        JSONObject goalJson = goals.getJSONObject(j);
                        Goal goal = getNewGoal(json);
                        goal.setAddresses(addresses);

                        String resourceId = goalJson.getString("resourceId");
                        goal.setId(resourceId);

                        String statusText = goalJson.getString("goalMetDcd");
                        goal.setStatus(getGoalStatus(statusText));

                        String description = goalJson.getString("goalExpectedOutcome");
                        CodeableConcept concept = new CodeableConcept();
                        concept.setText(description);
                        goal.setDescription(concept);

                        Optional<Date> startDate = InputValidator.parseAnyDate(goalJson.getString("dateTimeEnteredFHIR"));
                        if (startDate.isPresent()) {
                            DateType start = new DateType(startDate.get());
                            goal.setStart(start);
                        }

                        String targetDateRaw = goalJson.optString("targetDateFHIR");
                        Optional<Date> targetDate = InputValidator.parseAnyDate(targetDateRaw);
                        if (targetDate.isPresent()) {
                            Goal.GoalTargetComponent gt = new Goal.GoalTargetComponent();
                            DateType dateType = new DateType();
                            dateType.setValue(targetDate.get());
                            gt.setDue(dateType);
                            goal.setTarget(gt);
                        }

                        String expressedName = goalJson.getString("userWhoEntered");
                        String expressedId = goalJson.getString("userWhoEnteredResId");
                        goal.setExpressedBy(ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, expressedId, expressedName, ResourceHelper.ReferenceType.Practitioner));
                        result.add(goal);
                    }
                }
            }
        }
        return result;
    }

    private static Goal getNewGoal(JSONObject json) {
        Goal goal = new Goal();
        goal.setMeta(ResourceHelper.getVistaMeta());

        String patientName = json.getString("patient");
        String icn = json.getString("patientICN");
        Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
        goal.setSubject(patient);
        return goal;
    }

    private static Goal.GoalStatus getGoalStatus(String data) {

        switch (data.toUpperCase()) {
            case CURRENT:
                return Goal.GoalStatus.INPROGRESS;
            case MET:
                return Goal.GoalStatus.ACHIEVED;
            case DISCONTINUED:
                return Goal.GoalStatus.CANCELLED;
            default:
                return Goal.GoalStatus.NULL;
        }
    }
}
