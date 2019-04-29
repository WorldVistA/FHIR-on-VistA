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
package com.healthconcourse.vista.fhir.api.test;

import com.healthconcourse.vista.fhir.api.vista.WebVistaData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WebVistaDataTest {

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private final String DEFAULT_BODY = "12345^";
    private MockWebServer mServer = new MockWebServer();

    @Before
    public void setup(){

    }

    @After
    public void tearDown() throws IOException {
        mServer.close();
    }

    @Test
    public void getPatientByIcnSuccess() throws IOException {

        String body = "12345^";

        setServer(DEFAULT_BODY, "/DHPPATDEMICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getPatientData("12345");

        assertEquals(body, result);
    }

    @Test
    public void getPatientByIcnNotFound() throws IOException {
        setServer("This is an error message", "/DHPPATDEMICN?ICN=12345", 404);

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getPatientData("12345");

        assertEquals("", result);
    }

    @Test
    public void getPatientByIcnError() throws IOException {
        setServer("This is an error message", "/DHPPATDEMICN?ICN=12345", 500);

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getPatientData("12345");

        assertEquals("", result);
    }

    @Test
    public void getPatientByCriteriaSuccess() throws IOException, ParseException {

        setServer(DEFAULT_BODY, "/DHPPATDEM?NAME=SMITH&SSN=123456789&DOB=3010101&GENDER=F&MMDNM=JONES");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getPatientData("SMITH", "123456789", DATE_FORMAT.parse("10/10/1930"), Enumerations.AdministrativeGender.FEMALE);

        assertNotEquals("", result);
    }

    @Test
    public void getConditionByIcnSuccess() throws IOException {
        String body = "23456^";

        setServer(body, "/DHPPATCONICN?ICN=23456");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getConditions("23456");

        assertEquals(body, result);
    }

    @Test
    public void getConditiontByCriteriaSuccess() throws IOException, ParseException {
        setServer(DEFAULT_BODY, "/DHPPATCON?NAME=SMITH&SSN=123456789&DOB=3010101&GENDER=F&MMDNM=JONES");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getConditions("SMITH", "123456789", DATE_FORMAT.parse("10/10/1930"), Enumerations.AdministrativeGender.FEMALE);

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getPatientByConditionCodeSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATS4CON?SCT=112233");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getPatientsByCondition("112233");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getObservationsByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATVITICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        //Dates were never coded in Vista
        String result = data.getVitalsObservationsByIcn("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getObservationsByCriteriaSuccess() throws IOException, ParseException {
        setServer(DEFAULT_BODY, "/DHPPATVIT?NAME=SMITH&SSN=123456789&DOB=3010101&GENDER=F&MMDNM=JONES");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getObservationsByCriteria("SMITH", "123456789", DATE_FORMAT.parse("10/10/1930"), Enumerations.AdministrativeGender.FEMALE);

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getEncountersByIcnSuccess() throws IOException, InterruptedException {

        String path = "/DHPPATENCICN?ICN=12345";

        setServer(DEFAULT_BODY, path);

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getEncountersByPatient("12345");
        RecordedRequest request = mServer.takeRequest();
        mServer.close();

        assertEquals(path, request.getPath());
        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getMedicationStatementByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATMEDSICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getMedicationStatement("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getMedicationAdministrationByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATMEDAICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getMedicationAdministration("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getMedicationDispenseByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATMEDDICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getMedicationAdministration("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getLocationByNameSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPHLOCINSTHLOCNAM?HLOC=ALBANY%20FILE%20ROOM");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getLocationByName("ALBANY FILE ROOM");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getLocationByNameFailure() throws IOException {
        setServer("NotExpectedResult", "/DHPHLOCINSTHLOCNAM?HLOC=ALBANY%20FILE%20ROOM");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getLocationByName("ALBANY FILE ROOM");

        assertNotEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getFlagnByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATFLGICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getFlagByIcn("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getFlagByIcnFailure() throws IOException {
        setServer("NotExpectedResult", "/DHPPATFLGICN?ICN=54321");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getLocationByName("54321");

        assertNotEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getAppointmentByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATAPTICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getFlagByIcn("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getAllergyByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATALLICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getFlagByIcn("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getGoalByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATGOLICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getGoal("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getDiagnosticReportByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATDXRICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getDiagnosticReport("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    @Test
    public void getCarePlanByIcnSuccess() throws IOException {
        setServer(DEFAULT_BODY, "/DHPPATGOLICN?ICN=12345");

        WebVistaData data = new WebVistaData(getServerUrl(mServer));

        String result = data.getCarePlan("12345");

        assertEquals(result, DEFAULT_BODY);
    }

    private String getServerUrl(MockWebServer server) {
        return String.format("http://%s:%s/", server.getHostName(), server.getPort());
    }

    private void setServer(String body, String path) {
        setServer(body, path, 200);
    }

    private void setServer(String body, String path, int responseCode) {
        mServer.enqueue(new MockResponse().setBody(body).setResponseCode(responseCode));
        mServer.url(path);
    }
}
