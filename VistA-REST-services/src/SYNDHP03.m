SYNDHP03 ; HC/rbd/art - HealthConcourse - get patient condition/problem data ;2019-10-28  3:08 PM
 ;;1.0;DHP;;Jan 17, 2017
 ;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 ; (c) 2017-2019 Perspecta
 ; (c) 2019 OSEHRA
 ; 
 ; Licensed under the Apache License, Version 2.0 (the "License");
 ; you may not use this file except in compliance with the License.
 ; You may obtain a copy of the License at
 ; 
 ; http://www.apache.org/licenses/LICENSE-2.0
 ; 
 ; Unless required by applicable law or agreed to in writing, software
 ; distributed under the License is distributed on an "AS IS" BASIS,
 ; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ; See the License for the specific language governing permissions and
 ; limitations under the License.
 ;
 QUIT
 ;
 ;----------------  Get Patient Conditions  ----------------------
 ;
PATCONDS(RETSTA,NAME,SSN,DOB,GENDER) ; get condition SCT codes for a patient by traits
 S RETSTA="-1^This service has been retired"
 QUIT
 ; Return list of active conditions (problems) for name, SSN, DOB, and gender
 ;   conditions without a SNOMED CT code are not be reported
 ;
 ; Input:
 ;   NAME    - patient name
 ;   SSN     - social security number
 ;   DOB     - date of birth
 ;   GENDER  - gender
 ; Output:
 ;   RETSTA  - a delimited string that lists SNOMED CDT codes for the patient conditions
 ;           - ICN^SCT code1^SCT code2^...^SCT coden
 ;
 N C,ACU,ICNST,DHPICN,PATIEN,SCT
 S C=","
 N SCTA
 ;
 D PATVAL^SYNDHP43(.ICNST,NAME,SSN,DOB,GENDER)
 I +ICNST'=1 S RETSTA=ICNST Q
 ; if we are here we are dealing with a valid patient
 S DHPICN=$P(ICNST,U,3)
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 ;
 S (ACU,SCT)=""
 F  S ACU=$O(^PXRMINDX(9000011,"SCT","PSPI",PATIEN,"A",ACU)) Q:ACU=""  D
 .F  S SCT=$O(^PXRMINDX(9000011,"SCT","PSPI",PATIEN,"A",ACU,SCT)) Q:SCT=""  D
 ..S SCTA(SCT)=""
 S RETSTA=DHPICN
 S SCT=""
 F  S SCT=$O(SCTA(SCT)) Q:SCT=""  S RETSTA=RETSTA_U_SCT
 Q
 ;
 ;
