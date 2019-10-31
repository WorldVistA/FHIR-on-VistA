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
package com.healthconcourse.vista.fhir.api.test.mocks;

import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;

import java.util.Date;
import java.util.HashMap;

public class MockVistaData implements VistaData {

    public MockVistaData() {
    }

    public MockVistaData(String url) {

    }

    @Override
    public String getPatientData(String icn) {
        if (icn.equalsIgnoreCase("5000001534V744140")) {
            return "5000001534V744140^HYPERTENSION,PATIENT FEMALE^555-555-1938^FEMALE^09/13/1928^98765 Street St,,,";
        } else if (icn.equalsIgnoreCase("5000001519V211431")) {
            return "5000001519V211431^CDSSEVEN,PATIENT^9995263294^FEMALE^12/28/1986^780 FIRST STREET,,,^ANYTOWN^OKLAHOMA^99999";
        } else {
            return "thisIsNotDataFromVista";
        }
    }

    @Override
    public String getPatientData(String name, String ssn, Date dob, AdministrativeGender gender) {

        if(ssn.equalsIgnoreCase("555-555-1938")) {
            return "5000001534V744140^HYPERTENSION,PATIENT FEMALE^555-555-1938^FEMALE^09/13/1928^98765 Street St,,,";
        } else {
            return "ThisIsNotVistaData";
        }
    }

    @Override
    public String getConditions(String name, String ssn, Date dob, AdministrativeGender gender) {
        return null;
    }

    @Override
    public String getConditions(String icn) {
        String input ="";

        if(icn.equalsIgnoreCase("444")) {
            return "";
        } else {
            return "7849159139V519746^V-999-9000011-1225^473.9;UNSPECIFIED SINUSITIS (CHRONIC);ICD^19381021^;^INACTIVE^^problem|V-999-9000011-1226^R69.;Illness, unspecified;10D^19810323065524-0400^68496003;Polyp of colon^ACTIVE^^problem|V-999-9000011-1227^R69.;Illness, unspecified;10D^19900304121344-0400^53741008;Coronary arteriosclerosis^ACTIVE^^problem|V-999-9000011-1228^R69.;Illness, unspecified;10D^19970511231944-0400^68496003;Polyp of colon^ACTIVE^^problem|V-999-9000011-1229^R69.;Illness, unspecified;10D^19980625101218-0400^713197008;Recurrent rectal polyp^ACTIVE^^problem|V-999-9000011-1230^R69.;Illness, unspecified;10D^19980702065943-0400^109838007;Overlapping malignant neoplasm of colon^ACTIVE^^problem|";
        }
    }

    @Override
	public String getConditions(HashMap<String,String> options) {
        return "7849159139V519746^V-999-9000011-1225^473.9;UNSPECIFIED SINUSITIS (CHRONIC);ICD^19381021^;^INACTIVE^^problem|V-999-9000011-1226^R69.;Illness, unspecified;10D^19810323065524-0400^68496003;Polyp of colon^ACTIVE^^problem|V-999-9000011-1227^R69.;Illness, unspecified;10D^19900304121344-0400^53741008;Coronary arteriosclerosis^ACTIVE^^problem|V-999-9000011-1228^R69.;Illness, unspecified;10D^19970511231944-0400^68496003;Polyp of colon^ACTIVE^^problem|V-999-9000011-1229^R69.;Illness, unspecified;10D^19980625101218-0400^713197008;Recurrent rectal polyp^ACTIVE^^problem|V-999-9000011-1230^R69.;Illness, unspecified;10D^19980702065943-0400^109838007;Overlapping malignant neoplasm of colon^ACTIVE^^problem|" ;
    }

    @Override
    public String getPatientsByCondition(String code) {

        if(code.equalsIgnoreCase("12345")) {
            return "";
        } else {
            return "38341003^3043460050V279846|ZZZRETTWOSIXTYTWO,PATIENT^5000000359V775335|DIABETIC,PATIENT MALE^5000001533V676621|DEPRESSION,PATIENT FEMALE^5000001534V744140|HYPERTENSION,PATIENT FEMALE^6343735560V532404|ONEHUNDREDEIGHTYFOUR,PATIENT";
        }
    }

    @Override
    public String putTheCondition(Condition theCondition){
        if(theCondition.getId().equalsIgnoreCase("12345")) {
            return "";
        } else {
            return "201"; //mec... yoyo... TODO: need to determine a successful test case
        }
    }


    @Override
    public String getVitalsObservationsByIcn(String icn) {
        System.out.println("vitals thread: " + Thread.currentThread().getName());

        if(icn.equalsIgnoreCase("444")) {
            return "";
        } else {
            return "5000001534V744140^27113001|Body weight|144|20140630|442V29141^27113001|Body weight|145|20141125|442V29148^27113001|Body weight|151|2 0141229|442V29154^27113001|Body weight|143|20150107|442V29164^27113001|Body weight|149|20150630|442V29317^27113001|Body weight|145|2 0150706|442V29325^27113001|Body weight|142|20150810|442V29392^50373000|Body height|62|20140630|442V29142^50373000|Body height|62|201 50706|442V29323^75367002|Blood pressure|128/70|20140630|442V29137^75367002|Blood pressure|120/66|20141125|442V29143^75367002|Blood p ressure|123/71|20141229|442V29149^75367002|Blood pressure|115/64|20141229|442V29155^75367002|Blood pressure|107/58|20150107|442V2915 9^75367002|Blood pressure|115/70|20150630|442V29311^75367002|Blood pressure|103/62|20150706|442V29318^75367002|Blood pressure|118/66 |20150810|442V29386^78564009|Pulse rate|57|20140630|442V29138^78564009|Pulse rate|56|20141125|442V29144^78564009|Pulse rate|62|20141 229|442V29150^78564009|Pulse rate|59|20141229|442V29156^78564009|Pulse rate|63|20150107|442V29160^78564009|Pulse rate|63|20150630|44 2V29312^78564009|Pulse rate|65|20150706|442V29319^78564009|Pulse rate|62|20150810|442V29387^86290005|Respiration|14|20140630|442V291 40^86290005|Respiration|14|20141125|442V29147^86290005|Respiration|19|20141229|442V29153^86290005|Respiration|13|20141229|442V29158^ 86290005|Respiration|14|20150107|442V29163^86290005|Respiration|17|20150630|442V29315^86290005|Respiration|15|20150706|442V29320^862 90005|Respiration|12|20150810|442V29390^386725007|Body Temperature|98.9|20140630|442V29139^386725007|Body Temperature|97.8|20141125| 442V29146^386725007|Body Temperature|98.8|20141229|442V29152^386725007|Body Temperature|98.4|20150107|442V29162^386725007|Body Tempe rature|98.6|20150630|442V29314^386725007|Body Temperature|98.1|20150706|442V29321^386725007|Body Temperature|98.7|20150810|442V29389";
        }
    }

