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

import java.util.List;

public class PLMNInfoModel {
    private String pLMNId;
    private String snssai;
    private String status;
    private String gnbType;
    private String gnbId;
    private int nrCellId;
    private String nearrtricid;
    private List<ConfigData> configData;

    public PLMNInfoModel() {

    }

    public PLMNInfoModel(String pLMNId, String snssai, String status, String gnbType, String gnbId, int nrCellId,
            String nearrtricid, List<ConfigData> configData) {
        super();
        this.pLMNId = pLMNId;
        this.snssai = snssai;
        this.status = status;
        this.gnbType = gnbType;
        this.gnbId = gnbId;
        this.nrCellId = nrCellId;
        this.nearrtricid = nearrtricid;
        this.configData = configData;
    }

    public String getpLMNId() {
        return pLMNId;
    }

    public void setpLMNId(String pLMNId) {
        this.pLMNId = pLMNId;
    }

    public String getSnssai() {
        return snssai;
    }

    public void setSnssai(String snssai) {
        this.snssai = snssai;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGnbType() {
        return gnbType;
    }

    public void setGnbType(String gnbType) {
        this.gnbType = gnbType;
    }

    public String getGnbId() {
        return gnbId;
    }

    public void setGnbId(String gnbId) {
        this.gnbId = gnbId;
    }

    public int getNrCellId() {
        return nrCellId;
    }

    public void setNrCellId(int nrCellId) {
        this.nrCellId = nrCellId;
    }

    public String getNearrtricid() {
        return nearrtricid;
    }

    public void setNearrtricid(String nearrtricid) {
        this.nearrtricid = nearrtricid;
    }

    public List<ConfigData> getConfigData() {
        return configData;
    }

    public void setConfigData(List<ConfigData> configData) {
        this.configData = configData;
    }

    @Override
    public String toString() {
        return "PLMNInfoModel [pLMNId=" + pLMNId + ", snssai=" + snssai + ", status=" + status + ", gnbType=" + gnbType
                + ", gnbId=" + gnbId + ", nrCellId=" + nrCellId + ", nearrtricid=" + nearrtricid + ", configData="
                + configData + "]";
    }
}
