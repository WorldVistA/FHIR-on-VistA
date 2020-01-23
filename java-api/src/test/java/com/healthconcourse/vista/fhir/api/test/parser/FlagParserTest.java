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

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.parser.FlagParser;
import org.hl7.fhir.dstu3.model.Flag;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FlagParserTest {

    @Test
    public void TestSuccessfulFlagParse() {
        String input = "{ \"Flags\": [ { \"Flag\": { \"assignmentNarrative\": \"shows signs of potential violence \", \"flagIen\": 10, \"flagName\": \"BEHAVIORAL\", \"flagNameId\": \"1;DGPF(26.15,\", \"flagSCT\": 277843001, \"number\": 10, \"originatingSite\": \"ZZ ALBANY-PRRTP\", \"originatingSiteId\": 17007, \"ownerSite\": \"ZZ ALBANY-PRRTP\", \"ownerSiteId\": 17007, \"patientName\": \"TEN,PATIENT\", \"patientNameICN\": \"10110V004877\", \"patientNameId\": 8, \"resourceId\": \"V-500-26.13-10\", \"resourceType\": \"Flag\", \"reviewDate\": \"\", \"reviewDateFHIR\": \"\", \"reviewDateFM\": \"\", \"reviewDateHL7\": \"\", \"status\": \"INACTIVE\", \"statusCd\": 0 } } ] }";

        FlagParser parser = new FlagParser();

        List<Flag> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 1, result.size());
        Flag flag = result.get(0);
        Assert.assertEquals("Resource ID correct", "V-500-26.13-10", flag.getId());
        Assert.assertEquals("Correct ICN", "10110V004877", flag.getSubject().getIdentifier().getValue());
        Assert.assertEquals("Correct SNOMED Code", "277843001", flag.getCode().getCoding().get(0).getCode());
        Assert.assertEquals("Correct Status", Flag.FlagStatus.INACTIVE, flag.getStatus());
        Assert.assertEquals("Correct Location", "ZZ ALBANY-PRRTP", flag.getAuthor().getDisplay());
    }

    @Test
    public void TestFlagParseOrganizationBug() {
        String input = "{ \"Flags\": [ { \"Flag\": { \"assignmentNarrative\": \"shows signs of potential violence \", \"flagIen\": 10, \"flagName\": \"BEHAVIORAL\", \"flagNameId\": \"1;DGPF(26.15,\", \"flagSCT\": 277843001, \"number\": 10, \"originatingSite\": \"ZZ ALBANY-PRRTP\", \"originatingSiteId\": 17007, \"ownerSite\": \"ZZ ALBANY-PRRTP\", \"ownerSiteId\": 17007, \"patientName\": \"TEN,PATIENT\", \"patientNameICN\": \"10110V004877\", \"patientNameId\": 8, \"resourceId\": \"V-500-26.13-10\", \"resourceType\": \"Flag\", \"reviewDate\": \"\", \"reviewDateFHIR\": \"\", \"reviewDateFM\": \"\", \"reviewDateHL7\": \"\", \"status\": \"INACTIVE\", \"statusCd\": 0 } } ] }";

        FlagParser parser = new FlagParser();

        List<Flag> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 1, result.size());
        Flag flag = result.get(0);
        Assert.assertEquals("Resource ID correct", "V-500-26.13-10", flag.getId());
        Assert.assertEquals("Correct ICN", "10110V004877", flag.getSubject().getIdentifier().getValue());
        Assert.assertEquals("Correct SNOMED Code", "277843001", flag.getCode().getCoding().get(0).getCode());
        Assert.assertEquals("Correct Status", Flag.FlagStatus.INACTIVE, flag.getStatus());
        Assert.assertEquals("Correct Organization", "ZZ ALBANY-PRRTP", flag.getAuthor().getDisplay());
        Assert.assertEquals("Correct Type of Reference", true, flag.getAuthor().getReference().startsWith("/Organization"));
        Assert.assertEquals("Correct Author ID System", HcConstants.URN_VISTA_ORGANIZATION, flag.getAuthor().getIdentifier().getSystem());
    }

    @Test
    public void TestNoFlagParse() {
        String input = "";

        FlagParser parser = new FlagParser();

        List<Flag> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestEmptyAllergyParse() {
        String input = "{}";

        FlagParser parser = new FlagParser();

        List<Flag> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
