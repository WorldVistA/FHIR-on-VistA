/*
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
package com.healthconcourse.vista.fhir.api.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.api.Constants;
import com.healthconcourse.vista.fhir.api.service.PatientService;
import com.healthconcourse.vista.fhir.api.service.VistaPatientService;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.r4.model.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("MedicationStatement")
@Produces({MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML})
public class MedicationProvider extends AbstractJaxRsResourceProvider<MedicationStatement> {

    private final PatientService patientService;

    public MedicationProvider(VistaData data) {
        super(FhirContext.forR4(), MedicationProvider.class);
        patientService   = new VistaPatientService(data);
    }

    @Override
    public Class<MedicationStatement> getResourceType() {
        return MedicationStatement.class;
    }

    @Read
    public List<MedicationStatement> medicationStatementById(@IdParam IdType theIdn) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("_id", theIdn.getIdPart());
        return patientService.getMedicationStatement(options);
    }

    @Search
    public List<MedicationStatement> medicationStatementByPatient(
            @OptionalParam(name = MedicationStatement.SP_PATIENT)   final StringParam icn,
            @OptionalParam(name = MedicationStatement.SP_STATUS)    final StringParam status,
            @OptionalParam(name = MedicationStatement.SP_EFFECTIVE) final DateParam effectiveDate,
            @OptionalParam(name = "_id")                            final StringParam theIdn,
            RequestDetails request
            )
    {
        HashMap<String, String> options = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry: request.getParameters().entrySet()) {
             options.put(entry.getKey(), entry.getValue()[0]);
        }
        return patientService.getMedicationStatement(options);
    }

}