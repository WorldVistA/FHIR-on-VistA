SYNDTS89 ;AFHIL/HC/fjf - HealthConcourse - REST service tester ;03/28/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ; This routine tests the Health Concourse GETter REST services
 ;
 ; It specifically tests those services that have a request URL in the following format:
 ; http://ip-address-port/endpoint?ICN=icn&JSON=flag
 ; the architecture can be extended to accommodate those REST services
 ; that don't conveniently fit the above pattern
 ;
 ; the routine currently returns the REST call url for json and non-json
 ; plus the relevant data in the appropriate format for the resource 
 ;
ctrl(nopats) ;
 ; Input:
 ;   nopats - number of patiens per system
 ;     if nopats is not passed or evaluates to 0 then all patiens will be tested
 ;
 ; create array of urls of systems to be tested
 d urlrts
 ;
 ; create array of patient data endpoints to be tested
 d endpoints
 ;
 ; start of scan by url by patient by endpoint
 s (icn,url,endpoint)=""
 s nopats=$g(nopats)
 s s=";"
 s url=""
 f  s url=$o(urlrt(url)) q:url=""  d
 .; find ICN's in namespace denoted by url+port
 .s icns=$$icnstr(url)
 .i +nopats=0 s nopats=$l(icns,s)
 .s icns=$p(icns,s,1,nopats)
 .w !,"icns ",!,icns,!
 .; for namepace (specified by url+port) scan through ICN's
 .s pats=+$g(nopats)
 .i pats=0 s pats=$l(icns,s)
 .f i=1:1:pats s icn=$p(icns,s,i) q:icn=""  d
 ..;for ICN's scan through endpoints
 ..s endpoint=""
 ..f  s endpoint=$o(endpoints(endpoint)) q:endpoint=""  d
 ...f format="","J" d
 ....; build patient getter REST service URL and make call
 ....s RESTurl=$$RESTurl(url,endpoint,icn,,,format)
 ....w !,RESTurl,!
 ....s RET=$$GETURL^XTHC10(RESTurl,,"PDATA")
 ....i '$d(PDATA) w !,"no data" q
 ....s datstr=$$xthc2st(.PDATA)
 ....w !,datstr,!
 Q
 ;
icnstr(url) ; create icn string for system at url
 ; invokes DHPPATICNALL service
 ; Input:
 ;   url of system of interest
 ; Ouput:
 ;   semicolon delimited list of ICN's
 ;
 n s,icnurl,RET,icns
 s s=";"
 s icnurl=url_"DHPPATICNALL?JSON=T"
 s RET=$$GETURL^XTHC10(icnurl,,"icnar")
 s icnar=""
 s icns=$$xthc2st(.icnar)
 q icns
 ;
xthc2st(array) ; combines array elements from XTHC10 into a single string
 ; Input
 ;   array passed by reference
 ;
 n string,n,sub
 s sub=$o(array(""))
 s string=array(sub)
 s n=""
 f  s n=$o(array(sub,n)) q:n=""  d
 .s string=string_array(sub,n)
 .k array(sub,n)
 q string
 ;
RESTurl(url,endpoint,icn,fromdt,todt,format) ; create REST service URL
 ;
 s fromdt=$g(fromdt)
 s todt=$g(todt)
 s format=$g(format)
 s url=url_endpoint_"?ICN="_icn
 s:format'="" url=url_"&JSON="_format
 s:fromdt'="" url=url_"&FRDAT="_fromdt
 s:todt'="" url=url_"&TODAT="_todt
 q url
 ;
urlrts ; set up roots urls for healthConcourse dev
 s s=";"
 f i=1:1 s t=$t(urlli+i) q:t["urlend"  d
 .s urlrt($p(t,s,3))=""
 q
 ;
urlli ; list of url roots
 ;;https://vista.dev.openplatform.healthcare/rgnet-web/;
 ;;urlend
 ;;http://syn.vistaplex.org/;                                           GT.M server
 ;;http://ec2-18-208-29-125.compute-1.amazonaws.com:8001/;              AWS dev
 ;;https://vista.dev.openplatform.healthcare/rgnet-web/;                k8 dev
 ;;http://syn.vistaplex.org/;                                           GT.M server
 ;;https://vista.demo-staging.openplatform.healthcare/rgnet-web/;       k8 demo-staging
 ;;https://vista.demo.openplatform.healthcare/rgnet-web/;               k8 demo 
 ;;https://vista-general.dev.openplatform.healthcare/rgnet-web/;        tardis general
 ;;https://vista-emergency.dev.openplatform.healthcare/rgnet-web/;      tardis emergency
 ;;https://vista-specialization.dev.openplatform.healthcare/rgnet-web/; tardis specialization
 ;;urlend
 q
 ;
endpoints ; for patient data by ICN
 ; pattern is DHPPATxxxxxICN
 ;
 n n,s,q,pat
 s n="",s="/",q=""""
 s pat=1_q_"DHPPAT"_q_"2.5e"_1_q_"ICN"_q_".e"
 f  s n=$o(^RGNET(996.52,"B",n)) q:n=""  d
 .i n?@pat s endpoints($p(n,s))=""
 q
 ;;;;
ts ;
 ;
 s url="http://ec2-18-208-29-125.compute-1.amazonaws.com:8001/"
 s endpoint="DHPPATVITICN"
 s icn="9993306312V376330"
 s RESTurl=$$RESTurl(url,endpoint,icn,,,"J")
 s RET=$$GETURL^XTHC10(RESTurl,,"PDATA")
 q
