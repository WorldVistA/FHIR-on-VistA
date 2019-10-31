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
package com.healthconcourse.vista.fhir.api.service;

import com.healthconcourse.vista.fhir.api.parser.CareTeamParser;
import com.healthconcourse.vista.fhir.api.vista.VistaData;
import org.hl7.fhir.r4.model.CareTeam;

import java.util.List;

public class VistaCareTeamService implements CareTeamService {
    private VistaData service;

    public VistaCareTeamService(VistaData vista) {
        this.service = vista;
    }

    @Override
    public List<CareTeam> getCareTeamByName(String name) {
        String httpJson = service.getCareTeamByHame(name);

        CareTeamParser parser = new CareTeamParser();

        return parser.parseList(httpJson);
    }

    @Override
    public List<CareTeam> getAllCareTeams() {
        String httpJson = service.getAllCareTeams();

        CareTeamParser parser = new CareTeamParser();

        return parser.parseList(httpJson);
    }
}
