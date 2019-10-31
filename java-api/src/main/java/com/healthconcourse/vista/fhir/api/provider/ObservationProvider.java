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
package com.healthconcourse.vista.fhir.api.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthconcourse.vista.fhir.api.service.ObservationService;

import com.healthconcourse.vista.fhir.api.service.VistaObservationService;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Observation provider endpoint
 *
 */

@Path("Observation")
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class ObservationProvider extends AbstractJaxRsResourceProvider<Observation> {

    private static final Logger LOG = LoggerFactory.getLogger(ObservationProvider.class);
    private ObservationService service;

    @Autowired
    public ObservationProvider(VistaData data){

        super(FhirContext.forR4(), ObservationProvider.class);

        service = new VistaObservationService(data);
    }

    @Override
    public Class<Observation> getResourceType() {

        return Observation.class;
    }

    @Search
    public List<Observation> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name,
                                @RequiredParam(name = Patient.SP_BIRTHDATE) final DateParam dob,
                                @RequiredParam(name = Patient.SP_GENDER) final StringParam gender,
                                @RequiredParam(name = Patient.SP_IDENTIFIER) final StringParam ssn) {

        Enumerations.AdministrativeGender administrativeGender = InputValidator.parseGender(gender);

        List<Observation> results = service.getObservationsByCriteria(name.getValue(), dob.getValue(), administrativeGender, ssn.getValue());

        if(results.isEmpty()) {
            String message = "No observations found search criteria";
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }
}
