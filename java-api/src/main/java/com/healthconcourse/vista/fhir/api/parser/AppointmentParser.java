/* Created by Perspecta http://www.perspecta.com */
/*
        Licensed to the Apache Software Foundation (ASF) under one
        or more contributor license agreements.  See the NOTICE file
        distributed with this work for additional information
        regarding copyright ownership.  The ASF licenses this file
        to you under the Apache License, Version 2.0 (the
        "License"); you may not use this file except in compliance
        with the License.  You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
        Unless required by applicable law or agreed to in writing,
        software distributed under the License is distributed on an
        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
        KIND, either express or implied.  See the License for the
        specific language governing permissions and limitations
        under the License.
*/
package com.healthconcourse.vista.fhir.api.parser;

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import com.nfeld.jsonpathlite.JsonPath;
import com.nfeld.jsonpathlite.JsonResult;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AppointmentParser implements VistaParser<Appointment> {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentParser.class);

    @Override
    public List<Appointment> parseList(String httpData) {
        List<Appointment> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        JsonResult conditionJson = JsonPath.parseOrNull(httpData);
        if (conditionJson == null) {
            LOG.warn("Unable to parse Appointment JSON");
            return result;
        }

        JSONObject appointmentContainer = conditionJson.read("$.Patappt");
        if (appointmentContainer != null) {
            JSONArray allAppointments = appointmentContainer.getJSONObject("appointments").getJSONArray("appointment");
            for (int i = 0; i < allAppointments.length(); i++) {
                Appointment appointment = new Appointment();
                appointment.setMeta(ResourceHelper.getVistaMeta());
                JSONObject json = allAppointments.getJSONObject(i);

                String resourceId = json.getString("resourceId");
                appointment.setId(resourceId);

                String rawStatus = json.getString("status");
                Appointment.AppointmentStatus status = getStatus(rawStatus);
                appointment.setStatus(status);
                String patientName = appointmentContainer.getString("name");
                String icn = appointmentContainer.getString("fullIcn");

                List<Appointment.AppointmentParticipantComponent> participantList = getParticipant(icn, patientName, status);
                String clinicName = json.getString("clinic");
                if (!StringUtils.isEmpty(clinicName)) {
                    Long id = json.optLong("clinicId", HcConstants.MISSING_ID);
                    String clinicId = id.longValue() != HcConstants.MISSING_ID ? id.toString() : "";
                    Appointment.AppointmentParticipantComponent clinic = new Appointment.AppointmentParticipantComponent();
                    clinic.setActor(ResourceHelper.createReference(HcConstants.URN_VISTA_LOCATION, clinicId, clinicName, ResourceHelper.ReferenceType.Location));
                    participantList.add(clinic);
                }
                appointment.setParticipant(participantList);

                Optional<Date> startDate = InputValidator.parseAnyDate(json.getString("appointmentDateTimeFHIR"));
                if (startDate.isPresent()) {
                    appointment.setStart(startDate.get());
                }

                String description = json.getString("purposeOfVisit");
                appointment.setDescription(description);
                appointment.setAppointmentType(convertToType(description));

                result.add(appointment);
            }
        }
        return result;
    }

    private List<Appointment.AppointmentParticipantComponent> getParticipant(String icn, String name, Appointment.AppointmentStatus status) {

        Appointment.AppointmentParticipantComponent participant = new Appointment.AppointmentParticipantComponent();
        participant.setActor(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, name, ResourceHelper.ReferenceType.Patient));

        Appointment.ParticipationStatus participationStatus = convertStatus(status);

        if(participationStatus != Appointment.ParticipationStatus.NULL) {
            participant.setStatus(participationStatus);
        }

        List<Appointment.AppointmentParticipantComponent> result = new ArrayList<>(1);
        result.add(participant);

        return result;
    }

    private CodeableConcept convertToType(String vistaType) {

        switch (vistaType) {
            case "UNSCHED. VISIT":
                return  ResourceHelper.createCodeableConcept(HcConstants.APPOINTMENT_TYPE_CODING_SYSTEM, "WALKIN", "A previously unscheduled walk-in visit");
            case "SCHEDULED VISIT":
            default:
                return  ResourceHelper.createCodeableConcept(HcConstants.APPOINTMENT_TYPE_CODING_SYSTEM, "ROUTINE", "Routine appointment");
        }
    }

    private Appointment.ParticipationStatus convertStatus(Appointment.AppointmentStatus status) {

        if(status == Appointment.AppointmentStatus.FULFILLED) {
            return Appointment.ParticipationStatus.ACCEPTED;
        } else if (status == Appointment.AppointmentStatus.CANCELLED || status == Appointment.AppointmentStatus.NOSHOW) {
            return Appointment.ParticipationStatus.DECLINED;
        } else {
            return Appointment.ParticipationStatus.NULL;
        }
    }

    private Appointment.AppointmentStatus getStatus(String rawData) {

        switch (rawData) {
            case "CANCELLED BY PATIENT":
            case "CANCELLED BY CLINIC":
                return Appointment.AppointmentStatus.CANCELLED;
            case "NO ACTION TAKEN":
                return Appointment.AppointmentStatus.NOSHOW;
            case "INPATIENT APPOINTMENT":
            default: // This is a bad default but status cannot be null, so we assume the appointment was completed
                     // unless otherwise indicated by the data
                return Appointment.AppointmentStatus.FULFILLED;
        }
    }
}