    @Override
    public String getObservationsByIcnAndCode(String icn, String code) {
        if(icn.equalsIgnoreCase("444")) {
            return "";
        } else {
            return "5000001534V744140^27113001|Body weight|144|20140630|442V29141^27113001|Body weight|145|20141125|442V29148^27113001|Body weight|151|2 0141229|442V29154^27113001|Body weight|143|20150107|442V29164^27113001|Body weight|149|20150630|442V29317^27113001|Body weight|145|2 0150706|442V29325^27113001|Body weight|142|20150810|442V29392^50373000|Body height|62|20140630|442V29142^50373000|Body height|62|201 50706|442V29323^75367002|Blood pressure|128/70|20140630|442V29137^75367002|Blood pressure|120/66|20141125|442V29143^75367002|Blood p ressure|123/71|20141229|442V29149^75367002|Blood pressure|115/64|20141229|442V29155^75367002|Blood pressure|107/58|20150107|442V2915 9^75367002|Blood pressure|115/70|20150630|442V29311^75367002|Blood pressure|103/62|20150706|442V29318^75367002|Blood pressure|118/66 |20150810|442V29386^78564009|Pulse rate|57|20140630|442V29138^78564009|Pulse rate|56|20141125|442V29144^78564009|Pulse rate|62|20141 229|442V29150^78564009|Pulse rate|59|20141229|442V29156^78564009|Pulse rate|63|20150107|442V29160^78564009|Pulse rate|63|20150630|44 2V29312^78564009|Pulse rate|65|20150706|442V29319^78564009|Pulse rate|62|20150810|442V29387^86290005|Respiration|14|20140630|442V291 40^86290005|Respiration|14|20141125|442V29147^86290005|Respiration|19|20141229|442V29153^86290005|Respiration|13|20141229|442V29158^ 86290005|Respiration|14|20150107|442V29163^86290005|Respiration|17|20150630|442V29315^86290005|Respiration|15|20150706|442V29320^862 90005|Respiration|12|20150810|442V29390^386725007|Body Temperature|98.9|20140630|442V29139^386725007|Body Temperature|97.8|20141125| 442V29146^386725007|Body Temperature|98.8|20141229|442V29152^386725007|Body Temperature|98.4|20150107|442V29162^386725007|Body Tempe rature|98.6|20150630|442V29314^386725007|Body Temperature|98.1|20150706|442V29321^386725007|Body Temperature|98.7|20150810|442V29389";
        }
    }


    @Override
    public String getObservationsByCriteria(String name, String ssn, Date dob, AdministrativeGender gender) {
        if(name.equalsIgnoreCase("NoOne")) {
            return "";
        } else {
            return "5000001534V744140^27113001|Body weight|144|20140630|442V29141^27113001|Body weight|145|20141125|442V29148^27113001|Body weight|151|2 0141229|442V29154^27113001|Body weight|143|20150107|442V29164^27113001|Body weight|149|20150630|442V29317^27113001|Body weight|145|2 0150706|442V29325^27113001|Body weight|142|20150810|442V29392^50373000|Body height|62|20140630|442V29142^50373000|Body height|62|201 50706|442V29323^75367002|Blood pressure|128/70|20140630|442V29137^75367002|Blood pressure|120/66|20141125|442V29143^75367002|Blood p ressure|123/71|20141229|442V29149^75367002|Blood pressure|115/64|20141229|442V29155^75367002|Blood pressure|107/58|20150107|442V2915 9^75367002|Blood pressure|115/70|20150630|442V29311^75367002|Blood pressure|103/62|20150706|442V29318^75367002|Blood pressure|118/66 |20150810|442V29386^78564009|Pulse rate|57|20140630|442V29138^78564009|Pulse rate|56|20141125|442V29144^78564009|Pulse rate|62|20141 229|442V29150^78564009|Pulse rate|59|20141229|442V29156^78564009|Pulse rate|63|20150107|442V29160^78564009|Pulse rate|63|20150630|44 2V29312^78564009|Pulse rate|65|20150706|442V29319^78564009|Pulse rate|62|20150810|442V29387^86290005|Respiration|14|20140630|442V291 40^86290005|Respiration|14|20141125|442V29147^86290005|Respiration|19|20141229|442V29153^86290005|Respiration|13|20141229|442V29158^ 86290005|Respiration|14|20150107|442V29163^86290005|Respiration|17|20150630|442V29315^86290005|Respiration|15|20150706|442V29320^862 90005|Respiration|12|20150810|442V29390^386725007|Body Temperature|98.9|20140630|442V29139^386725007|Body Temperature|97.8|20141125| 442V29146^386725007|Body Temperature|98.8|20141229|442V29152^386725007|Body Temperature|98.4|20150107|442V29162^386725007|Body Tempe rature|98.6|20150630|442V29314^386725007|Body Temperature|98.1|20150706|442V29321^386725007|Body Temperature|98.7|20150810|442V29389";
        }
    }

    @Override
    public String getEncountersByPatient(String code) {
        return "";
    }

