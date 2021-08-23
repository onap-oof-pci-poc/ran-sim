/*
 * Copyright (c) 2016 Cisco and/or its affiliates.
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

public class AllocationUpdateMessage {

    private String pnfName;
    private String cell;
    private String slice;
    private String timeInterval;
    private BigDecimal bwAllocation;

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getSlice() {
        return slice;
    }

    public void setSlice(String slice) {
        this.slice = slice;
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
}
