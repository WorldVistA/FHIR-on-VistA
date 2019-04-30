SYNDHP48 ; HC/PWC/art - HealthConcourse - retrieve patient medication data ;04/15/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 Q
 ;
 ; ---------------- Get patient medication statement ----------------------------
PATMEDS(RETSTA,DHPICN,FRDAT,TODAT) ; Patient medication statement for ICN
 ;
 ; Return patient medication statement for a given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDAT   - from date (inclusive), optional
 ;   TODAT   - to date (inclusive), optional
 ; Output:
 ;   RETSTA  - a delimited string that lists the following information
 ;      PatientICN ^ RESOURSE ID ^ STATUS ^ MEDICATION ^ DATE ASSERTED ^ CONDITION
 ;      ^ DOSAGE , SITE ; ROUTE ; DOSE ^ QUANTITY ^ DAYS ^ RXN |
 ;
 ;   Identifier will be "V"_SITE ID_"_"_FILEMAN #_"_"_FILE IEN   i.e. V_500_55_930
 ;
 ; validate ICN
STM ;
 I $G(DHPICN)="" S RETSTA="-1^What patient?" QUIT
 I '$$UICNVAL^SYNDHPUTL(DHPICN) S RETSTA="-1^Patient identifier not recognised" Q
 ;
 S FRDAT=$S($G(FRDAT):$$HL7TFM^XLFDT(FRDAT),1:1000101)
 S TODAT=$S($G(TODAT):$$HL7TFM^XLFDT(TODAT),1:9991231)
 I $G(DEBUG) W !,"FRDAT: ",FRDAT,"   TODAT: ",TODAT,!
 I FRDAT>TODAT S RETSTA="-1^From date is greater than to date" QUIT
 ;
 N C,ACU,P,S,UL,D1,IDENT,ORDDAT,ORDNUM,ROUTE,STATUS,DOSORD,STDT,STDTFM,DRUG,SITE,SITEA
 N RETDESC,QTY,DAYS,RXN,IENS,IDATE,IDATEFM,RX,PATIEN,DRUGI
 S C=",",P="|",S=";",UL="_"
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 ;
 S RETDESC=""
 S SITE=$P($$SITE^VASITE,U,3)
 S RETSTA=""
 ; loop through the PHARMACY PATIENT file #55 (^PS(55)) for patient (this will contain both IP and OP orders)
 ;
 ; this is start of Inpatient Orders
 S D1=0
 F  S D1=$O(^PS(55,PATIEN,5,D1)) Q:D1'?1N.N  D
 .S IENS=D1_C_PATIEN_C
 .S (ORDDAT,ORDNUM,ROUTE,STATUS,DOSORD,STDT,STDTFM,DRUG,SITEA,QTY,DAYS,RXN)=""
 .N MEDX,MEDERR
 .D GETS^DIQ(55.06,IENS,".01;3;10;28","IEN","MEDX","MEDERR")
 .QUIT:$D(MEDERR)
 .S ORDNUM=$G(MEDX(55.06,IENS,.01,"E"))   ; order number
 .; route is most likely not stored in vista as SCT code, but should check this
 .S ROUTE=$G(MEDX(55.06,IENS,3,"E"))      ; route
 .S STATUS=$G(MEDX(55.06,IENS,28,"E"))    ; status
 .S DOSORD=$P($G(^PS(55,PATIEN,5,D1,.2)),U,2)          ; dosage ordered
 .S STDTFM=$G(MEDX(55.06,IENS,10,"I"))      ; start date
 .QUIT:((STDTFM\1)<FRDAT)!((STDTFM\1)>TODAT)  ;quit if outside of requested date range
 .S STDT=$$FMTHL7^XLFDT($G(MEDX(55.06,IENS,10,"I")))      ; start date
 .S DRUG=$$GET1^DIQ(55.07,1_C_IENS,.01,"E")  ; medication
 .S DRUGI=$$GET1^DIQ(55.07,1_C_IENS,.01,"I")
 .S RXN=$$GETRXN(DRUGI)
 .I RXN?1.N S RXN="RXN"_RXN
 .S IDENT=$$RESID^SYNDHP69("V",SITE)_UL_55_UL_PATIEN_UL_55.06_UL_D1
 .S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(STDT)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_$G(QTY)_U_$G(DAYS)_U_$G(RXN)_P
 ;
 ; start here for outpatient orders
 S D1=0
 F  S D1=$O(^PS(55,PATIEN,"P",D1)) Q:D1'?1N.N  D
 . S RX=$G(^PS(55,PATIEN,"P",D1,0))
 . S (IDENT,IDATE,DRUG,QTY,DAYS,STATUS,ROUTE,RXN)=""
 . N MEDX,MEDERR
 . D GETS^DIQ(52,RX_",",".01;1;2;6;7;8;100","IEN","MEDX","MEDERR")
 . QUIT:$D(MEDERR)
 . S IDENT=$$RESID^SYNDHP69("V",SITE)_UL_52_UL_RX
 . S IDATEFM=$G(MEDX(52,RX_",",1,"I"))     ;issue date
 . QUIT:((IDATEFM\1)<FRDAT)!((IDATEFM\1)>TODAT)  ;quit if outside of requested date range
 . S IDATE=$$FMTHL7^XLFDT($G(MEDX(52,RX_",",1,"I")))     ;issue date HL7
 . S DRUG=$G(MEDX(52,RX_",",6,"E"))      ;medication
 . S DRUGI=$G(MEDX(52,RX_",",6,"I"))
 . S RXN=$$GETRXN(DRUGI)
 . I RXN?1.N S RXN="RXN"_RXN
 . S QTY=$G(MEDX(52,RX_",",7,"E"))       ;quantity
 . S DAYS=$G(MEDX(52,RX_",",8,"E"))      ;days supply
 . S STATUS=$G(MEDX(52,RX_",",100,"E"))  ;status
 . S ROUTE=$$GET1^DIQ(52.0113,"1,"_RX_",",6,"EN")  ;route
 . S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(IDATE)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_$G(QTY)_U_$G(DAYS)_U_$G(RXN)_P
 S RETSTA=DHPICN_U_RETDESC
 Q
 ;
