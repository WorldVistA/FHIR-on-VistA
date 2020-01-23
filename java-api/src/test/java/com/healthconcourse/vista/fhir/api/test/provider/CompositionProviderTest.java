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
 * Unit tests for Composition Provider
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties= "spring.main.allow-bean-definition-overriding=true", classes = {Application.class, TestInjectionContext.class})
public class CompositionProviderTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCompositionByPatientSuccess() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Composition?patient=10112V399621", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testCompositionBySubjectSuccess() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Composition?subject=10112V399621", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testCompositionBySubjectNotFound() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Composition?subject=44", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCompositionSearchNoParameters() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/Composition", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
