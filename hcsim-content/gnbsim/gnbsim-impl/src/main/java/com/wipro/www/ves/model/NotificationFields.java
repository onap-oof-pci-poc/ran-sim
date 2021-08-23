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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"changeIdentifier", "changeType", "notificationFieldsVersion", "arrayOfNamedHashMap"})

public class NotificationFields {

    @JsonProperty("changeIdentifier")
    private String changeIdentifier;
    @JsonProperty("changeType")
    private String changeType;
    @JsonProperty("notificationFieldsVersion")
    private String notificationFieldsVersion;
    @JsonProperty("arrayOfNamedHashMap")
    private List<NamedHashMap> arrayOfNamedHashMap;

    @JsonProperty("changeIdentifier")
    public String getChangeIdentifier() {
        return changeIdentifier;
    }

    @JsonProperty("changeIdentifier")
    public void setChangeIdentifier(String changeIdentifier) {
        this.changeIdentifier = changeIdentifier;
    }

    @JsonProperty("changeType")
    public String getChangeType() {
        return changeType;
    }

    @JsonProperty("changeType")
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    @JsonProperty("notificationFieldsVersion")
    public String getNotificationFieldsVersion() {
        return notificationFieldsVersion;
    }

    @JsonProperty("notificationFieldsVersion")
    public void setNotificationFieldsVersion(String notificationFieldsVersion) {
        this.notificationFieldsVersion = notificationFieldsVersion;
    }

    @JsonProperty("arrayOfNamedHashMap")
    public List<NamedHashMap> getArrayOfNamedHashMap() {
        return arrayOfNamedHashMap;
    }

    @JsonProperty("arrayOfNamedHashMap")
    public void setArrayOfNamedHashMap(List<NamedHashMap> arrayOfNamedHashMap) {
        this.arrayOfNamedHashMap = arrayOfNamedHashMap;
    }

    @Override
    public String toString() {
        return "NotificationFields [arrayOfNamedHashMap=" + arrayOfNamedHashMap + "]";
    }
}
