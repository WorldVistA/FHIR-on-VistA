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

import com.healthconcourse.vista.fhir.api.parser.GoalParser;
import org.hl7.fhir.dstu3.model.Goal;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class GoalParserTest {

    @Test
    public void TestSuccesfulGoalParse() {
        String input = "10104V248233^442_216.8_229_31_19970603080702-0500|sleeps 7-9 hours without awakening|19970617|WARDCLERK,FIFTYTHREE|0;CURRENT^442_216.8_229_31_19970603080705-0500|verbalizes less trouble falling asleep|19970617|WARDCLERK,FIFTYTHREE|0;CURRENT";

        GoalParser parser = new GoalParser();

        List<Goal> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 2, result.size());
    }

    @Test
    public void TestInvalidGoalParse() {
        String input = "10104V248233^442_216.8_229_31_19970603080702-0500|";

        GoalParser parser = new GoalParser();

        List<Goal> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestEmptyGoalParse() {
        String input = "";

        GoalParser parser = new GoalParser();

        List<Goal> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
