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
package com.healthconcourse.vista.fhir.api.test.service;

import com.healthconcourse.vista.fhir.api.service.VistaConditionService;
import com.healthconcourse.vista.fhir.api.test.mocks.MockVistaData;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class VistaConditionServiceTest {

    @Test
    public void TestGetPatientsByCodeSuccess() {
        VistaConditionService service = new VistaConditionService(new MockVistaData());

        List<Patient> results = service.getPatientsByCode("38341003");

        Assert.assertTrue(results.size() == 5);
    }

    @Test
    public void TestGetConditionsByIdSuccess() {
        VistaConditionService service = new VistaConditionService(new MockVistaData());

        List<Condition> results = service.getConditionsByPatient("5000001533V676621");

        Assert.assertTrue(results.size() == 3);
    }

    @Test
    public void TestGetConditionsByIdFailure() {
        VistaConditionService service = new VistaConditionService(new MockVistaData());

        List<Condition> results = service.getConditionsByPatient("444");

        Assert.assertTrue(results.size() == 0);
    }

    @Test
    public void TestGetPatientsByCodeFailure() {
        VistaConditionService service = new VistaConditionService(new MockVistaData());

        List<Patient> results = service.getPatientsByCode("12345");

        Assert.assertTrue(results.size() == 0);
    }
}
