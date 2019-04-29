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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Flag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FlagParser implements VistaParser<Flag> {

    private static final Logger LOG = LoggerFactory.getLogger(FlagParser.class);

    @Override
    public List<Flag> parseList(String httpData) {

        List<Flag> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.split("\\^");

        if(records.length < 2) {
            LOG.error("No flag data received from Vista");
            return result;
        }

        String icn = records[0];

        for(int i = 1; i < records.length; i++) {

            String[] fields = records[i].split("\\|");

            Flag flag =  new Flag();

            flag.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, fields[1], fields[0]));

            flag.setId(fields[2]);

            flag.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, "", ResourceHelper.ReferenceType.Patient));

            Flag.FlagStatus status = Flag.FlagStatus.ACTIVE;

            if(!fields[4].equalsIgnoreCase("ACTIVE")) {
                status = Flag.FlagStatus.INACTIVE;
            }

            flag.setStatus(status);

            flag.setAuthor(ResourceHelper.createReference(HcConstants.URN_VISTA_ORGANIZATION, fields[5], ""));

            flag.setMeta(ResourceHelper.getVistaMeta());

            result.add(flag);
        }

        return result;
    }
}
