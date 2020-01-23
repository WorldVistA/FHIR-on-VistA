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
package com.healthconcourse.vista.fhir.api.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthconcourse.vista.fhir.api.service.PatientService;
import com.healthconcourse.vista.fhir.api.service.VistaPatientService;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("CarePlan")
@Produces({MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML})
public class CarePlanProvider extends AbstractJaxRsResourceProvider<CarePlan> {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyProvider.class);
    private final PatientService service;

    @Autowired
    public CarePlanProvider(VistaData data) {

        super(FhirContext.forDstu3(), ConditionProvider.class);

        service = new VistaPatientService(data);
    }

    @Override
    public Class<CarePlan> getResourceType() {
        return CarePlan.class;
    }

    @Search
    public List<CarePlan> search(@OptionalParam(name = CarePlan.SP_SUBJECT) StringParam subjectId,
                                 @OptionalParam(name = CarePlan.SP_PATIENT) StringParam patientId) {
        if (subjectId != null) {
            return this.findByPatient(subjectId.getValue());
        } else if (patientId != null) {
            return this.findByPatient(patientId.getValue());
        } else {
            throw new InvalidRequestException("No search parameters specified");
        }
    }

    private List<CarePlan> findByPatient(String id) {
        List<CarePlan> results = service.getCarePlan(id);

        if(results.isEmpty()) {
            String message = "No care plan records found for patient: " + id;
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }
}
