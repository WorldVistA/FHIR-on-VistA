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

import org.hl7.fhir.dstu3.model.*;

import java.util.Date;
import java.util.List;

public interface PatientService {

    Patient getPatientById(String id);
    List<Patient> getAllPatients();
    List<Condition> getCodesByIcn(String patientIcn);
    List<Encounter> getEncountersForPatient(String patientIcn);
    List<Observation> getObservationsByIcn(String id);
    List<MedicationStatement> getMedicationStatement(String patientIcn);
    List<MedicationDispense> getMedicationDispense(String patientIcn);
    List<MedicationAdministration> getMMedicationAdministration(String patientIcn);
    List<Procedure> getProcedures(String patientIcn);
    List<Practitioner> getPractitionersById(String patientIcn);
    List<DomainResource> getEverything(String patientIcn);
    List<Flag> getFlagsByIcn(String patientIcn);
    List<Appointment> getAppointmentsByIcn(String patientIcn);
    List<AllergyIntolerance> getAllergyByIcn(String patientIcn);
    List<Immunization> getImmunizationsByIcn(String patientIcn);
    List<Goal> getGoal(String patientIcn);
    List<DiagnosticReport> getDiagnosticReport(String patientIcn);
    List<CarePlan> getCarePlan(String patientIcn);
    List<Composition> getTiuNotes(String patientIcn);
}