GETRXN(X,IO) ; get RxNorm code for drug
 ;
 N NAME,F50P6IEN,VUID,NDFRT,RXNORM
 S NAME=$$GET1^DIQ(50,X_",",20,"E")
 I $G(DEBUG) W !,"name: ",NAME
 I NAME'="" D
 .S F50P6IEN=$O(^PSNDF(50.6,"B",NAME,""))
 .I $G(DEBUG) W !,"ien: ",F50P6IEN
 .S VUID=$$GET1^DIQ(50.6,F50P6IEN_",",99.99)
 .I $G(DEBUG) W !,"vuid: ",VUID
 .S NDFRT=$$graphmap^SYNGRAPH("ndfrt-map",VUID,"NDFRT")
 .I $G(DEBUG) W !,"ndfrt: ",NDFRT
 .N SCT S SCT=$$MAP^SYNDHPMP("rxn2ndf",NDFRT,"I")
 .I $G(DEBUG) W !,"sct: ",SCT
 .S RXNORM=$S(+SCT=-1:$P(SCT,U,2),1:$P(SCT,U,2))
 .I $G(DEBUG) W !,"rxnorm: ",RXNORM
 I NAME="" S RXNORM="^missing VistA data"
 I $G(DEBUG) W !,"rxnorm: ",RXNORM,!
 Q RXNORM
 ;I NAME="" S NAME=$$GET1^DIQ(50,X_",",26,"E")
 ;
 ; ---------------- Get patient medication administration ----------------------------
PATMEDA(RETSTA,DHPICN,FRDAT,TODAT) ; Patient medication administration for ICN
 ;
 ; Return patient medication administration for a given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDAT   - from date (inclusive), optional
 ;   TODAT   - to date (inclusive), optional
 ; Output:
 ;   RETSTA  - a delimited string that lists the following information
 ;      PatientICN ^ RESOURSE ID ^ STATUS ^ MEDICATION ^ DATE ASSERTED ^ CONDITION
 ;      ^ DOSAGE , SITE ; ROUTE ; DOSE ^ QUANTITY ^ DAYS ^ RXN |
 ;
 ;   Identifier will be "V"_SITE ID_"_"_FILEMAN #_"_"_FILE IEN   i.e. V_500_55_930
 ;
 ; validate ICN
