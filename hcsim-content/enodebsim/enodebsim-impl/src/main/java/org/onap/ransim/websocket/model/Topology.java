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

import java.util.List;

public class Topology {

    private String pnfName;

    private long pciId;

    private String cellId;

    private List<Neighbor> neighborList;

    public long getPciId() {
        return pciId;
    }

    public void setPciId(long pciId) {
        this.pciId = pciId;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public List<Neighbor> getNeighborList() {
        return neighborList;
    }

    public void setNeighborList(List<Neighbor> neighborList) {
        this.neighborList = neighborList;
    }

    @Override
    public String toString() {
        return "Topology [PnfName = " + pnfName + ", PciId = " + pciId
                + ", cellId = " + cellId + ", neighborList = " + neighborList
                + "]";
    }

    public Topology(String pnfName, long pciId, String cellId,
            List<Neighbor> neighborList) {
        super();
        this.pnfName = pnfName;
        this.pciId = pciId;
        this.cellId = cellId;
        this.neighborList = neighborList;
    }

    public Topology() {
    }

    public String getPnfName() {
        return pnfName;
    }

    public void setPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

}
