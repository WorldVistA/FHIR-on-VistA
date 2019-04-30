SYNDHP40 ; HC/fjf/art - HealthConcourse - retrieve patient encounters ;04/15/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
 ; ---------------- Get patient encounters ----------------------------
 ;
PATENCI(RETSTA,DHPICN,FRDAT,TODAT) ; Patient primary encounters for ICN
 ;
 ; Return primary encounters/visits for a given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDAT   - from date (inclusive), optional, compared to .01 VISIT/ADMIT DATE&TIME
 ;   TODAT   - to date (inclusive), optional, compared to .01 VISIT/ADMIT DATE&TIME
 ; Output:
 ;   RETSTA  - a delimited string that lists the following information
 ;      PatientICN ^ RESOURSE ID ^ TYPE ^ ENCOUNTER DATE ^ REASON ^ ICD DIAGNOSIS CODE ; ICD DIAGNOSIS NAME 
 ;      ^ SERVICE CATEGORY ^ SOURCE ^  
 ;      ^ LOCATION OF ENCOUNTER , LOCATION ADDRESS SEPARATED BY SEMICOLONS ^ HOSPITAL LOCATION
 ;      ^ PROVIDER(S) SEPARATED BY SEMICOLONS
 ;
 ;   Identifier will be "V_"_SITE ID_"_"_FILE #_"_"_FILE IEN   i.e. V_500_9000010_930
 ;
 S FRDAT=$S($G(FRDAT):$$HL7TFM^XLFDT(FRDAT),1:1000101)
 S TODAT=$S($G(TODAT):$$HL7TFM^XLFDT(TODAT),1:9991231)
 I $G(DEBUG) W !,"FRDAT: ",FRDAT,"   TODAT: ",TODAT,!
 I FRDAT>TODAT S RETSTA="-1^From date is greater than to date" QUIT
 ;
 ; validate ICN
 I $G(DHPICN)="" S RETSTA="-1^What patient?" QUIT
 I '$$UICNVAL^SYNDHPUTL(DHPICN) S RETSTA="-1^Patient identifier not recognised" Q
 ;
 N C,ACU,P,S,IDENT,ENCDT,ENCDTI,TYPE,SERV,SOURCE,LOC,HLOC,LOCI,LOCD,LOCA,SITE,CONIEN
 N DIAGC,DIAGN,ENFLG,DATEN,DISD,PROV,REASON,PROV,PROV1,PATIEN,RETDESC,PERIOD,PERIODFM
 N DIEN
 S C=",",P="|",S=";"
 S SITE=$P($$SITE^VASITE,"^",3)
 ;
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 S RETDESC=""
 ;
 S DIEN=""
 F  S DIEN=$O(^AUPNVSIT("C",PATIEN,DIEN)) QUIT:DIEN=""  D
 . N VISITX,VISITE
 . D GETS^DIQ(9000010,DIEN_",",".01;.05;.06;.07;.22;15002;15003;81203","EIN","VISITX","VISITE")
 . QUIT:$D(VISITE)
 . I $G(VISITX(9000010,DIEN_",",15003,"I"))'="P" QUIT
 . N IDENT,PERIOD,ENCDTI,TYPE,SERV,SOURCE,LOC,HLOC,LOCI,LOCD,LOCA,CONIEN,DIAGC,DIAGN
 . N PDO,ENFLG,DATEN,DISD,PROV,REASON,DIAGC,PROV1,PROV
 . S IDENT=$$RESID^SYNDHP69("V",SITE)_"_9000010_"_DIEN
 . S PERIODFM=$G(VISITX(9000010,DIEN_",",.01,"I"))   ; encounter date/time
 . QUIT:((PERIODFM\1)<FRDAT)!((PERIODFM\1)>TODAT)  ;quit if outside of requested date range
 . S PERIOD=$$FMTHL7^XLFDT($G(VISITX(9000010,DIEN_",",.01,"I")))   ; date/time, HL7 format
 . S ENCDTI=$P($G(VISITX(9000010,DIEN_",",.01,"I")),".",1)
 . S ENFLG=0,CONIEN=""
 . F  S CONIEN=$O(^AUPNPROB("AC",PATIEN,CONIEN)) QUIT:CONIEN=""  D  QUIT:ENFLG=1  ;get first diagnosis for patient
 .. S DATEN=$$GET1^DIQ(9000011,CONIEN_",",.08,"I") QUIT:DATEN'=$P($G(ENCDTI),".",1)  ;date/time must equal date entered
 .. S REASON=$$GET1^DIQ(9000011,CONIEN_",",1.01)   ; reason
 .. S DIAGC=$$GET1^DIQ(9000011,CONIEN_",",.01,"I") ; condition
 .. S DIAGN=$G(^ICD9(DIAGC,68,1,1))           ; diagnosis
 .. S ENFLG=1    ;set flag to know we have data and can move on
 . S:$D(VISITX(9000010,DIEN_",",15002,"E")) TYPE=$G(VISITX(9000010,DIEN_",",15002,"E"))    ; encounter type
 . S:$D(VISITX(9000010,DIEN_",",15003,"E")) DISD=$G(VISITX(9000010,DIEN_",",15003,"E"))    ; discharge disposition
 . S:$D(VISITX(9000010,DIEN_",",.06,"E")) LOC=$G(VISITX(9000010,DIEN_",",.06,"E")),LOCI=$G(VISITX(9000010,DIEN_",",.06,"I")) ; location
 . I $G(LOCI)'="" D
 .. S LOCD=$G(^DIC(4,LOCI,1))
 .. S LOCA=$P(LOCD,U,1)_S_$P(LOCD,U,2)_S_$P(LOCD,U,3)_S_$P(LOCD,U,4) ; location address
 . S:$D(VISITX(9000010,DIEN_",",.07,"E")) SERV=$G(VISITX(9000010,DIEN_",",.07,"E"))        ; service category
 . S:$D(VISITX(9000010,DIEN_",",.22,"E")) HLOC=$G(VISITX(9000010,DIEN_",",.22,"E"))        ; hospital location
 . S:$D(VISITX(9000010,DIEN_",",81203,"E")) SOURCE=$G(VISITX(9000010,DIEN_",",81203,"E"))  ; Source
 . S PDO=0,(PROV1,PROV)=""
 . F  S PDO=$O(^AUPNVPRV("AD",DIEN,PDO)) Q:PDO=""  D
 .. S PROV1=$P(^AUPNVPRV(PDO,0),U,1)
 .. S PROV=PROV_$$GET1^DIQ(200,PROV1_",",.01)_S
 . S RETDESC=RETDESC_$G(IDENT)_U_$G(TYPE)_U_$G(PERIOD)_U_$G(REASON)_U_$G(DIAGC)_S_$G(DIAGN)_U_$G(SERV)_U_$G(SOURCE)_U_$G(LOC)_C_$G(LOCA)_U_$G(HLOC)_U_PROV_P
 S RETSTA=DHPICN_U_RETDESC
 ;
 QUIT
 ;
