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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EncounterParser implements VistaParser<Encounter> {

    private static final Logger LOG = LoggerFactory.getLogger(EncounterParser.class);
    private String mPatientId;

    @Override
    public List<Encounter> parseList(String httpData) {

        List<Encounter> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return results;
        }

        String[] records = httpData.trim().split("\\|");
        boolean isFirst = true;
        for(String record : records) {

            Encounter encounter = parseEncounter(record, isFirst);
            if(encounter != null) {
                results.add(parseEncounter(record, isFirst));
            }
            isFirst = false;
        }

        return results;
    }

    private Encounter parseEncounter(final String record, boolean isFirst) {

        Encounter encounter = new Encounter();

        String[] fields = record.split("\\^");

        if (fields.length < 2) {
            return null;
        }

        int index = 0;

        if (isFirst) {
            mPatientId = fields[0];
            index = 1;
        }

        encounter.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, mPatientId, "", ResourceHelper.ReferenceType.Patient));

        encounter.setId(fields[index]);

        encounter.setStatus(Encounter.EncounterStatus.FINISHED);

        Optional<Date> encounterDate = InputValidator.parseAnyDate(fields[index + 2]);
        if (encounterDate.isPresent()) {
            Period period = new Period();
            period.setStart(encounterDate.get());
            encounter.setPeriod(period);
        }

        setReason(fields[index + 3], encounter);

        setDiagnosis(fields[index + 4], encounter);

        setHospitalization(encounter, fields[index + 5]);

        setHospitalizationDischarge(encounter, fields[index + 6]);

        setAddress(encounter, fields[index + 7]);

        // some cases we don't have a complete record
        if (fields.length > (index + 8)) {
            setProvider(encounter, fields[index + 8]);
        }

        encounter.setMeta(ResourceHelper.getVistaMeta());

        return encounter;
    }

    private static void setReason(final String rawData, Encounter encounter) {

        if(!rawData.isEmpty()) {
            encounter.setReason(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.URN_VISTA_ENCOUNTER_REASON, "UNKNOWN", rawData));
        }
    }

    private static void setDiagnosis(final String rawData, Encounter encounter) {
        if(!rawData.isEmpty()) {
            String[] diagnosisParts = rawData.split(";");
            if(diagnosisParts.length == 2) {

                List<Encounter.DiagnosisComponent> diagnoses = new ArrayList<>();

                Reference code = ResourceHelper.createReference(HcConstants.ICD_9, diagnosisParts[0], diagnosisParts[1]);

                Encounter.DiagnosisComponent diagnosis = new Encounter.DiagnosisComponent(code);


                diagnoses.add(diagnosis);

                encounter.setDiagnosis(diagnoses);
            }
        }
    }
    private static void setProvider(Encounter encounter, final String rawData) {
        if(!rawData.isEmpty()) {
            Reference provider = ResourceHelper.createReference(HcConstants.URN_VISTA_SERVICE_PROVIDER, rawData, rawData);
            encounter.setServiceProvider(provider);
        }
    }

    private static void setAddress(Encounter encounter, final String rawData) {
        if(!rawData.isEmpty()) {

            String[] addressItems = rawData.split(";");

            Encounter.EncounterLocationComponent locationComponent = encounter.addLocation();

            String[] nameSplit = addressItems[0].split(",");

            StringBuilder addr = new StringBuilder();
            try {
                addr.append(nameSplit[0]);
                addr.append(String.format(", %s, %s, ", nameSplit[1], addressItems[1]));
                addr.append(addressItems[2]);
                addr.append(String.format(", %s", addressItems[3]));
            } catch (Exception ex) {
                LOG.error("Error with address (" + rawData + "), continuing.");
            }

            try {
                locationComponent.setLocation(ResourceHelper.createReference("", nameSplit[0], addr.toString()));
            } catch (Exception ex) {
                LOG.error("Error with address, nameSplit not split (" + rawData + "), continuing.");
            }
            locationComponent.setStatus(Encounter.EncounterLocationStatus.ACTIVE);
        }
    }

    private static void setHospitalizationDischarge(Encounter encounter, final String rawData) {
        if(!rawData.isEmpty()) {
            Encounter.EncounterHospitalizationComponent hospital = encounter.getHospitalization();

            if(hospital == null) {
                hospital = new Encounter.EncounterHospitalizationComponent();
                encounter.setHospitalization(hospital);
            }

            hospital.setDischargeDisposition(ResourceHelper.createCodeableConcept(HcConstants.URN_VISTA_DISCHARGE, rawData, rawData));
        }
    }

    private static void setHospitalization(Encounter encounter, final String rawData) {
        if(!rawData.isEmpty()) {
            Encounter.EncounterHospitalizationComponent hospital = new Encounter.EncounterHospitalizationComponent();

            hospital.setAdmitSource(ResourceHelper.createCodeableConcept(HcConstants.URN_VISTA_ADMIN, rawData, rawData));

            encounter.setHospitalization(hospital);
        }
    }
}
