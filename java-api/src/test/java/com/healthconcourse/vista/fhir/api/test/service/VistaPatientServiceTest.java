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

        Patient result = service.getPatientById("5000000352V586511");

        Assert.assertNotNull("Got a patient object back", result);

        List<HumanName> names = result.getName();

        Assert.assertEquals("LastName is EHMP", "EHMP", names.get(0).getFamily());
        Assert.assertEquals("FirstName is FIVE", "FIVE", names.get(0).getGiven().get(0).getValue());

        Assert.assertEquals("ID is ICN", "5000000352V586511", result.getIdentifier().get(0).getValue());
        Assert.assertEquals("SSN is 666110005", "666110005", result.getIdentifier().get(1).getValue());
        Assert.assertEquals("Gender is MALE", Enumerations.AdministrativeGender.MALE, result.getGender());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        Date expectedDate = sdf.parse("03-3-1960");

        Assert.assertEquals("Birthdate is 3/3/1960", expectedDate, result.getBirthDate());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void TestGetByIdFailure () {

        Patient result = service.getPatientById("NotAnICN");
    }

    @Test
    public void TestGetConditionsForPatientFailure () {

        List<Condition> results = service.getCodesByIcn("444");

        Assert.assertEquals(0, results.size());
    }

    @Test
    public void TestGetConditionsForPatientSuccess () {

        List<Condition> results = service.getCodesByIcn("5000001534V744140");

        Assert.assertEquals(3, results.size());
    }

    @Test
    public void TestGetObservationsByIcnNoData() {

        List<Observation> results = service.getObservationsByIcn("444");

        Assert.assertEquals(0, results.size());

    }

    @Test
    public void TestGetObservationsByIcnSuccess() {

        List<Observation> results = service.getObservationsByIcn("5000001534V744140");

        Assert.assertEquals(18, results.size());

    }
}
