/* Created by Perspecta http://www.perspecta.com */
/*
(c) 2017-2019 Perspecta

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.healthconcourse.vista.fhir.api.test.parser;

import com.healthconcourse.vista.fhir.api.parser.FlagParser;
import org.hl7.fhir.dstu3.model.Flag;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FlagParserTest {

    @Test
    public void TestSuccesfulAllergyParse() {
        String input = "5000000023V897100^FALL RISK|129839007|116|Local|ACTIVE|CAMP MASTER|CAMP MASTER|^IDENTITY THEFT|-99999999999999|118|Local|ACTIVE|CAMP MASTER|CAMP MASTER|^WANDERER|704448006|117|Local|ACTIVE|CAMP MASTER|CAMP MASTER|20151220";

        FlagParser parser = new FlagParser();

        List<Flag> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 3, result.size());
    }

    @Test
    public void TestNoAllergyParse() {
        String input = "5000000325V783252";

        FlagParser parser = new FlagParser();

        List<Flag> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestEmptyAllergyParse() {
        String input = "";

        FlagParser parser = new FlagParser();

        List<Flag> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
