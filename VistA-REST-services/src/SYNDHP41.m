SYNDHP41 ; HC/rdb/art - HealthConcourse - retrieve patient appointments ;05/04/2019
 ;;1.0;DHP;;Jan 17, 2017
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
 ; ---------------- Get patient appointment information ------------------------------
 ;
PATAPTI(RETSTA,DHPICN,FRDAT,TODAT) ; Patient appointments for ICN
 ;
 ; Return patient appointments for a given patient ICN
 ; Returns a patient's appointments from PATIENT:APPOINTMENT (2:1900)
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDAT   - from date (inclusive), optional, compared to appointment date/time
 ;   TODAT   - to date (inclusive), optional, compared to appointment date/time
 ; Output:
 ;   RETSTA  - a delimited string that lists patient appointment information
 ;          ICN ^ Patient IEN _ Appt Date (HL7) | Clinic | Appt Status | purpose of visit | Appt Type | Resource ID ^...
 ;
 ; bypass for CQM
 ;
 ; ***********
 ; *********** Important Note for open source community
 ; ***********
 ; *********** Perspecta - who developed this source code and have released it to the open source
 ; *********** need the following six lines to remain intact
 ;
 ;I DHPICN="1686299845V246594" D  Q
 ;.S RETSTA="1686299845V246594^101916_201704281200-0500|GENERAL MEDICINE||UNSCHED. VISIT|REGULAR"
 ;
 ; *********** the above lines will be redacted by Perspecta at some suitable juncture to
 ; *********** be determined by Perspecta
 ; ***********
 ; *********** End of Important Note for open source community
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
 ; get patient IEN from ICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 ;
 S RETSTA=$$APPTS(PATIEN,DHPICN,FRDAT,TODAT)
 Q
 ;
APPTS(PATIEN,DHPICN,FRDAT,TODAT) ; get appointments for a patient
 ;
 N HLFCTS,FNUM,SITE,APPTDTTM,PTAPTID,CLNAM,APDTDISP,APPTSTAT,APPTPURP,APPTTYPE,ZARR
 N C,P,S
 S C=",",P="|",S="_"
 S SITE=$P($$SITE^VASITE,"^",3)
 S FNUM=2.98      ;  patient appointment sub-file
 ; scan PATIENT "S" index for Appointments
 S APPTDTTM=0
 F  S APPTDTTM=$O(^DPT(PATIEN,"S",APPTDTTM)) Q:APPTDTTM=""  D
 .QUIT:((APPTDTTM\1)<FRDAT)!((APPTDTTM\1)>TODAT)  ;quit if outside of requested date range
 .S PTAPTID=$$RESID^SYNDHP69("V",SITE)_S_2_S_PATIEN_S_FNUM_S_APPTDTTM
 .S CLNAM=$$GET1^DIQ(FNUM,APPTDTTM_","_PATIEN,.01) ;clinic
 .S APDTDISP=$$FMTHL7^XLFDT(APPTDTTM) ;appt. date/time
 .S APPTSTAT=$$GET1^DIQ(FNUM,APPTDTTM_","_PATIEN,3) ;status
 .S APPTPURP=$$GET1^DIQ(FNUM,APPTDTTM_","_PATIEN,9) ;purpose of visit
 .S APPTTYPE=$$GET1^DIQ(FNUM,APPTDTTM_","_PATIEN,9.5) ;appt. type
 .S ZARR(DHPICN,APPTDTTM)=PATIEN_S_APDTDISP_P_CLNAM_P_APPTSTAT_P_APPTPURP_P_APPTTYPE_P_PTAPTID
 ; serialize data
 S APPTDTTM=""
 N APPTREC
 S HLFCTS=DHPICN
 F  S APPTDTTM=$O(ZARR(DHPICN,APPTDTTM)) Q:APPTDTTM=""  D
 .S APPTREC=ZARR(DHPICN,APPTDTTM)
 .S HLFCTS=HLFCTS_U_APPTREC
 Q HLFCTS
 ;
 ; ----------- Unit Test -----------
T1 ;
 N ICN S ICN="1198013374V588739"
 N FRDAT S FRDAT=""
 N TODAT S TODAT=""
 N RETSTA
 D PATAPTI(.RETSTA,ICN,FRDAT,TODAT)
 W $$ZW^SYNDHPUTL("RETSTA")
 QUIT
 ;