    @Override
    public String getMedicationStatement(String code) {
        if(code.equalsIgnoreCase("44")){
            return "3043460050V279846^";
        }

        return "5000001533V676621^507_55_1560^ACTIVE^SAL ACID, LACTIC ACID, FLEX COL^JUL 26, 2017@18:00^REASON^;IV PIGGYBACK;10MG^^|507_55_^^AMOXAPINE 100MG TAB^^REASON^;;100MG^^|507_52_404298^EXPIRED^SIMVASTATIN 80MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404299^EXPIRED^SIMVASTATIN 80MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404300^EXPIRED^LISINOPRIL 20MG TAB^NOV 19, 2013^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404301^DISCONTINUED^LISINOPRIL 20MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404302^EXPIRED^LISINOPRIL 40MG TAB^MAR 01, 2015@13:56^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404303^EXPIRED^CHLORTHALIDONE 25MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^45^90|507_52_404304^EXPIRED^ASPIRIN 81MG EC TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404305^EXPIRED^CALCIUM CARBONATE 1.25GM (CA 500MG) TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^270^90|507_52_404306^EXPIRED^GLYBURIDE 2.5MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404307^EXPIRED^LEVOTHYROXINE NA (SYNTHROID) 0.125MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404308^EXPIRED^VENLAFAXINE HCL 37.5MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^360^90|507_52_404309^DISCONTINUED^CHLORTHALIDONE 25MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^45^90|507_52_404310^EXPIRED^ASPIRIN 81MG EC TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404311^EXPIRED^CALCIUM CARBONATE 1.25GM (CA 500MG) TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^270^90|507_52_404312^EXPIRED^GLYBURIDE 2.5MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404313^EXPIRED^LEVOTHYROXINE NA (SYNTHROID) 0.125MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404314^EXPIRED^VENLAFAXINE HCL 37.5MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^360^90|507_52_404315^DISCONTINUED^CHLORTHALIDONE 25MG TAB^MAR 01, 2015@13:56^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404323^EXPIRED^CHOLECALCIFEROL (VIT D3) 1,000UNIT TAB^NOV 19, 2013^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404324^EXPIRED^CHLORTHALIDONE 25MG TAB^MAR 01, 2015^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404325^EXPIRED^CHOLECALCIFEROL (VIT D3) 1,000UNIT TAB^DEC 02, 2014^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404435^DISCONTINUED^HALOPERIDOL 10MG TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^20^10|507_52_404436^DISCONTINUED^WARFARIN (C0UMADIN) NA 7.5MG TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^30^30|507_52_404437^DISCONTINUED^THYROID 60-65MG (1 GR.) TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^12^30|\";\n";

    }

    @Override
    public String getMedicationAdministration(String code) {

        if(code.equalsIgnoreCase("44")){
            return "3043460050V279846^";
        }

        return "5000001533V676621^507_55_1560^ACTIVE^SAL ACID, LACTIC ACID, FLEX COL^JUL 26, 2017@18:00^REASON^;IV PIGGYBACK;10MG^^|507_55_^^AMOXAPINE 100MG TAB^^REASON^;;100MG^^|507_52_404298^EXPIRED^SIMVASTATIN 80MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404299^EXPIRED^SIMVASTATIN 80MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404300^EXPIRED^LISINOPRIL 20MG TAB^NOV 19, 2013^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404301^DISCONTINUED^LISINOPRIL 20MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404302^EXPIRED^LISINOPRIL 40MG TAB^MAR 01, 2015@13:56^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404303^EXPIRED^CHLORTHALIDONE 25MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^45^90|507_52_404304^EXPIRED^ASPIRIN 81MG EC TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404305^EXPIRED^CALCIUM CARBONATE 1.25GM (CA 500MG) TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^270^90|507_52_404306^EXPIRED^GLYBURIDE 2.5MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404307^EXPIRED^LEVOTHYROXINE NA (SYNTHROID) 0.125MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404308^EXPIRED^VENLAFAXINE HCL 37.5MG TAB^NOV 19, 2013@08:55^REASON^;ORAL (BY MOUTH);100MG^360^90|507_52_404309^DISCONTINUED^CHLORTHALIDONE 25MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^45^90|507_52_404310^EXPIRED^ASPIRIN 81MG EC TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404311^EXPIRED^CALCIUM CARBONATE 1.25GM (CA 500MG) TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^270^90|507_52_404312^EXPIRED^GLYBURIDE 2.5MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404313^EXPIRED^LEVOTHYROXINE NA (SYNTHROID) 0.125MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404314^EXPIRED^VENLAFAXINE HCL 37.5MG TAB^DEC 02, 2014@13:44^REASON^;ORAL (BY MOUTH);100MG^360^90|507_52_404315^DISCONTINUED^CHLORTHALIDONE 25MG TAB^MAR 01, 2015@13:56^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404323^EXPIRED^CHOLECALCIFEROL (VIT D3) 1,000UNIT TAB^NOV 19, 2013^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404324^EXPIRED^CHLORTHALIDONE 25MG TAB^MAR 01, 2015^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404325^EXPIRED^CHOLECALCIFEROL (VIT D3) 1,000UNIT TAB^DEC 02, 2014^REASON^;ORAL (BY MOUTH);100MG^90^90|507_52_404435^DISCONTINUED^HALOPERIDOL 10MG TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^20^10|507_52_404436^DISCONTINUED^WARFARIN (C0UMADIN) NA 7.5MG TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^30^30|507_52_404437^DISCONTINUED^THYROID 60-65MG (1 GR.) TAB^JUL 25, 2017^REASON^;ORAL (BY MOUTH);100MG^12^30|\";\n";
    }

    @Override
    public String getProceduresByIcn(String icn) {
        return "5000000352V586511^10431008|Neuroplasty of median nerve at carpal tunnel|64721|20140803|442_130_10114^19780006|Debridement of Infection of Skin|11000|20100304|442_130_10110";
    }

    @Override
    public String getLabObservationsByIcn(String icn) {
        System.out.println("labs thread: " + Thread.currentThread().getName());

        if(icn.equalsIgnoreCase("444")) {
            return "";
        }

        return "5000001533V676621^2000-8|CALCIUM:SCNC:PT:SER/PLAS:QN|70|20150720083901-0500|44263673^2000-8|CALCIUM:SCNC:PT:SER/PLAS:QN|139|20150720083901-0500|44263673^2000-8|CALCIUM:SCNC:PT:SER/PLAS:QN|162|20150720083901-0500|44263673^2000-8|CALCIUM:SCNC:PT:SER/PLAS:QN|1|20150720083901-0500|44263673^2093-3|CHOLESTEROL:MCNC:PT:SER/PLAS:QN|3.06|20131119092704-0500|44263673^2093-3|CHOLESTEROL:MCNC:PT:SER/PLAS:QN|60|20131119092704-0500|44263673^2093-3|CHOLESTEROL:MCNC:PT:SER/PLAS:QN|35|20150720083901-0500|44263673^2093-3|CHOLESTEROL:MCNC:PT:SER/PLAS:QN|0.051|20131119092704-0500|44263673^2093-3|CHOLESTEROL:MCNC:PT:SER/PLAS:QN|0.357|20131119092704-0500|44263673^2093-3|CHOLESTEROL:MCNC:PT:SER/PLAS:QN|1.428|20131119092704-0500|44263673^2093-3|CHOLESTEROL:MCNC:PT:SER/PLAS:QN|8|20150720083902-0500|44263673^2093-3|CHOLESTEROL:MCNC:PT:SER/PLAS:QN|2.9|20150720083901-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|5.1|20131119092704-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|34.2|20131119092704-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|231|20131119092704-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|7.9|20131119092704-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|7|20131119092704-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|4|20131119092704-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|25|20150720083901-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|92|20150720083901-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|12.6|20131119092704-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|0.204|20131119092704-0500|44263673^2350-7|GLUCOSE:MCNC:PT:UR:QN|1.9|20150720083902-0500|44263673^5901-4|COAGULATION TISSUE FACTOR INDUCED:TIME:PT:PPP~CONTROL:QN:COAG|7.1|20150720083903-0500|44263673";
    }

