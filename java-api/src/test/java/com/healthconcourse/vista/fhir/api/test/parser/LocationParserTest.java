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
package com.healthconcourse.vista.fhir.api.test.parser;

import com.healthconcourse.vista.fhir.api.parser.LocationParser;
import org.hl7.fhir.dstu3.model.Location;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocationParserTest {

    @Test
    public void parseLocationSuccess() {

        String input = "ALBANY FILE ROOM^77|ABILENE (CAA)|17034|11850 NW TEST LANE|SUITE 550|ABILENE|KANSAS|67410|ROB DAVIS_954-748-9999*DAVID TRUXALL_954-749-9999";

        LocationParser parser = new LocationParser();

        Optional<Location> result = parser.parseSingleLocation(input);

        assertTrue(result.isPresent());

        Location loc = result.get();

        assertEquals(loc.getName(), "ALBANY FILE ROOM");
        assertEquals(loc.getId(), "77");
        assertEquals(loc.getTelecom().size(), 2);
    }

    @Test
    public void parseLocationNoLine2OneContact() {

        String input = "ALBANY FILE ROOM^77|ABILENE (CAA)|17034|11850 NW TEST LANE||ABILENE|KANSAS|67410|ROB DAVIS_954-748-9999";

        LocationParser parser = new LocationParser();

        Optional<Location> result = parser.parseSingleLocation(input);

        assertTrue(result.isPresent());

        Location loc = result.get();

        assertEquals(loc.getName(), "ALBANY FILE ROOM");
        assertEquals(loc.getId(), "77");
        assertEquals(loc.getTelecom().size(), 1);
    }


    @Test
    public void parseLocationMissing() {

        String input = "";

        LocationParser parser = new LocationParser();

        Optional<Location> result = parser.parseSingleLocation(input);

        assertFalse(result.isPresent());
    }

    @Test
    public void parseLocationBadData() {

        String input = "ThisIsNotDataFromVista As Plainly displayed";

        LocationParser parser = new LocationParser();

        Optional<Location> result = parser.parseSingleLocation(input);

        assertFalse(result.isPresent());
    }
}
