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
package com.healthconcourse.vista.fhir.api.data;

import java.util.ArrayList;
import java.util.List;

public class Provider {

    private String vistaId;
    private String name;
    private String role;
    private String encounter;
    private List<String> encounters;
    private boolean isPrimary;

    public Provider() {
        encounters = new ArrayList<>();
        vistaId = "";
    }

    public Provider(String vistaId, String name, String role, List<String> encounters, boolean isPrimary) {
        this.name = name;
        this.role = role;
        this.encounters = encounters;
        this.isPrimary = isPrimary;
        this.vistaId = vistaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<String> encounters) {
        this.encounters = encounters;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getVistaId() {
        return vistaId;
    }

    public void setVistaId(String vistaId) {
        this.vistaId = vistaId;
    }

    public String getEncounter() {
        return encounter;
    }

    public void setEncounter(String encounter) {
        this.encounter = encounter;
    }
}
