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
package com.healthconcourse.vista.fhir.api.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthconcourse.vista.fhir.api.service.ConditionService;
import com.healthconcourse.vista.fhir.api.service.PatientService;
import com.healthconcourse.vista.fhir.api.service.VistaConditionService;
import com.healthconcourse.vista.fhir.api.service.VistaPatientService;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("Condition")
@Produces({MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML})
public class ConditionProvider extends AbstractJaxRsResourceProvider<Condition> {

    private static final Logger LOG = LoggerFactory.getLogger(ConditionProvider.class);
    private final ConditionService conditionService;
    private final PatientService patientService;

    public ConditionProvider(VistaData data) {
        super(FhirContext.forR4(), ConditionProvider.class);
        patientService   = new VistaPatientService(data);
        conditionService = new VistaConditionService(data);
    }

    @Override
    public Class<Condition> getResourceType() {
        return Condition.class;
    }


    @Search(compartmentName = "Patient")
    public List<Patient> search(@IdParam IdType theIdn) {

        List<Patient> results = conditionService.getPatientsByCode(theIdn.getIdPart());

        if (results.isEmpty()) {
            String message = "No patients found for code: " + theIdn.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Read
    public List<Condition> conditionById(@IdParam IdType theIdn) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("_id", theIdn.getIdPart());
        return patientService.getConditionsForPatient(options);
    }

    @Search
    public List<Condition> conditionsByPatient(
            @RequiredParam(name = Condition.SP_PATIENT)         final StringParam icn,
            @OptionalParam(name = Condition.SP_CATEGORY)        final StringParam category,
            @OptionalParam(name = Condition.SP_CLINICAL_STATUS) final StringParam clinicalStatus,
            @OptionalParam(name = Condition.SP_CODE)            final StringParam code,
            @OptionalParam(name = Condition.SP_ONSET_DATE)      final DateParam onsetDate,
            RequestDetails request
            )
    {
        HashMap<String, String> options = new HashMap<String, String>();
         for (Map.Entry<String, String[]> entry: request.getParameters().entrySet()) {
             options.put(entry.getKey(), entry.getValue()[0]);
         }
        return patientService.getConditionsForPatient(options);
    }

    @Create()
    public MethodOutcome createCondition(
            @ResourceParam Condition theCondition
    ) {
        MethodOutcome methodOutcome = conditionService.putCondition(theCondition);
        return methodOutcome;
    }

}
