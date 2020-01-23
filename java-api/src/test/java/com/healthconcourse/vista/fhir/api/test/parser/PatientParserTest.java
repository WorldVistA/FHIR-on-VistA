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

import com.healthconcourse.vista.fhir.api.parser.PatientParser;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PatientParserTest {

    @Test
    public void TestSuccessfulPatientParse () {

        String input = "{ \"Patients\": [ { \"Patient\": { \"age\": 59, \"aliass\": { \"alias\": [ { \"alias\": \"POLYTRAUMA,INPATIENT\", \"aliasComponents\": 2.01, \"aliasComponentsId\": 5913, \"aliasSsn\": \"\", \"resourceId\": \"V-500-2-100848-2.01-1\" } ] }, \"city\": \"ANN ARBOR\", \"confidentialAddressCategorys\": { \"confidentialAddressCategory\": [ { \"confidentialAddressCategory\": \"MEDICAL RECORDS\", \"confidentialAddressCategoryCd\": 4, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-1\" }, { \"confidentialAddressCategory\": \"ELIGIBILITY\\/ENROLLMENT\", \"confidentialAddressCategoryCd\": 1, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-2\" }, { \"confidentialAddressCategory\": \"APPOINTMENT\\/SCHEDULING\", \"confidentialAddressCategoryCd\": 2, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-3\" }, { \"confidentialAddressCategory\": \"COPAYMENTS\\/VETERAN BILLING\", \"confidentialAddressCategoryCd\": 3, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-4\" }, { \"confidentialAddressCategory\": \"ALL OTHERS\", \"confidentialAddressCategoryCd\": 5, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-5\" } ] }, \"country\": \"USA\", \"countryId\": 1, \"county\": \"\", \"dateOfBirth\": \"03\\/03\\/1960\", \"dateOfBirthFHIR\": \"1960-03-03\", \"dateOfBirthFM\": 2600303, \"dateOfBirthHL7\": 19600303, \"dateOfDeath\": \"\", \"dateOfDeathFHIR\": \"\", \"dateOfDeathFM\": \"\", \"dateOfDeathHL7\": \"\", \"deathEnteredBy\": \"\", \"deathEnteredById\": \"\", \"emailAddress\": \"\", \"emergencyResponseIndicator\": \"\", \"emergencyResponseIndicatorCd\": \"\", \"employerCity\": \"\", \"employerName\": \"\", \"employerPhoneNumber\": \"\", \"employerState\": \"\", \"employerStateId\": \"\", \"employerStreetLine1\": \"\", \"employerStreetLine2\": \"\", \"employerStreetLine3\": \"\", \"employerZip4\": \"\", \"employerZipCode\": \"\", \"employmentStatus\": \"\", \"employmentStatusCd\": \"\", \"emrCity\": \"ANN ARBOR\", \"emrContactSameAsNok\": \"\", \"emrContactSameAsNokCd\": \"\", \"emrName\": \"EHMP,BROTHER\", \"emrPhoneNumber\": \"555-555-8765\", \"emrRelationshipToPatient\": \"BROTHER\", \"emrState\": \"MICHIGAN\", \"emrStateId\": 26, \"emrStreetAddressLine1\": \"54321 PARK WAY\", \"emrStreetAddressLine2\": \"\", \"emrStreetAddressLine3\": \"\", \"emrWorkPhoneNumber\": \"\", \"emrZip4\": 48103, \"emrZipCode\": 48103, \"ethnicityInformations\": { \"ethnicityInformation\": [ { \"ethnicityInformation\": \"NOT HISPANIC OR LATINO\", \"ethnicityInformationId\": 2, \"methodOfCollection\": \"SELF IDENTIFICATION\", \"methodOfCollectionId\": 1, \"resourceId\": \"V-500-2-100848-2.06-2\" } ] }, \"fatherSName\": \"EHMP,FATHER\", \"fullICN\": \"5000000352V586511\", \"governmentAgency\": \"\", \"governmentAgencyCd\": \"\", \"icnChecksum\": 586511, \"initlast4\": \"E0005\", \"integrationControlNumber\": 5000000352, \"kAddressSameAsPatientS\": \"YES\", \"kAddressSameAsPatientSCd\": \"Y\", \"kCity\": \"ANN ARBOR\", \"kNameOfPrimaryNok\": \"EHMP,BETTY\", \"kPhoneNumber\": \"555-555-1234\", \"kRelationshipToPatient\": \"SPOUSE\", \"kState\": \"MICHIGAN\", \"kStateId\": 26, \"kStreetAddressLine1\": \"12345 ANY STREET\", \"kStreetAddressLine2\": \"\", \"kStreetAddressLine3\": \"\", \"kWorkPhoneNumber\": \"\", \"kZip4\": 48103, \"kZipCode\": 48103, \"maritalStatus\": \"MARRIED\", \"maritalStatusId\": 2, \"motherSMaidenName\": \"CHDR,MOTHER\", \"motherSName\": \"EHMP,MOTHER\", \"multipleBirthIndicator\": \"NO\", \"multipleBirthIndicatorCd\": \"N\", \"name\": \"EHMP,FIVE\", \"occupation\": \"\", \"pagerNumber\": \"\", \"patientIen\": 100848, \"phoneNumberCellular\": \"555-555-1122\", \"phoneNumberResidence\": \"555-555-1234\", \"phoneNumberWork\": \"555-555-4321\", \"placeOfBirthCity\": \"ANN ARBOR, MI\", \"placeOfBirthState\": \"MICHIGAN\", \"placeOfBirthStateId\": 26, \"postalCode\": \"\", \"preferredFacility\": \"\", \"preferredFacilityId\": \"\", \"province\": \"\", \"race\": \"\", \"raceId\": \"\", \"raceInformations\": { \"raceInformation\": [ { \"methodOfCollection\": \"SELF IDENTIFICATION\", \"methodOfCollectionId\": 1, \"raceInformation\": \"BLACK OR AFRICAN AMERICAN\", \"raceInformationId\": 11, \"resourceId\": \"V-500-2-100848-2.02-11\" } ] }, \"religiousPreference\": \"CHRISTIAN (NON-SPECIFIC)\", \"remarks\": \"\", \"resourceId\": \"V-500-2-100848\", \"resourceType\": \"Patient\", \"selfIdentifiedGender\": \"\", \"selfIdentifiedGenderCd\": \"\", \"sex\": \"MALE\", \"sexCd\": \"M\", \"socialSecurityNumber\": 666110005, \"state\": \"MICHIGAN\", \"stateId\": 26, \"streetAddressLine1\": \"12345 ANY STREET\", \"streetAddressLine2\": \"\", \"streetAddressLine3\": \"\", \"tempAddressActive\": \"YES\", \"tempAddressActiveCd\": \"Y\", \"tempAddressCountry\": \"USA\", \"tempAddressCountryId\": 1, \"tempAddressCounty\": 161, \"tempAddressEndDate\": \"\", \"tempAddressEndDateFHIR\": \"\", \"tempAddressEndDateFM\": \"\", \"tempAddressEndDateHL7\": \"\", \"tempAddressPostalCode\": \"\", \"tempAddressProvince\": \"\", \"tempAddressStartDate\": \"MAR 14,2015\", \"tempAddressStartDateFHIR\": \"2015-03-14\", \"tempAddressStartDateFM\": 3150314, \"tempAddressStartDateHL7\": 20150314, \"tempCity\": \"ANN ARBOR\", \"tempPhoneNumber\": \"555-555-9876\", \"tempState\": \"MICHIGAN\", \"tempStateId\": 26, \"tempStreetLine1\": \"98765 NHCU STREET\", \"tempStreetLine2\": \"ROOM B-23\", \"tempStreetLine3\": \"\", \"tempZip4\": 48103, \"tempZipCode\": 48103, \"terminalDigitOfSsn\": \"050011666\", \"testPatientIndicator\": \"\", \"testPatientIndicatorCd\": \"\", \"veteranYN\": \"YES\", \"veteranYNCd\": \"Y\", \"zip4\": 48103, \"zipCode\": 48103 } } ] }";

        PatientParser parser = new PatientParser();

        Optional<Patient> patient = parser.parseSingle(input);

        Patient result = patient.get();

        List<HumanName> names = result.getName();

        Assert.assertEquals("LastName is EHMP", "EHMP", names.get(0).getFamily());
        Assert.assertEquals("FirstName is FIVE", "FIVE", names.get(0).getGiven().get(0).getValue());

        Assert.assertEquals("ID is ICN", "5000000352V586511", result.getIdentifier().get(0).getValue());
        Assert.assertEquals("SSN is 666110005", "666110005", result.getIdentifier().get(1).getValue());
        Assert.assertEquals("Gender is MALE", AdministrativeGender.MALE, result.getGender());

        Date expectedDate = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            expectedDate = sdf.parse("03-3-1960");
        } catch (ParseException px) {}

        Assert.assertEquals("Birthdate is 3/3/1960", expectedDate, result.getBirthDate());

        Address address = result.getAddress().get(0);
        Assert.assertEquals("City is ANN ARBOR", "ANN ARBOR", address.getCity());
        Assert.assertEquals("State is MICHIGAN", "MICHIGAN", address.getState());
        Assert.assertEquals("Postal is 48103", "48103", address.getPostalCode());
    }

    @Test
    public void TestMaritalStatusPatientParse () {

        String input = "{ \"Patients\": [ { \"Patient\": { \"age\": 59, \"aliass\": { \"alias\": [ { \"alias\": \"POLYTRAUMA,INPATIENT\", \"aliasComponents\": 2.01, \"aliasComponentsId\": 5913, \"aliasSsn\": \"\", \"resourceId\": \"V-500-2-100848-2.01-1\" } ] }, \"city\": \"ANN ARBOR\", \"confidentialAddressCategorys\": { \"confidentialAddressCategory\": [ { \"confidentialAddressCategory\": \"MEDICAL RECORDS\", \"confidentialAddressCategoryCd\": 4, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-1\" }, { \"confidentialAddressCategory\": \"ELIGIBILITY\\/ENROLLMENT\", \"confidentialAddressCategoryCd\": 1, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-2\" }, { \"confidentialAddressCategory\": \"APPOINTMENT\\/SCHEDULING\", \"confidentialAddressCategoryCd\": 2, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-3\" }, { \"confidentialAddressCategory\": \"COPAYMENTS\\/VETERAN BILLING\", \"confidentialAddressCategoryCd\": 3, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-4\" }, { \"confidentialAddressCategory\": \"ALL OTHERS\", \"confidentialAddressCategoryCd\": 5, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-5\" } ] }, \"country\": \"USA\", \"countryId\": 1, \"county\": \"\", \"dateOfBirth\": \"03\\/03\\/1960\", \"dateOfBirthFHIR\": \"1960-03-03\", \"dateOfBirthFM\": 2600303, \"dateOfBirthHL7\": 19600303, \"dateOfDeath\": \"\", \"dateOfDeathFHIR\": \"\", \"dateOfDeathFM\": \"\", \"dateOfDeathHL7\": \"\", \"deathEnteredBy\": \"\", \"deathEnteredById\": \"\", \"emailAddress\": \"\", \"emergencyResponseIndicator\": \"\", \"emergencyResponseIndicatorCd\": \"\", \"employerCity\": \"\", \"employerName\": \"\", \"employerPhoneNumber\": \"\", \"employerState\": \"\", \"employerStateId\": \"\", \"employerStreetLine1\": \"\", \"employerStreetLine2\": \"\", \"employerStreetLine3\": \"\", \"employerZip4\": \"\", \"employerZipCode\": \"\", \"employmentStatus\": \"\", \"employmentStatusCd\": \"\", \"emrCity\": \"ANN ARBOR\", \"emrContactSameAsNok\": \"\", \"emrContactSameAsNokCd\": \"\", \"emrName\": \"EHMP,BROTHER\", \"emrPhoneNumber\": \"555-555-8765\", \"emrRelationshipToPatient\": \"BROTHER\", \"emrState\": \"MICHIGAN\", \"emrStateId\": 26, \"emrStreetAddressLine1\": \"54321 PARK WAY\", \"emrStreetAddressLine2\": \"\", \"emrStreetAddressLine3\": \"\", \"emrWorkPhoneNumber\": \"\", \"emrZip4\": 48103, \"emrZipCode\": 48103, \"ethnicityInformations\": { \"ethnicityInformation\": [ { \"ethnicityInformation\": \"NOT HISPANIC OR LATINO\", \"ethnicityInformationId\": 2, \"methodOfCollection\": \"SELF IDENTIFICATION\", \"methodOfCollectionId\": 1, \"resourceId\": \"V-500-2-100848-2.06-2\" } ] }, \"fatherSName\": \"EHMP,FATHER\", \"fullICN\": \"5000000352V586511\", \"governmentAgency\": \"\", \"governmentAgencyCd\": \"\", \"icnChecksum\": 586511, \"initlast4\": \"E0005\", \"integrationControlNumber\": 5000000352, \"kAddressSameAsPatientS\": \"YES\", \"kAddressSameAsPatientSCd\": \"Y\", \"kCity\": \"ANN ARBOR\", \"kNameOfPrimaryNok\": \"EHMP,BETTY\", \"kPhoneNumber\": \"555-555-1234\", \"kRelationshipToPatient\": \"SPOUSE\", \"kState\": \"MICHIGAN\", \"kStateId\": 26, \"kStreetAddressLine1\": \"12345 ANY STREET\", \"kStreetAddressLine2\": \"\", \"kStreetAddressLine3\": \"\", \"kWorkPhoneNumber\": \"\", \"kZip4\": 48103, \"kZipCode\": 48103, \"maritalStatus\": \"MARRIED\", \"maritalStatusId\": 2, \"motherSMaidenName\": \"CHDR,MOTHER\", \"motherSName\": \"EHMP,MOTHER\", \"multipleBirthIndicator\": \"NO\", \"multipleBirthIndicatorCd\": \"N\", \"name\": \"EHMP,FIVE\", \"occupation\": \"\", \"pagerNumber\": \"\", \"patientIen\": 100848, \"phoneNumberCellular\": \"555-555-1122\", \"phoneNumberResidence\": \"555-555-1234\", \"phoneNumberWork\": \"555-555-4321\", \"placeOfBirthCity\": \"ANN ARBOR, MI\", \"placeOfBirthState\": \"MICHIGAN\", \"placeOfBirthStateId\": 26, \"postalCode\": \"\", \"preferredFacility\": \"\", \"preferredFacilityId\": \"\", \"province\": \"\", \"race\": \"\", \"raceId\": \"\", \"raceInformations\": { \"raceInformation\": [ { \"methodOfCollection\": \"SELF IDENTIFICATION\", \"methodOfCollectionId\": 1, \"raceInformation\": \"BLACK OR AFRICAN AMERICAN\", \"raceInformationId\": 11, \"resourceId\": \"V-500-2-100848-2.02-11\" } ] }, \"religiousPreference\": \"CHRISTIAN (NON-SPECIFIC)\", \"remarks\": \"\", \"resourceId\": \"V-500-2-100848\", \"resourceType\": \"Patient\", \"selfIdentifiedGender\": \"\", \"selfIdentifiedGenderCd\": \"\", \"sex\": \"MALE\", \"sexCd\": \"M\", \"socialSecurityNumber\": 666110005, \"state\": \"MICHIGAN\", \"stateId\": 26, \"streetAddressLine1\": \"12345 ANY STREET\", \"streetAddressLine2\": \"\", \"streetAddressLine3\": \"\", \"tempAddressActive\": \"YES\", \"tempAddressActiveCd\": \"Y\", \"tempAddressCountry\": \"USA\", \"tempAddressCountryId\": 1, \"tempAddressCounty\": 161, \"tempAddressEndDate\": \"\", \"tempAddressEndDateFHIR\": \"\", \"tempAddressEndDateFM\": \"\", \"tempAddressEndDateHL7\": \"\", \"tempAddressPostalCode\": \"\", \"tempAddressProvince\": \"\", \"tempAddressStartDate\": \"MAR 14,2015\", \"tempAddressStartDateFHIR\": \"2015-03-14\", \"tempAddressStartDateFM\": 3150314, \"tempAddressStartDateHL7\": 20150314, \"tempCity\": \"ANN ARBOR\", \"tempPhoneNumber\": \"555-555-9876\", \"tempState\": \"MICHIGAN\", \"tempStateId\": 26, \"tempStreetLine1\": \"98765 NHCU STREET\", \"tempStreetLine2\": \"ROOM B-23\", \"tempStreetLine3\": \"\", \"tempZip4\": 48103, \"tempZipCode\": 48103, \"terminalDigitOfSsn\": \"050011666\", \"testPatientIndicator\": \"\", \"testPatientIndicatorCd\": \"\", \"veteranYN\": \"YES\", \"veteranYNCd\": \"Y\", \"zip4\": 48103, \"zipCode\": 48103 } } ] }";

        PatientParser parser = new PatientParser();

        Optional<Patient> patient = parser.parseSingle(input);

        Patient result = patient.get();
        CodeableConcept status = result.getMaritalStatus();
        Assert.assertEquals("M", status.getCodingFirstRep().getCode());
        Assert.assertEquals("Married", status.getCodingFirstRep().getDisplay());
    }

    @Test
    public void TestPatientParseNoZipCode () {

        String input = "{ \"Patients\": [ { \"Patient\": { \"age\": 59, \"aliass\": { \"alias\": [ { \"alias\": \"POLYTRAUMA,INPATIENT\", \"aliasComponents\": 2.01, \"aliasComponentsId\": 5913, \"aliasSsn\": \"\", \"resourceId\": \"V-500-2-100848-2.01-1\" } ] }, \"city\": \"ANN ARBOR\", \"confidentialAddressCategorys\": { \"confidentialAddressCategory\": [ { \"confidentialAddressCategory\": \"MEDICAL RECORDS\", \"confidentialAddressCategoryCd\": 4, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-1\" }, { \"confidentialAddressCategory\": \"ELIGIBILITY\\/ENROLLMENT\", \"confidentialAddressCategoryCd\": 1, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-2\" }, { \"confidentialAddressCategory\": \"APPOINTMENT\\/SCHEDULING\", \"confidentialAddressCategoryCd\": 2, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-3\" }, { \"confidentialAddressCategory\": \"COPAYMENTS\\/VETERAN BILLING\", \"confidentialAddressCategoryCd\": 3, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-4\" }, { \"confidentialAddressCategory\": \"ALL OTHERS\", \"confidentialAddressCategoryCd\": 5, \"confidentialCategoryActive\": \"YES\", \"confidentialCategoryActiveCd\": \"Y\", \"resourceId\": \"V-500-2-100848-2.141-5\" } ] }, \"country\": \"USA\", \"countryId\": 1, \"county\": \"\", \"dateOfBirth\": \"03\\/03\\/1960\", \"dateOfBirthFHIR\": \"1960-03-03\", \"dateOfBirthFM\": 2600303, \"dateOfBirthHL7\": 19600303, \"dateOfDeath\": \"\", \"dateOfDeathFHIR\": \"\", \"dateOfDeathFM\": \"\", \"dateOfDeathHL7\": \"\", \"deathEnteredBy\": \"\", \"deathEnteredById\": \"\", \"emailAddress\": \"\", \"emergencyResponseIndicator\": \"\", \"emergencyResponseIndicatorCd\": \"\", \"employerCity\": \"\", \"employerName\": \"\", \"employerPhoneNumber\": \"\", \"employerState\": \"\", \"employerStateId\": \"\", \"employerStreetLine1\": \"\", \"employerStreetLine2\": \"\", \"employerStreetLine3\": \"\", \"employerZip4\": \"\", \"employerZipCode\": \"\", \"employmentStatus\": \"\", \"employmentStatusCd\": \"\", \"emrCity\": \"ANN ARBOR\", \"emrContactSameAsNok\": \"\", \"emrContactSameAsNokCd\": \"\", \"emrName\": \"EHMP,BROTHER\", \"emrPhoneNumber\": \"555-555-8765\", \"emrRelationshipToPatient\": \"BROTHER\", \"emrState\": \"MICHIGAN\", \"emrStateId\": 26, \"emrStreetAddressLine1\": \"54321 PARK WAY\", \"emrStreetAddressLine2\": \"\", \"emrStreetAddressLine3\": \"\", \"emrWorkPhoneNumber\": \"\", \"emrZip4\": 48103, \"emrZipCode\": 48103, \"ethnicityInformations\": { \"ethnicityInformation\": [ { \"ethnicityInformation\": \"NOT HISPANIC OR LATINO\", \"ethnicityInformationId\": 2, \"methodOfCollection\": \"SELF IDENTIFICATION\", \"methodOfCollectionId\": 1, \"resourceId\": \"V-500-2-100848-2.06-2\" } ] }, \"fatherSName\": \"EHMP,FATHER\", \"fullICN\": \"5000000352V586511\", \"governmentAgency\": \"\", \"governmentAgencyCd\": \"\", \"icnChecksum\": 586511, \"initlast4\": \"E0005\", \"integrationControlNumber\": 5000000352, \"kAddressSameAsPatientS\": \"YES\", \"kAddressSameAsPatientSCd\": \"Y\", \"kCity\": \"ANN ARBOR\", \"kNameOfPrimaryNok\": \"EHMP,BETTY\", \"kPhoneNumber\": \"555-555-1234\", \"kRelationshipToPatient\": \"SPOUSE\", \"kState\": \"MICHIGAN\", \"kStateId\": 26, \"kStreetAddressLine1\": \"12345 ANY STREET\", \"kStreetAddressLine2\": \"\", \"kStreetAddressLine3\": \"\", \"kWorkPhoneNumber\": \"\", \"kZip4\": 48103, \"kZipCode\": 48103, \"maritalStatus\": \"MARRIED\", \"maritalStatusId\": 2, \"motherSMaidenName\": \"CHDR,MOTHER\", \"motherSName\": \"EHMP,MOTHER\", \"multipleBirthIndicator\": \"NO\", \"multipleBirthIndicatorCd\": \"N\", \"name\": \"EHMP,FIVE\", \"occupation\": \"\", \"pagerNumber\": \"\", \"patientIen\": 100848, \"phoneNumberCellular\": \"555-555-1122\", \"phoneNumberResidence\": \"555-555-1234\", \"phoneNumberWork\": \"555-555-4321\", \"placeOfBirthCity\": \"ANN ARBOR, MI\", \"placeOfBirthState\": \"MICHIGAN\", \"placeOfBirthStateId\": 26, \"postalCode\": \"\", \"preferredFacility\": \"\", \"preferredFacilityId\": \"\", \"province\": \"\", \"race\": \"\", \"raceId\": \"\", \"raceInformations\": { \"raceInformation\": [ { \"methodOfCollection\": \"SELF IDENTIFICATION\", \"methodOfCollectionId\": 1, \"raceInformation\": \"BLACK OR AFRICAN AMERICAN\", \"raceInformationId\": 11, \"resourceId\": \"V-500-2-100848-2.02-11\" } ] }, \"religiousPreference\": \"CHRISTIAN (NON-SPECIFIC)\", \"remarks\": \"\", \"resourceId\": \"V-500-2-100848\", \"resourceType\": \"Patient\", \"selfIdentifiedGender\": \"\", \"selfIdentifiedGenderCd\": \"\", \"sex\": \"MALE\", \"sexCd\": \"M\", \"socialSecurityNumber\": 666110005, \"state\": \"MICHIGAN\", \"stateId\": 26, \"streetAddressLine1\": \"12345 ANY STREET\", \"streetAddressLine2\": \"\", \"streetAddressLine3\": \"\", \"tempAddressActive\": \"YES\", \"tempAddressActiveCd\": \"Y\", \"tempAddressCountry\": \"USA\", \"tempAddressCountryId\": 1, \"tempAddressCounty\": 161, \"tempAddressEndDate\": \"\", \"tempAddressEndDateFHIR\": \"\", \"tempAddressEndDateFM\": \"\", \"tempAddressEndDateHL7\": \"\", \"tempAddressPostalCode\": \"\", \"tempAddressProvince\": \"\", \"tempAddressStartDate\": \"MAR 14,2015\", \"tempAddressStartDateFHIR\": \"2015-03-14\", \"tempAddressStartDateFM\": 3150314, \"tempAddressStartDateHL7\": 20150314, \"tempCity\": \"ANN ARBOR\", \"tempPhoneNumber\": \"555-555-9876\", \"tempState\": \"MICHIGAN\", \"tempStateId\": 26, \"tempStreetLine1\": \"98765 NHCU STREET\", \"tempStreetLine2\": \"ROOM B-23\", \"tempStreetLine3\": \"\", \"tempZip4\": 48103, \"tempZipCode\": 48103, \"terminalDigitOfSsn\": \"050011666\", \"testPatientIndicator\": \"\", \"testPatientIndicatorCd\": \"\", \"veteranYN\": \"YES\", \"veteranYNCd\": \"Y\", \"zip4\": \"\", \"zipCode\": \"\" } } ] }";

        PatientParser parser = new PatientParser();

        Optional<Patient> patient = parser.parseSingle(input);

        Patient result = patient.get();

        List<HumanName> names = result.getName();

        Assert.assertEquals("LastName is EHMP", "EHMP", names.get(0).getFamily());
        Assert.assertEquals("FirstName is FIVE", "FIVE", names.get(0).getGiven().get(0).getValue());

        Assert.assertEquals("ID is ICN", "5000000352V586511", result.getIdentifier().get(0).getValue());
        Assert.assertEquals("SSN is 666110005", "666110005", result.getIdentifier().get(1).getValue());
        Assert.assertEquals("Gender is MALE", AdministrativeGender.MALE, result.getGender());

        Date expectedDate = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            expectedDate = sdf.parse("03-3-1960");
        } catch (ParseException px) {}

        Assert.assertEquals("Birthdate is 3/3/1960", expectedDate, result.getBirthDate());

        Address address = result.getAddress().get(0);
        Assert.assertEquals("City is ANN ARBOR", "ANN ARBOR", address.getCity());
        Assert.assertEquals("State is MICHIGAN", "MICHIGAN", address.getState());
        Assert.assertNull("Postal is empty", address.getPostalCode());
    }

    @Test
    public void TestInvalidPatientParse () {

        String input = "-1^Patientidentifiernotrecognised";

        PatientParser parser = new PatientParser();

        Optional<Patient> patient = parser.parseSingle(input);

        Assert.assertFalse(patient.isPresent());
    }


    @Test
    public void TestSuccesfulPatientByCodeParse() {
        String input = "38341003^3043460050V279846|ZZZRETTWOSIXTYTWO,PATIENT^5000000359V775335|DIABETIC,PATIENT MALE^5000001533V676621|DEPRESSION,PATIENT FEMALE^5000001534V744140|HYPERTENSION,PATIENT FEMALE^6343735560V532404|ONEHUNDREDEIGHTYFOUR,PATIENT";

        PatientParser parser = new PatientParser();

        List<Patient> result = parser.parseCodeList(input);

        Assert.assertEquals("Correct number of items", 5, result.size());
    }

    @Test
    public void TestAllPatientsSuccessfullParse() {
        String input = "1003672118V388695^ZZZRETFIVEFORTYSEVEN,PATIENT^^MALE^11/05/1938^,,,|1005701355V934125^BCMA,EIGHTYTWO-PATIENT^^MALE^11/16/1944^,,,|1006145121V631417^BHIEPATIENT,J TEN^^MALE^12/28/1933^,,,|1006147126V079083^BHIEPATIENT,I NINE^^MALE^02/10/1995^,,,|1006147276V569483^BHIEPATIENT,H EIGHT^^MALE^02/10/1965^,,,|1006151329V503966^BHIEPATIENT,F SIX^^MALE^02/15/1997^,,,|1006152719V948936^BHIEPATIENT,G SEVEN^^MALE^04/05/1951^,,,|1006167324V385420^BHIEPATIENT,C THREE^^MALE^09/08/1962^,,,|1006170580V294705^BHIEPATIENT,E FIVE^^MALE^02/09/1984^,,,";

        PatientParser parser = new PatientParser();

        List<Patient> result = parser.parseList(input);

        Assert.assertEquals("Correct number of patients", 9, result.size());
    }

    @Test
    public void TestAllPatientsNoInput() {
        String input = "";

        PatientParser parser = new PatientParser();

        List<Patient> result = parser.parseList(input);

        Assert.assertEquals("Correct number of patients", 0, result.size());
    }

    @Test
    public void TestPatientParseNoGender () {

        String input = "{ \"Patients\": [ { \"Patient\": { \"age\": 84, \"city\": \"\", \"country\": \"\", \"countryId\": \"\", \"county\": \"\", \"dateOfBirth\": \"04\\/07\\/1935\", \"dateOfBirthFHIR\": \"1935-04-07\", \"dateOfBirthFM\": 2350407, \"dateOfBirthHL7\": 19350407, \"dateOfDeath\": \"\", \"dateOfDeathFHIR\": \"\", \"dateOfDeathFM\": \"\", \"dateOfDeathHL7\": \"\", \"deathEnteredBy\": \"PROGRAMMER,TWENTYEIGHT\", \"deathEnteredById\": 923, \"emailAddress\": \"\", \"emergencyResponseIndicator\": \"\", \"emergencyResponseIndicatorCd\": \"\", \"employerCity\": \"\", \"employerName\": \"\", \"employerPhoneNumber\": \"\", \"employerState\": \"\", \"employerStateId\": \"\", \"employerStreetLine1\": \"\", \"employerStreetLine2\": \"\", \"employerStreetLine3\": \"\", \"employerZip4\": \"\", \"employerZipCode\": \"\", \"employmentStatus\": \"\", \"employmentStatusCd\": \"\", \"emrCity\": \"\", \"emrContactSameAsNok\": \"\", \"emrContactSameAsNokCd\": \"\", \"emrName\": \"\", \"emrPhoneNumber\": \"\", \"emrRelationshipToPatient\": \"\", \"emrState\": \"\", \"emrStateId\": \"\", \"emrStreetAddressLine1\": \"\", \"emrStreetAddressLine2\": \"\", \"emrStreetAddressLine3\": \"\", \"emrWorkPhoneNumber\": \"\", \"emrZip4\": \"\", \"emrZipCode\": \"\", \"fatherSName\": \"\", \"fullICN\": \"3042254422V310041\", \"governmentAgency\": \"\", \"governmentAgencyCd\": \"\", \"icnChecksum\": 310041, \"initlast4\": \"Z9087\", \"integrationControlNumber\": 3042254422, \"kAddressSameAsPatientS\": \"\", \"kAddressSameAsPatientSCd\": \"\", \"kCity\": \"\", \"kNameOfPrimaryNok\": \"\", \"kPhoneNumber\": \"\", \"kRelationshipToPatient\": \"\", \"kState\": \"\", \"kStateId\": \"\", \"kStreetAddressLine1\": \"\", \"kStreetAddressLine2\": \"\", \"kStreetAddressLine3\": \"\", \"kWorkPhoneNumber\": \"\", \"kZip4\": \"\", \"kZipCode\": \"\", \"maritalStatus\": \"\", \"maritalStatusId\": \"\", \"motherSMaidenName\": \"\", \"motherSName\": \"\", \"multipleBirthIndicator\": \"\", \"multipleBirthIndicatorCd\": \"\", \"name\": \"ZZZRETIREDTHIRTEEN,PATIENT\", \"nameFirst\": \"PATIENT\", \"nameLast\": \"ZZZRETIREDTHIRTEEN\", \"nameMiddle\": \"\", \"occupation\": \"\", \"pagerNumber\": \"\", \"patientIen\": 280, \"phoneNumberCellular\": \"\", \"phoneNumberResidence\": \"\", \"phoneNumberWork\": \"\", \"placeOfBirthCity\": \"\", \"placeOfBirthState\": \"\", \"placeOfBirthStateId\": \"\", \"postalCode\": \"\", \"preferredFacility\": \"\", \"preferredFacilityId\": \"\", \"province\": \"\", \"race\": \"\", \"raceId\": \"\", \"religiousPreference\": \"\", \"remarks\": \"\", \"resourceId\": \"V-500-2-280\", \"resourceType\": \"Patient\", \"selfIdentifiedGender\": \"\", \"selfIdentifiedGenderCd\": \"\", \"sex\": \"\", \"sexCd\": \"\", \"socialSecurityNumber\": 666569087, \"state\": \"\", \"stateId\": \"\", \"streetAddressLine1\": \"\", \"streetAddressLine2\": \"\", \"streetAddressLine3\": \"\", \"tempAddressActive\": \"\", \"tempAddressActiveCd\": \"\", \"tempAddressCountry\": \"\", \"tempAddressCountryId\": \"\", \"tempAddressCounty\": \"\", \"tempAddressEndDate\": \"\", \"tempAddressEndDateFHIR\": \"\", \"tempAddressEndDateFM\": \"\", \"tempAddressEndDateHL7\": \"\", \"tempAddressPostalCode\": \"\", \"tempAddressProvince\": \"\", \"tempAddressStartDate\": \"\", \"tempAddressStartDateFHIR\": \"\", \"tempAddressStartDateFM\": \"\", \"tempAddressStartDateHL7\": \"\", \"tempCity\": \"\", \"tempPhoneNumber\": \"\", \"tempState\": \"\", \"tempStateId\": \"\", \"tempStreetLine1\": \"\", \"tempStreetLine2\": \"\", \"tempStreetLine3\": \"\", \"tempZip4\": \"\", \"tempZipCode\": \"\", \"terminalDigitOfSsn\": 879056666, \"testPatientIndicator\": \"\", \"testPatientIndicatorCd\": \"\", \"veteranYN\": \"YES\", \"veteranYNCd\": \"Y\", \"zip4\": \"\", \"zipCode\": \"\" } } ] }";

        PatientParser parser = new PatientParser();

        Optional<Patient> patient = parser.parseSingle(input);

        Patient result = patient.get();
        Assert.assertEquals("Gender is Unknown", AdministrativeGender.UNKNOWN, result.getGender());

    }
}