STA ;
 I $G(DHPICN)="" S RETSTA="-1^What patient?" QUIT
 I '$$UICNVAL^SYNDHPUTL(DHPICN) S RETSTA="-1^Patient identifier not recognised" Q
 ;
 S FRDAT=$S($G(FRDAT):$$HL7TFM^XLFDT(FRDAT),1:1000101)
 S TODAT=$S($G(TODAT):$$HL7TFM^XLFDT(TODAT),1:9991231)
 I $G(DEBUG) W !,"FRDAT: ",FRDAT,"   TODAT: ",TODAT,!
 I FRDAT>TODAT S RETSTA="-1^From date is greater than to date" QUIT
 ;
 N C,ACU,P,S,UL,D1,IDENT,ORDDAT,ORDNUM,ROUTE,STATUS,DOSORD,STDT,STDTFM,DRUG,SITE ;,IENS
 N RETDESC,SITEA,QTY,DAYS,RXN,IDATE,IDATEFM,PATIEN,DRUGI,RX
 S C=",",P="|",S=";",UL="_"
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 ;
 S RETDESC=""
 S SITE=$P($$SITE^VASITE,U,3)
 S RETSTA=""
 ; loop through the PHARMACY PATIENT file #55 (^PS(55)) for patient (this will contain both IP and OP orders)
 ;
 ; this is start of Inpatient Orders
 S D1=0
 F  S D1=$O(^PS(55,PATIEN,5,D1)) Q:D1'?1N.N  D
 .S IENS=D1_C_PATIEN_C
 .S (ORDDAT,ORDNUM,ROUTE,STATUS,DOSORD,STDT,STDTFM,DRUG,SITEA,QTY,DAYS,RXN)=""
 .N MEDX,MEDERR
 .D GETS^DIQ(55.06,IENS,".01;3;10;28","IEN","MEDX","MEDERR")
 .QUIT:$D(MEDERR)
 .S ORDNUM=$G(MEDX(55.06,IENS,.01,"E"))   ; order number
 .; route is most likely not stored in vista as SCT code, but should check this
 .S ROUTE=$G(MEDX(55.06,IENS,3,"E"))      ; route
 .S STATUS=$G(MEDX(55.06,IENS,28,"E"))    ; status
 .S DOSORD=$P($G(^PS(55,PATIEN,5,D1,.2)),U,2)          ; dosage ordered
 .S STDTFM=$G(MEDX(55.06,IENS,10,"I"))      ; start date
 .QUIT:((STDTFM\1)<FRDAT)!((STDTFM\1)>TODAT)  ;quit if outside of requested date range
 .S STDT=$$FMTHL7^XLFDT($G(MEDX(55.06,IENS,10,"I")))      ; start date
 .S DRUG=$$GET1^DIQ(55.07,1_C_IENS,.01,"E")  ; medication
 .S DRUGI=$$GET1^DIQ(55.07,1_C_IENS,.01,"I")
 .S RXN=$$GETRXN(DRUGI)
 .I RXN?1.N S RXN="RXN"_RXN
 .S IDENT=$$RESID^SYNDHP69("V",SITE)_UL_55_UL_PATIEN_UL_55.06_UL_D1
 .S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(STDT)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_$G(QTY)_U_$G(DAYS)_U_$G(RXN)_P
 ;
 ; start here for outpatient orders
 S D1=0
 F  S D1=$O(^PS(55,PATIEN,"P",D1)) Q:D1'?1N.N  D
 . S RX=$G(^PS(55,PATIEN,"P",D1,0))
 . S (IDENT,IDATE,DRUG,QTY,DAYS,STATUS,ROUTE)=""
 . N MEDX,MEDERR
 . D GETS^DIQ(52,RX_",",".01;1;2;6;7;8;100","IEN","MEDX","MEDERR")
 . QUIT:$D(MEDERR)
 . S IDENT=$$RESID^SYNDHP69("V",SITE)_UL_52_UL_RX
 . S IDATEFM=$G(MEDX(52,RX_",",1,"I"))     ;issue date
 . QUIT:((IDATEFM\1)<FRDAT)!((IDATEFM\1)>TODAT)  ;quit if outside of requested date range
 . S IDATE=$$FMTHL7^XLFDT($G(MEDX(52,RX_",",1,"I")))     ;issue date
 . S DRUG=$G(MEDX(52,RX_",",6,"E"))      ;medication
 . S DRUGI=$G(MEDX(52,RX_",",6,"I"))
 . S RXN=$$GETRXN(DRUGI)
 . I RXN?1.N S RXN="RXN"_RXN
 . S QTY=$G(MEDX(52,RX_",",7,"E"))       ;quantity
 . S DAYS=$G(MEDX(52,RX_",",8,"E"))      ;days supply
 . S STATUS=$G(MEDX(52,RX_",",100,"E"))  ;status
 . S ROUTE=$$GET1^DIQ(52.0113,"1,"_RX_",",6,"EN")  ;route
 . S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(IDATE)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_$G(QTY)_U_$G(DAYS)_U_$G(RXN)_P
 S RETSTA=DHPICN_U_RETDESC
 Q
 ;
 ; ---------------- Get patient medication dispense ----------------------------
 ;
