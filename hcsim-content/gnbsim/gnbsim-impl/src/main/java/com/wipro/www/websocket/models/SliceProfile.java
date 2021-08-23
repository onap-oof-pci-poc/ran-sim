/*
 * Copyright (C) 2021 Wipro Limited.
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

public class SliceProfile {
    private static final long serialVersionUID = 1L;

    private String sliceProfileId;
    private String coverageareaList;
    private String sNSSAI;
    private String pLMNIdList;
    private Integer maxNumberofUEs;
    private Integer latency;
    private Integer dLThptPerSlice;
    private Integer uLThptPerSlice;
    private Integer maxNumberofConns;
    private String resourcesharinglevel;
    private String uemobilitylevel;
    private String rannfnssiid;

    public SliceProfile() {

    }

    public String getSliceProfileId() {
        return sliceProfileId;
    }

    public void setSliceProfileId(String sliceProfileId) {
        this.sliceProfileId = sliceProfileId;
    }

    public String getCoverageareaList() {
        return coverageareaList;
    }

    public void setCoverageareaList(String coverageareaList) {
        this.coverageareaList = coverageareaList;
    }

    public String getsNSSAI() {
        return sNSSAI;
    }

    public void setsNSSAI(String sNSSAI) {
        this.sNSSAI = sNSSAI;
    }

    public String getpLMNIdList() {
        return pLMNIdList;
    }

    public void setpLMNIdList(String pLMNIdList) {
        this.pLMNIdList = pLMNIdList;
    }

    public Integer getMaxNumberofUEs() {
        return maxNumberofUEs;
    }

    public void setMaxNumberofUEs(Integer maxNumberofUEs) {
        this.maxNumberofUEs = maxNumberofUEs;
    }

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public Integer getdLThptPerSlice() {
        return dLThptPerSlice;
    }

    public void setdLThptPerSlice(Integer dLThptPerSlice) {
        this.dLThptPerSlice = dLThptPerSlice;
    }

    public Integer getuLThptPerSlice() {
        return uLThptPerSlice;
    }

    public void setuLThptPerSlice(Integer uLThptPerSlice) {
        this.uLThptPerSlice = uLThptPerSlice;
    }

    public Integer getMaxNumberofConns() {
        return maxNumberofConns;
    }

    public void setMaxNumberofConns(Integer maxNumberofConns) {
        this.maxNumberofConns = maxNumberofConns;
    }

    public String getResourcesharinglevel() {
        return resourcesharinglevel;
    }

    public void setResourcesharinglevel(String resourcesharinglevel) {
        this.resourcesharinglevel = resourcesharinglevel;
    }

    public String getUemobilitylevel() {
        return uemobilitylevel;
    }

    public void setUemobilitylevel(String uemobilitylevel) {
        this.uemobilitylevel = uemobilitylevel;
    }

    public String getRannfnssiid() {
        return rannfnssiid;
    }

    public void setRannfnssiid(String rannfnssiid) {
        this.rannfnssiid = rannfnssiid;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
