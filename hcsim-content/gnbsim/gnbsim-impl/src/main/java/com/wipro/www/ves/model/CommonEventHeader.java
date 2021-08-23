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

package com.wipro.www.ves.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"version", "vesEventListenerVersion", "domain", "eventName", "eventId", "lastEpochMicrosec",
        "priority", "reportingEntityName", "sequence", "sourceName", "startEpochMicrosec", "timeZoneOffset"})
public class CommonEventHeader {

    @JsonProperty("version")
    private String version;
    @JsonProperty("vesEventListenerVersion")
    private String vesEventListenerVersion;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("eventName")
    private String eventName;
    @JsonProperty("eventId")
    private String eventId;
    @JsonProperty("lastEpochMicrosec")
    private Double lastEpochMicrosec;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("reportingEntityName")
    private String reportingEntityName;
    @JsonProperty("sequence")
    private Long sequence;
    @JsonProperty("sourceName")
    private String sourceName;
    @JsonProperty("startEpochMicrosec")
    private Double startEpochMicrosec;
    @JsonProperty("timeZoneOffset")
    private String timeZoneOffset;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("domain")
    public String getDomain() {
        return domain;
    }

    @JsonProperty("domain")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @JsonProperty("eventId")
    public String getEventId() {
        return eventId;
    }

    @JsonProperty("eventId")
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @JsonProperty("eventName")
    public String getEventName() {
        return eventName;
    }

    @JsonProperty("eventName")
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @JsonProperty("lastEpochMicrosec")
    public Double getLastEpochMicrosec() {
        return lastEpochMicrosec;
    }

    @JsonProperty("lastEpochMicrosec")
    public void setLastEpochMicrosec(Double lastEpochMicrosec) {
        this.lastEpochMicrosec = lastEpochMicrosec;
    }

    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @JsonProperty("reportingEntityName")
    public String getReportingEntityName() {
        return reportingEntityName;
    }

    @JsonProperty("reportingEntityName")
    public void setReportingEntityName(String reportingEntityName) {
        this.reportingEntityName = reportingEntityName;
    }

    @JsonProperty("sequence")
    public Long getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("sourceName")
    public String getSourceName() {
        return sourceName;
    }

    @JsonProperty("sourceName")
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @JsonProperty("startEpochMicrosec")
    public Double getStartEpochMicrosec() {
        return startEpochMicrosec;
    }

    @JsonProperty("startEpochMicrosec")
    public void setStartEpochMicrosec(Double startEpochMicrosec) {
        this.startEpochMicrosec = startEpochMicrosec;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("vesEventListenerVersion")
    public String getVesEventListenerVersion() {
        return vesEventListenerVersion;
    }

    @JsonProperty("vesEventListenerVersion")
    public void setVesEventListenerVersion(String vesEventListenerVersion) {
        this.vesEventListenerVersion = vesEventListenerVersion;
    }

    @JsonProperty("timeZoneOffset")
    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}
