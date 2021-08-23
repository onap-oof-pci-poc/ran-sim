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

package com.wipro.www.ves.model.xml.models;

import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MeasData {
    private ManagedElement managedElement;
    private List<MeasInfo> measInfo;

    public MeasData() {
    }

    public MeasData(ManagedElement managedElement, List<MeasInfo> measInfo) {
        super();
        this.managedElement = managedElement;
        this.measInfo = measInfo;
    }

    public ManagedElement getManagedElement() {
        return managedElement;
    }

    public void setManagedElement(ManagedElement managedElement) {
        this.managedElement = managedElement;
    }

    public List<MeasInfo> getMeasInfo() {
        return measInfo;
    }

    public void setMeasInfo(List<MeasInfo> measInfo) {
        this.measInfo = measInfo;
    }

    @Override
    public String toString() {
        return "MeasData [managedElement=" + managedElement + ", measInfo=" + measInfo + "]";
    }

}
