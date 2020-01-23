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
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.dstu3.model.Reference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProcedureParser implements VistaParser<Procedure> {

    private static final Logger LOG = LoggerFactory.getLogger(ProcedureParser.class);

    @Override
    public List<Procedure> parseList(String httpData) {
        List<Procedure> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            LOG.warn("No procedure JSON returned from VistA");
            return results;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData.trim());
        if (allJson  == null) {
            LOG.warn("Invalid procedure JSON");
            LOG.warn(httpData);
            return results;
        }
        JSONObject radPatJson = allJson.read("$Radpat");
        results.addAll(parseRadPat(radPatJson));

        JSONArray surgeryJson = allJson.read("$Surgery");
        results.addAll(parseSurgery(surgeryJson));

        JSONArray visitJson = allJson.read("$Visit");
        results.addAll(parseVisit(visitJson));

        return results;
    }

    private static List<Procedure> parseVisit(JSONArray visitArray) {
        List<Procedure> results = new ArrayList<>();
        if (visitArray != null) {
            for (int i = 0; i < visitArray.length(); i++) {
                JSONObject visitJson = visitArray.getJSONObject(i).getJSONObject("Vcpt");
                String patientName = visitJson.getString("patientName");
                String icn = visitJson.getString("patientICN");
                Procedure procedure = getNewProcedure(visitJson, patientName, icn);
                String procName = visitJson.getString("providerNarrative");
                procedure.setCode(ResourceHelper.createCodeableConcept(null, null, procName));
                procedure.setCategory(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "103693007", "Diagnostic procedure"));
                procedure.setStatus(Procedure.ProcedureStatus.COMPLETED); // Assumption
                Optional<Date> procedureDate = InputValidator.parseAnyDate(visitJson.getString("visitFHIR"));
                if (procedureDate.isPresent()) {
                    procedure.setPerformed(new DateTimeType(procedureDate.get()));
                }
                String encounterId = visitJson.optString("visitResId");
                if (encounterId != null) {
                    procedure.setContext(ResourceHelper.createReference(HcConstants.URN_VISTA_ENCOUNTER, encounterId, null, ResourceHelper.ReferenceType.Encounter));
                }
                String provider = visitJson.optString("encounterProvider");
                String providerId = visitJson.optString("encounterProviderResId");
                if (!StringUtils.isEmpty(providerId)) {
                    Procedure.ProcedurePerformerComponent performer = new Procedure.ProcedurePerformerComponent();
                    performer.setActor(ResourceHelper.createReference(HcConstants.URN_VISTA_PRACTITIONER, providerId, provider, ResourceHelper.ReferenceType.Practitioner));
                    procedure.addPerformer(performer);
                }
                results.add(procedure);
            }
        }
        return results;
    }

    private static List<Procedure> parseSurgery(JSONArray surgeryArray) {
        List<Procedure> results = new ArrayList<>();
        if (surgeryArray != null) {
            for (int i = 0; i < surgeryArray.length(); i++) {
                JSONObject surgeryJson = surgeryArray.getJSONObject(i).getJSONObject("Surg");
                String patientName = surgeryJson.getString("patient");
                String icn = surgeryJson.getString("patientICN");
                Procedure procedure = getNewProcedure(surgeryJson, patientName, icn);

                procedure.setCategory(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "387713003", "Surgical procedure"));

                String procName = surgeryJson.getString("principalProcedure");
                procedure.setCode(ResourceHelper.createCodeableConcept(null, null, procName));

                Optional<Date> surgeryDate = InputValidator.parseAnyDate(surgeryJson.getString("dateOfOperationFHIR"));
                if (surgeryDate.isPresent()) {
                    procedure.setPerformed(new DateTimeType(surgeryDate.get()));
                }

                procedure.setStatus(Procedure.ProcedureStatus.COMPLETED); // Assumption

                String locationName = surgeryJson.optString("division");
                Long locationId = surgeryJson.optLong("divisionId", HcConstants.MISSING_ID);
                if (!StringUtils.isEmpty(locationName) && !locationId.equals(HcConstants.MISSING_ID)) {
                    procedure.setLocation(ResourceHelper.createReference(HcConstants.URN_VISTA_LOCATION, locationId.toString(), locationName, ResourceHelper.ReferenceType.Location));
                }

                String encounterId = surgeryJson.optString("visitResId");
                if (encounterId != null && !StringUtils.isEmpty(encounterId)) {
                    procedure.setContext(ResourceHelper.createReference(HcConstants.URN_VISTA_ENCOUNTER, encounterId, null, ResourceHelper.ReferenceType.Encounter));
                }

                String primarySurgeon = surgeryJson.optString("primarySurgeon");
                String primarySurgeonId = surgeryJson.optString("primarySurgeonResId");
                procedure.addPerformer(createPerformer(primarySurgeon, primarySurgeonId, "304292004", "Surgeon"));

                String attendingSurgeon = surgeryJson.optString("attendingSurgeon");
                String attendingSurgeonId = surgeryJson.optString("attendingSurgeonResId");
                procedure.addPerformer(createPerformer(attendingSurgeon, attendingSurgeonId, "405279007", "Attending Physician"));

                String countVerifier = surgeryJson.optString("countVerifier");
                String countVerifierId = surgeryJson.optString("countVerifierResId");
                procedure.addPerformer(createPerformer(countVerifier, countVerifierId, "310182000", "General nurse"));

                String anesthesiologist = surgeryJson.optString("anesthesiologistSupvr");
                String anesthesiologistId = surgeryJson.optString("anesthesiologistSupvrResId");
                procedure.addPerformer(createPerformer(anesthesiologist, anesthesiologistId, "88189002", "Anesthesiologist"));

                String assistant = surgeryJson.optString("firstAsst");
                String assistantId = surgeryJson.optString("firstAsstResId");
                procedure.addPerformer(createPerformer(assistant, assistantId, "304292004", "Surgeon"));

                JSONArray scrubNurses = getNestedArray(surgeryJson, "orScrubSupports", "orScrubSupport");
                for (int j = 0; j < scrubNurses.length(); j++) {
                    String scrubName = scrubNurses.getJSONObject(j).optString("orScrubSupport");
                    String scrubId = scrubNurses.getJSONObject(j).optString("resourceId");
                    procedure.addPerformer(createPerformer(scrubName, scrubId, "415506007", "Scrub nurse"));
                }

                JSONArray supportNurses = getNestedArray(surgeryJson, "orCircSupports", "orCircSupport");
                for (int j = 0; j < supportNurses.length(); j++) {
                    String supportName = supportNurses.getJSONObject(j).optString("orCircSupport");
                    String supportId = supportNurses.getJSONObject(j).optString("orCircSupportResId");
                    procedure.addPerformer(createPerformer(supportName, supportId, "310182000", "General nurse"));
                }

                JSONArray medications = getNestedArray(surgeryJson, "medicationss", "medications");
                for (int j = 0; j < medications.length(); j++) {
                    String medName = medications.getJSONObject(j).getString("medications");
                    String medId = medications.getJSONObject(j).getString("resourceId");
                    procedure.addUsedReference(ResourceHelper.createReference(HcConstants.URN_VISTA_MEDICATION, medId, medName, ResourceHelper.ReferenceType.Medication));
                }

                results.add(procedure);
            }
        }
        return results;
    }

    private static Procedure.ProcedurePerformerComponent createPerformer(String name, String id, String code, String display) {
        if (!StringUtils.isEmpty(name) && ! StringUtils.isEmpty(id)) {
            Procedure.ProcedurePerformerComponent performer = new Procedure.ProcedurePerformerComponent();
            performer.setActor(ResourceHelper.createReference(HcConstants.URN_VISTA_PRACTITIONER, id, name, ResourceHelper.ReferenceType.Practitioner));
            performer.setRole(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, code, display));
            return performer;
        } else {
            return null;
        }
    }

    private static List<Procedure> parseRadPat(JSONObject radPatJson) {
        List<Procedure> results = new ArrayList<>();

        if (radPatJson != null) {
            String patientName = radPatJson.getString("patientName");
            String icn = radPatJson.getString("patientNameICN");
            JSONArray registeredExams = getNestedArray(radPatJson, "registeredExamss", "registeredExams");
            for (int k=0; k < registeredExams.length(); k++) {
                JSONObject registeredExam = registeredExams.getJSONObject(k);
                Optional<Date> examDate = InputValidator.parseAnyDate(registeredExam.getString("examDateFHIR"));
                String hospitalName = registeredExam.optString("hospitalDivision");
                Long hospitalId = registeredExam.optLong("hospitalDivisionId", HcConstants.MISSING_ID);
                JSONArray exams = getNestedArray(registeredExam, "examinationss", "examinations");
                for (int i=0; i < exams.length(); i++) {
                    JSONObject examJson = exams.getJSONObject(i);
                    Procedure procedure = getNewProcedure(radPatJson, patientName, icn);
                    String procName = examJson.getString("procedure");
                    String sct = examJson.getString("procedureSct");
                    procedure.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, sct, procName));

                    // Radpat is always a diagnostic procedure
                    procedure.setCategory(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "103693007", "Diagnostic procedure"));

                    if (examDate.isPresent()) {
                        procedure.setPerformed(new DateTimeType(examDate.get()));
                    }
                    String status = examJson.getString("examStatus");
                    procedure.setStatus(convertStatus(status));

                    if (!StringUtils.isEmpty(hospitalName) && !hospitalId.equals(HcConstants.MISSING_ID)) {
                        procedure.setLocation(ResourceHelper.createReference(HcConstants.URN_VISTA_LOCATION, hospitalId.toString(), hospitalName, ResourceHelper.ReferenceType.Location));
                    }

                    String encounterId = examJson.optString("visitResId");
                    if (!StringUtils.isEmpty(encounterId)) {
                        procedure.setContext(ResourceHelper.createReference(HcConstants.URN_VISTA_ENCOUNTER, encounterId, null, ResourceHelper.ReferenceType.Encounter));
                    }

                    JSONArray technologists = getNestedArray(examJson, "technologists", "technologist");
                    for (int j = 0; j < technologists.length(); j++) {
                        JSONObject technologistJson = technologists.optJSONObject(j);
                        if (technologistJson != null) {
                            Procedure.ProcedurePerformerComponent performer = new Procedure.ProcedurePerformerComponent();
                            String name = technologistJson.getString("technologist");
                            String id = technologistJson.getString("technologistResId");
                            performer.setActor(ResourceHelper.createReference(HcConstants.URN_VISTA_PRACTITIONER, id, name, ResourceHelper.ReferenceType.Practitioner));
                            procedure.addPerformer(performer);
                        }
                    }

                    results.add(procedure);
                }
            }
        }
        return results;
    }

    private static JSONArray getNestedArray(JSONObject source, String objectName, String arrayName) {
        JSONArray result = new JSONArray();

        if (source != null) {
            JSONObject container = source.optJSONObject(objectName);
            if (container != null) {
                JSONArray array = container.optJSONArray(arrayName);
                if (array != null) {
                    result = array;
                }
            }
        }

        return result;
    }

    private static Procedure.ProcedureStatus convertStatus(String rawStatus) {
        switch (rawStatus.toUpperCase()) {
            case "COMPLETE":
            case "EXAMINED":
            case "TRANSCRIBED":
            case "COMPLETE - RAD":
                return Procedure.ProcedureStatus.COMPLETED;
            case "CALLED FOR EXAM":
            case "WAITING FOR EXAM":
                return Procedure.ProcedureStatus.PREPARATION;
            case "CANCELLED":
                return Procedure.ProcedureStatus.ABORTED;
             default:
                return Procedure.ProcedureStatus.UNKNOWN;
        }
    }

    private static Procedure getNewProcedure(JSONObject json, String name, String icn) {
        Procedure procedure = new Procedure();
        procedure.setMeta(ResourceHelper.getVistaMeta());
        String resourceId = json.getString("resourceId");
        procedure.setId(resourceId);
        Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, name, ResourceHelper.ReferenceType.Patient);
        procedure.setSubject(patient);
        return procedure;
    }
}
