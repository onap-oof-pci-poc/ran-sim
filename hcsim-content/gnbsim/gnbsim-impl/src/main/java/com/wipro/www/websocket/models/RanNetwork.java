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
public class RanNetwork {

    @JsonProperty("NearRTRIC")
    private List<nearRTRIC> nearRTRICInstance;

    public List<nearRTRIC> getNearRTRIC() {
        return nearRTRICInstance;
    }

    public void setNearRTRIC(List<nearRTRIC> nearRTRICInstance) {
        this.nearRTRICInstance = nearRTRICInstance;
    }

    public RanNetwork() {

    }

}
