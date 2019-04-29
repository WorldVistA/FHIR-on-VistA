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

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthconcourse.vista.fhir.api.parser.LocationParser;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.dstu3.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class VistaLocationService implements LocationService {

    private static final Logger LOG = LoggerFactory.getLogger(VistaLocationService.class);
    private VistaData service;

    public VistaLocationService(VistaData data) {
        this.service = data;
    }

    @Override
    public Location findLocationByName(String name) {
        String httpBody = service.getLocationByName(name);

        LocationParser parser = new LocationParser();

        Optional<Location> location = parser.parseSingleLocation(httpBody);

        if(location.isPresent()) {
            return  location.get();
        } else {
            LOG.error(String.format("Unable to parse location for %s", name));
            throw new ResourceNotFoundException(name);
        }
    }
}
