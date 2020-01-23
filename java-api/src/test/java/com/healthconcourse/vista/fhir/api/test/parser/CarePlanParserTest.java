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

import com.healthconcourse.vista.fhir.api.parser.CarePlanParser;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CarePlanParserTest {

    @Test
    public void TestSuccessfulCarePlanParse() throws ParseException {
        String input = "{ \"CarePlan\": [ { \"Visit\": [ { \"HealthFactor\": { \"auditTrail\": \"38-A 1;\", \"comments\": \"Start: 2910415 End:  Status: active\", \"cpEnd\": \"\", \"cpEndFHIR\": \"\", \"cpEndHL7\": \"\", \"cpIntent\": \"plan\", \"cpResourceType\": \"CarePlan\", \"cpStart\": 2910415, \"cpStartFHIR\": \"1991-04-15\", \"cpStartHL7\": 19910415, \"cpState\": \"active\", \"cpType\": \"careplan\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 38, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"encounterProviderResId\": \"\", \"eventDateTime\": \"APR 15,1991\", \"eventDateTimeFHIR\": \"1991-04-15\", \"eventDateTimeFM\": 2910415, \"eventDateTimeHL7\": 19910415, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7099, \"hlfIen\": 13535, \"hlfName\": \"SYN CP SELF CARE (SCT:326051000000105)\", \"hlfNameId\": 7100, \"hlfNameSCT\": 326051000000105, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"orderingProviderResId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"WILLIAMSON769,ELANE105\", \"patientNameICN\": \"1729408030V259800\", \"patientNameId\": 103474, \"resourceId\": \"V-500-9000010.23-13535\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"APR 15,1991@13:51\", \"visitFHIR\": \"1991-04-15T13:51:00-05:00\", \"visitFM\": 2910415.1351, \"visitHL7\": \"199104151351-0500\", \"visitId\": 59766, \"visitResId\": \"V-500-9000010-59766\" } }, { \"HealthFactor\": { \"auditTrail\": \"38-A 1;\", \"comments\": \"Start: 2910415 End: \", \"cpResourceType\": \"CarePlan\", \"cpState\": \"\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 38, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"encounterProviderResId\": \"\", \"eventDateTime\": \"APR 15,1991\", \"eventDateTimeFHIR\": \"1991-04-15\", \"eventDateTimeFM\": 2910415, \"eventDateTimeHL7\": 19910415, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7099, \"hlfIen\": 13536, \"hlfName\": \"SYN ACT FOOD ALLERGY DIET (SCT:409002)\", \"hlfNameId\": 7101, \"hlfNameSCT\": 409002, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"orderingProviderResId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"WILLIAMSON769,ELANE105\", \"patientNameICN\": \"1729408030V259800\", \"patientNameId\": 103474, \"resourceId\": \"V-500-9000010.23-13536\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"APR 15,1991@13:51\", \"visitFHIR\": \"1991-04-15T13:51:00-05:00\", \"visitFM\": 2910415.1351, \"visitHL7\": \"199104151351-0500\", \"visitId\": 59766, \"visitResId\": \"V-500-9000010-59766\" } }, { \"HealthFactor\": { \"auditTrail\": \"38-A 1;\", \"comments\": \"Start: 2910415 End: \", \"cpResourceType\": \"CarePlan\", \"cpState\": \"\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 38, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"encounterProviderResId\": \"\", \"eventDateTime\": \"APR 15,1991\", \"eventDateTimeFHIR\": \"1991-04-15\", \"eventDateTimeFM\": 2910415, \"eventDateTimeHL7\": 19910415, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7099, \"hlfIen\": 13537, \"hlfName\": \"SYN ACT ALLERGY EDUCATION (SCT:58332002)\", \"hlfNameId\": 7102, \"hlfNameSCT\": 58332002, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"orderingProviderResId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"WILLIAMSON769,ELANE105\", \"patientNameICN\": \"1729408030V259800\", \"patientNameId\": 103474, \"resourceId\": \"V-500-9000010.23-13537\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"APR 15,1991@13:51\", \"visitFHIR\": \"1991-04-15T13:51:00-05:00\", \"visitFM\": 2910415.1351, \"visitHL7\": \"199104151351-0500\", \"visitId\": 59766, \"visitResId\": \"V-500-9000010-59766\" } }, { \"HealthFactor\": { \"auditTrail\": \"38-A 1;\", \"comments\": \"Start: 2910415 End: \", \"cpResourceType\": \"CarePlan\", \"cpState\": \"\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 38, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"encounterProviderResId\": \"\", \"eventDateTime\": \"APR 15,1991\", \"eventDateTimeFHIR\": \"1991-04-15\", \"eventDateTimeFM\": 2910415, \"eventDateTimeHL7\": 19910415, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7099, \"hlfIen\": 13538, \"hlfName\": \"SYN ACT ALLERGY EDUCATION (SCT:58332002)\", \"hlfNameId\": 7102, \"hlfNameSCT\": 58332002, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"orderingProviderResId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"WILLIAMSON769,ELANE105\", \"patientNameICN\": \"1729408030V259800\", \"patientNameId\": 103474, \"resourceId\": \"V-500-9000010.23-13538\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"APR 15,1991@13:51\", \"visitFHIR\": \"1991-04-15T13:51:00-05:00\", \"visitFM\": 2910415.1351, \"visitHL7\": \"199104151351-0500\", \"visitId\": 59766, \"visitResId\": \"V-500-9000010-59766\" } } ] }, { \"Visit\": [ { \"HealthFactor\": { \"auditTrail\": \"38-A 1;\", \"comments\": \"Start: 3170813 End: 3180318 Status: completed\", \"cpEnd\": 3180318, \"cpEndFHIR\": \"2018-03-18\", \"cpEndHL7\": 20180318, \"cpIntent\": \"plan\", \"cpResourceType\": \"CarePlan\", \"cpStart\": 3170813, \"cpStartFHIR\": \"2017-08-13\", \"cpStartHL7\": 20170813, \"cpState\": \"completed\", \"cpType\": \"careplan\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 38, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"encounterProviderResId\": \"\", \"eventDateTime\": \"AUG 13,2017\", \"eventDateTimeFHIR\": \"2017-08-13\", \"eventDateTimeFM\": 3170813, \"eventDateTimeHL7\": 20170813, \"hlfCategory\": \"SYN CPCAT ROUTINE ANTENATAL CARE (SCT:134435003) [C]\", \"hlfCategoryId\": 7072, \"hlfIen\": 13539, \"hlfName\": \"SYN CP ROUTINE ANTENATAL CARE (SCT:134435003)\", \"hlfNameId\": 7073, \"hlfNameSCT\": 134435003, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"orderingProviderResId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"WILLIAMSON769,ELANE105\", \"patientNameICN\": \"1729408030V259800\", \"patientNameId\": 103474, \"resourceId\": \"V-500-9000010.23-13539\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"AUG 13,2017@13:51\", \"visitFHIR\": \"2017-08-13T13:51:00-05:00\", \"visitFM\": 3170813.1351, \"visitHL7\": \"201708131351-0500\", \"visitId\": 59786, \"visitResId\": \"V-500-9000010-59786\" } }, { \"HealthFactor\": { \"auditTrail\": \"38-A 1;\", \"comments\": \"Start: 3170813 End: 3180318\", \"cpResourceType\": \"CarePlan\", \"cpState\": \"\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 38, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"encounterProviderResId\": \"\", \"eventDateTime\": \"AUG 13,2017\", \"eventDateTimeFHIR\": \"2017-08-13\", \"eventDateTimeFM\": 3170813, \"eventDateTimeHL7\": 20170813, \"hlfCategory\": \"SYN CPCAT ROUTINE ANTENATAL CARE (SCT:134435003) [C]\", \"hlfCategoryId\": 7072, \"hlfIen\": 13540, \"hlfName\": \"SYN ACT ANTENATAL EDUCATION (SCT:135892000)\", \"hlfNameId\": 7074, \"hlfNameSCT\": 135892000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"orderingProviderResId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"WILLIAMSON769,ELANE105\", \"patientNameICN\": \"1729408030V259800\", \"patientNameId\": 103474, \"resourceId\": \"V-500-9000010.23-13540\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"AUG 13,2017@13:51\", \"visitFHIR\": \"2017-08-13T13:51:00-05:00\", \"visitFM\": 3170813.1351, \"visitHL7\": \"201708131351-0500\", \"visitId\": 59786, \"visitResId\": \"V-500-9000010-59786\" } }, { \"HealthFactor\": { \"auditTrail\": \"38-A 1;\", \"comments\": \"Start: 3170813 End: 3180318\", \"cpResourceType\": \"CarePlan\", \"cpState\": \"\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 38, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"encounterProviderResId\": \"\", \"eventDateTime\": \"AUG 13,2017\", \"eventDateTimeFHIR\": \"2017-08-13\", \"eventDateTimeFM\": 3170813, \"eventDateTimeHL7\": 20170813, \"hlfCategory\": \"SYN CPCAT ROUTINE ANTENATAL CARE (SCT:134435003) [C]\", \"hlfCategoryId\": 7072, \"hlfIen\": 13541, \"hlfName\": \"SYN ACT ANTENATAL RISK ASSESSMENT (SCT:713076009)\", \"hlfNameId\": 7179, \"hlfNameSCT\": 713076009, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"orderingProviderResId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"WILLIAMSON769,ELANE105\", \"patientNameICN\": \"1729408030V259800\", \"patientNameId\": 103474, \"resourceId\": \"V-500-9000010.23-13541\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"AUG 13,2017@13:51\", \"visitFHIR\": \"2017-08-13T13:51:00-05:00\", \"visitFM\": 3170813.1351, \"visitHL7\": \"201708131351-0500\", \"visitId\": 59786, \"visitResId\": \"V-500-9000010-59786\" } }, { \"HealthFactor\": { \"auditTrail\": \"38-A 1;\", \"comments\": \"Start: 3170813 End: 3180318\", \"cpResourceType\": \"CarePlan\", \"cpState\": \"\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 38, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"encounterProviderResId\": \"\", \"eventDateTime\": \"AUG 13,2017\", \"eventDateTimeFHIR\": \"2017-08-13\", \"eventDateTimeFM\": 3170813, \"eventDateTimeHL7\": 20170813, \"hlfCategory\": \"SYN CPCAT ROUTINE ANTENATAL CARE (SCT:134435003) [C]\", \"hlfCategoryId\": 7072, \"hlfIen\": 13542, \"hlfName\": \"SYN ACT ANTENATAL BLOOD TESTS (SCT:312404004)\", \"hlfNameId\": 7180, \"hlfNameSCT\": 312404004, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"orderingProviderResId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"WILLIAMSON769,ELANE105\", \"patientNameICN\": \"1729408030V259800\", \"patientNameId\": 103474, \"resourceId\": \"V-500-9000010.23-13542\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"AUG 13,2017@13:51\", \"visitFHIR\": \"2017-08-13T13:51:00-05:00\", \"visitFM\": 3170813.1351, \"visitHL7\": \"201708131351-0500\", \"visitId\": 59786, \"visitResId\": \"V-500-9000010-59786\" } } ] } ] }";

        CarePlanParser parser = new CarePlanParser();

        List<CarePlan> result = parser.parseCarePlan(input, "1234");

        Assert.assertEquals("Correct number of care plans", 8, result.size());
        Assert.assertEquals("Correct number of activities", 3, result.get(0).getActivity().size());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("1991-04-15");
        Assert.assertEquals("Correct plan start date and time", expectedDate, result.get(0).getPeriod().getStart());
    }

    @Test
    public void TestInvalidCarePlanParse() {
        String input = "-1^Patient identifier not recognised";

        CarePlanParser parser = new CarePlanParser();

        List<CarePlan> result = parser.parseCarePlan(input, "1234");

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestNoDataCarePlanParse() {
        String input = "";

        CarePlanParser parser = new CarePlanParser();

        List<CarePlan> result = parser.parseCarePlan(input, "1234");

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestGarbageDataCarePlanParse() {
        String input = "asdfewqrvd#sdk*92Ldd";

        CarePlanParser parser = new CarePlanParser();

        List<CarePlan> result = parser.parseCarePlan(input, "1234");

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
