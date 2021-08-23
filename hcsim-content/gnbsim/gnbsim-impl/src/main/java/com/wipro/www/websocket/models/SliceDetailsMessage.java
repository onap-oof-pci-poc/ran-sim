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

import java.math.BigDecimal;
import java.util.*;

public class SliceDetailsMessage {

    private String pnfName;
    private String cellAlias;
    private String sliceName;
    private String timeInterval;
    private BigDecimal bwAllocation;
    private List<String> nssai;
    private boolean deleteFlag;

    public List<String> getNssai() {
        return nssai;
    }

    public void setNssai(List<String> nssai) {
        this.nssai = nssai;
    }

    public String getCellAlias() {
        return cellAlias;
    }

    public void setCellAlias(String cellAlias) {
        this.cellAlias = cellAlias;
    }

    public String getSliceName() {
        return sliceName;
    }

    public void setSliceName(String sliceName) {
        this.sliceName = sliceName;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public BigDecimal getBwAllocation() {
        return bwAllocation;
    }

    public void setBwAllocation(BigDecimal bwAllocation) {
        this.bwAllocation = bwAllocation;
    }

    public String getPnfName() {
        return pnfName;
    }

    public void setPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

    public void setdeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public boolean getdeleteFlag() {
        return deleteFlag;
    }

}
