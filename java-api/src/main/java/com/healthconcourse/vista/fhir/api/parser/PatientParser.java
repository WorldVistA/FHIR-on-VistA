/* Created by Perspecta http://www.perspecta.com */
/*
(c) 2017-2019 Perspecta
(c) 2019 OSEHRA

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
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.Patient.PatientCommunicationComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PatientParser  {

    private static final Logger LOG = LoggerFactory.getLogger(PatientParser.class);
    //TODO: This doesn't work yet
    //@Value("${fhir.use_us_core_ig}") private String USE_US_CORE_IG;

    public Optional<Patient> parseSingle(String httpData) {

        if(StringUtils.isEmpty(httpData)) {
            return Optional.empty();
        }

        String[] fields = httpData.trim().split("\\^");

        if(fields.length < 6) {
            LOG.error("Bad data received from Vista");
            return Optional.empty();
        }

        Patient result = new Patient();

        result.setId(createPatientId(fields[0], HcConstants.VERSION));

        String[] nameParts = fields[1].split(",");
        result.getName().add(new HumanName().setFamily(nameParts[0]));
        result.getName().get(0).addGiven(nameParts[1]);

        result.addIdentifier();
        result.getIdentifier().get(0).setSystem(HcConstants.URN_VISTA_ICN);
        result.getIdentifier().get(0).setValue(fields[0]);

        // Reuse ICN for MRN (as VistA has no true MRN & we don't want to use SSN)
        Coding code = new Coding();
        code.setCode(HcConstants.MRN_CODING);
        code.setSystem(HcConstants.MRN_CODING_SYSTEM);
        code.setDisplay(HcConstants.MRN_CODING_SYSTEM_DESC);
        CodeableConcept concept = new CodeableConcept();
        concept.addCoding(code);
        Identifier MRN = new Identifier();
        MRN.setSystem(HcConstants.MRN_CODING_SYSTEM);
        MRN.setType(concept);
        MRN.setValue(fields[0]);
        result.addIdentifier(MRN);

        // Self-identified gender
        // TODO: In the future, code unknown properly.
        if(fields.length > 10 && !fields[10].isEmpty())
        {
            try {
                result.setGender(InputValidator.parseGender(fields[10]));
            }
            catch (Exception ex) {
                result.setGender(InputValidator.parseGender(fields[3]));
            }
        }
        else
        {
            result.setGender(InputValidator.parseGender(fields[3]));
        }

        //LOG.debug("US CORE IG:" + USE_US_CORE_IG)
        Extension birthSexExtension = new Extension(
                "http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex");
        birthSexExtension.setValue(new CodeType(InputValidator.parseGender(fields[3]).toCode().substring(0,1).toUpperCase()));
        result.addExtension(birthSexExtension);

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

        // Telephone
        result.addTelecom().setSystem(ContactPointSystem.PHONE)
        .setUse(ContactPointUse.HOME)
        .setValue(fields[2]);

        // Email
        if (fields.length > 11 && !fields[11].isEmpty())
        {
            result.addTelecom().setSystem(ContactPointSystem.EMAIL)
            .setUse(ContactPointUse.HOME)
            .setValue(fields[11]);
        }

        // Language
        List<PatientCommunicationComponent> communication =
                new ArrayList<PatientCommunicationComponent>();
        if (fields.length > 12 && !fields[12].split(",",3)[0].isEmpty())
        {
            String[] langParts = fields[12].split(",",3);
            communication.add(new PatientCommunicationComponent(
                    ResourceHelper.createCodeableConcept(langParts[2],
                            langParts[1], langParts[0]))
                    .setPreferred(true));
        }
        else
        {
            communication.add(new PatientCommunicationComponent(
                    ResourceHelper.createCodeableConcept(HcConstants.UNK_CODING_SYSTEM, HcConstants.UNK_CODING, HcConstants.UNK_CODING_SYSTEM_DESC)
                    ));
        }
        result.setCommunication(communication);

        //Race Extension
        Extension raceExtension = new Extension(
                "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race");
        Extension raceCodingExtension = new Extension("ombCategory");
        Coding raceCoding = new Coding();
        if (fields.length > 13 && !fields[13].split(",",3)[0].isEmpty())
        {
            String[] raceParts = fields[13].split(",",3);
            String raceDisplay = raceParts[0];
            String raceHL7Code = raceParts[1];
            String raceSystem  = raceParts[2];
            raceCoding.setDisplay(raceDisplay);
            raceCoding.setCode(raceHL7Code);
            raceCoding.setSystem(raceSystem);
        }
        else
        {
            raceCoding.setDisplay(HcConstants.UNK_CODING_SYSTEM_DESC);
            raceCoding.setCode(HcConstants.UNK_CODING);
            raceCoding.setSystem(HcConstants.UNK_CODING_SYSTEM);
        }
        raceCodingExtension.setValue(raceCoding);
        raceExtension.addExtension(raceCodingExtension);
        Extension raceTextExtension = new Extension("text");
        raceTextExtension.setValue(new StringType(raceCoding.getDisplay()));
        raceExtension.addExtension(raceTextExtension);
        result.addExtension(raceExtension);

        //Ethnicity Extension
        Extension ethnicityExtension = new Extension(
                "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity");
        Extension ethnicityCodingExtension = new Extension("ombCategory");
        Coding ethnicityCoding = new Coding();
        if (fields.length > 14 && !fields[14].split(",",3)[0].isEmpty())
        {
            String[] ethParts = fields[14].split(",",3);
            String ethDisplay = ethParts[0];
            String ethHL7Code = ethParts[1];
            String ethSystem  = ethParts[2];
            ethnicityCoding.setDisplay(ethDisplay);
            ethnicityCoding.setCode(ethHL7Code);
            ethnicityCoding.setSystem(ethSystem);
        }
        else
        {
            ethnicityCoding.setDisplay(HcConstants.UNK_CODING_SYSTEM_DESC);
            ethnicityCoding.setCode(HcConstants.UNK_CODING);
            ethnicityCoding.setSystem(HcConstants.UNK_CODING_SYSTEM);
        }
        ethnicityCodingExtension.setValue(ethnicityCoding);
        ethnicityExtension.addExtension(ethnicityCodingExtension);
        Extension ethnicityTextExtension = new Extension("text");
        ethnicityTextExtension.setValue(new StringType(ethnicityCoding.getDisplay()));
        ethnicityExtension.addExtension(ethnicityTextExtension);
        result.addExtension(ethnicityExtension);

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
