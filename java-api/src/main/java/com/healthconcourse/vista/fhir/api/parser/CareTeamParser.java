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
import org.hl7.fhir.dstu3.model.CareTeam;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CareTeamParser implements VistaParser<CareTeam> {

    private static final Logger LOG = LoggerFactory.getLogger(CareTeamParser.class);
    @Override
    public List<CareTeam> parseList(String httpData) {
        List<CareTeam> result = new ArrayList<>();
        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult json = JsonPath.parseOrNull(httpData);
        if (json  == null) {
            LOG.warn("Unable to parse Care Team JSON");
            return result;
        }

        JSONArray allTeams = json.read("$.Teams..Team");

        if (allTeams != null) {
            for (int i = 0; i < allTeams.length(); i++) {
                JSONObject raw = allTeams.getJSONObject(i);
                CareTeam careTeam = new CareTeam();
                careTeam.setMeta(ResourceHelper.getVistaMeta());
                String resourceId = raw.getString( "resourceId");
                careTeam.setId(resourceId);
                String name = raw.getString("teamName");
                careTeam.setName(name);
                String status = raw.getString("currentStatusC");
                careTeam.setStatus(getStatus(status));
                String teamType = raw.getString("fhirTeamType");
                careTeam.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.CARE_TEAM_CODING_SYSTEM, teamType, getCategoryDesc(teamType)));
                String institution = raw.getString("institution");
                if (!StringUtils.isEmpty(institution)) {
                    Long institutionId = raw.getLong("institutionId");
                    careTeam.setManagingOrganization(ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_ORGANIZATION, institutionId.toString(), institution));
                }
                String startDate = raw.getString("currentActivationDateCFHIR");
                String endDate = raw.getString("currentInactivationDateCFHIR");
                Optional<Period> period = getPlanPeriod(startDate, endDate);
                if (period.isPresent()) {
                    careTeam.setPeriod(period.get());
                }
                careTeam.setParticipant(getParticipants(raw));

                result.add(careTeam);
            }
        }
        return result;
    }

    private static List<CareTeam.CareTeamParticipantComponent> getParticipants(JSONObject record) {
        List<CareTeam.CareTeamParticipantComponent> memberList = new ArrayList<>();
        try {
            JSONObject members = record.getJSONObject("positions");
            JSONArray positions = members.getJSONArray("position");
            if (positions != null) {
                for (int j = 0; j < positions.length(); j++) {
                    CareTeam.CareTeamParticipantComponent participant = new CareTeam.CareTeamParticipantComponent();
                    String positionResourceId = positions.getJSONObject(j).getString("resourceId");
                    participant.setId(positionResourceId);
                    Optional<Reference> practitioner = getPractitioner(positions.getJSONObject(j));
                    if (practitioner.isPresent()) {
                        participant.setMember(practitioner.get());
                    }

                    String role = positions.getJSONObject(j).getString("standRoleName");
                    if (!StringUtils.isEmpty(role)) {
                        Long roleId = positions.getJSONObject(j).getLong("standRoleNameId");
                        participant.setRole(ResourceHelper.createCodeableConcept(HcConstants.URN_VISTA_CARETEAM_ROLE, roleId.toString(), role));
                    }
                    memberList.add(participant);
                }
            }
        } catch (JSONException ex) {
            LOG.warn("Team Member Positions not found", ex);
        }
        return memberList;
    }

    private static Optional<Reference> getPractitioner (JSONObject json) {
        String currentPractitioner = json.getString("currPractitionerC");
        JSONObject history = json.optJSONObject("positionAssignmentHistory");
        if (history != null) {
            JSONArray assignment = history.getJSONArray("posAssignHist");
            JSONObject firstAssignment = assignment.getJSONObject(0);
            String id = firstAssignment.getString("practitionerResId");
            currentPractitioner = firstAssignment.getString("practitioner");
            Optional<Reference> result = Optional.of(ResourceHelper.createReference(HcConstants.URN_VISTA_PRACTITIONER, id, currentPractitioner, ResourceHelper.ReferenceType.Practitioner));
            return result;
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Period> getPlanPeriod(String planStart, String planEnd) {
        if (!StringUtils.isEmpty(planStart)) {
            Period planPeriod = new Period();
            Optional<Date> planStartDate = InputValidator.parseAnyDate(planStart);
            if (planStartDate.isPresent()) {
                planPeriod.setStart(planStartDate.get());
            }
            if (!StringUtils.isEmpty(planEnd)) {
                Optional<Date> planEndDate = InputValidator.parseAnyDate(planEnd);
                if (planEndDate.isPresent()) {
                    planPeriod.setEnd(planEndDate.get());
                }
            }
            return Optional.of(planPeriod);
        }
        return Optional.empty();
    }

    private static String getCategoryDesc(String catetgory) {
        switch (catetgory.toLowerCase()) {
            case "event":
                return "Event";
            case "encounter":
                return "Encounter";
            case "episode":
                return "Episode";
            case "longitudinal":
                return "Longitudinal Care Coordination";
            case "condition":
                return "Condition";
            case "clinical-research":
                return "Clinical Research";
            default:
                return "";
        }
    }

    private static CareTeam.CareTeamStatus getStatus(String teamStatus) {
        switch (teamStatus.toLowerCase()) {
            case "active":
                return CareTeam.CareTeamStatus.ACTIVE;
            case "inactive":
                return CareTeam.CareTeamStatus.INACTIVE;
            default:
                return CareTeam.CareTeamStatus.NULL;
        }
    }
}
