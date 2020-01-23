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
import com.healthconcourse.vista.fhir.api.service.VistaLocationService;
import com.healthconcourse.vista.fhir.api.test.mocks.MockVistaData;
import org.hl7.fhir.dstu3.model.Location;
import org.junit.Test;

import static org.junit.Assert.*;

public class VistaLocationServiceTest {

    @Test
    public void testFindLocationSuccess() {
        VistaLocationService service = new VistaLocationService(new MockVistaData());

        Location location = service.findLocationByName("ALBANY%20FILE%20ROOM");

        assertNotNull(location);
        assertNotNull(location.getAddress());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testFindLocationFail() {
        VistaLocationService service = new VistaLocationService(new MockVistaData());

        Location location = service.findLocationByName("nowhere");
    }

}
