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

package org.onap.ransim.websocket.model;

public class EventPm {

    private CommonEventHeaderPm commonEventHeader;
    private Measurement measurement;

    public CommonEventHeaderPm getCommonEventHeader() {
        return commonEventHeader;
    }

    public void setCommonEventHeader(CommonEventHeaderPm commonEventHeader) {
        this.commonEventHeader = commonEventHeader;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

}
