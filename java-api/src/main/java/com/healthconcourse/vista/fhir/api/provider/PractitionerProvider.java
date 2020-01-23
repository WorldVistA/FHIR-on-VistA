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
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import com.healthconcourse.vista.fhir.api.service.PractitionerService;
import com.healthconcourse.vista.fhir.api.service.VistaPractitionerService;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("Practitioner")
@Produces({MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML})
public class PractitionerProvider extends AbstractJaxRsResourceProvider<Practitioner> {

    private static final Logger LOG = LoggerFactory.getLogger(PractitionerProvider.class);

    private final PractitionerService service;

    @Autowired
    public PractitionerProvider(VistaData data) {

        super(FhirContext.forDstu3(), PractitionerProvider.class);

        service = new VistaPractitionerService(data);
    }

    @Override
    public Class<Practitioner> getResourceType() {
        return Practitioner.class;
    }

    @Read
    public Practitioner find(@IdParam final IdType theId) {

        return service.getPractitionerById(theId.getIdPart());
    }

}
