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

import java.io.Serializable;

public class StartSimulationReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private int gridSize;
    private int gridType;
    private int numberOfClusters;
    private int clusterLevel;

    public StartSimulationReq() {

    }

    /**
     * A constructor for StartSimulationReq.
     *
     * @param gridSize
     *            grid dimension for the topology (number of cells along the side)
     * @param gridType
     *            honeycomb or square grid
     * @param numberOfClusters
     *            number of clusters in a cell
     * @param clusterLevel
     *            cluster level for a given cell
     */
    public StartSimulationReq(int gridSize, int gridType, int numberOfClusters, int clusterLevel) {
        super();
        this.gridSize = gridSize;
        this.setGridType(gridSize);
        this.numberOfClusters = numberOfClusters;
        this.clusterLevel = clusterLevel;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
    }

    public int getClusterLevel() {
        return clusterLevel;
    }

    public void setClusterLevel(int clusterLevel) {
        this.clusterLevel = clusterLevel;
    }

    public int getGridType() {
        return gridType;
    }

    public void setGridType(int gridType) {
        this.gridType = gridType;
    }

}
