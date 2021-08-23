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
public class GNBDUFunction {

    private String idGNBDUFunction;

    private Attributes attributes;

    public List<NRCellDU> nRCellDU;

    public String getIdGNBDUFunction() {
        return idGNBDUFunction;
    }

    public void setIdGNBDUFunction(String idGNBDUFunction) {
        this.idGNBDUFunction = idGNBDUFunction;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<NRCellDU> getnRCellDU() {
        return nRCellDU;
    }

    public void setnRCellDU(List<NRCellDU> nRCellDU) {
        this.nRCellDU = nRCellDU;
    }

    public GNBDUFunction() {

    }
}
