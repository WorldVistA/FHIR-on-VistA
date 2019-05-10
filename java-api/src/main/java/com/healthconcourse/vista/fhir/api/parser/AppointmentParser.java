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
package com.healthconcourse.vista.fhir.api.parser;

import com.healthconcourse.vista.fhir.api.HcConstants;
import com.healthconcourse.vista.fhir.api.utils.InputValidator;
import com.healthconcourse.vista.fhir.api.utils.ResourceHelper;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.CodeableConcept;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AppointmentParser implements VistaParser<Appointment> {

    @Override
    public List<Appointment> parseList(String httpData) {

        List<Appointment> result = new ArrayList<>();

        if (StringUtils.isEmpty(httpData)) {
            return result;
        }

        String[] records = httpData.trim().split("\\^");

        if(records.length < 2) {
            return result;
        }

        for(int i = 1; i < records.length; i++) {
            Appointment appointment = new Appointment();

            String[] fields = records[i].split("\\|");

            appointment.setId(fields[0]);

            appointment.setStatus(getStatus(fields[2]));

            appointment.setParticipant(getParticipant(records[0], appointment.getStatus()));

            String[] rawDate = fields[0].split("_");
            Optional<Date> start = InputValidator.parseAnyDate(rawDate[1]);
            if (start.isPresent()) {
                appointment.setStart(start.get());
            }

            appointment.setDescription(fields[3]);

            appointment.setAppointmentType(convertToType(fields[3]));

            appointment.setMeta(ResourceHelper.getVistaMeta());

            result.add(appointment);
        }

        return result;
    }

    private CodeableConcept convertToType(String vistaType) {

        switch (vistaType) {
            case "SCHEDULED VISIT":
                return  ResourceHelper.createCodeableConcept(HcConstants.APPOINTMENT_TYPE_CODING_SYSTEM, "ROUTINE", "Routine appointment");
            case "UNSCHED. VISIT":
                return  ResourceHelper.createCodeableConcept(HcConstants.APPOINTMENT_TYPE_CODING_SYSTEM, "WALKIN", "A previously unscheduled walk-in visit");
            default:
                return  ResourceHelper.createCodeableConcept(HcConstants.APPOINTMENT_TYPE_CODING_SYSTEM, "ROUTINE", "Routine appointment");
        }
    }

    private List<Appointment.AppointmentParticipantComponent> getParticipant(String icn, Appointment.AppointmentStatus status) {

        Appointment.AppointmentParticipantComponent participant = new Appointment.AppointmentParticipantComponent();
        participant.setActor(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, icn, "", ResourceHelper.ReferenceType.Patient));

        Appointment.ParticipationStatus participationStatus = convertStatus(status);

        if(participationStatus != Appointment.ParticipationStatus.NULL) {
            participant.setStatus(participationStatus);
        }

        List<Appointment.AppointmentParticipantComponent> result = new ArrayList<>(1);
        result.add(participant);

        return result;
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
                return Appointment.AppointmentStatus.CANCELLED;
            case "CANCELLED BY CLINIC":
                return Appointment.AppointmentStatus.CANCELLED;
            case "NO ACTION TAKEN":
                return Appointment.AppointmentStatus.NOSHOW;
            case "INPATIENT APPOINTMENT":
                return Appointment.AppointmentStatus.FULFILLED;
            default: // This is a bad default but status cannot be null, so we assume the appointment was completed
                     // unless otherwise indicated by the data
                return Appointment.AppointmentStatus.FULFILLED;
        }
    }
}
