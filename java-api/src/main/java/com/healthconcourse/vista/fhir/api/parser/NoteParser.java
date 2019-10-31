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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.*;

public class NoteParser implements VistaParser<Composition> {

    private static final String[] confidentialityValues = new String[] {"N", "U", "L", "M", "R", "V"};

    @Override
    public List<Composition> parseList(String httpData) {
        List<Composition> result = new ArrayList<>();

        if(StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.trim().split("~");

        String icn = records[0];

        for(int i = 1; i < records.length; i++) {
            Composition note = new Composition();
            note.setMeta(ResourceHelper.getVistaMeta());
            note.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, "", ResourceHelper.ReferenceType.Patient));

            String[] fields = records[i].split("\\|");

            note.setId(fields[0]);

            Optional<Date> noteDate = InputValidator.parseAnyDate(fields[1]);
            if (noteDate.isPresent()) {
                note.setDate(noteDate.get());
            }

            if(!StringUtils.isEmpty(fields[2])) {
                note.setStatus(getStatus(fields[2]));
            }

            if(!StringUtils.isEmpty(fields[3]) && fields[3].length() == 1 && Arrays.asList(confidentialityValues).contains(fields[3])) {
                note.setConfidentiality(this.getConfidentiality(fields[3]));
            }

            if(!StringUtils.isEmpty(fields[4])) {
                note.setAuthor(ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_PRACTITIONER, fields[4],"", ResourceHelper.ReferenceType.Practitioner));
            }

            String[] content = fields[5].split("\\^");

            note.setTitle(this.getTitle(content[0]));
            note.setText(this.setContent(content));

            if (fields.length > 6) {
                String[] loincParts = fields[6].split("_");

                if (loincParts.length == 2) {
                    note.setType(ResourceHelper.createCodeableConcept(HcConstants.LOINC, loincParts[0], loincParts[1]));
                }
            }

            result.add(note);
        }

        return result;
    }

    private Narrative setContent(String[] rawContent) {
        Narrative text = new Narrative();
        text.setStatus(Narrative.NarrativeStatus.GENERATED);
        text.setDiv(this.getXhtml(rawContent));

        return text;
    }

    private XhtmlNode getXhtml(String[] rawContent) {
        XhtmlNode node = new XhtmlNode(NodeType.Element, "pre");
        XhtmlNode child = new XhtmlNode(NodeType.Text);
        StringBuilder content = new StringBuilder();

        for (String line: rawContent) {
            content.append(line.trim());
            content.append(System.lineSeparator());
        }
        child.setContent(content.toString());
        node.getChildNodes().add(child);
        return node;
    }

    private String getTitle(String raw) {
        if (raw.trim().startsWith("LOCAL TITLE:")) {
            return raw.trim().substring(13);
        } else {
            return "";
        }
    }

    private Composition.DocumentConfidentiality getConfidentiality(String input) {

        switch (input.toUpperCase()) {
            case "N":
                return Composition.DocumentConfidentiality.N;
            case "U":
                return Composition.DocumentConfidentiality.U;
            case "L":
                return Composition.DocumentConfidentiality.L;
            case "M":
                return Composition.DocumentConfidentiality.M;
            case "R":
                return Composition.DocumentConfidentiality.R;
            case "V":
                return Composition.DocumentConfidentiality.V;
            default:
                return Composition.DocumentConfidentiality.NULL;
        }

    }
    private Composition.CompositionStatus getStatus(String input) {

        switch (input.toLowerCase()) {
            case "final":
                return Composition.CompositionStatus.FINAL;
            case "preliminary":
                return Composition.CompositionStatus.PRELIMINARY;
            case "amended":
                return Composition.CompositionStatus.AMENDED;
            case "enered-in-error":
                return Composition.CompositionStatus.ENTEREDINERROR;
            default:
                return Composition.CompositionStatus.NULL;
        }

    }
}
