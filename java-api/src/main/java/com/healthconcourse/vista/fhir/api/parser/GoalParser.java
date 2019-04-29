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
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GoalParser implements VistaParser<Goal> {

    private static final Logger LOG = LoggerFactory.getLogger(GoalParser.class);
    private static final String CURRENT = "CURRENT";
    private static final String MET = "MET";
    private static final String DISCONTINUED = "DISCONTINUED";

    private String mPatientId;

    @Override
    public List<Goal> parseList(String httpData) {

        List<Goal> results = new ArrayList<>();

        String[] records = httpData.trim().split("\\^");
        boolean isFirst = true;

        for(String record : records) {
            Optional<Goal> med = parseGoalRecord(record, isFirst);
            med.ifPresent(results::add);
            isFirst = false;
        }

        return results;
    }

    private Optional<Goal> parseGoalRecord(String record, boolean isFirst) {

        Goal result = new Goal();

        String[] fields = record.split("\\|");

        if (isFirst) {
            mPatientId = fields[0];
            return Optional.empty();
        } else if (fields.length < 5) {
            return Optional.empty();
        }

        // Patient ID
        result.setSubject(ResourceHelper.createReference(HcConstants.URN_VISTA_ICN, mPatientId, "", ResourceHelper.ReferenceType.Patient));

        // Goal ID (FYI: This includes Goal Date at end of ID, which is parsed below)
        result.setId(fields[0]);

        // Start date (embedded and end of Goal ID)

        String[] rawDate = fields[0].split("_"); // Start date is the LAST component after split
        int size = rawDate.length;

        // Start date is the LAST component after split (thus size -1)
        Optional<Date> start = InputValidator.parseAnyDate(rawDate[size - 1]);
        if (start.isPresent()) {
            DateType dateType = new DateType();
            dateType.setValue(start.get());
            result.setStart(dateType);
        }

        // Description
        try {
            result.setDescription((new CodeableConcept()).setText(fields[1]));
        }
        catch (Exception ex) {
            LOG.error("Invalid goal description, exception " + ex.getMessage());
        }

        // Target date
        Optional<Date> target = InputValidator.parseAnyDate(fields[2]);

        if (target.isPresent()) {
            Goal.GoalTargetComponent gt = new Goal.GoalTargetComponent();
            DateType dateType = new DateType();
            dateType.setValue(target.get());
            gt.setDue(dateType);
            result.setTarget(gt);
        }

        // Who entered the goal
        try {
            result.setExpressedBy(new Reference().setDisplay(fields[3]));
        } catch (Exception e) {
            LOG.error("Bad Expressed by value from VistA goal, exception ", e);
        }

        // Goal Status
        try {
            String[] status = fields[4].split(";"); // Goal Status (2 parts delimited by ";")

            result.setStatus(getGoalStatus(status[1]));
        } catch (Exception ex) {
            LOG.error("Bad status info from VistA goal, exception ", ex);
        }

        result.setMeta(ResourceHelper.getVistaMeta());

        return Optional.of(result);
    }

    private static Goal.GoalStatus getGoalStatus(String data) {

        switch (data.toUpperCase()) {
            case CURRENT:
                return Goal.GoalStatus.INPROGRESS;
            case MET:
                return Goal.GoalStatus.ACHIEVED;
            case DISCONTINUED:
                return Goal.GoalStatus.CANCELLED;
            default:
                return Goal.GoalStatus.NULL;
        }
    }
}
