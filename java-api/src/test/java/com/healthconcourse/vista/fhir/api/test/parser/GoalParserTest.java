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

import com.healthconcourse.vista.fhir.api.parser.GoalParser;
import org.hl7.fhir.dstu3.model.Goal;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class GoalParserTest {

    @Test
    public void TestSuccessfulGoalParse() {
        String input = "{ \"NCPS\": [ { \"NurseCarePlan\": { \"evaluationDates\": { \"evaluationDate\": [ { \"dateTimeEntered\": \"FEB 12,1993@10:19:50\", \"dateTimeEnteredFHIR\": \"1993-02-12T10:19:50-05:00\", \"dateTimeEnteredFM\": 2930212.10195, \"dateTimeEnteredHL7\": \"19930212101950-0500\", \"evaluationDate\": \"MAY 13,1993\", \"evaluationDateFHIR\": \"1993-05-13\", \"evaluationDateFM\": 2930513, \"evaluationDateHL7\": 19930513, \"problem\": \"Depressive Behavior\", \"problemId\": 1000344, \"problemStatus\": \"EVALUATE\", \"problemStatusCd\": 0, \"resourceId\": \"V-500-216.8-6-216.82-1\", \"userWhoEvaluated\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEvaluatedId\": 10958, \"userWhoEvaluatedNPI\": \"\" }, { \"dateTimeEntered\": \"FEB 12,1993@10:22:19\", \"dateTimeEnteredFHIR\": \"1993-02-12T10:22:19-05:00\", \"dateTimeEnteredFM\": 2930212.102219, \"dateTimeEnteredHL7\": \"19930212102219-0500\", \"evaluationDate\": \"APR 13,1993\", \"evaluationDateFHIR\": \"1993-04-13\", \"evaluationDateFM\": 2930413, \"evaluationDateHL7\": 19930413, \"problem\": \"Substance Abuse, Drugs\", \"problemId\": 1000491, \"problemStatus\": \"EVALUATE\", \"problemStatusCd\": 0, \"resourceId\": \"V-500-216.8-6-216.82-2\", \"userWhoEvaluated\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEvaluatedId\": 10958, \"userWhoEvaluatedNPI\": \"\" } ] }, \"ncpIen\": 6, \"ordersInfo\": { \"orderInfo\": [ { \"resourceId\": \"V-500-216.8-6-216.84-1\", \"status\": \"\", \"statusCd\": \"\", \"userModifying\": \"WARDCLERK,FIFTYTHREE\", \"userModifyingId\": 10958, \"userModifyingNPI\": \"\" }, { \"resourceId\": \"V-500-216.8-6-216.84-2\", \"status\": \"\", \"statusCd\": \"\", \"userModifying\": \"WARDCLERK,FIFTYTHREE\", \"userModifyingId\": 10958, \"userModifyingNPI\": \"\" }, { \"resourceId\": \"V-500-216.8-6-216.84-3\", \"status\": \"\", \"statusCd\": \"\", \"userModifying\": \"WARDCLERK,FIFTYTHREE\", \"userModifyingId\": 10958, \"userModifyingNPI\": \"\" }, { \"resourceId\": \"V-500-216.8-6-216.84-4\", \"status\": \"\", \"statusCd\": \"\", \"userModifying\": \"WARDCLERK,FIFTYTHREE\", \"userModifyingId\": 10958, \"userModifyingNPI\": \"\" }, { \"resourceId\": \"V-500-216.8-6-216.84-5\", \"status\": \"\", \"statusCd\": \"\", \"userModifying\": \"WARDCLERK,FIFTYTHREE\", \"userModifyingId\": 10958, \"userModifyingNPI\": \"\" }, { \"resourceId\": \"V-500-216.8-6-216.84-6\", \"status\": \"\", \"statusCd\": \"\", \"userModifying\": \"WARDCLERK,FIFTYTHREE\", \"userModifyingId\": 10958, \"userModifyingNPI\": \"\" }, { \"resourceId\": \"V-500-216.8-6-216.84-7\", \"status\": \"\", \"statusCd\": \"\", \"userModifying\": \"WARDCLERK,FIFTYTHREE\", \"userModifyingId\": 10958, \"userModifyingNPI\": \"\" } ] }, \"patient\": \"ZZZRETIREDTWENTYFIVE,PATIENT\", \"patientICN\": \"1967316818V742124\", \"patientId\": 138, \"problemLists\": { \"problemList\": [ { \"problem\": \"Depressive Behavior\", \"problemId\": 1000344, \"resourceId\": \"V-500-216.8-6-216.81-1\" }, { \"problem\": \"Substance Abuse, Drugs\", \"problemId\": 1000491, \"resourceId\": \"V-500-216.8-6-216.81-2\" } ] }, \"resourceId\": \"V-500-216.8-6\", \"resourceType\": \"NurseCarePlan\", \"targetDates\": { \"targetDate\": [ { \"dateTimeEntered\": \"FEB 12,1993@10:18:40\", \"dateTimeEnteredFHIR\": \"1993-02-12T10:18:40-05:00\", \"dateTimeEnteredFM\": 2930212.10184, \"dateTimeEnteredHL7\": \"19930212101840-0500\", \"goalExpectedOutcome\": \"attends and participates in assigned groups\", \"goalExpectedOutcomeId\": 1001985, \"goalMetDcd\": \"CURRENT\", \"goalMetDcdCd\": 0, \"resourceId\": \"V-500-216.8-6-216.83-1\", \"targetDate\": \"FEB 19,1993\", \"targetDateFHIR\": \"1993-02-19\", \"targetDateFM\": 2930219, \"targetDateHL7\": 19930219, \"userWhoEntered\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEnteredId\": 10958, \"userWhoEnteredResId\": \"V-500-200-755\", \"userWhoEnteredNPI\": \"\" }, { \"dateTimeEntered\": \"FEB 12,1993@10:18:49\", \"dateTimeEnteredFHIR\": \"1993-02-12T10:18:49-05:00\", \"dateTimeEnteredFM\": 2930212.101849, \"dateTimeEnteredHL7\": \"19930212101849-0500\", \"goalExpectedOutcome\": \"identifies three alternative methods to deal with stressors\", \"goalExpectedOutcomeId\": 1002087, \"goalMetDcd\": \"CURRENT\", \"goalMetDcdCd\": 0, \"resourceId\": \"V-500-216.8-6-216.83-2\", \"targetDate\": \"FEB 26,1993\", \"targetDateFHIR\": \"1993-02-26\", \"targetDateFM\": 2930226, \"targetDateHL7\": 19930226, \"userWhoEntered\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEnteredId\": 10958, \"userWhoEnteredResId\": \"V-500-200-755\", \"userWhoEnteredNPI\": \"\" }, { \"dateTimeEntered\": \"FEB 12,1993@10:18:54\", \"dateTimeEnteredFHIR\": \"1993-02-12T10:18:54-05:00\", \"dateTimeEnteredFM\": 2930212.101854, \"dateTimeEnteredHL7\": \"19930212101854-0500\", \"goalExpectedOutcome\": \"reports feeling less depressed\", \"goalExpectedOutcomeId\": 1002076, \"goalMetDcd\": \"CURRENT\", \"goalMetDcdCd\": 0, \"resourceId\": \"V-500-216.8-6-216.83-3\", \"targetDate\": \"MAR 5,1993\", \"targetDateFHIR\": \"1993-03-05\", \"targetDateFM\": 2930305, \"targetDateHL7\": 19930305, \"userWhoEntered\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEnteredId\": 10958, \"userWhoEnteredResId\": \"V-500-200-755\", \"userWhoEnteredNPI\": \"\" }, { \"dateTimeEntered\": \"FEB 12,1993@10:21:04\", \"dateTimeEnteredFHIR\": \"1993-02-12T10:21:04-05:00\", \"dateTimeEnteredFM\": 2930212.102104, \"dateTimeEnteredHL7\": \"19930212102104-0500\", \"goalExpectedOutcome\": \"discusses feelings of loss re: giving up drugs by [date]\", \"goalExpectedOutcomeId\": 1001830, \"goalMetDcd\": \"CURRENT\", \"goalMetDcdCd\": 0, \"resourceId\": \"V-500-216.8-6-216.83-4\", \"targetDate\": \"MAR 4,1993\", \"targetDateFHIR\": \"1993-03-04\", \"targetDateFM\": 2930304, \"targetDateHL7\": 19930304, \"userWhoEntered\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEnteredId\": 10958, \"userWhoEnteredResId\": \"V-500-200-755\", \"userWhoEnteredNPI\": \"\" }, { \"dateTimeEntered\": \"FEB 12,1993@10:21:08\", \"dateTimeEnteredFHIR\": \"1993-02-12T10:21:08-05:00\", \"dateTimeEnteredFM\": 2930212.102108, \"dateTimeEnteredHL7\": \"19930212102108-0500\", \"goalExpectedOutcome\": \"makes contact with support person\\/agency prior to D\\/C\", \"goalExpectedOutcomeId\": 1002428, \"goalMetDcd\": \"CURRENT\", \"goalMetDcdCd\": 0, \"resourceId\": \"V-500-216.8-6-216.83-5\", \"targetDate\": \"FEB 17,1993\", \"targetDateFHIR\": \"1993-02-17\", \"targetDateFM\": 2930217, \"targetDateHL7\": 19930217, \"userWhoEntered\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEnteredId\": 10958, \"userWhoEnteredResId\": \"V-500-200-755\", \"userWhoEnteredNPI\": \"\" }, { \"dateTimeEntered\": \"FEB 12,1993@10:21:19\", \"dateTimeEnteredFHIR\": \"1993-02-12T10:21:19-05:00\", \"dateTimeEnteredFM\": 2930212.102119, \"dateTimeEnteredHL7\": \"19930212102119-0500\", \"goalExpectedOutcome\": \"writes list of support persons\\/agencies before discharge\", \"goalExpectedOutcomeId\": 1002427, \"goalMetDcd\": \"CURRENT\", \"goalMetDcdCd\": 0, \"resourceId\": \"V-500-216.8-6-216.83-6\", \"targetDate\": \"MAR 4,1993\", \"targetDateFHIR\": \"1993-03-04\", \"targetDateFM\": 2930304, \"targetDateHL7\": 19930304, \"userWhoEntered\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEnteredId\": 10958, \"userWhoEnteredResId\": \"V-500-200-755\", \"userWhoEnteredNPI\": \"\" } ] }, \"textFileEntry\": \"Nursing Care Plan\", \"textFileEntryId\": 6 } }, { \"NurseCarePlan\": { \"evaluationDates\": { \"evaluationDate\": [ { \"dateTimeEntered\": \"MAR 1,1993@16:08:42\", \"dateTimeEnteredFHIR\": \"1993-03-01T16:08:42-05:00\", \"dateTimeEnteredFM\": 2930301.160842, \"dateTimeEnteredHL7\": \"19930301160842-0500\", \"evaluationDate\": \"MAR 11,1993\", \"evaluationDateFHIR\": \"1993-03-11\", \"evaluationDateFM\": 2930311, \"evaluationDateHL7\": 19930311, \"problem\": \"Aspiration Potential\", \"problemId\": 1002559, \"problemStatus\": \"EVALUATE\", \"problemStatusCd\": 0, \"resourceId\": \"V-500-216.8-10-216.82-1\", \"userWhoEvaluated\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEvaluatedId\": 10958, \"userWhoEvaluatedNPI\": \"\" } ] }, \"ncpIen\": 10, \"patient\": \"ZZZRETIREDTWENTYFIVE,PATIENT\", \"patientICN\": \"1967316818V742124\", \"patientId\": 138, \"problemLists\": { \"problemList\": [ { \"problem\": \"Aspiration Potential\", \"problemId\": 1002559, \"resourceId\": \"V-500-216.8-10-216.81-1\" } ] }, \"resourceId\": \"V-500-216.8-10\", \"resourceType\": \"NurseCarePlan\", \"targetDates\": { \"targetDate\": [ { \"dateTimeEntered\": \"MAR 1,1993@16:08:18\", \"dateTimeEnteredFHIR\": \"1993-03-01T16:08:18-05:00\", \"dateTimeEnteredFM\": 2930301.160818, \"dateTimeEnteredHL7\": \"19930301160818-0500\", \"goalExpectedOutcome\": \"evidence of clear airway\", \"goalExpectedOutcomeId\": 1002582, \"goalMetDcd\": \"CURRENT\", \"goalMetDcdCd\": 0, \"resourceId\": \"V-500-216.8-10-216.83-1\", \"targetDate\": \"MAR 6,1993\", \"targetDateFHIR\": \"1993-03-06\", \"targetDateFM\": 2930306, \"targetDateHL7\": 19930306, \"userWhoEntered\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEnteredId\": 10958, \"userWhoEnteredResId\": \"V-500-200-755\", \"userWhoEnteredNPI\": \"\" } ] }, \"textFileEntry\": \"Nursing Care Plan\", \"textFileEntryId\": 10 } }, { \"NurseCarePlan\": { \"ncpIen\": 11, \"patient\": \"ZZZRETIREDTWENTYFIVE,PATIENT\", \"patientICN\": \"1967316818V742124\", \"patientId\": 138, \"resourceId\": \"V-500-216.8-11\", \"resourceType\": \"NurseCarePlan\", \"targetDates\": { \"targetDate\": [ { \"dateTimeEntered\": \"MAR 1,1993@16:43:47\", \"dateTimeEnteredFHIR\": \"1993-03-01T16:43:47-05:00\", \"dateTimeEnteredFM\": 2930301.164347, \"dateTimeEnteredHL7\": \"19930301164347-0500\", \"goalExpectedOutcome\": \"Diarrhea\", \"goalExpectedOutcomeId\": 1001081, \"goalMetDcd\": \"CURRENT\", \"goalMetDcdCd\": 0, \"resourceId\": \"V-500-216.8-11-216.83-1\", \"targetDate\": \"MAR 6,1993\", \"targetDateFHIR\": \"1993-03-06\", \"targetDateFM\": 2930306, \"targetDateHL7\": 19930306, \"userWhoEntered\": \"WARDCLERK,FIFTYTHREE\", \"userWhoEnteredId\": 10958, \"userWhoEnteredResId\": \"V-500-200-755\", \"userWhoEnteredNPI\": \"\" } ] }, \"textFileEntry\": \"Nursing Care Plan\", \"textFileEntryId\": 11 } } ] }";

        GoalParser parser = new GoalParser();

        List<Goal> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 8, result.size());
        Goal goal = result.get(0);
        Assert.assertEquals("Resource ID correct", "V-500-216.8-6-216.83-1", goal.getId());
        Assert.assertEquals("Correct ICN", "1967316818V742124", goal.getSubject().getIdentifier().getValue());
        Assert.assertEquals("Correct Conditions Count", 2, goal.getAddresses().size());
        Assert.assertEquals("Correct Status", Goal.GoalStatus.INPROGRESS, goal.getStatus());
        Assert.assertEquals("Correct Description", "attends and participates in assigned groups", goal.getDescription().getText());
        Assert.assertEquals("Correct Provider", "V-500-200-755", goal.getExpressedBy().getIdentifier().getValue());
        Assert.assertEquals("Correct Start Date", "1993-02-12", goal.getStartDateType().getValueAsString());
        Assert.assertEquals("Correct Target Date", "1993-02-19", goal.getTarget().getDueDateType().getValueAsString());
    }

    @Test
    public void TestMissingGoalParse() {
        String input = "";

        GoalParser parser = new GoalParser();

        List<Goal> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestEmptyGoalParse() {
        String input = "{}";

        GoalParser parser = new GoalParser();

        List<Goal> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
