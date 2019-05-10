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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

                Optional<Patient> patient = parseSingle(records[i]);

                if(patient.isPresent()) {
                    result.add(patient.get());
                } else {
                    LOG.info(records[i]);
                }
            }

        }

        return result;
    }

    public List<Patient> parseListFromSinglePatient(String httpData) {
        List<Patient> result = new ArrayList<>();
        //VistA really never returns more than one patient at this time
        try {
            Optional<Patient> patient = this.parseSingle(httpData);

            if (patient.isPresent()) {
                result.add(patient.get());
            }
        }
        catch(IllegalArgumentException e) {
            LOG.error("Unable to parse patient  data", e);
            LOG.info("Bad data: " + httpData);
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
}
