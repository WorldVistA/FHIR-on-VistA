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
package com.healthconcourse.vista.fhir.api.parser;

import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationParser {

    private static final Logger LOG = LoggerFactory.getLogger(LocationParser.class);
    public Optional<Location> parseSingleLocation(String httpData) {

        if(StringUtils.isEmpty(httpData)) {
            return Optional.empty();
        }

        String[] parts = httpData.split("\\^");

        if(parts.length != 2) {
            LOG.error("Bad data received from Vista");
            return Optional.empty();
        }

        if("-1".equalsIgnoreCase(parts[0])) {
            return Optional.empty();
        }

        Location result = new Location();
        result.setName(parts[0]);

        String[] fields = parts[1].split("\\|");

        result.setId(fields[0]);

        result.setPartOf(ResourceHelper.createReference("", fields[2], fields[1]));

        result.setAddress(createAddress(fields));

        if(fields.length >= 9) {
            result.setTelecom(createContact(fields[8]));
        }

        result.setMeta(ResourceHelper.getVistaMeta());

        return Optional.of(result);
    }

    private Address createAddress(String[] fields) {
        Address address = new Address();

        if(!fields[3].isEmpty()) {
            address.addLine(fields[3]);
        }

        if(!fields[4].isEmpty()) {
            address.addLine(fields[4]);
        }
        address.setCity(fields[5]);
        address.setState(fields[6]);
        address.setPostalCode(fields[7]);

        return address;
    }

    private List<ContactPoint> createContact(String input) {

        List<ContactPoint> result = new ArrayList<>();

        String[] records = input.split("\\*");

        for(String record: records) {
            String[] parts = record.split("_");

            ContactPoint contact = new ContactPoint();
            contact.setSystem(ContactPoint.ContactPointSystem.PHONE);
            contact.setValue(parts[1]);
            result.add(contact);
        }
        return result;
    }
}
