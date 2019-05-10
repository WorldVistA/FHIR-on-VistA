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

import com.healthconcourse.vista.fhir.api.service.VistaObservationService;
import com.healthconcourse.vista.fhir.api.test.mocks.MockVistaData;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Observation;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class VistaObservationServiceTest {



    @Test
    public void TestGetObservationsByCriteriaSuccess() {

        VistaObservationService service = new VistaObservationService(new MockVistaData());

        List<Observation> results = service.getObservationsByCriteria("5000001534V744140", new Date(), Enumerations.AdministrativeGender.FEMALE, "111-11-1111");

        Assert.assertTrue(results.size() == 40);

    }
}
