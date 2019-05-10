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
package com.healthconcourse.vista.fhir.api.vista;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public class VistaHelper {

    private static final Logger LOG = LoggerFactory.getLogger(VistaHelper.class);
    private VistaHelper() {

    }

    public static String convertAdministrativeGender(Enumerations.AdministrativeGender gender) {

        switch (gender){
            case MALE:
                return "M";
            case FEMALE:
                return "F";
            default:
                return "";
        }
    }

    public static String responseHandler(Response response) {
        int status = response.getStatus();

        if(status == 200) {
            return response.readEntity(String.class);
        } else {

            LOG.error("HTTP Status: " + status);
            LOG.error(response.readEntity(String.class));
            return "";
        }
    }
}