    @Override
    public String getHealthFactorObservationsByIcn(String icn) {
        System.out.println("health factors thread: " + Thread.currentThread().getName());

        if(icn.equalsIgnoreCase("444")) {
            return "";
        }

        return "5000001536V889384^266890009|Family History of Alcoholism|507_9000010.23_824|MINIMAL|10000000286|^371434005|History of Alcoholism|507_9000010.23_826|MODERATE|10000000285|";
    }

    @Override
    public String getMentalHealthObservationsByIcn(String icn) {

        if(icn.equalsIgnoreCase("444")) {
            return "";
        }
        return "5000000352V586511^44249-1|PHQ9 Result|715252007|PHQ9 Result|PHQ9|201411121444-0500|PRIMARY CARE TELEPHONE|PROVIDER,SIX|Total:4|V_500_601.84_100012^44249-1|PHQ9 Result|715252007|PHQ9 Result|PHQ9|201410081626-0500|PRIMARY CARE TELEPHONE|PROVIDER,SIX|Total:5|V_500_601.84_100011^|PCLC Result||PCLC Result|PCLC|201409101449-0500|MENTAL HEALTH|PROVIDER,THIRTYNINE|Total:52|V_500_601.84_100016^|AUDC Result||AUDC Result|AUDC|201408311400-0500|PRIMARY CARE|PROVIDER,SEVEN|Total:8|V_500_601.84_100009^71754-6|PC PTSD Result||PC PTSD Result|PC PTSD|201408311400-0500|PRIMARY CARE|PROVIDER,SEVEN|Total:3|V_500_601.84_100010^55758-7|PHQ-2 Result|454711000124102|PHQ-2 Result|PHQ-2|201408311400-0500|PRIMARY CARE|PROVIDER,SEVEN|Depression:4|V_500_601.84_100007^44249-1|PHQ9 Result|715252007|PHQ9 Result|PHQ9|201408311400-0500|PRIMARY CARE|PROVIDER,SEVEN|Total:7|V_500_601.84_100008^|GAF||GAF|Global Assessment of Functioning|20140910||PROVIDER,SEVEN|GAF Scale:65|V_500_627.8_172";
    }

    @Override
    public String getProvidersByIcn(String icn) {

        if(icn.equalsIgnoreCase("444")) {
            return "";
        }

        return "5000001533V676621^201311190800-0500|11525|PROVIDER,FIVE|Physician|PRIMARY^201311190924-0500|11537|PROVIDER,SIXTEEN|Physician|SECONDARY^201311190927-0500|11527|PROVIDER,SIXTEEN|Physician|SECONDARY^201409221300-0500|11531|PROVIDER,FIVE|Physician|PRIMARY^201409291257-0500|11538|PROVIDER,SIXTEEN|Physician|SECONDARY^201503011300-0500|11533|PROVIDER,FIVE|Physician|PRIMARY^201503131344-0500|11539|PROVIDER,SIXTEEN|Physician|SECONDARY^201507130821-0500|11615|NURSE,EIGHT|NURSE|PRIMARY^201507200839-0500|11616|PROVIDER,SIXTEEN|Physician|SECONDARY";
    }

    @Override
    public String getLocationByName(String name) {

        if(name.equalsIgnoreCase("nowhere")) {
            return "";
        }

        return "ALBANY FILE ROOM^77|ABILENE (CAA)|17034|11850 NW TEST LANE|SUITE 550|ABILENE|KANSAS|67410|ROB DAVIS_954-748-9999*DAVID TRUXALL_954-749-9999";
    }

    @Override
    public String getFlagByIcn(String icn) {

        if(icn.equalsIgnoreCase("444")) {
            return "";
        }

        return "5000000023V897100^FALL RISK|129839007|116|Local|ACTIVE|CAMP MASTER|CAMP MASTER|^IDENTITY THEFT|-99999999999999|118|Local|ACTIVE|CAMP MASTER|CAMP MASTER|^WANDERER|704448006|117|Local|ACTIVE|CAMP MASTER|CAMP MASTER|20151220";
    }

