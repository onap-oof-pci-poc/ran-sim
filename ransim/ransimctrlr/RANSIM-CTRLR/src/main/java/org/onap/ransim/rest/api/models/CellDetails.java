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

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CellDetails")
public class CellDetails {

    private String networkId;
    private String nodeId;
    private long physicalCellId;
    private String nodeName;
    private String nodeType;
    private boolean pciCollisionDetected;
    private boolean pciConfusionDetected;
    private Set<String> cellsAffectedDueToConfusion;
    private Set<String> cellsCausingConfusion;
    private int gridX;
    private int gridY;
    private float screenX;
    private float screenY;
    private String latitude;
    private String longitude;
    private String serverId;
    private int sectorNumber = 0;
    private String color;

    /**
     * A constructor for CellDetails( Database to store cell details).
     *
     * @param networkId
     *            network Id of the cell
     * @param nodeId
     *            node Id of the cell
     * @param physicalCellId
     *            PCI number of the cell
     * @param nodeName
     *            node name
     * @param nodeType
     *            node type based on the cluster
     * @param pciCollisionDetected
     *            Checks if the cell has collision
     * @param pciConfusionDetected
     *            Checks if the cell has confusion
     * @param cellsAffectedDueToConfusion
     *            List of cells it causes confusion to
     * @param cellsCausingConfusion
     *            List of cells that causes confusion to the cell
     * @param gridX
     *            --
     * @param gridY
     *            --
     * @param screenX
     *            x coordinate of the cell in the GUI
     * @param screenY
     *            y coordinate of the cell in the GUI
     * @param latitude
     *            latitude of the cell node
     * @param longitude
     *            longitude of the cell node
     * @param serverId
     *            server Id of the netconf server it belongs to
     * @param sectorNumber
     *            sector number of the cell
     * @param color
     *            color of the cell in the GUI
     */
    public CellDetails(String networkId, String nodeId, long physicalCellId, String nodeName, String nodeType,
            boolean pciCollisionDetected, boolean pciConfusionDetected, Set<String> cellsCausingConfusion,
            Set<String> cellsAffectedDueToConfusion, int gridX, int gridY, float screenX, float screenY,
            String latitude, String longitude, String serverId, int sectorNumber, String color) {
        super();
        this.networkId = networkId;
        this.nodeId = nodeId;
        this.physicalCellId = physicalCellId;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.pciCollisionDetected = pciCollisionDetected;
        this.pciConfusionDetected = pciConfusionDetected;
        this.cellsAffectedDueToConfusion = cellsAffectedDueToConfusion;
        this.cellsCausingConfusion = cellsCausingConfusion;
        this.gridX = gridX;
        this.gridY = gridY;
        this.screenX = screenX;
        this.screenY = screenY;
        this.latitude = latitude;
        this.longitude = longitude;
        this.serverId = serverId;
        this.sectorNumber = sectorNumber;
        this.color = color;
    }

    public CellDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    @Id
    @Column(name = "nodeId", unique = true, nullable = false, length = 50)
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public boolean isPciCollisionDetected() {
        return pciCollisionDetected;
    }

    public void setPciCollisionDetected(boolean pciCollisionDetected) {
        this.pciCollisionDetected = pciCollisionDetected;
    }

    public boolean isPciConfusionDetected() {
        return pciConfusionDetected;
    }

    public void setPciConfusionDetected(boolean pciConfusionDetected) {
        this.pciConfusionDetected = pciConfusionDetected;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public float getScreenX() {
        return screenX;
    }

    public void setScreenX(float screenX) {
        this.screenX = screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public void setScreenY(float screenY) {
        this.screenY = screenY;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getSectorNumber() {
        return sectorNumber;
    }

    public void setSectorNumber(int sectorNumber) {
        this.sectorNumber = sectorNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Set<String> getCellsAffectedDueToConfusion() {
        return cellsAffectedDueToConfusion;
    }

    public void setCellsAffectedDueToConfusion(Set<String> cellsAffectedDueToConfusion) {
        this.cellsAffectedDueToConfusion = cellsAffectedDueToConfusion;
    }

    public Set<String> getCellsCausingConfusion() {
        return cellsCausingConfusion;
    }

    public void setCellsCausingConfusion(Set<String> cellsCausingConfusion) {
        this.cellsCausingConfusion = cellsCausingConfusion;
    }

    @Override
    public String toString() {
        return "Cell Details [networkId=" + networkId + ", nodeId=" + nodeId + ", physicalCellId=" + physicalCellId
                + ", nodeName=" + nodeName + ", nodeType=" + nodeType + ", pciCollisionDetected=" + pciCollisionDetected
                + ", pciConfusionDetected=" + pciConfusionDetected + "]";
    }
}
