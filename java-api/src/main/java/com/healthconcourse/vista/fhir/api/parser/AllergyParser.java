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
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.dstu3.model.codesystems.AllergyVerificationStatus;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AllergyParser implements VistaParser<AllergyIntolerance> {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyParser.class);
    @Override
    public List<AllergyIntolerance> parseList(String httpData) {

        List<AllergyIntolerance> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.trim().split("\\^");

        if (records.length < 2) {
            return result;
        }

        if (records[0].equalsIgnoreCase("-1")) {
            return result;
        }

        for (int i = 1; i < records.length; i++) {
            AllergyIntolerance allergy = new AllergyIntolerance();

            allergy.setPatient(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, records[0], "", ResourceHelper.ReferenceType.Patient));

            String[] fields = records[i].split("\\|");

            allergy.setCode(getAllergenCode(fields[0], fields[1]));

            Optional<Date> assertedDate = InputValidator.parseAnyDate(fields[2]);
            if (assertedDate.isPresent()) {
                allergy.setRecordedDate(assertedDate.get());
            }

            String[] ids = fields[5].split(":");

            allergy.setId(ids[ids.length - 1]);

            allergy.setIdentifier(getIds(ids));

            allergy.setReaction(getReactions(ids));

            allergy.setCategory(convertCategory(fields[3]));

            allergy.setVerificationStatus(getStatus(fields[4]));

            allergy.setMeta(ResourceHelper.getVistaMeta());

            result.add(allergy);
        }

        return result;
    }

    private CodeableConcept getAllergenCode(String display, String code) {

        String[] codeParts = code.split(":");

        // Always prefer SNOMED codes
        if (codeParts[0].equalsIgnoreCase("SCT")) {
            return ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, codeParts[1], display);
        }

        if (codeParts[0].equalsIgnoreCase("NDFRT")) {
            String[] ndfrtParts = codeParts[1].split("~");
            return ResourceHelper.createCodeableConcept(HcConstants.NDFRT, ndfrtParts[0], display);
        }

        if (codeParts[0].equalsIgnoreCase("VANDF")) {
            String[] vandfParts = codeParts[1].split("~");
            return ResourceHelper.createCodeableConcept(HcConstants.URN_VISTA_VANDF, vandfParts[0], display);
        }
        return null;
    }

    private CodeableConcept getStatus(String vistaStatus) {

        AllergyVerificationStatus status;
        switch (vistaStatus) {
            case "confirmed":
                status = AllergyVerificationStatus.fromCode("confirmed");
                break;
            default:
                status = AllergyVerificationStatus.fromCode("NULL");;
        }

        return ResourceHelper.createCodeableConcept(status.getSystem(), status.getDefinition(),
                status.getDisplay());
    }

    private static List<Enumeration<AllergyIntolerance.AllergyIntoleranceCategory>> convertCategory(String vistaCategory) {

        List<Enumeration<AllergyIntolerance.AllergyIntoleranceCategory>> result = new ArrayList<>();

        String[] categories = vistaCategory.split(":");

        for (String category : categories) {

            StringType code = new StringType();
            code.setValue(category.toLowerCase());

            AllergyIntolerance.AllergyIntoleranceCategoryEnumFactory factory = new AllergyIntolerance.AllergyIntoleranceCategoryEnumFactory();

            try {
                Enumeration<AllergyIntolerance.AllergyIntoleranceCategory> enumCategory = null;
                enumCategory = factory.fromType(code);
                result.add(enumCategory);
            } catch (FHIRException e) {
                LOG.error("Error parsing category", e);
            }
        }

        return result;
    }

    private List<AllergyIntolerance.AllergyIntoleranceReactionComponent> getReactions(String[] ids) {

        List<AllergyIntolerance.AllergyIntoleranceReactionComponent> result = new ArrayList<>(ids.length);

        for (int i = 0; i < ids.length - 1; i++) {
            AllergyIntolerance.AllergyIntoleranceReactionComponent component = new AllergyIntolerance.AllergyIntoleranceReactionComponent();

            String[] parts = ids[i].split("~");

            if (parts.length > 1) {
                component.setDescription(parts[1]);
            }

            result.add(component);
        }

        return result;
    }

    private List<Identifier> getIds(String[] allergyIds) {

        List<Identifier> result = new ArrayList<>();

        if (allergyIds.length > 1) {
            for (int i = 0; i < allergyIds.length - 1; i++) {
                Identifier id = new Identifier();
                id.setValue(allergyIds[i]);
                result.add(id);
            }
        }

        return result;
    }
}
