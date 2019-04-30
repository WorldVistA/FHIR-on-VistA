SYNDHP15 ; HC/art - HealthConcourse - get care plan data ;03/01/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
 ;-------------------------------------------------------------
 ;
GET1HLF(HLFACT,HLFIEN,CP,RETJSON,HLFACTJ) ;get one Health Factor
 ;inputs: HLFIEN - Health Factor IEN
 ;        CP     - 0=get all records
 ;                 1= + SYN care plan data elements
 ;        RETJSON - J or F = Return JSON
 ;output: HLFACT  - array of Health Factor data, by reference
 ;        HLFACTJ - JSON structure of Health Factor data, by reference
 ;
 I $G(DEBUG) W !,"----------------------------- Health Factor -----------------------------",!
 N FNBR1 S FNBR1=9000010.23 ;V HEALTH FACTORS
 N S S S="_"
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 N IENS S IENS=HLFIEN_","
 N HLFARR,HLFERR
 D GETS^DIQ(FNBR1,IENS,"**","EI","HLFARR","HLFERR")
 ;I $G(DEBUG) W ! ZWRITE HLFARR
 ;I $G(DEBUG),$D(HLFERR) W ">>ERROR<<",! ZWRITE HLFERR
 I $D(HLFERR) D  QUIT
 . S HLFACT("HealthFactor","ERROR")=HLFIEN
 . D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.HLFACT,.HLFACTJ)
 N HLFACTOR S HLFACTOR=$NA(HLFACT("HealthFactor"))
 S @HLFACTOR@("hlfIen")=HLFIEN
 S @HLFACTOR@("resourceType")="HealthFactor"
 S @HLFACTOR@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_HLFIEN
 S @HLFACTOR@("visitId")=$G(HLFARR(FNBR1,IENS,.03,"I"))
 S @HLFACTOR@("visit")=$G(HLFARR(FNBR1,IENS,.03,"E"))
 S @HLFACTOR@("visitFM")=$$GET1^DIQ(9000010,@HLFACTOR@("visitId")_",",.01,"I")
 S @HLFACTOR@("visitHL7")=$$FMTHL7^XLFDT(@HLFACTOR@("visitFM"))
 S @HLFACTOR@("visitFHIR")=$$FMTFHIR^SYNDHPUTL(@HLFACTOR@("visitFM"))
 S @HLFACTOR@("hlfNameId")=$G(HLFARR(FNBR1,IENS,.01,"I"))
 S @HLFACTOR@("hlfName")=$G(HLFARR(FNBR1,IENS,.01,"E"))
 QUIT:$G(CP)&($E(@HLFACTOR@("hlfName"),1,4)'="SYN ")
 S @HLFACTOR@("hlfCategory")=$$GET1^DIQ(9999999.64,@HLFACTOR@("hlfNameId")_",",.03,"E")
 S @HLFACTOR@("hlfCategoryId")=$$GET1^DIQ(9999999.64,@HLFACTOR@("hlfNameId")_",",.03,"I")
 S @HLFACTOR@("patientNameId")=$G(HLFARR(FNBR1,IENS,.02,"I"))
 S @HLFACTOR@("patientName")=$G(HLFARR(FNBR1,IENS,.02,"E"))
 S @HLFACTOR@("patientICN")=$$GET1^DIQ(2,@HLFACTOR@("patientNameId")_",",991.1)
 S @HLFACTOR@("levelSeverityCd")=$G(HLFARR(FNBR1,IENS,.04,"I"))
 S @HLFACTOR@("levelSeverity")=$G(HLFARR(FNBR1,IENS,.04,"E"))
 S @HLFACTOR@("magnitude")=$G(HLFARR(FNBR1,IENS,220,"E"))
 S @HLFACTOR@("ucumCode")=$G(HLFARR(FNBR1,IENS,221,"I"))
 S @HLFACTOR@("ucumCode")=$G(HLFARR(FNBR1,IENS,221,"E"))
 S @HLFACTOR@("eventDateTime")=$G(HLFARR(FNBR1,IENS,1201,"E"))
 S @HLFACTOR@("eventDateTimeFM")=$G(HLFARR(FNBR1,IENS,1201,"I"))
 S @HLFACTOR@("eventDateTimeHL7")=$$FMTHL7^XLFDT($G(HLFARR(FNBR1,IENS,1201,"I")))
 S @HLFACTOR@("eventDateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(HLFARR(FNBR1,IENS,1201,"I")))
 S @HLFACTOR@("orderingProviderId")=$G(HLFARR(FNBR1,IENS,1202,"I"))
 S @HLFACTOR@("orderingProvider")=$G(HLFARR(FNBR1,IENS,1202,"E"))
 S @HLFACTOR@("encounterProviderId")=$G(HLFARR(FNBR1,IENS,1204,"I"))
 S @HLFACTOR@("encounterProvider")=$G(HLFARR(FNBR1,IENS,1204,"E"))
 S @HLFACTOR@("editedCd")=$G(HLFARR(FNBR1,IENS,80101,"I"))
 S @HLFACTOR@("edited")=$G(HLFARR(FNBR1,IENS,80101,"E"))
 S @HLFACTOR@("auditTrail")=$G(HLFARR(FNBR1,IENS,80102,"E"))
 S @HLFACTOR@("comments")=$G(HLFARR(FNBR1,IENS,81101,"E"))
 S @HLFACTOR@("verifiedCd")=$G(HLFARR(FNBR1,IENS,81201,"I"))
 S @HLFACTOR@("verified")=$G(HLFARR(FNBR1,IENS,81201,"E"))
 S @HLFACTOR@("packageId")=$G(HLFARR(FNBR1,IENS,81202,"I"))
 S @HLFACTOR@("package")=$G(HLFARR(FNBR1,IENS,81202,"E"))
 S @HLFACTOR@("dataSourceId")=$G(HLFARR(FNBR1,IENS,81203,"I"))
 S @HLFACTOR@("dataSource")=$G(HLFARR(FNBR1,IENS,81203,"E"))
 ;
 ;care plan record
 I $E(@HLFACTOR@("hlfName"),1,7)="SYN CP " D
 . S @HLFACTOR@("cpStart")=+($$STRIP^XLFSTR($P(@HLFACTOR@("comments"),":",2)," "))
 . I @HLFACTOR@("cpStart")=0 S @HLFACTOR@("cpStart")=""
 . S @HLFACTOR@("cpStartHL7")=$$FMTHL7^XLFDT(@HLFACTOR@("cpStart"))
 . S @HLFACTOR@("cpStartFHIR")=$$FMTFHIR^SYNDHPUTL(@HLFACTOR@("cpStart"))
 . S @HLFACTOR@("cpEnd")=+($$STRIP^XLFSTR($P(@HLFACTOR@("comments"),":",3)," "))
 . I @HLFACTOR@("cpEnd")=0 S @HLFACTOR@("cpEnd")=""
 . S @HLFACTOR@("cpEndHL7")=$$FMTHL7^XLFDT(@HLFACTOR@("cpEnd"))
 . S @HLFACTOR@("cpEndFHIR")=$$FMTFHIR^SYNDHPUTL(@HLFACTOR@("cpEnd"))
 . S @HLFACTOR@("cpState")=$$STRIP^XLFSTR($P(@HLFACTOR@("comments"),":",4)," ")
 . S @HLFACTOR@("cpIntent")="plan"
 ;
 I $G(CP) D
 . S @HLFACTOR@("cpType")=$$GETTYPE(@HLFACTOR@("hlfName"))
 . S @HLFACTOR@("hlfNameSCT")=$$GETSCT(@HLFACTOR@("hlfName"))
 . S @HLFACTOR@("cpState")=$$STRIP^XLFSTR($P(@HLFACTOR@("comments"),":",4)," ")
 . S @HLFACTOR@("cpResourceType")="CarePlan"
 ;
 I '$G(CP) D
 . ;get snomed
 . N SCT S SCT=""
 . I @HLFACTOR@("hlfNameId")'="" D
 . . S SCT=$$MAP^SYNDHPMP("sct2hf",@HLFACTOR@("hlfNameId"),"I")
 . . S @HLFACTOR@("hlfNameSCT")=$S(+SCT=-1:"",1:$P(SCT,U,2))
 ;
 ;I $G(DEBUG) W ! ZWRITE HLFACT
 ;
 D:$G(RETJSON)="J"!($G(RETJSON)="F") TOJASON^SYNDHPUTL(.HLFACT,.HLFACTJ)
 ;
 QUIT
 ;
GETTYPE(HFNAME) ;
 ;
 QUIT $S($E(HFNAME,1,7)="SYN CP ":"careplan",$E(HFNAME,1,8)="SYN ADDR":"addresses",$E(HFNAME,1,8)="SYN ACT ":"activity",$E(HFNAME,1,9)="SYN GOAL ":"goal",1:"")
 ;
GETSCT(HFNAME) ;
 ;
 QUIT $P($P(HFNAME,"SCT:",2),")",1)
 ;
 ;-------------------------------------------------------------
 ;
GET1NCP(NCP,NCPIEN,RETJSON,NCPJ) ;get one nursing care plan
 ;inputs: HLFIEN - Health Factor IEN
 ;        RETJSON - J = Return JSON
 ;output: NCP  - array of nursing care plan, by reference
 ;        NCPJ - JSON structure of nursing care plan data, by reference
 ;
 I $G(DEBUG) W !,"----------------------------- Nursing Care Plan -----------------------------",!
 N FNBR1 S FNBR1=216.8 ;NURS CARE PLAN
 N FNBR2 S FNBR2=216.81 ;NURS CARE PLAN:NURSING PROBLEM LIST
 N FNBR3 S FNBR3=216.82 ;NURS CARE PLAN:EVALUATION DATE
 N FNBR4 S FNBR4=216.83 ;NURS CARE PLAN:TARGET DATE
 N FNBR5 S FNBR5=216.84 ;NURS CARE PLAN:ORDER INFO
 N S S S="_"
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 N IENS S IENS=NCPIEN_","
 N NCPARR,NCPERR
 D GETS^DIQ(FNBR1,IENS,"**","EI","NCPARR","NCPERR")
 ;I $G(DEBUG) W ! ZWRITE NCPARR
 ;I $G(DEBUG),$D(NCPERR) W ">>ERROR<<",! ZWRITE NCPERR
 I $D(NCPERR) D  QUIT
 . S NCP("NurseCarePlan","ERROR")=NCPIEN
 . D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.NCP,.NCPJ)
 N CAREPLAN S CAREPLAN=$NA(NCP("NurseCarePlan"))
 S @CAREPLAN@("ncpIen")=NCPIEN
 S @CAREPLAN@("resourceType")="NurseCarePlan"
 S @CAREPLAN@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_NCPIEN
 S @CAREPLAN@("textFileEntryId")=$G(NCPARR(FNBR1,IENS,.01,"I"))
 S @CAREPLAN@("textFileEntry")=$G(NCPARR(FNBR1,IENS,.01,"E"))
 ;
 N PROB S PROB=""
 F  S PROB=$O(NCPARR(FNBR2,PROB)) QUIT:PROB=""  D
 . N FIEN S FIEN=+PROB
 . N PROBLIST S PROBLIST=$NA(NCP("NurseCarePlan","problemLists","problemList",FIEN))
 . S @PROBLIST@("problemId")=$G(NCPARR(FNBR2,PROB,.01,"I"))
 . S @PROBLIST@("problem")=$G(NCPARR(FNBR2,PROB,.01,"E"))
 . S @PROBLIST@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_NCPIEN_S_FNBR2_S_FIEN
 ;
 N EVAL S EVAL=""
 F  S EVAL=$O(NCPARR(FNBR3,EVAL)) QUIT:EVAL=""  D
 . N FIEN S FIEN=+EVAL
 . N EVALDATE S EVALDATE=$NA(NCP("NurseCarePlan","evaluationDates","evaluationDate",FIEN))
 . S @EVALDATE@("dateTimeEntered")=$G(NCPARR(FNBR3,EVAL,.01,"E"))
 . S @EVALDATE@("dateTimeEnteredFM")=$G(NCPARR(FNBR3,EVAL,.01,"I"))
 . S @EVALDATE@("dateTimeEnteredHL7")=$$FMTHL7^XLFDT($G(NCPARR(FNBR3,EVAL,.01,"I")))
 . S @EVALDATE@("dateTimeEnteredFHIR")=$$FMTFHIR^SYNDHPUTL($G(NCPARR(FNBR3,EVAL,.01,"I")))
 . S @EVALDATE@("problemId")=$G(NCPARR(FNBR3,EVAL,.02,"I"))
 . S @EVALDATE@("problem")=$G(NCPARR(FNBR3,EVAL,.02,"E"))
 . S @EVALDATE@("userWhoEvaluatedId")=$G(NCPARR(FNBR3,EVAL,1,"I"))
 . S @EVALDATE@("userWhoEvaluated")=$G(NCPARR(FNBR3,EVAL,1,"E"))
 . S @EVALDATE@("problemStatusCd")=$G(NCPARR(FNBR3,EVAL,2,"I"))
 . S @EVALDATE@("problemStatus")=$G(NCPARR(FNBR3,EVAL,2,"E"))
 . S @EVALDATE@("evaluationDate")=$G(NCPARR(FNBR3,EVAL,3,"E"))
 . S @EVALDATE@("evaluationDateFM")=$G(NCPARR(FNBR3,EVAL,3,"I"))
 . S @EVALDATE@("evaluationDateHL7")=$$FMTHL7^XLFDT($G(NCPARR(FNBR3,EVAL,3,"I")))
 . S @EVALDATE@("evaluationDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(NCPARR(FNBR3,EVAL,3,"I")))
 . S @EVALDATE@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_NCPIEN_S_FNBR3_S_FIEN
 ;
 N TARG S TARG=""
 F  S TARG=$O(NCPARR(FNBR4,TARG)) QUIT:TARG=""  D
 . N FIEN S FIEN=+TARG
 . N TARGDATE S TARGDATE=$NA(NCP("NurseCarePlan","targetDates","targetDate",FIEN))
 . S @TARGDATE@("dateTimeEntered")=$G(NCPARR(FNBR4,TARG,.01,"E"))
 . S @TARGDATE@("dateTimeEnteredFM")=$G(NCPARR(FNBR4,TARG,.01,"I"))
 . S @TARGDATE@("dateTimeEnteredHL7")=$$FMTHL7^XLFDT($G(NCPARR(FNBR4,TARG,.01,"I")))
 . S @TARGDATE@("dateTimeEnteredFHIR")=$$FMTFHIR^SYNDHPUTL($G(NCPARR(FNBR4,TARG,.01,"I")))
 . S @TARGDATE@("goalExpectedOutcomeId")=$G(NCPARR(FNBR4,TARG,.03,"I"))
 . S @TARGDATE@("goalExpectedOutcome")=$G(NCPARR(FNBR4,TARG,.03,"E"))
 . S @TARGDATE@("userWhoEnteredId")=$G(NCPARR(FNBR4,TARG,1,"I"))
 . S @TARGDATE@("userWhoEntered")=$G(NCPARR(FNBR4,TARG,1,"E"))
 . S @TARGDATE@("goalMetDcdCd")=$G(NCPARR(FNBR4,TARG,2,"I"))
 . S @TARGDATE@("goalMetDcd")=$G(NCPARR(FNBR4,TARG,2,"E"))
 . S @TARGDATE@("targetDate")=$G(NCPARR(FNBR4,TARG,3,"E"))
 . S @TARGDATE@("targetDateFM")=$G(NCPARR(FNBR4,TARG,3,"I"))
 . S @TARGDATE@("targetDateHL7")=$$FMTHL7^XLFDT($G(NCPARR(FNBR4,TARG,3,"I")))
 . S @TARGDATE@("targetDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(NCPARR(FNBR4,TARG,3,"I")))
 . S @TARGDATE@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_NCPIEN_S_FNBR4_S_FIEN
 ;
 N ORD S ORD=""
 F  S ORD=$O(NCPARR(FNBR5,ORD)) QUIT:ORD=""  D
 . N FIEN S FIEN=+ORD
 . N ORDINFO S ORDINFO=$NA(NCP("NurseCarePlan","ordersInfo","orderInfo",FIEN))
 . S @ORDINFO@("statusCd")=$G(NCPARR(FNBR5,ORD,1,"I"))
 . S @ORDINFO@("status")=$G(NCPARR(FNBR5,ORD,1,"E"))
 . S @ORDINFO@("userModifyingId")=$G(NCPARR(FNBR5,ORD,2,"I"))
 . S @ORDINFO@("userModifying")=$G(NCPARR(FNBR5,ORD,2,"E"))
 . S @ORDINFO@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_NCPIEN_S_FNBR5_S_FIEN
 ;
 ;I $G(DEBUG) W ! ZWRITE NCP
 ;
 D:$G(RETJSON)="J"!($G(RETJSON)="F") TOJASON^SYNDHPUTL(.NCP,.NCPJ)
 ;
 QUIT
 ;
 ;-------------------------------------------------------------
 ;
GET1GMR(GMRTEXT,GMRIEN,RETJSON,GMRTEXTJ) ;get one GMR Text record
 ;inputs: GMRIEN - GMR Text IEN
 ;        RETJSON - J = Return JSON
 ;output: GMRTEXT  - array of GMR Text data, by reference
 ;        GMRTEXTJ - JSON structure of GMR Text data, by reference
 ;
 I $G(DEBUG) W !,"----------------------------- GMR Text -----------------------------",!
 N FNBR1 S FNBR1=124.3 ;GMR TEXT
 N FNBR2 S FNBR2=124.31 ;GMR TEXT:SELECTION
 N FNBR3 S FNBR3=124.313 ;GMR TEXT:SELECTION:AUDIT TRAIL
 N S S S="_"
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 N IENS S IENS=GMRIEN_","
 N GMRARR,GMRERR
 D GETS^DIQ(FNBR1,IENS,"**","EI","GMRARR","GMRERR")
 ;I $G(DEBUG) W ! ZWRITE GMRARR
 ;I $G(DEBUG),$D(GMRERR) W ">>ERROR<<",! ZWRITE GMRERR
 I $D(GMRERR) D  QUIT
 . S GMRTEXT("GMRtext","ERROR")=GMRIEN
 . D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.GMRTEXT,.GMRTEXTJ)
 N GMRTXT S GMRTXT=$NA(GMRTEXT("GMRtext"))
 S @GMRTXT@("GMRIen")=GMRIEN
 S @GMRTXT@("resourceType")="GMRtext"
 S @GMRTXT@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_GMRIEN
 S @GMRTXT@("textBlockId")=$G(GMRARR(FNBR1,IENS,.01,"I"))
 S @GMRTXT@("textBlock")=$G(GMRARR(FNBR1,IENS,.01,"E"))
 S @GMRTXT@("patientId")=$G(GMRARR(FNBR1,IENS,.02,"I"))
 S @GMRTXT@("patient")=$G(GMRARR(FNBR1,IENS,.02,"E"))
 S @GMRTXT@("patientICN")=$$GET1^DIQ(2,@GMRTXT@("patientId")_",",991.1)
 S @GMRTXT@("dateCreated")=$G(GMRARR(FNBR1,IENS,.03,"E"))
 S @GMRTXT@("dateCreatedFM")=$G(GMRARR(FNBR1,IENS,.03,"I"))
 S @GMRTXT@("dateCreatedHL7")=$$FMTHL7^XLFDT($G(GMRARR(FNBR1,IENS,.03,"I")))
 S @GMRTXT@("dateCreatedFHIR")=$$FMTFHIR^SYNDHPUTL($G(GMRARR(FNBR1,IENS,.03,"I")))
 S @GMRTXT@("authorId")=$G(GMRARR(FNBR1,IENS,3,"I"))
 S @GMRTXT@("author")=$G(GMRARR(FNBR1,IENS,3,"E"))
 S @GMRTXT@("enteredInErrorCd")=$G(GMRARR(FNBR1,IENS,5,"I"))
 S @GMRTXT@("enteredInError")=$G(GMRARR(FNBR1,IENS,5,"E"))
 S @GMRTXT@("dateEnteredInError")=$G(GMRARR(FNBR1,IENS,5.1,"E"))
 S @GMRTXT@("dateEnteredInErrorFM")=$G(GMRARR(FNBR1,IENS,5.1,"I"))
 S @GMRTXT@("dateEnteredInErrorHL7")=$$FMTHL7^XLFDT($G(GMRARR(FNBR1,IENS,5.1,"I")))
 S @GMRTXT@("dateEnteredInErrorFHIR")=$$FMTFHIR^SYNDHPUTL($G(GMRARR(FNBR1,IENS,5.1,"I")))
 S @GMRTXT@("userEnteringInErrorId")=$G(GMRARR(FNBR1,IENS,5.2,"I"))
 S @GMRTXT@("userEnteringInError")=$G(GMRARR(FNBR1,IENS,5.2,"E"))
 S @GMRTXT@("dateLastUpdated")=$G(GMRARR(FNBR1,IENS,6,"E"))
 S @GMRTXT@("dateLastUpdatedFM")=$G(GMRARR(FNBR1,IENS,6,"I"))
 S @GMRTXT@("dateLastUpdatedHL7")=$$FMTHL7^XLFDT($G(GMRARR(FNBR1,IENS,6,"I")))
 S @GMRTXT@("dateLastUpdatedFHIR")=$$FMTFHIR^SYNDHPUTL($G(GMRARR(FNBR1,IENS,6,"I")))
 ;
 N SEL S SEL=""
 F  S SEL=$O(GMRARR(FNBR2,SEL)) QUIT:SEL=""  D
 . N FIEN S FIEN=+SEL
 . N SELECTION S SELECTION=$NA(GMRTEXT("GMRtext","selections","selection",FIEN))
 . S @SELECTION@("selectionId")=$G(GMRARR(FNBR2,SEL,.01,"I"))
 . S @SELECTION@("selection")=$G(GMRARR(FNBR2,SEL,.01,"E"))
 . S @SELECTION@("appendedInternalText")=$G(GMRARR(FNBR2,SEL,1,"E"))
 . S @SELECTION@("additionalText")=$G(GMRARR(FNBR2,SEL,2,"E"))
 . S @SELECTION@("added")=$G(GMRARR(FNBR2,SEL,4,"E"))
 . S @SELECTION@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_GMRIEN_S_FNBR2_S_FIEN
 . N AUDIT S AUDIT=""
 . F  S AUDIT=$O(GMRARR(FNBR3,AUDIT)) QUIT:AUDIT=""  D
 . . N AIEN S AIEN=+AUDIT
 . . N AUDIT S AUDIT=$NA(GMRTEXT("GMRtext","selections","selection",FIEN,"auditTrails","auditTrail",AIEN))
 . . S @AUDIT@("auditTrailDateTime")=$G(GMRARR(FNBR3,AUDIT,.01,"E"))
 . . S @AUDIT@("auditTrailDateTimeFM")=$G(GMRARR(FNBR3,AUDIT,.01,"I"))
 . . S @AUDIT@("auditTrailDateTimeHL7")=$$FMTHL7^XLFDT($G(GMRARR(FNBR3,AUDIT,.01,"I")))
 . . S @AUDIT@("auditTrailDateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(GMRARR(FNBR3,AUDIT,.01,"I")))
 . . S @AUDIT@("modification")=$G(GMRARR(FNBR3,AUDIT,1,"E"))
 . . S @AUDIT@("userWhoModifiedId")=$G(GMRARR(FNBR3,AUDIT,2,"I"))
 . . S @AUDIT@("userWhoModified")=$G(GMRARR(FNBR3,AUDIT,2,"E"))
 . . S @AUDIT@("appendedInternalText")=$G(GMRARR(FNBR3,AUDIT,3,"E"))
 . . S @AUDIT@("additionalText")=$G(GMRARR(FNBR3,AUDIT,4,"E"))
 . . S @AUDIT@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_GMRIEN_S_FNBR2_S_FIEN_S_FNBR3_S_AIEN
 ;
 ;I $G(DEBUG) W ! ZWRITE GMRTEXT
 ;
 D:$G(RETJSON)="J"!($G(RETJSON)="F") TOJASON^SYNDHPUTL(.GMRTEXT,.GMRTEXTJ)
 ;
 QUIT
 ;
