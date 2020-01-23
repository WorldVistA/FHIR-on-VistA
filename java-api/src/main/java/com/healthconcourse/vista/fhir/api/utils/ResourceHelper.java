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
package com.healthconcourse.vista.fhir.api.utils;

import com.healthconcourse.vista.fhir.api.HcConstants;
import org.hl7.fhir.dstu3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class ResourceHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceHelper.class);

    public enum ReferenceType {
        Patient,
        Practitioner,
        Location,
        Condition,
        Organization,
        Procedure,
        Encounter,
        Medication,
        None
    }

    /**
     * Generate a reference to a different FHIR record for an unknown FHIR type
     *
     * @param system Optional. A unique identifier for the referenced item
     * @param id Required. The unique identifier of the referenced item
     * @param display Optional. The display element of the referenced item
     * @return
     */
    public static Reference createReference(String system, String id, String display) {

        return createReference(system, id, display, ReferenceType.None);
    }


    /**
     * Generate a reference to a different FHIR record
     *
     * @param system Optional. A unique identifier for the referenced item
     * @param id Required. The unique identifier of the referenced item
     * @param display Optional. The display element of the referenced item
     * @param type Required. The FHIR type to create a reference for
     * @return
     */
    public static Reference createReference(String system, String id, String display, ReferenceType type) {

        if(id == null || id.isEmpty()) {
            throw new InvalidParameterException("ID is required");
        }

        Identifier referenceId = new Identifier();
        referenceId.setSystem(system);
        referenceId.setValue(id);

        Reference reference = new Reference();
        reference.setIdentifier(referenceId);
        reference.setDisplay(display);

        switch (type) {
            case Patient:
                reference.setReference(String.format("/Patient/%s", id));
                break;
            case Practitioner:
                reference.setReference(String.format("/Practitioner/%s", id));
                break;
            case Location:
                try {
                    reference.setReference(String.format("/Location/%s", URLEncoder.encode(id, StandardCharsets.UTF_8.toString())));
                } catch (Exception ex) {
                    LOG.error("Unable to encode url", ex);
                }
                break;
            case Condition:
                reference.setReference(String.format("/Condition/%s", id));
                break;
            case Organization:
                reference.setReference(String.format("/Organization/%s", id));
                break;
            case Procedure:
                reference.setReference(String.format("/Procedure/%s", id));
                break;
            case Encounter:
                reference.setReference(String.format("/Encounter/%s", id));
                break;
            case Medication:
                reference.setReference(String.format("/Medication/%s", id));
                break;
            case None:
                break;
        }

        return reference;
    }

    /**
     * Generate a list of references to a single FHIR record
     *
     * @param system Optional. A unique identifier for the referenced item
     * @param id Required. The unique identifier of the referenced item
     * @param display Optional. The display element of the referenced item
     * @param type Required. The FHIR type to create a reference for
     * @return
     */
    public static List<Reference> createSingleReferenceAsList(String system, String id, String display, ReferenceType type) {
        List<Reference> references = new ArrayList<>();

        references.add(ResourceHelper.createReference(system, id, display, type));

        return references;
    }

    /**
     * Generate a list of references to a single FHIR record with an unknown type
     *
     * @param system Optional. A unique identifier for a system which the referenced item belongs
     * @param id Required. The unique identifier of the referenced item
     * @param display Optional. The display element of the referenced item
     * @return
     */
    public static List<Reference> createSingleReferenceAsList(String system, String id, String display) {

        return createSingleReferenceAsList(system, id, display, ReferenceType.None);
    }


    /**
     * Create a CodeableConcept object
     *
     * @param system Optional. A unique identifier for the referenced item's system
     * @param codeId Optional. The unique identifier of the coded item
     * @param display Optional. The display element of the referenced item
     * @return
     */
    public static CodeableConcept createCodeableConcept(String system, String codeId, String display) {
        CodeableConcept concept = new CodeableConcept();
        if (StringUtils.isEmpty(system)) {
            concept.setText(display);
        } else {
            Coding code = new Coding();
            code.setSystem(system);
            code.setCode(codeId);
            code.setDisplay(display);
            concept.addCoding(code);
        }
        return concept;
    }

    /**
     * Create a list of CodeableConcept objects for a single CodeableConcept
     *
     * @param system Optional. A unique identifier for the referenced item's system
     * @param codeId Optional. The unique identifier of the coded item
     * @param display Optional. The display element of the referenced item
     * @return
     */
    public static List<CodeableConcept> createSingleCodeableConceptAsList(String system, String codeId, String display) {
        List<CodeableConcept> results = new ArrayList<>();

        results.add(ResourceHelper.createCodeableConcept(system, codeId, display));

        return results;
    }


    /**
     * Generates a Meta object for data returned from VistA.
     * @return a FHIR Meta
     */
    public static Meta getVistaMeta() {
        Meta result = new Meta();
        Coding code = new Coding();
        code.setSystem(HcConstants.URN_VISTA_SOURCE);
        code.setDisplay("VistA");
        code.setCode("HC-002");
        result.addTag(code);
        return result;
    }
}
