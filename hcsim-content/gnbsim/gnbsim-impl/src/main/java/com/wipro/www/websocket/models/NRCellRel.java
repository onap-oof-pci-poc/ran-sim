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
public class NRCellRel {

    private String idNRCellCU;
    private String idNRCellRelation;
    private NRCellRelAttributes attributes;

    public String getIdNRCellCU() {
	    return idNRCellCU;
    }

    public void setIdNRCellCU(String idNRCellCU) {
	    this.idNRCellCU = idNRCellCU;
    }

    public String getIdNRCellRelation() {
        return idNRCellRelation;
    }

    public void setIdNRCellRelation(String idNRCellRelation) {
        this.idNRCellRelation = idNRCellRelation;
    }

    @JsonProperty("attributes")
    public NRCellRelAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(NRCellRelAttributes attributes) {
        this.attributes = attributes;
    }

    public NRCellRel(String idNRCellCU, String idNRCellRelation, NRCellRelAttributes attributes) {
        super();
	this.idNRCellCU = idNRCellCU;
	this.idNRCellRelation = idNRCellRelation; 
        this.attributes = attributes;
    }

    public NRCellRel() {
    }

    @Override
    public String toString() {
        return "NRCellRel [ idNRCellCU=" + idNRCellCU +  ", idNRCellRelation=" + idNRCellRelation + ", Attributes="+ attributes + "]";
    }
}
