/*
 * Copyright (C) 2021 Wipro Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wipro.www.websocket.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NearRTRIC {

    String idNearRTRIC;
    public Attributes attributes;
    String locationName;
    String nearRTRICgNBId;
    String managedBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("GNBDUFunction")
    public List<GNBDUFunction> gNBDUFunction;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("GNBCUUPFunction")
    public List<GNBCUUPFunction> gNBCUUPFunction;

    public String getIdNearRTRIC() {
        return idNearRTRIC;
    }

    public void setIdNearRTRIC(String idNearRTRIC) {
        this.idNearRTRIC = idNearRTRIC;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<GNBDUFunction> getgNBDUFunction() {
        return gNBDUFunction;
    }

    public void setgNBDUFunction(List<GNBDUFunction> gNBDUFunction) {
        this.gNBDUFunction = gNBDUFunction;
    }

    public List<GNBCUUPFunction> getgNBCUUPFunction() {
        return gNBCUUPFunction;
    }

    public void setgNBCUUPFunction(List<GNBCUUPFunction> gNBCUUPFunction) {
        this.gNBCUUPFunction = gNBCUUPFunction;
    }

    public NearRTRIC() {

    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getNearRTRICgNBId() {
        return nearRTRICgNBId;
    }

    public void setNearRTRICgNBId(String nearRTRICgNBId) {
        this.nearRTRICgNBId = nearRTRICgNBId;
    }

    public String getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(String managedBy) {
        this.managedBy = managedBy;
    }
}
