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

public class SetConstants {

    private int gridSize;
    private int gridType;
    private boolean collision;

    private int processCapacity;
    private int numberOfMachines;
    private String serverIdPrefix;

    public SetConstants() {

    }

    /**
     * A constructor for SetConstants.
     *
     * @param gridSize
     *            grid dimension for the topology (number of cells along the side)
     * @param gridType
     *            honeycomb or square grid
     * @param collision
     *            to set if the new simulation has collision between the cells
     * @param processCapacity
     *            number of netconf servers
     * @param numberOfMachines
     *            number of machines running in one netconf server
     * @param serverIdPrefix
     *            server id prefix
     */
    public SetConstants(int gridSize, int gridType, boolean collision, int processCapacity, int numberOfMachines,
            String serverIdPrefix) {
        super();
        this.gridSize = gridSize;
        this.gridType = gridType;
        this.collision = collision;
        this.processCapacity = processCapacity;
        this.numberOfMachines = numberOfMachines;
        this.serverIdPrefix = serverIdPrefix;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getGridType() {
        return gridType;
    }

    public void setGridType(int gridType) {
        this.gridType = gridType;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public int getProcessCapacity() {
        return processCapacity;
    }

    public void setProcessCapacity(int processCapacity) {
        this.processCapacity = processCapacity;
    }

    public int getNumberOfMachines() {
        return numberOfMachines;
    }

    public void setNumberOfMachines(int numberOfMachines) {
        this.numberOfMachines = numberOfMachines;
    }

    public String getServerIdPrefix() {
        return serverIdPrefix;
    }

    public void setServerIdPrefix(String serverIdPrefix) {
        this.serverIdPrefix = serverIdPrefix;
    }

}
