SYNDHP17 ; HC/art - HealthConcourse - get MH Diagnosis, Flags data ;03/01/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
 ;-------------------------------------------------------------
 ;
GET1DXMH(DXMH,DXMHIEN,RETJSON,DXMHJ) ;get one MH Diagnosis record
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
 I $D(DXMHERR) D  QUIT
 . S DXMH("Dxmh","ERROR")=DXMHIEN
 . D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.DXMH,.DXMHJ)
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
GET1FLAG(FLAG,FLAGIEN,RETJSON,FLAGJ) ;get one Flag record
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
 I $D(FLAGERR) D  QUIT
 . S FLAG("Flag","ERROR")=FLAGIEN
 . D:$G(RETJSON)="J" TOJASON^SYNDHPUTL(.FLAG,.FLAGJ)
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
