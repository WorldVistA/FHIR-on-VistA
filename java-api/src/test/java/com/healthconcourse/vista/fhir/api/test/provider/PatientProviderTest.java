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
package com.healthconcourse.vista.fhir.api.test.provider;

import com.healthconcourse.vista.fhir.api.Application;
import com.healthconcourse.vista.fhir.api.test.TestInjectionContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for Patient Provider
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties= "spring.main.allow-bean-definition-overriding=true", classes = {Application.class, TestInjectionContext.class})
public class PatientProviderTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testPatientReadSuccess() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Patient/5000001534V744140/", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String json = response.getBody();
        String typeName = JsonPath.parse(json).read("$.resourceType");
        assertEquals("should return a patient type", "Patient", typeName);
    }

    @Test
    public void testPatientReadSuccessWithAddress() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Patient/5000001519V211431/", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String json = response.getBody();
        String state = JsonPath.parse(json).read("$.address[0].state");
        assertEquals("should return a patient type", "OKLAHOMA", state);
    }

    @Test
    public void testPatientReadMissing() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Patient/444/", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testAllPatientSuccess() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Patient", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testPatientGetConditionsSuccess() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Patient/5000001534V744140/Condition", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String json = response.getBody();
        int recordCount = JsonPath.parse(json).read("$.total");
        assertEquals("should return five records", 7, recordCount);
    }

    @Test
    public void testPatientGetConditionsNoneFound() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Patient/444/Condition", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testObservationByIcnNotFound() {
        String url = "/api/Patient/444/Observation";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testObservationByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/Observation";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testObservationByIcnAndCodeNotFound() {
        String url = "/api/Patient/444/Observation";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testObservationByIcnAndCodeSuccess() {
        String url = "/api/Patient/5000001534V744140/Observation?code=asdf";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testMedicationAdminByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/MedicationAdministration";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testMedicationAdminByIcnFailure() {
        String url = "/api/Patient/44/MedicationAdministration";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testMedicationStatementByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/MedicationStatement";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testMedicationStatementByIcnFailure() {
        String url = "/api/Patient/44/MedicationStatement";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testMedicationDispenseByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/MedicationDispense";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testMedicationDispenseByIcnFailure() {
        String url = "/api/Patient/44/MedicationDispense";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testPractitionerByIcnFailure() {
        String url = "/api/Patient/444/Practitioner";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testPractitionerByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/Practitioner";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testAppointmentByIcnFailure() {
        String url = "/api/Patient/444/Appointment";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testAppointmentByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/Appointment";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFlagByIcnFailure() {
        String url = "/api/Patient/444/Flag";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testFlagByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/Flag";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testAllergyByIcnFailure() {
        String url = "/api/Patient/444/AllergyIntolerance";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testAllergyByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/AllergyIntolerance";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testImmunizationByIcnFailure() {
        String url = "/api/Patient/444/Immunization";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testImmunizationByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/Immunization";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGoalByIcnSuccess() {
        String url = "/api/Patient/10104V248233/Goal";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGoalByIcnFailure() {
        String url = "/api/Patient/44/Goal";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDiagnosticReportByIcnSuccess() {
        String url = "/api/Patient/5000001534V744140/DiagnosticReport";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDiagnosticReportByIcnFailure() {
        String url = "/api/Patient/44/DiagnosticReport";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCarePlanByIcnSuccess() {
        String url = "/api/Patient/10104V248233/CarePlan";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testCarePlanByIcnFailure() {
        String url = "/api/Patient/44/CarePlan";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCompositionByIcnSuccess() {
        String url = "/api/Patient/10112V399621/Composition";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testCompositionByIcnFailure() {
        String url = "/api/Patient/44/Composition";
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
