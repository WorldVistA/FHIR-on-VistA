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

import com.healthconcourse.vista.fhir.api.parser.AllergyParser;
import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AllergyParserTest {

    @Test
    public void TestSuccesfulAllergyParse() {
        String input = "10110V004877^PENICILLIN|SCT:6369005|20130914|environment|confirmed|442_120.8_967~RASH:112625008~ITCHING,WATERING EYES:74776002^AMOXICILLIN|NDFRT:N0000005840~NDFRT:N0000145985~RXNORM:723~VANDF:4017605|20130914|environment|confirmed|442_120.8_967~DIARRHEA:62315008^CEFAZOLIN|NDFRT:N0000005828~NDFRT:N0000147751~RXNORM:2180~VANDF:4019659|20130914|environment|confirmed|442_120.8_967~HIVES:247472004^CITRUS|SCT:78161008|20130914|environment|confirmed|442_120.8_967~HIVES:247472004^SULFAMETHOXAZOLE|NDFRT:N0000006904~NDFRT:N0000146102~RXNORM:10180~VANDF:4017734|20130914|environment|confirmed|442_120.8_967~DIARRHEA:62315008^CHOCOLATE|SCT:102262009|20130914|environment|confirmed|442_120.8_967~DIARRHEA:62315008^CHLORPROMAZINE|NDFRT:N0000006947~NDFRT:N0000146214~RXNORM:2403~VANDF:4017861|20130914|environment|confirmed|442_120.8_967~DIARRHEA:62315008^BEE STINGS|SCT:157930002|20130914|environment|confirmed|442_120.8_967~ANAPHYLAXIS:39579001^ASPIRIN|NDFRT:N0000006582~NDFRT:N0000145918~RXNORM:1191~VANDF:4017536|20130914|environment|confirmed|442_120.8_967~BREAST ENGORGEMENT:49746001";

        AllergyParser parser = new AllergyParser();

        List<AllergyIntolerance> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 9, result.size());
    }

    @Test
    public void TestFailedllergyParse() {
        String input = "-1^Patient identifier not recognised";

        AllergyParser parser = new AllergyParser();

        List<AllergyIntolerance> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestFailedllergyParseNoInput() {
        String input = "";

        AllergyParser parser = new AllergyParser();

        List<AllergyIntolerance> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestFailedllergyParseGarbageInput() {
        String input = "asdfewrqadfsdsa ewf afdd";

        AllergyParser parser = new AllergyParser();

        List<AllergyIntolerance> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
