/* Created by Perspecta http://www.perspecta.com */
/*
(c) 2017-2019 Perspecta

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.healthconcourse.vista.fhir.api.test.service;

import com.healthconcourse.vista.fhir.api.service.VistaConditionService;
import com.healthconcourse.vista.fhir.api.test.mocks.MockVistaData;
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
    public void TestGetPatientsByCodeFailure() {

        VistaConditionService service = new VistaConditionService(new MockVistaData());

        List<Patient> results = service.getPatientsByCode("12345");

        Assert.assertTrue(results.size() == 0);

    }
}
