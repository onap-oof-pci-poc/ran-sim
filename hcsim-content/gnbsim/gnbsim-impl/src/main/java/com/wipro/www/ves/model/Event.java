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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commonEventHeader",
    "notificationFields"
})

public class Event {

    @JsonProperty("commonEventHeader")
    private CommonEventHeader commonEventHeader;
    @JsonProperty("notificationFields")
    private NotificationFields notificationFields;

    @JsonProperty("commonEventHeader")
    public CommonEventHeader getCommonEventHeader() {
        return commonEventHeader;
    }

    @JsonProperty("commonEventHeader")
    public void setCommonEventHeader(CommonEventHeader commonEventHeader) {
        this.commonEventHeader = commonEventHeader;
    }

    @JsonProperty("notificationFields")
    public NotificationFields getNotificationFields() {
        return notificationFields;
    }

    @JsonProperty("notificationFields")
    public void setNotificationFields(NotificationFields notificationFields) {
        this.notificationFields = notificationFields;
    }

}
