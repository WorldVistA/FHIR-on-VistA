SYNDHP56 ; HC/ART - HealthConcourse - retrieve patient mental health observations ;05/04/2019
 ;;1.0;DHP;;Jan 17, 2017
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
 ; ---------------- Get patient Observations ----------------------
 ;  MH Administrations
 ;  GAF
 ;
PATOBS(RETSTA,NAME,SSN,DOB,GENDER,FRDAT,TODAT) ; get Observations for a patient by traits
 S RETSTA="-1^This service has been retired"
 QUIT
 ; Return patient Observations for name, SSN, DOB, and gender
 ;
 ; Input:
 ;   NAME    - patient name
 ;   SSN     - social security number
 ;   DOB     - date of birth
 ;   GENDER  - gender
 ;   FRDAT   - from date (inclusive), optional, compared to date given (MH) date/time of diagnosis (GAF)
 ;   TODAT   - to date (inclusive), optional, compared to date given (MH) date/time of diagnosis (GAF)
 ; Output:
 ;   RETSTA  - a delimited string that lists LOINC and SNOMED CT codes for the patient Observations
 ;           - ICN^SCT code|descrption|LOINC code|description|instrument name|date given|clinic name|ordered by|scale:score|identifier
 ;
 S FRDAT=$S($G(FRDAT):$$HL7TFM^XLFDT(FRDAT),1:1000101)
 S TODAT=$S($G(TODAT):$$HL7TFM^XLFDT(TODAT),1:9991231)
 I $G(DEBUG) W !,"FRDAT: ",FRDAT,"   TODAT: ",TODAT,!
 ;
 N C,P,S,ZARR,ICNST,DHPICN,PATIEN
 ;
 S C=",",P="|",S="_"
 D PATVAL^SYNDHP43(.ICNST,NAME,SSN,DOB,GENDER)
 I +ICNST'=1 S RETSTA=ICNST QUIT
 ; if we are here we are dealing with a valid patient
 S DHPICN=$P(ICNST,U,3)
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 ;
 S RETSTA=DHPICN
 D GET(.RETSTA,PATIEN,FRDAT,TODAT)
 ;
 QUIT
 ;
PATOBSI(RETSTA,DHPICN,FRDAT,TODAT) ; Patient Observations for ICN
 ;
 ; Return patient Observations for a given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDAT   - from date (inclusive), optional, compared to date given (MH) date/time of diagnosis (GAF)
 ;   TODAT   - to date (inclusive), optional, compared to date given (MH) date/time of diagnosis (GAF)
 ; Output:
 ;   RETSTA  - a delimited string that lists LOINC and SNOMED CT codes for the patient Observations
 ;           - ICN^LOINC code|description|SCT code|description|Instrument Name|date given|clinic name|ordered by|scale:score|resource ID^...
 ;
 ; validate ICN
 I $G(DHPICN)="" S RETSTA="-1^What patient?" QUIT
 I '$$UICNVAL^SYNDHPUTL(DHPICN) S RETSTA="-1^Patient identifier not recognised" Q
 ;
 S FRDAT=$S($G(FRDAT):$$HL7TFM^XLFDT(FRDAT),1:1000101)
 S TODAT=$S($G(TODAT):$$HL7TFM^XLFDT(TODAT),1:9991231)
 I $G(DEBUG) W !,"FRDAT: ",FRDAT,"   TODAT: ",TODAT,!
 I FRDAT>TODAT S RETSTA="-1^From date is greater than to date" QUIT
 ;
 N C,P,S,ZARR,PATIEN
 S C=",",P="|",S="_"
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 ;
 S RETSTA=DHPICN
 D GET(.RETSTA,PATIEN,FRDAT,TODAT)
 ;
 QUIT
 ;
