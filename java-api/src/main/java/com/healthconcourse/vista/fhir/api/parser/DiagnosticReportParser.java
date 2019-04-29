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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DiagnosticReportParser implements VistaParser<DiagnosticReport> {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticReportParser.class);
    private String mPatientId;

    @Override
    public List<DiagnosticReport> parseList(String httpData) {

        List<DiagnosticReport> results = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return results;
        }

        String[] records = httpData.trim().split("\\^");
        boolean isFirst = true;

        for (String record : records) {
            Optional<DiagnosticReport> med = parseDiagnosticReportRecord(record, isFirst);
            med.ifPresent(results::add);
            isFirst = false;
        }

        return results;
    }

    private Optional<DiagnosticReport> parseDiagnosticReportRecord(String record, boolean isFirst) {

        DiagnosticReport result = new DiagnosticReport();

        String[] fields = record.split("\\|");

        if (isFirst) {
            mPatientId = fields[0];
            return Optional.empty();
        } else if (fields.length < 6) {
            return Optional.empty();
        }

        // Patient ID
        result.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, mPatientId, "", ResourceHelper.ReferenceType.Patient));
        result.setId(mPatientId);

        // category
        try {
            result.setCategory((new CodeableConcept()).setText(fields[0]));
        } catch (Exception ex) {
            LOG.error("Invalid DiagnosticReport Category, exception " + ex.getMessage());
        }

        result.setStatus(getDiagnosticReportStatus(fields[1]));

        // dateTime
        Optional<Date> observationDate = InputValidator.parseAnyDate(fields[2]);
        if (observationDate.isPresent()) {
            result.setIssued(observationDate.get());
            Period period = new Period();
            period.setStart(observationDate.get());
            period.setEnd(observationDate.get());
            result.setEffective(period);
        }

        // provider
        result.setPerformer(getPerformers(fields[3]));

        // identifier
        result.setIdentifier(getIds(fields[4]));

        result.setMeta(ResourceHelper.getVistaMeta());

        if(fields.length > 6) {
            String display = "";
            if(fields.length > 7) {
                display = fields[7];
            }
            result.setCode(ResourceHelper.createCodeableConcept(HcConstants.CPT, fields[6], display));
        }

        if(fields.length > 7) {
            result.setCodedDiagnosis(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.SNOMED_URN, fields[8], fields[7]));

        }

        // conclusion
        if(fields.length > 9) {
            result.setConclusion(fields[10]);
        }

        return Optional.of(result);
    }

    private List<Identifier> getIds(String ids) {

        List<Identifier> result = new ArrayList<>();

        Identifier id = new Identifier();
        id.setValue(ids);
        result.add(id);

        return result;
    }

    private List<DiagnosticReport.DiagnosticReportPerformerComponent> getPerformers(String actor) {

        List<DiagnosticReport.DiagnosticReportPerformerComponent> result = new ArrayList<>(1);

        DiagnosticReport.DiagnosticReportPerformerComponent component = new DiagnosticReport.DiagnosticReportPerformerComponent();
        component.setActor(new Reference().setDisplay(actor));

        result.add(component);

        return result;
    }


    private static DiagnosticReport.DiagnosticReportStatus getDiagnosticReportStatus(String data) {
        switch (data.toUpperCase()) {
            case "AMENDED":
                return DiagnosticReport.DiagnosticReportStatus.AMENDED;
            case "APPENDED":
                return DiagnosticReport.DiagnosticReportStatus.APPENDED;
            case "CANCELLED":
                return DiagnosticReport.DiagnosticReportStatus.CANCELLED;
            case "CORRECTED":
                return DiagnosticReport.DiagnosticReportStatus.CORRECTED;
            case "ENTEREDINERROR":
                return DiagnosticReport.DiagnosticReportStatus.ENTEREDINERROR;
            case "FINAL":
                return DiagnosticReport.DiagnosticReportStatus.FINAL;
            case "PARTIAL":
                return DiagnosticReport.DiagnosticReportStatus.PARTIAL;
            case "PRELIMINARY":
                return DiagnosticReport.DiagnosticReportStatus.PRELIMINARY;
            case "REGISTERED":
                return DiagnosticReport.DiagnosticReportStatus.REGISTERED;
            case "UNKNOWN":
                return DiagnosticReport.DiagnosticReportStatus.UNKNOWN;
            default:
                return DiagnosticReport.DiagnosticReportStatus.UNKNOWN;
        }
    }
}
