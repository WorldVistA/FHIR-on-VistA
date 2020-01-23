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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AllergyParser implements VistaParser {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyParser.class);

    @Override
    public List<AllergyIntolerance> parseList(String httpData) {

        List<AllergyIntolerance> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            LOG.warn("No allergy JSON returned from VistA");
            return result;
        }
        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson  == null) {
            LOG.warn("Invalid allergy JSON");
            LOG.warn(httpData);
            return result;
        }

        JSONArray allRecords = allJson.read("$Allergies");
        if (allRecords != null) {
            for (int i = 0; i < allRecords.length(); i++) {
                JSONObject json = allRecords.getJSONObject(i).getJSONObject("Allergy");
                try {
                    AllergyIntolerance allergy = new AllergyIntolerance();
                    allergy.setMeta(ResourceHelper.getVistaMeta());

                    String resourceId = json.getString("resourceId");
                    allergy.setId(resourceId);

                    String patientName = json.getString("patient");
                    String icn = json.getString("patientICN");
                    Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                    allergy.setPatient(patient);

                    Optional<Date> assertedDate = InputValidator.parseAnyDate(json.getString("originationDateTimeFHIR"));
                    if (assertedDate.isPresent()) {
                        allergy.setAssertedDate(assertedDate.get());
                    }

                    String allergen = json.getString("reactant");
                    if (!StringUtils.isEmpty(allergen)) {
                        Long allergenId = json.optLong("reactantSCT", HcConstants.MISSING_ID);
                        String id = allergenId.longValue() != HcConstants.MISSING_ID ? allergenId.toString() : "";
                        allergy.setCode(ResourceHelper.createCodeableConcept(HcConstants.VACCINE, id, allergen));
                    }
                    allergy.setReaction(this.getReactions(json));

                    String mechanism = json.getString("mechanism");
                    if (mechanism != null) {
                        if (mechanism.equalsIgnoreCase("PHARMACOLOGIC")) {
                            allergy.addCategory(AllergyIntolerance.AllergyIntoleranceCategory.MEDICATION);
                        }
                        // Other VistA categories of ALLERGY and UNKNOWN do not map to FHIR AllergyIntoleranceCategory enum
                    }

                    String verified = json.getString("verified");
                    if (verified != null) {
                        if (verified.equalsIgnoreCase("YES")) {
                            allergy.setVerificationStatus(AllergyIntolerance.AllergyIntoleranceVerificationStatus.CONFIRMED);
                        }
                        if (verified.equalsIgnoreCase("NO")) {
                            allergy.setVerificationStatus(AllergyIntolerance.AllergyIntoleranceVerificationStatus.UNCONFIRMED);
                        }
                    }

                    allergy.setRecorder(this.getRecorder(json));

                    result.add(allergy);
                }
                catch (Exception ex) {
                    LOG.warn("Error parsing allergy JSON", ex);
                    LOG.warn(json.toString());
                }
            }
        }
        return result;
    }

    private Reference getRecorder(JSONObject json) {
        Reference result = null;
        JSONObject chartsMarkeds = json.optJSONObject("chartMarkeds");
        if (chartsMarkeds != null) {
            JSONArray charts = chartsMarkeds.optJSONArray("chartMarked");
            if (charts != null) {
                // Only take the first record, HAPI FHIR only allows for one although the spec shows more than one is allowed
                String resourceId = charts.getJSONObject(0).getString("userEnteringResId");
                String name = charts.getJSONObject(0).getString("userEntering");
                result = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, resourceId, name, ResourceHelper.ReferenceType.Practitioner);
            }
        }
        return result;
    }

    private List<AllergyIntolerance.AllergyIntoleranceReactionComponent> getReactions (JSONObject json) {
        List<AllergyIntolerance.AllergyIntoleranceReactionComponent> results = new ArrayList<>();
        JSONObject reactionHolder = json.optJSONObject("reactionss");
        if (reactionHolder != null) {
            JSONArray reactions = reactionHolder.optJSONArray("reactions");
            if (reactions != null) {
                for (int j = 0; j < reactions.length(); j++) {
                    AllergyIntolerance.AllergyIntoleranceReactionComponent component = new AllergyIntolerance.AllergyIntoleranceReactionComponent();
                    String reaction = reactions.getJSONObject(j).getString("reaction");
                    Long reactionId = reactions.getJSONObject(j).optLong("reactionSCT", HcConstants.MISSING_ID);
                    String id = reactionId.longValue() != HcConstants.MISSING_ID ? reactionId.toString() : "";
                    component.setManifestation(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.SNOMED_URN, id, reaction));
                    component.setDescription(reaction);
                    results.add(component);
                }
            }
        }
        return results;
    }
}