GET(RETSTA,PATIEN,FRDAT,TODAT) ;
 ;
 ;Mental Health Observations (Assessments/Administrations)
 S RETSTA=RETSTA_$$MHOBS(PATIEN,FRDAT,TODAT)
 ;
 ;GAF (Global Assessment of Functioning)
 S RETSTA=RETSTA_$$GAFOBS(PATIEN,FRDAT,TODAT)
 ;
 QUIT
 ;
 ;
MHOBS(PATIEN,FRDAT,TODAT) ;Mental Health Observations
 ;patient's MH administration results - observation
 ;
 N PROCS,SERPROCS,SEQ,FNBR1,FNBR2
 N ADMINIEN,ADMINNBR,INSTID,INSTNAME,GIVDATE,GIVDATEHL,PROVO,PROVA,LOCID,LOCATION,SCALE,SCORE,PROVNAMO,PROVNAMA
 N SNOMED,SDESC,LOINC,LDESC,MH2LOINC,LOINC2MH,MH2SNOMED,SCT
 N RESIEN,RIENS
 N YS,YSRET,YSSEQ
 ;
 N USER S USER=$$DUZ^SYNDHP69
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 S PROCS=""
 S FNBR1=601.84 ;administrations
 S FNBR2=601.92 ;administration results
 ;D LOADREF(.MH2LOINC,.LOINC2MH,.MH2SNOMED)  ;load temporary code arrays
 S SEQ=0
 ;
 S YS("DFN")=PATIEN
 S YS("COMPLETE")="Y"
 D ADMINS^YTQAPI5(.YSRET,.YS)  ;call MH API
 I $G(DEBUG) D
 . W !,"Mental Health",!!
 . ;W ";                 1        2          3         4          5       6       7      8         9       10         11            12        13           14           15",!
 . ;W ";MH API output:Adm. ID^Inst. Name^Date Given^Date Saved^Orderer^Admin.By^Signed^# Answers^R_Privl^Is Legacy^INSTRUMENT id^Test IENS^copyright^location iens^DAY RESTART",!
 . ;ZWRITE YSRET
 . W "Adm. ID^Inst. Name^Date Given^Date Saved^Orderer^Admin.By^Signed^# Answers^R_Privl^Is Legacy^INSTRUMENT id^Test IENS^copyright^location iens^DAY RESTART",!
 QUIT:YSRET(1)["[ERROR]" SERPROCS
 S YSSEQ=2
 F  S YSSEQ=$O(YSRET(YSSEQ)) Q:YSSEQ=""  D
 . S ADMINNBR=$P(YSRET(YSSEQ),U,1) ;administration number
 . QUIT:ADMINNBR=""
 . S INSTNAME=$P(YSRET(YSSEQ),U,2) ;instrument name
 . S INSTID=$O(^YTT(601.71,"B",INSTNAME,"")) ;instrument id
 . S GIVDATE=$P(YSRET(YSSEQ),U,3) ;date given
 . QUIT:((GIVDATE\1)<FRDAT)!((GIVDATE\1)>TODAT)  ;quit if outside of requested date range
 . S GIVDATEHL=$$FMTHL7^XLFDT(GIVDATE) ;hl7 format date given
 . S PROVO=$P(YSRET(YSSEQ),U,5) ;ordered by
 . S:+PROVO PROVNAMO=$$GET1^DIQ(200,PROVO_",",.01) ;ordered by
 . S PROVA=$P(YSRET(YSSEQ),U,6) ;administered by
 . S:+PROVA PROVNAMA=$$GET1^DIQ(200,PROVA_",",.01) ;administered by
 . S LOCID=$P(YSRET(YSSEQ),U,14) ;location ien
 . S LOCATION=$$GET1^DIQ(44,LOCID_",",.01) ;location
 . ;get administration results
 . S RESULT=""
 . S RESIEN=""
 . F  S RESIEN=$O(^YTT(601.92,"AC",ADMINNBR,RESIEN)) Q:RESIEN=""  D
 . . S RIENS=RESIEN_","
 . . S SCALE=$$GET1^DIQ(FNBR2,RIENS,2) ;scale
 . . S SCORE=$$GET1^DIQ(FNBR2,RIENS,3) ;raw score
 . . S RESULT=SCALE_":"_SCORE
 . . S PID=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_ADMINNBR_S_FNBR2_S_RESIEN
 . . ;
 . . ;Look-up LOINC CODE & SNOMED CT code
 . . S LDESC=INSTNAME_" Result"
 . . S SDESC=INSTNAME_" Result"
 . . S LOINC=""
 . . I INSTID'="" D
 . . . S LOINC=$$MAP^SYNDHPMP("mh2loinc",INSTID,"D")
 . . . S LOINC=$S(+LOINC=-1:"",1:$P(LOINC,U,2))
 . . I LOINC="",INSTNAME'="" D
 . . . S LOINC=$$MAP^SYNDHPMP("mh2loinc",INSTNAME,"D")
 . . . S LOINC=$S(+LOINC=-1:"",1:$P(LOINC,U,2))
 . . S SCT=""
 . . I INSTID'="" D
 . . . S SCT=$$MAP^SYNDHPMP("mh2sct",INSTID,"D")
 . . . S SCT=$S(+SCT=-1:"",1:$P(SCT,U,2))
 . . I SCT="",INSTNAME'="" D
 . . . S SCT=$$MAP^SYNDHPMP("mh2sct",INSTNAME,"D")
 . . . S SCT=$S(+SCT=-1:"",1:$P(SCT,U,2))
 . . ;
 . . I $G(DEBUG) D
 . . . W !,"Admin Number:    ",ADMINNBR,!
 . . . W "Instrument:      ",INSTID,"     ",INSTNAME,!
 . . . W "Date Given:      ",GIVDATE,"     ",GIVDATEHL,!
 . . . W "Ordered by:      ",PROVO,"     ",PROVNAMO,!
 . . . W "Administered by: ",PROVA,"     ",PROVNAMA,!
 . . . W "Location:        ",LOCID,"     ",LOCATION,!
 . . . W "Results:     ",RESULT,!
 . . . W "LOINC Code:  ",LOINC,!
 . . . W "SNOMED Code: ",SCT,!
 . . . W "Description: ",SDESC,!
 . . ;
 . . ;record is created for each result for an instrument administration
 . . ;LOINC code|description|SCT code|descrption|Instrument Name|date given|clinic name|ordered by|scale:score|resource id
 . . S SEQ=SEQ+1
 . . S PROCS(SEQ)=U_LOINC_P_LDESC_P_SCT_P_SDESC_P_INSTNAME_P_GIVDATEHL_P_LOCATION_P_PROVNAMO_P_RESULT_P_PID
 ;
 ;I $G(DEBUG) W ! ZWRITE PROCS
 ;serialize data
 S SERPROCS=""
 S SEQ=""
 F  S SEQ=$O(PROCS(SEQ)) QUIT:SEQ=""  D
 . S SERPROCS=SERPROCS_PROCS(SEQ)
 ;
 ;I $G(DEBUG) W ! ZWRITE SERPROCS
 ;
 QUIT SERPROCS
 ;
GAFOBS(PATIEN,FRDAT,TODAT) ;get GAF score
 ;
 N PROCS,SERPROCS,SEQ,FNBR1,MHIEN,IENS
 N FILEDTFM,FILEDT,FILEDTHL,GAFDTFM,GAFDT,GAFDTHL,GAFBYID,GAFBY,TRANSCID,TRANSCR,GAF,RESULT,PID
 N SNOMED,SDESC,LOINC,LDESC,DESC,PATTYPE
 ;
 N USER S USER=$$DUZ^SYNDHP69
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 S (SNOMED,LOINC)=""
 S (SDESC,LDESC)="GAF"
 S DESC="Global Assessment of Functioning"
 ;
 I $G(DEBUG) W !,"GAF - Global Assessment of Functioning",!!
 S PROCS=""
 S FNBR1=627.8 ;Diagnostic Results-Mental Health
 S SEQ=0
 ;
 S MHIEN=""
 F  S MHIEN=$O(^YSD(627.8,"C",PATIEN,MHIEN)) QUIT:MHIEN=""  D
 . S IENS=MHIEN_","
 . S GAF=$$GET1^DIQ(FNBR1,IENS,65) ;axis 5 (GAF)
 . QUIT:GAF=""
 . S FILEDTFM=$$GET1^DIQ(FNBR1,IENS,.01,"I") ;file entry date fm format
 . S FILEDT=$$GET1^DIQ(FNBR1,IENS,.01) ;file entry date
 . S FILEDTHL=$$FMTHL7^XLFDT(FILEDTFM) ;hl7 format file entry date
 . S GAFDT=$$GET1^DIQ(FNBR1,IENS,.03) ;date/time of diagnosis
 . S GAFDTFM=$$GET1^DIQ(FNBR1,IENS,.03,"I") ;date/time of diagnosis fm format
 . QUIT:((GAFDTFM\1)<FRDAT)!((GAFDTFM\1)>TODAT)  ;quit if outside of requested date range
 . S GAFDTHL=$$FMTHL7^XLFDT(GAFDTFM) ;hl7 format date/time of diagnosis
 . S GAFBYID=$$GET1^DIQ(FNBR1,IENS,.04,"I") ;diagnosed by ien
 . S GAFBY=$$GET1^DIQ(FNBR1,IENS,.04) ;diagnosed by
 . S TRANSCID=$$GET1^DIQ(FNBR1,IENS,.05,"I") ;transcriber ien
 . S TRANSCR=$$GET1^DIQ(FNBR1,IENS,.05) ;transcriber
 . S RESULT="GAF Scale:"_GAF
 . S PATTYPE=$$GET1^DIQ(FNBR1,IENS,66,"I") ;patient type
 . S PID=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_MHIEN
 . I $G(DEBUG) D
 . . W "File Date Time: ",FILEDTFM,"     ",FILEDT,"     ",FILEDTHL,!
 . . W "GAF Date Time:  ",GAFDTFM,"     ",GAFDT,"     ",GAFDTHL,!
 . . W "Diagnosis by:   ",GAFBYID,"     ",GAFBY,!
 . . W "Transcriber:    ",TRANSCID,"     ",TRANSCR,!
 . . W "Patient Type:   ",PATTYPE,!
 . . W "Result:         ",RESULT,!
 . . W "LOINC Code:  ",LOINC,!
 . . W "SNOMED Code: ",SNOMED,!
 . . W "Description: ",SDESC,!
 . ;LOINC code|description|SCT code|descrption|Instrument Name|date given|clinic name|ordered by|scale:score|identifier
 . S SEQ=SEQ+1
 . S PROCS(SEQ)=U_LOINC_P_LDESC_P_SNOMED_P_SDESC_P_DESC_P_GAFDTHL_P_""_P_GAFBY_P_RESULT_P_PID
 ;
 ;I $G(DEBUG) W ! ZWRITE PROCS
 ;
 ;serialize data
 S SERPROCS=""
 S SEQ=""
 F  S SEQ=$O(PROCS(SEQ)) QUIT:SEQ=""  D
 . S SERPROCS=SERPROCS_PROCS(SEQ)
 ;
 ;I $G(DEBUG) W ! ZWRITE SERPROCS
 ;
 QUIT SERPROCS
 ;
 ; ----------- Unit Test -----------
T1 ;
 N ICN S ICN="5000000352V586511"
 N FRDAT S FRDAT=""
 N TODAT S TODAT=""
 N RETSTA
 D PATOBSI(.RETSTA,ICN,FRDAT,TODAT)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
