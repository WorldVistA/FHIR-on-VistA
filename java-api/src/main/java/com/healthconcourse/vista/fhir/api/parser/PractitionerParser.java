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

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import com.nfeld.jsonpathlite.JsonPath;
import com.nfeld.jsonpathlite.JsonResult;
import org.hl7.fhir.dstu3.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;

public class PractitionerParser implements VistaParser {

    private static final Logger LOG = LoggerFactory.getLogger(PractitionerParser.class);

    public Optional<Practitioner> parseSingle(String httpData) {

        if (StringUtils.isEmpty(httpData)) {
            return Optional.empty();
        }

        JsonResult practitionerJSON = JsonPath.parseOrNull(httpData);
        if (practitionerJSON == null) {
            LOG.warn("Unable to parse single Practitioner JSON");
            return Optional.empty();
        }

        JSONObject raw = practitionerJSON.read("$.Np");

        Practitioner practitioner = new Practitioner();
        practitioner.setMeta(ResourceHelper.getVistaMeta());
        String resourceId = raw.getString("resourceId");
        practitioner.setId(resourceId);
        practitioner.addIdentifier();
        practitioner.getIdentifier().get(0).setSystem(HcConstants.URN_VISTA_PRACTITIONER);
        practitioner.getIdentifier().get(0).setValue(resourceId);

        String rawName = raw.getString("name");
        practitioner.setName(this.parseName(rawName));

        String gender = raw.getString("sex");
        if (!StringUtils.isEmpty(gender)) {
            Enumerations.AdministrativeGender parsedGender = this.getGender(gender);
            if (parsedGender != Enumerations.AdministrativeGender.NULL) {
                practitioner.setGender(parsedGender);
            }
        }

        String streetAddress1 = raw.getString("streetAddress1");
        if (!StringUtils.isEmpty(streetAddress1)) {
            Address address = new Address();
            address.addLine(streetAddress1);

            String streetAddress2 = raw.getString("streetAddress2");
            if (!StringUtils.isEmpty(streetAddress2)) {
                address.addLine(streetAddress2);
            }
            String streetAddress3 = raw.getString("streetAddress3");
            if (!StringUtils.isEmpty(streetAddress3)) {
                address.addLine(streetAddress3);
            }
            String city = raw.getString("city");
            if (!StringUtils.isEmpty(city)) {
                address.setCity(city);
            }
            String state = raw.getString("state");
            if (!StringUtils.isEmpty(state)) {
                address.setState(state);
            }
            Long zip = raw.optLong("zipCode", HcConstants.MISSING_ID);
            if (zip.longValue() != HcConstants.MISSING_ID) {
                address.setPostalCode(zip.toString());
            }
            List<Address> addressList = new ArrayList<>();
            addressList.add(address);
            practitioner.setAddress(addressList);
        }

        String dob = raw.getString("dobFHIR");
        Optional<Date> birthDate = InputValidator.parseAnyDate(dob);
        if (birthDate.isPresent()) {
            practitioner.setBirthDate(birthDate.get());
        }

        boolean active = false;
        String inactiveDate = raw.getString("inactiveDateFHIR");
        String terminatinonDate = raw.getString("terminationDateFHIR");
        if (StringUtils.isEmpty(inactiveDate) && StringUtils.isEmpty(terminatinonDate)) {
            active = true;
        }
        practitioner.setActive(active);

        return Optional.of(practitioner);
    }

    private Enumerations.AdministrativeGender getGender(String rawGender) {
        if (rawGender.equalsIgnoreCase("F")) {
            return Enumerations.AdministrativeGender.FEMALE;
        } else if (rawGender.equalsIgnoreCase("M")) {
            return Enumerations.AdministrativeGender.FEMALE;
        } else {
            return Enumerations.AdministrativeGender.NULL;
        }
    }

    /**
     * Helper function for parsing names from VistA
     * @param totalName The name string that needs to be broken up. Expects "Lastname,FirstName"
     * @return A list of FHIR HumanName
     */
    private List<HumanName> parseName(String totalName) {
        String[] nameParts = totalName.split(",");
        if (nameParts.length == 2) {
            HumanName name = new HumanName();
            name.setFamily(nameParts[0]);
            List<StringType> given = new ArrayList<>(1);
            StringType givenName = new StringType();
            givenName.setValue(nameParts[1]);
            given.add(givenName);
            name.setGiven(given);
            List<HumanName> allNames = new ArrayList<>(1);
            allNames.add(name);
            return allNames;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Practitioner> parseList(String httpData) {
        HashMap<String, Practitioner> result = new HashMap<>();

        if (StringUtils.isEmpty(httpData)) {
            return new ArrayList<>();
        }

        JsonResult practitionerJSON = JsonPath.parseOrNull(httpData);
        if (practitionerJSON == null) {
            LOG.warn("Unable to parse practitioner list JSON");
            return new ArrayList<>();
        }

        JSONArray allPractitioners = practitionerJSON.read("$.EncProv..Vprov");
        if (allPractitioners != null) {
            for (int i = 0; i < allPractitioners.length(); i++) {
                JSONObject json = allPractitioners.getJSONObject(i);
                String resourceId = json.getString("providerResId");
                if (!result.containsKey(resourceId)) {
                    Practitioner practitioner = new Practitioner();
                    practitioner.setMeta(ResourceHelper.getVistaMeta());
                    practitioner.setId(resourceId);

                    String totalName = json.getString("provider");
                    practitioner.setName(this.parseName(totalName));
                    result.put(resourceId,practitioner);
                }
            }
        }
        Collection<Practitioner> values = result.values();
        return new ArrayList<>(values);
    }
}
