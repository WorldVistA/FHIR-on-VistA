/* Created by Perspecta http://www.perspecta.com */
/*
(c) 2017-2019 Perspecta
(c) 2019 OSEHRA

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
package com.healthconcourse.vista.fhir.api.provider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Flag;
import org.hl7.fhir.dstu3.model.Goal;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.MedicationDispense;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.dstu3.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.healthconcourse.vista.fhir.api.service.PatientService;
import com.healthconcourse.vista.fhir.api.service.VistaPatientService;
import com.healthconcourse.vista.fhir.api.vista.VistaData;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;


/**
 * FHIR web service for patients
 *
 */

@Path("Patient")
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class PatientProvider extends AbstractJaxRsResourceProvider<Patient> {

    private static final Logger LOG = LoggerFactory.getLogger(PatientProvider.class);
    private static final Integer DEFAULT_MONTHS = 6;

    private final PatientService service;

    @Autowired
    public PatientProvider(VistaData data) {

        super(FhirContext.forDstu3(), PatientProvider.class);

        service = new VistaPatientService(data);
    }


    @Override
    public Class<Patient> getResourceType() {

        return Patient.class;
    }

    @Read
    public Patient find(@IdParam final IdType theId) {

        return service.getPatientById(theId.getIdPart());
    }

    @Search(compartmentName = "Encounter")
    public List<Encounter> findEncounters(@IdParam IdType theId) {

        List<Encounter> results = service.getEncountersForPatient(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No encounters found for patient: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "MedicationDispense")
    public List<MedicationDispense> findMedicationDispenses(@IdParam IdType theId) {

        List<MedicationDispense> results;

        results = service.getMedicationDispense(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No medication dispense found for ICN: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "MedicationAdministration")
    public List<MedicationAdministration> findMedicationAdmin(@IdParam IdType theId) {

        List<MedicationAdministration> results;

        results = service.getMMedicationAdministration(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No medication administrations found for ICN: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "MedicationStatement")
    public List<MedicationStatement> findMedicationStatement(@IdParam IdType theId) {
        List<MedicationStatement> results;

        results = service.getMedicationStatement(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No medication statements found for ICN: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }
        return results;
    }

    @Search(compartmentName = "Procedure")
    public List<Procedure> findProcedures(@IdParam IdType theId) {
        List<Procedure> results;

        results = service.getProcedures(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No procedures found for ICN: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "Observation")
    public List<Observation> findObservations(@IdParam IdType theIdn, @OptionalParam(name = Observation.SP_CODE) final StringParam code) {

        List<Observation> results;

        if(code == null) {
            results = service.getObservationsByIcn(theIdn.getIdPart());
        } else {
            results = service.getObservationsByIcnAndCode(theIdn.getIdPart(), code.getValue());
        }

        if(results.isEmpty()) {
            String message = "No observations found for ICN: " + theIdn.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "Condition")
    public List<Condition> findConditions(@IdParam IdType theId) {

        List<Condition> results = service.getConditionsForPatient(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No conditions found for patient: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;

    }

    @Search(compartmentName = "Practitioner")
    public List<Practitioner> findPractitioner(@IdParam IdType theId) {

        List<Practitioner> results = service.getPractitionersById(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No practitioners found for patient: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "Flag")
    public List<Flag> findFlags(@IdParam IdType theId) {

        List<Flag> results = service.getFlagsByIcn(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No flags found for patient: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "Appointment")
    public List<Appointment> findAppointments(@IdParam IdType theId) {

        List<Appointment> results = service.getAppointmentsByIcn(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No appointments found for patient: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "AllergyIntolerance")
    public List<AllergyIntolerance> findAllergy(@IdParam IdType theId) {

        List<AllergyIntolerance> results = service.getAllergyByIcn(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No allergy intolerance found for patient: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "Immunization")
    public List<Immunization> findImmunizations(@IdParam IdType theId) {

        List<Immunization> results = service.getImmunizationsByIcn(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No immunizations found for patient: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "Goal")
    public List<Goal> findGoals(@IdParam IdType theId) {

        List<Goal> results;

        results = service.getGoal(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No goal found for ICN: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "DiagnosticReport")
    public List<DiagnosticReport> findDiagnosticReports(@IdParam IdType theId) {

        List<DiagnosticReport> results;

        results = service.getDiagnosticReport(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No DiagnosticReport found for ICN: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search(compartmentName = "CarePlan")
    public List<CarePlan> findCarePlans(@IdParam IdType theId) {

        List<CarePlan> results;

        results = service.getCarePlan(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No CarePlan found for ICN: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    @Search
    public Bundle getAllPatients(
            @OptionalParam(name = Patient.SP_IDENTIFIER) final StringParam identifier,
            @OptionalParam(name = Patient.SP_NAME)       final StringParam name,
            @OptionalParam(name = Patient.SP_GENDER)     final StringParam gender,
            @OptionalParam(name = Patient.SP_FAMILY)     final StringParam family,
            @OptionalParam(name = Patient.SP_GIVEN)      final StringParam given,
            @OptionalParam(name = Patient.SP_BIRTHDATE)  final DateParam   dob,
            @OptionalParam(name = Constants.PARAM_COUNT) final NumberParam count,
            @OptionalParam(name = "_page")               final NumberParam page,
            RequestDetails request
            )
    {
        int defaultServerCount = 100;

        Bundle bundle = createBundle(String.format("%s/%s", request.getFhirServerBase(), request.getRequestPath()));

        HashMap<String, String> options = new HashMap<String, String>();
        if (identifier != null) options.put(Patient.SP_IDENTIFIER, identifier.getValue());
        if (name       != null) options.put(Patient.SP_NAME,       name.getValue());
        if (gender     != null) options.put(Patient.SP_GENDER,     gender.getValue());
        if (family     != null) options.put(Patient.SP_FAMILY,     family.getValue());
        if (given      != null) options.put(Patient.SP_GIVEN,      given.getValue());
        if (dob        != null) options.put(Patient.SP_BIRTHDATE,  dob.getValueAsString());
        if (count      != null) options.put(Constants.PARAM_COUNT, count.getValue().toPlainString());
        // page is not a FHIR standard thing; but our own. FHIR doesn't have it.
        if (page       != null) options.put("_page",               page.getValue().toPlainString());

        List<Patient> results = service.getAllPatients(options);

        if(results.isEmpty()) {
            String message = "No patients found";
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        for(Resource item : results){
            bundle.addEntry().setResource(item);
        }

        bundle.setTotal(results.size());

        Bundle.BundleLinkComponent nextlink = new Bundle.BundleLinkComponent();
        Bundle.BundleLinkComponent prevlink = new Bundle.BundleLinkComponent();
        List<Bundle.BundleLinkComponent> links = new ArrayList<>();
        String baseUrl = bundle.getLink(Constants.LINK_SELF).getUrl();
        String urlWithParameters = baseUrl + "?";

        FhirContext c = getFhirContext();
        if (identifier != null) urlWithParameters += Patient.SP_IDENTIFIER + "=" + identifier.getValueAsQueryToken(c) + "&";
        if (name       != null) urlWithParameters += Patient.SP_NAME +       "=" + name.getValueAsQueryToken(c)       + "&";
        if (gender     != null) urlWithParameters += Patient.SP_GENDER +     "=" + gender.getValueAsQueryToken(c)     + "&";
        if (family     != null) urlWithParameters += Patient.SP_FAMILY +     "=" + family.getValueAsQueryToken(c)     + "&";
        if (given      != null) urlWithParameters += Patient.SP_GIVEN  +     "=" + given.getValueAsQueryToken(c)      + "&";
        if (dob        != null) urlWithParameters += Patient.SP_BIRTHDATE +  "=" + dob.getValueAsQueryToken(c)        + "&";

        int contextCount;
        if (count      != null) contextCount = count.getValue().intValue();
        else                    contextCount = defaultServerCount;
        urlWithParameters += Constants.PARAM_COUNT + "=" + contextCount + "&";

        // Page is special
        if (page != null) {
            int originalPage = page.getValue().intValue();

            // If we have less results than the max, we are at the end, and no need to have next page
            if (!(results.size() < contextCount)) {
                String nextUrl = urlWithParameters;
                nextUrl += "_page=" + page.setValue(BigDecimal.valueOf(originalPage + 1)).getValueAsQueryToken(c);
                nextlink.setRelation(Constants.LINK_NEXT);
                nextlink.setUrl(nextUrl);
                links.add(nextlink);
            }

            if (originalPage != 1)
            {
                String prevUrl = urlWithParameters;
                prevUrl += "_page=" + page.setValue(BigDecimal.valueOf(originalPage - 1)).getValueAsQueryToken(c);
                prevlink.setRelation(Constants.LINK_PREVIOUS);
                prevlink.setUrl(prevUrl);
                links.add(prevlink);
            }
        }
        else {
            urlWithParameters += "_page=2";
            String nextUrl = urlWithParameters;
            nextlink.setRelation(Constants.LINK_NEXT);
            nextlink.setUrl(nextUrl);
            links.add(nextlink);
        }

        bundle.setLink(links);

        return bundle;
    }

    @Search(compartmentName = "Composition")
    public List<Composition> findNotes(@IdParam IdType theId) {

        List<Composition> results = service.getTiuNotes(theId.getIdPart());

        if(results.isEmpty()) {
            String message = "No compositions/notes found for patient: " + theId.getIdPart();
            LOG.info(message);
            throw new ResourceNotFoundException(message);
        }

        return results;
    }

    private static Date getDateFromOptionalParameter(DateParam dateParam) {
        Date startDate;

        if(dateParam == null || dateParam.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, DEFAULT_MONTHS);
            startDate = cal.getTime();
        } else {
            startDate = dateParam.getValue();
        }

        return startDate;
    }


    @Operation(name="$everything", idempotent=true)
    public Bundle patientInstanceOperation(@IdParam IdType theId, RequestDetails request) {

        Bundle result = createBundle(String.format("%s/%s", request.getFhirServerBase(), request.getRequestPath()));

        List<DomainResource> resources = service.getEverything(theId.getIdPart());

        for(Resource item : resources){
            result.addEntry().setResource(item);
        }

        result.setTotal(resources.size());

        return result;
    }

    private static Bundle createBundle(String url) {
        Bundle result = new Bundle();
        result.setType(Bundle.BundleType.SEARCHSET);

        Meta lastUpdate = new Meta();
        lastUpdate.setLastUpdated(new Date());
        result.setMeta(lastUpdate);

        Bundle.BundleLinkComponent link = new Bundle.BundleLinkComponent();
        link.setRelation("self");
        link.setUrl(url);
        List<Bundle.BundleLinkComponent> links = new ArrayList<>();
        links.add(link);
        result.setLink(links);

        return result;
    }
}
