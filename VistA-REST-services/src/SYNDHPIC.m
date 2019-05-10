SYNDHPIC ;AFHIL DHP/fjf - HealthConcourse - get ICNs list ;03/28/2019
 ;;1.0;DHP;;Jan 17, 2017
 Q
 ;
ICNS(RETSTA,DHPCNT,DHPJSON) ; list all ICNs in a namespace
 ;
 ; Input:
 ;   DHPCNT  - Number of ICN's to return
 ;               nn - Integer
 ;               A  - all (default)
 ;   DHPJSON - Return format
 ;               J for JSON (default)
 ;               T for plain text
 ; Output:
 ;   ICN's in JSON format, or
 ;   ICN's in semicolon delimited text string
 ;
 N ICNAR,JSICNS,ICN,N,I
 S DHPCNT=$G(DHPCNT)
 S DHPCNT=$S(+DHPCNT>0:DHPCNT,1:"A")
 S DHPJSON=$G(DHPJSON,"J")
 S ICN=""
 F I=1:1 S ICN=$O(^DPT("AFICN",ICN)) Q:ICN=""  Q:DHPCNT'="A"&(I>DHPCNT)  D
 .S ICNAR(ICN)=""
 ;
 I DHPJSON="J" D
 .; put ICN array into json format
 .D ENCODE^XLFJSON("ICNAR","JSICNS")
 .; merge array elements into one string
 .S (RETSTA,N)="" F  S N=$O(JSICNS(N)) Q:N=""  S RETSTA=RETSTA_JSICNS(N)
 I DHPJSON'="J" D
 .S (RETSTA,N)="" F  S N=$O(ICNAR(N)) Q:N=""  S RETSTA=RETSTA_N_";"
 Q
 ;
TEST ; tests
T1 ; count
 F FT="T","J","" D
 .S CT=7
 .D ICNS(.ZXC,CT,FT)
 .W !!!,$$ZW^SYNDHPUTL("ZXC")
 Q
T2 ; all
 F FT="T","J","" D
 .S CT="A"
 .D ICNS(.ZXC,"T")
 .W !!!,$$ZW^SYNDHPUTL("ZXC")
 Q
