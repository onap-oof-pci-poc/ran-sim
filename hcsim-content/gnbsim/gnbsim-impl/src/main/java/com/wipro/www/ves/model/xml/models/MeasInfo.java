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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"measInfoId", "job", "granPeriod", "repPeriod", "measType", "measValue"})
public class MeasInfo {
    private String measInfoId;
    private Job job;
    private GranularityPeriod granPeriod;
    private ReportingPeriod repPeriod;
    private List<MeasType> measType;
    private List<MeasValue> measValue;

    public MeasInfo(String measInfoId, Job job, GranularityPeriod granPeriod, ReportingPeriod repPeriod,
            List<MeasType> measType, List<MeasValue> measValue) {
        super();
        this.measInfoId = measInfoId;
        this.job = job;
        this.granPeriod = granPeriod;
        this.repPeriod = repPeriod;
        this.measType = measType;
        this.measValue = measValue;
    }

    public MeasInfo() {
    }

    @XmlAttribute
    public String getMeasInfoId() {
        return measInfoId;
    }

    public void setMeasInfoId(String measInfoId) {
        this.measInfoId = measInfoId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public GranularityPeriod getGranPeriod() {
        return granPeriod;
    }

    public void setGranPeriod(GranularityPeriod granPeriod) {
        this.granPeriod = granPeriod;
    }

    public ReportingPeriod getRepPeriod() {
        return repPeriod;
    }

    public void setRepPeriod(ReportingPeriod repPeriod) {
        this.repPeriod = repPeriod;
    }

    public List<MeasType> getMeasType() {
        return measType;
    }

    public void setMeasType(List<MeasType> measType) {
        this.measType = measType;
    }

    public List<MeasValue> getMeasValue() {
        return measValue;
    }

    public void setMeasValue(List<MeasValue> measValue) {
        this.measValue = measValue;
    }

    @Override
    public String toString() {
        return "MeasInfo [measInfoId=" + measInfoId + ", job=" + job + ", granPeriod=" + granPeriod + ", repPeriod="
                + repPeriod + ", measType=" + measType + ", measValue=" + measValue + "]";
    }
}
