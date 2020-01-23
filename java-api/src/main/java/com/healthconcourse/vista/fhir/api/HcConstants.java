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
package com.healthconcourse.vista.fhir.api;

/**
* Constant values used across the application.
*/
public class HcConstants {
    public static final String URN_VISTA_ICN = "urn:dxc:vista:icn";
    public static final String URN_VISTA_SOURCE = "urn:dxc:vista:source";
    public static final String ICD_9 = "http://hl7.org/fhir/sid/icd-9-cm";
    public static final String LOINC = "http://loinc.org";
    public static final String CPT = "http://www.ama-assn.org/go/cpt";
    public static final String RX_NORM = "http://www.nlm.nih.gov/research/umls/rxnorm";
    public static final String NDFRT = "http://hl7.org/fhir/ndfrt";
    public static final String VACCINE = "http://hl7.org/fhir/sid/cvx";
    public static final String URN_VISTA_PROVIDER = "urn:healthconcourse:vista:provider";
    public static final String URN_VISTA_CONDITION = "urn:healthconcourse:vista:condition";
    public static final String URN_VISTA_MEDICATION = "urn:healthconcourse:vista:medication";
    public static final String URN_VISTA_PRACTITIONER = "urn:healthconcourse:vista:practitioner";
    public static final String URN_VISTA_ORGANIZATION = "urn:healthconcourse:vista:organization";
    public static final String URN_VISTA_ENCOUNTER = "urn:healthconcourse:vista:encounter";
    public static final String URN_VISTA_LOCATION = "urn:healthconcourse:vista:location";
    public static final String URN_VISTA_CARETEAM_ROLE = "urn:healthconcourse:vista:careteam:role";
    public static final String URN_VISTA_HEALTH_FACTOR = "urn:healthconcourse:vista:healthfactor";
    public static final String UNIT_OF_MEASURE_SYSTEM = "http://unitsofmeasure.org";

    public static final String SNOMED_URN = "http://snomed.info/sct";

    public static final long VERSION = 1L;

    public static final String SSN_CODING = "SB";
    public static final String SSN_CODING_SYSTEM = "http://hl7.org/fhir/identifier-type";
    public static final String PARTICIPANT_CODING_SYSTEM = "http://hl7.org/fhir/v3/ParticipationType";
    public static final String OBSERVATION_CODING_SYSTEM = "http://hl7.org/fhir/ValueSet/observation-category";
    public static final String APPOINTMENT_TYPE_CODING_SYSTEM = "http://hl7.org/fhir/v2/0276";
    public static final String CARE_TEAM_CODING_SYSTEM = "http://hl7.org/fhir/care-team-category";
    public static final String MARITAL_STATUS_CODING_SYSTEM = "http://hl7.org/fhir/v3/MaritalStatus";
    public static final String HL7_NULL_CODE = "http://hl7.org/fhir/v3/NullFlavor";
    public static final String HL7_ACT_ENCOUNTER_CODE = "http://hl7.org/fhir/v3/ActCode";

    static final String SERVER_DESC = "HealthConcourse FHIR API using VistA as a data source";
    static final String SERVER_NAME = "HealthConcourse VistA FHIR API";
    static final String SERVER_VERSION = "1.3";

    public static final Long MISSING_ID = -999L;

    private HcConstants(){}
}
