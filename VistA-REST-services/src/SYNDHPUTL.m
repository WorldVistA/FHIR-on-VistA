SYNDHPUTL ; HC/art - HealthConcourse - various utilities ;04/08/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
TOJASON(ARRAY,JSONSTR) ;convert input array to JSON string
 ;input:  ARRAY - input array
 ;output: JSONSTR - JSON string
 ;
 N ERR,TMP
 D ENCODE^XLFJSON("ARRAY","TMP","ERR")
 ;I $G(DEBUG),$D(ERR) W !,">>ERROR<<" ZWRITE ERR
 ;I $G(DEBUG) W ! ZWRITE TMP
 ;
 S JSONSTR=""
 N IDX S IDX=""
 F  S IDX=$O(TMP(IDX)) QUIT:IDX=""  D
 . S JSONSTR=JSONSTR_$G(TMP(IDX))
 ;I $G(DEBUG) W ! ZWRITE JSONSTR
 ;
 QUIT
 ;
TOFHIR(ARRAY,FHIRARRAY) ;create FHIR tagged array from input array
 ;input:  ARRAY - input array
 ;output: FHIRARRAY - FHIR array
 ;
 ;
 QUIT
 ;
meta(fields,fileNbr) ;return field metadata
 ;input: fileNbr - file number
 ;output: fields - return array
 ;        fields(fieldNbr)=fieldName^fieldNameCC^fieldType^fieldValueSet^pointsTo
 ;
 n node0,fieldName,fieldNameCC,fieldType,fieldValueSet,pointsTo
 n fieldNbr s fieldNbr=0
 f  s fieldNbr=$o(^DD(fileNbr,fieldNbr)) quit:'+fieldNbr  d
 . s node0=^DD(fileNbr,fieldNbr,0)
 . s fieldName=$p(node0,U,1)
 . s fieldNameCC=$$camelCase(fieldName)
 . s fieldType=$p(node0,U,2)
 . s fieldType=$s(+fieldType:"M",fieldType["P":"P",fieldType["F":"F",fieldType["D":"D",fieldType["N":"N",fieldType["S":"S",fieldType["C":"C",fieldType["V":"V",1:"")
 . s fieldValueSet=$s(fieldType="S":$p(node0,U,3),1:"")
 . s pointsTo=$s(fieldType="P":+$p($p(node0,U,2),"P",2),1:"")
 . s fields(fieldNbr)=fieldName_U_fieldNameCC_U_fieldType_U_fieldValueSet_U_pointsTo_U
 ;
 ;i $g(DEBUG) w !,"fields",! zwrite fields
 ;
 QUIT
 ;
camelCase(x) ;return a string in camel case
 ;input: x - string
 ;return: camel case string
 ;
 s x=$tr(x,"`-=[]\;',./~!@#$%^&*()_+{}|:""<>?","                                 ") ;eol is out here
 s x=$$TITLE^XLFSTR(x)
 s $p(x," ",1)=$$LOW^XLFSTR($p(x," ",1))
 s x=$$STRIP^XLFSTR(x," ")
 ;
 QUIT x
 ;
PATLIST ;Get list of patients from %wd graph
 ;
 N IEN,DFN,ICN,NAME
 W !,"Patient Name",?33,"Graph IEN",?45,"DFN",?55,"ICN",!
 S IEN=0
 F  S IEN=$O(^%wd(17.040801,1,IEN)) QUIT:'+IEN  DO
 . S DFN=$$ien2dfn^SYNFUTL(IEN)
 . S ICN=$$ien2icn^SYNFUTL(IEN)
 . S NAME=$$GET1^DIQ(2,DFN_",",.01)
 . W NAME,?35,IEN,?45,DFN,?55,ICN,!
 QUIT
 ;
GETOS() ;
 ;Determine OS
 NEW ZZOS
 SET ZZOS=$$OS^%ZOSV()
 SET ZZOS=$SELECT(ZZOS["VMS":"1-VMS",ZZOS["NT":"2-MSW",ZZOS["UNIX":"3-LINUX",1:"0-")
 ;W !,ZZOS,!
 QUIT ZZOS
 ;
OPEN(ZZFILE,ZZDIR,ZZFILENM,ZZMODE) ;Open a File
 ; Inputs - ZZFILE - File tag to open
 ;          ZZDIR - Host File Directory Name
 ;          ZZFILENM - File Name
 ;          ZZMODE - Open mode - R=Read, W=Write, A=Append
 ; Output - 1 if file was opened
 ;          0 if not opened
 ;          IO - opened device designator
 ;
 NEW POP
 DO OPEN^%ZISH(ZZFILE,ZZDIR,ZZFILENM,ZZMODE)
 IF POP DO  QUIT 0
 . USE 0
 . WRITE !,"ERROR: Could not open the file: ",ZZDIR,"\",ZZFILENM,!
 . WRITE "       Ensure the directory name is valid.",!
 QUIT 1
 ;
CLOSE(ZZFILE) ;Close a File
 ; Input  - ZZFILE - File tag to close
 ; Output - none
 ;
 DO CLOSE^%ZISH(ZZFILE)
 QUIT
 ;
EOF() ;Test for EOF
 ; Input  - none
 ; Output - 1 if end of file has been reached
 ;
 QUIT $$STATUS^%ZISH
 ;
GETDFN(ZZPNAME) ;Get patient's DFN
 ;Inputs - ZZPNAME - Patient name
 ;Returns - Patient DFN
 ;
 QUIT:$GET(ZZPNAME)="" "0^Missing name"
 ;
 NEW ZZDFN,ZZMSG
 SET ZZDFN=$$FIND1^DIC(2,,"BQUX",ZZPNAME,"B",,"ZZMSG")
 ;
 QUIT ZZDFN
 ;
GETIEN(ZZFILE,ZZNAME) ;Get IEN for name
 ;Inputs - ZZFILE - File number
 ;         ZZNAME - name (.01 field)
 ;Returns - IEN
 ;
 QUIT:$GET(ZZFILE)="" "0^Missing file number"
 QUIT:$GET(ZZNAME)="" "0^Missing name"
 ;
 NEW ZZIEN,ZZMSG
 SET ZZIEN=$$FIND1^DIC(ZZFILE,,"BQUX",ZZNAME,"B",,"ZZMSG")
 ;
 QUIT ZZIEN
 ;
UICN(PATIEN) ; ICN for IEN
 ; 
 Q $$GET1^DIQ(2,PATIEN_",",991.1)
 ;
ICN(IEN) ; obtain ICN
 N ICN
 S ICN=$$GET1^DIQ(2,IEN_",",991.1)
 I ICN="" D
 . S ICN=$$GET1^DIQ(2,IEN_",",991.01)_"V"_$$GET1^DIQ(2,IEN_",",991.02)
 Q ICN
 ;
UICNVAL(ICN) ; validate ICN
 ; Input: ICN
 ; Returns: 0 - invalid
 ;          1 - valid
 ;
 I $G(ICN)="" Q 0
 I '$D(^DPT("AFICN",ICN)) Q 0
 QUIT 1
 ;
USCTPT(SCT) ; SNOMED CT preferred term
 ;Input: SCT - SNOMED Code
 ;Returns: SCT Preferred Term
 ;
 N LEX,PREF
 S LEX=$$CODE^LEXTRAN(SCT,"SCT")
 S PREF=""
 I $D(LEX("P")) S PREF=LEX("P")
 Q PREF
 ;
DATECNVT(DATE) ;convert DATE to Fileman format
 ;
 I $G(DATE)="" QUIT ""
 N %DT S %DT="T"
 N X S X=DATE
 N Y
 D ^%DT
 QUIT Y
 ;
FMTFHIR(FMDATE) ;Convert Fileman date to FHIR date
 ;Inputs - FMDATE - Fileman date/time
 ;Returns - FHIR Date
 ;
 QUIT:$GET(FMDATE)="" ""
 ;
 NEW FHIRDATE SET FHIRDATE=""
 ;
 I $L(FMDATE)<7 QUIT ""
 I $L(FMDATE)=7 D  QUIT FHIRDATE
 . S FHIRDATE=$E(FMDATE,1,3)+1700_"-"_$E(FMDATE,4,5)_"-"_$E(FMDATE,6,7)
 ;
 N hl7,dtm,tz
 S hl7=$$FMTHL7^XLFDT(FMDATE)
 I hl7="" QUIT ""
 I hl7=-1 QUIT -1
 S dtm=$P(hl7,"-",1)
 S tz="-"_$E($P(hl7,"-",2),1,2)_":"_$E($P(hl7,"-",2),3,4)
 S FHIRDATE=$E(hl7,1,4)_"-"_$E(hl7,5,6)_"-"_$E(hl7,7,8)_"T"_$E(hl7,9,10)_":"_$E(hl7,11,12)_":"_$S(+$E(hl7,13,14)>0:$E(hl7,13,14),1:"00")_tz
 ;
 QUIT FHIRDATE
 ;
CTRLS(X) ; strip control characters
 N I,CTRLS
 S CTRLS=""
 F I=1:1:31,127:1:190 S CTRLS=CTRLS_$C(I)
 Q $TR(X,CTRLS)
 ;
ZW(VAR) ; simulate Caché or GT.M ZW or ZWRITE fuction
 ; VAR - variable to be ZWritten
 ; 
 N TSUB,I
 W $G(@VAR)
 S TSUB=VAR F I=1:1 S TSUB=$Q(@TSUB) Q:TSUB'[VAR  W !,TSUB,"=",@TSUB
 Q ""
 ;
ICNPAT(ICN) ; patient for ICN
 ; Input: ICN
 ; Returns: 0 - invalid
 ;          ICN^IEN^NAME - valid
 ;
 I $G(ICN)="" Q 0
 I '$D(^DPT("AFICN",ICN)) Q 0
 N PATIEN S PATIEN=$O(^DPT("AFICN",ICN,""))
 N PATNAME S PATNAME=$$GET1^DIQ(2,PATIEN,.01)
 QUIT ICN_U_PATIEN_U_PATNAME
 ;
