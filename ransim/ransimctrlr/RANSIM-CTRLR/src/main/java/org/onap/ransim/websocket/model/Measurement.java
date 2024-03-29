/*-
 * ============LICENSE_START=======================================================
 * Ran Simulator Controller
 * ================================================================================
 * Copyright (C) 2018 Wipro Limited.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.ransim.websocket.model;

import java.util.List;

public class Measurement {
    
    private int measurementInterval;
    private List<AdditionalMeasurements> additionalMeasurements;
    
    public int getMeasurementInterval() {
        return measurementInterval;
    }
    
    public void setMeasurementInterval(int measurementInterval) {
        this.measurementInterval = measurementInterval;
    }
    
    public List<AdditionalMeasurements> getAdditionalMeasurements() {
        return additionalMeasurements;
    }
    
    public void setAdditionalMeasurements(
            List<AdditionalMeasurements> additionalMeasurements) {
        this.additionalMeasurements = additionalMeasurements;
    }
    
}
