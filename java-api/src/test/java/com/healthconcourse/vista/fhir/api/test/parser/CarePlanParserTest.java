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
import java.util.List;

public class CarePlanParserTest {

    @Test
    public void TestSuccessfulCarePlanParse() {
        String input = "{ \"CarePlan\": [ { \"Visit\": [ { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: active\", \"cpEnd\": \"\", \"cpEndFHIR\": \"\", \"cpEndHL7\": \"\", \"cpIntent\": \"plan\", \"cpStart\": 2821208, \"cpStartFHIR\": \"1982-12-08\", \"cpStartHL7\": 19821208, \"cpState\": \"active\", \"cpType\": \"careplan\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT DIABETES SELF MANAGEMENT PLAN (SCT:698360004) [C]\", \"hlfCategoryId\": 7071, \"hlfIen\": 933, \"hlfName\": \"SYN CP DIABETES SELF MANAGEMENT PLAN (SCT:698360004)\", \"hlfNameId\": 7072, \"hlfNameSCT\": 698360004, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_933\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End: \", \"cpState\": \"\", \"cpType\": \"addresses\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT DIABETES SELF MANAGEMENT PLAN (SCT:698360004) [C]\", \"hlfCategoryId\": 7071, \"hlfIen\": 934, \"hlfName\": \"SYN ADDR  (SCT:15777000)\", \"hlfNameId\": 7098, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_934\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT DIABETES SELF MANAGEMENT PLAN (SCT:698360004) [C]\", \"hlfCategoryId\": 7071, \"hlfIen\": 935, \"hlfName\": \"SYN ACT DIABETIC DIET (SCT:160670007)\", \"hlfNameId\": 7073, \"hlfNameSCT\": 160670007, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_935\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT DIABETES SELF MANAGEMENT PLAN (SCT:698360004) [C]\", \"hlfCategoryId\": 7071, \"hlfIen\": 936, \"hlfName\": \"SYN ACT EXERCISE THERAPY (SCT:229065009)\", \"hlfNameId\": 7074, \"hlfNameSCT\": 229065009, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_936\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 937, \"hlfName\": \"SYN GOAL ADDRESS PATIENT KNOWLEDGE DEFICIT ON DIA (SCT:15777000)\", \"hlfNameId\": 7117, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_937\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 938, \"hlfName\": \"SYN GOAL IMPROVE AND MAINTENANCE OF OPTIMAL FOOT  (SCT:15777000)\", \"hlfNameId\": 7118, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_938\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 939, \"hlfName\": \"SYN GOAL MAINTAIN BLOOD PRESSURE BELOW 140\\/90 MMH (SCT:15777000)\", \"hlfNameId\": 7119, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_939\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 940, \"hlfName\": \"SYN GOAL GLUCOSE [MASS\\/VOLUME] IN BLOOD < 108 (SCT:15777000)\", \"hlfNameId\": 7120, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_940\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 941, \"hlfName\": \"SYN GOAL HEMOGLOBIN A1C TOTAL IN BLOOD < 7.0 (SCT:15777000)\", \"hlfNameId\": 7121, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_941\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } } ] } ] }";

        CarePlanParser parser = new CarePlanParser();

        List<CarePlan> result = parser.parseCarePlan(input, "1234");

        Assert.assertEquals("Correct number of care plans", 9, result.size());
        Assert.assertEquals("Correct number of activities", 2, result.get(0).getActivity().size());
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
