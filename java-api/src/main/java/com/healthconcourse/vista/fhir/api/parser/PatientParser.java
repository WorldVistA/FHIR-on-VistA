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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PatientParser  {

    private static final Logger LOG = LoggerFactory.getLogger(PatientParser.class);

    public Optional<Patient> parseSingle(String httpData) {
        if(StringUtils.isEmpty(httpData)) {
            return Optional.empty();
        }

        JsonResult json = JsonPath.parseOrNull(httpData);
        if (json  == null) {
            LOG.error("Unable to parse Patient JSON");
            return Optional.empty();
        }

        JSONObject raw = json.read("$.Patients[0].Patient");

        Patient patient = new Patient();
        patient.setMeta(ResourceHelper.getVistaMeta());

        String icn = raw.getString("fullICN");
        patient.setId(icn); // Shouldn't this really be resourceId? The old text way uses ICN, but that's inconsistent

        patient.addIdentifier();
        patient.getIdentifier().get(0).setSystem(HcConstants.URN_VISTA_ICN);
        patient.getIdentifier().get(0).setValue(icn);

        Coding code = new Coding();
        code.setCode(HcConstants.SSN_CODING);
        code.setSystem(HcConstants.SSN_CODING_SYSTEM);

        CodeableConcept concept = new CodeableConcept();
        concept.addCoding(code);
        Identifier ssn = new Identifier();
        ssn.setType(concept);
        Long condensedSSN = raw.getLong("socialSecurityNumber");
        ssn.setValue(condensedSSN.toString());
        patient.addIdentifier(ssn);

        String gender = raw.getString("sex");
        patient.setGender(InputValidator.parseGender(gender));

        String name = raw.getString("name");
        String[] nameParts = name.split(",");
        patient.getName().add(new HumanName().setFamily(nameParts[0]));
        patient.getName().get(0).addGiven(nameParts[1]);

        String dob = raw.getString("dateOfBirthFHIR");
        Optional<Date> birthDate = InputValidator.parseAnyDate(dob);
        if (birthDate.isPresent()) {
            patient.setBirthDate(birthDate.get());
        }

        String vistaMarital = raw.getString("maritalStatus");
        if (!StringUtils.isEmpty(vistaMarital)) {
            patient.setMaritalStatus(getMaritalStatus(vistaMarital));
        }

        Address address = new Address();
        String streetAddressLine1 = raw.getString("streetAddressLine1");
        if (!StringUtils.isEmpty(streetAddressLine1)) {
            address.addLine(streetAddressLine1);
        }
        String streetAddressLine2 = raw.getString("streetAddressLine2");
        if (!StringUtils.isEmpty(streetAddressLine2)) {
            address.addLine(streetAddressLine2);
        }
        String streetAddressLine3 = raw.getString("streetAddressLine3");
        if (!StringUtils.isEmpty(streetAddressLine3)) {
            address.addLine(streetAddressLine3);
        }
        String city = raw.getString("city");
        if (!StringUtils.isEmpty(city)) {
            address.setCity(city);
        }
        String state = raw.getString("state");
        if (!StringUtils.isEmpty(state)) {
            address.setState(state);
        }
        Long zip = raw.optLong("zipCode", HcConstants.MISSING_ID);
        if (zip.longValue() != HcConstants.MISSING_ID) {
            address.setPostalCode(zip.toString());
        }

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        patient.setAddress(addressList);

        return Optional.of(patient);
    }

    public Optional<Patient> parseSingleOld(String httpData) {

        if(StringUtils.isEmpty(httpData)) {
            return Optional.empty();
        }

        String[] fields = httpData.split("\\^");

        if(fields.length < 6) {
            LOG.error("Bad data received from Vista");
            return Optional.empty();
        }

        Coding code = new Coding();
        code.setCode(HcConstants.SSN_CODING);
        code.setSystem(HcConstants.SSN_CODING_SYSTEM);

        CodeableConcept concept = new CodeableConcept();
        concept.addCoding(code);

        Patient result = new Patient();

        result.setId(createPatientId(fields[0], HcConstants.VERSION));

        String[] nameParts = fields[1].split(",");
        result.getName().add(new HumanName().setFamily(nameParts[0]));
        result.getName().get(0).addGiven(nameParts[1]);

        result.addIdentifier();
        result.getIdentifier().get(0).setSystem(HcConstants.URN_VISTA_ICN);
        result.getIdentifier().get(0).setValue(fields[0]);

        Identifier ssn = new Identifier();
        ssn.setType(concept);
        ssn.setValue(fields[2]);

        result.addIdentifier(ssn);

        result.setGender(InputValidator.parseGender(fields[3]));

        Optional<Date> birthDate = InputValidator.parseAnyDate(fields[4]);
        if (birthDate.isPresent()) {
            result.setBirthDate(birthDate.get());
        }

        Address address = new Address();

        String[] streetParts = fields[5].split(",");

        for(String line : streetParts) {
            address.addLine(line);
        }

        if(fields.length > 6) {
            address.setCity(fields[6]);
        }

        if(fields.length > 7) {
            address.setState(fields[7]);
        }

        if(fields.length > 8) {
            address.setPostalCode(fields[8]);
        }

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);

        result.setAddress(addressList);
        result.setMeta(ResourceHelper.getVistaMeta());

        return Optional.of(result);
    }

    public List<Patient> parseList(String httpData) {
        List<Patient> result = new ArrayList<>();

        if(!httpData.isEmpty()) {
            String[] records = httpData.split("\\|");
            for(int i = 0; i < records.length; i++) {

                Optional<Patient> patient = parseSingleOld(records[i]);

                if(patient.isPresent()) {
                    result.add(patient.get());
                } else {
                    LOG.info(records[i]);
                }
            }

        }

        return result;
    }

    private static IdType createPatientId(final String id, final Long theVersionId) {
        return new IdType("Patient", id, "" + theVersionId);
    }

    public List<Patient> parseCodeList(String httpData) {
        String[] parts = httpData.split("\\^");
        List<Patient> result = new ArrayList<>();

        for(int i = 1; i < parts.length; i++) {

            String[] patientParts = parts[i].split("\\|");
            String id = patientParts[0];
            String name =patientParts[1];

            Patient patient = new Patient();

            patient.setId(createPatientId(id, HcConstants.VERSION));
            patient.addIdentifier();
            patient.getIdentifier().get(0).setSystem(HcConstants.URN_VISTA_ICN);
            patient.getIdentifier().get(0).setValue(id);

            String[] nameParts = name.split(",");
            patient.getName().add(new HumanName().setFamily(nameParts[0]));
            patient.getName().get(0).addGiven(nameParts[1]);

            patient.setMeta(ResourceHelper.getVistaMeta());

            result.add(patient);
        }

        return result;
    }

    private static CodeableConcept getMaritalStatus(String vistaStatus) {
        switch (vistaStatus.toUpperCase()) {
            case "MARRIED":
                return ResourceHelper.createCodeableConcept(HcConstants.MARITAL_STATUS_CODING_SYSTEM, "M", "Married");
            case "DIVORCED":
                return ResourceHelper.createCodeableConcept(HcConstants.MARITAL_STATUS_CODING_SYSTEM, "D", "Divorced");
            case "NEVER MARRIED":
                return ResourceHelper.createCodeableConcept(HcConstants.MARITAL_STATUS_CODING_SYSTEM, "S", "Never Married");
            case "Separated":
                return ResourceHelper.createCodeableConcept(HcConstants.MARITAL_STATUS_CODING_SYSTEM, "L", "Legally Separated");
            case "WIDOWED":
                return ResourceHelper.createCodeableConcept(HcConstants.MARITAL_STATUS_CODING_SYSTEM, "W", "Widowed");
            case "UNKNOWN":
            default:
                return ResourceHelper.createCodeableConcept(HcConstants.HL7_NULL_CODE, "UNK", "Unknown");
        }
    }
}
