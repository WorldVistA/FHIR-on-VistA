# VistA REST Services#

## Architecture ##

The REST service software resides on the VistA instance to be queried. The KIDS
build can be found
[here](https://github.com/OSEHRA/FHIR-on-VistA/releases/download/2.0.0-T2/VISTA_FHIR_DATA_LOADER_BUNDLE_0P4.KID.zip).
Unzip and load the build. The build will download the M-Web-Server in the
process of installation. The server will listen by default on port 9080.

## Configuration ##
There is no configuration required currently.

## Test Installation ##

To test that the installation has completed succesfully run the following to obtain some
 ICN's (the example requests 3):

```
curl "http://localhost:8001/DHPPATICNALL?CNT=3"
1001949719V291733;1004751168V745102;1006145121V631417;
```

Then make a call for a resource (e.g. vitals) using one of the ICN's returned by the above (DHPPATICNALL):

```
curl "http://localhost:8001/DHPPATVITICN?ICN=1001949719V291733"
1001949719V291733^27113001|Weight|158.2|20110901201904-0500|V_500_120.5_33062^27113001|Weight|170.9|20141216091908-0500|V_500_120.5_33065^50373000|Height|68|20110901201904-0500|V_500_120.5_33061^50373000|Height|68|20141216091908-0500|V_500_120.5_33064^75367002|Blood pressure|144/96|20110901201904-0500|V_500_120.5_33063^75367002|Blood pressure|167/116|20141216091908-0500|V_500_120.5_33066
```
