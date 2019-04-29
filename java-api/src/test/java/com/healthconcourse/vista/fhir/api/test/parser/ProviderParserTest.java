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

import com.healthconcourse.vista.fhir.api.data.Provider;
import com.healthconcourse.vista.fhir.api.parser.ProviderParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class ProviderParserTest {

    @Test
    public void TestSuccesfulProviderParse() {
        String input = "5000001533V676621^201311190800-0500|11525|PROVIDER,FIVE|Physician|PRIMARY^201311190924-0500|11537|PROVIDER,SIXTEEN|Physician|SECONDARY^201311190927-0500|11527|PROVIDER,SIXTEEN|Physician|SECONDARY^201409221300-0500|11531|PROVIDER,FIVE|Physician|PRIMARY^201409291257-0500|11538|PROVIDER,SIXTEEN|Physician|SECONDARY^201503011300-0500|11533|PROVIDER,FIVE|Physician|PRIMARY^201503131344-0500|11539|PROVIDER,SIXTEEN|Physician|SECONDARY^201507130821-0500|11615|NURSE,EIGHT|NURSE|PRIMARY^201507200839-0500|11616|PROVIDER,SIXTEEN|Physician|SECONDARY";

        ProviderParser parser = new ProviderParser();

        List<Provider> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 9, result.size());
    }

    @Test
    public void TestProviderParseBlankInput() {
        String input = "";

        ProviderParser parser = new ProviderParser();

        List<Provider> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestProviderParseNullInput() {

        ProviderParser parser = new ProviderParser();

        List<Provider> result = parser.parseList(null);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
