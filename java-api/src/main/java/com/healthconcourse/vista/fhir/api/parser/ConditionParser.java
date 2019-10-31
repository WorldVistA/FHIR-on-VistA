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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.codesystems.ConditionVerStatus;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.ConditionCategory;
import org.hl7.fhir.r4.model.codesystems.ConditionClinical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;

public class ConditionParser implements VistaParser<Condition> {
    private static final Logger LOG = LoggerFactory.getLogger(ConditionParser.class);

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
            // NB: Don't encode R69. (Unknown illness); we always have a good SNOMED code instead.
            if (diagnosisParts.length == 3 && !diagnosisParts[0].equals("R69.")) {
                if (diagnosisParts[2].equals("10D")) code.setSystem(HcConstants.ICD_10);
                else if (diagnosisParts[2].equals("ICD")) code.setSystem(HcConstants.ICD_9);
                else {
                    LOG.warn("Found a problem with Unknown coding system: " + records[i]);
                    code.setSystem("unknown");
                }

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

            // Clinical Status
            String status = parts[index + 4];
            Optional<Date> resolutionDate = InputValidator.parseAnyDate(parts[index + 5]);
            ConditionClinical clinicalStatus;
            if (resolutionDate.isPresent())
            {
                condition.setAbatement(new DateTimeType(resolutionDate.get()));
                clinicalStatus = ConditionClinical.RESOLVED;
            }
            else
            {
                if (status.equals("ACTIVE")) clinicalStatus = ConditionClinical.ACTIVE;
                else                         clinicalStatus = ConditionClinical.INACTIVE;
            }
            condition.setClinicalStatus(ResourceHelper.createCodeableConcept(
                    clinicalStatus.getSystem(), clinicalStatus.toCode(), clinicalStatus.getDisplay()));

            // Verification Status
            // This is always the case from VistA. There is no "provisional" dx there.
            ConditionVerStatus verificationStatus = ConditionVerStatus.CONFIRMED;
            condition.setVerificationStatus(
                    ResourceHelper.createCodeableConcept(
                    verificationStatus.getSystem(), verificationStatus.toCode(), verificationStatus.getDisplay())
                    );

            // Category
            CodeableConcept categoryConcept = new CodeableConcept();
            Coding conceptCode;
            if (parts[index + 6].equals("problem")) {
                conceptCode = new Coding(ConditionCategory.PROBLEMLISTITEM.getSystem(),
                    ConditionCategory.PROBLEMLISTITEM.toCode(), ConditionCategory.PROBLEMLISTITEM.getDisplay());
            }
            else if (parts[index + 6].equals("encounter")) {
                conceptCode = new Coding(ConditionCategory.ENCOUNTERDIAGNOSIS.getSystem(),
                    ConditionCategory.ENCOUNTERDIAGNOSIS.toCode(), ConditionCategory.ENCOUNTERDIAGNOSIS.getDisplay());
            }
            else {
                LOG.warn("Found a condition with unknown type: " + records[i]);
                conceptCode = new Coding(ConditionCategory.NULL.getSystem(),
                    ConditionCategory.NULL.toCode(), ConditionCategory.NULL.getDisplay());
            }
            categoryConcept.addCoding(conceptCode);
            condition.addCategory(categoryConcept);

            result.add(condition);
       }
        return result;
    }
}
