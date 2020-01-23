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

import com.healthconcourse.vista.fhir.api.parser.AppointmentParser;
import org.hl7.fhir.dstu3.model.Appointment;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentParserTest {

    @Test
    public void TestSuccessfulAppointmentParse() throws ParseException {
        String input = "{ \"Patappt\": { \"appointments\": { \"appointment\": [ { \"appointmentDateTime\": \"NOV 10,2015@08:00\", \"appointmentDateTimeFHIR\": \"2015-11-10T08:00:00-05:00\", \"appointmentDateTimeFM\": 3151110.08, \"appointmentDateTimeHL7\": \"201511100800-0500\", \"appointmentType\": \"REGULAR\", \"appointmentTypeFHIR\": \"ROUTINE\", \"appointmentTypeId\": 9, \"appointmentTypeSubCategory\": \"\", \"appointmentTypeSubCategoryId\": \"\", \"apptCancelled\": \"\", \"apptCancelledId\": \"\", \"autoRebookedApptDateTime\": \"\", \"autoRebookedApptDateTimeFHIR\": \"\", \"autoRebookedApptDateTimeFM\": \"\", \"autoRebookedApptDateTimeHL7\": \"\", \"cancellationReason\": \"\", \"cancellationReasonId\": \"\", \"cancellationRemarks\": \"\", \"clinic\": \"DIABETIC\", \"clinicId\": 285, \"collateralVisit\": \"\", \"collateralVisitCd\": \"\", \"currentStatus\": \"ACT REQ\\/CHECKED OUT\", \"dataEntryClerk\": \"PROGRAMMER,ONE\", \"dataEntryClerkId\": 1, \"dateApptMade\": \"NOV 6,2015\", \"dateApptMadeFHIR\": \"2015-11-06\", \"dateApptMadeFM\": 3151106, \"dateApptMadeHL7\": 20151106, \"desiredDateOfAppointment\": \"NOV 6,2015\", \"desiredDateOfAppointmentFHIR\": \"2015-11-06\", \"desiredDateOfAppointmentFM\": 3151106, \"desiredDateOfAppointmentHL7\": 20151106, \"ekgDateTime\": \"\", \"ekgDateTimeFHIR\": \"\", \"ekgDateTimeFM\": \"\", \"ekgDateTimeHL7\": \"\", \"encounterConversionStatus\": \"\", \"encounterConversionStatusCd\": \"\", \"encounterFormsAsAddOns\": \"\", \"encounterFormsAsAddOnsCd\": \"\", \"encounterFormsPrinted\": \"\", \"encounterFormsPrintedCd\": \"\", \"followUpVisit\": \"NO\", \"followUpVisitCd\": 0, \"labDateTime\": \"\", \"labDateTimeFHIR\": \"\", \"labDateTimeFM\": \"\", \"labDateTimeHL7\": \"\", \"nextAvaApptIndicator\": \"'NEXT AVA.' APPT. INDICATED BY USER & CALCULATION\", \"nextAvaApptIndicatorCd\": 3, \"noShowCancelDateTime\": \"\", \"noShowCancelDateTimeFHIR\": \"\", \"noShowCancelDateTimeFM\": \"\", \"noShowCancelDateTimeHL7\": \"\", \"noShowCancelledBy\": \"\", \"noShowCancelledById\": \"\", \"numberOfCollateralSeen\": \"\", \"outpatientEncounter\": \"NOV 10,2015@08:00\", \"outpatientEncounterId\": 12765, \"purposeOfVisit\": \"SCHEDULED VISIT\", \"purposeOfVisitCd\": 3, \"realAppointment\": 0, \"resourceId\": \"V-500-2-100855-2.98-3151110.08\", \"routingSlipPrintDate\": \"\", \"routingSlipPrintDateFHIR\": \"\", \"routingSlipPrintDateFM\": \"\", \"routingSlipPrintDateHL7\": \"\", \"routingSlipPrinted\": \"\", \"routingSlipPrintedCd\": \"\", \"schedulingRequestType\": \"'NEXT AVAILABLE' APPT.\", \"schedulingRequestTypeCd\": \"N\", \"specialSurveyDisposition\": \"\", \"status\": \"NO ACTION TAKEN\", \"statusCd\": \"NT\", \"statusFHIR\": \"booked\", \"telephoneOfClinic\": \"\", \"xRayDateTime\": \"\", \"xRayDateTimeFHIR\": \"\", \"xRayDateTimeFM\": \"\", \"xRayDateTimeHL7\": \"\" }, { \"appointmentDateTime\": \"NOV 30,2015@08:00\", \"appointmentDateTimeFHIR\": \"2015-11-30T08:00:00-05:00\", \"appointmentDateTimeFM\": 3151130.08, \"appointmentDateTimeHL7\": \"201511300800-0500\", \"appointmentType\": \"REGULAR\", \"appointmentTypeFHIR\": \"ROUTINE\", \"appointmentTypeId\": 9, \"appointmentTypeSubCategory\": \"\", \"appointmentTypeSubCategoryId\": \"\", \"apptCancelled\": \"\", \"apptCancelledId\": \"\", \"autoRebookedApptDateTime\": \"\", \"autoRebookedApptDateTimeFHIR\": \"\", \"autoRebookedApptDateTimeFM\": \"\", \"autoRebookedApptDateTimeHL7\": \"\", \"cancellationReason\": \"CLINIC CANCELLED\", \"cancellationReasonId\": 13, \"cancellationRemarks\": \"\", \"clinic\": \"PODIATRY\", \"clinicId\": 233, \"collateralVisit\": \"\", \"collateralVisitCd\": \"\", \"currentStatus\": \"CANCELLED BY CLINIC\", \"dataEntryClerk\": \"PROGRAMMER,ONE\", \"dataEntryClerkId\": 1, \"dateApptMade\": \"NOV 9,2015\", \"dateApptMadeFHIR\": \"2015-11-09\", \"dateApptMadeFM\": 3151109, \"dateApptMadeHL7\": 20151109, \"desiredDateOfAppointment\": \"NOV 9,2015\", \"desiredDateOfAppointmentFHIR\": \"2015-11-09\", \"desiredDateOfAppointmentFM\": 3151109, \"desiredDateOfAppointmentHL7\": 20151109, \"ekgDateTime\": \"\", \"ekgDateTimeFHIR\": \"\", \"ekgDateTimeFM\": \"\", \"ekgDateTimeHL7\": \"\", \"encounterConversionStatus\": \"\", \"encounterConversionStatusCd\": \"\", \"encounterFormsAsAddOns\": \"\", \"encounterFormsAsAddOnsCd\": \"\", \"encounterFormsPrinted\": \"\", \"encounterFormsPrintedCd\": \"\", \"followUpVisit\": \"NO\", \"followUpVisitCd\": 0, \"labDateTime\": \"\", \"labDateTimeFHIR\": \"\", \"labDateTimeFM\": \"\", \"labDateTimeHL7\": \"\", \"nextAvaApptIndicator\": \"'NEXT AVA.' APPT. INDICATED BY USER\", \"nextAvaApptIndicatorCd\": 1, \"noShowCancelDateTime\": \"NOV 30,2015@13:59:49\", \"noShowCancelDateTimeFHIR\": \"2015-11-30T13:59:49-05:00\", \"noShowCancelDateTimeFM\": 3151130.135949, \"noShowCancelDateTimeHL7\": \"20151130135949-0500\", \"noShowCancelledBy\": \"PROGRAMMER,ONE\", \"noShowCancelledById\": 1, \"numberOfCollateralSeen\": \"\", \"outpatientEncounter\": \"\", \"outpatientEncounterId\": \"\", \"purposeOfVisit\": \"SCHEDULED VISIT\", \"purposeOfVisitCd\": 3, \"realAppointment\": 0, \"resourceId\": \"V-500-2-100855-2.98-3151130.08\", \"routingSlipPrintDate\": \"\", \"routingSlipPrintDateFHIR\": \"\", \"routingSlipPrintDateFM\": \"\", \"routingSlipPrintDateHL7\": \"\", \"routingSlipPrinted\": \"\", \"routingSlipPrintedCd\": \"\", \"schedulingRequestType\": \"'NEXT AVAILABLE' APPT.\", \"schedulingRequestTypeCd\": \"N\", \"specialSurveyDisposition\": \"\", \"status\": \"CANCELLED BY CLINIC\", \"statusCd\": \"C\", \"statusFHIR\": \"cancelled\", \"telephoneOfClinic\": \"\", \"xRayDateTime\": \"\", \"xRayDateTimeFHIR\": \"\", \"xRayDateTimeFM\": \"\", \"xRayDateTimeHL7\": \"\" }, { \"appointmentDateTime\": \"NOV 30,2015@10:00\", \"appointmentDateTimeFHIR\": \"2015-11-30T10:00:00-05:00\", \"appointmentDateTimeFM\": 3151130.1, \"appointmentDateTimeHL7\": \"201511301000-0500\", \"appointmentType\": \"REGULAR\", \"appointmentTypeFHIR\": \"ROUTINE\", \"appointmentTypeId\": 9, \"appointmentTypeSubCategory\": \"\", \"appointmentTypeSubCategoryId\": \"\", \"apptCancelled\": \"\", \"apptCancelledId\": \"\", \"autoRebookedApptDateTime\": \"\", \"autoRebookedApptDateTimeFHIR\": \"\", \"autoRebookedApptDateTimeFM\": \"\", \"autoRebookedApptDateTimeHL7\": \"\", \"cancellationReason\": \"\", \"cancellationReasonId\": \"\", \"cancellationRemarks\": \"\", \"clinic\": \"OPHTHALMOLOGY\", \"clinicId\": 437, \"collateralVisit\": \"\", \"collateralVisitCd\": \"\", \"currentStatus\": \"ACT REQ\\/CHECKED OUT\", \"dataEntryClerk\": \"PROGRAMMER,ONE\", \"dataEntryClerkId\": 1, \"dateApptMade\": \"NOV 9,2015\", \"dateApptMadeFHIR\": \"2015-11-09\", \"dateApptMadeFM\": 3151109, \"dateApptMadeHL7\": 20151109, \"desiredDateOfAppointment\": \"NOV 9,2015\", \"desiredDateOfAppointmentFHIR\": \"2015-11-09\", \"desiredDateOfAppointmentFM\": 3151109, \"desiredDateOfAppointmentHL7\": 20151109, \"ekgDateTime\": \"\", \"ekgDateTimeFHIR\": \"\", \"ekgDateTimeFM\": \"\", \"ekgDateTimeHL7\": \"\", \"encounterConversionStatus\": \"\", \"encounterConversionStatusCd\": \"\", \"encounterFormsAsAddOns\": \"\", \"encounterFormsAsAddOnsCd\": \"\", \"encounterFormsPrinted\": \"\", \"encounterFormsPrintedCd\": \"\", \"followUpVisit\": \"NO\", \"followUpVisitCd\": 0, \"labDateTime\": \"\", \"labDateTimeFHIR\": \"\", \"labDateTimeFM\": \"\", \"labDateTimeHL7\": \"\", \"nextAvaApptIndicator\": \"'NEXT AVA.' APPT. INDICATED BY USER\", \"nextAvaApptIndicatorCd\": 1, \"noShowCancelDateTime\": \"\", \"noShowCancelDateTimeFHIR\": \"\", \"noShowCancelDateTimeFM\": \"\", \"noShowCancelDateTimeHL7\": \"\", \"noShowCancelledBy\": \"\", \"noShowCancelledById\": \"\", \"numberOfCollateralSeen\": \"\", \"outpatientEncounter\": \"NOV 30,2015@10:00\", \"outpatientEncounterId\": 12773, \"purposeOfVisit\": \"SCHEDULED VISIT\", \"purposeOfVisitCd\": 3, \"realAppointment\": 0, \"resourceId\": \"V-500-2-100855-2.98-3151130.1\", \"routingSlipPrintDate\": \"\", \"routingSlipPrintDateFHIR\": \"\", \"routingSlipPrintDateFM\": \"\", \"routingSlipPrintDateHL7\": \"\", \"routingSlipPrinted\": \"\", \"routingSlipPrintedCd\": \"\", \"schedulingRequestType\": \"'NEXT AVAILABLE' APPT.\", \"schedulingRequestTypeCd\": \"N\", \"specialSurveyDisposition\": \"\", \"status\": \"NO ACTION TAKEN\", \"statusCd\": \"NT\", \"statusFHIR\": \"booked\", \"telephoneOfClinic\": \"\", \"xRayDateTime\": \"\", \"xRayDateTimeFHIR\": \"\", \"xRayDateTimeFM\": \"\", \"xRayDateTimeHL7\": \"\" }, { \"appointmentDateTime\": \"NOV 30,2015@13:00\", \"appointmentDateTimeFHIR\": \"2015-11-30T13:00:00-05:00\", \"appointmentDateTimeFM\": 3151130.13, \"appointmentDateTimeHL7\": \"201511301300-0500\", \"appointmentType\": \"REGULAR\", \"appointmentTypeFHIR\": \"ROUTINE\", \"appointmentTypeId\": 9, \"appointmentTypeSubCategory\": \"\", \"appointmentTypeSubCategoryId\": \"\", \"apptCancelled\": \"\", \"apptCancelledId\": \"\", \"autoRebookedApptDateTime\": \"\", \"autoRebookedApptDateTimeFHIR\": \"\", \"autoRebookedApptDateTimeFM\": \"\", \"autoRebookedApptDateTimeHL7\": \"\", \"cancellationReason\": \"CLINIC CANCELLED\", \"cancellationReasonId\": 13, \"cancellationRemarks\": \"\", \"clinic\": \"NEUROLOGY\", \"clinicId\": 430, \"collateralVisit\": \"\", \"collateralVisitCd\": \"\", \"currentStatus\": \"CANCELLED BY CLINIC\", \"dataEntryClerk\": \"PROGRAMMER,ONE\", \"dataEntryClerkId\": 1, \"dateApptMade\": \"NOV 9,2015\", \"dateApptMadeFHIR\": \"2015-11-09\", \"dateApptMadeFM\": 3151109, \"dateApptMadeHL7\": 20151109, \"desiredDateOfAppointment\": \"NOV 9,2015\", \"desiredDateOfAppointmentFHIR\": \"2015-11-09\", \"desiredDateOfAppointmentFM\": 3151109, \"desiredDateOfAppointmentHL7\": 20151109, \"ekgDateTime\": \"\", \"ekgDateTimeFHIR\": \"\", \"ekgDateTimeFM\": \"\", \"ekgDateTimeHL7\": \"\", \"encounterConversionStatus\": \"\", \"encounterConversionStatusCd\": \"\", \"encounterFormsAsAddOns\": \"\", \"encounterFormsAsAddOnsCd\": \"\", \"encounterFormsPrinted\": \"\", \"encounterFormsPrintedCd\": \"\", \"followUpVisit\": \"NO\", \"followUpVisitCd\": 0, \"labDateTime\": \"\", \"labDateTimeFHIR\": \"\", \"labDateTimeFM\": \"\", \"labDateTimeHL7\": \"\", \"nextAvaApptIndicator\": \"'NEXT AVA.' APPT. INDICATED BY USER\", \"nextAvaApptIndicatorCd\": 1, \"noShowCancelDateTime\": \"NOV 30,2015@14:00:18\", \"noShowCancelDateTimeFHIR\": \"2015-11-30T14:00:18-05:00\", \"noShowCancelDateTimeFM\": 3151130.140018, \"noShowCancelDateTimeHL7\": \"20151130140018-0500\", \"noShowCancelledBy\": \"PROGRAMMER,ONE\", \"noShowCancelledById\": 1, \"numberOfCollateralSeen\": \"\", \"outpatientEncounter\": \"\", \"outpatientEncounterId\": \"\", \"purposeOfVisit\": \"SCHEDULED VISIT\", \"purposeOfVisitCd\": 3, \"realAppointment\": 0, \"resourceId\": \"V-500-2-100855-2.98-3151130.13\", \"routingSlipPrintDate\": \"\", \"routingSlipPrintDateFHIR\": \"\", \"routingSlipPrintDateFM\": \"\", \"routingSlipPrintDateHL7\": \"\", \"routingSlipPrinted\": \"\", \"routingSlipPrintedCd\": \"\", \"schedulingRequestType\": \"'NEXT AVAILABLE' APPT.\", \"schedulingRequestTypeCd\": \"N\", \"specialSurveyDisposition\": \"\", \"status\": \"CANCELLED BY CLINIC\", \"statusCd\": \"C\", \"statusFHIR\": \"cancelled\", \"telephoneOfClinic\": \"\", \"xRayDateTime\": \"\", \"xRayDateTimeFHIR\": \"\", \"xRayDateTimeFM\": \"\", \"xRayDateTimeHL7\": \"\" } ] }, \"dateOfBirth\": \"04\\/10\\/1963\", \"dateOfBirthFHIR\": \"1963-04-10\", \"dateOfBirthFM\": 2630410, \"dateOfBirthHL7\": 19630410, \"fullIcn\": \"5000000359V775335\", \"genderFHIR\": \"male\", \"name\": \"DIABETIC,PATIENT MALE\", \"patIen\": 100855, \"resourceId\": \"V-500-2-100855\", \"resourceType\": \"Appointment\", \"selfIdentifiedGender\": \"\", \"selfIdentifiedGenderCd\": \"\", \"sex\": \"MALE\", \"sexCd\": \"M\", \"socialSecurityNumber\": 111221111 } }";

        AppointmentParser parser = new AppointmentParser();

        List<Appointment> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 4, result.size());

        Appointment firstRecord = result.get(0);
        Assert.assertEquals("Correct status", Appointment.AppointmentStatus.NOSHOW,firstRecord.getStatus());
        Assert.assertEquals("Correct type", "ROUTINE", firstRecord.getAppointmentType().getCoding().get(0).getCode());
        Assert.assertEquals("Correct description", "SCHEDULED VISIT", firstRecord.getDescription());
        Assert.assertEquals("Correct patient", "5000000359V775335", firstRecord.getParticipant().get(0).getActor().getIdentifier().getValue());
        Assert.assertEquals("Correct location", "DIABETIC", firstRecord.getParticipant().get(1).getActor().getDisplay());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        Date expectedDate = sdf.parse("2015-11-10T08:00:00-05:00");
        Assert.assertEquals("Correct appointment start date and time", expectedDate, firstRecord.getStart());
    }

    @Test
    public void TestNoAppointmentParse() {
        String input = "{}";

        AppointmentParser parser = new AppointmentParser();

        List<Appointment> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestNoAppointmentDataParse() {
        String input = "";

        AppointmentParser parser = new AppointmentParser();

        List<Appointment> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
