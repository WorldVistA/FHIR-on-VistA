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

import com.healthconcourse.vista.fhir.api.parser.ConditionParser;
import com.healthconcourse.vista.fhir.api.parser.PatientParser;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
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

        String input = "5000001534V744140^HYPERTENSION,PATIENT FEMALE^555-555-1938^FEMALE^09/13/1928^98765 Street St,,,";

        PatientParser parser = new PatientParser();

        Optional<Patient> patient = parser.parseSingle(input);

        Patient result = patient.get();

        List<HumanName> names = result.getName();

        Assert.assertEquals("LastName is HYPERTENSION", "HYPERTENSION", names.get(0).getFamily());
        Assert.assertEquals("FirstName is PATIENT FEMALE", "PATIENT FEMALE", names.get(0).getGiven().get(0).getValue());

        Assert.assertEquals("ID is ICN", "5000001534V744140", result.getIdentifier().get(0).getValue());
        Assert.assertEquals("SSN is 555-555-1938", "555-555-1938", result.getIdentifier().get(1).getValue());
        Assert.assertEquals("Gender is FEMALE", AdministrativeGender.FEMALE, result.getGender());

        Date expectedDate = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            expectedDate = sdf.parse("13-9-1928");
        } catch (ParseException px) {}

        Assert.assertEquals("Birthdate is 9/13/1928", expectedDate, result.getBirthDate());
    }

    @Test
    public void TestSuccessfulPatientParseWithAddress () {

        String input = "5000001519V211431^CDSSEVEN,PATIENT^9995263294^FEMALE^12/28/1986^780 FIRST STREET,,,^ANYTOWN^OKLAHOMA^99999";

        PatientParser parser = new PatientParser();

        Optional<Patient> patient = parser.parseSingle(input);

        Patient result = patient.get();

        Address address = result.getAddress().get(0);

        Assert.assertEquals("City is ANYTOWN", "ANYTOWN", address.getCity());
        Assert.assertEquals("State is OKLAHOMA", "ANYTOWN", address.getCity());
        Assert.assertEquals("City is 99999", "ANYTOWN", address.getCity());
    }

    @Test
    public void TestInvalidPatientParse () {

        String input = "ThisIsNotDataFromVista";

        PatientParser parser = new PatientParser();

        Optional<Patient> patient = parser.parseSingle(input);

        Assert.assertFalse(patient.isPresent());
    }

    @Test
    public void TestSuccessfulCodeParse() {

        String input = "7849159139V519746^V-999-9000011-1225^473.9;UNSPECIFIED SINUSITIS (CHRONIC);ICD^19381021^;^INACTIVE^^problem|V-999-9000011-1226^R69.;Illness, unspecified;10D^19810323065524-0400^68496003;Polyp of colon^ACTIVE^^problem|V-999-9000011-1227^R69.;Illness, unspecified;10D^19900304121344-0400^53741008;Coronary arteriosclerosis^ACTIVE^^problem|V-999-9000011-1228^R69.;Illness, unspecified;10D^19970511231944-0400^68496003;Polyp of colon^ACTIVE^^problem|V-999-9000011-1229^R69.;Illness, unspecified;10D^19980625101218-0400^713197008;Recurrent rectal polyp^ACTIVE^^problem|V-999-9000011-1230^R69.;Illness, unspecified;10D^19980702065943-0400^109838007;Overlapping malignant neoplasm of colon^ACTIVE^^problem|";

        ConditionParser parser = new ConditionParser();

        List<Condition> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 6, result.size());
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
}
