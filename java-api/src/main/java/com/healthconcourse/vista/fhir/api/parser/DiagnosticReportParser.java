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

public class DiagnosticReportParser implements VistaParser<DiagnosticReport> {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticReportParser.class);

    @Override
    public List<DiagnosticReport> parseList(String httpData) {
        List<DiagnosticReport> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson == null) {
            LOG.error("Unable to parse Diagnostic Report JSON");
            return result;
        }

        JSONArray allReports = allJson.read("$.DxReportRad");
        if (allReports != null) {
            for (int i = 0; i < allReports.length(); i++) {
                JSONObject reportJson = allReports.getJSONObject(i).optJSONObject("RadReport");
                if (reportJson != null) {
                    DiagnosticReport report = new DiagnosticReport();
                    report.setMeta(ResourceHelper.getVistaMeta());

                    String resourceId = reportJson.getString("resourceId");
                    report.setId(resourceId);

                    Optional<Date> observationDate = InputValidator.parseAnyDate(reportJson.getString("dateTimeFHIR"));
                    if (observationDate.isPresent()) {
                        report.setIssued(observationDate.get());
                        DateTimeType effective = new DateTimeType(observationDate.get());
                        report.setEffective(effective);
                    }

                    report.setPerformer(this.getPerformers(reportJson));

                    String conclusion = reportJson.getString("conclusion");
                    report.setConclusion(conclusion);

                    JSONObject examJson = allReports.getJSONObject(i).optJSONObject("RadExam");
                    if (examJson != null) {
                        String status = examJson.getString("status");
                        report.setStatus(getDiagnosticReportStatus(status));
                    }
                    result.add(report);
                }
            }
        }
        JSONArray allVisits = allJson.read("$.DxReportVisit");
        String patientName = "";
        String patientICN = "";

        if (allVisits != null) {
            for (int i = 0; i < allVisits.length(); i++) {
                JSONObject visitJson = allVisits.getJSONObject(i).getJSONObject("V POV");
                if (visitJson != null) {
                    if (StringUtils.isEmpty(patientName) && StringUtils.isEmpty(patientICN)) {
                        patientName = visitJson.getString("patientName");
                        patientICN = visitJson.getString("patientICN");
                        break;
                    }
                }
            }
        }

        for(DiagnosticReport report:result) {
            if (!StringUtils.isEmpty(patientName) && !StringUtils.isEmpty(patientICN)) {
                report.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, patientICN, patientName, ResourceHelper.ReferenceType.Patient));
            }
        }

        return result;
    }

    private List<DiagnosticReport.DiagnosticReportPerformerComponent> getPerformers(JSONObject reportJson) {

        List<DiagnosticReport.DiagnosticReportPerformerComponent> result = new ArrayList<>(1);

        DiagnosticReport.DiagnosticReportPerformerComponent component = new DiagnosticReport.DiagnosticReportPerformerComponent();
        String name = reportJson.getString("verifyingPhysicianName");
        Long id = reportJson.optLong("verifyingPhysicianId", HcConstants.MISSING_ID);
        String actorId = id.longValue() != HcConstants.MISSING_ID ? id.toString() : "";
        component.setActor(ResourceHelper.createReference(HcConstants.URN_VISTA_PRACTITIONER, actorId, name, ResourceHelper.ReferenceType.Practitioner));

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
            default:
                return DiagnosticReport.DiagnosticReportStatus.UNKNOWN;
        }
    }
}