PATMEDD(RETSTA,DHPICN,FRDAT,TODAT) ; Patient medication dispense for ICN
 ;
 ; Return patient medication dispense for a given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDAT   - from date (inclusive), optional
 ;   TODAT   - to date (inclusive), optional
 ; Output:
 ;   RETSTA  - a delimited string that lists the following information
 ;      PatientICN ^ RESOURSE ID ^ STATUS ^ MEDICATION ^ DATE ASSERTED ^ CONDITION
 ;      ^ DOSAGE , SITE ; ROUTE ; DOSE ^ QUANTITY ^ DAYS ^ RXN |
 ;
 ;   Identifier will be "V"_SITE ID_"_"_FILEMAN #_"_"_FILE IEN   i.e. V_500_55_930
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
 N C,ACU,P,S,UL,D1,IDENT,ORDDAT,ORDNUM,ROUTE,STATUS,DOSORD,STDT,STDTFM,DRUG,SITEA
 N RETDESC,SITE,QTY,DAYS,RXN,PATIEN,RX
 S C=",",P="|",S=";",UL="_"
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 ;
 S RETDESC=""
 S SITE=$P($$SITE^VASITE,U,3)
 S RETSTA=""
 ; loop through the PHARMACY PATIENT file #55 (^PS(55)) for patient (this will contain both IP and OP orders).
 ; OP orders will need to get additional information from the PRESCRIPTION file #52 (^PSRX)
 S D1=0
 F  S D1=$O(^PS(55,PATIEN,5,D1)) Q:D1'?1N.N  D
 .S IENS=D1_C_PATIEN_C
 .S (ORDDAT,ORDNUM,ROUTE,STATUS,DOSORD,STDT,STDTFM,DRUG,SITEA,QTY,DAYS,RXN)=""
 .N MEDX,MEDERR
 .D GETS^DIQ(55.06,IENS,".01;3;10;28","IEN","MEDX","MEDERR")
 .QUIT:$D(MEDERR)
 .S ORDNUM=$G(MEDX(55.06,IENS,.01,"E"))   ; order number
 .; route is most likely not stored in vista as SCT code, but should check this
 .S ROUTE=$G(MEDX(55.06,IENS,3,"E"))      ; route
 .S STATUS=$G(MEDX(55.06,IENS,28,"E"))    ; status
 .S DOSORD=$P($G(^PS(55,PATIEN,5,D1,.2)),U,2)          ; dosage ordered
 .S STDTFM=$G(MEDX(55.06,IENS,10,"I"))      ; start date
 .QUIT:((STDTFM\1)<FRDAT)!((STDTFM\1)>TODAT)  ;quit if outside of requested date range
 .S STDT=$$FMTHL7^XLFDT($G(MEDX(55.06,IENS,10,"I")))      ; start date
 .S DRUG=$$GET1^DIQ(55.07,1_C_IENS,.01,"E")  ; medication
 .S DRUGI=$$GET1^DIQ(55.07,1_C_IENS,.01,"I")
 .S RXN=$$GETRXN(DRUGI)
 .I RXN?1.N S RXN="RXN"_RXN
 .S IDENT=$$RESID^SYNDHP69("V",SITE)_UL_55_UL_PATIEN_UL_55.06_UL_D1
 .S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(STDT)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_$G(QTY)_U_$G(DAYS)_U_$G(RXN)_P
 ;
 S RETSTA=DHPICN_U_RETDESC
 Q
 ;
TESTS ;
 Q
T1 ;
 N ICN S ICN=""
 F  S ICN=$O(^DPT("AFICN",ICN)) Q:ICN=""  D
 .W !!!,ICN,!!!
 .D PATMEDS(.RETSTA,ICN)
 .ZWRITE RETSTA
 .W !!!
 Q
T2 ;
 N ICN S ICN=""
 F  S ICN=$O(^DPT("AFICN",ICN)) Q:ICN=""  D
 .W !,ICN,!
 .D PATMEDD(.RETSTA,ICN)
 .ZWRITE RETSTA
 Q
T3 ;
 N ICN,XPIEN
 S ICN=""
 F  S ICN=$O(^DPT("AFICN",ICN)) Q:ICN=""  D
 .S XPIEN=$O(^DPT("AFICN",ICN,""))
 .W !,ICN,!,XPIEN,!,^DPT(XPIEN,0),!
 .D PATMEDD(.RETSTA,ICN)
 .ZWRITE RETSTA
 Q
 ;Statement
 ;IP S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(STDT)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_QTY_U_DAYS_U_RXN_P
 ;OP S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(IDATE)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_QTY_U_DAYS_U_RXN_P
 ;Administer
 ;IP S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(STDT)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_QTY_U_DAYS_U_RXN_P
 ;OP S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(IDATE)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_QTY_U_DAYS_U_RXN_P
 ;Dispense
 ;IP S RETDESC=RETDESC_$G(IDENT)_U_$G(STATUS)_U_$G(DRUG)_U_$G(STDT)_U_"REASON"_U_$G(SITEA)_S_$G(ROUTE)_S_$G(DOSORD)_U_QTY_U_DAYS_U_$G(RXN)_P
