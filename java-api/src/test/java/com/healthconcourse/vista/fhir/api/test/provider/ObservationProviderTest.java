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
package com.healthconcourse.vista.fhir.api.test.provider;

import com.healthconcourse.vista.fhir.api.Application;
import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.test.TestInjectionContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ObservationProvider
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties= "spring.main.allow-bean-definition-overriding=true", classes = {Application.class, TestInjectionContext.class})
public class ObservationProviderTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testObservationCriteriaNotFound() throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder();
        url.append("/api/Observation?name=NoOne&birthdate=");
        url.append(URLEncoder.encode("1961-11-11", HcConstants.UNICODE));
        url.append("&identifier=");
        url.append(URLEncoder.encode("555-555-1938", HcConstants.UNICODE));
        url.append("&gender=MALE");
        ResponseEntity<String> response = this.restTemplate.getForEntity(url.toString(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testObservationCriteriaSuccess() throws Exception {
        StringBuilder url = new StringBuilder();
        url.append("/api/Observation?name=SomeOne&birthdate=");
        url.append(URLEncoder.encode("1961-11-11", HcConstants.UNICODE));
        url.append("&identifier=");
        url.append(URLEncoder.encode("555-555-1938", HcConstants.UNICODE));
        url.append("&gender=FEMALE");
        ResponseEntity<String> response = this.restTemplate.getForEntity(url.toString(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
