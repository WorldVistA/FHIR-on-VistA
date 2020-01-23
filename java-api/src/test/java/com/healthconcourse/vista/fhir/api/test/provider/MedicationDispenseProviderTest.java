package com.healthconcourse.vista.fhir.api.test.provider;

import com.healthconcourse.vista.fhir.api.Application;
import com.healthconcourse.vista.fhir.api.test.TestInjectionContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for MedicationAdministration Provider
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties= "spring.main.allow-bean-definition-overriding=true", classes = {Application.class, TestInjectionContext.class})
public class MedicationDispenseProviderTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testMedicationDispenseByPatientSuccess() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/MedicationDispense?patient=5000001533V676621", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testMedicationDispenseBySubjectSuccess() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/MedicationDispense?subject=5000001533V676621", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testMedicationDispenseBySubjectNotFound() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/MedicationDispense?subject=44", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testMedicationDispenseSearchNoParameters() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/MedicationDispense", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
