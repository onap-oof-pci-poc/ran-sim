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

public class CreateACellReq implements Serializable {

    private long physicalCellId;

    private int gridX;
    private int gridY;

    private static final long serialVersionUID = 3736300675426332512L;

    public CreateACellReq() {
        // Default constructor for CreateACellReq
    }

    /**
     * A constructor for CreateACellReq.
     *
     * @param physicalCellId
     *            PCI number of the new cell
     * @param gridX
     *            x coordinate value for the cell
     * @param gridY
     *            y coordinate value for the cell
     */
    public CreateACellReq(long physicalCellId, int gridX, int gridY) {
        super();

        this.physicalCellId = physicalCellId;

        this.gridX = gridX;
        this.gridY = gridY;
    }

    public long getPhysicalCellId() {
        return physicalCellId;
    }

    public void setPhysicalCellId(long physicalCellId) {
        this.physicalCellId = physicalCellId;
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
}
