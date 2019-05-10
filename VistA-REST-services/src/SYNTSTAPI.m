SYNTSTAPI ; HC/art - HealthConcourse - run in code unit tests ;05/07/2019
 ;;1.0;DHP;;Jan 17, 2017
 ;;
 ;;Original routine authored by Andrew Thompson & Ferdinand Frankson of Perspecta 2017-2019
 ;
 QUIT
 ;
EN ;Run unit tests
 ;
 N STARTTM S STARTTM=$$NOW^XLFDT()
 ;
 W !!,"----- Patient Vitals",!!
 D T1^SYNDHP01
 D T2^SYNDHP01
 W !!,"----- Patient Immunizations",!!
 D T1^SYNDHP02
 D T2^SYNDHP02
 W !!,"----- Patient Conditions",!!
 D T1^SYNDHP03
 W !!,"----- Patients with a Condition",!!
 D T2^SYNDHP03
 D T3^SYNDHP03
 W !!,"----- Patient Procedures",!!
 D T1^SYNDHP04
 W !!,"----- Patient Diagnostic Reports",!!
 D T1^SYNDHP05
 D T2^SYNDHP05
 W !!,"----- Institution Details for Hospital Location",!!
 D T1^SYNDHP06
 D T2^SYNDHP06
 W !!,"----- Patient Nursing Care Plan Goals",!!
 D T1^SYNDHP07
 D T2^SYNDHP07
 W !!,"----- Patient Flags",!!
 D T1^SYNDHP08
 D T2^SYNDHP08
 W !!,"----- Patient Health Factors",!!
 D T1^SYNDHP09
 D T2^SYNDHP09
 ;D HLFTEST0^SYNDHP09
 ;D HLFTEST1^SYNDHP09
 W !!,"----- Patient Encounters",!!
 D T1^SYNDHP40
 W !!,"----- Patient Appointments",!!
 D T1^SYNDHP41
 W !!,"----- validate patient",!!
 D TESTP^SYNDHP43
 D TESTF1^SYNDHP43
 D TESTF2^SYNDHP43
 D TESTF3^SYNDHP43
 D TESTF4^SYNDHP43
 D TESTF5^SYNDHP43
 D T3^SYNDHP43
 W !!,"----- Patient Demographics",!!
 D T1^SYNDHP47
 D T2^SYNDHP47
 D T3^SYNDHP47
 D T4^SYNDHP47
 ;D T5^SYNDHP47
 D DEMTEST0^SYNDHP47
 W !!,"----- Patient Medication Statement",!!
 ;D T1^SYNDHP48
 D T6^SYNDHP48
 W !!,"----- Patient Medication Dispense",!!
 ;D T2^SYNDHP48
 D T5^SYNDHP48
 W !!,"----- Patient Medication Administration",!!
 ;D T3^SYNDHP48
 D T4^SYNDHP48
 W !!,"----- Patient Providers for Encounters",!!
 D T1^SYNDHP54
 D T2^SYNDHP54
 W !!,"----- Patient Observations",!!
 D T1^SYNDHP56
 W !!,"----- Patient Allergies",!!
 D T1^SYNDHP57
 D T2^SYNDHP57
 D ALLTEST0^SYNDHP57
 ;D PROTEST^SYNDHP57
 W !!,"----- Care Team",!!
 D T1^SYNDHP58
 D T2^SYNDHP58
 W !!,"----- Care Teams",!!
 D T3^SYNDHP58
 D T4^SYNDHP58
 W !!,"----- Patient Care Plans",!!
 D T1^SYNDHP59
 D T2^SYNDHP59
 W !!,"----- Patient Care Plan for a Visit",!!
 D T3^SYNDHP59
 D T4^SYNDHP59
 W !!,"----- Patient TIU Notes",!!
 D T1^SYNDHP67
 D T2^SYNDHP67
 D T3^SYNDHP67
 W !!,"----- M Unit Test",!!
 ;D T1^SYNDHP69
 W !!,"----- VistA Record by Resource ID",!!
 D T1^SYNDHP99
 ;
 N ENDTM S ENDTM=$$NOW^XLFDT()
 N ELAPSED S ELAPSED=$$FMDIFF^XLFDT(ENDTM,STARTTM,3)
 ;
 W !!,"Start time:   ",$$FMTE^XLFDT(STARTTM,7),!
 W "End time:     ",$$FMTE^XLFDT(ENDTM,7),!
 W "Elapsed time: ",ELAPSED,!
 ;
 QUIT
 ;