    @Override
    public String getAppointmentsByIcn(String icn) {
        if(icn.equalsIgnoreCase("444")) {
            return "";
        }

        return "5000001640V413398^1_198811010900-0500|ORTHO-CLINIC|CANCELLED BY PATIENT|SCHEDULED VISIT|REGULAR^1_198907030800-0500|TDENTAL|CANCELLED BY CLINIC|SCHEDULED VISIT|REGULAR^1_198908110800-0500|NEUROSURGEON|INPATIENT APPOINTMENT|SCHEDULED VISIT|REGULAR^1_199109300900-0500|PRIMARY CARE||SCHEDULED VISIT|REGULAR^1_199110010800-0500|PRIMARY CARE||SCHEDULED VISIT|REGULAR^1_199110020800-0500|GENERAL MEDICINE||SCHEDULED VISIT|REGULAR^1_199110021330-0500|AUDIOLOGY||SCHEDULED VISIT|REGULAR^1_199204010800-0500|GENERAL MEDICINE||SCHEDULED VISIT|REGULAR^1_199204010801-0500|GENERAL MEDICINE||UNSCHED. VISIT|REGULAR^1_199212161300-0500|FILE ROOM REQUEST|NO ACTION TAKEN|SCHEDULED VISIT|REGULAR^1_199212220900-0500|RYMANOWSKI|NO ACTION TAKEN|SCHEDULED VISIT|REGULAR^1_199310141400-0500|GENERAL MEDICINE||SCHEDULED VISIT|REGULAR^1_199310150800-0500|GENERAL MEDICINE||SCHEDULED VISIT|REGULAR^1_199401110900-0500|BON-HBHC SOCIAL WORK|CANCELLED BY PATIENT|SCHEDULED VISIT|REGULAR^1_199402281100-0500|PAT GM/SURG|NO ACTION TAKEN|SCHEDULED VISIT|REGULAR^1_199404151130-0500|NUCLEAR MEDICINE||SCHEDULED VISIT|REGULAR^1_199408290800-0500|X-RAY||SCHEDULED VISIT|REGULAR^1_199410210800-0500|SHERYL'S CLINIC|CANCELLED BY PATIENT|SCHEDULED VISIT|REGULAR^1_199410211000-0500|SHERYL'S CLINIC|NO ACTION TAKEN|SCHEDULED VISIT|REGULAR^1_199410280900-0500|ORTHO-CLINIC|NO ACTION TAKEN|SCHEDULED VISIT|REGULAR^1_199411170800-0500|SHERYL'S CLINIC||SCHEDULED VISIT|REGULAR^1_199411180900-0500|SHERYL'S CLINIC||SCHEDULED VISIT|REGULAR^1_199411220800-0500|SHERYL'S CLINIC|CANCELLED BY PATIENT|SCHEDULED VISIT|REGULAR^1_199509290800-0500|LINDA'S FUNNY CLINIC|CANCELLED BY PATIENT|SCHEDULED VISIT|REGULAR^1_199511050800-0500|GENERAL MEDICINE||SCHEDULED VISIT|REGULAR^1_199608231000-0500|PRIMARY CARE|NO ACTION TAKEN|SCHEDULED VISIT|REGULAR^1_199608301300-0500|20 MINUTE|CANCELLED BY CLINIC|SCHEDULED VISIT|REGULAR^1_199701270800-0500|SURGICAL CLINIC||SCHEDULED VISIT|REGULAR^1_199901251445-0500|MIKE'S MEDICAL CLINIC||UNSCHED. VISIT|REGULAR^1_199901251526-0500|MIKES MENTAL CLINIC||UNSCHED. VISIT|REGULAR";
    }

    @Override
    public String getAllergiesByIcn(String icn) {
        if(icn.equalsIgnoreCase("444")) {
            return "";
        }

        return "10110V004877^PENICILLIN|SCT:6369005|20130914|environment|confirmed|442_120.8_967~RASH:112625008~ITCHING,WATERING EYES:74776002^AMOXICILLIN|NDFRT:N0000005840~NDFRT:N0000145985~RXNORM:723~VANDF:4017605|20130914|environment|confirmed|442_120.8_967~DIARRHEA:62315008^CEFAZOLIN|NDFRT:N0000005828~NDFRT:N0000147751~RXNORM:2180~VANDF:4019659|20130914|environment|confirmed|442_120.8_967~HIVES:247472004^CITRUS|SCT:78161008|20130914|environment|confirmed|442_120.8_967~HIVES:247472004^SULFAMETHOXAZOLE|NDFRT:N0000006904~NDFRT:N0000146102~RXNORM:10180~VANDF:4017734|20130914|environment|confirmed|442_120.8_967~DIARRHEA:62315008^CHOCOLATE|SCT:102262009|20130914|environment|confirmed|442_120.8_967~DIARRHEA:62315008^CHLORPROMAZINE|NDFRT:N0000006947~NDFRT:N0000146214~RXNORM:2403~VANDF:4017861|20130914|environment|confirmed|442_120.8_967~DIARRHEA:62315008^BEE STINGS|SCT:157930002|20130914|environment|confirmed|442_120.8_967~ANAPHYLAXIS:39579001^ASPIRIN|NDFRT:N0000006582~NDFRT:N0000145918~RXNORM:1191~VANDF:4017536|20130914|environment|confirmed|442_120.8_967~BREAST ENGORGEMENT:49746001";
    }

    @Override
    public String getImmunizationsByIcn(String icn) {
        if(icn.equalsIgnoreCase("444")) {
            return "";
        }

        return "10110V004877^20000405112729-0500|109|PNEUMOCOCCAL, UNSPECIFIED FORMULATION|;||FT. LOGAN|;|^20040325083104-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20130602|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;|||;|^20130827141422-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20130828140108-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20130828143713-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20130828145435-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20130910091630-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20130918161830-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20130926174559-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20140320175526-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20140320175809-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|^20140516094757-0500|26|CHOLERA, ORAL (HISTORICAL)|;||CAMP MASTER|;|^20140516094757-0500|28|DT (PEDIATRIC)|1;SERIES 1||CAMP MASTER|2;IRRITABILITY|^20140516094835-0500|88|INFLUENZA, UNSPECIFIED FORMULATION (HISTORICAL)|;||CAMP MASTER|;|";
    }

    @Override
    public String getGoal(String code) {
        if(code.equalsIgnoreCase("44")){
            return "10104V248233^";
        }

        return "10104V248233^442_216.8_229_31_19970603080702-0500|sleeps 7-9 hours without awakening|19970617|WARDCLERK,FIFTYTHREE|0;CURRENT^442_216.8_229_31_19970603080705-0500|verbalizes less trouble falling asleep|19970617|WARDCLERK,FIFTYTHREE|0;CURRENT";
    }

    @Override
    public String getDiagnosticReport(String code) {
        if(code.equalsIgnoreCase("44")){
            return "5000001534V744140^";
        }

        return "5000001534V744140^IMG|unknown|20141229|PROGRAMMER,FIVE|442_74_596|CLINICAL HISTORY: Dyspnea and orthopnea. FINDINGS: LUNGS: diffuse opacities noted in bases bilaterally; most consistent withpulmonary edema. BONES: no fracture dislocation or abnormal lesions noted.MEDIASTINUM: normal width and contour. IMPRESSION: Exam most consistent with diffuse pulmonary edema.^IMG|unknown|20141229|PROGRAMMER,FIVE|442_74_597|EXAM: Transthoracic Echocardiogram CLINICAL HISTORY: Dyspnea and orthopnea. FINDINGS: VALVES: No stenosis or regurgitation noted. CHAMBERS: Both atria demonstrate normal size and movement.  The  right ventricle demonstrates normal size and function.  The Left ventricle is hypertrophic.  LVEF is estimated at 60%.   IMPRESSION: LVH with preserved LVEF of 60%.";
    }

