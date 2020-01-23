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
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import com.nfeld.jsonpathlite.JsonPath;
import com.nfeld.jsonpathlite.JsonResult;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Flag;
import org.hl7.fhir.dstu3.model.Reference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FlagParser implements VistaParser<Flag> {

    private static final Logger LOG = LoggerFactory.getLogger(FlagParser.class);

    @Override
    public List<Flag> parseList(String httpData) {
        List<Flag> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            LOG.info("No flag data found for patient");
            return result;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson == null) {
            LOG.warn("Unable to parse Flag JSON");
            return result;
        }

        JSONArray allFlags = allJson.read("$.Flags..Flag");
        if (allFlags != null) {
            for (int i = 0; i < allFlags.length(); i++) {
                JSONObject json = allFlags.getJSONObject(i);
                Flag flag = new Flag();
                flag.setMeta(ResourceHelper.getVistaMeta());
                String resourceId = json.getString("resourceId");
                flag.setId(resourceId);

                String patientName = json.getString("patientName");
                String icn = json.getString("patientNameICN");
                Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                flag.setSubject(patient);

                Long sctCode = json.optLong("flagSCT", HcConstants.MISSING_ID);
                String snomedCode = sctCode.longValue() != HcConstants.MISSING_ID ? sctCode.toString() : "";
                String snomedDesc = json.getString("flagName");
                CodeableConcept code = ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, snomedCode, snomedDesc);
                String narrative = json.getString("assignmentNarrative");
                code.setText(narrative);
                flag.setCode(code);

                Flag.FlagStatus status = Flag.FlagStatus.ACTIVE;
                String statusText = json.getString("status");
                if(!statusText.equalsIgnoreCase("ACTIVE")) {
                    status = Flag.FlagStatus.INACTIVE;
                }
                flag.setStatus(status);

                Long rawSiteId = json.optLong("originatingSiteId", HcConstants.MISSING_ID);
                String siteId = rawSiteId.longValue() != HcConstants.MISSING_ID ? rawSiteId.toString() : "";
                String siteName = json.getString("originatingSite");
                flag.setAuthor(ResourceHelper.createReference(HcConstants.URN_VISTA_ORGANIZATION, siteId, siteName, ResourceHelper.ReferenceType.Organization));
                result.add(flag);
            }
        }
        return result;
    }
}
