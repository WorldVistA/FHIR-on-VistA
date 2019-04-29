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
package com.healthconcourse.vista.fhir.api.service;

import ca.uhn.fhir.rest.api.MethodOutcome;
import com.healthconcourse.vista.fhir.api.parser.PatientParser;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;


import java.util.List;

public class VistaConditionService implements ConditionService {

    private VistaData service;

    public VistaConditionService(VistaData data) {
        service = data;
    }

    @Override
    public List<Patient> getPatientsByCode(String code) {

        String httpBody = service.getPatientsByCondition(code);

        PatientParser parser = new PatientParser();

        return parser.parseCodeList(httpBody);
    }

    @Override
    public MethodOutcome putCondition(Condition theCondition) {
        MethodOutcome methodOutcome = new MethodOutcome();

        // TODO: fix this
        String httpBody = service.putTheCondition(theCondition);

        String outcomeId = (theCondition.hasId()) ? theCondition.getId() : (theCondition.hasSubject() ? theCondition.getSubject().getReference() : "n/a");
        String outcomeIssueId = "n/a";
        String outcomeDetailsText = "Unknown error";
        String[] results = httpBody.split("\\^");
        if (results.length > 0) {
            if (results[0].equals("1")) {
                outcomeIssueId = results[0];
                outcomeDetailsText = "Success";
            } else if (results.length > 1) {
                outcomeIssueId = results[0];
                outcomeDetailsText = results[1];
            }
        }

        OperationOutcome outcome = new OperationOutcome();
        CodeableConcept cc = new CodeableConcept();
        cc.setText(outcomeDetailsText);
        outcome.addIssue().setDetails(cc).setId(outcomeIssueId);
        outcome.setId(outcomeId);

        methodOutcome.setOperationOutcome(outcome);

        return methodOutcome;
    }
}
