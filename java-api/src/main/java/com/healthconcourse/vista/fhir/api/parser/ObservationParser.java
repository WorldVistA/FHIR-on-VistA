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
import org.hl7.fhir.dstu3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ObservationParser {

    private static final Logger LOG = LoggerFactory.getLogger(ObservationParser.class);
    public List<Observation> parseVitalsList(String httpData) {

        List<Observation> result = new ArrayList<>();

        if(StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] parts = httpData.split("\\^");

        String snomedCode;
        String rawData;

        if(parts.length > 1) {
            String icn = parts[0].trim();

            for(int i = 1; i < parts.length; i++) {

                String[] observationParts = parts[i].split("\\|");

                snomedCode = observationParts[0];
                rawData = observationParts[2];

                CodeableConcept concept = ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, snomedCode, observationParts[1]);

                Observation observation = new Observation();
                observation.setId(observationParts[4].replaceAll("(\\r|\\n)", ""));
                observation.setStatus(Observation.ObservationStatus.FINAL);
                observation.setCode(concept);

                Optional<Date> observationDate = InputValidator.parseAnyDate(observationParts[3]);
                if(observationDate.isPresent()) {
                    observation.setIssued(observationDate.get());
                    DateTimeType effective = new DateTimeType(observationDate.get());
                    observation.setEffective(effective);
                }

                observation.setValue(new StringType(rawData));

                Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, "", ResourceHelper.ReferenceType.Patient);

                observation.setSubject(patient);

                observation.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.OBSERVATION_CODING_SYSTEM, "vital-signs", "Vital Signs"));

                observation.setMeta(ResourceHelper.getVistaMeta());

                result.add(observation);
            }

        } else {
            LOG.error("Bad observation data received from Vista");
        }

        return result;
    }

    public List<Observation> parseLabsList(String httpData) {

        List<Observation> result = new ArrayList<>();

        String[] records = httpData.split("\\^");

        String patientId = records[0];

        for(int i = 1; i < records.length; i++) {
            Observation observation = new Observation();

            observation.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, patientId, "", ResourceHelper.ReferenceType.Patient));

            String[] fields = records[i].split("\\|");

            String id = fields[4].replaceAll("(\\r|\\n)", "");

            observation.setId(id);
            observation.setStatus(Observation.ObservationStatus.FINAL);
            Optional<Date> observationDate = InputValidator.parseAnyDate(fields[3]);

            if(observationDate.isPresent()) {
                observation.setIssued(observationDate.get());
                DateTimeType effective = new DateTimeType(observationDate.get());
                observation.setEffective(effective);
            }

            observation.setValue(new StringType(fields[2]));

            String[] codeParts = fields[1].split(":");

            observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.LOINC, fields[0], codeParts[0]));

            observation.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.OBSERVATION_CODING_SYSTEM, "laboratory", "Laboratory"));

            observation.setMeta(ResourceHelper.getVistaMeta());

            result.add(observation);
        }

        return result;
    }

    public List<Observation> parseHealthFactorsList(String httpData) {

        List<Observation> result = new ArrayList<>();

         String[] records = httpData.split("\\^");

        String patientId = records[0];


        for(int i = 1; i < records.length; i++) {
            Observation observation = new Observation();

            observation.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, patientId, "", ResourceHelper.ReferenceType.Patient));

            String[] fields = records[i].split("\\|");

            //SCT code|SCT Preferred Name|Resource ID|Severity|Provider|
            String snomedCode = fields[0];
            String snomedDesc = fields[1];
            String id = fields[2];
            String severity = fields[3]; // interpretation? maybe Value
            String provider = "";
            if (fields.length > 4) {
                provider = fields[4].replaceAll("(\\r|\\n)", ""); // performer (reference)
            }

            // Id and required status
            observation.setId(id);
            observation.setStatus(Observation.ObservationStatus.FINAL);

            // snomed codes
            observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, snomedCode, snomedDesc));

            // category
            observation.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.OBSERVATION_CODING_SYSTEM, "social-history", "Social History"));

            // severity
            observation.setValue(new StringType(severity));

            // provider
            List<Reference> thePerformer = new ArrayList<>();
            Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, provider, provider, ResourceHelper.ReferenceType.Practitioner);
            thePerformer.add(ref);

            observation.setPerformer(thePerformer); //mec... TODO: look up provider? getProvider(provider));

            observation.setMeta(ResourceHelper.getVistaMeta());

            if(fields.length > 5) {
                Optional<Date> effective = InputValidator.parseAnyDate(fields[5]);
                if(effective.isPresent()) {
                    DateTimeType effectiveDate = new DateTimeType(effective.get());
                    observation.setEffective(effectiveDate);
                }
            }
            result.add(observation);
        }

        return result;
    }

    public List<Observation> parseMentalHealthList(String httpData) {
        List<Observation> result = new ArrayList<>();

        String[] records = httpData.split("\\^");

        String patientId = records[0];

        for(int i = 1; i < records.length; i++) {
            Observation observation = new Observation();

            observation.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, patientId, "", ResourceHelper.ReferenceType.Patient));

            observation.setStatus(Observation.ObservationStatus.FINAL);
            observation.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.OBSERVATION_CODING_SYSTEM, "survey", "Survey"));

            String[] fields = records[i].split("\\|");

            observation.setId(fields[9]);
            // SNOMED code
            observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, fields[0], fields[1]));
            // LOINC code
            observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.LOINC, fields[2], fields[3]));

            Optional<Date> observationDate = InputValidator.parseAnyDate(fields[5]);
            if (observationDate.isPresent()) {
                observation.setIssued(observationDate.get());
                DateTimeType effective = new DateTimeType(observationDate.get());
                observation.setEffective(effective);
            }

            // provider and location
            List<Reference> orderedBy = new ArrayList<>();
            Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, fields[7], fields[7], ResourceHelper.ReferenceType.Practitioner);
            orderedBy.add(ref);
            if (!StringUtils.isEmpty(fields[6])) {
                ref = ResourceHelper.createReference(HcConstants.URN_VISTA_LOCATION, fields[6], fields[6], ResourceHelper.ReferenceType.Location);
                orderedBy.add(ref);
            }
            observation.setPerformer(orderedBy);

            observation.setMeta(ResourceHelper.getVistaMeta());

            String[] quantities = fields[8].split("~");

            // TODO: Handle more than one quantities entry?? Still unclear - DLT
            String[] observationParts = quantities[0].split(":");

            if(observationParts.length == 2) {
                Quantity value = new Quantity();
                value.setUnit(observationParts[0]);
                value.setValue(Long.parseLong(observationParts[1]));
                observation.setValue(value);
            }
            result.add(observation);
        }
        return result;
    }

}
