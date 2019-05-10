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
package com.healthconcourse.vista.fhir.api.parser;

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProcedureParser implements VistaParser<Procedure> {

    private static final Logger LOG = LoggerFactory.getLogger(ObservationParser.class);
    public List<Procedure> parseList(String httpData) {

        List<Procedure> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.trim().split("\\^");

        if(records.length < 2) {
            return result;
        }

        String icn = records[0];

        for(int i = 1; i < records.length; i++) {
            Procedure procedure = new Procedure();

            String[] fields = records[i].split("\\|");

            procedure.setStatus(Procedure.ProcedureStatus.COMPLETED);
            procedure.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, "", ResourceHelper.ReferenceType.Patient));

            procedure.setCode(ResourceHelper.createCodeableConcept(HcConstants.SNOMED_URN,fields[0], fields[1]));

            CodeableConcept concept = procedure.getCode();
            Coding code = new Coding();
            code.setSystem(HcConstants.CPT);
            code.setCode(fields[2]);
            code.setDisplay(fields[1]);
            concept.addCoding(code);

            Optional<Date> performedDate = InputValidator.parseAnyDate(fields[3]);
            if (performedDate.isPresent()) {
                procedure.setPerformed(new DateTimeType(performedDate.get()));
            }


            procedure.setId(fields[4]);

            procedure.setMeta(ResourceHelper.getVistaMeta());

            result.add(procedure);
        }

        return result;
    }
}
