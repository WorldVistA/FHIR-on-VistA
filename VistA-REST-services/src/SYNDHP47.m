SYNDHP47 ; HC/fjf/art - HealthConcourse - retrieve patient demographics ;05/07/2019
 ;;1.0;DHP;;Jan 17, 2017
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
 ; ----------------  Get Patient Demographics  ----------------------
 ;
PATDEM(RETSTA,NAME,SSN,DOB,GENDER,RETJSON) ; get demographics for a patient by traits
 S RETSTA="-1^This service has been retired"
 QUIT
 ; Return patient demographics for name, SSN, DOB, and gender
 ;
 ; Input:
 ;   NAME    - patient name
 ;   SSN     - social security number
 ;   DOB     - date of birth
 ;   GENDER  - gender
 ;   RETJSON - J = Return JSON
 ;             F = Return FHIR
 ;             0 or null = Return patient demographics string (default)
 ; Output:
 ;   RETSTA  - a delimited string that has the following
 ;             ICN^Name^Phone Number^Gender^Date of Birth^Address1,Address2,Address3,^City^State^ZIP^resID
 ;          or patient demographics in JSON format
 ;
 ; validate patient
 N ICNST
 D PATVAL^SYNDHP43(.ICNST,NAME,SSN,DOB,GENDER)
 I +ICNST'=1 S RETSTA=ICNST QUIT
 S DHPICN=$P(ICNST,U,3)
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 ;
 ; get patient demographics
 N PATARRAY
 S RETSTA=$$GETPATS(.PATARRAY,PATIEN)
 ;
 ;I $G(RETJSON)="F" D xxx  ;create array for FHIR
 ;
 I $G(RETJSON)="J"!($G(RETJSON)="F") D
 . S RETSTA=""
 . D TOJASON^SYNDHPUTL(.PATARRAY,.RETSTA)
 ;
 QUIT
 ;
 ; ----------------  Get Patient Demographics  ----------------------
 ;
PATDEMI(RETSTA,DHPICN,RETJSON) ; get demographics for patient by ICN
 ;
 ; Return patient demographics for given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   RETJSON - J = Return JSON
 ;             F = Return FHIR
 ;             0 or null = Return patient demographics string (default)
 ; Output:
 ;   RETSTA  - a delimited string that has the following
 ;             ICN^Name^Phone Number^Gender^Date of Birth^Address1,Address2,Address3,^City^State^ZIP^resID
 ;          or patient demographics in JSON format
 ;
 ; validate ICN
 I $G(DHPICN)="" S RETSTA="-1^What patient?" QUIT
 I '$$UICNVAL^SYNDHPUTL(DHPICN) S RETSTA="-1^Patient identifier not recognised" QUIT
 ; get patient IEN from ICN
 N PATIEN S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 ;
 ; get patient demographics
 N PATARRAY
 S RETSTA=$$GETPATS(.PATARRAY,PATIEN)
 ;
 ;I $G(RETJSON)="F" D xxx  ;create array for FHIR
 ;
 I $G(RETJSON)="J"!($G(RETJSON)="F") D
 . S RETSTA=""
 . D TOJASON^SYNDHPUTL(.PATARRAY,.RETSTA)
 ;
 QUIT
 ;
GETPATS(PATARRAY,PATIEN) ; get demographics for patient
 ;
 N C,S,RESID,NAME,PHONE,SEX,DOB,ADRS1,ADRS2,ADRS3,CITY,STATE,ZIP,ICN
 S C=","
 S S="_"
 ;
 N PATIENT
 D GETPATIENT^SYNDHP20(.PATIENT,PATIEN,0,"")
 I $D(PATIENT("Patient","ERROR")) QUIT "-1^error reading patient file"
 S RESID=PATIENT("Patient","resourceId")
 S NAME=PATIENT("Patient","name")
 S PHONE=PATIENT("Patient","phoneNumberResidence")
 S SEX=PATIENT("Patient","sex")
 S DOB=PATIENT("Patient","dateOfBirthHL7")
 S ADRS1=PATIENT("Patient","streetAddressLine1")
 S ADRS2=PATIENT("Patient","streetAddressLine2")
 S ADRS3=PATIENT("Patient","streetAddressLine3")
 S CITY=PATIENT("Patient","city")
 S STATE=PATIENT("Patient","state")
 S ZIP=PATIENT("Patient","zipCode")
 S ICN=PATIENT("Patient","fullICN")
 S PATSTR=ICN_U_NAME_U_PHONE_U_SEX_U_DOB_U_ADRS1_C_ADRS2_C_ADRS3_C_U_CITY_U_STATE_U_ZIP_U_RESID
 M PATARRAY("Patients",PATIEN)=PATIENT
 ;
 QUIT PATSTR
 ;
