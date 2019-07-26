/*
 * Copyright (C) 2018 Wipro Limited.
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

package org.onap.ransim.ves.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commonEventHeader",
    "faultFields",
    "measurement"
})

public class Event {

    @JsonProperty("commonEventHeader")
    private CommonEventHeader commonEventHeader;
    @JsonProperty("faultFields")
    private FaultFields faultFields;
    @JsonProperty("measurementFields")
    private Measurement measurementFields;

    @JsonProperty("commonEventHeader")
    public CommonEventHeader getCommonEventHeader() {
        return commonEventHeader;
    }

    @JsonProperty("commonEventHeader")
    public void setCommonEventHeader(CommonEventHeader commonEventHeader) {
        this.commonEventHeader = commonEventHeader;
    }

    @JsonProperty("faultFields")
    public FaultFields getFaultFields() {
        return faultFields;
    }

    @JsonProperty("faultFields")
    public void setFaultFields(FaultFields faultFields) {
        this.faultFields = faultFields;
    }

    @JsonProperty("measurementFields")
    public Measurement getMeasurement() {
        return measurementFields;
    }

    @JsonProperty("measurementFields")
    public void setMeasurement(Measurement measurementFields) {
        this.measurementFields = measurementFields;
    }

}
