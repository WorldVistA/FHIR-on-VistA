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
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.dstu3.model.Practitioner;

import java.util.ArrayList;
import java.util.List;

public class PractitionerParser implements VistaParser {

    @Override
    public List<Practitioner> parseList(String httpData) {

        List<Practitioner> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.trim().split("\\^");

        if(records.length < 2 || records[0].equals("-1")) {
            return result;
        }

        for(int i = 1; i < records.length; i++) {
            Practitioner practitioner = new Practitioner();

            String[] fields = records[i].split("\\|");

            practitioner.setId(fields[0]);

            practitioner.setMeta(ResourceHelper.getVistaMeta());

            result.add(practitioner);
        }

        return result;
    }
}
