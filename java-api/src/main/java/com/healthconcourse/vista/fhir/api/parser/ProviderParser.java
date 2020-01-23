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

import com.healthconcourse.vista.fhir.api.data.Provider;
import com.nfeld.jsonpathlite.JsonPath;
import com.nfeld.jsonpathlite.JsonResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ProviderParser {
    private static final Logger LOG = LoggerFactory.getLogger(ProviderParser.class);

    public List<Provider> parseList(String httpData) {
        HashMap<String, Provider> result = new HashMap<>();

        if (StringUtils.isEmpty(httpData)) {
            return new ArrayList<Provider>();
        }

        JsonResult practitionerJSON = JsonPath.parseOrNull(httpData);
        if (practitionerJSON == null) {
            LOG.warn("Unable to parse ConditionJSON");
            return new ArrayList<Provider>();
        }

        JSONArray allPractitioners = practitionerJSON.read("$.EncProv..Vprov");
        if (allPractitioners != null) {
            for (int i = 0; i < allPractitioners.length(); i++) {
                JSONObject json = allPractitioners.getJSONObject(i);
                Provider provider = new Provider();
                String resourceId = json.getString("providerResId");

                if (!result.containsKey(resourceId)) {
                    provider.setVistaId(resourceId);

                    String name = json.getString("provider");
                    provider.setName(name);

                    String primary = json.getString("primarySecondary");
                    if (primary.equalsIgnoreCase("PRIMARY")) {
                        provider.setPrimary(true);
                    } else {
                        provider.setPrimary(false);
                    }

                    String role = json.getString("provRole");
                    provider.setRole(role);

                    String encounterId = json.getString("visitResId");
                    if (!StringUtils.isEmpty(encounterId)) {
                        provider.setEncounter(encounterId);
                    }

                    result.put(resourceId, provider);
                }
            }
        }
        Collection<Provider> values = result.values();
        ArrayList<Provider> providerList = new ArrayList<>(values);
        return providerList;
    }
}
