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

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DUAttributes {

    private int nRPCI;
    private String operationalState;
    private String cellState;

    @JsonProperty("nRPCI")
    public int getNRPCI() {
        return nRPCI;
    }

    public void setNRPCI(int nRPCI) {
        this.nRPCI = nRPCI;
    }

    public String getOperationalState() {
        return operationalState;
    }

    public void setOperationalState(String operationalState) {
        this.operationalState = operationalState;
    }

    public String getCellState() {
        return cellState;
    }

    public void setCellState(String cellState) {
        this.cellState = cellState;
    }

    public DUAttributes() {

    }

    public DUAttributes(int nRPCI) {
        super();
        this.nRPCI = nRPCI;
    }

    @Override
    public String toString() {
        return "Attributes [ nRPCI=" + nRPCI + "]";
    }
}
