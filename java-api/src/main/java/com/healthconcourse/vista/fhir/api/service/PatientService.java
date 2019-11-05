/* Created by Perspecta http://www.perspecta.com */
/*
(c) 2017-2019 Perspecta
(c) 2019 OSEHRA

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
package com.healthconcourse.vista.fhir.api.service;

import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface PatientService {

    Patient getPatientById(String id);
    List<Patient> findPatient(String name, Date dob, String ssn, Enumerations.AdministrativeGender gender);
    List<Patient> getAllPatients(HashMap<String, String> options);
    List<Condition> getConditionsForPatient(String patientIcn);
    List<Condition> getConditionsForPatient(HashMap<String, String> options);
    List<Encounter> getEncountersForPatient(String patientIcn);
    List<Observation> getObservationsByIcn(String id);
    List<Observation> getObservationsByIcnAndCode(String id, String code);
    List<MedicationStatement> getMedicationStatement(String patientIcn);
    List<MedicationStatement> getMedicationStatement(HashMap<String, String> options);
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
