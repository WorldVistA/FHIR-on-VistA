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
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Dosage.DosageDoseAndRateComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MedicationParser {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationParser.class);
    private static final String ACTIVE = "ACTIVE";
    private static final String EXPIRED = "EXPIRED";
    private static final String DISCONTINUED = "DISCONTINUED BY PROVIDER";

    private String mPatientId;

    public List<MedicationStatement> parseMedicationStatement(String httpData) {

        List<MedicationStatement> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return results;
        }

        String[] records = httpData.trim().split("\\|");
        boolean isFirst = true;

        for(String record : records) {
            Optional<MedicationStatement> med = parseMedicationStatementRecord(record, isFirst);
            med.ifPresent(results::add);
            isFirst = false;
        }

        return results;
    }


    public List<MedicationDispense> parseMedicationDispense(String httpData) {

        List<MedicationDispense> results = new ArrayList<>();

        String[] records = httpData.trim().split("\\|");
        boolean isFirst = true;

        for(String record : records) {
            Optional<MedicationDispense> med = parseMedicationDispenseRecord(record, isFirst);
            med.ifPresent(results::add);
            isFirst = false;
        }

        return results;
    }

    public List<MedicationAdministration> parseMedicationAdmin(String httpData) {

        LOG.debug(httpData);

        List<MedicationAdministration> results = new ArrayList<>();

        String[] records = httpData.trim().split("\\|");
        boolean isFirst = true;

        for(String record : records) {

            Optional<MedicationAdministration> med = parseMedicationAdminRecord(record, isFirst);
            med.ifPresent(results::add);
            isFirst = false;
        }

        return results;
    }

    private Optional<MedicationStatement> parseMedicationStatementRecord(String record, boolean isFirst) {

        MedicationStatement result = new MedicationStatement();

        String[] fields = record.split("\\^");

        if(fields.length < 5) {
            return Optional.empty();
        }

        int index = 0;

        if(isFirst) {
            mPatientId = fields[0];
            index = 1;
        }

        result.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, mPatientId, "", ResourceHelper.ReferenceType.Patient));

        result.setId(fields[0]);

        MedicationStatement.MedicationStatementStatus status = getStatementStatus(fields[index + 1]);

        if(status != MedicationStatement.MedicationStatementStatus.NULL) {
            result.setStatus(status);
        }

        Optional<Date> start = InputValidator.parseAnyDate(fields[index + 3]);
        if (start.isPresent()) {
            Period period = new Period();
            period.setStart(start.get());
            result.setEffective(period);
        }

        result.setMeta(ResourceHelper.getVistaMeta());

        result.setDosage(getDosages(fields[index + 5]));

        if(fields.length >= 10  && fields[index + 8].startsWith("RXN")) {
            String code = fields[index + 8].substring(3);
            result.setMedication(ResourceHelper.createCodeableConcept(HcConstants.RX_NORM, code, fields[index + 2]));
        } else {
            Reference reference = new Reference();
            reference.setDisplay(fields[index + 2]);
            result.setMedication(reference);
        }

        return Optional.of(result);
    }



    private Optional<MedicationDispense> parseMedicationDispenseRecord(String record, boolean isFirst) {

        MedicationDispense result = new MedicationDispense();

        String[] fields = record.split("\\^");

        if(fields.length < 5) {
            return Optional.empty();
        }

        int index = 0;

        if(isFirst) {
            mPatientId = fields[0];
            index = 1;
        }

        result.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, mPatientId, "", ResourceHelper.ReferenceType.Patient));
        result.setId(fields[0]);

        result.setStatus(fields[index + 1]);

        Reference reference = new Reference();
        reference.setDisplay(fields[index + 2]);
        result.setMedication(reference);

        result.setDosageInstruction(getDosages(fields[index + 5]));

        if(fields.length > (6 + index) && !fields[index + 6].isEmpty()) {

            try {
                SimpleQuantity simpleNumber = new SimpleQuantity();
                int quantity = Integer.parseInt(fields[index + 6]);
                simpleNumber.setValue(quantity);
                result.setQuantity(simpleNumber);
            } catch (Exception ex) {
                LOG.error("Error parsing numerical value (" + fields[index + 6] + "), continuing.");
            }
        }

        if(fields.length > (7 + index) && !fields[index + 7].isEmpty()) {

            try {
                SimpleQuantity simpleNumber = new SimpleQuantity();
                int quantity = Integer.parseInt(fields[index + 7]);
                simpleNumber.setValue(quantity);
                result.setDaysSupply(simpleNumber);
            } catch (Exception ex) {
                LOG.error("Error parsing numerical value (" + fields[index + 7] + "), continuing.");
            }
        }

        result.setMeta(ResourceHelper.getVistaMeta());

        return Optional.of(result);
    }

    private Optional<MedicationAdministration> parseMedicationAdminRecord(String record, boolean isFirst) {

        MedicationAdministration result = new MedicationAdministration();

        String[] fields = record.split("\\^");

        if(fields.length < 5) {
            return Optional.empty();
        }

        int index = 0;

        if(isFirst) {
            mPatientId = fields[0];
            index = 1;
        }

        result.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, mPatientId, "", ResourceHelper.ReferenceType.Patient));

        result.setId(fields[0]);

        result.setStatus(fields[index + 1]);

        Reference reference = new Reference();
        reference.setDisplay(fields[index + 2]);
        result.setMedication(reference);

        Optional<Date> effectivDate = InputValidator.parseAnyDate(fields[index + 3]);
        if (effectivDate.isPresent()) {
            result.setEffective(new DateTimeType());
        }

        String[] doseParts = fields[index +5].split(";");

        MedicationAdministration.MedicationAdministrationDosageComponent dosage = new MedicationAdministration.MedicationAdministrationDosageComponent();

        if(doseParts.length > 1 && !doseParts[1].isEmpty()) {
            Optional<CodeableConcept> route = getDoseRoute(doseParts[1]);
            if(route.isPresent()) {
                dosage.setRoute(route.get());
            }
        }

        if(doseParts.length > 2) {
            dosage.setText(doseParts[2]);
            dosage.setDose(getDoseQuantity(doseParts[2]));
        }

        result.setDosage(dosage);

        result.setMeta(ResourceHelper.getVistaMeta());

        return Optional.of(result);
    }

    private static DosageDoseAndRateComponent getDoseQuantityRate(String doseAmount) {

        DosageDoseAndRateComponent doseRate = new DosageDoseAndRateComponent();
        Quantity d = new Quantity();
        Quantity r = new Quantity();

        d.setUnit("mg");
        r.setUnit("day");
        r.setValue(1);

        if(doseAmount.contains("MG")){
            String[] parts = doseAmount.split("MG");
            try {
                Integer milligrams = Integer.parseInt(parts[0]);
                d.setValue(milligrams);
            } catch (NumberFormatException ex) {
                d.setValue(0);
            }

        } else {
            d.setValue(0);
        }
        doseRate.setDose(d);
        doseRate.setRate(r);

        return doseRate;
    }

    private static Quantity getDoseQuantity(String doseAmount) {
        Quantity d = new Quantity();
        d.setUnit("mg");

        if(doseAmount.contains("MG")){
            String[] parts = doseAmount.split("MG");
            try {
                Integer milligrams = Integer.parseInt(parts[0]);
                d.setValue(milligrams);
            } catch (NumberFormatException ex) {
                d.setValue(0);
            }

        } else {
            d.setValue(0);
        }
        return d;
    }

    private static List<Dosage> getDosages(String doseRawdata) {

        String[] doseParts = doseRawdata.split(";");

        List<Dosage> dosages = new ArrayList<>();

        Dosage dosage = new Dosage();

        if(doseParts.length > 1 && !doseParts[1].isEmpty()) {
            Optional<CodeableConcept> route = getDoseRoute(doseParts[1]);
            if (route.isPresent()) {
                dosage.setRoute(route.get());
            }
        }

        if(doseParts.length > 2) {
            //Seems hard-coded in data
            dosage.setText(doseParts[2]);
            dosage.addDoseAndRate(getDoseQuantityRate(doseParts[2]));
        }

        dosages.add(dosage);

        return dosages;
    }

    private static Optional<CodeableConcept> getDoseRoute(String routeName) {

        switch (routeName.toUpperCase()) {
            case "ORAL (BY MOUTH)":
                return Optional.of(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "26643006", "Oral route"));
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

    private static MedicationStatement.MedicationStatementStatus getStatementStatus(String data) {

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
