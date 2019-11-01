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
package com.healthconcourse.vista.fhir.api.test.parser;

import com.healthconcourse.vista.fhir.api.parser.MedicationParser;
import org.hl7.fhir.r4.model.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class MedicationParserTest {


    @Test
    public void TestSuccesfulMedicationAdminParse() {
        String input = "3043460050V279846^507_52_404253^EXPIRED^ACEBUTOLOL HCL 200MG CAP^APR 28, 2015^REASON^;ORAL (BY MOUTH);^180^90|507_52_404254^DISCONTINUED BY PROVIDER^AMLODIPINE BESYLATE 5MG TAB^APR 28, 2015^REASON^;ORAL (BY MOUTH);^90^90|507_52_404255^EXPIRED^FUROSEMIDE 10MG/ML ORAL SOLN^APR 28, 2015^REASON^;ORAL (BY MOUTH);^5^90|507_52_404256^DISCONTINUED BY PROVIDER^METOLAZONE 2.5MG TAB^APR 28, 2015^REASON^;ORAL (BY MOUTH);^90^90|507_52_404262^EXPIRED^CLOPIDOGREL BISULFATE 75MG TAB^APR 29, 2015^REASON^;ORAL (BY MOUTH);^60^30|";

        MedicationParser parser = new MedicationParser();

        List<MedicationAdministration> result = parser.parseMedicationAdmin(input);

        Assert.assertEquals("Correct number of items", 5, result.size());
    }

    @Test
    public void TestSuccesfulMedicationAdminBadDataParse() {
        String input = "3043460050V279846^507_52_404253^ACTIVE^ACEBUTOLOL HCL 200MG CAP^TEN 13, 2015^REASON^SITE;ORAL (BY MOUTH);200MG^180^90|507_52_404254^DISCONTINUED BY PROVIDER^AMLODIPINE BESYLATE 5MG TAB^APR 28, 2015^REASON^;TOPICAL;5^90^90|507_52_404255^EXPIRED^FUROSEMIDE 10MG/ML ORAL SOLN^APR 28, 2015^REASON^;NASAL;^5^90|507_52_404256^^METOLAZONE 2.5MG TAB^APR 28, 2015^REASON^;ORAL (BY MOUTH);^90^90|507_52_404262^EXPIRED^CLOPIDOGREL BISULFATE 75MG TAB^APR 29, 2015^REASON^;ORAL (BY MOUTH);^60^30|";

        MedicationParser parser = new MedicationParser();

        List<MedicationAdministration> result = parser.parseMedicationAdmin(input);

        Assert.assertEquals("Correct number of items", 5, result.size());
    }

    @Test
    public void TestInvaledMedicationAdminParse() {
        String input = "3043460050V279846^";

        MedicationParser parser = new MedicationParser();

        List<MedicationAdministration> result = parser.parseMedicationAdmin(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestSuccesfulDispenseParse() {
        String input = "5000001533V676621^507_55_1560^ACTIVE^SAL ACID, LACTIC ACID, FLEX COL^JUL 26, 2017@18:00^REASON^;IV PIGGYBACK;10MG^^|507_55_^^AMOXAPINE 100MG TAB^^REASON^;;100MG^^|507_52_404298^EXPIRED^SIMVASTATIN 80MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404299^EXPIRED^SIMVASTATIN 80MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404300^EXPIRED^LISINOPRIL 20MG TAB^NOV 19, 2013^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404301^DISCONTINUED^LISINOPRIL 20MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404302^EXPIRED^LISINOPRIL 40MG TAB^MAR 01, 2015@13:56^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404303^EXPIRED^CHLORTHALIDONE 25MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^45^90|507_52_404304^EXPIRED^ASPIRIN 81MG EC TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404305^EXPIRED^CALCIUM CARBONATE 1.25GM (CA 500MG) TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^270^90|507_52_404306^EXPIRED^GLYBURIDE 2.5MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404307^EXPIRED^LEVOTHYROXINE NA (SYNTHROID) 0.125MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404308^EXPIRED^VENLAFAXINE HCL 37.5MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^360^90|507_52_404309^DISCONTINUED^CHLORTHALIDONE 25MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^45^90|507_52_404310^EXPIRED^ASPIRIN 81MG EC TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404311^EXPIRED^CALCIUM CARBONATE 1.25GM (CA 500MG) TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^270^90|507_52_404312^EXPIRED^GLYBURIDE 2.5MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404313^EXPIRED^LEVOTHYROXINE NA (SYNTHROID) 0.125MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404314^EXPIRED^VENLAFAXINE HCL 37.5MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^360^90|507_52_404315^DISCONTINUED^CHLORTHALIDONE 25MG TAB^MAR 01, 2015@13:56^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404323^EXPIRED^CHOLECALCIFEROL (VIT D3) 1,000UNIT TAB^NOV 19, 2013^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404324^EXPIRED^CHLORTHALIDONE 25MG TAB^MAR 01, 2015^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404325^EXPIRED^CHOLECALCIFEROL (VIT D3) 1,000UNIT TAB^DEC 02, 2014^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404435^DISCONTINUED^HALOPERIDOL 10MG TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^20^10|507_52_404436^DISCONTINUED^WARFARIN (C0UMADIN) NA 7.5MG TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^30^30|507_52_404437^DISCONTINUED^THYROID 60-65MG (1 GR.) TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^12^30|";

        MedicationParser parser = new MedicationParser();

        List<MedicationDispense> result = parser.parseMedicationDispense(input);

        Assert.assertEquals("Correct number of items", 26, result.size());
    }

    @Test
    public void TestInvalidDispenseParse() {
        String input = "5000001533V676621^507_55_1560^";

        MedicationParser parser = new MedicationParser();

        List<MedicationDispense> result = parser.parseMedicationDispense(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestSuccesfulMedicationStatementParse() {
        String input = "2421492932V802082^V-999-52-2150^EXPIRED^CETIRIZINE HCL 5MG TAB^200711201737-0400;200711201737-0400;20081120^REASON^;;^30^30^RXN1014676|V-999-52-2151^EXPIRED^EPINEPHRINE (EQV-ADRENACLICK) 0.3MG/0.3M^200711201737-0400;200711201737-0400;20081120^REASON^;;^30^30^RXN1870230|V-999-52-2152^EXPIRED^APAP 325MG/DM 15MG/DOXYLAMINE 6.25MG/15M^201306131837-0400;201306131837-0400;20140614^REASON^;;^30^30^RXN1043400|V-999-52-2153^EXPIRED^IBUPROFEN 100MG TAB^201307111837-0400;201307111837-0400;20140712^REASON^;;^30^30^RXN198405|";

        MedicationParser parser = new MedicationParser();

        List<MedicationStatement> result = parser.parseMedicationStatement(input);

        Assert.assertEquals("Correct number of items", 4, result.size());
    }

    @Test
    public void TestInvalidMedicationStatementParse() {
        String input = "3043460050V279846^507_52_404253^EXPIRED^";

        MedicationParser parser = new MedicationParser();

        List<MedicationStatement> result = parser.parseMedicationStatement(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestEmptyMedicationStatementParse() {
        String input = "";

        MedicationParser parser = new MedicationParser();

        List<MedicationStatement> result = parser.parseMedicationStatement(input);

        Assert.assertEquals("Correct number of items", 0, result.size());
    }

    @Test
    public void TestAdditionOfRxNormCodes() {
        String input = "2421492932V802082^V-999-52-2150^EXPIRED^CETIRIZINE HCL 5MG TAB^200711201737-0400;200711201737-0400;20081120^REASON^;;^30^30^RXN1014676|V-999-52-2151^EXPIRED^EPINEPHRINE (EQV-ADRENACLICK) 0.3MG/0.3M^200711201737-0400;200711201737-0400;20081120^REASON^;;^30^30^RXN1870230|V-999-52-2152^EXPIRED^APAP 325MG/DM 15MG/DOXYLAMINE 6.25MG/15M^201306131837-0400;201306131837-0400;20140614^REASON^;;^30^30^RXN1043400|V-999-52-2153^EXPIRED^IBUPROFEN 100MG TAB^201307111837-0400;201307111837-0400;20140712^REASON^;;^30^30^RXN198405|";

        MedicationParser parser = new MedicationParser();

        List<MedicationStatement> result = parser.parseMedicationStatement(input);

        Assert.assertEquals("Correct number of items", 4, result.size());

        CodeableConcept code = result.get(0).getMedicationCodeableConcept();

        Assert.assertTrue("Record contains an RXNorm code", code.getCoding() != null);
    }
}
