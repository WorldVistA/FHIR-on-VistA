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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProviderParser {

    public List<Provider> parseList(String httpData) {
        List<Provider> result = new ArrayList<>();

        if(StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.trim().split("\\^");

        if(records.length < 2) {
            return result;
        }

        for(int i = 1; i < records.length; i++) {

            String[] fields = records[i].split("\\|");

            Provider provider = findProvider(result, fields[0]);
            if(provider.getVistaId().isEmpty()) {
                provider.setVistaId(fields[0]);
                provider.setName(fields[2]);
                provider.setRole(fields[3]);
                if("PRIMARY".equalsIgnoreCase(fields[4])){
                    provider.setPrimary(true);
                }

            }
            provider.setEncounter(fields[1]);
            provider.getEncounters().add(fields[1]);

            result.add(provider);
        }

        return result;

    }

    private Provider findProvider(List<Provider> data, String id) {
        for(Provider item: data) {
            if(item.getVistaId().equalsIgnoreCase(id)) {
                return item;
            }
        }

        return new Provider();
    }
}
