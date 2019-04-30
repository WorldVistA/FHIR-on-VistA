SYNDHP60 ; DHP/ART - HealthConcourse - get Allergy, MH Diagnosis, Vitals, Flags data ;02/13/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;
 ;
 ;>>>>>>>>>>>>>>>>>>> THIS CODE HAS BEEN MOVED TO SYNDHP16 & SYNDHP17 <<<<<<<<<<<<<<<<<<<<<<<<
 ;                    THIS ROUTINE WILL BE DELETED 
 QUIT
 ;
 ;-------------------------------------------------------------
 ;
GET1ALLERGY(ALLERGY,ALLERGYIEN,RETJSON,ALLERGYJ) ;get one Allergy record
 QUIT
 ;inputs: ALLERGYIEN - Allergy IEN
 ;        RETJSON - J = Return JSON
 ;output: ALLERGY  - array of Allergy data, by reference
 ;        ALLERGYJ - JSON structure of Allergy data, by reference
 ;
 I $G(DEBUG) W !,"--------------------------- Allergy -----------------------------",!
 N S S S="_"
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 N FNBR1 S FNBR1=120.8 ;PATIENT ALLERGIES
 N FNBR2 S FNBR2=120.81 ;REACTIONS
 N FNBR3 S FNBR3=120.813 ;CHART MARKED
 N FNBR4 S FNBR4=120.814 ;ID BAND MARKED
 N FNBR5 S FNBR5=120.826 ;COMMENTS
 N IENS1 S IENS1=ALLERGYIEN_","
 ;
 N ALLERGYARR,ALLERGYERR,ALLERGYN
 D GETS^DIQ(FNBR1,IENS1,"**","EI","ALLERGYARR","ALLERGYERR")
 ;I $G(DEBUG) W ! ZWRITE ALLERGYARR
 ;I $G(DEBUG),$D(ALLERGYERR) W ">>ERROR<<",! ZWRITE ALLERGYERR
 I $D(ALLERGYERR) S ALLERGY("Allergy","ERROR")=ALLERGYIEN QUIT
 S ALLERGYN=$NA(ALLERGY("Allergy"))
 S @ALLERGYN@("allergyIen")=ALLERGYIEN
 S @ALLERGYN@("resourceType")="AllergyIntolerance"
 S @ALLERGYN@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_ALLERGYIEN
 S @ALLERGYN@("patient")=$G(ALLERGYARR(FNBR1,IENS1,.01,"E"))
 S @ALLERGYN@("patientId")=$G(ALLERGYARR(FNBR1,IENS1,.01,"I"))
 S @ALLERGYN@("patientICN")=$$GET1^DIQ(2,@ALLERGYN@("patientId")_",",991.1)
 S @ALLERGYN@("reactant")=$G(ALLERGYARR(FNBR1,IENS1,.02,"E"))
 S @ALLERGYN@("reactantSCT")=$$MAPR^SYNDHPMP(@ALLERGYN@("reactant"),"T","A")
 S @ALLERGYN@("gmrAllergy")=$G(ALLERGYARR(FNBR1,IENS1,1,"E"))
 S @ALLERGYN@("gmrAllergyId")=$G(ALLERGYARR(FNBR1,IENS1,1,"I"))
 S @ALLERGYN@("allergyType")=$G(ALLERGYARR(FNBR1,IENS1,3.1,"E"))
 S @ALLERGYN@("allergyTypeFHIR")=$$CNVTTYPE(@ALLERGYN@("allergyType"))
 S @ALLERGYN@("originationDateTime")=$G(ALLERGYARR(FNBR1,IENS1,4,"E"))
 S @ALLERGYN@("originationDateTimeFM")=$G(ALLERGYARR(FNBR1,IENS1,4,"I"))
 S @ALLERGYN@("originationDateTimeHL7")=$$FMTHL7^XLFDT($G(ALLERGYARR(FNBR1,IENS1,4,"I")))
 S @ALLERGYN@("originationDateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(ALLERGYARR(FNBR1,IENS1,4,"I")))
 S @ALLERGYN@("originator")=$G(ALLERGYARR(FNBR1,IENS1,5,"E"))
 S @ALLERGYN@("originatorId")=$G(ALLERGYARR(FNBR1,IENS1,5,"I"))
 S @ALLERGYN@("observedHistorical")=$G(ALLERGYARR(FNBR1,IENS1,6,"E"))
 S @ALLERGYN@("observedHistoricalCd")=$G(ALLERGYARR(FNBR1,IENS1,6,"I"))
 S @ALLERGYN@("reportable")=$G(ALLERGYARR(FNBR1,IENS1,7,"E"))
 S @ALLERGYN@("reportableCd")=$G(ALLERGYARR(FNBR1,IENS1,7,"I"))
 S @ALLERGYN@("originatorSignOff")=$G(ALLERGYARR(FNBR1,IENS1,15,"E"))
 S @ALLERGYN@("originatorSignOffCd")=$G(ALLERGYARR(FNBR1,IENS1,15,"I"))
 S @ALLERGYN@("mechanism")=$G(ALLERGYARR(FNBR1,IENS1,17,"E"))
 S @ALLERGYN@("mechanismCd")=$G(ALLERGYARR(FNBR1,IENS1,17,"I"))
 S @ALLERGYN@("verified")=$G(ALLERGYARR(FNBR1,IENS1,19,"E"))
 S @ALLERGYN@("verifiedCd")=$G(ALLERGYARR(FNBR1,IENS1,19,"I"))
 S @ALLERGYN@("verificationDateTime")=$G(ALLERGYARR(FNBR1,IENS1,20,"E"))
 S @ALLERGYN@("verificationDateTimeFM")=$G(ALLERGYARR(FNBR1,IENS1,20,"I"))
 S @ALLERGYN@("verificationDateTimeHL7")=$$FMTHL7^XLFDT($G(ALLERGYARR(FNBR1,IENS1,20,"I")))
 S @ALLERGYN@("verificationDateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(ALLERGYARR(FNBR1,IENS1,20,"I")))
 S @ALLERGYN@("verifier")=$G(ALLERGYARR(FNBR1,IENS1,21,"E"))
 S @ALLERGYN@("verifierId")=$G(ALLERGYARR(FNBR1,IENS1,21,"I"))
 S @ALLERGYN@("enteredInError")=$G(ALLERGYARR(FNBR1,IENS1,22,"E"))
 S @ALLERGYN@("enteredInErrorCd")=$G(ALLERGYARR(FNBR1,IENS1,22,"I"))
 S @ALLERGYN@("dateTimeEnteredInError")=$G(ALLERGYARR(FNBR1,IENS1,23,"E"))
 S @ALLERGYN@("dateTimeEnteredInErrorFM")=$G(ALLERGYARR(FNBR1,IENS1,23,"I"))
 S @ALLERGYN@("dateTimeEnteredInErrorHL7")=$$FMTHL7^XLFDT($G(ALLERGYARR(FNBR1,IENS1,23,"I")))
 S @ALLERGYN@("dateTimeEnteredInErrorFHIR")=$$FMTFHIR^SYNDHPUTL($G(ALLERGYARR(FNBR1,IENS1,23,"I")))
 S @ALLERGYN@("userEnteringInError")=$G(ALLERGYARR(FNBR1,IENS1,24,"E"))
 S @ALLERGYN@("userEnteringInErrorId")=$G(ALLERGYARR(FNBR1,IENS1,24,"I"))
 N REACTION
 N IENS2 S IENS2=""
 F  S IENS2=$O(ALLERGYARR(FNBR2,IENS2)) QUIT:IENS2=""  D
 . S REACTION=$NA(ALLERGY("Allergy","reactionss","reactions",+IENS2))
 . S @REACTION@("reaction")=$G(ALLERGYARR(FNBR2,IENS2,.01,"E"))
 . S @REACTION@("reactionId")=$G(ALLERGYARR(FNBR2,IENS2,.01,"I"))
 . S @REACTION@("reactionSCT")=$$MAPR^SYNDHPMP($G(ALLERGYARR(FNBR2,IENS2,.01,"E")),"T","R")
 . S @REACTION@("otherReaction")=$G(ALLERGYARR(FNBR2,IENS2,1,"E"))
 . S @REACTION@("enteredBy")=$G(ALLERGYARR(FNBR2,IENS2,2,"E"))
 . S @REACTION@("enteredById")=$G(ALLERGYARR(FNBR2,IENS2,2,"I"))
 . S @REACTION@("dateEntered")=$G(ALLERGYARR(FNBR2,IENS2,3,"E"))
 . S @REACTION@("dateEnteredFM")=$G(ALLERGYARR(FNBR2,IENS2,3,"I"))
 . S @REACTION@("dateEnteredHL7")=$$FMTHL7^XLFDT($G(ALLERGYARR(FNBR2,IENS2,3,"I")))
 . S @REACTION@("dateEnteredFHIR")=$$FMTFHIR^SYNDHPUTL($G(ALLERGYARR(FNBR2,IENS2,3,"I")))
 . S @REACTION@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_ALLERGYIEN_S_FNBR2_S_+IENS2
 N CHART
 N IENS3 S IENS3=""
 F  S IENS3=$O(ALLERGYARR(FNBR3,IENS3)) QUIT:IENS3=""  D
 . S CHART=$NA(ALLERGY("Allergy","chartMarkeds","chartMarked",+IENS3))
 . S @CHART@("dateTime")=$G(ALLERGYARR(FNBR3,IENS3,.01,"E"))
 . S @CHART@("dateTimeFM")=$G(ALLERGYARR(FNBR3,IENS3,.01,"I"))
 . S @CHART@("dateTimeHL7")=$$FMTHL7^XLFDT($G(ALLERGYARR(FNBR3,IENS3,.01,"I")))
 . S @CHART@("dateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(ALLERGYARR(FNBR3,IENS3,.01,"I")))
 . S @CHART@("userEntering")=$G(ALLERGYARR(FNBR3,IENS3,1,"E"))
 . S @CHART@("userEnteringId")=$G(ALLERGYARR(FNBR3,IENS3,1,"I"))
 . S @CHART@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_ALLERGYIEN_S_FNBR3_S_+IENS3
 N IDBAND
 N IENS4 S IENS4=""
 F  S IENS4=$O(ALLERGYARR(FNBR4,IENS4)) QUIT:IENS4=""  D
 . S IDBAND=$NA(ALLERGY("Allergy","idBandMarkeds","idBandMarked",+IENS4))
 . S @IDBAND@("dateTime")=$G(ALLERGYARR(FNBR4,IENS4,.01,"E"))
 . S @IDBAND@("dateTimeFM")=$G(ALLERGYARR(FNBR4,IENS4,.01,"I"))
 . S @IDBAND@("dateTimeHL7")=$$FMTHL7^XLFDT($G(ALLERGYARR(FNBR4,IENS4,.01,"I")))
 . S @IDBAND@("dateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(ALLERGYARR(FNBR4,IENS4,.01,"I")))
 . S @IDBAND@("userEntering")=$G(ALLERGYARR(FNBR4,IENS4,1,"E"))
 . S @IDBAND@("userEnteringId")=$G(ALLERGYARR(FNBR4,IENS4,1,"I"))
 . S @IDBAND@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_ALLERGYIEN_S_FNBR4_S_+IENS4
 N COMMENTS
 N IENS5 S IENS5=""
 F  S IENS5=$O(ALLERGYARR(FNBR5,IENS5)) QUIT:IENS5=""  D
 . S COMMENTS=$NA(ALLERGY("Allergy","commentss","comments",+IENS5))
 . S @COMMENTS@("dateTimeCommentEntered")=$G(ALLERGYARR(FNBR5,IENS5,.01,"E"))
 . S @COMMENTS@("dateTimeCommentEnteredFM")=$G(ALLERGYARR(FNBR5,IENS5,.01,"I"))
 . S @COMMENTS@("dateTimeCommentEnteredHL7")=$$FMTHL7^XLFDT($G(ALLERGYARR(FNBR5,IENS5,.01,"I")))
 . S @COMMENTS@("dateTimeCommentEnteredFHIR")=$$FMTFHIR^SYNDHPUTL($G(ALLERGYARR(FNBR5,IENS5,.01,"I")))
 . S @COMMENTS@("userEntering")=$G(ALLERGYARR(FNBR5,IENS5,1,"E"))
 . S @COMMENTS@("userEnteringId")=$G(ALLERGYARR(FNBR5,IENS5,1,"I"))
 . S @COMMENTS@("commentType")=$G(ALLERGYARR(FNBR5,IENS5,1.5,"E"))
 . S @COMMENTS@("commentTypeCd")=$G(ALLERGYARR(FNBR5,IENS5,1.5,"I"))
 . S @COMMENTS@("comments")=$G(ALLERGYARR(FNBR5,IENS5,2,"E"))
 . S @COMMENTS@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_ALLERGYIEN_S_FNBR5_S_+IENS5
 ;
 ;I $G(DEBUG) W ! ZWRITE ALLERGY W !
 ;
 D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.ALLERGY,.ALLERGYJ)
 ;
 QUIT
 ;
CNVTTYPE(TYPE) ;convert allery type to fhir values
 ;input: TYPE - VistA allergy type
 ;returns: FHIR allery type
 ;
 N IDX,TYPES,ALLTYPES
 F IDX=1:1 S TYPES=$P($T(TYPES+IDX),";;",2) QUIT:TYPES="zzzzz"  D
 . S ALLTYPES($P(TYPES,U,1))=$P(TYPES,U,2)
 ;
 I $G(TYPE)="" QUIT ""
 I $D(ALLTYPES(TYPE)) QUIT ALLTYPES(TYPE)
 QUIT ""
 ;
TYPES ;
  ;;FOOD^food
  ;;DRUG^medication
  ;;OTHER^environment
  ;;FOOD, DRUG^food:medication
  ;;DRUG, FOOD^medication:food
  ;;FOOD, OTHER^food:environment
  ;;DRUG, OTHER^medication:environment
  ;;FOOD, DRUG, OTHER^food:medication:environment
  ;;DRUG, FOOD, OTHER^medication:food:environment
  ;;F^food
  ;;D^medication
  ;;O^environment
  ;;FD^food:medication
  ;;DF^medication:food
  ;;FO^food:environment
  ;;DO^medication:environment
  ;;FDO^food:medication:environment
  ;;DFO^medication:food:environment
  ;;zzzzz
 ;
GET1DXMH(DXMH,DXMHIEN,RETJSON,DXMHJ) ;get one MH Diagnosis record
 QUIT
 ;inputs: DXMHIEN - MH Diagnosis IEN
 ;        RETJSON - J = Return JSON
 ;output: DXMH  - array of MH Diagnosis data, by reference
 ;        DXMHJ - JSON structure of MH Diagnosis data, by reference
 ;
 I $G(DEBUG) W !,"--------------------------- MH Diagnosis -----------------------------",!
 N S S S="_"
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 N FNBR1 S FNBR1=627.8 ;DIAGNOSTIC RESULTS - MENTAL HEALTH
 N FNBR2 S FNBR2=627.82 ;MODIFIERS
 N IENS1 S IENS1=DXMHIEN_","
 ;
 N DXMHARR,DXMHERR
 D GETS^DIQ(FNBR1,IENS1,"**","EI","DXMHARR","DXMHERR")
 ;I $G(DEBUG) W ! ZWRITE DXMHARR
 ;I $G(DEBUG),$D(DXMHERR) W ">>ERROR<<" ZWRITE DXMHERR
 I $D(DXMHERR) S DXMH("Dxmh","ERROR")=DXMHIEN QUIT
 S DXMH("Dxmh","dxmhIen")=DXMHIEN
 S DXMH("Dxmh","resourceType")="DiagnosticReport"
 S DXMH("Dxmh","resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_DXMHIEN
 S DXMH("Dxmh","fileEntryDate")=$G(DXMHARR(FNBR1,IENS1,.01,"E"))
 S DXMH("Dxmh","fileEntryDateFM")=$G(DXMHARR(FNBR1,IENS1,.01,"I"))
 S DXMH("Dxmh","fileEntryDateHL7")=$$FMTHL7^XLFDT($G(DXMHARR(FNBR1,IENS1,.01,"I")))
 S DXMH("Dxmh","fileEntryDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(DXMHARR(FNBR1,IENS1,.01,"I")))
 S DXMH("Dxmh","patientName")=$G(DXMHARR(FNBR1,IENS1,.02,"E"))
 S DXMH("Dxmh","patientNameId")=$G(DXMHARR(FNBR1,IENS1,.02,"I"))
 S DXMH("Dxmh","patientICN")=$$GET1^DIQ(2,DXMH("Dxmh","patientNameId")_",",991.1)
 S DXMH("Dxmh","dateTimeOfDiagnosis")=$G(DXMHARR(FNBR1,IENS1,.03,"E"))
 S DXMH("Dxmh","dateTimeOfDiagnosisFM")=$G(DXMHARR(FNBR1,IENS1,.03,"I"))
 S DXMH("Dxmh","dateTimeOfDiagnosisHL7")=$$FMTHL7^XLFDT($G(DXMHARR(FNBR1,IENS1,.03,"I")))
 S DXMH("Dxmh","dateTimeOfDiagnosisFHIR")=$$FMTFHIR^SYNDHPUTL($G(DXMHARR(FNBR1,IENS1,.03,"I")))
 S DXMH("Dxmh","diagnosisBy")=$G(DXMHARR(FNBR1,IENS1,.04,"E"))
 S DXMH("Dxmh","diagnosisById")=$G(DXMHARR(FNBR1,IENS1,.04,"I"))
 S DXMH("Dxmh","transcriber")=$G(DXMHARR(FNBR1,IENS1,.05,"E"))
 S DXMH("Dxmh","transcriberId")=$G(DXMHARR(FNBR1,IENS1,.05,"I"))
 S DXMH("Dxmh","diagnosis")=$G(DXMHARR(FNBR1,IENS1,1,"E"))
 S DXMH("Dxmh","diagnosisId")=$G(DXMHARR(FNBR1,IENS1,1,"I"))
 S DXMH("Dxmh","diagnosisDesc")=$P($$ICDDX^ICDEX(DXMH("Dxmh","diagnosis")),U,4)
 S DXMH("Dxmh","statusVPRINRu")=$G(DXMHARR(FNBR1,IENS1,5,"E"))
 S DXMH("Dxmh","statusVPRINRuCd")=$G(DXMHARR(FNBR1,IENS1,5,"I"))
 S DXMH("Dxmh","condition")=$G(DXMHARR(FNBR1,IENS1,7,"E"))
 S DXMH("Dxmh","conditionCd")=$G(DXMHARR(FNBR1,IENS1,7,"I"))
 S DXMH("Dxmh","inactivatedDate")=$G(DXMHARR(FNBR1,IENS1,8,"E"))
 S DXMH("Dxmh","inactivatedDateFM")=$G(DXMHARR(FNBR1,IENS1,8,"I"))
 S DXMH("Dxmh","inactivatedDateHL7")=$$FMTHL7^XLFDT($G(DXMHARR(FNBR1,IENS1,8,"I")))
 S DXMH("Dxmh","inactivatedDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(DXMHARR(FNBR1,IENS1,8,"I")))
 S DXMH("Dxmh","statusChanged")=$G(DXMHARR(FNBR1,IENS1,9,"E"))
 S DXMH("Dxmh","statusChangedCd")=$G(DXMHARR(FNBR1,IENS1,9,"I"))
 S DXMH("Dxmh","dxls")=$G(DXMHARR(FNBR1,IENS1,10,"E"))
 S DXMH("Dxmh","dxlsCd")=$G(DXMHARR(FNBR1,IENS1,10,"I"))
 S DXMH("Dxmh","psychosocialStressor")=$G(DXMHARR(FNBR1,IENS1,60,"E"))
 S DXMH("Dxmh","severityCode")=$G(DXMHARR(FNBR1,IENS1,61,"E"))
 S DXMH("Dxmh","severityCodeCd")=$G(DXMHARR(FNBR1,IENS1,61,"I"))
 S DXMH("Dxmh","axis5")=$G(DXMHARR(FNBR1,IENS1,65,"E"))
 S DXMH("Dxmh","patientType")=$G(DXMHARR(FNBR1,IENS1,66,"E"))
 S DXMH("Dxmh","patientTypeCd")=$G(DXMHARR(FNBR1,IENS1,66,"I"))
 N MODIFIER
 N IENS2 S IENS2=""
 F  S IENS2=$O(DXMHARR(FNBR2,IENS2)) QUIT:IENS2=""  D
 . S MODIFIER=$NA(DXMH("Dxmh","modifierss","modifiers",+IENS2))
 . S @MODIFIER@("modifier")=$G(DXMHARR(FNBR2,IENS2,.01,"E"))
 . S @MODIFIER@("modifierId")=$G(DXMHARR(FNBR2,IENS2,.01,"I"))
 . S @MODIFIER@("numberOfAnswer")=$G(DXMHARR(FNBR2,IENS2,1,"E"))
 . S @MODIFIER@("standsFor")=$G(DXMHARR(FNBR2,IENS2,2,"E"))
 . S @MODIFIER@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_DXMHIEN_S_FNBR2_S_+IENS2
 ;
 ;get SCT code
 S DXMH("Dxmh","diagnosisSCT")=""
 I DXMH("Dxmh","diagnosis")'="" D
 . N DATE S DATE=$S(DXMH("Dxmh","dateTimeOfDiagnosisFM")'="":DXMH("Dxmh","dateTimeOfDiagnosisFM"),1:DXMH("Dxmh","fileEntryDateFM"))
 . N MAPPING S MAPPING=$S(DATE>3150930:"sct2icd",1:"sct2icdnine")
 . N SNOMED S SNOMED=$$MAP^SYNDHPMP(MAPPING,DXMH("Dxmh","diagnosis"),"I")
 . S DXMH("Dxmh","diagnosisSCT")=$S(+SNOMED=-1:"",1:$P(SNOMED,U,2))
 ;
 ;I $G(DEBUG) W ! ZWRITE DXMH
 ;
 D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.DXMH,.DXMHJ)
 ;
 QUIT
 ;
GET1VITALS(VITALS,VITALSIEN,RETJSON,VITALSJ) ;get one Vitals record
 QUIT
 ;inputs: VITALSIEN - Vitals IEN
 ;        RETJSON - J = Return JSON
 ;output: VITALS  - array of Vitals data, by reference
 ;        VITALSJ - JSON structure of Vitals data, by reference
 ;
 I $G(DEBUG) W !,"--------------------------- Vitals -----------------------------",!
 N S S S="_"
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 N FNBR1 S FNBR1=120.5 ;GMRV VITAL MEASUREMENT
 N FNBR2 S FNBR2=120.506 ;REASON ENTERED IN ERROR
 N FNBR3 S FNBR3=120.505 ;QUALIFIER
 N IENS1 S IENS1=VITALSIEN_","
 ;
 N VITALSARR,VITALSERR
 D GETS^DIQ(FNBR1,IENS1,"**","EI","VITALSARR","VITALSERR")
 ;I $G(DEBUG) W ! ZWRITE VITALSARR
 ;I $G(DEBUG),$D(VITALSERR) W ">>ERROR<<" ZWRITE VITALSERR
 I $D(VITALSERR) S VITALS("Vitals","ERROR")=VITALSIEN QUIT
 S VITALS("Vitals","vitalsIen")=VITALSIEN
 S VITALS("Vitals","resourceType")="Observation"
 S VITALS("Vitals","resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_VITALSIEN
 S VITALS("Vitals","dateTimeVitalsTaken")=$G(VITALSARR(FNBR1,IENS1,.01,"E"))
 S VITALS("Vitals","dateTimeVitalsTakenFM")=$G(VITALSARR(FNBR1,IENS1,.01,"I"))
 S VITALS("Vitals","dateTimeVitalsTakenHL7")=$$FMTHL7^XLFDT($G(VITALSARR(FNBR1,IENS1,.01,"I")))
 S VITALS("Vitals","dateTimeVitalsTakenFHIR")=$$FMTFHIR^SYNDHPUTL($G(VITALSARR(FNBR1,IENS1,.01,"I")))
 S VITALS("Vitals","patient")=$G(VITALSARR(FNBR1,IENS1,.02,"E"))
 S VITALS("Vitals","patientId")=$G(VITALSARR(FNBR1,IENS1,.02,"I"))
 S VITALS("Vitals","patientICN")=$$GET1^DIQ(2,VITALS("Vitals","patientId")_",",991.1)
 S VITALS("Vitals","vitalType")=$G(VITALSARR(FNBR1,IENS1,.03,"E"))
 S VITALS("Vitals","vitalTypeSc")=$$SENTENCE^XLFSTR(VITALS("Vitals","vitalType"))
 S VITALS("Vitals","vitalTypeId")=$G(VITALSARR(FNBR1,IENS1,.03,"I"))
 S VITALS("Vitals","dateTimeVitalsEntered")=$G(VITALSARR(FNBR1,IENS1,.04,"E"))
 S VITALS("Vitals","dateTimeVitalsEnteredFM")=$G(VITALSARR(FNBR1,IENS1,.04,"I"))
 S VITALS("Vitals","dateTimeVitalsEnteredHL7")=$$FMTHL7^XLFDT($G(VITALSARR(FNBR1,IENS1,.04,"I")))
 S VITALS("Vitals","dateTimeVitalsEnteredFHIR")=$$FMTFHIR^SYNDHPUTL($G(VITALSARR(FNBR1,IENS1,.04,"I")))
 S VITALS("Vitals","hospitalLocation")=$G(VITALSARR(FNBR1,IENS1,.05,"E"))
 S VITALS("Vitals","hospitalLocationId")=$G(VITALSARR(FNBR1,IENS1,.05,"I"))
 S VITALS("Vitals","enteredBy")=$G(VITALSARR(FNBR1,IENS1,.06,"E"))
 S VITALS("Vitals","enteredById")=$G(VITALSARR(FNBR1,IENS1,.06,"I"))
 S VITALS("Vitals","rate")=$G(VITALSARR(FNBR1,IENS1,1.2,"E"))
 S VITALS("Vitals","supplementalO2")=$G(VITALSARR(FNBR1,IENS1,1.4,"E"))
 S VITALS("Vitals","enteredInError")=$G(VITALSARR(FNBR1,IENS1,2,"E"))
 S VITALS("Vitals","enteredInErrorCd")=$G(VITALSARR(FNBR1,IENS1,2,"I"))
 S VITALS("Vitals","errorEnteredBy")=$G(VITALSARR(FNBR1,IENS1,3,"E"))
 S VITALS("Vitals","errorEnteredById")=$G(VITALSARR(FNBR1,IENS1,3,"I"))
 N REASERR
 N IENS2 S IENS2=""
 F  S IENS2=$O(VITALSARR(FNBR2,IENS2)) QUIT:IENS2=""  D
 . S REASERR=$NA(VITALS("Vitals","reasonEnteredInErrors","reasonEnteredInError",+IENS2))
 . S @REASERR@("reasonEnteredInError")=$G(VITALSARR(FNBR2,IENS2,.01,"E"))
 . S @REASERR@("reasonEnteredInErrorCd")=$G(VITALSARR(FNBR2,IENS2,.01,"I"))
 . S @REASERR@("dateReasonEnteredInError")=$G(VITALSARR(FNBR2,IENS2,.02,"E"))
 . S @REASERR@("dateReasonEnteredInErrorFM")=$G(VITALSARR(FNBR2,IENS2,.02,"I"))
 . S @REASERR@("dateReasonEnteredInErrorHL7")=$$FMTHL7^XLFDT($G(VITALSARR(FNBR2,IENS2,.02,"I")))
 . S @REASERR@("dateReasonEnteredInErrorFHIR")=$$FMTFHIR^SYNDHPUTL($G(VITALSARR(FNBR2,IENS2,.02,"I")))
 . S @REASERR@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_VITALSIEN_S_FNBR2_S_+IENS2
 N QUALIFY
 N IENS3 S IENS3=""
 F  S IENS3=$O(VITALSARR(FNBR3,IENS3)) QUIT:IENS3=""  D
 . S QUALIFY=$NA(VITALS("Vitals","qualifiers","qualifier",+IENS3))
 . S @QUALIFY@("qualifier")=$G(VITALSARR(FNBR3,IENS3,.01,"E"))
 . S @QUALIFY@("qualifierId")=$G(VITALSARR(FNBR3,IENS3,.01,"I"))
 . S @QUALIFY@("resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_VITALSIEN_S_FNBR3_S_+IENS3
 ;
 ;get snomed
 N SCT S SCT=""
 S SCT=$$MAP^SYNDHPMP("sct2vit",VITALS("Vitals","vitalTypeId"),"I")
 S VITALS("Vitals","vitalTypeSCT")=$S(+SCT=-1:"",1:$P(SCT,U,2))
 ;
 ;I $G(DEBUG) W ! ZWRITE VITALS
 ;
 D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.VITALS,.VITALSJ)
 ;
 QUIT
 ;
GET1FLAG(FLAG,FLAGIEN,RETJSON,FLAGJ) ;get one Flag record
 QUIT
 ;inputs: FLAGIEN - Flag IEN
 ;        RETJSON - J = Return JSON
 ;output: FLAG  - array of Flag data, by reference
 ;        FLAGJ - JSON structure of Flag data, by reference
 ;
 I $G(DEBUG) W !,"--------------------------- Flag -----------------------------",!
 N S S S="_"
 N SITE S SITE=$P($$SITE^VASITE,"^",3)
 N FNBR1 S FNBR1=26.13 ;PRF ASSIGNMENT
 N FNBR2 S FNBR2=26.132 ;ASSIGNMENT NARRATIVE
 N IENS1 S IENS1=FLAGIEN_","
 ;
 N FLAGARR,FLAGERR
 D GETS^DIQ(FNBR1,IENS1,"**","EI","FLAGARR","FLAGERR")
 ;I $G(DEBUG) W ! ZWRITE FLAGARR
 ;I $G(DEBUG),$D(FLAGERR) W ">>ERROR<<" ZWRITE FLAGERR
 I $D(FLAGERR) S FLAG("Flag","ERROR")=FLAGIEN QUIT
 S FLAG("Flag","flagIen")=FLAGIEN
 S FLAG("Flag","resourceType")="Observation"
 S FLAG("Flag","resourceId")=$$RESID^SYNDHP69("V",SITE)_S_FNBR1_S_FLAGIEN
 S FLAG("Flag","number")=$G(FLAGARR(FNBR1,IENS1,.001,"E"))
 S FLAG("Flag","patientName")=$G(FLAGARR(FNBR1,IENS1,.01,"E"))
 S FLAG("Flag","patientNameId")=$G(FLAGARR(FNBR1,IENS1,.01,"I"))
 S FLAG("Flag","patientICN")=$$GET1^DIQ(2,FLAG("Flag","patientNameId")_",",991.1)
 S FLAG("Flag","flagName")=$G(FLAGARR(FNBR1,IENS1,.02,"E"))
 S FLAG("Flag","flagNameId")=$G(FLAGARR(FNBR1,IENS1,.02,"I"))
 S FLAG("Flag","status")=$G(FLAGARR(FNBR1,IENS1,.03,"E"))
 S FLAG("Flag","statusCd")=$G(FLAGARR(FNBR1,IENS1,.03,"I"))
 S FLAG("Flag","ownerSite")=$G(FLAGARR(FNBR1,IENS1,.04,"E"))
 S FLAG("Flag","ownerSiteId")=$G(FLAGARR(FNBR1,IENS1,.04,"I"))
 S FLAG("Flag","originatingSite")=$G(FLAGARR(FNBR1,IENS1,.05,"E"))
 S FLAG("Flag","originatingSiteId")=$G(FLAGARR(FNBR1,IENS1,.05,"I"))
 S FLAG("Flag","reviewDate")=$G(FLAGARR(FNBR1,IENS1,.06,"E"))
 S FLAG("Flag","reviewDateFM")=$G(FLAGARR(FNBR1,IENS1,.06,"I"))
 S FLAG("Flag","reviewDateHL7")=$$FMTHL7^XLFDT($G(FLAGARR(FNBR1,IENS1,.06,"I")))
 S FLAG("Flag","reviewDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(FLAGARR(FNBR1,IENS1,.06,"I")))
 S FLAG("Flag","assignmentNarrative")=""
 N x S x=""
 F  S x=$O(FLAGARR(FNBR1,IENS1,1,x)) QUIT:'+x  D
 . S FLAG("Flag","assignmentNarrative")=FLAG("Flag","assignmentNarrative")_$G(FLAGARR(FNBR1,IENS1,1,x))
 ;
 ;get snomed
 N SCT
 S SCT=$$MAP^SYNDHPMP("flag2sct",FLAG("Flag","flagName"),"D")
 S FLAG("Flag","flagSCT")=$S(+SCT=-1:"",1:$P(SCT,U,2))
 ;
 ;I $G(DEBUG) W ! ZWRITE FLAG
 ;
 D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.FLAG,.FLAGJ)
 ;
 QUIT
 ;
