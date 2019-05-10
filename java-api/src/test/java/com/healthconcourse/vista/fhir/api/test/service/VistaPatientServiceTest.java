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

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthconcourse.vista.fhir.api.service.PatientService;
import com.healthconcourse.vista.fhir.api.service.VistaPatientService;
import com.healthconcourse.vista.fhir.api.test.mocks.MockVistaData;
import org.hl7.fhir.dstu3.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VistaPatientServiceTest {

    private final PatientService service = new VistaPatientService(new MockVistaData());

    @Test
    public void TestGetByIdSuccess () throws ParseException {

        Patient result = service.getPatientById("5000001534V744140");

        Assert.assertNotNull("Got a patient object back", result);

        List<HumanName> names = result.getName();

        Assert.assertEquals("LastName is HYPERTENSION", "HYPERTENSION", names.get(0).getFamily());
        Assert.assertEquals("FirstName is PATIENT FEMALE", "PATIENT FEMALE", names.get(0).getGiven().get(0).getValue());

        Assert.assertEquals("ID is ICN", "5000001534V744140", result.getIdentifier().get(0).getValue());
        Assert.assertEquals("SSN is 555-555-1938", "555-555-1938", result.getIdentifier().get(1).getValue());
        Assert.assertEquals("Gender is FEMALE", Enumerations.AdministrativeGender.FEMALE, result.getGender());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        Date expectedDate = sdf.parse("13-9-1928");

        Assert.assertEquals("Birthdate is 9/13/1928", expectedDate, result.getBirthDate());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void TestGetByIdFailure () {

        Patient result = service.getPatientById("NotAnICN");
    }

    @Test
    public void TestGetByCriteraSuccess () {

        List<Patient> results = service.findPatient("name", new Date(), "555-555-1938", Enumerations.AdministrativeGender.FEMALE);

        Assert.assertEquals(1, results.size());
    }

    @Test
    public void TestGetByCriteraFailure () {

        List<Patient> results = service.findPatient("name", new Date(), "ssn", Enumerations.AdministrativeGender.FEMALE);

        Assert.assertEquals(0, results.size());
    }

    @Test
    public void TestGetConditionsForPatientFailure () {

        List<Condition> results = service.getCodesByIcn("444");

        Assert.assertEquals(0, results.size());
    }

    @Test
    public void TestGetConditionsForPatientSuccess () {

        List<Condition> results = service.getCodesByIcn("5000001534V744140");

        Assert.assertEquals(7, results.size());
    }

    @Test
    public void TestGetObservationsByIcnNoData() {

        List<Observation> results = service.getObservationsByIcn("444");

        Assert.assertEquals(0, results.size());

    }

    @Test
    public void TestGetObservationsByIcnSuccess() {

        List<Observation> results = service.getObservationsByIcn("5000001534V744140");

        Assert.assertEquals(74, results.size());

    }

    @Test
    public void TestGetObservationsByIcnAndCodeNoData() {

        List<Observation> results = service.getObservationsByIcnAndCode("444", "code");

        Assert.assertEquals(0, results.size());

    }

    @Test
    public void TestGetObservationsByIcnAndCodeSuccess() {

        List<Observation> results = service.getObservationsByIcnAndCode("5000001534V744140", "code");

        Assert.assertEquals(40, results.size());

    }

}
