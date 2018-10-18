/* ============LICENSE_START=======================================================
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

public class GetTopology {

    private CellDetails currentCell;
    private int gridX;
    private int gridY;
    private String nodeId;
    private long physicalCellId;

    /**
     * A constructor GetTopology.
     *
     * @param currentCell
     *            current cell details
     * @param gridX
     *            x coordinate value for the cell
     * @param gridY
     *            y coordinate value for the cell
     * @param nodeId
     *            node id of the cell
     * @param physicalCellId
     *            pci value for the cell
     */
    public GetTopology(CellDetails currentCell, int gridX, int gridY, String nodeId, long physicalCellId) {
        super();
        this.currentCell = currentCell;
        this.gridX = gridX;
        this.gridY = gridY;
        this.nodeId = nodeId;
        this.physicalCellId = physicalCellId;
    }

    public GetTopology() {

    }

    public CellDetails getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(CellDetails currentCell) {
        this.currentCell = currentCell;
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

}