PATCONI(RETSTA,DHPICN,FRDAT,TODAT,RETJSON) ; Patient conditions for ICN
 ;
 ; Return patient conditions (problems) for a given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDATE  - from date (inclusive), optional, compared to .13 DATE OF ONSET
 ;   TODATE  - to date (inclusive), optional, compared to .13 DATE OF ONSET
 ;   RETJSON - J = Return JSON
 ;             F = Return FHIR
 ;             0 or null = Return string (default)
 ; Output:
 ;   RETSTA  - a delimited string that lists the following information
 ;      PatientICN ^ resourceId ^ DiagnosisCode ; DiagnosisName ; Coding System (ICD or 10D)^ DateOfOnset ^ SNOMED CT Code ; SNOMED CT Name ^ Status ^ Resolution Date ^ Condition Type (problem or encounter) |
 ;      and continue this for every diagnosis for a particular patient ICN
 ;    or patient conditions in JSON format
 ;
 ; Filter validation
 ; Id (e.g. V-999-9000011-1805)
 N QID,QIDENC,QIDPROB
 I $D(HTTPARGS("_id")) D  I $G(HTTPERR) QUIT
 . S QID=HTTPARGS("_id")
 . N P2,P3,P4
 . S P2=$P(QID,"-",2)
 . S P3=$P(QID,"-",3)
 . S P4=$P(QID,"-",4)
 . N SITE S SITE=$$SITE^VASITE($$DT^XLFDT)
 . N STATION S STATION=$P(SITE,U,3)
 . I P2'=STATION D ERRMSG^SYNDHPUTL(400,"Condition not located at this site") QUIT
 . I P3'=9000011 D ERRMSG^SYNDHPUTL(400,"Only problems are now supported") QUIT
 . I '$D(^AUPNPROB(P4,0)) D ERRMSG^SYNDHPUTL(400,"No such problem exists in VistA") QUIT
 . S QIDPROB=P4
 . N DFN S DFN=$P(^AUPNPROB(P4,0),U,2)
 . S DHPICN=$$GETICN^MPIF001(DFN) ; ICR 2701
 ;
 ; ICN
 I '$G(DHPICN),$D(HTTPARGS("patient")) S DHPICN=HTTPARGS("patient")
 I $G(DHPICN)="" S RETSTA="-1^What patient?" QUIT
 I '$$UICNVAL^SYNDHPUTL(DHPICN) S RETSTA="-1^Patient identifier not recognised" QUIT
 ;
 ; Category
 ; problem-list-item | encounter-diagnosis
 N QCATEGORY
 I $D(HTTPARGS("category")) D  I $G(HTTPERR) QUIT
 . S QCATEGORY=HTTPARGS("category")
 . I QCATEGORY'["problem",QCATEGORY'["encounter" D ERRMSG^SYNDHPUTL(400,"Invalid Category") QUIT
 ;
 ; Clinical Status
 ; active | recurrence | inactive | remission | resolved
 N QCLINSTATUS
 I $D(HTTPARGS("clinical-status")) D  I $G(HTTPERR) QUIT
 . S QCLINSTATUS=HTTPARGS("clinical-status")
 . I "^active^recurrence^inactive^remission^resolved^"'[(U_QCLINSTATUS_U) D ERRMSG^SYNDHPUTL(400,"Invalid Clinical Status") QUIT
 . ; VistA does not have recurrence and remission, so we will switch these to active and resolved.
 . I QCLINSTATUS="recurrence" S QCLINSTATUS="active"
 . I QCLINSTATUS="remission"  S QCLINSTATUS="resolved"
 . S QCLINSTATUS=$$UP^XLFSTR(QCLINSTATUS) ; Active and inactive are uppercased in VistA
 ;
 ; Code
 N QCODE,QCODETYPE
 I $D(HTTPARGS("code")) D
 . S QCODE=HTTPARGS("code")
 . I QCODE["|" D
 .. N TYPEURL S TYPEURL=$P(QCODE,"|"),QCODE=$P(QCODE,"|",2)
 .. I TYPEURL["sct" S QCODETYPE="SCT"
 .. I TYPEURL["icd" S QCODETYPE="ICD"
 ;
 ; Onset
 N QONSET
 N QONSETREV S QONSETREV=0 ; Reverse condition
 I $D(HTTPARGS("onset-date")) D  I $G(HTTPERR) QUIT
 . S QONSET=HTTPARGS("onset-date")
 . ;
 . ; Validate
 . n prefix s prefix=""
 . n date s date=""
 . n hasPrefix s hasPrefix=$E(QONSET,1,2)?2A
 . i hasPrefix s prefix=$E(QONSET,1,2),date=$E(QONSET,3,99)
 . e  s date=QONSET
 . i hasPrefix,"^eq^ne^lt^gt^ge^le^sa^eb^ap^"'[(U_prefix_U) D ERRMSG^SYNDHPUTL(400,"Invalid Date Prefix") QUIT
 . ;
 . ; Convert to FM Date
 . n fmdate s fmdate=$$HL7TFM^XLFDT($tr(date,"-"))
 . i fmdate=-1 D ERRMSG^SYNDHPUTL(400,"Invalid Date") QUIT
 . ;
 . ; Inexact Date?
 . n inexactMonth,inexactDay
 . s (inexactMonth,inexactDay)=0
 . i $e(fmdate,4,5)="00" s inexactMonth=1
 . i $e(fmdate,6,7)="00" s inexactDay=1
 . ;
 . ; Now set from and to for base case
 . i 'inexactMonth,'inexactDay S FRDAT=fmdate,TODAT=fmdate+1-.000001       ; Add day (30/31 no matter as this is only for range checking)
 . i inexactDay,'inexactMonth  S FRDAT=fmdate,TODAT=fmdate+100-.000001     ; Add month
 . i inexactMonth              S FRDAT=fmdate,TODAT=fmdate+10000-.000001   ; Add year
 . ;
 . ; Adjust for prefixes
 . i prefix="eq"  ; no change
 . i prefix="ne" S QONSETREV=1 ; reverse condition
 . i prefix="lt"!(prefix="eb") S FRDAT=0,TODAT=fmdate-.000001 ; less than, & ends before
 . i prefix="gt"!(prefix="sa") S FRDAT=fmdate+1,TODAT=9999999 ; greater than, & starts after
 . i prefix="le" S FRDAT=0,TODAT=fmdate                       ; less than or equals
 . i prefix="ge" S FRDAT=fmdate,TODAT=9999999                 ; greater than
 . i prefix="ap" n diff s diff=fmdate*.001,FRDAT=FRDAT-diff,TODAT=TODAT+diff ; approximately (within 3 months)
 ;
 N C,P,S
 S C=",",P="|",S=";"
 ;
 ; get patient IEN from ICN
 N PATIEN S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I $G(DEBUG) W "PATIEN: ",PATIEN,!
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 ;
 S RETSTA=DHPICN
 N RETDESC S RETDESC=""
 ;
 N CONDITION,DIAGC,DIAGN,DIGNCS,ONSET,DONSET,SNOMEDC,SNOMEDN,IDENT,CONTYPE
 N ENCONLY S ENCONLY=0         ; Encounter only conditions
 I $D(QCATEGORY),QCATEGORY'["problem" S ENCONLY=1  ; Don't go through problem list if we want only encounters
 ;
 N PROBIEN S PROBIEN=""
 ; QIDPROB: If we requested a problem by IEN, backtrack and loop to itself, then quit
 I $D(QIDPROB) S PROBIEN=QIDPROB-.0000001
 ; ENCONLY = Encounter only
 I 'ENCONLY F  S PROBIEN=$O(^AUPNPROB("AC",PATIEN,PROBIEN)) Q:PROBIEN=""  D  Q:$D(QIDPROB)
 . N PROBLEM
 . D GET1PROB^SYNDHP11(.PROBLEM,PROBIEN,0) ;get one Patient Problem (Condition) record
 . I $D(PROBLEM("Problem","ERROR")) M CONDITION("Conditions",DIEN)=PROBLEM QUIT
 . S ONSET=PROBLEM("Problem","dateOfOnsetFM")
 . S DIAGC=PROBLEM("Problem","diagnosis")
 . S DIAGN=PROBLEM("Problem","diagnosisText")
 . S DIGNCS=PROBLEM("Problem","codingSystem")
 . S DONSET=PROBLEM("Problem","dateOfOnsetHL7")  ;date of onset set to HL7 format
 . S SNOMEDC=PROBLEM("Problem","snomedCTconceptCode")
 . S SNOMEDN=PROBLEM("Problem","snomedCTconceptCodeText")
 . S IDENT=PROBLEM("Problem","resourceId")
 . N STATUS S STATUS=PROBLEM("Problem","status")
 . N RESDT  S RESDT=PROBLEM("Problem","dateResolvedHL7")
 . I RESDT  S STATUS="RESOLVED"
 . N CONTYPE S CONTYPE="problem" ; or (future enhancement) encounter
 . ;
 . ; Filters
 . I $D(QONSET) N INRANGE D  Q:'INRANGE
 .. W:$G(SYNDEBUG) QONSET," ",ONSET," ",FRDAT," ",TODAT
 .. S INRANGE=$$RANGECK^SYNDHPUTL(ONSET,FRDAT,TODAT)  ;quit if outside of requested date range
 .. I QONSETREV S INRANGE='INRANGE
 .. W:$G(SYNDEBUG) " ",$S(INRANGE:"OK",1:"NOT OK"),!
 . I $D(QCLINSTATUS),QCLINSTATUS'=STATUS QUIT
 . I $D(QCODE) N QUIT S QUIT=0 D  QUIT:QUIT
 .. N SCTMATCH S SCTMATCH=QCODE=SNOMEDC
 .. N ICDMATCH S ICDMATCH=QCODE=DIAGC
 .. I $D(QCODETYPE),QCODETYPE="SCT",'SCTMATCH S QUIT=1
 .. I $D(QCODETYPE),QCODETYPE="ICD",'ICDMATCH S QUIT=1
 .. I 'SCTMATCH,'ICDMATCH S QUIT=1
 . ; End filters
 . ;
 . ; Output
 . S RETDESC=RETDESC_IDENT_U_DIAGC_S_DIAGN_S_DIGNCS_U_DONSET_U_SNOMEDC_S_SNOMEDN_U_STATUS_U_RESDT_U_CONTYPE_P
 . M CONDITION("Conditions",PROBIEN)=PROBLEM
 S RETSTA=RETSTA_U_RETDESC
 ;
 I $G(RETJSON)="J"!($G(RETJSON)="F") D
 . S RETSTA=""
 . D TOJASON^SYNDHPUTL(.CONDITION,.RETSTA)
 ;
 QUIT
 ;
 ;  ----------------  Patients for a given active condition  --------------
 ;
PATCONAL(RETSTA,DHPSCT,RETJSON) ; Patients for a given active condition
 ;
 ; Return list of all patients who have a particular active condition (problem)
 ;
 ; Input:
 ;   DHPSCT - SNOMED CT code  (mandatory)
 ;   RETJSON - J = Return JSON
 ;             F = Return FHIR
 ;             0 or null = Return string (default)
 ; Output:
 ;   RETSTA  - a delimited string that lists SNOMED CDT codes for active  patient conditions
 ;           - SCT^ICN 1|patient name 1^ICN 2|patient name 2^...^ICN n|patient name n
 ;           - -1 - error
 ;          or patients with a condition in JSON format
 ;
 ; validate SNOMED CT code
 I $G(DHPSCT)="" S RETSTA="-1^What code?" QUIT
 I +$$CODE^LEXTRAN(DHPSCT,"SCT")'=1 S RETSTA="-1^SNOMED CT code not recognised" Q
 ;
 S SCTPF=$$USCTPT^SYNDHPUTL(DHPSCT) ;lookup preferred term for code
 N C,ACU,P,PATA,PATIEN,PAT,PATSWCON
 S C=",",P="|"
 S ACU=""
 F  S ACU=$O(^PXRMINDX(9000011,"SCT","ISPP",DHPSCT,"A",ACU)) Q:ACU=""  D
 . S PATIEN=""
 . F  S PATIEN=$O(^PXRMINDX(9000011,"SCT","ISPP",DHPSCT,"A",ACU,PATIEN)) Q:PATIEN=""  D
 . . S PATA($$UICN^SYNDHPUTL(PATIEN))=$$GET1^DIQ(2,PATIEN_C,.01)
 . . I $G(RETJSON)="J"!($G(RETJSON)="F") D
 . . . S PATSWCON("Patients",PATIEN,"patientName")=$$GET1^DIQ(2,PATIEN_C,.01)
 . . . S PATSWCON("Patients",PATIEN,"patientIcn")=$$UICN^SYNDHPUTL(PATIEN)
 . . . S PATSWCON("Patients",PATIEN,"patientId")=PATIEN
 . . . S PATSWCON("Patients",PATIEN,"conditionSCT")=DHPSCT
 . . . S PATSWCON("Patients",PATIEN,"conditionText")=SCTPF
 S RETSTA=DHPSCT
 S PAT=""
 F  S PAT=$O(PATA(PAT)) Q:PAT=""  D
 . S RETSTA=RETSTA_U_PAT_P_PATA(PAT)
 ;
 ;I $G(RETJSON)="F" D xxx  ;create array for FHIR
 ;
 I $G(RETJSON)="J"!($G(RETJSON)="F") D
 . S RETSTA=""
 . D TOJASON^SYNDHPUTL(.PATSWCON,.RETSTA)
 ;
 Q
 ;
 ; ----------- Unit Test -----------
TEST D EN^%ut($T(+0),3) QUIT
STARTUP ; [Constants]. Modify to suit your system so tests would pass.
 S ICN="2421492932V802082"
 S CODEICD="B00.2"
 S CODESCT="22298006"
 QUIT
SHUTDOWN K ICN,CODEICD,CODESCT QUIT
SETUP    K HTTPERR,FRDAT,TODAT,RETSTA,HTTPARGS QUIT
TEARDOWN K HTTPERR,FRDAT,TODAT,RETSTA,HTTPARGS QUIT
 ;
T1 ; @TEST Get All Problems for single patient
 D PATCONI(.RETSTA,ICN)
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T111 ; @TEST Get All Problems for single patient in category problem
 S HTTPARGS("category")="problem-list-item"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T112 ; @TEST Get All Problems for single patient in category encounter
 S HTTPARGS("category")="encounter-diagnosis"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKTF^%ut($L(RETSTA,"|")=1)
 QUIT
 ;
T113 ; @TEST Get All Problems for single patient in invalid category
 S HTTPARGS("category")="boo"
 D PATCONI(.RETSTA,ICN)
 D CHKTF^%ut(HTTPERR=400)
 QUIT
 ;
 ; "^active^recurrence^inactive^remission^resolved^"
T114 ; @TEST All Problem for single patient with clinical status active
 S HTTPARGS("clinical-status")="active"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") I $P(RETSTA,"|",I)'="" D CHKEQ^%ut($P($P(RETSTA,"|",I),U,5),"ACTIVE")
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T115 ; @TEST All Problem for single patient with clinical status inactive
 S HTTPARGS("clinical-status")="inactive"
 D PATCONI(.RETSTA,ICN)
 ; Adjust output so we can walk through it, as ICN is first piece, then the problem string
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") I $P(RETSTA,"|",I)'="" D CHKEQ^%ut($P($P(RETSTA,"|",I),U,5),"INACTIVE")
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T116 ; @TEST All Problem for single patient with clinical status resolved
 S HTTPARGS("clinical-status")="resolved"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") I $P(RETSTA,"|",I)'="" D CHKEQ^%ut($P($P(RETSTA,"|",I),U,5),"RESOLVED")
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T117 ; @TEST All Problem for single patient with invalid clinical status
 S HTTPARGS("clinical-status")="boo"
 D PATCONI(.RETSTA,ICN)
 D CHKTF^%ut(HTTPERR=400)
 QUIT
 ;
T118 ; @TEST Single problem by Code - SCT
 S HTTPARGS("code")=CODESCT
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKTF^%ut(RETSTA[CODESCT)
 D CHKTF^%ut(RETSTA'[CODEICD)
 QUIT
 ;
T1181 ; @TEST Single problem by Coding Sytem | Code - SCT
 S HTTPARGS("code")="sct|"_CODESCT
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKTF^%ut(RETSTA[CODESCT)
 D CHKTF^%ut(RETSTA'[CODEICD)
 QUIT
 ;
T119 ; @TEST Single problem by Code - ICD
 S HTTPARGS("code")=CODEICD
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKTF^%ut(RETSTA[CODEICD)
 D CHKTF^%ut(RETSTA'[CODESCT)
 QUIT
 ;
T120 ; @TEST problem by inexact date - year
 S HTTPARGS("onset-date")=2001
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 S HTTPARGS("onset-date")=2000
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKEQ^%ut(RETSTA,"")
 S HTTPARGS("onset-date")=2002
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKEQ^%ut(RETSTA,"")
 S HTTPARGS("onset-date")=2011
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T121 ; @TEST problem by inexact date - year/month
 S HTTPARGS("onset-date")="2012-05"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 S HTTPARGS("onset-date")="2012-06"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKEQ^%ut(RETSTA,"")
 QUIT
 ;
T122 ; @TEST problem by exact date
 S HTTPARGS("onset-date")="2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 S HTTPARGS("onset-date")="1982-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 D CHKEQ^%ut(RETSTA,"")
 QUIT
 ;
T123 ; @TEST problem by eqdate
 S HTTPARGS("onset-date")="eq2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T124 ; @TEST problem by nedate
 S HTTPARGS("onset-date")="ne2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T125 ; @TEST problem by ltdate
 S HTTPARGS("onset-date")="lt2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T126 ; @TEST problem by gtdate
 S HTTPARGS("onset-date")="gt2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T127 ; @TEST problem by ledate
 S HTTPARGS("onset-date")="le2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T128 ; @TEST problem by gedate
 S HTTPARGS("onset-date")="ge2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T129 ; @TEST problem by sadate
 S HTTPARGS("onset-date")="sa2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T130 ; @TEST problem by ebdate
 S HTTPARGS("onset-date")="eb2012-01-12"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T131 ; @TEST problem by apdate
 S HTTPARGS("onset-date")="ap2012-01"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T132 ; @TEST problem by _id
 S HTTPARGS("_id")="V-999-9000011-1807"
 D PATCONI(.RETSTA,ICN)
 S RETSTA=$E(RETSTA,$F(RETSTA,U),$L(RETSTA))
 N I F I=1:1:$L(RETSTA,"|") W $P(RETSTA,"|",I),!
 D CHKTF^%ut($L(RETSTA,"|"))
 QUIT
 ;
T2 ;
 N ICN S ICN="10112V399621"
 N FRDAT S FRDAT=""
 N TODAT S TODAT=""
 N RETSTA
 D PATCONI(.RETSTA,ICN,FRDAT,TODAT)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
T6 ;
 N ICN S ICN="10112V399621"
 N FRDAT S FRDAT=20000101
 N TODAT S TODAT=20051231
 N RETSTA
 D PATCONI(.RETSTA,ICN,FRDAT,TODAT)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
T7 ;
 N ICN S ICN="10112V399621"
 N FRDAT S FRDAT=""
 N TODAT S TODAT=""
 N JSON S JSON="J"
 N RETSTA
 D PATCONI(.RETSTA,ICN,FRDAT,TODAT,JSON)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
T3 ;
 N SNOMED S SNOMED=10509002
 N RETSTA
 D PATCONAL(.RETSTA,SNOMED)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
T4 ;
 N SNOMED S SNOMED=47505003
 N RETSTA
 D PATCONAL(.RETSTA,SNOMED)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
T5 ;
 N SNOMED S SNOMED=47505003
 N JSON S JSON="J"
 N RETSTA
 D PATCONAL(.RETSTA,SNOMED,JSON)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
