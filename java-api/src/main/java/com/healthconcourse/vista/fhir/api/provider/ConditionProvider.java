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
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthconcourse.vista.fhir.api.service.ConditionService;
import com.healthconcourse.vista.fhir.api.service.VistaConditionService;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.dstu3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("Condition")
@Produces({MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML})
public class ConditionProvider extends AbstractJaxRsResourceProvider<Condition> {

    private static final Logger LOG = LoggerFactory.getLogger(ConditionProvider.class);
    private final ConditionService service;

    @Autowired
    public ConditionProvider(VistaData data) {

        super(FhirContext.forDstu3(), ConditionProvider.class);

        service = new VistaConditionService(data);
    }

    @Override
    public Class<Condition> getResourceType() {
        return Condition.class;
    }


    @Search(compartmentName = "Patient")
    public List<Patient> search(@IdParam IdType theIdn) {

        List<Patient> results = service.getPatientsByCode(theIdn.getIdPart());

        if (results.isEmpty()) {
            String message = "No patients found for code: " + theIdn.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Create()
    public MethodOutcome createCondition(
            @ResourceParam Condition theCondition
    ) {
        MethodOutcome methodOutcome = service.putCondition(theCondition);
        return methodOutcome;
    }

}
