SYNPRE1 ; HC/art - HealthConcourse - Pre Install for SYN*1.0*2 ;04/10/2019
 ;;1.0;DHP;;Jan 17, 2017;Build 47
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
EN ;
 ;Delete the global for the NETSERV HTTP ENDPOINT file
 ;Standard data will be loaded
 ;
 KILL ^RGNET(996.52)
 SET ^RGNET(996.52,0)="NETSERV HTTP ENDPOINT^996.52^0^0"
 ;
 QUIT
 ;
