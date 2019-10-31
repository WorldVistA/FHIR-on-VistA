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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        try {
            ReadContext ctx = JsonPath.parse(httpData);

            Integer totalCareTeams = ctx.read("$.Teams.length()");

            for (int careTeamCounter = 0; careTeamCounter < totalCareTeams; careTeamCounter++) {
                CareTeam careTeam = new CareTeam();
                careTeam.setMeta(ResourceHelper.getVistaMeta());

                String basePath = "$.Teams[" + careTeamCounter + "].Team.";
                String resourceId = ctx.read(basePath + "resourceId");
                careTeam.setId(resourceId);
                String name = ctx.read(basePath + "teamName");
                careTeam.setName(name);
                String status = ctx.read(basePath + "currentStatusC");
                careTeam.setStatus(getStatus(status));
                String teamType = ctx.read(basePath + "fhirTeamType");
                careTeam.setCategory(ResourceHelper.createSingleCodeableConceptAsList("http://hl7.org/fhir/care-team-category", teamType, getCategoryDesc(teamType)));
                String institution = ctx.read(basePath + "institution");
                if (!StringUtils.isEmpty(institution)) {
                    Integer institutionId = ctx.read(basePath + "institutionId");
                    careTeam.setManagingOrganization(ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_ORGANIZATION, institutionId.toString(), institution));
                }
                String startDate = ctx.read(basePath + "currentActivationDateCFHIR");
                String endDate = ctx.read(basePath + "currentInactivationDateCFHIR");
                Optional<Period> period = getPlanPeriod(startDate, endDate);
                if (period.isPresent()) {
                    careTeam.setPeriod(period.get());
                }
                String positionsPath = basePath  + "positions.position.length()";
                if (InputValidator.pathExists(ctx, positionsPath)) {
                    List<CareTeam.CareTeamParticipantComponent> positions = getParticipants(ctx, basePath);
                    careTeam.setParticipant(positions);
                }

                result.add(careTeam);
            }
        } catch (PathNotFoundException pex) {
            LOG.warn("Invalid JSON found", pex);
            LOG.warn(httpData);
        } catch (Exception ex) {
            LOG.warn("Parsing failure", ex);
            LOG.warn(httpData);
        }
        return result;
    }

    private List<CareTeam.CareTeamParticipantComponent> getParticipants(ReadContext context, String basePath) {
        List<CareTeam.CareTeamParticipantComponent> result = new ArrayList<>();
        Integer positionCount = context.read(basePath  + "positions.position.length()");

        for(int positionIndex = 0; positionIndex < positionCount; positionIndex++) {
            CareTeam.CareTeamParticipantComponent participant = new CareTeam.CareTeamParticipantComponent();
            String resourceId = context.read(basePath + "positions.position[" + positionIndex + "].resourceId");
            participant.setId(resourceId);
            String currentPractitioner = context.read(basePath + "positions.position[" + positionIndex + "].currPractitionerC");
            if (!StringUtils.isEmpty(currentPractitioner)) {
                participant.setMember(ResourceHelper.createReference(HcConstants.URN_VISTA_PRACTITIONER, resourceId, currentPractitioner));
            }
            String role = context.read(basePath + "positions.position[" + positionIndex + "].standRoleName");
            if (!StringUtils.isEmpty(role)) {
                Integer roleId = context.read(basePath + "positions.position[" + positionIndex + "].standRoleNameId");
                participant.addRole(ResourceHelper.createCodeableConcept(HcConstants.URN_VISTA_CARETEAM_ROLE, roleId.toString(), role));
            }
            result.add(participant);
        }

        return result;
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
