SYNDHP03 ; HC/rbd/art - HealthConcourse - get patient condition/problem data ;07/23/2019
 ;;1.0;DHP;;Jan 17, 2017
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
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
 ;      PatientICN ^ resourceId ^ DiagnosisCode ; DiagnosisName ^ DateOfOnset ^ SNOMED CT Code ; SNOMED CT Name |
 ;      and continue this for every diagnosis for a particular patient ICN
 ;    or patient conditions in JSON format
 ;
 S FRDAT=$S($G(FRDAT):$$HL7TFM^XLFDT(FRDAT),1:1000101)
 S TODAT=$S($G(TODAT):$$HL7TFM^XLFDT(TODAT),1:9991231)
 I $G(DEBUG) W !,"FRDAT: ",FRDAT,"   TODAT: ",TODAT,!
 I FRDAT>TODAT S RETSTA="-1^From date is greater than to date" QUIT
 ;
 ; validate ICN
 I $G(DHPICN)="" S RETSTA="-1^What patient?" QUIT
 I '$$UICNVAL^SYNDHPUTL(DHPICN) S RETSTA="-1^Patient identifier not recognised" QUIT
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
 N CONDITION,DIAGC,DIAGN,ONSET,DONSET,SNOMEDC,SNOMEDN,IDENT
 N PROBIEN S PROBIEN=""
 F  S PROBIEN=$O(^AUPNPROB("AC",PATIEN,PROBIEN)) Q:PROBIEN=""  D
 . N PROBLEM
 . D GET1PROB^SYNDHP11(.PROBLEM,PROBIEN,0) ;get one Patient Problem (Condition) record
 . I $D(PROBLEM("Problem","ERROR")) M CONDITION("Conditions",DIEN)=PROBLEM QUIT
 . S ONSET=PROBLEM("Problem","dateOfOnsetFM")
 . QUIT:'$$RANGECK^SYNDHPUTL(ONSET,FRDAT,TODAT)  ;quit if outside of requested date range
 . S DIAGC=PROBLEM("Problem","diagnosis")
 . S DIAGN=PROBLEM("Problem","diagnosisText")
 . S DONSET=PROBLEM("Problem","dateOfOnsetHL7")  ;date of onset set to HL7 format
 . S SNOMEDC=PROBLEM("Problem","snomedCTconceptCode")
 . S SNOMEDN=PROBLEM("Problem","snomedCTconceptCodeText")
 . S IDENT=PROBLEM("Problem","resourceId")
 . S RETDESC=RETDESC_IDENT_U_$G(DIAGC)_S_$G(DIAGN)_U_$G(DONSET)_U_$G(SNOMEDC)_S_$G(SNOMEDN)_P
 . M CONDITION("Conditions",PROBIEN)=PROBLEM
 S RETSTA=RETSTA_U_RETDESC
 ;
 ;I $G(RETJSON)="F" D xxx  ;create array for FHIR
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
T1 ;
 N ICN S ICN="2676604935V204585"
 N FRDAT S FRDAT=""
 N TODAT S TODAT=""
 N RETSTA
 D PATCONI(.RETSTA,ICN,FRDAT,TODAT)
 W $$ZW^SYNDHPUTL("RETSTA")
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
