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

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.data.Provider;
import com.healthconcourse.vista.fhir.api.parser.*;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.dstu3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class VistaPatientService implements PatientService {

    private static final Logger LOG = LoggerFactory.getLogger(VistaPatientService.class);
    private VistaData service;

    public VistaPatientService(VistaData data) {
        service = data;
    }

    @Override
    public Patient getPatientById(String icn) {

        String httpBody = service.getPatientData(icn);
        PatientParser parser = new PatientParser();
        Optional<Patient> patient = parser.parseSingle(httpBody);
        if (patient.isPresent()) {
            return patient.get();
        } else {
            //TODO: better here
            throw new ResourceNotFoundException(icn);
        }
    }

    @Override
    public List<Patient> findPatient(String name, Date dob, String ssn, Enumerations.AdministrativeGender gender) {

        String httpBody = service.getPatientData(name, ssn, dob, gender);

        PatientParser parser = new PatientParser();

        return parser.parseListFromSinglePatient(httpBody);
    }

    @Override
    public List<Patient> getAllPatients() {
        String httpBody = service.getAllPatients();

        PatientParser parser = new PatientParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<Condition> getCodesByIcn(String patientIcn) {

        String httpBody = service.getConditions(patientIcn);

        LOG.debug(httpBody);

        ConditionParser parser = new ConditionParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<Encounter> getEncountersForPatient(String code) {

        List<Encounter> results = new ArrayList<>();
        HashMap<String, List<Provider>> providerData = new HashMap();

        CompletableFuture<List<Encounter>> encounterFetcher = CompletableFuture.supplyAsync(() -> service.getEncountersByPatient(code))
                .thenApply(httpBody -> {
                    EncounterParser parser = new EncounterParser();
                    return parser.parseList(httpBody);
                })
                .whenComplete((encounters, exception) -> {
                    if (exception == null) {
                        results.addAll(encounters);
                    } else {
                        LOG.error("Unable to fetch or parse Encounters", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse Encounters", ex);
                    return null;
                });

        CompletableFuture<List<Provider>> practitionerFetcher = CompletableFuture.supplyAsync(() -> service.getProvidersByIcn(code))
                .thenApply(httpBody -> {
                    ProviderParser parser = new ProviderParser();
                    return parser.parseList(httpBody);
                })
                .whenComplete((providers, exception) -> {
                    if (exception == null) {
                        for (Provider item : providers) {

                            if (providerData.containsKey(item.getEncounter())) {
                                providerData.get(item.getEncounter()).add(item);
                            } else {
                                List<Provider> providerItem = new ArrayList<>();
                                providerItem.add(item);
                                providerData.put(item.getEncounter(), providerItem);
                            }
                        }
                    } else {
                        LOG.error("Unable to fetch or parse Providers Encounters", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse Providers Encounters", ex);
                    return null;
                });

        CompletableFuture.allOf(encounterFetcher, practitionerFetcher).join();

        for (Encounter encounter : results) {
            List<Provider> providers = providerData.get(parseEncounterId(encounter.getId()));
            if (providers != null) {
                for (Provider item : providers) {
                    encounter.addParticipant(getParticipant(item));
                }
            }
        }

        return results;
    }

    private Encounter.EncounterParticipantComponent getParticipant(Provider provider) {

        Encounter.EncounterParticipantComponent participant = new Encounter.EncounterParticipantComponent();
        participant.setIndividual(ResourceHelper.createReference(HcConstants.URN_VISTA_PROVIDER, provider.getVistaId(), provider.getName(), ResourceHelper.ReferenceType.PractitionerRole));

        if (provider.getRole().equalsIgnoreCase("Physician")) {
            participant.setType(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.PARTICIPANT_CODING_SYSTEM, "PPRF", "primary performer"));
        } else if (provider.getRole().equalsIgnoreCase("NURSE")) {
            participant.setType(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.PARTICIPANT_CODING_SYSTEM, "SPRF", "secondary performer"));
        } else {
            LOG.debug(provider.getRole());
            participant.setType(ResourceHelper.createSingleCodeableConceptAsList(HcConstants.PARTICIPANT_CODING_SYSTEM, "PART", "Participation"));
        }

        return participant;
    }

    private String parseEncounterId(String encounterId) {

        String[] parts = encounterId.split("_");
        if (parts.length == 3) {
            return parts[2];
        }

        return encounterId;
    }

    @Override
    public List<Observation> getObservationsByIcn(String id) {

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

    @Override
    public List<Observation> getObservationsByIcnAndCode(String id, String code) {

        String httpBody = service.getObservationsByIcnAndCode(id, code);

        ObservationParser parser = new ObservationParser();

        return parser.parseVitalsList(httpBody);
    }

    @Override
    public List<MedicationStatement> getMedicationStatement(String patientIcn) {

        String httpBody = service.getMedicationStatement(patientIcn);

        MedicationParser parser = new MedicationParser();

        return parser.parseMedicationStatement(httpBody);
    }

    @Override
    public List<MedicationDispense> getMedicationDispense(String patientIcn) {

        String httpBody = service.getMedicationAdministration(patientIcn);

        MedicationParser parser = new MedicationParser();

        return parser.parseMedicationDispense(httpBody);
    }

    @Override
    public List<MedicationAdministration> getMMedicationAdministration(String patientIcn) {

        String httpBody = service.getMedicationAdministration(patientIcn);

        MedicationParser parser = new MedicationParser();

        return parser.parseMedicationAdmin(httpBody);
    }

    @Override
    public List<Procedure> getProcedures(String patientIcn) {

        String httpBody = service.getProceduresByIcn(patientIcn);

        ProcedureParser parser = new ProcedureParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<Practitioner> getPractitionersById(String patientIcn) {

        String httpBody = service.getProvidersByIcn(patientIcn);

        PractitionerParser parser = new PractitionerParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<Flag> getFlagsByIcn(String patientIcn) {
        String httpBody = service.getFlagByIcn(patientIcn);

        FlagParser parser = new FlagParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<Appointment> getAppointmentsByIcn(String patientIcn) {
        String httpBody = service.getAppointmentsByIcn(patientIcn);

        AppointmentParser parser = new AppointmentParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<AllergyIntolerance> getAllergyByIcn(String patientIcn) {
        String httpBody = service.getAllergiesByIcn(patientIcn);

        AllergyParser parser = new AllergyParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<Immunization> getImmunizationsByIcn(String patientIcn) {
        String httpBody = service.getImmunizationsByIcn(patientIcn);

        ImmunizationParser parser = new ImmunizationParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<Goal> getGoal(String patientIcn) {

        String httpBody = service.getGoal(patientIcn);

        GoalParser parser = new GoalParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<DiagnosticReport> getDiagnosticReport(String patientIcn) {

        String httpBody = service.getDiagnosticReport(patientIcn);

        DiagnosticReportParser parser = new DiagnosticReportParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<CarePlan> getCarePlan(String patientIcn) {

        String httpBody = service.getCarePlan(patientIcn);

        CarePlanParser parser = new CarePlanParser();

        return parser.parseCarePlan(httpBody, patientIcn);
    }

    @Override
    public List<Composition> getTiuNotes(String patientIcn) {
        String httpBody = service.getTiuNotes(patientIcn);

        NoteParser parser = new NoteParser();

        return parser.parseList(httpBody);
    }

    @Override
    public List<DomainResource> getEverything(String patientIcn) {

        List<DomainResource> results = new ArrayList<>();

        final boolean[] patientFound = {false};

        CompletableFuture<Patient> patientCall = CompletableFuture.supplyAsync(() -> getPatientById(patientIcn))
                .whenComplete((patient, exception) -> {
                    if (exception == null) {
                        results.add(patient);
                        patientFound[0] = true;
                    } else {
                        LOG.error("Unable to find patient", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to find patient", ex);
                    return null;
                });

        CompletableFuture<List<Condition>> condition = CompletableFuture.supplyAsync(() -> getCodesByIcn(patientIcn))
                .whenComplete((conditions, exception) -> {
                    if (exception == null) {
                        results.addAll(conditions);
                    } else {
                        LOG.error("Unable to fetch or parse conditions", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse conditions", ex);
                    return null;
                });

        CompletableFuture<List<Observation>> observation = CompletableFuture.supplyAsync(() -> getObservationsByIcn(patientIcn))
                .whenComplete((observations, exception) -> {
                    if (exception == null) {
                        results.addAll(observations);
                    } else {
                        LOG.error("Unable to fetch or parse observations", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse observations", ex);
                    return null;
                });
        CompletableFuture<List<Encounter>> encounter = CompletableFuture.supplyAsync(() -> getEncountersForPatient(patientIcn))
                .whenComplete((encounters, exception) -> {
                    if (exception == null) {
                        results.addAll(encounters);
                    } else {
                        LOG.error("Unable to fetch or parse encounters", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse encounters", ex);
                    return null;
                });

        CompletableFuture<List<Procedure>> procedure = CompletableFuture.supplyAsync(() -> getProcedures(patientIcn))
                .whenComplete((procedures, exception) -> {
                    if (exception == null) {
                        results.addAll(procedures);
                    } else {
                        LOG.error("Unable to fetch or parse procedures", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or parse procedures", ex);
                    return null;
                });

        CompletableFuture<List<MedicationStatement>> medStatement = CompletableFuture.supplyAsync(() -> getMedicationStatement(patientIcn))
                .whenComplete((statements, exception) -> {
                    if (exception == null) {
                        results.addAll(statements);
                    } else {
                        LOG.error("Unable to fetch or medication statements", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch or medication statements", ex);
                    return null;
                });

        CompletableFuture<List<MedicationAdministration>> medAdmin = CompletableFuture.supplyAsync(() -> getMMedicationAdministration(patientIcn))
                .whenComplete((admins, exception) -> {
                    if (exception == null) {
                        results.addAll(admins);
                    } else {
                        LOG.error("Unable to fetch medication administrations", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch medication administrations", ex);
                    return null;
                });


        CompletableFuture<List<MedicationDispense>> medDispense = CompletableFuture.supplyAsync(() -> getMedicationDispense(patientIcn))
                .whenComplete((dispenses, exception) -> {
                    if (exception == null) {
                        results.addAll(dispenses);
                    } else {
                        LOG.error("Unable to fetch medication dispenses", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch medication dispenses", ex);
                    return null;
                });

        CompletableFuture<List<Flag>> flag = CompletableFuture.supplyAsync(() -> getFlagsByIcn(patientIcn))
                .whenComplete((flags, exception) -> {
                    if (exception == null) {
                        results.addAll(flags);
                    } else {
                        LOG.error("Unable to fetch flags", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch flags", ex);
                    return null;
                });


        CompletableFuture<List<Appointment>> appointment = CompletableFuture.supplyAsync(() -> getAppointmentsByIcn(patientIcn))
                .whenComplete((appointments, exception) -> {
                    if (exception == null) {
                        results.addAll(appointments);
                    } else {
                        LOG.error("Unable to fetch appointments", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch appointments", ex);
                    return null;
                });

        CompletableFuture<List<Practitioner>> practitioner = CompletableFuture.supplyAsync(() -> getPractitionersById(patientIcn))
                .whenComplete((practitioners, exception) -> {
                    if (exception == null) {
                        results.addAll(practitioners);
                    } else {
                        LOG.error("Unable to fetch practitioners", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch practitioners", ex);
                    return null;
                });

        CompletableFuture<List<AllergyIntolerance>> allergy = CompletableFuture.supplyAsync(() -> getAllergyByIcn(patientIcn))
                .whenComplete((allergies, exception) -> {
                    if (exception == null) {
                        results.addAll(allergies);
                    } else {
                        LOG.error("Unable to fetch allergies", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch allergies", ex);
                    return null;
                });

        CompletableFuture<List<Immunization>> immunization = CompletableFuture.supplyAsync(() -> getImmunizationsByIcn(patientIcn))
                .whenComplete((immunizations, exception) -> {
                    if (exception == null) {
                        results.addAll(immunizations);
                    } else {
                        LOG.error("Unable to fetch immunizations", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch immunizations", ex);
                    return null;
                });

        CompletableFuture<List<Goal>> goal = CompletableFuture.supplyAsync(() -> getGoal(patientIcn))
                .whenComplete((goals, exception) -> {
                    if (exception == null) {
                        results.addAll(goals);
                    } else {
                        LOG.error("Unable to fetch goals", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch goals", ex);
                    return null;
                });

        CompletableFuture<List<DiagnosticReport>> diagnosticReport = CompletableFuture.supplyAsync(() -> getDiagnosticReport(patientIcn))
                .whenComplete((diagnosticReports, exception) -> {
                    if (exception == null) {
                        results.addAll(diagnosticReports);
                    } else {
                        LOG.error("Unable to fetch diagnosticReports", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch diagnosticReports", ex);
                    return null;
                });

        CompletableFuture<List<CarePlan>> careplan = CompletableFuture.supplyAsync(() -> getCarePlan(patientIcn))
                .whenComplete((careplans, exception) -> {
                    if (exception == null) {
                        results.addAll(careplans);
                    } else {
                        LOG.error("Unable to fetch CarePlans", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch CarePlans", ex);
                    return null;
                });

        CompletableFuture<List<Composition>> composition = CompletableFuture.supplyAsync(() -> getTiuNotes(patientIcn))
                .whenComplete((compositions, exception) -> {
                    if (exception == null) {
                        results.addAll(compositions);
                    } else {
                        LOG.error("Unable to fetch notes", exception);
                    }
                }).exceptionally(ex -> {
                    LOG.error("Unable to fetch notes", ex);
                    return null;
                });

        CompletableFuture.allOf(patientCall, condition, observation, encounter, procedure, medStatement, medAdmin, flag, appointment, practitioner, allergy, immunization, medDispense, goal, diagnosticReport, careplan, composition).join();

        if (!patientFound[0]) {
            throw new ResourceNotFoundException("Patient Not Found");
        }

        return results;
    }
}
