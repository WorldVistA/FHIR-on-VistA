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
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthconcourse.vista.fhir.api.service.CareTeamService;
import com.healthconcourse.vista.fhir.api.service.VistaCareTeamService;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.r4.model.CareTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("Condition")
@Produces({MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML})
public class CareTeamProvider extends AbstractJaxRsResourceProvider<CareTeam> {

    private static final Logger LOG = LoggerFactory.getLogger(CareTeamProvider.class);
    private final CareTeamService service;

    @Autowired
    public CareTeamProvider(VistaData data) {

        super(FhirContext.forR4(), ConditionProvider.class);

        service = new VistaCareTeamService(data);
    }

    @Override
    public Class<CareTeam> getResourceType() {
        return CareTeam.class;
    }

    @Search
    public List<CareTeam> search(@RequiredParam(name = "team") final StringParam name) {
        List<CareTeam> results = service.getCareTeamByName(name.getValue());

        if(results.isEmpty()) {
            String message = "No care teams found for " + name.getValue();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search
    public List<CareTeam> getAllCareTeams() {
        List<CareTeam> results = service.getAllCareTeams();

        if(results.isEmpty()) {
            String message = "No care teams found";
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }
}
