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
import org.json.JSONArray;
import org.hl7.fhir.dstu3.model.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ObservationParser {

    private static final Logger LOG = LoggerFactory.getLogger(ObservationParser.class);

    public List<Observation> parseVitalsList(String httpData) {

        List<Observation> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson  == null) {
            LOG.error("Invalid Vitals JSON");
            LOG.info(httpData);
            return result;
        }

        JSONArray allVitals = allJson.read("$Vitals");
        if (allVitals != null) {
            for (int i = 0; i < allVitals.length(); i++) {
                JSONObject json = allVitals.getJSONObject(i).getJSONObject("Vitals");
                try {
                    Observation observation = new Observation();
                    observation.setStatus(Observation.ObservationStatus.FINAL);
                    observation.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.OBSERVATION_CODING_SYSTEM, "vital-signs", "Vital Signs"));
                    observation.setMeta(ResourceHelper.getVistaMeta());

                    String resourceId = json.getString("resourceId");
                    observation.setId(resourceId);

                    Long code = json.optLong("vitalTypeSCT");
                    String snomedCode = code != HcConstants.MISSING_ID ? code.toString() : "";
                    String snomedDesc = json.getString("vitalTypeSc");
                    observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, snomedCode, snomedDesc));

                    String patientName = json.getString("patient");
                    String icn = json.getString("patientICN");
                    Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                    observation.setSubject(patient);

                    String value;
                    Double rate = json.optDouble("rate");
                    if (rate.isNaN()) {
                        value = json.getString("rate");
                        observation.setValue(new StringType(value));
                    } else {
                        Quantity quantity = new Quantity();
                        quantity.setValue(rate);
                        observation.setValue(quantity);
                    }

                    Optional<Date> observationDate = InputValidator.parseAnyDate(json.getString("dateTimeVitalsTakenFHIR"));
                    if (observationDate.isPresent()) {
                        observation.setIssued(observationDate.get());
                        DateTimeType effective = new DateTimeType(observationDate.get());
                        observation.setEffective(effective);
                    }

                    List<Reference> performers = new ArrayList<>();
                    String requester = json.getString("enteredBy");
                    if (!StringUtils.isEmpty(requester)) {
                        String requesterId = json.getString("enteredByResId");
                        Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, requesterId, requester, ResourceHelper.ReferenceType.Practitioner);
                        performers.add(ref);
                    }

                    String site = json.getString("hospitalLocation");
                    if (!StringUtils.isEmpty(site)) {
                        Long siteId = json.optLong("hospitalLocationId", HcConstants.MISSING_ID);
                        String id = siteId.longValue() != HcConstants.MISSING_ID ? siteId.toString() : "";
                        Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_ORGANIZATION, id, site, ResourceHelper.ReferenceType.Organization);
                        performers.add(ref);
                    }

                    if (performers.size() > 0) {
                        observation.setPerformer(performers);
                    }

                    result.add(observation);
                } catch (Exception ex) {
                    LOG.warn("Error parsing vitals JSON", ex);
                    LOG.warn(json.toString());
                }
            }
        }
        return result;
    }

    public List<Observation> parseLabsList(String httpData) {
        List<Observation> result = new ArrayList<>();
        if(StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson  == null) {
            return result;
        }
        String patientIcn = allJson.read("$.Labs[0].Lab.patientIcn");
        String patientName = allJson.read("$.Labs[0].Lab.patientName");
        JSONArray allLabs = allJson.read("$.Labs[0].Lab.chemHemToxRiaSerEtcs.chemHemToxRiaSerEtc");
        if (allLabs != null) {
            for (int i = 0; i < allLabs.length(); i++) {
                JSONObject json = allLabs.getJSONObject(i);
                try {
                    Observation observation = createNewObservation("laboratory", "Laboratory");

                    String resourceId = json.getString("resourceId");
                    observation.setId(resourceId);
                    observation.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, patientIcn, patientName, ResourceHelper.ReferenceType.Patient));

                    String completed = json.getString("dateReportCompletedFHIR");
                    Optional<Date> observationDate = InputValidator.parseAnyDate(completed);
                    if (observationDate.isPresent()) {
                        observation.setIssued(observationDate.get());
                        DateTimeType effective = new DateTimeType(observationDate.get());
                        observation.setEffective(effective);
                    }

                    Double value = json.optDouble("result");
                    if (value.isNaN()) {
                        String nonNumber = json.getString("result");
                        observation.setValue(new StringType(nonNumber));
                    } else {
                        String units = json.getString("units");

                        Quantity quantity = new Quantity();
                        quantity.setValue(value);
                        quantity.setUnit(units);
                        quantity.setSystem(HcConstants.UNIT_OF_MEASURE_SYSTEM);
                        quantity.setCode(units);
                        observation.setValue(quantity);
                    }
                    String loincCode = json.getString("loincCode");
                    String loincName = json.getString("loincName");
                    observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.LOINC, loincCode, loincName));

                    List<Reference> performers = new ArrayList<>();
                    String requester = json.getString("requestingPerson");
                    if (!StringUtils.isEmpty(requester)) {
                        String id = json.getString("requestingPersonResId");
                        Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, id, requester, ResourceHelper.ReferenceType.Practitioner);
                        performers.add(ref);
                    }

                    String site = json.getString("releasingSite");
                    if (!StringUtils.isEmpty(site)) {
                        Long id = json.optLong("releasingSiteId", HcConstants.MISSING_ID);
                        String siteId = id.longValue() != HcConstants.MISSING_ID ? id.toString() : "";
                        Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_ORGANIZATION, siteId, site, ResourceHelper.ReferenceType.Organization);
                        performers.add(ref);
                    }

                    if (performers.size() > 0) {
                        observation.setPerformer(performers);
                    }

                    Double low = json.optDouble("referenceLow");
                    if (!low.isNaN()) {
                        Observation.ObservationReferenceRangeComponent component = new Observation.ObservationReferenceRangeComponent();
                        SimpleQuantity lowRange = new SimpleQuantity();
                        lowRange.setValue(low);
                        component.setLow(lowRange);
                        Double high = json.optDouble("referenceHigh");
                        if (!high.isNaN()) {
                            SimpleQuantity highRange = new SimpleQuantity();
                            lowRange.setValue(high);
                            component.setLow(highRange);
                        }
                        List<Observation.ObservationReferenceRangeComponent> components = new ArrayList<>();
                        components.add(component);
                        observation.setReferenceRange(components);
                    }
                    result.add(observation);
                } catch (Exception ex) {
                    LOG.warn("Error parsing labs JSON", ex);
                    LOG.warn(json.toString());
                }
            }
        }

        return result;
    }


    public List<Observation> parseHealthFactorsList(String httpData) {
        List<Observation> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson  == null) {
            return result;
        }

        JSONArray allFactors = allJson.read("$HealthFactors");
        if (allFactors != null) {
            for (int i = 0; i < allFactors.length(); i++) {
                JSONObject json = allFactors.getJSONObject(i).getJSONObject("HealthFactor");
                try {
                    Observation observation = createNewObservation("social-history", "Social History");

                    String resourceId = json.getString("resourceId");
                    observation.setId(resourceId);

                    Long category = json.optLong("hlfCategoryId", HcConstants.MISSING_ID);
                    String factorName = json.getString("hlfCategory");
                    String factorId = category.longValue() != HcConstants.MISSING_ID ? category.toString() : "";
                    observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.URN_VISTA_HEALTH_FACTOR, factorId, factorName));

                    String patientName = json.getString("patientName");
                    String icn = json.getString("patientICN");
                    Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                    observation.setSubject(patient);

                    Optional<Date> observationDate = InputValidator.parseAnyDate(json.getString("visitFHIR"));
                    if (observationDate.isPresent()) {
                        observation.setIssued(observationDate.get());
                        DateTimeType effective = new DateTimeType(observationDate.get());
                        observation.setEffective(effective);
                    }

                    observation.setValue(new StringType(json.getString("hlfName")));

                    List<Reference> performers = new ArrayList<>();
                    String provider = json.getString("encounterProvider");
                    if (!StringUtils.isEmpty(provider)) {
                        String id = json.getString("encounterProviderResId");
                        Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, id, provider, ResourceHelper.ReferenceType.Practitioner);
                        performers.add(ref);
                    }

                    String encounterId = json.getString("visitResId");
                    if (!StringUtils.isEmpty(encounterId)) {
                        observation.setContext(ResourceHelper.createReference(HcConstants.URN_VISTA_ENCOUNTER, encounterId, null, ResourceHelper.ReferenceType.Encounter));
                    }

                    if (performers.size() > 0) {
                        observation.setPerformer(performers);
                    }

                    result.add(observation);
                } catch (Exception ex) {
                    LOG.warn("Error parsing health factors JSON", ex);
                    LOG.warn(json.toString());
                }
            }
        }
        return result;
    }

    public List<Observation> parseMentalHealthList(String httpData) {
        List<Observation> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson  == null) {
            return result;
        }

        JSONArray allMentalHealth = allJson.read("$Observations");
        if (allMentalHealth != null) {
            for (int i = 0; i < allMentalHealth.length(); i++) {
                JSONObject json = allMentalHealth.getJSONObject(i).optJSONObject("Mhadm");
                if (json != null) {
                    try {
                        Observation observation = createNewObservation("survey", "Survey");

                        String resourceId = json.getString("resourceId");
                        observation.setId(resourceId);

                        String patientName = json.getString("patient");
                        String icn = json.getString("patientICN");
                        Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                        observation.setSubject(patient);

                        Optional<Date> observationDate = InputValidator.parseAnyDate(json.getString("dateGivenFHIR"));
                        if (observationDate.isPresent()) {
                            observation.setIssued(observationDate.get());
                            DateTimeType effective = new DateTimeType(observationDate.get());
                            observation.setEffective(effective);
                        }

                        List<Reference> performers = new ArrayList<>();
                        String requester = json.getString("administeredBy");
                        if (!StringUtils.isEmpty(requester)) {
                            String id = json.getString("administeredByResId");
                            Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, id, requester, ResourceHelper.ReferenceType.Practitioner);
                            performers.add(ref);
                        }

                        String site = json.getString("location");
                        if (!StringUtils.isEmpty(site)) {
                            Long id = json.optLong("locationId", HcConstants.MISSING_ID);
                            String siteId = id.longValue() != HcConstants.MISSING_ID ? id.toString() : "";
                            Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_ORGANIZATION, siteId, site, ResourceHelper.ReferenceType.Organization);
                            performers.add(ref);
                        }

                        if (performers.size() > 0) {
                            observation.setPerformer(performers);
                        }

                        String loincCode = json.getString("loincCode");
                        if (!StringUtils.isEmpty(loincCode)) {
                            String instrumentName = json.getString("instrumentName");
                            observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.LOINC, loincCode, instrumentName));
                        }
                        String snomedDesc = json.getString("sctPreferredTerm");
                        if (!StringUtils.isEmpty(snomedDesc)) {
                            Long code = json.optLong("snomedCode", HcConstants.MISSING_ID);
                            String snomedCode = code.longValue() != HcConstants.MISSING_ID ? code.toString() : "";
                            observation.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, snomedCode, snomedDesc));
                        }

                        JSONObject mhResult = json.optJSONObject("results").getJSONObject("Mhres");
                        if (mhResult != null) {
                            Long value = mhResult.optLong("rawScore", HcConstants.MISSING_ID);
                            if (value.longValue() != HcConstants.MISSING_ID) {
                                String units = mhResult.getString("scale");
                                Quantity quantity = new Quantity();
                                quantity.setValue(value);
                                quantity.setUnit(units);
                                observation.setValue(quantity);
                            }
                        }

                        result.add(observation);
                    } catch (Exception ex) {
                        LOG.warn("Error parsing mental health Mhadm JSON", ex);
                        LOG.warn(json.toString());
                    }
                } else {
                    json = allMentalHealth.getJSONObject(i).optJSONObject("Mhdiag");
                    if (json != null) {
                        try {
                            Observation observation = createNewObservation("survey", "Survey");

                            String resourceId = json.getString("resourceId");
                            observation.setId(resourceId);

                            String patientName = json.getString("patientName");
                            String icn = json.getString("patientICN");
                            Reference patient = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, patientName, ResourceHelper.ReferenceType.Patient);
                            observation.setSubject(patient);

                            Optional<Date> observationDate = InputValidator.parseAnyDate(json.getString("dateTimeOfDiagnosisFHIR"));
                            if (observationDate.isPresent()) {
                                observation.setIssued(observationDate.get());
                                DateTimeType effective = new DateTimeType(observationDate.get());
                                observation.setEffective(effective);
                            }

                            List<Reference> performers = new ArrayList<>();
                            String requester = json.getString("diagnosisBy");
                            if (!StringUtils.isEmpty(requester)) {
                                Long id = json.optLong("diagnosisById", HcConstants.MISSING_ID);
                                String requesterId = id.longValue() != HcConstants.MISSING_ID ? id.toString() : "";
                                Reference ref = ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, requesterId, requester, ResourceHelper.ReferenceType.Practitioner);
                                performers.add(ref);
                            }
                            if (performers.size() > 0) {
                                observation.setPerformer(performers);
                            }

                            Long value = json.optLong("axis5-GAF", HcConstants.MISSING_ID);
                            if (value.longValue() != HcConstants.MISSING_ID) {
                                String units = "Global Assessment of Functioning";
                                Quantity quantity = new Quantity();
                                quantity.setValue(value);
                                quantity.setUnit(units);
                                observation.setValue(quantity);
                            }

                            result.add(observation);
                        } catch (Exception ex) {
                            LOG.warn("Error parsing mental health Mhdiag JSON", ex);
                            LOG.warn(json.toString());
                        }
                    }
                }
            }
        }
        return result;
    }

    private Observation createNewObservation(String code, String display) {
        Observation observation = new Observation();
        observation.setStatus(Observation.ObservationStatus.FINAL); // All VistA observations are final
        observation.setCategory(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.OBSERVATION_CODING_SYSTEM, code, display));
        observation.setMeta(ResourceHelper.getVistaMeta());
        return observation;
    }
}
