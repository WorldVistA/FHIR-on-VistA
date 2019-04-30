SYNDHP99 ; HC/art - HealthConcourse - retrieve VistA data for a resource id ;04/04/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
GETREC(RETSTR,RESID) ; Get VistA record for Resource ID
 ;Inputs: RETSTR - return string, by reference
 ;        RESID  - resource id - V_site_file#_record#
 ;Output: RETSTR - VistA record as a JSON string
 ;              or error message   
 ;
 new UL set UL="_"
 new P set P="|"
 set DUZ=$$DUZ^SYNDHP69
 set RETSTR=""
 ;
 ;check parameter
 if $g(RESID)="" set RETSTR="{""error:"":""resourceId is null""}" QUIT
 if $e(RESID)'="V" set RETSTR="{""error:"":""this is not a VistA resourceId""}" QUIT
 if $p(RESID,UL,2)'=DUZ(2) set RETSTR="{""error:"":""this is not the site requested""}" QUIT
 if +$p(RESID,UL,3)=0 set RETSTR="{""error:"":""the file number must be numeric""}" QUIT
 if +$p(RESID,UL,4)=0 set RETSTR="{""error:"":""the record number must be numeric""}" QUIT
 ;
 ;load file array
 new IDX,FILES,VFILES,FNBR,EP,FNAME
 for IDX=1:1 set FILES=$p($t(FILELIST+IDX),";;",2) QUIT:FILES="zzzzz"  do
 . set FNBR=$p(FILES,P,1)
 . set EP=$p(FILES,P,2)
 . set FNAME=$p(FILES,P,3)
 . set VFILES(FNBR)=EP_P_FNAME
 ;zwrite VFILES
 ;
 new FILE,RECORD
 set FILE=$p(RESID,UL,3)
 set RECORD=$p(RESID,UL,4)
 if '$d(VFILES(FILE)) set RETSTR="{""error:"":""this file is currently not supported""}" QUIT
 if $$GET1^DIQ(FILE,RECORD_",",.01)="" set RETSTR="{""error:"":""the record was not found""}" QUIT
 ;
 new GETTER
 set GETTER=$p($g(VFILES(FILE)),P,1)
 if GETTER="" set RETSTR="{""error:"":""fatal internal error""}" QUIT
 new ARRAY,JSONSTR
 set GETTER=GETTER_"(.ARRAY,RECORD,""J"",.RETSTR)"
 do @GETTER
 ;
 QUIT
 ;
FILELIST ;file#|GETer routine|file name
 ;;2|GETPATIENT^SYNDHP20|Patient
 ;;4|GET1SITE^SYNDHP10|Institution
 ;;26.13|GET1FLAG^SYNDHP17|PRF Assignment (flags)
 ;;44|GETHLOC^SYNDHP22|Hospital location
 ;;63|GETLABS^SYNDHP21|Lab Data
 ;;120.5|GET1VITALS^SYNDHP16|GMRV Vital Measurement
 ;;120.8|GET1ALLERGY^SYNDHP16|Patient Allergies
 ;;124.3|GET1GMR^SYNDHP15|GMR Text
 ;;216.8|GET1NCP^SYNDHP15|Nurs Care Plan
 ;;404.51|GET1TEAM^SYNDHP18|Team
 ;;404.52|ASGNHIST^SYNDHP19|Position Assignment History
 ;;404.53|PRECHIST^SYNDHP19|Preceptor Assignment History
 ;;404.57|TEAMPOS^SYNDHP18|Team Position
 ;;404.58|GET1TEAM^SYNDHP18|Team History
 ;;404.59|TEAMPOS^SYNDHP18|Team Position History
 ;;627.8|GET1DXMH^SYNDHP17|Diagnostic Report-Mental Health
 ;;9000010|GET1VISIT^SYNDHP13|Visit
 ;;9000010.06|GET1VPROV^SYNDHP23|V Provider
 ;;9000010.07|GET1VPOV^SYNDHP13|V POV
 ;;9000010.11|GET1IMMUN^SYNDHP12|V Immunization
 ;;90000100.18|GET1VCPT^SYNDHP14|V CPT
 ;;9000010.23|GET1HLF^SYNDHP15|V Health Factors
 ;;9000010.71|GET1VSCODE^SYNDHP14|V Standard Codes
 ;;9000011|GET1PROB^SYNDHP11|Problem
 ;;zzzzz
