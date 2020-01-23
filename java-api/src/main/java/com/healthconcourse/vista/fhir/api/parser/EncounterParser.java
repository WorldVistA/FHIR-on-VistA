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

public class EncounterParser implements VistaParser<Encounter> {

    private static final Logger LOG = LoggerFactory.getLogger(EncounterParser.class);

    @Override
    public List<Encounter> parseList(String httpData) {

        List<Encounter> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return results;
        }
        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson  == null) {
            LOG.error("Unable to parse Encounter JSON");
            return results;
        }

        JSONArray allRecords = allJson.read("$Encounters");
        if (allRecords != null) {
            for (int i = 0; i < allRecords.length(); i++) {
                JSONObject json = allRecords.getJSONObject(i).getJSONObject("Visit");
                try {
                    Encounter encounter = new Encounter();
                    encounter.setMeta(ResourceHelper.getVistaMeta());

                    String resourceId = json.getString("resourceId");
                    encounter.setId(resourceId);
                    LOG.debug(resourceId);
                    String patientName = json.getString("patientName");
                    String icn = json.getString("patientICN");
                    Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                    encounter.setSubject(patient);

                    encounter.setStatus(Encounter.EncounterStatus.FINISHED);

                    Optional<Date> visitDate = InputValidator.parseAnyDate(json.getString("visitAdmitDateTimeFHIR"));
                    if (visitDate.isPresent()) {
                        Period period = new Period();
                        period.setStart(visitDate.get());
                        encounter.setPeriod(period);
                    }

                    String serviceCategory = json.getString("serviceCategory");
                    if (!StringUtils.isEmpty(serviceCategory)) {
                        encounter.setClass_(this.getEncounterClass(serviceCategory));
                    }

                    JSONObject diagnosisJSON = json.optJSONObject("diagnosis").optJSONObject("Problem");
                    if (diagnosisJSON != null) {
                        String display = diagnosisJSON.getString("snomedCTconceptCodeText");
                        if (!StringUtils.isEmpty(display)) {
                            Long sctId = diagnosisJSON.optLong("snomedCTconceptCode", HcConstants.MISSING_ID);
                            String snomedId = sctId.longValue() != HcConstants.MISSING_ID ? sctId.toString() : "";
                            List<CodeableConcept> code = ResourceHelper.createSingleCodeableConceptAsList(HcConstants.SNOMED_URN, snomedId, display);
                            encounter.setReason(code);
                        }

                        Encounter.DiagnosisComponent diagnosis = new Encounter.DiagnosisComponent();
                        String conditionId = diagnosisJSON.getString("resourceId");
                        String conditionName = diagnosisJSON.getString("problem");
                        Reference condition = ResourceHelper.createReference(HcConstants.URN_VISTA_CONDITION, conditionId, conditionName, ResourceHelper.ReferenceType.Condition);
                        diagnosis.setCondition(condition);

                        List<Encounter.DiagnosisComponent> diagnoses = new ArrayList<>();
                        diagnoses.add(diagnosis);
                        encounter.setDiagnosis(diagnoses);

                        String site = diagnosisJSON.getString("facility");
                        if (!StringUtils.isEmpty(site)) {
                            Long siteId = diagnosisJSON.optLong("facilityId", HcConstants.MISSING_ID);
                            String id = siteId.longValue() != HcConstants.MISSING_ID ? siteId.toString() : "";
                            Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_LOCATION, id, site, ResourceHelper.ReferenceType.Location);
                            List<Encounter.EncounterLocationComponent> locations = new ArrayList<>();
                            Encounter.EncounterLocationComponent encounterLocation = new Encounter.EncounterLocationComponent();
                            encounterLocation.setLocation(ref);
                            locations.add(encounterLocation);
                            encounter.setLocation(locations);
                        }

                        String requester = diagnosisJSON.getString("responsibleProvider");
                        if (!StringUtils.isEmpty(requester)) {
                            String requesterId = diagnosisJSON.getString("responsibleProviderResId");
                            Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, requesterId, requester, ResourceHelper.ReferenceType.Practitioner);
                            List<Encounter.EncounterParticipantComponent> participantList = new ArrayList<>();
                            Encounter.EncounterParticipantComponent participant = new Encounter.EncounterParticipantComponent();
                            participant.setId(requesterId);
                            participant.setIndividual(ref);
                            participantList.add(participant);
                            encounter.setParticipant(participantList);
                        }
                    }
                    JSONObject locationJSON = json.optJSONObject("location");
                    if (locationJSON != null) {
                        JSONObject providerJSON = locationJSON.getJSONObject("Site");
                        if (providerJSON != null) {
                            String agency = providerJSON.getString("officialVaName");
                            String agencyId = providerJSON.getString("resourceId");
                            encounter.setServiceProvider(ResourceHelper.createReference(HcConstants.URN_VISTA_ORGANIZATION, agencyId, agency));
                        }
                    }
                    results.add(encounter);
                } catch (Exception ex) {
                    LOG.warn("Error parsing encounter JSON", ex);
                    LOG.warn(json.toString());
                }
            }
        }
        return results;
    }

    private Coding getEncounterClass(String serviceType) {
        Coding result = new Coding();
        result.setSystem(HcConstants.HL7_ACT_ENCOUNTER_CODE);
        switch (serviceType.toUpperCase()) {
            case "AMBULATORY":
                result.setCode("AMB");
                result.setDisplay("ambulatory");
                break;
            case "HOSPITALIZATION":
            case "IN HOSPITAL":
            case "DAILY HOSPITALIZATION DATA":
            case "OBSERVATION":
                result.setCode("IMP");
                result.setDisplay("inpatient encounter");
                break;
            case "TELECOMMUNICATIONS":
                result.setCode("VR");
                result.setDisplay("virtual");
                break;
            case "EVENT (HISTORICAL)":
                result.setCode("HISTORIC");
                result.setDisplay("record recorded as historical");
                break;
            case "CHART REVIEW":
                result.setCode("CLINNOTEREV");
                result.setDisplay("clinical note review taskl");
                break;
            case "DAY SURGERY":
                result.setCode("SS");
                result.setDisplay("short stay");
                break;
            case "NURSING HOME":
                result.setCode("HH");
                result.setDisplay("home health");
                break;
            case "ANCILLARY PACKAGE DAILY DATA":
            default:
                LOG.info(String.format("Service Type %s does not match HL7 definitions", serviceType));
                result.setCode("CODE_INVAL");
                result.setDisplay("code is not valid");
                break;
        }
        return result;
    }
}
