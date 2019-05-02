# VistA REST Services#

## Architecture ##

The REST services use the RGNET web server.

![VistA REST Service architecture](../images/VistA-architecture.png)

## Installation ##

Install RGNET - the builds should be avaialle from the following links
	 1.	rgut-3.0.kid – VistA-RG-Utilities:
	    https://github.com/mdgeek/VistA-RG-Utilities

	 2.	rgne-1.0.kid – VistA Network Services (NETSERV):
	    https://github.com/mdgeek/VistA-Network-Services/tree/master/kid

	 3.	rgse-1.0.kid – VistA-Serialization-Framework:
	    https://github.com/mdgeek/VistA-Serialization-Framework/tree/master/kid
		

Install VistA REST Services patch from file SYN_1_0_2.KID

## Configuration ##



## Test Installation ##


To test that the installation has completed succesfully run the following to obtain some
 ICN's (the example requests 3):

curl "http://localhost:8001/DHPPATICNALL?CNT=3"

1001949719V291733;1004751168V745102;1006145121V631417;

Then make a call for a resource (e.g. vitals) using one of the ICN's returned by the above (DHPPATICNALL):

curl "http://localhost:8001/DHPPATVITICN?ICN=1001949719V291733"

1001949719V291733^27113001|Weight|158.2|20110901201904-0500|V_500_120.5_33062^27113001|Weight|170.9|20141216091908-0500|V_500_120.5_33065^50373000|Height|68|20110901201904-0500|V_500_120.5_33061^50373000|Height|68|20141216091908-0500|V_500_120.5_33064^75367002|Blood pressure|144/96|20110901201904-0500|V_500_120.5_33063^75367002|Blood pressure|167/116|20141216091908-0500|V_500_120.5_33066



~~~~