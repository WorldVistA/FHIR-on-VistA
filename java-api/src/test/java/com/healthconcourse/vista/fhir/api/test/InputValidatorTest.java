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

import ca.uhn.fhir.rest.param.StringParam;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Optional;

public class InputValidatorTest {

    @Test
    public void TestGenderValidatorNullInput(){

        StringParam input = null;

        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.UNKNOWN", AdministrativeGender.UNKNOWN, result);
    }

    @Test
    public void TestGenderValidatorEmptyInput(){

        StringParam input = new StringParam("");

        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.UNKNOWN", AdministrativeGender.UNKNOWN, result);
    }

    @Test
    public void TestGenderValidatorFemaleInput(){

        StringParam input = new StringParam("FEMALE");
        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.FEMALE", AdministrativeGender.FEMALE, result);
    }

    @Test
    public void TestGenderValidatorMaleInput(){

        StringParam input = new StringParam("male");
        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.MALE", AdministrativeGender.MALE, result);
    }

    @Test
    public void TestGenderValidatorNonsenseInput(){
        StringParam input = new StringParam("asdf");
        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.UNKNOWN", AdministrativeGender.UNKNOWN, result);
    }

    @Test
    public void TestGenderStringValidatorNullInput(){

        String input = null;

        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.UNKNOWN", AdministrativeGender.UNKNOWN, result);
    }

    @Test
    public void TestGenderStringValidatorEmptyInput(){

        String input = "";

        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.UNKNOWN", AdministrativeGender.UNKNOWN, result);
    }

    @Test
    public void TestGenderStringValidatorFemaleInput(){

        String input = "FEMALE";
        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.FEMALE", AdministrativeGender.FEMALE, result);
    }

    @Test
    public void TestGenderStringValidatorMaleInput(){

        String input = "male";
        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.MALE", AdministrativeGender.MALE, result);
    }

    @Test
    public void TestGenderStringValidatorNonsenseInput(){
        String input = "asdf";
        AdministrativeGender result = InputValidator.parseGender(input);

        Assert.assertEquals("Should return AdministrativeGender.UNKNOWN", AdministrativeGender.UNKNOWN, result);
    }

    @Test
    public void TestDateValidatorNonsenseInput() {
        String input = "asdf";

        Optional<Date> result = InputValidator.parseAnyDate(input);

        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void TestDateValidatorValidInput() {
        String input = "1974-10-11";

        Optional<Date> result = InputValidator.parseAnyDate(input);

        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void TestDateValidatorNullInput() {
        String input = null;
        Optional<Date> result = InputValidator.parseAnyDate(input);

        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void TestStringValidatorValidInput(){
        StringParam input = new StringParam("asdf");
        String result = InputValidator.parseString(input);

        Assert.assertEquals("Should return string contents", "asdf", result);
    }

    @Test
    public void TestStringValidatorNullInput(){
        String result = InputValidator.parseString(null);

        Assert.assertEquals("Should return string contents", "", result);
    }

    @Test
    public void TestSsnValidatorValidInput(){
        StringParam input = new StringParam("111223333");
        String result = InputValidator.parseSSN(input);

        Assert.assertEquals("Should return string contents", "111223333", result);
    }

    @Test
    public void TestSsnValidatorNullInput(){
        String result = InputValidator.parseSSN(null);

        Assert.assertEquals("Should return string contents", "", result);
    }
}
