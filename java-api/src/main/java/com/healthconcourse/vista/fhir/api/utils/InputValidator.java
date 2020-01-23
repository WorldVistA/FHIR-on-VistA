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
package com.healthconcourse.vista.fhir.api.utils;

import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class InputValidator {

    private static final Logger LOG = LoggerFactory.getLogger(InputValidator.class);
    private InputValidator() {}

    /**
     * Turn a StringParam from a Provider into an AdministrativeGender object
     *
     * @param gender StringParam from a Provider
     * @return
     */
    public static AdministrativeGender parseGender(StringParam gender) {

        if(gender == null ||  gender.getValue().isEmpty()) {
            return AdministrativeGender.UNKNOWN;
        }

        try {
            return AdministrativeGender.fromCode(gender.getValue().toLowerCase());
        } catch (FHIRException e) {
            LOG.info(String.format("Unable to parse gender from StringParam: %s", gender.getValue()), e);
            return AdministrativeGender.UNKNOWN;
        }
    }

    /**
     * Turn a String from a Provider into an AdministrativeGender object
     *
     * @param gender String from a Provider
     * @return
     */
    public static AdministrativeGender parseGender(String gender) {

        if(gender == null ||  gender.isEmpty()) {
            return AdministrativeGender.UNKNOWN;
        }

        try {
            return AdministrativeGender.fromCode(gender.toLowerCase());
        } catch (FHIRException e) {
            LOG.info(String.format("Unable to parse gender from String: %s", gender), e);
            return AdministrativeGender.UNKNOWN;
        }
    }


    /**
     * This method is to accept a string that can house a wide variety of dates formats and return it
     *     as a Date
     *
     * @param dateAsText a String containing a date
     * @return a Java Date
     */
    public static Optional<Date> parseAnyDate(String dateAsText) {
        // The master list of all valid (and formerly valid) date formats
        // NOTICE: We start with the most complete date, as the simple dates first may take undesired priority
        List<SimpleDateFormat> validDateFormats = new ArrayList<>();
        // JSON date formats
        validDateFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"));
        validDateFormats.add(new SimpleDateFormat("yyyy-MM-dd"));

        // Legacy/Text date formats, for future removal
        validDateFormats.add(new SimpleDateFormat("MMM dd, yyyy"));
        validDateFormats.add(new SimpleDateFormat("MMM dd,yyyy"));
        validDateFormats.add(new SimpleDateFormat("M/dd/yyyy"));
        validDateFormats.add(new SimpleDateFormat("yyyyMMddHHmmssX")); //19961209113329-0500
        validDateFormats.add(new SimpleDateFormat("yyyyMMddHHmmX")); //199506091400-0500
        validDateFormats.add(new SimpleDateFormat("yyyyMMdd"));

        if (dateAsText == null || dateAsText.length() == 0) {
            return Optional.empty();
        } else {
            String errorMessage = "";
            ParseException pex = null;

            // loop through the valid formats and when we find a match that works, use it
            for (SimpleDateFormat sdf : validDateFormats) {
                try {
                    sdf.setLenient(false);
                    Date newDate = sdf.parse(dateAsText);
                    return Optional.of(newDate);
                } catch (ParseException e) {
                    pex = e;
                    errorMessage = "Invalid date format for (" + dateAsText + "), exception : (" + e.getMessage() + ")";
                }
            }

            // If we have reached here, we have NOT found any formats that work
            if (pex != null) {
                LOG.info(errorMessage, pex);
            }
            return Optional.empty();
        }
    }

    public static String parseSSN(StringParam input) {

        if(input == null) {
            return "";
        }

        //TODO: Validate SSN format
        return input.getValue();
    }

    public static String parseString(StringParam input) {

        if (input == null) {
            return "";
        }

        return input.getValue();
    }
}
