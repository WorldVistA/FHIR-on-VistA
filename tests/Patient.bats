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

@test "/api/Patient?_count=20 will limit count" {
  result=$(curl -s localhost:8080/api/Patient?_count=20 | jq '.entry | length')
  [ "$result" -eq 20 ]
}

@test "/api/Patient?_count=20 next will retain _count" {
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

@test "/api/Patient?name=ehmp will return >=15 patients" {
  n=$(curl -s localhost:8080/api/Patient?name=ehmp | jq -r '.entry | length')
  [ "$n" -ge 15 ]
  [ "$n" -lt 100 ]
}

@test "/api/Patient?family=ehmp same as ?name" {
  n=$(curl -s localhost:8080/api/Patient?name=ehmp | jq -r '.entry | length')
  [ "$n" -ge 15 ]
  [ "$n" -lt 100 ]
}

@test "/api/Patient?given=s will return some results" {
  n=$(curl -s localhost:8080/api/Patient?given=s | jq -r '.entry | length')
  [ "$n" -gt 0 ]
  [ "$n" -lt 100 ]
}

@test "/api/Patient?family=ehmp&birthdate=1960&_count=5&page=2 works" {
  n=$(curl -s 'localhost:8080/api/Patient?family=ehmp&birthdate=1960&_count=5&_page=2' | jq -r '.entry | length')
  [ "$n" -eq 5 ]
}

@test "/api/Patient?identifier=urn:dxc:vista:ICN|5000000367V135883 works {
  n=$(curl -s 'localhost:8080/api/Patient?identifier=urn:dxc:vista:ICN%7C5000000367V135883' | jq -r '.entry | length')
  [ "$n" -eq 1 ]
}

@test "/api/Patient?identifier=urn:dxc:vista:dfn|1 works {
  n=$(curl -s 'localhost:8080/api/Patient?identifier=urn:dxc:vista:dfn%7C1' | jq -r '.entry | length')
  [ "$n" -eq 1 ]
}

@test "/api/Patient?identifier=http://hl7.org/fhir/sid/us-ssn|666110006 works {
  n=$(curl -s 'localhost:8080/api/Patient?identifier=http://hl7.org/fhir/sid/us-ssn%7C666110006' | jq -r '.entry | length')
  [ "$n" -eq 1 ]
}
