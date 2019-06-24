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

package org.onap.ransim.rest.api.models;

public class CellInfo {
    
    private String networkId;
    private String nodeId;
    private long physicalCellId;
    private String pnfName;
    private int sectorNumber;
    private String latitude;
    private String longitude;
    
    /**
     * A constructor for CellInfo.
     *
     * @param networkId
     *            network Id of the cell
     * @param nodeId
     *            node Id of the cell
     * @param physicalCellId
     *            PCI number of the cell
     * @param pnfName
     *            netconf server id
     * @param sectorNumber
     *            sector number for the cell
     * @param latitude
     *            latitude of the node
     * @param longitude
     *            longitude of the node
     */
    public CellInfo(String networkId, String nodeId, long physicalCellId,
            String pnfName, int sectorNumber, String latitude, String longitude) {
        super();
        this.networkId = networkId;
        this.nodeId = nodeId;
        this.physicalCellId = physicalCellId;
        this.pnfName = pnfName;
        this.sectorNumber = sectorNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public CellInfo() {
        
    }
    
    public String getNetworkId() {
        return networkId;
    }
    
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }
    
    public String getNodeId() {
        return nodeId;
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    public long getPhysicalCellId() {
        return physicalCellId;
    }
    
    public void setPhysicalCellId(long physicalCellId) {
        this.physicalCellId = physicalCellId;
    }
    
    public String getPnfName() {
        return pnfName;
    }
    
    public void setPnfName(String pnfName) {
        this.pnfName = pnfName;
    }
    
    public int getSectorNumber() {
        return sectorNumber;
    }
    
    public void setSectorNumber(int sectorNumber) {
        this.sectorNumber = sectorNumber;
    }
    
    public String getLatitude() {
        return latitude;
    }
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getLongitude() {
        return longitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
}
