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

@JsonInclude(JsonInclude.Include.NON_NULL)
public class nearRTRIC {

    private String idNearRTRIC;

    private Attributes attributes;

    private List<GNBDUFunction> gNBDUFunction;

    private List<GNBCUUPFunction> gNBCUUPFunction;

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

    public nearRTRIC() {

    }
}
