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

import com.healthconcourse.vista.fhir.api.parser.LocationParser;
import org.hl7.fhir.dstu3.model.Location;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocationParserTest {

    @Test
    public void parseLocationSuccess() {

        String input = "{ \"Location\": [ { \"Hosploc\": { \"abbreviation\": \"A-FM\", \"agency\": \"\", \"agencyId\": \"\", \"availabilityFlag\": \"\", \"availabilityFlagFHIR\": \"\", \"availabilityFlagFM\": \"\", \"availabilityFlagHL7\": \"\", \"categoryOfVisit\": \"\", \"clinicMeetsAtThisFacility\": \"\", \"clinicMeetsAtThisFacilityCd\": \"\", \"creditStopCode\": \"\", \"creditStopCodeId\": \"\", \"defaultProvider\": \"\", \"defaultProviderId\": \"\", \"defaultProviderNPI\": \"\", \"division\": \"VEHU DIVISION\", \"divisionId\": 1, \"hosplocIen\": 77, \"inactivateDate\": \"\", \"inactivateDateFHIR\": \"\", \"inactivateDateFM\": \"\", \"inactivateDateHL7\": \"\", \"institution\": \"ABILENE (CAA)\", \"institutionId\": 17034, \"module\": \"\", \"moduleId\": \"\", \"name\": \"ALBANY FILE ROOM\", \"nonCountClinicYOrN\": \"\", \"nonCountClinicYOrNCd\": \"\", \"physicalLocation\": \"\", \"principalClinic\": \"\", \"principalClinicId\": \"\", \"prvYearCreditStopCode\": \"\", \"prvYearCreditStopCodeId\": \"\", \"prvYearStopCode\": \"\", \"prvYearStopCodeId\": \"\", \"reactivateDate\": \"\", \"reactivateDateFHIR\": \"\", \"reactivateDateFM\": \"\", \"reactivateDateHL7\": \"\", \"resourceId\": \"V-500-44-77\", \"resourceType\": \"Organization\", \"service\": \"\", \"serviceCd\": \"\", \"specialAmisStop\": \"\", \"specialAmisStopCd\": \"\", \"stopCodeNumber\": \"\", \"stopCodeNumberId\": \"\", \"telephone\": \"\", \"telephoneExtension\": \"\", \"treatingSpecialty\": \"\", \"treatingSpecialtyId\": \"\", \"type\": \"FILE AREA\", \"typeCd\": \"F\", \"typeExtension\": \"FILE AREA\", \"typeExtensionId\": 6, \"visitLocation\": \"\", \"wardLocationFilePointer\": \"\", \"wardLocationFilePointerId\": \"\" }, \"institution\": { \"acosHospitalId\": \"\", \"agencyCode\": \"\", \"agencyCodeCd\": \"\", \"billingFacilityName\": \"\", \"city\": \"\", \"cityMailing\": \"\", \"country\": \"\", \"countryId\": \"\", \"currentLocation\": \"\", \"currentLocationCd\": \"\", \"district\": \"\", \"domain\": \"CAA.DOMAIN.GOV\", \"domainId\": 407, \"facilityDeaExpirationDate\": \"\", \"facilityDeaExpirationDateFHIR\": \"\", \"facilityDeaExpirationDateFM\": \"\", \"facilityDeaExpirationDateHL7\": \"\", \"facilityDeaNumber\": \"\", \"facilityType\": \"ISC\", \"facilityTypeId\": 34, \"identifiers\": { \"identifier\": [ { \"codingSystem\": \"VASTANUM\", \"effectiveDateTime\": \"\", \"effectiveDateTimeFHIR\": \"\", \"effectiveDateTimeFM\": \"\", \"effectiveDateTimeHL7\": \"\", \"id\": 998, \"resourceId\": \"V-500-4-17034-4.9999-1\", \"status\": \"\", \"statusCd\": \"\" } ] }, \"inactiveFacilityFlag\": \"\", \"inactiveFacilityFlagCd\": \"\", \"institutionAssociationTypess\": { \"institutionAssociationTypes\": [ { \"name\": \"VISN\", \"number\": 1, \"resourceId\": \"V-500-4-17034-4.05-1\" }, { \"name\": \"PARENT FACILITY\", \"number\": 2, \"resourceId\": \"V-500-4-17034-4.05-2\" } ] }, \"locationTimezone\": \"\", \"locationTimezoneId\": \"\", \"multiDivisionFacility\": \"\", \"multiDivisionFacilityCd\": \"\", \"npi\": \"\", \"officialVaName\": \"\", \"pointerToAgency\": \"\", \"pointerToAgencyId\": \"\", \"region\": \"\", \"reportingStation\": \"\", \"reportingStationId\": \"\", \"resourceId\": \"V-500-4-17034\", \"resourceType\": \"Organization\", \"shortName\": \"CAMP A\", \"siteIen\": 17034, \"siteName\": \"ABILENE (CAA)\", \"stAddr1Mailing\": \"\", \"stAddr2Mailing\": \"\", \"state\": \"KANSAS\", \"stateId\": 20, \"stateMailing\": \"\", \"stateMailingId\": \"\", \"stationNumber\": 998, \"status\": \"National\", \"statusCd\": \"N\", \"streetAddr1\": \"\", \"streetAddr2\": \"\", \"timezoneException\": \"\", \"timezoneExceptionCd\": \"\", \"vaTypeCode\": \"\", \"vaTypeCodeCd\": \"\", \"zip\": \"\", \"zipMailing\": \"\" } } ] }";

        LocationParser parser = new LocationParser();

        Optional<Location> result = parser.parseSingleLocation(input);

        assertTrue(result.isPresent());

        Location loc = result.get();

        assertEquals("Name is correct", "ALBANY FILE ROOM", loc.getName());
        assertEquals("ID is correct", "V-500-44-77", loc.getId());
        assertEquals("Organization is correct", "ABILENE (CAA)", loc.getManagingOrganization().getDisplay());
        assertEquals("State is correct", "KANSAS", loc.getAddress().getState());
    }

    @Test
    public void parseLocationMissing() {

        String input = "";

        LocationParser parser = new LocationParser();

        Optional<Location> result = parser.parseSingleLocation(input);

        assertTrue(!result.isPresent());
    }

    @Test
    public void parseLocationBadData() {

        String input = "-1^HospitalLocationnamenotrecognised^ALBANYFILEROOME";

        LocationParser parser = new LocationParser();

        Optional<Location> result = parser.parseSingleLocation(input);

        assertTrue(!result.isPresent());
    }

    @Test
    public void parseLocationVistaError() {
        String input = "{ \"Location\": [ { \"Site\": { \"ERROR\": \"\" } } ] }";

        LocationParser parser = new LocationParser();

        Optional<Location> result = parser.parseSingleLocation(input);

        assertTrue(!result.isPresent());
    }
}
