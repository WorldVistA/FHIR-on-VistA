SYNDHP67 ; AFHIL/fjf/art - HealthConcourse - retrieve patient data ;04/15/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
PATTIUI(RETSTA,DHPICN,FRDAT,TODAT) ; Patient TIU Notes for ICN
 ;
 ; Return patient Text Integration Utility Notes for a given patient ICN
 ;
 ; Input:
 ;   ICN     - unique patient identifier across all VistA systems
 ;   FRDAT   - from date (inclusive), optional, compared to 1201 ENTRY DATE/TIME
 ;   TODAT   - to date (inclusive), optional, compared to 1201 ENTRY DATE/TIME
 ; Output:
 ;   RETSTA  - a delimited string that lists the following information
 ;      PatientICN~ResourceID|DateTime|NoteStatus|NoteConfidentiality
 ;        |NoteAuthor|NoteLine(1)^NoteLine(2)^NoteLine(3)...
 ;        ^NoteLine(n)^|LOINC Code_LOINC Name~...
 ;
 ;  Identifier will be "V"_SITE ID_FILE #_FILE IEN   i.e. V_500_8925_930
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
 N P,S,T,SITE,USER,PATIEN,NTS,NT,NTX,NTIEN,VID,NTSTA,NTDTMFM,NTDTM,NTAUTH,NCONF,NTLS,NTL
 S P="|",S="_",T="~"
 S U=$G(U,"^")
 I '$D(DT) S DT=$$DT^XLFDT
 S DUZ=$G(DUZ,1)
 S USER=$G(DUZ,1),DUZ("AG")="V"
 S SITE=$P($$SITE^VASITE,"^",3)
 ;
 ; get list of notes for patient
 S RETSTA=DHPICN
 S PATIEN=$O(^DPT("AFICN",DHPICN,""))
 I PATIEN="" S RETSTA="-1^Internal data structure error" QUIT
 ;
 N ZXC
 D NOTES^TIUSRVLO(.ZXC,PATIEN,,,USER)
 ; now scan list for individual notes
 S NTS=$NA(@ZXC)
 S NT=0
 ; set generic LOINC code required by Azure implementation of FHIR
 N NTLOINC S NTLOINC="11506-3_Provider-unspecified Progress note"
 F  S NT=$O(@NTS@(NT)) Q:NT=""  D
 .I $E(RETSTA,$L(RETSTA))=U S RETSTA=$E(RETSTA,1,$L(RETSTA)-1)
 .S NTX=@NTS@(NT)
 .S NTIEN=$P(NTX,U)
 .S NTDTMFM=$$GET1^DIQ(8925,NTIEN_",",1201,"I")
 .QUIT:((NTDTMFM\1)<FRDAT)!((NTDTMFM\1)>TODAT)  ;quit if outside of requested date range
 .S RETSTA=RETSTA_T
 .S VID=$$RESID^SYNDHP69("V",SITE)_S_8925_S_NTIEN
 .S NTSTA=$P(NTX,U,7)
 .S NTSTA=$$NOTSTAT(NTSTA)
 .S NTDTM=$$FMTHL7^XLFDT(NTDTMFM)
 .S NTAUTH=$$GET1^DIQ(8925,NTIEN_",",1202,"I")
 .S NTAUTH=$$GET1^DIQ(200,NTAUTH_",",41.99)
 .; note confidentiality
 .S NCONF="N"
 .;W !,NT,"  -  ",NTSTA,!
 .S RETSTA=RETSTA_VID_P_NTDTM_P_NTSTA_P_NCONF_P_NTAUTH_P ;_NTLOINC_P
 .; now get note text
 .N ZXCV,NTLTX
 .D TGET^TIUSRVR1(.ZXCV,NTIEN)
 .;ZW @ZXCV
 .S NTLS=$NA(@ZXCV)
 .S NTL=0
 .F  S NTL=$O(@NTLS@(NTL)) Q:NTL=""  D
 ..S NTLTX=@NTLS@(NTL)
 ..S RETSTA=RETSTA_NTLTX_U
 .S RETSTA=RETSTA_P_NTLOINC_P
 Q
 ;
NOTSTAT(X) ; Note status mapping
 ; the VistA status must map to one of the following FHIR statii
 ;   preliminary | final | amended | entered-in-error
 ;
 ; X is External Status value (8925, .05) converted to lower case
 ;
 ;  statii from TIU(8925.6
 ;    1)="UNDICTATED^preliminary"
 ;    2)="UNTRANSCRIBED^preliminary"
 ;    3)="UNRELEASED^preliminary"
 ;    4)="UNVERIFIED^preliminary"
 ;    5)="UNSIGNED^preliminary"
 ;    6)="UNCOSIGNED^preliminary"
 ;    7)="COMPLETED^final"
 ;    8)="AMENDED^amended"
 ;    9)="PURGED^entered-in-error"
 ;    10)="TEST^entered-in-error"
 ;    11)="ACTIVE^preliminary"
 ;    13)="INACTIVE^preliminary"
 ;    14)="DELETED^enered-in-error"
 ;    15)="RETRACTED^entered-in-error"
 ;
 ;
 ; converted to values returned by TIU RPC
 ;
 N XNS
 S XNS("active")="preliminary"
 S XNS("amended")="amended"
 S XNS("completed")="final"
 S XNS("deleted")="enered-in-error"
 S XNS("inactive")="preliminary"
 S XNS("purged")="entered-in-error"
 S XNS("retracted")="entered-in-error"
 S XNS("test")="entered-in-error"
 S XNS("uncosigned")="preliminary"
 S XNS("undictated")="preliminary"
 S XNS("unreleased")="preliminary"
 S XNS("unsigned")="preliminary"
 S XNS("untranscribed")="preliminary"
 S XNS("unverified")="preliminary"
 ; 
 Q:$D(XNS(X)) $P(XNS(X),U)
 Q "error - composition status not recognised"
 ;
 ;
T1 ;
 D PATTIUI(.ZXC,"5000000001V324625")
 Q
T2 ;
 K ZXS
 D PATTIUI(.ZXS,"11004V412157")
 Q
