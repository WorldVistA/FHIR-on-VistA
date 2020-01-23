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
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.Reference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ImmunizationParser  implements VistaParser<Immunization> {

    private static final Logger LOG = LoggerFactory.getLogger(ImmunizationParser.class);

    @Override
    public List<Immunization> parseList(String httpData) {

        List<Immunization> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson  == null) {
            LOG.error("Unable to parse Immunization JSON");
            return result;
        }

        JSONArray allRecords = allJson.read("$Immunizations");
        if (allRecords != null) {
            for (int i = 0; i < allRecords.length(); i++) {
                JSONObject json = allRecords.getJSONObject(i).getJSONObject("Immunize");
                try {
                    Immunization immunization = new Immunization();
                    immunization.setMeta(ResourceHelper.getVistaMeta());

                    String resourceId = json.getString("resourceId");
                    immunization.setId(resourceId);

                    immunization.setPrimarySource(true);

                    String patientName = json.getString("patientName");
                    String icn = json.getString("patientICN");
                    Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                    immunization.setPatient(patient);

                    Optional<Date> visitDate = InputValidator.parseAnyDate(json.getString("visitFHIR"));
                    if (visitDate.isPresent()) {
                        immunization.setDate(visitDate.get());
                    }

                    String encounterId = json.getString("visitResId");
                    if (!StringUtils.isEmpty(encounterId)) {
                        immunization.setEncounter(ResourceHelper.createReference(HcConstants.URN_VISTA_ENCOUNTER, encounterId, null, ResourceHelper.ReferenceType.Encounter));
                    }

                    String site = json.getString("location");
                    if (!StringUtils.isEmpty(site)) {
                        Long siteId = json.optLong("locationId", HcConstants.MISSING_ID);
                        String id = siteId.longValue() != HcConstants.MISSING_ID ? siteId.toString() : "";
                        Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, id, site, ResourceHelper.ReferenceType.Location);
                        immunization.setLocation(ref);
                    }

                    String vaccine = json.getString("immunization");
                    if (!StringUtils.isEmpty(vaccine)) {
                        Long vaccineId = json.optLong("immunizationId", HcConstants.MISSING_ID);
                        String id = vaccineId.longValue() != HcConstants.MISSING_ID ? vaccineId.toString() : "";
                        immunization.setVaccineCode(ResourceHelper.createCodeableConcept(HcConstants.VACCINE, id, vaccine));
                    }

                    String series = json.getString("series");
                    if (!StringUtils.isEmpty(series)) {
                        Immunization.ImmunizationVaccinationProtocolComponent component = new Immunization.ImmunizationVaccinationProtocolComponent();
                        component.setSeries(series);
                        immunization.addVaccinationProtocol(component);
                    }
                    String practitioner = json.getString("immunizationDocumenter");
                    if (!StringUtils.isEmpty(practitioner)) {
                        Long practitionerId = json.optLong("immunizationDocumenterId", HcConstants.MISSING_ID);
                        String id = practitionerId.longValue() != HcConstants.MISSING_ID ? practitionerId.toString() : "";
                        List<Immunization.ImmunizationPractitionerComponent> practitionerList = new ArrayList<>();
                        Immunization.ImmunizationPractitionerComponent component = new Immunization.ImmunizationPractitionerComponent();
                        component.setActor(ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, id, practitioner, ResourceHelper.ReferenceType.Practitioner));
                        practitionerList.add(component);
                        immunization.setPractitioner(practitionerList);
                    }

                    result.add(immunization);
                } catch (Exception ex) {
                    LOG.warn("Error parsing immunization JSON", ex);
                    LOG.warn(json.toString());
                }
            }
        }
        return result;
    }
}
