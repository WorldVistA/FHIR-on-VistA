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

public class MedicationParser {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationParser.class);
    private static final String ACTIVE = "ACTIVE";
    private static final String EXPIRED = "EXPIRED";
    private static final String DISCONTINUED = "DISCONTINUED BY PROVIDER";

    private static class Medication {
        private String resourceId;
        private String patientName;
        private String patientIcn;
        private String status;
        private Date startDate;
        private String drugName;
        private String rxNorm;
        private Double dosage;
        private String route;
        private String dosageUnits;
        private Long daysSupply;

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getPatientIcn() {
            return patientIcn;
        }

        public void setPatientIcn(String patientIcn) {
            this.patientIcn = patientIcn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public String getDrugName() {
            return drugName;
        }

        public void setDrugName(String drugName) {
            this.drugName = drugName;
        }

        public String getRxNorm() {
            return rxNorm;
        }

        public void setRxNorm(String rxNorm) {
            this.rxNorm = rxNorm;
        }

        public Double getDosage() {
            return dosage;
        }

        public void setDosage(Double dosage) {
            this.dosage = dosage;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getDosageUnits() {
            return dosageUnits;
        }

        public void setDosageUnits(String dosageUnits) {
            this.dosageUnits = dosageUnits;
        }

        public Long getDaysSupply() {
            return daysSupply;
        }

        public void setDaysSupply(Long daysSupply) {
            this.daysSupply = daysSupply;
        }
    }

    public List<MedicationStatement> parseMedicationStatement(String httpData) {
        List<MedicationStatement> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return results;
        }

        JsonResult medicationJson = JsonPath.parseOrNull(httpData);
        if (medicationJson == null) {
            LOG.warn("Unable to parse Medication Statement JSON");
            return results;
        }

        Medication outPatient = this.getOutpatientStatements(medicationJson);
        if (outPatient != null) {
            results.add(convertToStatement(outPatient));
        }

        List<Medication> inPatient = this.getInpatientStatements(medicationJson);
        for (Medication med : inPatient) {
            results.add(convertToStatement(med));
        }
        return results;
    }

    private static MedicationStatement convertToStatement(Medication med) {
        MedicationStatement medicationStatement = new MedicationStatement();
        medicationStatement.setMeta(ResourceHelper.getVistaMeta());
        medicationStatement.setId(med.getResourceId());
        Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, med.getPatientIcn(), med.getPatientName(), ResourceHelper.ReferenceType.Patient);
        medicationStatement.setSubject(patient);
        MedicationStatement.MedicationStatementStatus status = getStatementStatus(med.getStatus());
        if(status != MedicationStatement.MedicationStatementStatus.NULL) {
            medicationStatement.setStatus(status);
        }
        if (med.getStartDate() != null) {
            Period period = new Period();
            period.setStart(med.getStartDate());
            medicationStatement.setEffective(period);
        }
        if (med.getRxNorm() != null && !med.getRxNorm().equals("code not mapped")) {
            medicationStatement.setMedication(ResourceHelper.createCodeableConcept(HcConstants.RX_NORM, med.getRxNorm(), med.getDrugName()));
        } else {
            Reference reference = new Reference();
            reference.setDisplay(med.getDrugName());
            medicationStatement.setMedication(reference);
        }

        Dosage dose = new Dosage();
        if (med.getDosage() != null && med.getDosage() != 0) {
            SimpleQuantity doseAmount = new SimpleQuantity();
            doseAmount.setValue(med.getDosage());
            doseAmount.setCode(med.getDosageUnits());
            dose.setDose(doseAmount);
            dose.setText(med.getDrugName());
            if (!StringUtils.isEmpty(med.getRoute())) {
                Optional<CodeableConcept> route = getDoseRoute(med.getRoute());
                if (route.isPresent()) {
                    dose.setRoute(route.get());
                }
            }
            List<Dosage> doseList = new ArrayList<>();
            doseList.add(dose);
            medicationStatement.setDosage(doseList);
        }
        return medicationStatement;
    }


    private Medication getOutpatientStatements (JsonResult input) {
        JSONObject outPatientJson = input.read("$.Patrx");
        if (outPatientJson != null) {
            Medication med = new Medication();

            med.setResourceId(outPatientJson.getString("resourceId"));
            med.setPatientName(outPatientJson.getString("patient"));
            med.setPatientIcn(outPatientJson.getString("patientICN"));
            med.setStatus(outPatientJson.getString("status"));

            Optional<Date> start = InputValidator.parseAnyDate(outPatientJson.getString("dispensedDateFHIR"));
            if (start.isPresent()) {
                med.setStartDate(start.get());
            }
            med.setDrugName(outPatientJson.getString("drug"));
            med.setRxNorm(outPatientJson.getString("rxnorm"));

            // Dosage
            JSONObject medicationInstructions = outPatientJson.optJSONObject("medicationInstructionss");
            if (medicationInstructions != null) {
                JSONArray instructions = medicationInstructions.optJSONArray("medicationInstructions");
                if (instructions != null) {
                    JSONObject instruction = instructions.getJSONObject(0);
                    med.setRoute(instruction.getString("route"));
                    med.setDosageUnits(instruction.getString("units"));
                    Double dosage = instruction.optDouble("dosageOrdered", HcConstants.MISSING_ID);
                    if (dosage.longValue() != HcConstants.MISSING_ID) {
                        med.setDosage(dosage);
                    }
                }
            }

            Long supply = outPatientJson.optLong("daysSupply", HcConstants.MISSING_ID);
            if (supply.longValue() != HcConstants.MISSING_ID) {
                med.setDaysSupply(supply);
            }

            return med;
        } else {
            return null;
        }
    }

    private List<Medication> getInpatientStatements (JsonResult input) {
        List<Medication> results = new ArrayList<>();

        JSONObject inPatientJson = input.read("$.Phpat");
        if (inPatientJson != null) {
            String name = inPatientJson.getString("name");
            String icn = inPatientJson.getString("nameICN");

            JSONObject unitDoseHolder = inPatientJson.optJSONObject("unitDoses");
            if (unitDoseHolder != null) {
                JSONArray unitDoses = unitDoseHolder.optJSONArray("unitDose");
                if (unitDoses != null) {
                    for (int i = 0; i < unitDoses.length(); i++) {
                        JSONObject json = unitDoses.getJSONObject(i);
                        Medication med = new Medication();
                        med.setPatientName(name);
                        med.setPatientIcn(icn);
                        med.setResourceId(json.getString("resourceId"));
                        med.setStatus(json.getString("status"));
                        Optional<Date> start = InputValidator.parseAnyDate(json.getString("startDateTimeFHIR"));
                        if (start.isPresent()) {
                            med.setStartDate(start.get());
                        }

                        JSONObject drugListHolder = json.optJSONObject("dispenseDrugs");
                        if (drugListHolder != null) {
                            JSONArray drugList = drugListHolder.optJSONArray("dispenseDrug");
                            if (drugList != null) {
                                JSONObject drug = drugList.getJSONObject(0);
                                med.setDrugName(drug.getString("dispenseDrugName"));
                                med.setRxNorm(drug.getString("dispenseDrugRxNorm"));
                            }
                        } else {
                            med.setDrugName(json.getString("orderableItem"));
                        }
                        med.setRoute(json.getString("medRoute"));
                        String combinedDosage = json.getString("dosageOrdered");
                        if (combinedDosage.contains("MG")) {
                            med.setDosageUnits("MG");
                            String[] parts = combinedDosage.split("MG");
                            Double doseAmount = Double.parseDouble(parts[0]);
                            med.setDosage(doseAmount);
                        }
                        results.add(med);
                    }
                }
            }

            JSONObject bcmaHolder = inPatientJson.optJSONObject("bcmaIds");
            if (bcmaHolder != null) {
                results.addAll(processSolutions(bcmaHolder, "bcmaId", "labelDateFHIR", name, icn));
            }
            JSONObject ivHolder = inPatientJson.optJSONObject("ivs");
            if (ivHolder != null) {
                results.addAll(processSolutions(ivHolder, "iv", "startDateTimeFHIR", name, icn));
            }

            JSONObject profileHolder = inPatientJson.optJSONObject("prescriptionProfiles");
            if (profileHolder != null) {
                JSONArray profiles = profileHolder.optJSONArray("prescriptionProfile");
                if (profiles != null) {
                    for (int i = 0; i < profiles.length(); i++) {
                        JSONObject json = profiles.getJSONObject(i);
                        Medication med = new Medication();
                        med.setPatientName(name);
                        med.setPatientIcn(icn);
                        med.setResourceId(json.getString("resourceId"));
                        med.setDrugName(json.getString("drugC"));
                        med.setStatus(json.getString("statusC"));
                        results.add(med);
                    }
                }
            }
        }

        return results;
    }

    private static List<Medication> processSolutions(JSONObject holder, String selector, String dateSelector, String name, String icn) {
        List<Medication> results = new ArrayList<>();
        JSONArray iv = holder.optJSONArray(selector);
        if (iv != null) {
            for (int i = 0; i < iv.length(); i++) {
                JSONObject json = iv.getJSONObject(i);
                Medication med = new Medication();
                med.setPatientName(name);
                med.setPatientIcn(icn);
                med.setResourceId(json.getString("resourceId"));
                med.setStatus(json.optString("status"));
                Optional<Date> start = InputValidator.parseAnyDate(json.getString(dateSelector));
                if (start.isPresent()) {
                    med.setStartDate(start.get());
                }

                JSONObject solutionHolder = json.optJSONObject("additives");
                if (solutionHolder != null) {
                    JSONArray drugList = solutionHolder.optJSONArray("additive");
                    if (drugList != null) {
                        JSONObject drug = drugList.getJSONObject(0);
                        med.setDrugName(drug.getString("additive"));
                        med.setRxNorm(drug.getString("additiveRxNorm"));
                        String combinedDosage = drug.optString("strength");
                        if (!StringUtils.isEmpty(combinedDosage)) {
                            String[] parts = combinedDosage.split(" ");
                            if (parts.length > 1) {
                                med.setDosageUnits(parts[1]);
                            }
                            Double doseAmount = Double.parseDouble(parts[0]);
                            med.setDosage(doseAmount);
                        }
                    }
                    results.add(med);
                } else {
                    // this may be a solution without additives
                    solutionHolder = json.optJSONObject("solutions");
                    if (solutionHolder != null) {
                        JSONArray solutionList = solutionHolder.optJSONArray("solution");
                        JSONObject solution = solutionList.getJSONObject(0);
                        med.setDrugName(solution.getString("solution"));
                        med.setRxNorm(solution.getString("solutionRxNorm"));
                        String combinedVolume = solution.optString("volume");
                        if (!StringUtils.isEmpty(combinedVolume)) {
                            String[] parts = combinedVolume.split(" ");
                            if (parts.length > 1) {
                                med.setDosageUnits(parts[1]);
                            }
                            Double doseAmount = Double.parseDouble(parts[0]);
                            med.setDosage(doseAmount);
                        }
                    }
                    results.add(med);
                }
            }
        }
        return results;
    }

    private static MedicationDispense convertToDispense(Medication med) {
        MedicationDispense medicationDispense = new MedicationDispense();
        medicationDispense.setMeta(ResourceHelper.getVistaMeta());
        medicationDispense.setId(med.getResourceId());
        Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, med.getPatientIcn(), med.getPatientName(), ResourceHelper.ReferenceType.Patient);
        medicationDispense.setSubject(patient);
        MedicationDispense.MedicationDispenseStatus status = getDispenseStatus(med.getStatus());
        if(status != MedicationDispense.MedicationDispenseStatus.NULL) {
            medicationDispense.setStatus(status);
        }

        if (med.getStartDate() != null) {
            medicationDispense.setWhenHandedOver(med.getStartDate());
        }

        if (med.getRxNorm() != null && !med.getRxNorm().equals("code not mapped")) {
            medicationDispense.setMedication(ResourceHelper.createCodeableConcept(HcConstants.RX_NORM, med.getRxNorm(), med.getDrugName()));
        } else {
            Reference reference = new Reference();
            reference.setDisplay(med.getDrugName());
            medicationDispense.setMedication(reference);
        }

        if (med.getDaysSupply() != null) {
            SimpleQuantity amount = new SimpleQuantity();
            amount.setValue(med.getDaysSupply());
            medicationDispense.setDaysSupply(amount);
        }

        if (med.getDosage() != null && med.getDosage() != 0) {
            Dosage dose = new Dosage();
            SimpleQuantity doseAmount = new SimpleQuantity();
            doseAmount.setValue(med.getDosage());
            doseAmount.setCode(med.getDosageUnits());
            dose.setDose(doseAmount);
            dose.setText(med.getDrugName());
            if (!StringUtils.isEmpty(med.getRoute())) {
                Optional<CodeableConcept> route = getDoseRoute(med.getRoute());
                if (route.isPresent()) {
                    dose.setRoute(route.get());
                }
            }
            List<Dosage> doseList = new ArrayList<Dosage>();
            doseList.add(dose);
            medicationDispense.setDosageInstruction(doseList);
        }

        return medicationDispense;
    }

    public List<MedicationDispense> parseMedicationDispense(String httpData) {
        List<MedicationDispense> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return results;
        }

        JsonResult medicationJson = JsonPath.parseOrNull(httpData);
        if (medicationJson == null) {
            LOG.warn("Unable to parse Medication Dispense JSON");
            return results;
        }

        Medication outPatient = this.getOutpatientStatements(medicationJson);
        if (outPatient != null) {
            results.add(convertToDispense(outPatient));
        }

        List<Medication> inPatient = this.getInpatientStatements(medicationJson);
        for (Medication med : inPatient) {
            results.add(convertToDispense(med));
        }
        return results;
    }

    public List<MedicationAdministration> parseMedicationAdmin(String httpData) {
        List<MedicationAdministration> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return results;
        }

        JsonResult medicationJson = JsonPath.parseOrNull(httpData);
        if (medicationJson == null) {
            LOG.warn("Unable to parse Medication Statement JSON");
            return results;
        }

        Medication outPatient = this.getOutpatientStatements(medicationJson);
        if (outPatient != null) {
            results.add(convertToAdministration(outPatient));
        }

        List<Medication> inPatient = this.getInpatientStatements(medicationJson);
        for (Medication med : inPatient) {
            results.add(convertToAdministration(med));
        }
        return results;
    }

    private static MedicationAdministration convertToAdministration (Medication med) {
        MedicationAdministration medicationAdmin = new MedicationAdministration();
        medicationAdmin.setMeta(ResourceHelper.getVistaMeta());
        medicationAdmin.setId(med.getResourceId());
        Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, med.getPatientIcn(), med.getPatientName(), ResourceHelper.ReferenceType.Patient);
        medicationAdmin.setSubject(patient);
        MedicationAdministration.MedicationAdministrationStatus status = getStatus(med.getStatus());
        if(status != MedicationAdministration.MedicationAdministrationStatus.NULL) {
            medicationAdmin.setStatus(status);
        }
        if (med.getStartDate() != null) {
            medicationAdmin.setEffective(new DateTimeType(med.getStartDate()));
        }

        if (med.getRxNorm() != null && !med.getRxNorm().equals("code not mapped")) {
            medicationAdmin.setMedication(ResourceHelper.createCodeableConcept(HcConstants.RX_NORM, med.getRxNorm(), med.getDrugName()));
        } else {
            Reference reference = new Reference();
            reference.setDisplay(med.getDrugName());
            medicationAdmin.setMedication(reference);
        }

        if (med.getDosage() != null && med.getDosage() != 0) {
            MedicationAdministration.MedicationAdministrationDosageComponent dosage = new MedicationAdministration.MedicationAdministrationDosageComponent();
            SimpleQuantity doseAmount = new SimpleQuantity();
            doseAmount.setValue(med.getDosage());
            doseAmount.setCode(med.getDosageUnits());
            dosage.setDose(doseAmount);
            dosage.setText(med.getDrugName());
            if (!StringUtils.isEmpty(med.getRoute())) {
                Optional<CodeableConcept> route = getDoseRoute(med.getRoute());
                if (route.isPresent()) {
                    dosage.setRoute(route.get());
                }
            }
            medicationAdmin.setDosage(dosage);
        }

        return medicationAdmin;
    }


    private static Optional<CodeableConcept> getDoseRoute(String routeName) {
        switch (routeName.toUpperCase()) {
            case "ORAL (BY MOUTH)":
                return Optional.of(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "26643006", "Oral route"));
            case "INTRAVENOUS":
            case "IV PIGGYBACK":
                return Optional.of(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "47625008", "Intravenous route"));
            case "NASAL":
                return Optional.of(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "46713006", "Nasal route"));
            case "TOPICAL":
                return Optional.of(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "6064005", "Topical route"));
            default:
                return Optional.empty();
        }
    }

    private static MedicationAdministration.MedicationAdministrationStatus getStatus(String data) {
        switch (data.toUpperCase()) {
            case ACTIVE:
                return MedicationAdministration.MedicationAdministrationStatus.INPROGRESS;
            case EXPIRED:
                return MedicationAdministration.MedicationAdministrationStatus.COMPLETED;
            case DISCONTINUED:
                return MedicationAdministration.MedicationAdministrationStatus.STOPPED;
            default:
                return MedicationAdministration.MedicationAdministrationStatus.UNKNOWN;
        }
    }

    private static MedicationDispense.MedicationDispenseStatus getDispenseStatus(String data) {
        switch (data.toUpperCase()) {
            case ACTIVE:
                return MedicationDispense.MedicationDispenseStatus.INPROGRESS;
            case EXPIRED:
                return MedicationDispense.MedicationDispenseStatus.COMPLETED;
            case DISCONTINUED:
                return MedicationDispense.MedicationDispenseStatus.STOPPED;
            default:
                return MedicationDispense.MedicationDispenseStatus.NULL;
        }
    }

    private static MedicationStatement.MedicationStatementStatus getStatementStatus(String data) {
        if (StringUtils.isEmpty(data)) {
            return MedicationStatement.MedicationStatementStatus.NULL;
        }

        switch (data.toUpperCase()) {
            case ACTIVE:
                return MedicationStatement.MedicationStatementStatus.ACTIVE;
            case EXPIRED:
                return MedicationStatement.MedicationStatementStatus.COMPLETED;
            case DISCONTINUED:
                return MedicationStatement.MedicationStatementStatus.STOPPED;
            default:
                return MedicationStatement.MedicationStatementStatus.NULL;
        }
    }
}
