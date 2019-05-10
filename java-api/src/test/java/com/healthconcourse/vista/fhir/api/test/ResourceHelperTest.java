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
package com.healthconcourse.vista.fhir.api.test;

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Reference;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ResourceHelperTest {


    @Test
    public void getSingleCodeableConceptSuccess() {

        CodeableConcept result = ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN, "12345", "Test");

        List<Coding> coding = result.getCoding();
        assertEquals("Test", coding.get(0).getDisplay());
    }

    @Test
    public void getSingleCodeableConceptAsListSuccess() {

        List<CodeableConcept> result = ResourceHelper.createSingleCodeableConceptAsList(HcConstants.SNOMED_URN, "12345", "Test");

        assertEquals(1, result.size());
    }

    @Test
    public void getSingleReferenceAsListSuccess() {

        List<Reference> result = ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_ICN, "123456789", "Test");

        assertEquals(1, result.size());
    }

    @Test(expected = InvalidParameterException.class)
    public void getSingleReferenceAsListMissingId() {

        List<Reference> result = ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_ICN, "", "Test");

        assertEquals(1, result.size());
    }

    @Test(expected = InvalidParameterException.class)
    public void getSingleReferenceAsListNullId() {

        List<Reference> result = ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_ICN, null, "Test");

        assertEquals(1, result.size());
    }

    @Test
    public void getSingleReferenceWithValueAsListSuccess() {

        List<Reference> result = ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_ICN, "123456789", "Test");

        assertEquals(1, result.size());
    }

    @Test
    public void createReferenceNoReferenceType(){

        Reference reference = ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, "123456", "Display");

        assertNull("No reference should be found", reference.getReference());
    }
}