    @Override
    public String getCarePlan(String code) {
        if(code.equalsIgnoreCase("44")){
            return "-1^Patient identifier not recognised";
        }

        return "{ \"CarePlan\": [ { \"Visit\": [ { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: active\", \"cpEnd\": \"\", \"cpEndFHIR\": \"\", \"cpEndHL7\": \"\", \"cpIntent\": \"plan\", \"cpStart\": 2821208, \"cpStartFHIR\": \"1982-12-08\", \"cpStartHL7\": 19821208, \"cpState\": \"active\", \"cpType\": \"careplan\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT DIABETES SELF MANAGEMENT PLAN (SCT:698360004) [C]\", \"hlfCategoryId\": 7071, \"hlfIen\": 933, \"hlfName\": \"SYN CP DIABETES SELF MANAGEMENT PLAN (SCT:698360004)\", \"hlfNameId\": 7072, \"hlfNameSCT\": 698360004, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_933\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End: \", \"cpState\": \"\", \"cpType\": \"addresses\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT DIABETES SELF MANAGEMENT PLAN (SCT:698360004) [C]\", \"hlfCategoryId\": 7071, \"hlfIen\": 934, \"hlfName\": \"SYN ADDR  (SCT:15777000)\", \"hlfNameId\": 7098, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_934\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT DIABETES SELF MANAGEMENT PLAN (SCT:698360004) [C]\", \"hlfCategoryId\": 7071, \"hlfIen\": 935, \"hlfName\": \"SYN ACT DIABETIC DIET (SCT:160670007)\", \"hlfNameId\": 7073, \"hlfNameSCT\": 160670007, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_935\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"activity\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT DIABETES SELF MANAGEMENT PLAN (SCT:698360004) [C]\", \"hlfCategoryId\": 7071, \"hlfIen\": 936, \"hlfName\": \"SYN ACT EXERCISE THERAPY (SCT:229065009)\", \"hlfNameId\": 7074, \"hlfNameSCT\": 229065009, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_936\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 937, \"hlfName\": \"SYN GOAL ADDRESS PATIENT KNOWLEDGE DEFICIT ON DIA (SCT:15777000)\", \"hlfNameId\": 7117, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_937\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 938, \"hlfName\": \"SYN GOAL IMPROVE AND MAINTENANCE OF OPTIMAL FOOT  (SCT:15777000)\", \"hlfNameId\": 7118, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_938\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 939, \"hlfName\": \"SYN GOAL MAINTAIN BLOOD PRESSURE BELOW 140\\/90 MMH (SCT:15777000)\", \"hlfNameId\": 7119, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_939\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 940, \"hlfName\": \"SYN GOAL GLUCOSE [MASS\\/VOLUME] IN BLOOD < 108 (SCT:15777000)\", \"hlfNameId\": 7120, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_940\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } }, { \"HealthFactor\": { \"auditTrail\": \"37-A 10000000285;\", \"comments\": \"Start: 2821208 End:  Status: in-progress\", \"cpState\": \"in-progress\", \"cpType\": \"goal\", \"dataSource\": \"DHP DATA INGEST\", \"dataSourceId\": 37, \"edited\": \"\", \"editedCd\": \"\", \"encounterProvider\": \"\", \"encounterProviderId\": \"\", \"eventDateTime\": \"DEC 08, 1982\", \"eventDateTimeFHIR\": \"1982-12-08\", \"eventDateTimeFM\": 2821208, \"eventDateTimeHL7\": 19821208, \"hlfCategory\": \"SYN CPCAT SELF CARE (SCT:326051000000105) [C]\", \"hlfCategoryId\": 7067, \"hlfIen\": 941, \"hlfName\": \"SYN GOAL HEMOGLOBIN A1C TOTAL IN BLOOD < 7.0 (SCT:15777000)\", \"hlfNameId\": 7121, \"hlfNameSCT\": 15777000, \"levelSeverity\": \"\", \"levelSeverityCd\": \"\", \"magnitude\": \"\", \"orderingProvider\": \"\", \"orderingProviderId\": \"\", \"package\": \"PCE PATIENT CARE ENCOUNTER\", \"packageId\": 507, \"patientName\": \"JAST445,LAVINA141\", \"patientNameId\": 102105, \"resourceId\": \"V_500_9000010.23_941\", \"resourceType\": \"HealthFactor\", \"ucumCode\": \"\", \"verified\": \"\", \"verifiedCd\": \"\", \"visit\": \"SEP 27, 2010@09:52\", \"visitFHIR\": \"2010-09-27T09:52:00-05:00\", \"visitFM\": 3100927.0952, \"visitHL7\": \"201009270952-0500\", \"visitId\": 34647 } } ] } ] }";
    }

    @Override
    public String getAllPatients(HashMap<String, String> options) {
        return "1003672118V388695^ZZZRETFIVEFORTYSEVEN,PATIENT^^MALE^11/05/1938^,,,|1005701355V934125^BCMA,EIGHTYTWO-PATIENT^^MALE^11/16/1944^,,,|1006145121V631417^BHIEPATIENT,J TEN^^MALE^12/28/1933^,,,|1006147126V079083^BHIEPATIENT,I NINE^^MALE^02/10/1995^,,,|1006147276V569483^BHIEPATIENT,H EIGHT^^MALE^02/10/1965^,,,|1006151329V503966^BHIEPATIENT,F SIX^^MALE^02/15/1997^,,,|1006152719V948936^BHIEPATIENT,G SEVEN^^MALE^04/05/1951^,,,|1006167324V385420^BHIEPATIENT,C THREE^^MALE^09/08/1962^,,,|1006170580V294705^BHIEPATIENT,E FIVE^^MALE^02/09/1984^,,,";
    }

