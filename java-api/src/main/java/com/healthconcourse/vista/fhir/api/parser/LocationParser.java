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

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import com.nfeld.jsonpathlite.JsonPath;
import com.nfeld.jsonpathlite.JsonResult;
import org.hl7.fhir.dstu3.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationParser {

    private static final Logger LOG = LoggerFactory.getLogger(LocationParser.class);

    public Optional<Location> parseSingleLocation(String httpData) {
        if (StringUtils.isEmpty(httpData)) {
            LOG.info("No data found for location");
            return Optional.empty();
        }

        JsonResult allJson = JsonPath.parseOrNull(httpData);
        if (allJson == null) {
            LOG.warn("Unable to parse Location JSON");
            return Optional.empty();
        } else {
            JSONArray locationList = allJson.read("$.Location");
            if (locationList != null) {
                JSONObject json = locationList.getJSONObject(0).optJSONObject("Hosploc");
                if (json != null) {
                    Location result = new Location();
                    result.setMeta(ResourceHelper.getVistaMeta());
                    String resourceId = json.getString("resourceId");
                    result.setId(resourceId);
                    String name = json.getString("name");
                    result.setName(name);
                    Long phone = json.optLong("telephone", HcConstants.MISSING_ID);
                    if (phone != HcConstants.MISSING_ID) {
                        result.setTelecom(this.createContact(phone.toString()));
                    }
                    JSONObject institutionJSON = locationList.getJSONObject(0).optJSONObject("institution");
                    if (institutionJSON != null) {
                        Address address = new Address();
                        String street1 = institutionJSON.getString("streetAddr1");
                        if (!StringUtils.isEmpty(street1)) {
                            address.addLine(street1);
                            String street2 = institutionJSON.getString("streetAddr2");
                            if (!StringUtils.isEmpty(street2)) {
                                address.addLine(street2);
                            }
                        }
                        String city = institutionJSON.getString("city");
                        if (!StringUtils.isEmpty(city)) {
                            address.setCity(city);
                        }
                        String state = institutionJSON.getString("state");
                        if (!StringUtils.isEmpty(state)) {
                            address.setState(state);
                        }
                        String postal = institutionJSON.getString("zip");
                        if (!StringUtils.isEmpty(postal)) {
                            address.setPostalCode(postal);
                        }
                        result.setAddress(address);

                        String site = institutionJSON.getString("siteName");
                        if (!StringUtils.isEmpty(site)) {
                            String resourceID = institutionJSON.getString("resourceId");
                            result.setManagingOrganization(ResourceHelper.createReference(HcConstants.URN_VISTA_ORGANIZATION, resourceID, site, ResourceHelper.ReferenceType.Organization));
                        }
                    }
                    return Optional.of(result);
                } else {
                    return Optional.empty();
                }

            } else {
                LOG.warn("Error parsing Location JSON");
                return Optional.empty();
            }
        }
    }

    private List<ContactPoint> createContact(String input) {
        List<ContactPoint> result = new ArrayList<>();
        ContactPoint contact = new ContactPoint();
        contact.setSystem(ContactPoint.ContactPointSystem.PHONE);
        contact.setValue(input);
        result.add(contact);
        return result;
    }
}
