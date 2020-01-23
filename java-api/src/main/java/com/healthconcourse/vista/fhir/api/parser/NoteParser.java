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
package com.healthconcourse.vista.fhir.api.parser;

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import com.nfeld.jsonpathlite.JsonPath;
import com.nfeld.jsonpathlite.JsonResult;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;

public class NoteParser implements VistaParser<Composition> {

    private static final Logger LOG = LoggerFactory.getLogger(NoteParser.class);

    @Override
    public List<Composition> parseList(String httpData) {
        List<Composition> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult parsedJson = JsonPath.parseOrNull(httpData);
        if (parsedJson == null) {
            LOG.warn("Unable to parse Tiu Note JSON");
            return result;
        }
        JSONArray allNotes = parsedJson.read("$.TiuNotes");
        if (allNotes != null) {
            for (int i = 0; i < allNotes.length(); i++) {
                Composition note = new Composition();
                note.setMeta(ResourceHelper.getVistaMeta());
                JSONObject json = allNotes.getJSONObject(i).getJSONObject("Tiu");

                //Hard-coded according to VistA team
                note.setConfidentiality(this.getConfidentiality("N"));

                String id = json.getString("resourceId");
                note.setId(id);

                String icn = json.getString("patientICN");
                note.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, "", ResourceHelper.ReferenceType.Patient));

                String entry = json.getString( "entryDateTimeFHIR");
                if (!StringUtils.isEmpty(entry)) {
                    Optional<Date> entryDate = InputValidator.parseAnyDate(entry);
                    if (entryDate.isPresent()) {
                        note.setDate(entryDate.get());
                    }
                }
                String status = json.getString("statusFHIR");
                if (!StringUtils.isEmpty(status)) {
                    note.setStatus(getStatus(status));
                }
                String author = json.getString("authorDictator");
                String authorId = json.getString("authorDictatorResId");
                if (!StringUtils.isEmpty(author)) {
                    note.setAuthor(ResourceHelper.createSingleReferenceAsList(HcConstants.URN_VISTA_PRACTITIONER, authorId, author, ResourceHelper.ReferenceType.Practitioner));
                }
                String title = json.getString("documentType");
                if (!StringUtils.isEmpty(title)) {
                    note.setTitle(title);
                }
                String content = json.getString( "reportText");
                note.setText(this.setContent(content));

                String loincCode = json.getString("loincCode");
                String loincDesc = json.getString("loincDesc");
                note.setType(ResourceHelper.createCodeableConcept(HcConstants.LOINC, loincCode, loincDesc));

                result.add(note);
            }
        }
        return result;
    }

    private Narrative setContent(String rawContent) {
        Narrative text = new Narrative();
        text.setStatus(Narrative.NarrativeStatus.GENERATED);
        text.setDiv(this.getXhtml(rawContent));

        return text;
    }

    private XhtmlNode getXhtml(String rawContent) {
        XhtmlNode node = new XhtmlNode(NodeType.Element, "pre");
        XhtmlNode child = new XhtmlNode(NodeType.Text);
        child.setContent(rawContent);
        node.getChildNodes().add(child);
        return node;
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
