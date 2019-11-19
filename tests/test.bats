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

#@test "addition using dc" {
#  result="$(echo 2 2+p | dc)"
#  [ "$result" -eq 4 ]
#}
