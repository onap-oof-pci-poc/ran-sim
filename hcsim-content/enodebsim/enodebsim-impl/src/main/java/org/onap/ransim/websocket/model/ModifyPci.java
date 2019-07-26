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

public class ModifyPci {

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

    @Override
    public String toString() {
        return "ModifyPci [PnfName = " + pnfName + ", PciId = " + pciId
                + ", cellId = " + cellId + "neighborList:" + neighborList + "]";
    }

    public ModifyPci() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Modify Pci value from sdnr.
     *
     * @param pnfName
     *            server id name
     * @param pciId
     *            pci number
     * @param cellId
     *            node id for the cell
     * @param neighborList
     *            neighbor list for the cell
     */
    public ModifyPci(String pnfName, long pciId, String cellId,
            List<Neighbor> neighborList) {
        super();
        this.pnfName = pnfName;
        this.pciId = pciId;
        this.cellId = cellId;
        this.neighborList = neighborList;
    }

    public String getPnfName() {
        return pnfName;
    }

    public void setPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

    public List<Neighbor> getNeighborList() {
        return neighborList;
    }

    public void setNeighborList(List<Neighbor> neighborList) {
        this.neighborList = neighborList;
    }

    /**
     * Checks if all the parameters are set correctly .
     *
     * @return returns true if the parameter are set correctly
     */
    public boolean isAllSet() {
        if (pnfName == null || pnfName.trim().equals("")) {
            return false;
        }
        if (cellId == null || cellId.trim().equals("")) {
            return false;
        }
        return true;
    }

}
