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
public class NRCellDU {

    private String idNRCellDU;

    private DUAttributes attributes;

    public String getIdNRCellDU() {
        return idNRCellDU;
    }

    public void setIdNRCellDU(String idNRCellDU) {
        this.idNRCellDU = idNRCellDU;
    }

    @JsonProperty("attributes")
    public DUAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(DUAttributes attributes) {
        this.attributes = attributes;
    }

    public NRCellDU(String idNRCellDU, DUAttributes attributes) {
        super();
	this.idNRCellDU = idNRCellDU;
        this.attributes = attributes;
    }

    public NRCellDU() {

    }

    @Override
    public String toString() {
        return "NRCellDU [idNRCellDU=" + idNRCellDU + ", Attributes=" + attributes + "]";
    }

}
