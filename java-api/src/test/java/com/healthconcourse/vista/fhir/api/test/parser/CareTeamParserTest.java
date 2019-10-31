/* Created by Perspecta http://www.perspecta.com */
/*
(c) 2017-2019 Perspecta

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.healthconcourse.vista.fhir.api.test.parser;

import com.healthconcourse.vista.fhir.api.parser.CareTeamParser;
import org.hl7.fhir.r4.model.CareTeam;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CareTeamParserTest {

    @Test
    public void TestSuccessfulCareTeamParse() {
        String json = "{ \"Teams\": [ { \"Team\": { \"autoAssign\": \"YES\", \"autoAssignId\": 1, \"autoDischarge\": \"YES\", \"autoDischargeId\": 1, \"canBePC\": \"YES\", \"canBePcCd\": 1, \"closeToFutureAssign\": \"NO\", \"closeToFutureAssignId\": 0, \"current#PatientsC\": 0, \"currentActivationDateC\": \"\", \"currentActivationDateCFHIR\": \"\", \"currentActivationDateCFM\": \"\", \"currentActivationDateCHL7\": \"\", \"currentEffectiveDateC\": \"\", \"currentEffectiveDateCFHIR\": \"\", \"currentEffectiveDateCFM\": \"\", \"currentEffectiveDateCHL7\": \"\", \"currentInactivationDateC\": \"\", \"currentInactivationDateCFHIR\": \"\", \"currentInactivationDateCFM\": \"\", \"currentInactivationDateCHL7\": \"\", \"currentStatusC\": \"Inactive\", \"description\": \"\", \"fhirTeamType\": \"longitudinal\", \"institution\": \"CAMP MASTER\", \"institutionId\": 500, \"max%PCPatients\": 50, \"maxPatients\": 500, \"resourceId\": \"V_500HS_404.51_1\", \"resourceType\": \"CareTeam\", \"restrictConsults\": \"NO\", \"restrictConsultsId\": 0, \"serviceDept\": \"MEDICINE\", \"serviceDeptId\": 2, \"teamIen\": 1, \"teamName\": \"RED TEAM\", \"teamPhone\": \"\", \"teamPrinter\": \"\", \"teamPrinterId\": \"\", \"teamPurpose\": \"PRIMARY CARE\", \"teamPurposeId\": 2 } }, { \"Team\": { \"autoAssign\": \"\", \"autoAssignId\": \"\", \"autoDischarge\": \"\", \"autoDischargeId\": \"\", \"canBePC\": \"NO\", \"canBePcCd\": 0, \"closeToFutureAssign\": \"\", \"closeToFutureAssignId\": \"\", \"current#PatientsC\": 1, \"currentActivationDateC\": \"OCT 27, 2011\", \"currentActivationDateCFHIR\": \"2011-10-27\", \"currentActivationDateCFM\": 3111027, \"currentActivationDateCHL7\": 20111027, \"currentEffectiveDateC\": \"OCT 27, 2011\", \"currentEffectiveDateCFHIR\": \"2011-10-27\", \"currentEffectiveDateCFM\": 3111027, \"currentEffectiveDateCHL7\": 20111027, \"currentInactivationDateC\": \"\", \"currentInactivationDateCFHIR\": \"\", \"currentInactivationDateCFM\": \"\", \"currentInactivationDateCHL7\": \"\", \"currentStatusC\": \"Active\", \"description\": \"\", \"fhirTeamType\": \"longitudinal\", \"institution\": \"\", \"institutionId\": \"\", \"max%PCPatients\": \"\", \"maxPatients\": \"\", \"positions\": { \"position\": [ { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"autoInactMsg\": \"\", \"beeperNbr\": \"\", \"canBePreceptor\": \"\", \"consultMsg\": \"\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"\", \"currActDateCFHIR\": \"\", \"currActDateCFM\": \"\", \"currActDateCHL7\": \"\", \"currEffectDateC\": \"\", \"currEffectDateCFHIR\": \"\", \"currEffectDateCFM\": \"\", \"currEffectDateCHL7\": \"\", \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currInactDateFHIR\": \"\", \"currPractitionerC\": \"\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Inactive\", \"deathMsg\": \"\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"\", \"max#patients\": \"\", \"positionAssignmentHistory\": { \"posAssignHist\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 1, \"availablePositionsC\": 3, \"currActivePatientsC\": 0, \"dateFlagedInactivation\": \"\", \"dateFlagedInactivationFHIR\": \"\", \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEntered\": \"JUL 26, 2013@09:29:49\", \"dateTimeEnteredFHIR\": \"2013-07-26T09:29:49-05:00\", \"dateTimeEnteredFM\": 3130726.092949, \"dateTimeEnteredHL7\": \"20130726092949-0500\", \"effectiveDate\": \"JUL 26, 2013\", \"effectiveDateFHIR\": \"2013-07-26\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": 1, \"fteeHistory\": { \"fteeHist\": [ { \"fteeHistoryDate\": \"JUL 26, 2013@09:29\", \"fteeHistoryDateFHIR\": \"2013-07-26T09:29:00-05:00\", \"fteeHistoryDateFM\": 3130726.0929, \"fteeHistoryDateHL7\": \"201307260929-0500\", \"resourceId\": \"V_500HS_404.52_1_404.521_1,1,\", \"user\": \"PROGRAMMER,ONE\", \"userId\": 1, \"value\": 1 } ] }, \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 3, \"practitioner\": \"PROVIDER,EIGHT\", \"practitionerId\": 991, \"resourceId\": \"V_500HS_404.52_1\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 2, \"teamPositionName\": \"PROVIDER\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ] }, \"positionIen\": 1, \"positionName\": \"OIF\\/OEF\", \"possPrimPract\": \"\", \"possPrimPractId\": \"\", \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500HS_404.57_1\", \"standRoleName\": \"TCM CLINICAL CASE MANAGER\", \"standRoleNameId\": 25, \"standRoleSct\": \"\", \"team\": \"OIF\\/OEF\", \"teamId\": 2, \"teamMsg\": \"\", \"userClass\": \"\" }, { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"autoInactMsg\": \"\", \"beeperNbr\": \"\", \"canBePreceptor\": \"\", \"consultMsg\": \"\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 2, \"currActDateC\": \"MAR 23, 2015\", \"currActDateCFHIR\": \"2015-03-23\", \"currActDateCFM\": 3150323, \"currActDateCHL7\": 20150323, \"currEffectDateC\": \"MAR 23, 2015\", \"currEffectDateCFHIR\": \"2015-03-23\", \"currEffectDateCFM\": 3150323, \"currEffectDateCHL7\": 20150323, \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currInactDateFHIR\": \"\", \"currPractitionerC\": \"VEHU,TWO\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Active\", \"deathMsg\": \"\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 2, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"\", \"max#patients\": \"\", \"positionAssignmentHistory\": { \"posAssignHist\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 1, \"availablePositionsC\": 3, \"currActivePatientsC\": 0, \"dateFlagedInactivation\": \"\", \"dateFlagedInactivationFHIR\": \"\", \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEntered\": \"JUL 26, 2013@09:29:49\", \"dateTimeEnteredFHIR\": \"2013-07-26T09:29:49-05:00\", \"dateTimeEnteredFM\": 3130726.092949, \"dateTimeEnteredHL7\": \"20130726092949-0500\", \"effectiveDate\": \"JUL 26, 2013\", \"effectiveDateFHIR\": \"2013-07-26\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": 1, \"fteeHistory\": { \"fteeHist\": [ { \"fteeHistoryDate\": \"JUL 26, 2013@09:29\", \"fteeHistoryDateFHIR\": \"2013-07-26T09:29:00-05:00\", \"fteeHistoryDateFM\": 3130726.0929, \"fteeHistoryDateHL7\": \"201307260929-0500\", \"resourceId\": \"V_500HS_404.52_1_404.521_1,1,\", \"user\": \"PROGRAMMER,ONE\", \"userId\": 1, \"value\": 1 } ] }, \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 3, \"practitioner\": \"PROVIDER,EIGHT\", \"practitionerId\": 991, \"resourceId\": \"V_500HS_404.52_1\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 2, \"teamPositionName\": \"PROVIDER\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ] }, \"positionIen\": 23, \"positionName\": \"MH COORDINATOR\", \"positionStatus\": { \"status\": [ { \"effectiveDate\": \"MAR 23, 2015\", \"effectiveDateFHIR\": \"2015-03-23\", \"effectiveDateFM\": 3150323, \"effectiveDateHL7\": 20150323, \"resourceId\": \"V_500HS_404.59_23\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18 } ] }, \"possPrimPract\": \"\", \"possPrimPractId\": \"\", \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500HS_404.57_23\", \"standRoleName\": \"PHYSICIAN-PSYCHIATRIST (MHTC)\", \"standRoleNameId\": 34, \"standRoleSct\": 80584001, \"team\": \"MH TEAM\", \"teamId\": 10, \"teamMsg\": \"\", \"userClass\": \"\" } ] }, \"resourceId\": \"V_500HS_404.51_2\", \"resourceType\": \"CareTeam\", \"restrictConsults\": \"\", \"restrictConsultsId\": \"\", \"serviceDept\": \"\", \"serviceDeptId\": \"\", \"teamIen\": 2, \"teamName\": \"OIF\\/OEF\", \"teamPhone\": \"\", \"teamPrinter\": \"\", \"teamPrinterId\": \"\", \"teamPurpose\": \"OIF OEF\", \"teamPurposeId\": 10, \"teamStatus\": { \"status\": [ { \"effectiveDate\": \"OCT 27, 2011\", \"effectiveDateFHIR\": \"2011-10-27\", \"effectiveDateFM\": 3111027, \"effectiveDateHL7\": 20111027, \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM\", \"statusReasonId\": 17, \"statusRecordId\": \"V_500HS_404.58_1\" } ] } } }, { \"Team\": { \"autoAssign\": \"\", \"autoAssignId\": \"\", \"autoDischarge\": \"\", \"autoDischargeId\": \"\", \"canBePC\": \"YES\", \"canBePcCd\": 1, \"closeToFutureAssign\": \"\", \"closeToFutureAssignId\": \"\", \"current#PatientsC\": 11, \"currentActivationDateC\": \"JUL 01, 2013\", \"currentActivationDateCFHIR\": \"2013-07-01\", \"currentActivationDateCFM\": 3130701, \"currentActivationDateCHL7\": 20130701, \"currentEffectiveDateC\": \"JUL 01, 2013\", \"currentEffectiveDateCFHIR\": \"2013-07-01\", \"currentEffectiveDateCFM\": 3130701, \"currentEffectiveDateCHL7\": 20130701, \"currentInactivationDateC\": \"\", \"currentInactivationDateCFHIR\": \"\", \"currentInactivationDateCFM\": \"\", \"currentInactivationDateCHL7\": \"\", \"currentStatusC\": \"Active\", \"description\": \"RED TEAM\", \"fhirTeamType\": \"longitudinal\", \"institution\": \"CAMP MASTER\", \"institutionId\": 500, \"max%PCPatients\": \"\", \"maxPatients\": 5, \"positions\": { \"position\": [ { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"associatedClinics\": { \"clinic\": [ { \"assocClinc\": \"GENERAL MEDICINE\", \"assocClincId\": 23, \"resourceId\": \"V_500HS_404.57_2_404.575_23\" } ] }, \"autoInactMsg\": \"DO NOT SEND\", \"beeperNbr\": 5551, \"canBePreceptor\": \"YES\", \"consultMsg\": \"POSITION'S PATIENTS\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"JUL 26, 2013\", \"currActDateCFHIR\": \"2013-07-26\", \"currActDateCFM\": 3130726, \"currActDateCHL7\": 20130726, \"currEffectDateC\": \"JUL 26, 2013\", \"currEffectDateCFHIR\": \"2013-07-26\", \"currEffectDateCFM\": 3130726, \"currEffectDateCHL7\": 20130726, \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currInactDateFHIR\": \"\", \"currPractitionerC\": \"PROVIDER,EIGHT\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Active\", \"deathMsg\": \"POSITION'S PATIENTS\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"POSITION'S PATIENTS\", \"max#patients\": 3, \"positionAssignmentHistory\": { \"posAssignHist\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 2, \"availablePositionsC\": 3, \"currActivePatientsC\": 0, \"dateFlagedInactivation\": \"\", \"dateFlagedInactivationFHIR\": \"\", \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEntered\": \"JUL 26, 2013@09:34:23\", \"dateTimeEnteredFHIR\": \"2013-07-26T09:34:23-05:00\", \"dateTimeEnteredFM\": 3130726.093423, \"dateTimeEnteredHL7\": \"20130726093423-0500\", \"effectiveDate\": \"JUL 26, 2013\", \"effectiveDateFHIR\": \"2013-07-26\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": 1, \"fteeHistory\": { \"fteeHist\": [ { \"fteeHistoryDate\": \"JUL 26, 2013@09:34\", \"fteeHistoryDateFHIR\": \"2013-07-26T09:34:00-05:00\", \"fteeHistoryDateFM\": 3130726.0934, \"fteeHistoryDateHL7\": \"201307260934-0500\", \"resourceId\": \"V_500HS_404.52_2_404.521_1,2,\", \"user\": \"PROGRAMMER,ONE\", \"userId\": 1, \"value\": 1 } ] }, \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 3, \"practitioner\": \"PHYSICIAN,ASSISTANT\", \"practitionerId\": 11820, \"resourceId\": \"V_500HS_404.52_2\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 3, \"teamPositionName\": \"PHYSICIANS ASSISTANT\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ] }, \"positionIen\": 2, \"positionName\": \"PROVIDER\", \"positionStatus\": { \"status\": [ { \"effectiveDate\": \"JUL 26, 2013\", \"effectiveDateFHIR\": \"2013-07-26\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"resourceId\": \"V_500HS_404.59_1\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18 } ] }, \"possPrimPract\": \"YES\", \"possPrimPractId\": 1, \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500HS_404.57_2\", \"standRoleName\": \"PHYSICIAN-PRIMARY CARE\", \"standRoleNameId\": 2, \"standRoleSct\": 446050000, \"team\": \"RED\", \"teamId\": 3, \"teamMsg\": \"POSITION'S PATIENTS\", \"userClass\": \"\" }, { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"associatedClinics\": { \"clinic\": [ { \"assocClinc\": \"GENERAL MEDICINE\", \"assocClincId\": 23, \"resourceId\": \"V_500HS_404.57_3_404.575_23\" } ] }, \"autoInactMsg\": \"DO NOT SEND\", \"beeperNbr\": 555, \"canBePreceptor\": \"YES\", \"consultMsg\": \"POSITION'S PATIENTS\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"JUL 25, 2013\", \"currActDateCFHIR\": \"2013-07-25\", \"currActDateCFM\": 3130725, \"currActDateCHL7\": 20130725, \"currEffectDateC\": \"JUL 25, 2013\", \"currEffectDateCFHIR\": \"2013-07-25\", \"currEffectDateCFM\": 3130725, \"currEffectDateCHL7\": 20130725, \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currInactDateFHIR\": \"\", \"currPractitionerC\": \"PHYSICIAN,ASSISTANT\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Active\", \"deathMsg\": \"POSITION'S PATIENTS\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"POSITION'S PATIENTS\", \"max#patients\": 3, \"positionAssignmentHistory\": { \"posAssignHist\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 2, \"availablePositionsC\": 3, \"currActivePatientsC\": 0, \"dateFlagedInactivation\": \"\", \"dateFlagedInactivationFHIR\": \"\", \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEntered\": \"JUL 26, 2013@09:34:23\", \"dateTimeEnteredFHIR\": \"2013-07-26T09:34:23-05:00\", \"dateTimeEnteredFM\": 3130726.093423, \"dateTimeEnteredHL7\": \"20130726093423-0500\", \"effectiveDate\": \"JUL 26, 2013\", \"effectiveDateFHIR\": \"2013-07-26\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": 1, \"fteeHistory\": { \"fteeHist\": [ { \"fteeHistoryDate\": \"JUL 26, 2013@09:34\", \"fteeHistoryDateFHIR\": \"2013-07-26T09:34:00-05:00\", \"fteeHistoryDateFM\": 3130726.0934, \"fteeHistoryDateHL7\": \"201307260934-0500\", \"resourceId\": \"V_500HS_404.52_2_404.521_1,2,\", \"user\": \"PROGRAMMER,ONE\", \"userId\": 1, \"value\": 1 } ] }, \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 3, \"practitioner\": \"PHYSICIAN,ASSISTANT\", \"practitionerId\": 11820, \"resourceId\": \"V_500HS_404.52_2\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 3, \"teamPositionName\": \"PHYSICIANS ASSISTANT\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ] }, \"positionIen\": 3, \"positionName\": \"PHYSICIANS ASSISTANT\", \"positionStatus\": { \"status\": [ { \"effectiveDate\": \"JUL 25, 2013\", \"effectiveDateFHIR\": \"2013-07-25\", \"effectiveDateFM\": 3130725, \"effectiveDateHL7\": 20130725, \"resourceId\": \"V_500HS_404.59_2\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18 } ] }, \"possPrimPract\": \"YES\", \"possPrimPractId\": 1, \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500HS_404.57_3\", \"standRoleName\": \"PHYSICIAN ASSISTANT\", \"standRoleNameId\": 5, \"standRoleSct\": 449161006, \"team\": \"RED\", \"teamId\": 3, \"teamMsg\": \"POSITION'S PATIENTS\", \"userClass\": \"\" }, { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"autoInactMsg\": \"\", \"beeperNbr\": 5545, \"canBePreceptor\": \"\", \"consultMsg\": \"\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"JUL 25, 2013\", \"currActDateCFHIR\": \"2013-07-25\", \"currActDateCFM\": 3130725, \"currActDateCHL7\": 20130725, \"currEffectDateC\": \"JUL 26, 2013\", \"currEffectDateCFHIR\": \"2013-07-26\", \"currEffectDateCFM\": 3130726, \"currEffectDateCHL7\": 20130726, \"currInactDateC\": \"JUL 26, 2013\", \"currInactDateCFM\": 3130726, \"currInactDateCHL7\": 20130726, \"currInactDateFHIR\": \"2013-07-26\", \"currPractitionerC\": \"\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Inactive\", \"deathMsg\": \"\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"\", \"max#patients\": \"\", \"positionAssignmentHistory\": { \"posAssignHist\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 2, \"availablePositionsC\": 3, \"currActivePatientsC\": 0, \"dateFlagedInactivation\": \"\", \"dateFlagedInactivationFHIR\": \"\", \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEntered\": \"JUL 26, 2013@09:34:23\", \"dateTimeEnteredFHIR\": \"2013-07-26T09:34:23-05:00\", \"dateTimeEnteredFM\": 3130726.093423, \"dateTimeEnteredHL7\": \"20130726093423-0500\", \"effectiveDate\": \"JUL 26, 2013\", \"effectiveDateFHIR\": \"2013-07-26\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": 1, \"fteeHistory\": { \"fteeHist\": [ { \"fteeHistoryDate\": \"JUL 26, 2013@09:34\", \"fteeHistoryDateFHIR\": \"2013-07-26T09:34:00-05:00\", \"fteeHistoryDateFM\": 3130726.0934, \"fteeHistoryDateHL7\": \"201307260934-0500\", \"resourceId\": \"V_500HS_404.52_2_404.521_1,2,\", \"user\": \"PROGRAMMER,ONE\", \"userId\": 1, \"value\": 1 } ] }, \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 3, \"practitioner\": \"PHYSICIAN,ASSISTANT\", \"practitionerId\": 11820, \"resourceId\": \"V_500HS_404.52_2\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 3, \"teamPositionName\": \"PHYSICIANS ASSISTANT\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ] }, \"positionIen\": 4, \"positionName\": \"NURSE PRACTITIONER\", \"positionStatus\": { \"status\": [ { \"effectiveDate\": \"JUL 25, 2013\", \"effectiveDateFHIR\": \"2013-07-25\", \"effectiveDateFM\": 3130725, \"effectiveDateHL7\": 20130725, \"resourceId\": \"V_500HS_404.59_3\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18 }, { \"effectiveDate\": \"JUL 26, 2013\", \"effectiveDateFHIR\": \"2013-07-26\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"resourceId\": \"V_500HS_404.59_5\", \"status\": \"INACTIVE\", \"statusCd\": 0, \"statusReason\": \"STAFF CHANGES\", \"statusReasonId\": 26 } ] }, \"possPrimPract\": \"\", \"possPrimPractId\": \"\", \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500HS_404.57_4\", \"standRoleName\": \"NURSE PRACTITIONER\", \"standRoleNameId\": 4, \"standRoleSct\": 224571005, \"team\": \"RED\", \"teamId\": 3, \"teamMsg\": \"\", \"userClass\": \"\" }, { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"associatedClinics\": { \"clinic\": [ { \"assocClinc\": \"GENERAL MEDICINE\", \"assocClincId\": 23, \"resourceId\": \"V_500HS_404.57_5_404.575_23\" } ] }, \"autoInactMsg\": \"DO NOT SEND\", \"beeperNbr\": \"\", \"canBePreceptor\": \"\", \"consultMsg\": \"POSITION'S PATIENTS\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"JUL 25, 2013\", \"currActDateCFHIR\": \"2013-07-25\", \"currActDateCFM\": 3130725, \"currActDateCHL7\": 20130725, \"currEffectDateC\": \"JUL 25, 2013\", \"currEffectDateCFHIR\": \"2013-07-25\", \"currEffectDateCFM\": 3130725, \"currEffectDateCHL7\": 20130725, \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currInactDateFHIR\": \"\", \"currPractitionerC\": \"NURSE,EIGHT\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Active\", \"deathMsg\": \"POSITION'S PATIENTS\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"POSITION'S PATIENTS\", \"max#patients\": \"\", \"positionAssignmentHistory\": { \"posAssignHist\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 2, \"availablePositionsC\": 3, \"currActivePatientsC\": 0, \"dateFlagedInactivation\": \"\", \"dateFlagedInactivationFHIR\": \"\", \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEntered\": \"JUL 26, 2013@09:34:23\", \"dateTimeEnteredFHIR\": \"2013-07-26T09:34:23-05:00\", \"dateTimeEnteredFM\": 3130726.093423, \"dateTimeEnteredHL7\": \"20130726093423-0500\", \"effectiveDate\": \"JUL 26, 2013\", \"effectiveDateFHIR\": \"2013-07-26\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": 1, \"fteeHistory\": { \"fteeHist\": [ { \"fteeHistoryDate\": \"JUL 26, 2013@09:34\", \"fteeHistoryDateFHIR\": \"2013-07-26T09:34:00-05:00\", \"fteeHistoryDateFM\": 3130726.0934, \"fteeHistoryDateHL7\": \"201307260934-0500\", \"resourceId\": \"V_500HS_404.52_2_404.521_1,2,\", \"user\": \"PROGRAMMER,ONE\", \"userId\": 1, \"value\": 1 } ] }, \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 3, \"practitioner\": \"PHYSICIAN,ASSISTANT\", \"practitionerId\": 11820, \"resourceId\": \"V_500HS_404.52_2\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 3, \"teamPositionName\": \"PHYSICIANS ASSISTANT\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ] }, \"positionIen\": 5, \"positionName\": \"NURSE\", \"positionStatus\": { \"status\": [ { \"effectiveDate\": \"JUL 25, 2013\", \"effectiveDateFHIR\": \"2013-07-25\", \"effectiveDateFM\": 3130725, \"effectiveDateHL7\": 20130725, \"resourceId\": \"V_500HS_404.59_4\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18 } ] }, \"possPrimPract\": \"\", \"possPrimPractId\": \"\", \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500HS_404.57_5\", \"standRoleName\": \"NURSE (RN)\", \"standRoleNameId\": 6, \"standRoleSct\": 224535009, \"team\": \"RED\", \"teamId\": 3, \"teamMsg\": \"POSITION'S PATIENTS\", \"userClass\": \"\" } ] }, \"resourceId\": \"V_500HS_404.51_3\", \"resourceType\": \"CareTeam\", \"restrictConsults\": \"\", \"restrictConsultsId\": \"\", \"serviceDept\": \"MEDICAL\", \"serviceDeptId\": 1018, \"teamIen\": 3, \"teamName\": \"RED\", \"teamPhone\": \"555-555-5551\", \"teamPrinter\": \"\", \"teamPrinterId\": \"\", \"teamPurpose\": \"PRIMARY CARE\", \"teamPurposeId\": 2, \"teamStatus\": { \"status\": [ { \"effectiveDate\": \"JUL 01, 2013\", \"effectiveDateFHIR\": \"2013-07-01\", \"effectiveDateFM\": 3130701, \"effectiveDateHL7\": 20130701, \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM\", \"statusReasonId\": 17, \"statusRecordId\": \"V_500HS_404.58_2\" } ] } } } ] }";
        CareTeamParser parser = new CareTeamParser();

        List<CareTeam> result = parser.parseList(json);

        Assert.assertEquals("Correct number of records", 3, result.size());
    }

    @Test
    public void TestFailedCareTeamParse() {
        String json = "-1;NOTaTEaM";
        CareTeamParser parser = new CareTeamParser();

        List<CareTeam> result = parser.parseList(json);

        Assert.assertEquals("Correct number of records", 0, result.size());
    }

    @Test
    public void TestFailedCareTeamParseEmptyJSON() {
        String json = "";
        CareTeamParser parser = new CareTeamParser();

        List<CareTeam> result = parser.parseList(json);

        Assert.assertEquals("Correct number of records", 0, result.size());
    }

    @Test
    public void TestCareTeamParseGarbage() {
        String json = "dqwer 978K@#$$;sim,";
        CareTeamParser parser = new CareTeamParser();

        List<CareTeam> result = parser.parseList(json);

        Assert.assertEquals("Correct number of records", 0, result.size());
    }

    @Test
    public void TestSuccessfulSingleCareTeamParse() {
        String json = "{\n" +
                "  \"Teams\": [\n" +
                "    {\n" +
                "      \"Team\": {\n" +
                "        \"autoAssign\": \"\",\n" +
                "        \"autoAssignId\": \"\",\n" +
                "        \"autoDischarge\": \"\",\n" +
                "        \"autoDischargeId\": \"\",\n" +
                "        \"canBePC\": \"NO\",\n" +
                "        \"canBePcCd\": 0,\n" +
                "        \"closeToFutureAssign\": \"\",\n" +
                "        \"closeToFutureAssignId\": \"\",\n" +
                "        \"current#PatientsC\": 1,\n" +
                "        \"currentActivationDateC\": \"OCT 27, 2011\",\n" +
                "        \"currentActivationDateCFHIR\": \"2011-10-27\",\n" +
                "        \"currentActivationDateCFM\": 3111027,\n" +
                "        \"currentActivationDateCHL7\": 20111027,\n" +
                "        \"currentEffectiveDateC\": \"OCT 27, 2011\",\n" +
                "        \"currentEffectiveDateCFHIR\": \"2011-10-27\",\n" +
                "        \"currentEffectiveDateCFM\": 3111027,\n" +
                "        \"currentEffectiveDateCHL7\": 20111027,\n" +
                "        \"currentInactivationDateC\": \"\",\n" +
                "        \"currentInactivationDateCFHIR\": \"\",\n" +
                "        \"currentInactivationDateCFM\": \"\",\n" +
                "        \"currentInactivationDateCHL7\": \"\",\n" +
                "        \"currentStatusC\": \"Active\",\n" +
                "        \"description\": \"\",\n" +
                "        \"fhirTeamType\": \"longitudinal\",\n" +
                "        \"institution\": \"\",\n" +
                "        \"institutionId\": \"\",\n" +
                "        \"max%PCPatients\": \"\",\n" +
                "        \"maxPatients\": \"\",\n" +
                "        \"positions\": {\n" +
                "          \"position\": [\n" +
                "            {\n" +
                "              \"activePreceptsC\": \"\",\n" +
                "              \"allowPreceptedChngC\": \"YES\",\n" +
                "              \"allowPreceptorChngC\": \"YES\",\n" +
                "              \"autoInactMsg\": \"\",\n" +
                "              \"beeperNbr\": \"\",\n" +
                "              \"canBePreceptor\": \"\",\n" +
                "              \"consultMsg\": \"\",\n" +
                "              \"curr#PCpatsC\": 0,\n" +
                "              \"curr#PatientsC\": 0,\n" +
                "              \"currActDateC\": \"\",\n" +
                "              \"currActDateCFHIR\": \"\",\n" +
                "              \"currActDateCFM\": \"\",\n" +
                "              \"currActDateCHL7\": \"\",\n" +
                "              \"currEffectDateC\": \"\",\n" +
                "              \"currEffectDateCFHIR\": \"\",\n" +
                "              \"currEffectDateCFM\": \"\",\n" +
                "              \"currEffectDateCHL7\": \"\",\n" +
                "              \"currInactDateC\": \"\",\n" +
                "              \"currInactDateCFM\": \"\",\n" +
                "              \"currInactDateCHL7\": \"\",\n" +
                "              \"currInactDateFHIR\": \"\",\n" +
                "              \"currPractitionerC\": \"\",\n" +
                "              \"currPreceptorC\": \"\",\n" +
                "              \"currPrecptorPosC\": \"\",\n" +
                "              \"currStatusC\": \"Inactive\",\n" +
                "              \"deathMsg\": \"\",\n" +
                "              \"description\": \"\",\n" +
                "              \"future#PCpatsC\": 0,\n" +
                "              \"future#PatientsC\": 0,\n" +
                "              \"inconsistentReasonC\": \"\",\n" +
                "              \"inpatientMsg\": \"\",\n" +
                "              \"max#patients\": \"\",\n" +
                "              \"positionAssignmentHistory\": {\n" +
                "                \"posAssignHist\": [\n" +
                "                  {\n" +
                "                    \"adjustedPanelSizeC\": 0,\n" +
                "                    \"assignHistIen\": 1,\n" +
                "                    \"availablePositionsC\": 3,\n" +
                "                    \"currActivePatientsC\": 0,\n" +
                "                    \"dateFlagedInactivation\": \"\",\n" +
                "                    \"dateFlagedInactivationFHIR\": \"\",\n" +
                "                    \"dateFlagedInactivationFM\": \"\",\n" +
                "                    \"dateFlagedInactivationHL7\": \"\",\n" +
                "                    \"dateTimeEntered\": \"JUL 26, 2013@09:29:49\",\n" +
                "                    \"dateTimeEnteredFHIR\": \"2013-07-26T09:29:49-05:00\",\n" +
                "                    \"dateTimeEnteredFM\": 3130726.092949,\n" +
                "                    \"dateTimeEnteredHL7\": \"20130726092949-0500\",\n" +
                "                    \"effectiveDate\": \"JUL 26, 2013\",\n" +
                "                    \"effectiveDateFHIR\": \"2013-07-26\",\n" +
                "                    \"effectiveDateFM\": 3130726,\n" +
                "                    \"effectiveDateHL7\": 20130726,\n" +
                "                    \"fteeEquivalent\": 1,\n" +
                "                    \"fteeHistory\": {\n" +
                "                      \"fteeHist\": [\n" +
                "                        {\n" +
                "                          \"fteeHistoryDate\": \"JUL 26, 2013@09:29\",\n" +
                "                          \"fteeHistoryDateFHIR\": \"2013-07-26T09:29:00-05:00\",\n" +
                "                          \"fteeHistoryDateFM\": 3130726.0929,\n" +
                "                          \"fteeHistoryDateHL7\": \"201307260929-0500\",\n" +
                "                          \"resourceId\": \"V_500HS_404.52_1_404.521_1,1,\",\n" +
                "                          \"user\": \"PROGRAMMER,ONE\",\n" +
                "                          \"userId\": 1,\n" +
                "                          \"value\": 1\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    },\n" +
                "                    \"inactivatedAutomaticly\": \"\",\n" +
                "                    \"inactivatedAutomaticlyCd\": \"\",\n" +
                "                    \"max#PatientsC\": 3,\n" +
                "                    \"practitioner\": \"PROVIDER,EIGHT\",\n" +
                "                    \"practitionerId\": 991,\n" +
                "                    \"resourceId\": \"V_500HS_404.52_1\",\n" +
                "                    \"status\": \"ACTIVE\",\n" +
                "                    \"statusCd\": 1,\n" +
                "                    \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\",\n" +
                "                    \"statusReasonId\": 21,\n" +
                "                    \"teamPositionId\": 2,\n" +
                "                    \"teamPositionName\": \"PROVIDER\",\n" +
                "                    \"teamletPosition\": \"\",\n" +
                "                    \"teamletPositionCd\": \"\",\n" +
                "                    \"userEntering\": \"PROGRAMMER,ONE\",\n" +
                "                    \"userEnteringId\": 1\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              \"positionIen\": 1,\n" +
                "              \"positionName\": \"OIF\\/OEF\",\n" +
                "              \"possPrimPract\": \"\",\n" +
                "              \"possPrimPractId\": \"\",\n" +
                "              \"precepConsultMsg\": \"\",\n" +
                "              \"precepDeathMsg\": \"\",\n" +
                "              \"precepInactMsg\": \"\",\n" +
                "              \"precepInpatientMsg\": \"\",\n" +
                "              \"precepTeamMsg\": \"\",\n" +
                "              \"resourceId\": \"V_500HS_404.57_1\",\n" +
                "              \"standRoleName\": \"TCM CLINICAL CASE MANAGER\",\n" +
                "              \"standRoleNameId\": 25,\n" +
                "              \"standRoleSct\": \"\",\n" +
                "              \"team\": \"OIF\\/OEF\",\n" +
                "              \"teamId\": 2,\n" +
                "              \"teamMsg\": \"\",\n" +
                "              \"userClass\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"activePreceptsC\": \"\",\n" +
                "              \"allowPreceptedChngC\": \"YES\",\n" +
                "              \"allowPreceptorChngC\": \"YES\",\n" +
                "              \"autoInactMsg\": \"\",\n" +
                "              \"beeperNbr\": \"\",\n" +
                "              \"canBePreceptor\": \"\",\n" +
                "              \"consultMsg\": \"\",\n" +
                "              \"curr#PCpatsC\": 0,\n" +
                "              \"curr#PatientsC\": 2,\n" +
                "              \"currActDateC\": \"MAR 23, 2015\",\n" +
                "              \"currActDateCFHIR\": \"2015-03-23\",\n" +
                "              \"currActDateCFM\": 3150323,\n" +
                "              \"currActDateCHL7\": 20150323,\n" +
                "              \"currEffectDateC\": \"MAR 23, 2015\",\n" +
                "              \"currEffectDateCFHIR\": \"2015-03-23\",\n" +
                "              \"currEffectDateCFM\": 3150323,\n" +
                "              \"currEffectDateCHL7\": 20150323,\n" +
                "              \"currInactDateC\": \"\",\n" +
                "              \"currInactDateCFM\": \"\",\n" +
                "              \"currInactDateCHL7\": \"\",\n" +
                "              \"currInactDateFHIR\": \"\",\n" +
                "              \"currPractitionerC\": \"VEHU,TWO\",\n" +
                "              \"currPreceptorC\": \"\",\n" +
                "              \"currPrecptorPosC\": \"\",\n" +
                "              \"currStatusC\": \"Active\",\n" +
                "              \"deathMsg\": \"\",\n" +
                "              \"description\": \"\",\n" +
                "              \"future#PCpatsC\": 0,\n" +
                "              \"future#PatientsC\": 2,\n" +
                "              \"inconsistentReasonC\": \"\",\n" +
                "              \"inpatientMsg\": \"\",\n" +
                "              \"max#patients\": \"\",\n" +
                "              \"positionAssignmentHistory\": {\n" +
                "                \"posAssignHist\": [\n" +
                "                  {\n" +
                "                    \"adjustedPanelSizeC\": 0,\n" +
                "                    \"assignHistIen\": 1,\n" +
                "                    \"availablePositionsC\": 3,\n" +
                "                    \"currActivePatientsC\": 0,\n" +
                "                    \"dateFlagedInactivation\": \"\",\n" +
                "                    \"dateFlagedInactivationFHIR\": \"\",\n" +
                "                    \"dateFlagedInactivationFM\": \"\",\n" +
                "                    \"dateFlagedInactivationHL7\": \"\",\n" +
                "                    \"dateTimeEntered\": \"JUL 26, 2013@09:29:49\",\n" +
                "                    \"dateTimeEnteredFHIR\": \"2013-07-26T09:29:49-05:00\",\n" +
                "                    \"dateTimeEnteredFM\": 3130726.092949,\n" +
                "                    \"dateTimeEnteredHL7\": \"20130726092949-0500\",\n" +
                "                    \"effectiveDate\": \"JUL 26, 2013\",\n" +
                "                    \"effectiveDateFHIR\": \"2013-07-26\",\n" +
                "                    \"effectiveDateFM\": 3130726,\n" +
                "                    \"effectiveDateHL7\": 20130726,\n" +
                "                    \"fteeEquivalent\": 1,\n" +
                "                    \"fteeHistory\": {\n" +
                "                      \"fteeHist\": [\n" +
                "                        {\n" +
                "                          \"fteeHistoryDate\": \"JUL 26, 2013@09:29\",\n" +
                "                          \"fteeHistoryDateFHIR\": \"2013-07-26T09:29:00-05:00\",\n" +
                "                          \"fteeHistoryDateFM\": 3130726.0929,\n" +
                "                          \"fteeHistoryDateHL7\": \"201307260929-0500\",\n" +
                "                          \"resourceId\": \"V_500HS_404.52_1_404.521_1,1,\",\n" +
                "                          \"user\": \"PROGRAMMER,ONE\",\n" +
                "                          \"userId\": 1,\n" +
                "                          \"value\": 1\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    },\n" +
                "                    \"inactivatedAutomaticly\": \"\",\n" +
                "                    \"inactivatedAutomaticlyCd\": \"\",\n" +
                "                    \"max#PatientsC\": 3,\n" +
                "                    \"practitioner\": \"PROVIDER,EIGHT\",\n" +
                "                    \"practitionerId\": 991,\n" +
                "                    \"resourceId\": \"V_500HS_404.52_1\",\n" +
                "                    \"status\": \"ACTIVE\",\n" +
                "                    \"statusCd\": 1,\n" +
                "                    \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\",\n" +
                "                    \"statusReasonId\": 21,\n" +
                "                    \"teamPositionId\": 2,\n" +
                "                    \"teamPositionName\": \"PROVIDER\",\n" +
                "                    \"teamletPosition\": \"\",\n" +
                "                    \"teamletPositionCd\": \"\",\n" +
                "                    \"userEntering\": \"PROGRAMMER,ONE\",\n" +
                "                    \"userEnteringId\": 1\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              \"positionIen\": 23,\n" +
                "              \"positionName\": \"MH COORDINATOR\",\n" +
                "              \"positionStatus\": {\n" +
                "                \"status\": [\n" +
                "                  {\n" +
                "                    \"effectiveDate\": \"MAR 23, 2015\",\n" +
                "                    \"effectiveDateFHIR\": \"2015-03-23\",\n" +
                "                    \"effectiveDateFM\": 3150323,\n" +
                "                    \"effectiveDateHL7\": 20150323,\n" +
                "                    \"resourceId\": \"V_500HS_404.59_23\",\n" +
                "                    \"status\": \"ACTIVE\",\n" +
                "                    \"statusCd\": 1,\n" +
                "                    \"statusReason\": \"NEW TEAM POSITION\",\n" +
                "                    \"statusReasonId\": 18\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              \"possPrimPract\": \"\",\n" +
                "              \"possPrimPractId\": \"\",\n" +
                "              \"precepConsultMsg\": \"\",\n" +
                "              \"precepDeathMsg\": \"\",\n" +
                "              \"precepInactMsg\": \"\",\n" +
                "              \"precepInpatientMsg\": \"\",\n" +
                "              \"precepTeamMsg\": \"\",\n" +
                "              \"resourceId\": \"V_500HS_404.57_23\",\n" +
                "              \"standRoleName\": \"PHYSICIAN-PSYCHIATRIST (MHTC)\",\n" +
                "              \"standRoleNameId\": 34,\n" +
                "              \"standRoleSct\": 80584001,\n" +
                "              \"team\": \"MH TEAM\",\n" +
                "              \"teamId\": 10,\n" +
                "              \"teamMsg\": \"\",\n" +
                "              \"userClass\": \"\"\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        \"resourceId\": \"V_500HS_404.51_2\",\n" +
                "        \"resourceType\": \"CareTeam\",\n" +
                "        \"restrictConsults\": \"\",\n" +
                "        \"restrictConsultsId\": \"\",\n" +
                "        \"serviceDept\": \"\",\n" +
                "        \"serviceDeptId\": \"\",\n" +
                "        \"teamIen\": 2,\n" +
                "        \"teamName\": \"OIF\\/OEF\",\n" +
                "        \"teamPhone\": \"\",\n" +
                "        \"teamPrinter\": \"\",\n" +
                "        \"teamPrinterId\": \"\",\n" +
                "        \"teamPurpose\": \"OIF OEF\",\n" +
                "        \"teamPurposeId\": 10,\n" +
                "        \"teamStatus\": {\n" +
                "          \"status\": [\n" +
                "            {\n" +
                "              \"effectiveDate\": \"OCT 27, 2011\",\n" +
                "              \"effectiveDateFHIR\": \"2011-10-27\",\n" +
                "              \"effectiveDateFM\": 3111027,\n" +
                "              \"effectiveDateHL7\": 20111027,\n" +
                "              \"status\": \"ACTIVE\",\n" +
                "              \"statusCd\": 1,\n" +
                "              \"statusReason\": \"NEW TEAM\",\n" +
                "              \"statusReasonId\": 17,\n" +
                "              \"statusRecordId\": \"V_500HS_404.58_1\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        //String json = "{ \"Teams\": [ { \"Teams\": { \"autoAssign\": \"\", \"autoAssignId\": \"\", \"autoDischarge\": \"\", \"autoDischargeId\": \"\", \"canBePC\": \"YES\", \"canBePcCd\": 1, \"closeToFutureAssign\": \"\", \"closeToFutureAssignId\": \"\", \"current#PatientsC\": 7, \"currentActivationDateC\": \"OCT 30, 2013\", \"currentActivationDateCFM\": 3131030, \"currentActivationDateCHL7\": 20131030, \"currentEffectiveDateC\": \"OCT 30, 2013\", \"currentEffectiveDateCFM\": 3131030, \"currentEffectiveDateCHL7\": 20131030, \"currentInactivationDateC\": \"\", \"currentInactivationDateCFM\": \"\", \"currentInactivationDateCHL7\": \"\", \"currentStatusC\": \"Active\", \"description\": \"\", \"fhirTeamType\": \"longitudinal\", \"institution\": \"CAMP MASTER\", \"institutionId\": 500, \"max%PCPatients\": \"\", \"maxPatients\": 5, \"positions\": [ { \"position\": { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"associatedClinics\": [ { \"assocClinc\": \"PRIMARY CARE\", \"assocClincId\": 32 } ], \"autoInactMsg\": \"DO NOT SEND\", \"beeperNbr\": 5758, \"canBePreceptor\": \"YES\", \"consultMsg\": \"POSITION'S PATIENTS\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"OCT 30, 2013\", \"currActDateCFM\": 3131030, \"currActDateCHL7\": 20131030, \"currEffectDateC\": \"OCT 30, 2013\", \"currEffectDateCFM\": 3131030, \"currEffectDateCHL7\": 20131030, \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currPractitionerC\": \"PROVIDER,FIFTYONE\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Active\", \"deathMsg\": \"POSITION'S PATIENTS\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"POSITION'S PATIENTS\", \"max#patients\": 5, \"positionAssignmentHistory\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 3, \"availablePositionsC\": 0, \"currActivePatientsC\": 0, \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEnteredFM\": 3130726.09411, \"dateTimeEnteredHL7\": \"20130726094110-0500\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": \"\", \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 0, \"practitioner\": \"NURSE,EIGHT\", \"practitionerId\": 20171, \"resourceId\": \"V_500_404.52_3\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 5, \"teamPositionName\": \"NURSE\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ], \"positionIen\": 9, \"positionName\": \"PROVIDER\", \"possPrimPract\": \"YES\", \"possPrimPractId\": 1, \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500_404.57_9\", \"standRoleName\": \"PHYSICIAN-PRIMARY CARE\", \"standRoleNameId\": 2, \"standRoleSct\": 446050000, \"status\": [ { \"effectiveDateFM\": 3131030, \"effectiveDateHL7\": 20131030, \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18, \"statusRecordId\": \"V_500_404.59_9\" } ], \"team\": \"BLUE\", \"teamId\": 5, \"teamMsg\": \"POSITION'S PATIENTS\", \"userClass\": \"\" } }, { \"position\": { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"associatedClinics\": [ { \"assocClinc\": \"PRIMARY CARE\", \"assocClincId\": 32 } ], \"autoInactMsg\": \"\", \"beeperNbr\": \"\", \"canBePreceptor\": \"\", \"consultMsg\": \"\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"OCT 30, 2013\", \"currActDateCFM\": 3131030, \"currActDateCHL7\": 20131030, \"currEffectDateC\": \"OCT 30, 2013\", \"currEffectDateCFM\": 3131030, \"currEffectDateCHL7\": 20131030, \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currPractitionerC\": \"NURSE,TEN\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Active\", \"deathMsg\": \"\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"\", \"max#patients\": \"\", \"positionAssignmentHistory\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 3, \"availablePositionsC\": 0, \"currActivePatientsC\": 0, \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEnteredFM\": 3130726.09411, \"dateTimeEnteredHL7\": \"20130726094110-0500\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": \"\", \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 0, \"practitioner\": \"NURSE,EIGHT\", \"practitionerId\": 20171, \"resourceId\": \"V_500_404.52_3\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 5, \"teamPositionName\": \"NURSE\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ], \"positionIen\": 10, \"positionName\": \"NURSE\", \"possPrimPract\": \"\", \"possPrimPractId\": \"\", \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500_404.57_10\", \"standRoleName\": \"NURSE (RN)\", \"standRoleNameId\": 6, \"standRoleSct\": 224535009, \"status\": [ { \"effectiveDateFM\": 3131030, \"effectiveDateHL7\": 20131030, \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18, \"statusRecordId\": \"V_500_404.59_10\" } ], \"team\": \"BLUE\", \"teamId\": 5, \"teamMsg\": \"\", \"userClass\": \"\" } }, { \"position\": { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"associatedClinics\": [ { \"assocClinc\": \"PRIMARY CARE\", \"assocClincId\": 32 } ], \"autoInactMsg\": \"\", \"beeperNbr\": \"\", \"canBePreceptor\": \"\", \"consultMsg\": \"\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"OCT 30, 2013\", \"currActDateCFM\": 3131030, \"currActDateCHL7\": 20131030, \"currEffectDateC\": \"OCT 30, 2013\", \"currEffectDateCFM\": 3131030, \"currEffectDateCHL7\": 20131030, \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currPractitionerC\": \"PHARMACIST,FOUR\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Active\", \"deathMsg\": \"\", \"description\": \"\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"\", \"max#patients\": \"\", \"positionAssignmentHistory\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 3, \"availablePositionsC\": 0, \"currActivePatientsC\": 0, \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEnteredFM\": 3130726.09411, \"dateTimeEnteredHL7\": \"20130726094110-0500\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": \"\", \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 0, \"practitioner\": \"NURSE,EIGHT\", \"practitionerId\": 20171, \"resourceId\": \"V_500_404.52_3\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 5, \"teamPositionName\": \"NURSE\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ], \"positionIen\": 11, \"positionName\": \"PHARMACIST\", \"possPrimPract\": \"\", \"possPrimPractId\": \"\", \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500_404.57_11\", \"standRoleName\": \"CLINICAL PHARMACIST\", \"standRoleNameId\": 8, \"standRoleSct\": 734293001, \"status\": [ { \"effectiveDateFM\": 3131030, \"effectiveDateHL7\": 20131030, \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18, \"statusRecordId\": \"V_500_404.59_11\" } ], \"team\": \"BLUE\", \"teamId\": 5, \"teamMsg\": \"\", \"userClass\": \"\" } }, { \"position\": { \"activePreceptsC\": \"\", \"allowPreceptedChngC\": \"YES\", \"allowPreceptorChngC\": \"YES\", \"autoInactMsg\": \"\", \"beeperNbr\": \"\", \"canBePreceptor\": \"\", \"consultMsg\": \"\", \"curr#PCpatsC\": 0, \"curr#PatientsC\": 0, \"currActDateC\": \"AUG 03, 2015\", \"currActDateCFM\": 3150803, \"currActDateCHL7\": 20150803, \"currEffectDateC\": \"AUG 03, 2015\", \"currEffectDateCFM\": 3150803, \"currEffectDateCHL7\": 20150803, \"currInactDateC\": \"\", \"currInactDateCFM\": \"\", \"currInactDateCHL7\": \"\", \"currPractitionerC\": \"\", \"currPreceptorC\": \"\", \"currPrecptorPosC\": \"\", \"currStatusC\": \"Active\", \"deathMsg\": \"\", \"description\": \"This person is responsible for coordinating the setup of the Blue team\", \"future#PCpatsC\": 0, \"future#PatientsC\": 0, \"inconsistentReasonC\": \"\", \"inpatientMsg\": \"\", \"max#patients\": \"\", \"positionAssignmentHistory\": [ { \"adjustedPanelSizeC\": 0, \"assignHistIen\": 3, \"availablePositionsC\": 0, \"currActivePatientsC\": 0, \"dateFlagedInactivationFM\": \"\", \"dateFlagedInactivationHL7\": \"\", \"dateTimeEnteredFM\": 3130726.09411, \"dateTimeEnteredHL7\": \"20130726094110-0500\", \"effectiveDateFM\": 3130726, \"effectiveDateHL7\": 20130726, \"fteeEquivalent\": \"\", \"inactivatedAutomaticly\": \"\", \"inactivatedAutomaticlyCd\": \"\", \"max#PatientsC\": 0, \"practitioner\": \"NURSE,EIGHT\", \"practitionerId\": 20171, \"resourceId\": \"V_500_404.52_3\", \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"EMPLOYEE ASSIGNED TO POSITION\", \"statusReasonId\": 21, \"teamPositionId\": 5, \"teamPositionName\": \"NURSE\", \"teamletPosition\": \"\", \"teamletPositionCd\": \"\", \"userEntering\": \"PROGRAMMER,ONE\", \"userEnteringId\": 1 } ], \"positionIen\": 27, \"positionName\": \"ADMIN COORDINATOR\", \"possPrimPract\": \"\", \"possPrimPractId\": \"\", \"precepConsultMsg\": \"\", \"precepDeathMsg\": \"\", \"precepInactMsg\": \"\", \"precepInpatientMsg\": \"\", \"precepTeamMsg\": \"\", \"resourceId\": \"V_500_404.57_27\", \"standRoleName\": \"ADMIN COORDINATOR\", \"standRoleNameId\": 13, \"standRoleSct\": 106331006, \"status\": [ { \"effectiveDateFM\": 3150803, \"effectiveDateHL7\": 20150803, \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM POSITION\", \"statusReasonId\": 18, \"statusRecordId\": \"V_500_404.59_27\" } ], \"team\": \"BLUE\", \"teamId\": 5, \"teamMsg\": \"\", \"userClass\": \"\" } } ], \"resourceId\": \"V_500_404.51_5\", \"resourceType\": \"VistaCareTeam\", \"restrictConsults\": \"\", \"restrictConsultsId\": \"\", \"serviceDept\": \"MEDICINE\", \"serviceDeptId\": 2, \"status\": [ { \"effectiveDateFM\": 3131030, \"effectiveDateHL7\": 20131030, \"status\": \"ACTIVE\", \"statusCd\": 1, \"statusReason\": \"NEW TEAM\", \"statusReasonId\": 17, \"statusRecordId\": \"V_500_404.58_4\" } ], \"teamIen\": 5, \"teamName\": \"BLUE\", \"teamPhone\": \"555-555-5757\", \"teamPrinter\": \"\", \"teamPrinterId\": \"\", \"teamPurpose\": \"PRIMARY CARE\", \"teamPurposeId\": 2 } } ] }\n";
        CareTeamParser parser = new CareTeamParser();

        List<CareTeam> result = parser.parseList(json);

        Assert.assertEquals("Correct number of records", 1, result.size());
    }
}
