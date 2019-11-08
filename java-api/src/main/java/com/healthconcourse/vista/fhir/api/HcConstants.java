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
package com.healthconcourse.vista.fhir.api;

/**
* Constant values used across the application.
*/
public class HcConstants {

    public static final String HELLO_MESSAGE = "Bazinga!";

    public static final String PROVIDER_PACKAGE = "com.healthconcourse.vista.fhir.api.provider";

    public static final String URN_VISTA_ICN = "urn:dxc:vista:icn";
    public static final String URN_VISTA_SOURCE = "urn:dxc:vista:source";
    public static final String ICD_9  = "http://hl7.org/fhir/sid/icd-9-cm";
    public static final String ICD_10 = "http://hl7.org/fhir/sid/icd-10-cm";
    public static final String LOINC = "http://loinc.org";
    public static final String CPT = "http://www.ama-assn.org/go/cpt";
    public static final String RX_NORM = "http://www.nlm.nih.gov/research/umls/rxnorm";
    public static final String NDFRT = "http://hl7.org/fhir/ndfrt";
    public static final String VACCINE = "http://hl7.org/fhir/sid/cvx";
    public static final String URN_VISTA_ENCOUNTER_REASON = "urn:dxc:vista:encounter-reason";
    public static final String URN_VISTA_ADMIN = "urn:dxc:vista:admit-source";
    public static final String URN_VISTA_DISCHARGE = "urn:dxc:vista:discharge-disposition";
    public static final String URN_VISTA_SERVICE_PROVIDER = "urn:dxc:vista:service-provider";
    public static final String URN_VISTA_PROVIDER = "urn:dxc:vista:provider";
    public static final String URN_VISTA_PRACTITIONER = "urn:dxc:vista:practitioner";
    public static final String URN_VISTA_ORGANIZATION = "urn:dxc:vista:organization";
    public static final String URN_VISTA_VANDF = "urn:dxc:vista:vandf";
    public static final String URN_VISTA_DRUG  = "urn:dxc:vista:drug";
    public static final String URN_VISTA_LOCATION = "urn:dxc:vista:location";
    public static final String URN_VISTA_CARETEAM_ROLE = "urn:dxc:vista:careteam:role";

    public static final String SNOMED_URN = "http://snomed.info/sct";

    public static final long VERSION = 1L;

    public static final String SSN_CODING = "SB";
    public static final String SSN_CODING_SYSTEM = "http://hl7.org/fhir/identifier-type";
    public static final String MRN_CODING = "MR";
    public static final String MRN_CODING_SYSTEM = "http://terminology.hl7.org/CodeSystem/v2-0203";
    public static final String MRN_CODING_SYSTEM_DESC="Medical Record Number";
    public static final String UNK_CODING = "UNK";
    public static final String UNK_CODING_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-NullFlavor";
    public static final String UNK_CODING_SYSTEM_DESC= "Unknown";
    public static final String PARTICIPANT_CODING_SYSTEM = "http://hl7.org/fhir/v3/ParticipationType";
    public static final String OBSERVATION_CODING_SYSTEM = "http://hl7.org/fhir/ValueSet/observation-category";
    public static final String APPOINTMENT_TYPE_CODING_SYSTEM = "http://hl7.org/fhir/v2/0276";

    public static final String SP_START = "startDate";

    public static final String UNICODE = "UTF-8";

    static final String SERVER_DESC = "HealthConcourse FHIR API using VistA as a data source";
    static final String SERVER_NAME = "HealthConcourse VistA FHIR API";
    static final String SERVER_VERSION = "1.2";

    static final String CONFIG_VISTA_URL = "VistaBaseUrl";

    private HcConstants(){}
}
