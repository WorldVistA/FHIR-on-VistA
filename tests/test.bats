#!/usr/bin/env bats
@test "/api/Patient will get 100 patients" {
  result=$(curl -s localhost:8080/api/Patient | jq '.entry | length')
  [ "$result" -eq 100 ]
}

@test "/api/Patient paging next page works" {
  next=$(curl -s localhost:8080/api/Patient | jq -r '.link[] | select(.relation == "next").url')
  result=$(curl -s $next | jq '.entry | length')
  [ "$result" -eq 100 ]
}

@test "/api/Patient patients on next page different from previous" {
  family1=$(curl -s localhost:8080/api/Patient | jq -r '.entry[0].resource.name[0].family')
  next=$(curl -s localhost:8080/api/Patient | jq -r '.link[] | select(.relation == "next").url')
  family2=$(curl -s $next | jq -r '.entry[0].resource.name[0].family')
  [ "$family1" != "$family2" ]
}

@test "/api/Patient paging prev page works" {
  prev=$(curl -s localhost:8080/api/Patient?_page=3 | jq -r '.link[] | select(.relation == "previous").url')
  result=$(curl -s $prev | jq '.entry | length')
  [ "$result" -eq 100 ]
}

@test "/api/Patient _count will limit count" {
  result=$(curl -s localhost:8080/api/Patient?_count=20 | jq '.entry | length')
  [ "$result" -eq 20 ]
}

@test "/api/Patient _count next will retain _count" {
  next=$(curl -s localhost:8080/api/Patient?_count=20 | jq -r '.link[] | select(.relation == "next").url')
  result=$(curl -s $next | jq '.entry | length')
  [ "$result" -eq 20 ]
}

@test "/api/Patient/{identifier} works {
  icn=$(curl -s localhost:8080/api/Patient | jq -r '.entry[0].resource.identifier[0].value')
  result=$(curl -s localhost:8080/api/Patient/$icn | jq -r '.resourceType')
  [ "$result" == "Patient" ]
}

@test "/api/Patient?_id={identifier} works {
  icn=$(curl -s localhost:8080/api/Patient | jq -r '.entry[0].resource.identifier[0].value')
  result=$(curl -s localhost:8080/api/Patient?_id=$icn | jq -r '.entry[0].resource.resourceType')
  [ "$result" == "Patient" ]
}

@test "/api/Patient?gender=female works {
  gender=$(curl -s localhost:8080/api/Patient?gender=female | jq -r '.entry[].resource.gender' | uniq)
  [ "$gender" == "female" ]
}

@test "/api/Patient?birthdate=1933 will return >=3 patients, but not 100" {
  n=$(curl -s localhost:8080/api/Patient?birthdate=1933 | jq -r '.entry | length')
  [ "$n" -ge 3 ]
  [ "$n" -lt 100 ]
}

@test "/api/Patient?birthdate=1933-07 will return >=1 patients" {
  n=$(curl -s localhost:8080/api/Patient?birthdate=1933-07 | jq -r '.entry | length')
  [ "$n" -ge 1 ]
}

@test "/api/Patient?birthdate=1933-07-04 will return >=1 patients" {
  n=$(curl -s localhost:8080/api/Patient?birthdate=1933-07-04 | jq -r '.entry | length')
  [ "$n" -ge 1 ]
}

#    http://localhost:8080/api/Patient?name=AB - Get patients whose lastname,firstname begins with AB
#    http://localhost:8080/api/Patient?family=AB - Same as name
#    http://localhost:8080/api/Patient?given=AB - Get patients whose firstname begins with AB
#    http://localhost:8080/api/Patient?birthdate=2009&family=D&given=K&gender=male&_count=10&_page=2 - Can combine mulitple conditions and paging
#    http://localhost:8080/api/Patient?identifier=urn:dxc:vista:ICN%7C2740702627V766080 - Get single patient with ICN of 2740702627V766080
#    http://localhost:8080/api/Patient?identifier=urn:dxc:vista:dfn%7C1 - Get single patient with DFN of 1
#    http://localhost:8080/api/Patient?identifier=http://hl7.org/fhir/sid/us-ssn%7C999969252 -  Get single patient with SSN of 999969252

