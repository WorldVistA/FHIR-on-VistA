SYNDHP03 ; HC/rbd/art - HealthConcourse - get patient condition/problem data ;05/04/2019
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
PATCONI(RETSTA,DHPICN,FRDAT,TODAT) ; Patient conditions for ICN
 ;
 ; Return patient conditions (problems) for a given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDATE  - from date (inclusive), optional, compared to .13 DATE OF ONSET
 ;   TODATE  - to date (inclusive), optional, compared to .13 DATE OF ONSET
 ; Output:
 ;   RETSTA  - a delimited string that lists the following information
 ;      PatientICN ^ resourceId ^ DiagnosisCode ; DiagnosisName ^ DateOfOnset ^ SNOMED CT Code ; SNOMED CT Name |
 ;      and continue this for every diagnosis for a particular patient ICN
 ;
 ;  Identifier will be "V_"_SITE ID_"_"_FILE #_"_"_FILE IEN   i.e. V_500_9000011_930
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
 N C,ACU,P,S,ONSET,DONSET,DIEN,RETDESC,SITE,SCTA
 N PATIEN
 S C=",",P="|",S=";"
 ;
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I $G(DEBUG) W "PATIEN: ",PATIEN,!
 S RETSTA=DHPICN
 S RETDESC=""
 S SITE=$P($$SITE^VASITE,"^",3)
 ;
 S DIEN=""
 F  S DIEN=$O(^AUPNPROB("AC",PATIEN,DIEN)) Q:DIEN=""  D
 . N PROBX,PROBE
 . D GETS^DIQ(9000011,DIEN_",",".01;.02;.13;80001","EIN","PROBX","PROBE")
 . QUIT:$D(PROBE)
 . N DIAGC,DIAGN,ONSET,DONSET,SNOMEDC,SNOMEDN,IDENT
 . I $D(PROBX(9000011,DIEN_",",.01,"E")) D
 . . S DIAGC=PROBX(9000011,DIEN_",",.01,"E")
 . . S DIAGN=$G(^ICD9(PROBX(9000011,DIEN_",",.01,"I"),68,1,1))
 . S DONSET=""
 . S ONSET=""
 . I $D(PROBX(9000011,DIEN_",",.13,"I")) D
 . . S ONSET=PROBX(9000011,DIEN_",",.13,"I")
 . . ; if no month or day of month is set, default to 01
 . . S:$E(ONSET,4,5)="00" $E(ONSET,4,5)="01"
 . . S:$E(ONSET,6,7)="00" $E(ONSET,6,7)="01"
 . . S DONSET=$$FMTHL7^XLFDT(ONSET)  ;date of onset set to HL7 format
 . QUIT:DONSET'=""&(((ONSET\1)<FRDAT)!((ONSET\1)>TODAT))  ;quit if outside of requested date range
 . I $D(PROBX(9000011,DIEN_",",80001,"E")) D
 . . S SNOMEDC=PROBX(9000011,DIEN_",",80001,"E")
 . . S SNOMEDN=$$USCTPT^SYNDHPUTL(SNOMEDC)
 . S IDENT=$$RESID^SYNDHP69("V",SITE)_"_9000011_"_DIEN
 . S RETDESC=RETDESC_IDENT_U_$G(DIAGC)_S_$G(DIAGN)_U_$G(DONSET)_U_$G(SNOMEDC)_S_$G(SNOMEDN)_P
 S RETSTA=RETSTA_U_RETDESC
 ;
 QUIT
 ;
 ;  ----------------  Patients for a given active condition  --------------
 ;
PATCONAL(RETSTA,DHPSCT) ; Patients for a given active condition
 ;
 ; Return list of all patients who have a particular active condition (problem)
 ;
 ; Input:
 ;   DHPSCT - SNOMED CT code  (mandatory)
 ; Output:
 ;   RETSTA  - a delimited string that lists SNOMED CDT codes for active  patient conditions
 ;           - SCT^ICN 1|patient name 1^ICN 2|patient name 2^...^ICN n|patient name n
 ;           - -1 - error
 ;
 ; validate SNOMED CT code
 I $G(DHPSCT)="" S RETSTA="-1^What code?" QUIT
 I +$$CODE^LEXTRAN(DHPSCT,"SCT")'=1 S RETSTA="-1^SNOMED CT code not recognised" Q
 ;
 N C,ACU,P,PATA,PATIEN,PAT
 S C=",",P="|"
 S (ACU,PATIEN)=""
 F  S ACU=$O(^PXRMINDX(9000011,"SCT","ISPP",DHPSCT,"A",ACU)) Q:ACU=""  D
 .F  S PATIEN=$O(^PXRMINDX(9000011,"SCT","ISPP",DHPSCT,"A",ACU,PATIEN)) Q:PATIEN=""  D
 ..S PATA($$UICN^SYNDHPUTL(PATIEN))=$$GET1^DIQ(2,PATIEN_C,.01)
 S RETSTA=DHPSCT
 S PAT=""
 F  S PAT=$O(PATA(PAT)) Q:PAT=""  S RETSTA=RETSTA_U_PAT_P_PATA(PAT)
 Q
 ;
 ; ----------- Unit Test -----------
T1 ;
 N ICN S ICN="10111V183702"
 N FRDAT S FRDAT=""
 N TODAT S TODAT=""
 N RETSTA
 D PATCONI(.RETSTA,ICN,FRDAT,TODAT)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
T2 ;
 N SNOMED S SNOMED=47505003
 N RETSTA
 D PATCONAL(.RETSTA,SNOMED)
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
