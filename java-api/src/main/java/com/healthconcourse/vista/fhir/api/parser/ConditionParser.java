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

public class ConditionParser implements VistaParser<Condition> {

    private static final Logger LOG = LoggerFactory.getLogger(ConditionParser.class);

    @Override
    public List<Condition> parseList(String httpData) {

        List<Condition> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult conditionJson = JsonPath.parseOrNull(httpData);
        if (conditionJson == null) {
            LOG.warn("Unable to parse Condition JSON");
            return result;
        }

        JSONArray allConditions = conditionJson.read("$.Conditions..Problem");

        if (allConditions != null) {
            for (int i = 0; i < allConditions.length(); i++) {
                JSONObject json = allConditions.getJSONObject(i);
                Condition condition = new Condition();
                condition.setMeta(ResourceHelper.getVistaMeta());

                String resourceId = json.getString("resourceId");
                condition.setId(resourceId);

                String patientName = json.getString("patientName");
                String icn = json.getString("patientICN");
                Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                condition.setSubject(patient);

                Long code = json.optLong("snomedCTconceptCode", HcConstants.MISSING_ID);
                String snomedCode = code.longValue() != HcConstants.MISSING_ID ? code.toString() : "";
                String snomedDesc = json.getString("snomedCTconceptCodeText");
                condition.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, snomedCode, snomedDesc));

                Optional<Date> onsetDate = InputValidator.parseAnyDate(json.getString("dateOfOnsetFHIR"));
                if (onsetDate.isPresent()) {
                    DateTimeType onset = new DateTimeType(onsetDate.get());
                    condition.setOnset(onset);
                }

                Optional<Date> recordedDate = InputValidator.parseAnyDate(json.optString("dateEnteredFHIR"));
                if (recordedDate.isPresent()) {
                    DateTimeType recorded = new DateTimeType(recordedDate.get());
                    condition.setAssertedDateElement(recorded);
                }

                String status = json.getString("status");
                if (!StringUtils.isEmpty(status)) {
                    condition.setClinicalStatus(getStatus(status));
                }

                String requester = json.getString("responsibleProvider");
                if (!StringUtils.isEmpty(requester)) {
                    String requesterId = json.getString("responsibleProviderResId");
                    Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, requesterId, requester, ResourceHelper.ReferenceType.Practitioner);
                    condition.setAsserter(ref);
                }

                String problem = json.getString("problem");
                if (!StringUtils.isEmpty(problem)) {
                    Annotation annotation = new Annotation();
                    annotation.setText(problem);
                    List<Annotation> annotations = new ArrayList<>();
                    condition.setNote(annotations);
                }
                result.add(condition);
            }
        }

        return result;
    }

    private static Condition.ConditionClinicalStatus getStatus(String input) {
        switch (input.toUpperCase()) {
            case "ACTIVE":
                return Condition.ConditionClinicalStatus.ACTIVE;
            case "INACTIVE":
                return Condition.ConditionClinicalStatus.INACTIVE;
            default:
                return Condition.ConditionClinicalStatus.NULL;
        }
    }
}
