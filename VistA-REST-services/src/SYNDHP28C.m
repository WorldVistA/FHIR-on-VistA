SYNDHP28C ; HC/art - HealthConcourse - continuation of get Patient Prescription data ;08/28/2019
 ;;1.0;DHP;;Jan 17, 2017
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
RXCONT3 ; continuation of GET1PATRX^SYNDHP28 - get Patient Prescription data
 ;
 N IENS17 S IENS17=""
 F  S IENS17=$O(PATRXARR(FNBR17,IENS17)) QUIT:IENS17=""  D
 . N REJECTCOM S REJECTCOM=$NA(PATRX("Patrx","rejectInfos","rejectInfo",$P(IENS17,C,2),"commentss","comments",+IENS17))
 . S @REJECTCOM@("dateTime")=$G(PATRXARR(FNBR17,IENS17,.01,"E"))
 . S @REJECTCOM@("dateTimeFM")=$G(PATRXARR(FNBR17,IENS17,.01,"I"))
 . S @REJECTCOM@("dateTimeHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR17,IENS17,.01,"I")))
 . S @REJECTCOM@("dateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR17,IENS17,.01,"I")))
 . S @REJECTCOM@("user")=$G(PATRXARR(FNBR17,IENS17,1,"E"))
 . S @REJECTCOM@("userId")=$G(PATRXARR(FNBR17,IENS17,1,"I"))
 . S @REJECTCOM@("comments")=$G(PATRXARR(FNBR17,IENS17,2,"E"))
 . S @REJECTCOM@("resourceId")=$$RESID^SYNDHP69("V",SITE,FNBR1,PATRXIEN,FNBR7_U_$P(IENS17,C,2)_U_FNBR17_U_+IENS17)
 ;
 N IENS10 S IENS10=""
 F  S IENS10=$O(PATRXARR(FNBR10,IENS10)) QUIT:IENS10=""  D
 . N PARTIAL S PARTIAL=$NA(PATRX("Patrx","partialDates","partialDate",+IENS10))
 . S @PARTIAL@("partialDate")=$G(PATRXARR(FNBR10,IENS10,.01,"E"))
 . S @PARTIAL@("partialDateFM")=$G(PATRXARR(FNBR10,IENS10,.01,"I"))
 . S @PARTIAL@("partialDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR10,IENS10,.01,"I")))
 . S @PARTIAL@("partialDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR10,IENS10,.01,"I")))
 . S @PARTIAL@("mailWindow")=$G(PATRXARR(FNBR10,IENS10,.02,"E"))
 . S @PARTIAL@("mailWindowCd")=$G(PATRXARR(FNBR10,IENS10,.02,"I"))
 . S @PARTIAL@("remarks")=$G(PATRXARR(FNBR10,IENS10,.03,"E"))
 . S @PARTIAL@("qty")=$G(PATRXARR(FNBR10,IENS10,.04,"E"))
 . S @PARTIAL@("daysSupply")=$G(PATRXARR(FNBR10,IENS10,.041,"E"))
 . S @PARTIAL@("currentUnitPriceOfDrug")=$G(PATRXARR(FNBR10,IENS10,.042,"E"))
 . S @PARTIAL@("pharmacistName")=$G(PATRXARR(FNBR10,IENS10,.05,"E"))
 . S @PARTIAL@("pharmacistNameId")=$G(PATRXARR(FNBR10,IENS10,.05,"I"))
 . S @PARTIAL@("pharmacistNameResId")=$$RESID^SYNDHP69("V",SITE,200,@PARTIAL@("pharmacistNameId"))
 . S @PARTIAL@("lot")=$G(PATRXARR(FNBR10,IENS10,.06,"E"))
 . S @PARTIAL@("clerkCode")=$G(PATRXARR(FNBR10,IENS10,.07,"E"))
 . S @PARTIAL@("clerkCodeId")=$G(PATRXARR(FNBR10,IENS10,.07,"I"))
 . S @PARTIAL@("loginDate")=$G(PATRXARR(FNBR10,IENS10,.08,"E"))
 . S @PARTIAL@("loginDateFM")=$G(PATRXARR(FNBR10,IENS10,.08,"I"))
 . S @PARTIAL@("loginDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR10,IENS10,.08,"I")))
 . S @PARTIAL@("loginDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR10,IENS10,.08,"I")))
 . S @PARTIAL@("division")=$G(PATRXARR(FNBR10,IENS10,.09,"E"))
 . S @PARTIAL@("divisionId")=$G(PATRXARR(FNBR10,IENS10,.09,"I"))
 . S @PARTIAL@("ndc")=$G(PATRXARR(FNBR10,IENS10,1,"E"))
 . S @PARTIAL@("manufacturer")=$G(PATRXARR(FNBR10,IENS10,2,"E"))
 . S @PARTIAL@("returnedToStock")=$G(PATRXARR(FNBR10,IENS10,5,"E"))
 . S @PARTIAL@("returnedToStockFM")=$G(PATRXARR(FNBR10,IENS10,5,"I"))
 . S @PARTIAL@("returnedToStockHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR10,IENS10,5,"I")))
 . S @PARTIAL@("returnedToStockFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR10,IENS10,5,"I")))
 . S @PARTIAL@("provider")=$G(PATRXARR(FNBR10,IENS10,6,"E"))
 . S @PARTIAL@("providerId")=$G(PATRXARR(FNBR10,IENS10,6,"I"))
 . S @PARTIAL@("providerNPI")=$$GET1^DIQ(200,@PARTIAL@("providerId")_",",41.99) ;NPI
 . S @PARTIAL@("providerResId")=$$RESID^SYNDHP69("V",SITE,200,@PARTIAL@("providerId"))
 . S @PARTIAL@("genericProvider")=$G(PATRXARR(FNBR10,IENS10,7,"E"))
 . S @PARTIAL@("dispensedDate")=$G(PATRXARR(FNBR10,IENS10,7.5,"E"))
 . S @PARTIAL@("dispensedDateFM")=$G(PATRXARR(FNBR10,IENS10,7.5,"I"))
 . S @PARTIAL@("dispensedDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR10,IENS10,7.5,"I")))
 . S @PARTIAL@("dispensedDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR10,IENS10,7.5,"I")))
 . S @PARTIAL@("releasedDateTime")=$G(PATRXARR(FNBR10,IENS10,8,"E"))
 . S @PARTIAL@("releasedDateTimeFM")=$G(PATRXARR(FNBR10,IENS10,8,"I"))
 . S @PARTIAL@("releasedDateTimeHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR10,IENS10,8,"I")))
 . S @PARTIAL@("releasedDateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR10,IENS10,8,"I")))
 . S @PARTIAL@("bingoWaitTime")=$G(PATRXARR(FNBR10,IENS10,9,"E"))
 . S @PARTIAL@("fillingPerson")=$G(PATRXARR(FNBR10,IENS10,10,"E"))
 . S @PARTIAL@("fillingPersonId")=$G(PATRXARR(FNBR10,IENS10,10,"I"))
 . S @PARTIAL@("fillingPersonResId")=$$RESID^SYNDHP69("V",SITE,200,@PARTIAL@("fillingPersonId"))
 . S @PARTIAL@("checkingPharmacist")=$G(PATRXARR(FNBR10,IENS10,11,"E"))
 . S @PARTIAL@("checkingPharmacistId")=$G(PATRXARR(FNBR10,IENS10,11,"I"))
 . S @PARTIAL@("checkingPharmacistResId")=$$RESID^SYNDHP69("V",SITE,200,@PARTIAL@("checkingPharmacistId"))
 . S @PARTIAL@("drugExpirationDate")=$G(PATRXARR(FNBR10,IENS10,12,"E"))
 . S @PARTIAL@("drugExpirationDateFM")=$G(PATRXARR(FNBR10,IENS10,12,"I"))
 . S @PARTIAL@("drugExpirationDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR10,IENS10,12,"I")))
 . S @PARTIAL@("drugExpirationDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR10,IENS10,12,"I")))
 . S @PARTIAL@("resourceId")=$$RESID^SYNDHP69("V",SITE,FNBR1,PATRXIEN,FNBR10_U_+IENS10)
 ;
 N IENS11 S IENS11=""
 F  S IENS11=$O(PATRXARR(FNBR11,IENS11)) QUIT:IENS11=""  D
 . N RETURN S RETURN=$NA(PATRX("Patrx","returnToStockLogs","returnToStockLog",+IENS11))
 . S @RETURN@("returnToStockDateTime")=$G(PATRXARR(FNBR11,IENS11,.01,"E"))
 . S @RETURN@("returnToStockDateTimeFM")=$G(PATRXARR(FNBR11,IENS11,.01,"I"))
 . S @RETURN@("returnToStockDateTimeHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR11,IENS11,.01,"I")))
 . S @RETURN@("returnToStockDateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR11,IENS11,.01,"I")))
 . S @RETURN@("fillNumber")=$G(PATRXARR(FNBR11,IENS11,1,"E"))
 . S @RETURN@("fillDate")=$G(PATRXARR(FNBR11,IENS11,2,"E"))
 . S @RETURN@("fillDateFM")=$G(PATRXARR(FNBR11,IENS11,2,"I"))
 . S @RETURN@("fillDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR11,IENS11,2,"I")))
 . S @RETURN@("fillDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR11,IENS11,2,"I")))
 . S @RETURN@("quantity")=$G(PATRXARR(FNBR11,IENS11,3,"E"))
 . S @RETURN@("daysSupply")=$G(PATRXARR(FNBR11,IENS11,4,"E"))
 . S @RETURN@("unitPriceOfDrug")=$G(PATRXARR(FNBR11,IENS11,5,"E"))
 . S @RETURN@("mailWindow")=$G(PATRXARR(FNBR11,IENS11,6,"E"))
 . S @RETURN@("mailWindowCd")=$G(PATRXARR(FNBR11,IENS11,6,"I"))
 . S @RETURN@("remarks")=$G(PATRXARR(FNBR11,IENS11,7,"E"))
 . S @RETURN@("pharmacist")=$G(PATRXARR(FNBR11,IENS11,8,"E"))
 . S @RETURN@("pharmacistId")=$G(PATRXARR(FNBR11,IENS11,8,"I"))
 . S @RETURN@("pharmacistResId")=$$RESID^SYNDHP69("V",SITE,200,@RETURN@("pharmacistId"))
 . S @RETURN@("lot")=$G(PATRXARR(FNBR11,IENS11,9,"E"))
 . S @RETURN@("clerk")=$G(PATRXARR(FNBR11,IENS11,10,"E"))
 . S @RETURN@("clerkId")=$G(PATRXARR(FNBR11,IENS11,10,"I"))
 . S @RETURN@("clerkResId")=$$RESID^SYNDHP69("V",SITE,200,@RETURN@("clerkId"))
 . S @RETURN@("loginDate")=$G(PATRXARR(FNBR11,IENS11,11,"E"))
 . S @RETURN@("loginDateFM")=$G(PATRXARR(FNBR11,IENS11,11,"I"))
 . S @RETURN@("loginDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR11,IENS11,11,"I")))
 . S @RETURN@("loginDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR11,IENS11,11,"I")))
 . S @RETURN@("division")=$G(PATRXARR(FNBR11,IENS11,12,"E"))
 . S @RETURN@("divisionId")=$G(PATRXARR(FNBR11,IENS11,12,"I"))
 . S @RETURN@("ibNumber")=$G(PATRXARR(FNBR11,IENS11,13,"E"))
 . S @RETURN@("ibNumberId")=$G(PATRXARR(FNBR11,IENS11,13,"I"))
 . S @RETURN@("copayExceedingCap")=$G(PATRXARR(FNBR11,IENS11,14,"E"))
 . S @RETURN@("copayExceedingCapId")=$G(PATRXARR(FNBR11,IENS11,14,"I"))
 . S @RETURN@("dispensedDate")=$G(PATRXARR(FNBR11,IENS11,15,"E"))
 . S @RETURN@("dispensedDateFM")=$G(PATRXARR(FNBR11,IENS11,15,"I"))
 . S @RETURN@("dispensedDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR11,IENS11,15,"I")))
 . S @RETURN@("dispensedDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR11,IENS11,15,"I")))
 . S @RETURN@("ndc")=$G(PATRXARR(FNBR11,IENS11,16,"E"))
 . S @RETURN@("manufacturer")=$G(PATRXARR(FNBR11,IENS11,17,"E"))
 . S @RETURN@("drugExpirationDate")=$G(PATRXARR(FNBR11,IENS11,18,"E"))
 . S @RETURN@("drugExpirationDateFM")=$G(PATRXARR(FNBR11,IENS11,18,"I"))
 . S @RETURN@("drugExpirationDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR11,IENS11,18,"I")))
 . S @RETURN@("drugExpirationDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR11,IENS11,18,"I")))
 . S @RETURN@("provider")=$G(PATRXARR(FNBR11,IENS11,19,"E"))
 . S @RETURN@("providerId")=$G(PATRXARR(FNBR11,IENS11,19,"I"))
 . S @RETURN@("providerNPI")=$$GET1^DIQ(200,@RETURN@("providerId")_",",41.99) ;NPI
 . S @RETURN@("providerResId")=$$RESID^SYNDHP69("V",SITE,200,@RETURN@("providerId"))
 . S @RETURN@("administeredInClinic")=$G(PATRXARR(FNBR11,IENS11,20,"E"))
 . S @RETURN@("administeredInClinicCd")=$G(PATRXARR(FNBR11,IENS11,20,"I"))
 . S @RETURN@("releasedDateTime")=$G(PATRXARR(FNBR11,IENS11,21,"E"))
 . S @RETURN@("releasedDateTimeFM")=$G(PATRXARR(FNBR11,IENS11,21,"I"))
 . S @RETURN@("releasedDateTimeHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR11,IENS11,21,"I")))
 . S @RETURN@("releasedDateTimeFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR11,IENS11,21,"I")))
 . S @RETURN@("genericProvider")=$G(PATRXARR(FNBR11,IENS11,22,"E"))
 . S @RETURN@("bingoBoardWaitTime")=$G(PATRXARR(FNBR11,IENS11,23,"E"))
 . S @RETURN@("fillingPerson")=$G(PATRXARR(FNBR11,IENS11,24,"E"))
 . S @RETURN@("fillingPersonId")=$G(PATRXARR(FNBR11,IENS11,24,"I"))
 . S @RETURN@("checkingPharmacist")=$G(PATRXARR(FNBR11,IENS11,25,"E"))
 . S @RETURN@("checkingPharmacistId")=$G(PATRXARR(FNBR11,IENS11,25,"I"))
 . S @RETURN@("checkingPharmacistResId")=$$RESID^SYNDHP69("V",SITE,200,@RETURN@("checkingPharmacistId"))
 . S @RETURN@("pfssAccountReference")=$G(PATRXARR(FNBR11,IENS11,26,"E"))
 . S @RETURN@("pfssAccountReferenceId")=$G(PATRXARR(FNBR11,IENS11,26,"I"))
 . S @RETURN@("pfssChargeId")=$G(PATRXARR(FNBR11,IENS11,27,"E"))
 . S @RETURN@("dawCode")=$G(PATRXARR(FNBR11,IENS11,28,"E"))
 . S @RETURN@("dateTimeNdcValidated")=$G(PATRXARR(FNBR11,IENS11,29,"E"))
 . S @RETURN@("dateTimeNdcValidatedFM")=$G(PATRXARR(FNBR11,IENS11,29,"I"))
 . S @RETURN@("dateTimeNdcValidatedHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR11,IENS11,29,"I")))
 . S @RETURN@("dateTimeNdcValidatedFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR11,IENS11,29,"I")))
 . S @RETURN@("ndcValidatedBy")=$G(PATRXARR(FNBR11,IENS11,30,"E"))
 . S @RETURN@("ndcValidatedById")=$G(PATRXARR(FNBR11,IENS11,30,"I"))
 . S @RETURN@("billingEligibilityIndicator")=$G(PATRXARR(FNBR11,IENS11,31,"E"))
 . S @RETURN@("billingEligibilityIndicatorCd")=$G(PATRXARR(FNBR11,IENS11,31,"I"))
 . S @RETURN@("epharmacySuspenseHoldDate")=$G(PATRXARR(FNBR11,IENS11,32,"E"))
 . S @RETURN@("epharmacySuspenseHoldDateFM")=$G(PATRXARR(FNBR11,IENS11,32,"I"))
 . S @RETURN@("epharmacySuspenseHoldDateHL7")=$$FMTHL7^XLFDT($G(PATRXARR(FNBR11,IENS11,32,"I")))
 . S @RETURN@("epharmacySuspenseHoldDateFHIR")=$$FMTFHIR^SYNDHPUTL($G(PATRXARR(FNBR11,IENS11,32,"I")))
 . S @RETURN@("resourceId")=$$RESID^SYNDHP69("V",SITE,FNBR1,PATRXIEN,FNBR11_U_+IENS11)
 ;
 ;
 QUIT
 ;