    @Override
    public String getTiuNotes(String icn) {
        if(icn.equalsIgnoreCase("10112V399621")) {
            return "10112V399621~V_507_3857|20070516095921-0500||N|9990011642| LOCAL TITLE: ADVANCE DIRECTIVE COMPLETED                        ^STANDARD TITLE: ADVANCE DIRECTIVE                               ^DATE OF NOTE: MAY 16, 2007@09:59     ENTRY DATE: MAY 16, 2007@09:59:21      ^      AUTHOR: LABTECH,FIFTYNINE    EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^   VistA Imaging - Scanned Document^ ^/es/ FIFTYNINE LABTECH^^Signed: 05/16/2007 09:59~V_507_3116|20040401231238-0500||N|9990006949| LOCAL TITLE: AUDIOLOGY - HEARING LOSS CONSULT                   ^DATE OF NOTE: APR 01, 2004@23:12     ENTRY DATE: APR 01, 2004@23:12:38      ^      AUTHOR: PATHOLOGY,ONE        EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^HX:  Patient was seen for hearing aid fitting and orientation.^The batteries supplied for this hearing aid were: za312.^ ^IMP:   The veteran was seen for hearing aid fitting and orientation.  A complete ^orientation was performed, and the patient can insert the aids(s), change the ^batteries, and adjust the volume.  Response curves were obtained  using a real-^ear probe microphone in-situ Measurement System and indicated appropriate gain ^for the demonstrated hearing loss.  Care of the aid(s), warranty information and ^dangers of battery ingestion were discussed with the veteran. ^ ^REC:   Continued use of present amplification is recommended^ ^/es/ ONE PROVIDER^^Signed: 04/01/2004 23:13^for ONE PATHOLOGY                                 ^                                                  ~V_507_2893|20040331152253-0500||N|9990006915| LOCAL TITLE: GENERAL MEDICINE NOTE                              ^DATE OF NOTE: MAR 31, 2004@15:22     ENTRY DATE: MAR 31, 2004@15:22:53      ^      AUTHOR: LABTECH,SPECIAL      EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^Patient is in good health. Diabetes has been controlled through diet and ^excercise.^ ^/es/ ONE PROVIDER^PHYSICIAN^Signed: 03/31/2004 15:23^for SPECIAL LABTECH                               ^PHYSICIAN                                         ~V_507_2771|20040331121137-0500||N|9990006915| LOCAL TITLE: DIABETES                                           ^DATE OF NOTE: MAR 31, 2004@12:11     ENTRY DATE: MAR 31, 2004@12:11:37      ^      AUTHOR: LABTECH,SPECIAL      EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^CHIEF COMPLAINT: diabetic male presents with a chief ^complaint of weakness and disorientation. ^ ^HISTORY OF PRESENT ILLNESS: The patient awoke this morning feeling very ^weak, queasy, and disoriented. This is similar to episodes that he's had ^in the past when he's had low blood sugar. His wife had difficulty ^getting him up out of bed due to leg weakness and discoordination. She ^gave him some orange juice and brought him into the ER for evaluation. ^He reports that over the past few days he has noted increased leg ^swelling, facial puffiness, and increased SOB. He has noted some PND and ^orthopnea. He has had some vague chest discomfort this morning, left ^anterior chest, which is now resolved. Otherwise, he reports that he's ^been doing reasonably well without episodes of hypoglycemia. He's not ^been experiencing chest pain. He generally eats no added salt diet. He's ^been taking his medications as prescribed, however, his wife questions ^this at times. ^ ^CURRENT MEDICATIONS: ^^Active Inpatient and Outpatient Medications (including Supplies): ^^     Outpatient Medications                                 Status ^========================================================================= ^1)   SIMVASTATIN 40MG TAB TAKE ONE TABLET BY BY MOUTH       ACTIVE ^       EVERY EVENING ^^ALLERGIES: Penicillins. ^ ^SOCIAL HISTORY: The patient lives in Timbuktu with his spouse of many ^years. He's longtime retired. He does not smoke or use tobacco products. ^He does not use alcohol. ^ ^PAST MEDICAL HISTORY: 1) Longstanding type 2 diabetes mellitus with the ^following complications: nephropathy, severe neuropathy. 2) Recurrent ^osteomyelitis of the toes with amputations. 3) Coronary artery disease, ^status post CABG. 4) Chronic atrial fibrillation. 5) Chronic ^anticoagulation. 6) Hyperlipidemia. 7) Hypertension. 8) Chronic renal ^insufficiency. 9) Chronic anemia. 10) Diverticulosis. 11) History of ^congestive heart failure. ^ ^PAST SURGICAL HISTORY: 1) CABG. 2) Toe amputations as described above. ^ ^ROS: As reviewed in the HPI. ^ ^PHYSICAL EXAMINATION: On admission B/P was 139/73, pulse 63, weight 182 ^lbs. (this was up about 7 pounds since last seen in the clinic two weeks ^ago). In general the patient appears an ill, weak, elderly appearing ^male in no acute distress. He is oriented and is able to give a good ^history. ^ ^Skin: Somewhat clammy without notable rash. ^ ^HEENT: Eyes: Sclerae anicteric. Oropharynx with moist mucous membranes. ^ ^Neck: Supple without palpable mass or adenopathy. ^ ^Lungs: Diminished throughout, particularly diminished in the bases ^bilaterally. There is dullness to percussion in both bases. I hear faint ^basilar rales, not prominent. ^ ^Heart: Distant, regular rate and rhythm with a 2 out of 6 systolic ^murmur heard loudest at the left upper sternal border. S1 is normal. S2 ^has a fixed split. I do not hear and S3 gallop. PMI is nondisplaced. ^ ^Abdomen: Nondistended with active bowel sounds. No focal mass or ^tenderness. No organomegaly. ^ ^GU/Rectal: Deferred. ^ ^Extremities: Cool. Feet are somewhat cyanotic. Pedal pulses are ^diminished and sensation is diminished in a stocking distribution. ^ ^Neurologic: The patient presently is alert and oriented. Speech is ^normal. Thought processes are logical. There is no focal motor deficit. ^ ^EKG appears sinus rhythm with a right bundle branch block. There is some ^ST abnormality, nonspecific. Chest x-ray: There is cardiomegaly with ^hilar fullness and cephalization. There appears to be bilateral pleural ^effusions consistent with heart failure. ^ ^ASSESSMENT: X: 1. Probable hypoglycemic reaction. ^               2. Worsening congestive failure. Recent echocardiogram ^                   revealed good LV function. There was also pulmonary ^                   hypertension. It does appear that there is a ^                        component of right heart failure and possibly ^                        diastolic dysfunction. ^               3. Chronic renal insufficiency. The patient has not ^                   tolerated ace inhibitors in the past. ^               4. Coronary artery disease, status post CABG. Cannot ^                   exclude component of ischemia. ^               5. Type 2 diabetes mellitus, longstanding with multiple ^                   complications. ^ ^PLAN: Admit patient to Acute Medical Unit for close observation and ^further evaluation. Initially decrease Insulin by half and monitor his ^capillary blood sugars closely. Initiate diuresis with IV Furosemide. ^Monitor cardiac enzymes and serial EKG's.^ ^/es/ ONE PROVIDER^PHYSICIAN^Signed: 03/31/2004 12:11^for SPECIAL LABTECH                               ^PHYSICIAN                                         ~V_507_2078|20010523035044-0500||N|9990006949| LOCAL TITLE: CAMPER02                                           ^DATE OF NOTE: MAY 23, 2001@03:50     ENTRY DATE: MAY 23, 2001@03:50:44      ^      AUTHOR: PATHOLOGY,ONE        EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^Y9VT8VC76CX7X4646^TWELVE,PATIENT is in an acute yahoo state.  The cause of this condition is ^most^likely too much fun at New VistAs in Patient Care.  This is considered a^healthy state and to maintain condition, the patient is required to^purchase drinks at the hotel bar for the instructors.^ ^/es/ ONE PROVIDER^^Signed: 05/23/2001 03:51^for ONE PATHOLOGY                                 ^                                                  ~V_507_2077|20010523034939-0500||N|9990006949| LOCAL TITLE: 21 DAY CERTIFICATION                               ^DATE OF NOTE: MAY 23, 2001@03:49     ENTRY DATE: MAY 23, 2001@03:49:39      ^      AUTHOR: PATHOLOGY,ONE        EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^TY97T8B7TR 87R7^ ^/es/ ONE PROVIDER^^Signed: 05/23/2001 03:50^for ONE PATHOLOGY                                 ^                                                  ~V_507_1784|20001018160441-0500||N|9990002252| LOCAL TITLE: ADVANCE DIRECTIVE                                  ^DATE OF NOTE: OCT 18, 2000@16:04:41  ENTRY DATE: OCT 18, 2000@16:04:41      ^      AUTHOR: WARDCLERK,SIXTYEIGH  EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^TWELVE,PATIENT has requested that he not be resuscitated in the event that^he goes into respiratory distress.   See attached scanned document for^patient's signature. ^ ^/es/ ONE PROVIDER^^Signed: 10/18/2000 16:04^for SIXTYEIGHT WARDCLERK                          ^MD,DDS,DDA,                                       ~V_507_1783|20001018160417-0500||N|9990002252| LOCAL TITLE: ADVANCE DIRECTIVE                                  ^DATE OF NOTE: OCT 18, 2000@16:04:17  ENTRY DATE: OCT 18, 2000@16:04:17      ^      AUTHOR: WARDCLERK,SIXTYEIGH  EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^TWELVE,PATIENT has requested that he not be resuscitated in the event that^he goes into respiratory distress.   See attached scanned document for^patient's signature. ^ ^/es/ ONE PROVIDER^^Signed: 10/18/2000 16:04^for SIXTYEIGHT WARDCLERK                          ^MD,DDS,DDA,                                       ~V_507_1708|20000521120444-0500||N|9990007384| LOCAL TITLE: CRISIS NOTE                                        ^DATE OF NOTE: MAY 21, 2000@12:04:44  ENTRY DATE: MAY 21, 2000@12:04:44      ^      AUTHOR: VEHU,TWENTYONE       EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^Patient is armed and dangerous.^ ^/es/ TWENTYONE CAMP^^Signed: 05/21/2000 12:05~V_507_1689|20000521115612-0500||N|9990007384| LOCAL TITLE: CRISIS NOTE                                        ^DATE OF NOTE: MAY 21, 2000@11:56:13  ENTRY DATE: MAY 21, 2000@11:56:12      ^      AUTHOR: VEHU,TWENTYONE       EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^Patient called Dr. Strange and reported suicidal thoughts. Family member ^reported patient acting inappropriately in a subsequent call.^ ^/es/ TWENTYONE CAMP^^Signed: 05/21/2000 11:56~V_507_1562|20000521103350-0500||N|9990007384| LOCAL TITLE: CRISIS NOTE                                        ^DATE OF NOTE: MAY 21, 2000@10:33     ENTRY DATE: MAY 21, 2000@10:33:50      ^      AUTHOR: VEHU,TWENTYONE       EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^Patient is having a bad hair day.^ ^/es/ TWENTYONE CAMP^^Signed: 05/21/2000 10:34~V_507_1521|20000521100358-0500||N|9990007384| LOCAL TITLE: GENERAL MEDICINE NOTE                              ^DATE OF NOTE: MAY 21, 2000@10:03:58  ENTRY DATE: MAY 21, 2000@10:03:58      ^      AUTHOR: VEHU,TWENTYONE       EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^Patient is totally deaf and is only able communicate using sign language.^ ^/es/ TWENTYONE CAMP^^Signed: 05/21/2000 10:04~V_507_972|19990126114439-0500||N|9990005438| LOCAL TITLE: NURSING NOTE                                       ^STANDARD TITLE: RN CLINICAL NOTE                                ^DATE OF NOTE: JAN 26, 1999@11:44:39  ENTRY DATE: JAN 26, 1999@11:44:39      ^      AUTHOR: PROVIDER,PRF         EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^ ^HT:   ^WT:   ^ ^/es/ ONE PROVIDER^^Signed: 01/26/1999 11:44^for PRF PROVIDER                                  ^COMPUTER WIZARD                                   ~V_507_971|19990126113726-0500||N|9990005438| LOCAL TITLE: NURSING NOTE                                       ^STANDARD TITLE: RN CLINICAL NOTE                                ^DATE OF NOTE: JAN 26, 1999@11:37:26  ENTRY DATE: JAN 26, 1999@11:37:26      ^      AUTHOR: PROVIDER,PRF         EXP COSIGNER:                           ^     URGENCY:                            STATUS: COMPLETED                     ^^HT:   ^WT:   ^ ^/es/ ONE PROVIDER^^Signed: 01/26/1999 11:37^for PRF PROVIDER                                  ^COMPUTER WIZARD                                   ^\n";
        } else {
            return "";
        }
    }

    @Override
    public String getAllCareTeams() {
        return null;
    }

    @Override
    public String getCareTeamByHame(String name) {
        return null;
    }
}
