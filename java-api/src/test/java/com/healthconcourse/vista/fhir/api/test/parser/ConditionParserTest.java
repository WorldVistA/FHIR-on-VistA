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

import com.healthconcourse.vista.fhir.api.parser.ConditionParser;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ConditionParserTest {

    @Test
    public void TestSuccessfulConditionParse() throws ParseException {
        String json = "{ \"Conditions\": [ { \"Problem\": { \"agentOrangeExposure\": \"NO\", \"agentOrangeExposureCd\": 0, \"class\": \"\", \"classCd\": \"\", \"clinic\": \"\", \"clinicId\": \"\", \"codingSystem\": \"ICD\", \"combatVeteran\": \"NO\", \"combatVeteranCd\": 0, \"condition\": \"PERMANENT\", \"conditionCd\": \"P\", \"dateEntered\": \"FEB 1,2010\", \"dateEnteredFHIR\": \"2010-02-01\", \"dateEnteredFM\": 3100201, \"dateEnteredHL7\": 20100201, \"dateLastModified\": \"FEB 1,2010\", \"dateLastModifiedFHIR\": \"2010-02-01\", \"dateLastModifiedFM\": 3100201, \"dateLastModifiedHL7\": 20100201, \"dateOfInterestFM\": \"FEB 1,2010\", \"dateOfInterestHL7\": \"FEB 1,2010\", \"dateOfOnset\": \"SEP 30,2009\", \"dateOfOnsetFHIR\": \"2009-09-30\", \"dateOfOnsetFM\": 3090930, \"dateOfOnsetHL7\": 20090930, \"dateRecorded\": \"FEB 1,2010\", \"dateRecordedFHIR\": \"2010-02-01\", \"dateRecordedFM\": 3100201, \"dateRecordedHL7\": 20100201, \"dateResolved\": \"\", \"dateResolvedFHIR\": \"\", \"dateResolvedFM\": \"\", \"dateResolvedHL7\": \"\", \"diagnosis\": \"V49.70\", \"diagnosisId\": 13116, \"diagnosisText\": \"UNSPECIFIED LOWER LIMB AMPUTATION STATUS\", \"enteredBy\": \"PROVIDER,FIVE\", \"enteredById\": 988, \"facility\": \"CAMP MASTER\", \"facilityId\": 500, \"headAndOrNeckCancer\": \"NO\", \"headAndOrNeckCancerCd\": 0, \"ionizingRadiationExposure\": \"NO\", \"ionizingRadiationExposureCd\": 0, \"militarySexualTrauma\": \"NO\", \"militarySexualTraumaCd\": 0, \"nmbr\": 1, \"noteFacilitys\": { \"noteFacility\": [ { \"noteFacility\": \"CAMP MASTER\", \"noteFacilityId\": 500, \"notes\": { \"note\": [ { \"author\": \"PROVIDER,FIVE\", \"authorId\": 988, \"dateNoteAdded\": \"FEB 1,2010\", \"dateNoteAddedFHIR\": \"2010-02-01\", \"dateNoteAddedFM\": 3100201, \"dateNoteAddedHL7\": 20100201, \"noteNarrative\": \"per DoD records\", \"noteNmbr\": 1, \"resourceId\": \"V-500-9000011-886-9000011.11-1-9000011.1111-1\", \"status\": \"ACTIVE\", \"statusCd\": \"A\" } ] }, \"resourceId\": \"V-500-9000011-886-9000011.11-1\" } ] }, \"patientICN\": \"5000000352V586511\", \"patientName\": \"EHMP,FIVE\", \"patientNameId\": 100848, \"persianGulfExposure\": \"NO\", \"persianGulfExposureCd\": 0, \"priority\": \"\", \"priorityCd\": \"\", \"problem\": \"O\\/E - Amputated left leg\", \"problemId\": 7489557, \"problemIen\": 886, \"providerNarative\": \"O\\/E - Amputated left leg\", \"providerNarativeId\": 709, \"recordingProvider\": \"PROVIDER,FIVE\", \"recordingProviderId\": 988, \"recordingProviderNPI\": \"\", \"resourceId\": \"V-500-9000011-886\", \"resourceType\": \"Condition\", \"responsibleProvider\": \"PROVIDER,FIVE\", \"responsibleProviderResId\": \"V-500-200-988\", \"responsibleProviderId\": 988, \"responsibleProviderNPI\": \"\", \"service\": \"\", \"serviceConnected\": \"NO\", \"serviceConnectedCd\": 0, \"serviceId\": \"\", \"shipboardHazardDefense\": \"NO\", \"shipboardHazardDefenseCd\": 0, \"snomedCTconceptCode\": 308094003, \"snomedCTconceptCodeText\": \"O\\/E - Amputated left leg\", \"snomedCTdesignationCode\": 451399018, \"snomedCTdesignationCodeText\": \"\", \"snomedCtToIcdMapStatus\": \"\", \"snomedCtToIcdMapStatusCd\": \"\", \"status\": \"ACTIVE\", \"statusCd\": \"A\", \"uniqueNewTermRequested\": \"\", \"uniqueNewTermRequestedCd\": \"\", \"uniqueTermRequestComment\": \"\", \"vhatConceptVuid\": \"\", \"vhatDesignationVuid\": \"\" } }, { \"Problem\": { \"agentOrangeExposure\": \"NO\", \"agentOrangeExposureCd\": 0, \"class\": \"\", \"classCd\": \"\", \"clinic\": \"\", \"clinicId\": \"\", \"codingSystem\": \"ICD\", \"combatVeteran\": \"NO\", \"combatVeteranCd\": 0, \"condition\": \"PERMANENT\", \"conditionCd\": \"P\", \"dateEntered\": \"SEP 30,2009\", \"dateEnteredFHIR\": \"2009-09-30\", \"dateEnteredFM\": 3090930, \"dateEnteredHL7\": 20090930, \"dateLastModified\": \"FEB 1,2010\", \"dateLastModifiedFHIR\": \"2010-02-01\", \"dateLastModifiedFM\": 3100201, \"dateLastModifiedHL7\": 20100201, \"dateOfInterestFM\": \"FEB 1,2010\", \"dateOfInterestHL7\": \"FEB 1,2010\", \"dateOfOnset\": \"SEP 30,2009\", \"dateOfOnsetFHIR\": \"2009-09-30\", \"dateOfOnsetFM\": 3090930, \"dateOfOnsetHL7\": 20090930, \"dateRecorded\": \"FEB 1,2010\", \"dateRecordedFHIR\": \"2010-02-01\", \"dateRecordedFM\": 3100201, \"dateRecordedHL7\": 20100201, \"dateResolved\": \"\", \"dateResolvedFHIR\": \"\", \"dateResolvedFM\": \"\", \"dateResolvedHL7\": \"\", \"diagnosis\": \"806.00\", \"diagnosisId\": 9846, \"diagnosisText\": \"CLOSED FRACTURE OF C1-C4 LEVEL WITH UNSPECIFIED SPINAL CORD INJURY\", \"enteredBy\": \"PROVIDER,FIVE\", \"enteredById\": 988, \"facility\": \"CAMP MASTER\", \"facilityId\": 500, \"headAndOrNeckCancer\": \"NO\", \"headAndOrNeckCancerCd\": 0, \"ionizingRadiationExposure\": \"NO\", \"ionizingRadiationExposureCd\": 0, \"militarySexualTrauma\": \"NO\", \"militarySexualTraumaCd\": 0, \"nmbr\": 5, \"patientICN\": \"5000000352V586511\", \"patientName\": \"EHMP,FIVE\", \"patientNameId\": 100848, \"persianGulfExposure\": \"NO\", \"persianGulfExposureCd\": 0, \"priority\": \"CHRONIC\", \"priorityCd\": \"C\", \"problem\": \"Closed fracture of C1-C4 level with spinal cord injury\", \"problemId\": 7015555, \"problemIen\": 890, \"providerNarative\": \"Closed fracture of C1-C4 level with spinal cord injury\", \"providerNarativeId\": 713, \"recordingProvider\": \"PROVIDER,FIVE\", \"recordingProviderId\": 988, \"recordingProviderNPI\": \"\", \"resourceId\": \"V-500-9000011-890\", \"resourceType\": \"Condition\", \"responsibleProvider\": \"PROVIDER,FIVE\", \"responsibleProviderResId\": \"V-500-200-988\", \"responsibleProviderId\": 988, \"responsibleProviderNPI\": \"\", \"service\": \"\", \"serviceConnected\": \"NO\", \"serviceConnectedCd\": 0, \"serviceId\": \"\", \"shipboardHazardDefense\": \"NO\", \"shipboardHazardDefenseCd\": 0, \"snomedCTconceptCode\": 8840000, \"snomedCTconceptCodeText\": \"Closed fracture of C1-C4 level with spinal cord injury\", \"snomedCTdesignationCode\": 15592018, \"snomedCTdesignationCodeText\": \"\", \"snomedCtToIcdMapStatus\": \"\", \"snomedCtToIcdMapStatusCd\": \"\", \"status\": \"ACTIVE\", \"statusCd\": \"A\", \"uniqueNewTermRequested\": \"\", \"uniqueNewTermRequestedCd\": \"\", \"uniqueTermRequestComment\": \"\", \"vhatConceptVuid\": \"\", \"vhatDesignationVuid\": \"\" } }, { \"Problem\": { \"agentOrangeExposure\": \"NO\", \"agentOrangeExposureCd\": 0, \"class\": \"\", \"classCd\": \"\", \"clinic\": \"\", \"clinicId\": \"\", \"codingSystem\": \"ICD\", \"combatVeteran\": \"NO\", \"combatVeteranCd\": 0, \"condition\": \"PERMANENT\", \"conditionCd\": \"P\", \"dateEntered\": \"FEB 10,2010\", \"dateEnteredFHIR\": \"2010-02-10\", \"dateEnteredFM\": 3100210, \"dateEnteredHL7\": 20100210, \"dateLastModified\": \"FEB 10,2010\", \"dateLastModifiedFHIR\": \"2010-02-10\", \"dateLastModifiedFM\": 3100210, \"dateLastModifiedHL7\": 20100210, \"dateOfInterestFM\": \"FEB 10,2010\", \"dateOfInterestHL7\": \"FEB 10,2010\", \"dateOfOnset\": \"FEB 10,2010\", \"dateOfOnsetFHIR\": \"2010-02-10\", \"dateOfOnsetFM\": 3100210, \"dateOfOnsetHL7\": 20100210, \"dateRecorded\": \"JAN 28,2015\", \"dateRecordedFHIR\": \"2015-01-28\", \"dateRecordedFM\": 3150128, \"dateRecordedHL7\": 20150128, \"dateResolved\": \"\", \"dateResolvedFHIR\": \"\", \"dateResolvedFM\": \"\", \"dateResolvedHL7\": \"\", \"diagnosis\": \"345.90\", \"diagnosisId\": 12427, \"diagnosisText\": \"EPILEPSY, UNSPECIFIED, WITHOUT MENTION OF INTRACTABLE EPILEPSY\", \"enteredBy\": \"PROVIDER,FIVE\", \"enteredById\": 988, \"facility\": \"CAMP MASTER\", \"facilityId\": 500, \"headAndOrNeckCancer\": \"NO\", \"headAndOrNeckCancerCd\": 0, \"ionizingRadiationExposure\": \"NO\", \"ionizingRadiationExposureCd\": 0, \"militarySexualTrauma\": \"NO\", \"militarySexualTraumaCd\": 0, \"nmbr\": 6, \"patientICN\": \"5000000352V586511\", \"patientName\": \"EHMP,FIVE\", \"patientNameId\": 100848, \"persianGulfExposure\": \"NO\", \"persianGulfExposureCd\": 0, \"priority\": \"CHRONIC\", \"priorityCd\": \"C\", \"problem\": \"Epilepsy\", \"problemId\": 7151429, \"problemIen\": 891, \"providerNarative\": \"Epilepsy\", \"providerNarativeId\": 714, \"recordingProvider\": \"PROVIDER,FIVE\", \"recordingProviderId\": 988, \"recordingProviderNPI\": \"\", \"resourceId\": \"V-500-9000011-891\", \"resourceType\": \"Condition\", \"responsibleProvider\": \"PROVIDER,FIVE\", \"responsibleProviderId\": 988, \"responsibleProviderResId\": \"V-500-200-988\", \"responsibleProviderNPI\": \"\", \"service\": \"\", \"serviceConnected\": \"NO\", \"serviceConnectedCd\": 0, \"serviceId\": \"\", \"shipboardHazardDefense\": \"NO\", \"shipboardHazardDefenseCd\": 0, \"snomedCTconceptCode\": 84757009, \"snomedCTconceptCodeText\": \"Epilepsy\", \"snomedCTdesignationCode\": 178739011, \"snomedCTdesignationCodeText\": \"\", \"snomedCtToIcdMapStatus\": \"\", \"snomedCtToIcdMapStatusCd\": \"\", \"status\": \"ACTIVE\", \"statusCd\": \"A\", \"uniqueNewTermRequested\": \"\", \"uniqueNewTermRequestedCd\": \"\", \"uniqueTermRequestComment\": \"\", \"vhatConceptVuid\": \"\", \"vhatDesignationVuid\": \"\" } } ] }";
        ConditionParser parser = new ConditionParser();

        List<Condition> result = parser.parseList(json);

        Assert.assertEquals("Correct number of records", 3, result.size());

        Condition first = result.get(0);
        Assert.assertEquals("MetaData is correct", "VistA", first.getMeta().getTagFirstRep().getDisplay());

        Assert.assertEquals("ID is correct", "V-500-9000011-886", first.getId());

        SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");

        DateTimeType expectedDate = new DateTimeType(sdf.parse("9/30/2009"));

        DateTimeType foundDate = (DateTimeType) first.getOnset();

        Assert.assertEquals("Onset day is correct", expectedDate.getDay(), foundDate.getDay());
        Assert.assertEquals("Onset year is correct", expectedDate.getYear(), foundDate.getYear());
        Assert.assertEquals("Onset month is correct", expectedDate.getMonth(), foundDate.getMonth());
    }

    @Test
    public void TestFailedCareTeamParse() {
        String json = "--1^Patientidentifiernotrecognised";
        ConditionParser parser = new ConditionParser();

        List<Condition> result = parser.parseList(json);

        Assert.assertEquals("Correct number of records", 0, result.size());
    }

    @Test
    public void TestFailedCareTeamParseEmptyJSON() {
        String json = "";
        ConditionParser parser = new ConditionParser();

        List<Condition> result = parser.parseList(json);

        Assert.assertEquals("Correct number of records", 0, result.size());
    }
}
