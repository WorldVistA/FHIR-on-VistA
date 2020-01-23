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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImmunizationParserTest {

    @Test
    public void TestSuccessfulImmunizationParse() throws ParseException {
        String input = "{ \"Immunizations\": [ { \"Immunize\": { \"administrativeNotes\": \"\", \"ancillaryPov\": \"\", \"ancillaryPovId\": \"\", \"auditTrail\": \"19-A 991;\", \"clinic\": \"\", \"clinicId\": \"\", \"comments\": \"\", \"contraindicated\": \"NO (OK TO USE IN THE FUTURE)\", \"contraindicatedCd\": 0, \"createdByVCptEntry\": \"\", \"dataSource\": \"TEXT INTEGRATION UTILITIES\", \"dataSourceId\": 19, \"dateOfVacInfoStatement\": \"\", \"dateOfVacInfoStatementFHIR\": \"\", \"dateOfVacInfoStatementFM\": \"\", \"dateOfVacInfoStatementHL7\": \"\", \"dateTimeEntered\": \"\", \"dateTimeEnteredFHIR\": \"\", \"dateTimeEnteredFM\": \"\", \"dateTimeEnteredHL7\": \"\", \"dateTimeLastModified\": \"\", \"dateTimeLastModifiedFHIR\": \"\", \"dateTimeLastModifiedFM\": \"\", \"dateTimeLastModifiedHL7\": \"\", \"dateTimeRead\": \"\", \"dateTimeReadFHIR\": \"\", \"dateTimeReadFM\": \"\", \"dateTimeReadHL7\": \"\", \"dateTimeRecorded\": \"\", \"dateTimeRecordedFHIR\": \"\", \"dateTimeRecordedFM\": \"\", \"dateTimeRecordedHL7\": \"\", \"dosage\": \"\", \"dose\": \"\", \"doseOverride\": \"\", \"doseOverrideCd\": \"\", \"doseUnits\": \"\", \"doseUnitsId\": \"\", \"editedFlag\": \"\", \"editedFlagCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"enteredBy\": \"\", \"enteredById\": \"\", \"eventDateAndTime\": \"\", \"eventDateAndTimeFHIR\": \"\", \"eventDateAndTimeFM\": \"\", \"eventDateAndTimeHL7\": \"\", \"eventInformationSource\": \"\", \"eventInformationSourceId\": \"\", \"externalKey\": \"\", \"hoursReadPostInoculation\": \"\", \"immunization\": \"PNEUMOCOCCAL POLYSACCHARIDE PPV23\", \"immunizationDocumenter\": \"PROVIDER,EIGHT\", \"immunizationDocumenterId\": 991, \"immunizationId\": 500007, \"immunizeIen\": 1153, \"importFromOutsideRegistry\": \"\", \"importFromOutsideRegistryCd\": \"\", \"injectionSite\": \"\", \"injectionSiteCd\": \"\", \"lastModifiedBy\": \"\", \"lastModifiedById\": \"\", \"location\": \"CAMP MASTER\", \"locationId\": 500, \"lot\": \"\", \"lotId\": \"\", \"lotNumber\": \"\", \"lotNumberId\": \"\", \"ndc\": \"\", \"ndcId\": \"\", \"orderingLocation\": \"\", \"orderingLocationId\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"outsideProviderName\": \"\", \"package\": \"ORDER ENTRY\\/RESULTS REPORTING\", \"packageId\": 170, \"parent\": \"\", \"parentId\": \"\", \"patientICN\": \"5000000359V775335\", \"patientName\": \"DIABETIC,PATIENT MALE\", \"patientNameId\": 100855, \"primaryDiagnosis\": \"\", \"primaryDiagnosisId\": \"\", \"reaction\": \"\", \"reactionCd\": \"\", \"reader\": \"\", \"readerId\": \"\", \"reading\": \"\", \"readingComment\": \"\", \"readingRecorded\": \"\", \"readingRecordedFHIR\": \"\", \"readingRecordedFM\": \"\", \"readingRecordedHL7\": \"\", \"remarks\": \"\", \"resourceId\": \"V-500-9000010.11-1153\", \"resourceType\": \"Immunization\", \"results\": \"\", \"resultsCd\": \"\", \"routeOfAdministration\": \"\", \"routeOfAdministrationId\": \"\", \"series\": \"\", \"seriesCd\": \"\", \"siteOfAdministrationBody\": \"\", \"siteOfAdministrationBodyId\": \"\", \"timestamp\": \"\", \"timestampFHIR\": \"\", \"timestampFM\": \"\", \"timestampHL7\": \"\", \"userLastUpdate\": \"\", \"userLastUpdateId\": \"\", \"vacEligibility\": \"\", \"vacEligibilityId\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"JAN 1,2011@08:00\", \"visitFHIR\": \"2011-01-01T08:00:00-05:00\", \"visitFM\": 3110101.08, \"visitHL7\": \"201101010800-0500\", \"visitId\": 11754, \"visitResId\": \"V-500-9000010-11431\", \"volume\": \"\", \"warningAcknowledged\": \"\", \"warningAcknowledgedCd\": \"\", \"warningOverrideReason\": \"\" } }, { \"Immunize\": { \"administrativeNotes\": \"\", \"ancillaryPov\": \"\", \"ancillaryPovId\": \"\", \"auditTrail\": \"19-A 991;\", \"clinic\": \"\", \"clinicId\": \"\", \"comments\": \"\", \"contraindicated\": \"NO (OK TO USE IN THE FUTURE)\", \"contraindicatedCd\": 0, \"createdByVCptEntry\": \"\", \"dataSource\": \"TEXT INTEGRATION UTILITIES\", \"dataSourceId\": 19, \"dateOfVacInfoStatement\": \"\", \"dateOfVacInfoStatementFHIR\": \"\", \"dateOfVacInfoStatementFM\": \"\", \"dateOfVacInfoStatementHL7\": \"\", \"dateTimeEntered\": \"\", \"dateTimeEnteredFHIR\": \"\", \"dateTimeEnteredFM\": \"\", \"dateTimeEnteredHL7\": \"\", \"dateTimeLastModified\": \"\", \"dateTimeLastModifiedFHIR\": \"\", \"dateTimeLastModifiedFM\": \"\", \"dateTimeLastModifiedHL7\": \"\", \"dateTimeRead\": \"\", \"dateTimeReadFHIR\": \"\", \"dateTimeReadFM\": \"\", \"dateTimeReadHL7\": \"\", \"dateTimeRecorded\": \"\", \"dateTimeRecordedFHIR\": \"\", \"dateTimeRecordedFM\": \"\", \"dateTimeRecordedHL7\": \"\", \"dosage\": \"\", \"dose\": \"\", \"doseOverride\": \"\", \"doseOverrideCd\": \"\", \"doseUnits\": \"\", \"doseUnitsId\": \"\", \"editedFlag\": \"\", \"editedFlagCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"encounterProviderNPI\": \"\", \"enteredBy\": \"\", \"enteredById\": \"\", \"eventDateAndTime\": \"\", \"eventDateAndTimeFHIR\": \"\", \"eventDateAndTimeFM\": \"\", \"eventDateAndTimeHL7\": \"\", \"eventInformationSource\": \"\", \"eventInformationSourceId\": \"\", \"externalKey\": \"\", \"hoursReadPostInoculation\": \"\", \"immunization\": \"INFLUENZA, SEASONAL, INJECTABLE\", \"immunizationDocumenter\": \"PROVIDER,EIGHT\", \"immunizationDocumenterId\": 991, \"immunizationId\": 1141, \"immunizeIen\": 1154, \"importFromOutsideRegistry\": \"\", \"importFromOutsideRegistryCd\": \"\", \"injectionSite\": \"\", \"injectionSiteCd\": \"\", \"lastModifiedBy\": \"\", \"lastModifiedById\": \"\", \"location\": \"CAMP MASTER\", \"locationId\": 500, \"lot\": \"\", \"lotId\": \"\", \"lotNumber\": \"\", \"lotNumberId\": \"\", \"ndc\": \"\", \"ndcId\": \"\", \"orderingLocation\": \"\", \"orderingLocationId\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"orderingProviderNPI\": \"\", \"outsideProviderName\": \"\", \"package\": \"ORDER ENTRY\\/RESULTS REPORTING\", \"packageId\": 170, \"parent\": \"\", \"parentId\": \"\", \"patientICN\": \"5000000359V775335\", \"patientName\": \"DIABETIC,PATIENT MALE\", \"patientNameId\": 100855, \"primaryDiagnosis\": \"\", \"primaryDiagnosisId\": \"\", \"reaction\": \"\", \"reactionCd\": \"\", \"reader\": \"\", \"readerId\": \"\", \"reading\": \"\", \"readingComment\": \"\", \"readingRecorded\": \"\", \"readingRecordedFHIR\": \"\", \"readingRecordedFM\": \"\", \"readingRecordedHL7\": \"\", \"remarks\": \"\", \"resourceId\": \"V-500-9000010.11-1154\", \"resourceType\": \"Immunization\", \"results\": \"\", \"resultsCd\": \"\", \"routeOfAdministration\": \"\", \"routeOfAdministrationId\": \"\", \"series\": \"Series1\", \"seriesCd\": \"1\", \"siteOfAdministrationBody\": \"\", \"siteOfAdministrationBodyId\": \"\", \"timestamp\": \"\", \"timestampFHIR\": \"\", \"timestampFM\": \"\", \"timestampHL7\": \"\", \"userLastUpdate\": \"\", \"userLastUpdateId\": \"\", \"vacEligibility\": \"\", \"vacEligibilityId\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"JAN 1,2011@08:00\", \"visitFHIR\": \"2011-01-01T08:00:00-05:00\", \"visitFM\": 3110101.08, \"visitHL7\": \"201101010800-0500\", \"visitId\": 11754, \"visitResId\": \"V-500-9000010-11431\", \"volume\": \"\", \"warningAcknowledged\": \"\", \"warningAcknowledgedCd\": \"\", \"warningOverrideReason\": \"\" } } ] }";

        ImmunizationParser parser = new ImmunizationParser();

        List<Immunization> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 2, result.size());

        Immunization firstRecord = result.get(0);
        Assert.assertEquals("Correct Patient", "DIABETIC,PATIENT MALE", firstRecord.getPatient().getDisplay());
        Assert.assertEquals("Correct Immunization", "PNEUMOCOCCAL POLYSACCHARIDE PPV23", firstRecord.getVaccineCode().getCoding().get(0).getDisplay());
        Assert.assertEquals("Correct Location", "CAMP MASTER", firstRecord.getLocation().getDisplay());
        Assert.assertEquals("Practitioner Correct", "PROVIDER,EIGHT", firstRecord.getPractitioner().get(0).getActor().getDisplay());
        Assert.assertEquals("Encounter Correct", "V-500-9000010-11431", firstRecord.getEncounter().getIdentifier().getValue());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        Date expectedDate = sdf.parse("2011-01-01T08:00:00-05:00");
        Assert.assertEquals("Correct appointment start date and time", expectedDate, firstRecord.getDate());
    }

    @Test
    public void TestFailedImmunizationParse() {
        String input = "-1^Patient identifier not recognised";

        ImmunizationParser parser = new ImmunizationParser();

        List<Immunization> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestImmunizationParseNoData() {
        String input = "";

        ImmunizationParser parser = new ImmunizationParser();

        List<Immunization> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestFailedImmunizationParseEmptyData() {
        String input = "{}";

        ImmunizationParser parser = new ImmunizationParser();

        List<Immunization> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }
}
