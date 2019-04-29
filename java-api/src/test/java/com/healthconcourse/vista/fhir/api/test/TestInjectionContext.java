package com.healthconcourse.vista.fhir.api.test;

import com.healthconcourse.vista.fhir.api.test.mocks.MockVistaData;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class TestInjectionContext {
    @Autowired
    @Bean
    public VistaData vistaData(Environment env) {

        return new MockVistaData(env.getProperty("vistaUrl"));
    }
}