PATDEMAL(RETSTA,DHPICN,RETJSON) ; get demographics for all patients
 ;
 ; Return patient demographics for all patients
 ;
 ; Input:
 ;   DHPICN - "ALL"
 ;   RETJSON - J = Return JSON (JSON is not currently supported for this call)
 ;             F = Return FHIR
 ;             0 or null = Return patient demographics string (default)
 ; Output:
 ;   RETSTA  - a delimited string that has the following data elements for each patient
 ;             ICN^Name^Phone Number^Gender^Date of Birth^Address1,Address2,Address3,^City^State^ZIP^resID|...
 ;             data for each patient is separated by "|"
 ;          or patient demographics in JSON format
 ;
 I $G(DHPICN)'="ALL" S RETSTA="-1^please specify ICN=ALL" QUIT
 I ($G(RETJSON)="J"!($G(RETJSON)="F")) S RETSTA="-1^JSON for all patients is currently not supported" QUIT
 ;
 N C,S,CT
 S C=","
 S S="_"
 S CT=0
 K ^TMP("DHPPATALL",$J)
 S ^TMP("DHPPATALL",$J)=""
 K ^SYNDHPTMP("DHPCOUNT")
 ;
 N PATARRAY,PATSTR
 N PATIEN S PATIEN=0
 F  S PATIEN=$O(^DPT(PATIEN)) Q:+PATIEN=0  D
 .S PATSTR=$$GETPATS(.PATARRAY,PATIEN)
 .S ^TMP("DHPPATALL",$J)=^TMP("DHPPATALL",$J)_PATSTR_"|"
 .S CT=CT+1
 .S ^SYNDHPTMP("DHPCOUNT",$J,CT)=PATSTR
 S RETSTA=^TMP("DHPPATALL",$J)
 ;
 ;I $G(RETJSON)="F" D xxx  ;create array for FHIR
 ;
 I $G(RETJSON)="J"!($G(RETJSON)="F") D
 . S RETSTA=""
 . D TOJASON^SYNDHPUTL(.PATARRAY,.RETSTA)
 ;
 Q
 ;
PATDEMRNG(RETSTA,DHPPATS,RETJSON) ; get demographics for range of patients
 ;
 ; Return patient demographics for a range of patients
 ;
 ; Input:
 ;   DHPPATS - mmmmRnnnn where nnnn and mmmm are patient DFNs, mmmm<nnnn (limited 1000 patients for JSON)
 ;   RETJSON - J = Return JSON
 ;             F = Return FHIR
 ;             0 or null = Return patient demographics string (default)
 ; Output:
 ;   RETSTA  - a delimited string that has the following
 ;             ICN^Name^Phone Number^Gender^Date of Birth^Address1,Address2,Address3,^City^State^ZIP^resID|...
 ;          or patient demographics in JSON format
 ;
 ;
 N C,M,S,SOR,EOR,N,NOPATS,FPATIEN,PTNTS,DHPICN,PATSTR,I,RESID
 S C=",",M="R"
 S S="_"
 N SITE S SITE=$P($$SITE^VASITE,U,3)
 ;
 S CT=0
 K ^TMP("DHPPATRNG",$J) S ^TMP("DHPPATRNG",$J)=""
 K ^SYNDHPTMP("DHPCOUNT")
 N PATARRAY
 ;
 I DHPPATS'?1.N1"R"1.N S RETSTA="-1^Invalid range format" QUIT
 S SOR=$P(DHPPATS,M),EOR=$P(DHPPATS,M,2)
 I EOR<SOR S RETSTA="-1^End before beginning" QUIT
 S NOPATS=EOR-SOR+1
 I ($G(RETJSON)="J"!($G(RETJSON)="F")),NOPATS>1000 S RETSTA="-1^please limit JSON request to 1000 patients" QUIT
 ; find first patient IEN
 S PATIEN=SOR-1
 F  S PATIEN=$O(^DPT(PATIEN)) QUIT:(PATIEN>EOR)!(+PATIEN=0)  D
 .;W !,PATIEN
 .;Q
 .S PATSTR=$$GETPATS(.PATARRAY,PATIEN)
 .S ^TMP("DHPPATRNG",$J)=^TMP("DHPPATRNG",$J)_PATSTR_"|"
 .S CT=CT+1
 .S ^SYNDHPTMP("DHPCOUNT",$J,CT)=PATSTR
 S RETSTA=^TMP("DHPPATRNG",$J)
 ;
 ;I $G(RETJSON)="F" D xxx  ;create array for FHIR
 ;
 I $G(RETJSON)="J"!($G(RETJSON)="F") D
 . S RETSTA=""
 . D TOJASON^SYNDHPUTL(.PATARRAY,.RETSTA)
 ;
 Q
 ;
 ; ----------- Unit Test -----------
T1 ;
 N ICN S ICN="1034989029V875306"
 N JSON S JSON=""
 N RETSTA
 D PATDEMI(.RETSTA,ICN,JSON)
 W $$ZW^SYNDHPUTL("RETSTA"),!!
 QUIT
 ;
T2 ;
 N ICN S ICN="1034989029V875306"
 N JSON S JSON="J"
 N RETSTA
 D PATDEMI(.RETSTA,ICN,JSON)
 W $$ZW^SYNDHPUTL("RETSTA"),!!
 QUIT
 ;
T3 ;
 N RANGE S RANGE="10R15"
 N JSON S JSON=""
 N RETSTA
 D PATDEMRNG(.RETSTA,RANGE,JSON)
 W $$ZW^SYNDHPUTL("RETSTA"),!!
 QUIT
 ;
T4 ;
 N RANGE S RANGE="10R15"
 N JSON S JSON="J"
 N RETSTA
 D PATDEMRNG(.RETSTA,RANGE,JSON)
 W $$ZW^SYNDHPUTL("RETSTA"),!!
 QUIT
 ;
T5 ;
 N ICN S ICN="ALL"
 N JSON S JSON=""
 N RETSTA
 D PATDEMAL(.RETSTA,ICN,JSON)
 W $$ZW^SYNDHPUTL("RETSTA"),!!
 QUIT
 ;
