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
package com.healthconcourse.vista.fhir.api.test;

import com.healthconcourse.vista.fhir.api.vista.VistaHelper;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class VistaHelperTest {


    @Test
    public void convertAdministrativeGenderFemaleSuccess() {

        String result = VistaHelper.convertAdministrativeGender(Enumerations.AdministrativeGender.FEMALE);

        Assert.assertEquals("Should be an F", "F", result);
    }

    @Test
    public void convertAdministrativeGenderMaleSuccess() {

        String result = VistaHelper.convertAdministrativeGender(Enumerations.AdministrativeGender.MALE);

        Assert.assertEquals("Should be an M", "M", result);
    }

    @Test
    public void convertAdministrativeGenderNotMaleOrFemale() {

        String result = VistaHelper.convertAdministrativeGender(Enumerations.AdministrativeGender.NULL);

        Assert.assertEquals("Should be an empty string", "", result);
    }

    private Response createResponse(String body, int status) {
        return new Response() {
            String defaultResponse = body;
            @Override
            public int getStatus() {
                return status;
            }

            @Override
            public StatusType getStatusInfo() {
                return null;
            }

            @Override
            public Object getEntity() {
                return defaultResponse;
            }

            @Override
            public <T> T readEntity(Class<T> entityType) {
                return (T) body;
            }

            @Override
            public <T> T readEntity(GenericType<T> entityType) {
                return null;
            }

            @Override
            public <T> T readEntity(Class<T> entityType, Annotation[] annotations) {
                return null;
            }

            @Override
            public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) {
                return null;
            }

            @Override
            public boolean hasEntity() {
                return true;
            }

            @Override
            public boolean bufferEntity() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Set<String> getAllowedMethods() {
                return null;
            }

            @Override
            public Map<String, NewCookie> getCookies() {
                return null;
            }

            @Override
            public EntityTag getEntityTag() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public Date getLastModified() {
                return null;
            }

            @Override
            public URI getLocation() {
                return null;
            }

            @Override
            public Set<Link> getLinks() {
                return null;
            }

            @Override
            public boolean hasLink(String relation) {
                return false;
            }

            @Override
            public Link getLink(String relation) {
                return null;
            }

            @Override
            public Link.Builder getLinkBuilder(String relation) {
                return null;
            }

            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getStringHeaders() {
                return null;
            }

            @Override
            public String getHeaderString(String name) {
                return null;
            }
        };
    }

    @Test
    public void responseHandlerSuccess(){

        Response response = createResponse("Success", 200);

        String result = VistaHelper.responseHandler(response);

        Assert.assertEquals("Success", result);
    }

    @Test
    public void responseHandlerFailure(){

        Response response = createResponse("NotFound", 404);

        String result = VistaHelper.responseHandler(response);

        Assert.assertEquals("", result);
    }
}
