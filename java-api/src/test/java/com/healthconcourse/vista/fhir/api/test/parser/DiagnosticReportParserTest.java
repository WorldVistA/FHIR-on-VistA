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
package com.healthconcourse.vista.fhir.api.test.parser;

import com.healthconcourse.vista.fhir.api.parser.DiagnosticReportParser;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class DiagnosticReportParserTest {

    @Test
    public void TestSuccesfulDiagnosticReportParse() {
        String input = "5000001534V744140^IMG|unknown|20141229|PROGRAMMER,FIVE|442_74_596|CLINICAL HISTORY: Dyspnea and orthopnea. FINDINGS: LUNGS: diffuse opacities noted in bases bilaterally; most consistent withpulmonary edema. BONES: no fracture dislocation or abnormal lesions noted.MEDIASTINUM: normal width and contour. IMPRESSION: Exam most consistent with diffuse pulmonary edema.^IMG|unknown|20141229|PROGRAMMER,FIVE|442_74_597|EXAM: Transthoracic Echocardiogram CLINICAL HISTORY: Dyspnea and orthopnea. FINDINGS: VALVES: No stenosis or regurgitation noted. CHAMBERS: Both atria demonstrate normal size and movement.  The  right ventricle demonstrates normal size and function.  The Left ventricle is hypertrophic.  LVEF is estimated at 60%.   IMPRESSION: LVH with preserved LVEF of 60%.";

        DiagnosticReportParser parser = new DiagnosticReportParser();

        List<DiagnosticReport> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 2, result.size());
    }

    @Test
    public void TestInvalidDiagnosticReportParse() {
        String input = "5000001534V744140^IMG|unknown|";

        DiagnosticReportParser parser = new DiagnosticReportParser();

        List<DiagnosticReport> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestNoDataDiagnosticReportParse() {
        String input = "";

        DiagnosticReportParser parser = new DiagnosticReportParser();

        List<DiagnosticReport> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }


    @Test
    public void TestGarbageDataDiagnosticReportParse() {
        String input = "dsewrq %%9/^sdfg";

        DiagnosticReportParser parser = new DiagnosticReportParser();

        List<DiagnosticReport> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }


    @Test
    public void TestSuccesfulUpdatedDiagnosticReportParse() {
        String input = "10101V964144^IMG|VERIFIED|199902171140-0500|WARDCLERK,FIFTYTHREE|V_500_74_501||||NORMAL||DKJF;LSKDFE^IMG|VERIFIED|200909011216-0500|LABTECH,FORTYEIGHT|V_500_74_530||||NORMAL||^MHDX|VERIFIED|200403081125-0500|LABTECH,SPECIAL|V_500_627.8_169|428.22|441481004|";

        DiagnosticReportParser parser = new DiagnosticReportParser();

        List<DiagnosticReport> result = parser.parseList(input);

        Assert.assertEquals("Correct number of items", 3, result.size());
    }
}
