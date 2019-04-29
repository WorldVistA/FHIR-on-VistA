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
import org.hl7.fhir.dstu3.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ConditionParser implements VistaParser<Condition> {

    @Override
    public List<Condition> parseList(String httpData) {

        List<Condition> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.split("\\|");

        int index = 0;
        if(records.length <= 1) {
            return result;
        }

        String patientId = "";

        for(int i = 0; i < records.length; i++) {
            index = 0;
            String[] parts = records[i].split("\\^");

            if(parts.length <= 3) {
                continue;
            }

            Condition condition = new Condition();
            if (i == 0) {
                patientId = parts[0];
                condition.setId(patientId);
                index = 1;
            }

            condition.setId(parts[index]);

            CodeableConcept concept = new CodeableConcept();
            Coding code = new Coding();

            String[] diagnosisParts = parts[index + 1].split(";");
            if (diagnosisParts.length == 2) {

                code.setSystem(HcConstants.ICD_9);
                code.setCode(diagnosisParts[0]);
                code.setDisplay(diagnosisParts[1]);
                concept.addCoding(code);
            }

            Optional<Date> onsetDate = InputValidator.parseAnyDate(parts[index + 2]);
            if (onsetDate.isPresent()) {
                condition.setOnset(new DateTimeType(onsetDate.get()));
            }

            String[] snomedParts = parts[index + 3].split(";");
            if (snomedParts.length == 2) {
                code = new Coding();
                code.setSystem(HcConstants.SNOMED_URN);
                code.setCode(snomedParts[0]);
                code.setDisplay(snomedParts[1]);
                concept.addCoding(code);
            }

            condition.setCode(concept);

            Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, patientId, "", ResourceHelper.ReferenceType.Patient);
            condition.setSubject(ref);

            condition.setMeta(ResourceHelper.getVistaMeta());

            result.add(condition);
       }
        return result;
    }
}
