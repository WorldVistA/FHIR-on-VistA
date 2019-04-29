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
import org.hl7.fhir.dstu3.model.Immunization;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ImmunizationParser  implements VistaParser<Immunization> {

    @Override
    public List<Immunization> parseList(String httpData) {

        List<Immunization> result = new ArrayList<>();

        if(StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.trim().split("\\^");

        if(records.length < 2) {
            return result;
        }

        if(records[0].equalsIgnoreCase("-1")) {
            return result;
        }

        for(int i = 1; i < records.length; i++) {
            String[] fields = records[i].split("\\|");

            Immunization immunization = new Immunization();
            immunization.setId(fields[0]);
            immunization.setStatus(Immunization.ImmunizationStatus.COMPLETED);
            immunization.setPrimarySource(true);
            immunization.setPatient(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, records[0], "", ResourceHelper.ReferenceType.Patient));

            Optional<Date> immunizationDate = InputValidator.parseAnyDate(fields[1]);
            if (immunizationDate.isPresent()) {
                immunization.setDate(immunizationDate.get());
            }

            immunization.setVaccineCode(ResourceHelper.createCodeableConcept(HcConstants.VACCINE, fields[2], fields[3]));
            if(!fields[6].isEmpty()) {
                immunization.setLocation(ResourceHelper.createReference(HcConstants.URN_VISTA_LOCATION, fields[6], fields[6], ResourceHelper.ReferenceType.Location));
            }

            if(!fields[4].isEmpty() && !fields[4].equalsIgnoreCase(";")) {
                String[] doseParts = fields[4].split(";");
                int sequence = Integer.parseInt(doseParts[0]);
                Immunization.ImmunizationVaccinationProtocolComponent component = new Immunization.ImmunizationVaccinationProtocolComponent();
                component.setDoseSequence(sequence);
                component.setDescription(doseParts[1]);
                immunization.addVaccinationProtocol(component);
            }

            immunization.setMeta(ResourceHelper.getVistaMeta());

            result.add(immunization);
        }

        return result;
    }
}
