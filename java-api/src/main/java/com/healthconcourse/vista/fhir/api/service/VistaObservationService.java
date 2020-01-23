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
package com.healthconcourse.vista.fhir.api.service;

import com.healthconcourse.vista.fhir.api.parser.ObservationParser;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VistaObservationService implements ObservationService {
    private static final Logger LOG = LoggerFactory.getLogger(VistaObservationService.class);
    private VistaData service;

    public VistaObservationService(VistaData data) {
        service = data;
    }

    public List<Observation> getObservationsByCriteria(String name, Date dob, Enumerations.AdministrativeGender gender, String ssn) {

        String httpBody = service.getObservationsByCriteria(name, ssn, dob, gender);

        ObservationParser parser = new ObservationParser();

        return parser.parseVitalsList(httpBody);
    }

    @Override
    public List<Observation> getObservationsByPatient(String id) {
        List<Observation> results = new ArrayList<>();

        CompletableFuture<List<Observation>> vitals = CompletableFuture.supplyAsync(() -> service.getVitalsObservationsByIcn(id))
                .thenApply(x -> {
                    ObservationParser parser = new ObservationParser();
                    return parser.parseVitalsList(x);
                })
                .whenComplete((observations, exception) -> {
                    if (exception == null) {
                        results.addAll(observations);
                    } else {
                        LOG.error("Unable to fetch or parse Vitals", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse Vitals", ex);
                    return null;
                });


        CompletableFuture<List<Observation>> labs = CompletableFuture.supplyAsync(() -> service.getLabObservationsByIcn(id))
                .thenApply(x -> {
                    ObservationParser parser = new ObservationParser();
                    return parser.parseLabsList(x);
                })
                .whenComplete((observations, exception) -> {
                    if (exception == null) {
                        results.addAll(observations);
                    } else {
                        LOG.error("Unable to fetch or parse labs", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse labs", ex);
                    return null;
                });


        CompletableFuture<List<Observation>> healthFactors = CompletableFuture.supplyAsync(() -> service.getHealthFactorObservationsByIcn(id))
                .thenApply(x -> {
                    ObservationParser parser = new ObservationParser();
                    return parser.parseHealthFactorsList(x);
                })
                .whenComplete((observations, exception) -> {
                    if (exception == null) {
                        results.addAll(observations);
                    } else {
                        LOG.error("Unable to fetch or parse Health Factors", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse Health Factors", ex);
                    return null;
                });

        CompletableFuture<List<Observation>> mentalHealth = CompletableFuture.supplyAsync(() -> service.getMentalHealthObservationsByIcn(id))
                .thenApply(x -> {
                    ObservationParser parser = new ObservationParser();
                    return parser.parseMentalHealthList(x);
                })
                .whenComplete((observations, exception) -> {
                    if (exception == null) {
                        results.addAll(observations);
                    } else {
                        LOG.error("Unable to fetch or parse Mental Health", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse Mental Health", ex);
                    return null;
                });

        //Wait for completion
        CompletableFuture.allOf(vitals, labs, healthFactors, mentalHealth).join();

        return results;
    }
}
