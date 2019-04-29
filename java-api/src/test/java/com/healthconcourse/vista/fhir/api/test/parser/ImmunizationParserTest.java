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

import com.healthconcourse.vista.fhir.api.parser.ImmunizationParser;
import org.hl7.fhir.dstu3.model.Immunization;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ImmunizationParserTest {

    @Test
    public void TestSuccesfulImmunizationParse() {
        String input = "10110V004877^442_9000010.11_8_54|20000405112729-0500|109|PNEUMOCOCCAL, UNSPECIFIED FORMULATION|;||FT. LOGAN|;|^442_9000010.11_8_815|20040325083104-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_86|20130602|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;|||;|^442_9000010.11_8_74|20130827141422-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_75|20130828140108-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_78|20130828143713-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_79|20130828145435-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_81|20130910091630-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_83|20130918161830-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_85|20130926174559-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_668|20140320175526-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_669|20140320175809-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_965|20140516094757-0500|26|CHOLERA, ORAL (HISTORICAL)|;||CAMP MASTER|;|^442_9000010.11_8_966|20140516094757-0500|28|DT (PEDIATRIC)|1;SERIES 1||CAMP MASTER|2;IRRITABILITY|^442_9000010.11_8_846|20140516094835-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|";

        ImmunizationParser parser = new ImmunizationParser();

        List<Immunization> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 15, result.size());
    }

    @Test
    public void TestFailedImmunizatinonParse() {
        String input = "-1^Patient identifier not recognised";

        ImmunizationParser parser = new ImmunizationParser();

        List<Immunization> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestImmunizatinonParseNoData() {
        String input = "";

        ImmunizationParser parser = new ImmunizationParser();

        List<Immunization> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestFailedImmunizatinonParseGarbageData() {
        String input = "adseqwdfsa4565eraser;aers ";

        ImmunizationParser parser = new ImmunizationParser();

        List<Immunization> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
