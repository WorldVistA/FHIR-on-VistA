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
import com.healthconcourse.vista.fhir.api.parser.PractitionerParser;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class VistaPractitionerService implements PractitionerService {

    private static final Logger LOG = LoggerFactory.getLogger(VistaPatientService.class);
    private VistaData service;

    public VistaPractitionerService(VistaData data) {
        service = data;
    }

    @Override
    public Practitioner getPractitionerById(String id) {
        String httpBody = service.getPractitionerById(id);

        PractitionerParser parser = new PractitionerParser();

        Optional<Practitioner> practitioner = parser.parseSingle(httpBody);

        if (practitioner.isPresent()) {
            return practitioner.get();
        } else {
            LOG.info(String.format("Practitioner not found for %s", id));
            throw new ResourceNotFoundException(id);
        }
    }
}
