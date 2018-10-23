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

import java.util.List;

public class Topology {
    int gridSize;
    float minScreenX = 0;
    float minScreenY = 0;
    float maxScreenX = 10;
    float maxScreenY = 10;
    List<CellDetails> cellTopology;

    /**
     * A constructor for Topology.
     *
     * @param gridSize
     *            grid size
     * @param minScreenX
     *            min value of screen X for a cell in the topology
     * @param minScreenY
     *            min value of screen Y for a cell in the topology
     * @param maxScreenX
     *            max value of screen X for a cell in the topology
     * @param maxScreenY
     *            max value of screen Y for a cell in the topology
     * @param cellTopology
     *            list of cells within the topology
     */
    public Topology(int gridSize, float minScreenX, float minScreenY, float maxScreenX, float maxScreenY,
            List<CellDetails> cellTopology) {
        super();
        this.gridSize = gridSize;
        this.minScreenX = minScreenX;
        this.minScreenY = minScreenY;
        this.maxScreenX = maxScreenX;
        this.maxScreenY = maxScreenY;
        this.cellTopology = cellTopology;
    }

    public Topology() {

    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public float getMinScreenX() {
        return minScreenX;
    }

    public void setMinScreenX(float minScreenX) {
        this.minScreenX = minScreenX;
    }

    public float getMinScreenY() {
        return minScreenY;
    }

    public void setMinScreenY(float minScreenY) {
        this.minScreenY = minScreenY;
    }

    public float getMaxScreenX() {
        return maxScreenX;
    }

    public void setMaxScreenX(float maxScreenX) {
        this.maxScreenX = maxScreenX;
    }

    public float getMaxScreenY() {
        return maxScreenY;
    }

    public void setMaxScreenY(float maxScreenY) {
        this.maxScreenY = maxScreenY;
    }

    public List<CellDetails> getCellTopology() {
        return cellTopology;
    }

    public void setCellTopology(List<CellDetails> cellTopology) {
        this.cellTopology = cellTopology;
    }

}
