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

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "alarmCondition", "eventSeverity", "eventSourceType",
    "faultFieldsVersion", "specificProblem", "vfStatus",
    "alarmAdditionalInformation", "alarmInterfaceA", "eventCategory" })
public class FaultFields {

    @JsonProperty("alarmCondition")
    private String alarmCondition;
    @JsonProperty("eventSeverity")
    private String eventSeverity;
    @JsonProperty("eventSourceType")
    private String eventSourceType;
    @JsonProperty("faultFieldsVersion")
    private String faultFieldsVersion;
    @JsonProperty("specificProblem")
    private String specificProblem;
    @JsonProperty("vfStatus")
    private String vfStatus;
    @JsonProperty("alarmAdditionalInformation")
    private Map<String, String> alarmAdditionalInformation = null;

    @JsonProperty("alarmCondition")
    public String getAlarmCondition() {
        return alarmCondition;
    }

    @JsonProperty("alarmCondition")
    public void setAlarmCondition(String alarmCondition) {
        this.alarmCondition = alarmCondition;
    }

    @JsonProperty("eventSeverity")
    public String getEventSeverity() {
        return eventSeverity;
    }

    @JsonProperty("eventSeverity")
    public void setEventSeverity(String eventSeverity) {
        this.eventSeverity = eventSeverity;
    }

    @JsonProperty("eventSourceType")
    public String getEventSourceType() {
        return eventSourceType;
    }

    @JsonProperty("eventSourceType")
    public void setEventSourceType(String eventSourceType) {
        this.eventSourceType = eventSourceType;
    }

    @JsonProperty("faultFieldsVersion")
    public String getFaultFieldsVersion() {
        return faultFieldsVersion;
    }

    @JsonProperty("faultFieldsVersion")
    public void setFaultFieldsVersion(String faultFieldsVersion) {
        this.faultFieldsVersion = faultFieldsVersion;
    }

    @JsonProperty("specificProblem")
    public String getSpecificProblem() {
        return specificProblem;
    }

    @JsonProperty("specificProblem")
    public void setSpecificProblem(String specificProblem) {
        this.specificProblem = specificProblem;
    }

    @JsonProperty("vfStatus")
    public String getVfStatus() {
        return vfStatus;
    }

    @JsonProperty("vfStatus")
    public void setVfStatus(String vfStatus) {
        this.vfStatus = vfStatus;
    }

    @JsonProperty("alarmAdditionalInformation")
    public Map<String, String> getAlarmAdditionalInformation() {
        return alarmAdditionalInformation;
    }

    @JsonProperty("alarmAdditionalInformation")
    public void setAlarmAdditionalInformation(
            Map<String, String> alarmAdditionalInformation) {
        this.alarmAdditionalInformation = alarmAdditionalInformation;
    }

}
