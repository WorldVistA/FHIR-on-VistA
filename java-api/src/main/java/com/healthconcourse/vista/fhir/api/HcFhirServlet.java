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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.healthconcourse.vista.fhir.api.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import com.healthconcourse.vista.fhir.api.vista.VistaData;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/api/*", loadOnStartup = 1)
public class HcFhirServlet extends RestfulServer {

    private static final long serialVersionUID = 1L;
    private VistaData service;

    private Environment env;

    @Autowired
    HcFhirServlet(VistaData dataService, Environment environment) {

        super(FhirContext.forR4());
        this.service = dataService;
        this.env = environment;
    }

    @Override
    public void initialize() {

        List<IResourceProvider> providers = new ArrayList<>();

        providers.add(new PatientProvider(service));
        providers.add(new ConditionProvider(service));
        providers.add(new ObservationProvider(service));
        providers.add(new LocationProvider(service));
        providers.add(new CareTeamProvider(service));
        providers.add(new MedicationProvider(service));
        setResourceProviders(providers);

        setDefaultResponseEncoding(EncodingEnum.JSON);
        setImplementationDescription(HcConstants.SERVER_DESC);
        setServerName(HcConstants.SERVER_NAME);
        setServerVersion(HcConstants.SERVER_VERSION);

        String serverUrl = env.getProperty("serverUrl");
        IServerAddressStrategy addressStrategy = new HardcodedServerAddressStrategy(serverUrl);
        setServerAddressStrategy(addressStrategy);

		/*
		 * Use a narrative generator. This is a completely optional step,
		 * but can be useful as it causes HAPI to generate narratives for
		 * resources which don't otherwise have one.
		 */
        INarrativeGenerator narrativeGen = new DefaultThymeleafNarrativeGenerator();
        getFhirContext().setNarrativeGenerator(narrativeGen);

		/*
		 * Enable CORS
		 */
        CorsConfiguration config = new CorsConfiguration();
        CorsInterceptor corsInterceptor = new CorsInterceptor(config);
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Content-Type");
        config.addAllowedOrigin("*");
        config.addExposedHeader("Location");
        config.addExposedHeader("Content-Location");
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        registerInterceptor(corsInterceptor);

		/*
		 * This server interceptor causes the server to return nicely
		 * formatter and coloured responses instead of plain JSON/XML if
		 * the request is coming from a browser window. It is optional,
		 * but can be nice for testing.
		 */
        registerInterceptor(new ResponseHighlighterInterceptor());

		/*
		 * Tells the server to return pretty-printed responses by default
		 */
        setDefaultPrettyPrint(true);
    }
}
