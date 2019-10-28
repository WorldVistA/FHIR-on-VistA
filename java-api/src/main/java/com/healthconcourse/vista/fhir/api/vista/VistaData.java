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
package com.healthconcourse.vista.fhir.api.vista;

import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;

import java.util.Date;
import java.util.HashMap;

public interface VistaData {

    String getPatientData(String icn);

    String getPatientData(String name, String ssn, Date dob, AdministrativeGender gender);

    String getConditions(String name, String ssn, Date dob, AdministrativeGender gender);

    String getConditions(String icn);
    String getConditions(HashMap<String,String> options);

    String getPatientsByCondition(String code);

    String putTheCondition(Condition theCondition);

    String getVitalsObservationsByIcn(String icn);

    String getObservationsByIcnAndCode(String icn, String code);

    String getObservationsByCriteria(String name, String ssn, Date dob, AdministrativeGender gender);

    String getEncountersByPatient(String code);

    String getMedicationStatement(String code);

    String getMedicationAdministration(String code);

    String getProceduresByIcn(String icn);

    String getLabObservationsByIcn(String icn);

    String getHealthFactorObservationsByIcn(String icn);

    String getMentalHealthObservationsByIcn(String icn);

    String getProvidersByIcn(String icn);

    String getLocationByName(String name);

    String getFlagByIcn(String icn);

    String getAppointmentsByIcn(String icn);

    String getAllergiesByIcn(String icn);

    String getImmunizationsByIcn(String icn);

    String getGoal(String code);

    String getDiagnosticReport(String code);

    String getCarePlan(String code);

    String getAllPatients(HashMap<String, String> options);

    String getTiuNotes(String icn);

    String getAllCareTeams();

    String getCareTeamByHame(String name);
}
