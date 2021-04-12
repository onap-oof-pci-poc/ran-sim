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

package com.wipro.www.websocket.models;

import java.util.HashMap;
import java.util.Map;

public class CommonEventHeaderPm {

    private String version;
    private String vesEventListenerVersion;
    private String domain;
    private String eventName;
    private String eventId;
    private Double lastEpochMicrosec;
    private String priority;
    private String reportingEntityName;
    private Long sequence;
    private String sourceName;
    private Double startEpochMicrosec;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Double getLastEpochMicrosec() {
        return lastEpochMicrosec;
    }

    public void setLastEpochMicrosec(Double lastEpochMicrosec) {
        this.lastEpochMicrosec = lastEpochMicrosec;
    }


    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReportingEntityName() {
        return reportingEntityName;
    }

    public void setReportingEntityName(String reportingEntityName) {
        this.reportingEntityName = reportingEntityName;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Double getStartEpochMicrosec() {
        return startEpochMicrosec;
    }

    public void setStartEpochMicrosec(Double startEpochMicrosec) {
        this.startEpochMicrosec = startEpochMicrosec;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVesEventListenerVersion() {
        return vesEventListenerVersion;
    }

    public void setVesEventListenerVersion(String vesEventListenerVersion) {
        this.vesEventListenerVersion = vesEventListenerVersion;
    }
}
