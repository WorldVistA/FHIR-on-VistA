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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllergyParserTest {

    @Test
    public void TestSuccessfulAllergyParse() throws ParseException {
        String input = "{ \"Allergies\": [ { \"Allergy\": { \"allergyIen\": 984, \"allergyType\": \"DRUG\", \"allergyTypeFHIR\": \"medication\", \"chartMarkeds\": { \"chartMarked\": [ { \"dateTime\": \"JUL 1,2015@13:47:52\", \"dateTimeFHIR\": \"2015-07-01T13:47:52-05:00\", \"dateTimeFM\": 3150701.134752, \"dateTimeHL7\": \"20150701134752-0500\", \"resourceId\": \"V-500-120.8-984-120.813-1\", \"userEntering\": \"PROVIDER,SIX\", \"userEnteringResId\": \"V-500-200-988\", \"userEnteringId\": 989, \"userEnteringNPI\": \"\" } ] }, \"dateTimeEnteredInError\": \"\", \"dateTimeEnteredInErrorFHIR\": \"\", \"dateTimeEnteredInErrorFM\": \"\", \"dateTimeEnteredInErrorHL7\": \"\", \"enteredInError\": \"\", \"enteredInErrorCd\": \"\", \"gmrAllergy\": \"PENICILLIN\", \"gmrAllergyId\": \"125;GMRD(120.82,\", \"mechanism\": \"PHARMACOLOGIC\", \"mechanismCd\": \"P\", \"observedHistorical\": \"OBSERVED\", \"observedHistoricalCd\": \"o\", \"originationDateTime\": \"JUL 1,2015@13:47\", \"originationDateTimeFHIR\": \"2015-07-01T13:47:00-05:00\", \"originationDateTimeFM\": 3150701.1347, \"originationDateTimeHL7\": \"201507011347-0500\", \"originator\": \"PROVIDER,SIX\", \"originatorId\": 989, \"originatorSignOff\": \"YES\", \"originatorSignOffCd\": 1, \"patient\": \"TRASH,TONY\", \"patientICN\": \"5000000364V598024\", \"patientId\": 100860, \"reactant\": \"PENICILLIN\", \"reactantSCT\": 6369005, \"reactionss\": { \"reactions\": [ { \"dateEntered\": \"\", \"dateEnteredFHIR\": \"\", \"dateEnteredFM\": \"\", \"dateEnteredHL7\": \"\", \"enteredBy\": \"PROVIDER,SIX\", \"enteredById\": 989, \"enteredByNPI\": \"\", \"otherReaction\": \"\", \"reaction\": \"RASH\", \"reactionId\": 133, \"reactionSCT\": 112625008, \"resourceId\": \"V-500-120.8-984-120.81-1\" } ] }, \"reportable\": \"\", \"reportableCd\": \"\", \"resourceId\": \"V-500-120.8-984\", \"resourceType\": \"AllergyIntolerance\", \"severity\": \"MODERATE\", \"severityCd\": 2, \"userEnteringInError\": \"\", \"userEnteringInErrorId\": \"\", \"userEnteringResId\": \"\", \"userEnteringInErrorNPI\": \"\", \"verificationDateTime\": \"\", \"verificationDateTimeFHIR\": \"\", \"verificationDateTimeFM\": \"\", \"verificationDateTimeHL7\": \"\", \"verified\": \"NO\", \"verifiedCd\": 0, \"verifier\": \"\", \"verifierId\": \"\", \"verifierNPI\": \"\" } }, { \"Allergy\": { \"allergyIen\": 985, \"allergyType\": \"DRUG\", \"allergyTypeFHIR\": \"medication\", \"chartMarkeds\": { \"chartMarked\": [ { \"dateTime\": \"JUL 1,2015@13:48:53\", \"dateTimeFHIR\": \"2015-07-01T13:48:53-05:00\", \"dateTimeFM\": 3150701.134853, \"dateTimeHL7\": \"20150701134853-0500\", \"resourceId\": \"V-500-120.8-985-120.813-1\", \"userEntering\": \"PROVIDER,SIX\", \"userEnteringResId\": \"V-500-200-988\", \"userEnteringId\": 989, \"userEnteringNPI\": \"\" } ] }, \"dateTimeEnteredInError\": \"\", \"dateTimeEnteredInErrorFHIR\": \"\", \"dateTimeEnteredInErrorFM\": \"\", \"dateTimeEnteredInErrorHL7\": \"\", \"enteredInError\": \"\", \"enteredInErrorCd\": \"\", \"gmrAllergy\": \"AM114\", \"gmrAllergyId\": \"11;PS(50.605,\", \"mechanism\": \"PHARMACOLOGIC\", \"mechanismCd\": \"P\", \"observedHistorical\": \"HISTORICAL\", \"observedHistoricalCd\": \"h\", \"originationDateTime\": \"JUL 1,2015@13:48\", \"originationDateTimeFHIR\": \"2015-07-01T13:48:00-05:00\", \"originationDateTimeFM\": 3150701.1348, \"originationDateTimeHL7\": \"201507011348-0500\", \"originator\": \"PROVIDER,SIX\", \"originatorId\": 989, \"originatorSignOff\": \"YES\", \"originatorSignOffCd\": 1, \"patient\": \"TRASH,TONY\", \"patientICN\": \"5000000364V598024\", \"patientId\": 100860, \"reactant\": \"PENICILLINS AND BETA-LACTAM ANTIMICROBIALS\", \"reactantSCT\": \"unmapped\", \"reactionss\": { \"reactions\": [ { \"dateEntered\": \"APR 1,2015\", \"dateEnteredFHIR\": \"2015-04-01\", \"dateEnteredFM\": 3150401, \"dateEnteredHL7\": 20150401, \"enteredBy\": \"PROVIDER,SIX\", \"enteredById\": 989, \"enteredByNPI\": \"\", \"otherReaction\": \"\", \"reaction\": \"RASH\", \"reactionId\": 133, \"reactionSCT\": 112625008, \"resourceId\": \"V-500-120.8-985-120.81-1\" } ] }, \"reportable\": \"\", \"reportableCd\": \"\", \"resourceId\": \"V-500-120.8-985\", \"resourceType\": \"AllergyIntolerance\", \"severity\": \"\", \"severityCd\": \"\", \"userEnteringInError\": \"\", \"userEnteringInErrorId\": \"\", \"userEnteringInErrorNPI\": \"\", \"verificationDateTime\": \"JUL 1,2015@13:48:54\", \"verificationDateTimeFHIR\": \"2015-07-01T13:48:54-05:00\", \"verificationDateTimeFM\": 3150701.134854, \"verificationDateTimeHL7\": \"20150701134854-0500\", \"verified\": \"YES\", \"verifiedCd\": 1, \"verifier\": \"\", \"verifierId\": \"\", \"verifierNPI\": \"\" } } ] }";

        AllergyParser parser = new AllergyParser();

        List<AllergyIntolerance> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 2, result.size());
        String allergen = result.get(0).getCode().getCoding().get(0).getDisplay();
        Assert.assertEquals("Correct allergen", "PENICILLIN", allergen);
        Assert.assertEquals("One reaction exists", 1, result.get(0).getReaction().size());
        Assert.assertEquals("Correct reaction", "RASH", result.get(0).getReaction().get(0).getDescription());
        Assert.assertEquals("Verified NO correct", AllergyIntolerance.AllergyIntoleranceVerificationStatus.UNCONFIRMED, result.get(0).getVerificationStatus());
        Assert.assertEquals("Verified YES correct", AllergyIntolerance.AllergyIntoleranceVerificationStatus.CONFIRMED, result.get(1).getVerificationStatus());
        Assert.assertEquals("Recorder correct", "V-500-200-988", result.get(0).getRecorder().getIdentifier().getValue());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        Date expectedDate = sdf.parse("2015-07-01T13:47:00-05:00");
        Assert.assertEquals("Date correct", expectedDate, result.get(0).getAssertedDate());
    }

    @Test
    public void TestFailedAllergyParse() {
        String input = "-1^Patient identifier not recognised";

        AllergyParser parser = new AllergyParser();

        List<AllergyIntolerance> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestFailedAllergyParseNoInput() {
        String input = "";

        AllergyParser parser = new AllergyParser();

        List<AllergyIntolerance> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestFailedAllergyParseGarbageInput() {
        String input = "{}";

        AllergyParser parser = new AllergyParser();

        List<AllergyIntolerance> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
