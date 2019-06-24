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

public class CellData {
    
    private CellInfo Cell;
    private List<NbrDump> neighbor;
    
    public CellData() {
        super();
    }
    
    /**
     * Constructor with all parameters
     * 
     * @param cell
     *            Contains cell details.
     * @param neighbor
     *            Contains list of neighbor details.
     */
    public CellData(CellInfo cell, List<NbrDump> neighbor) {
        super();
        Cell = cell;
        this.neighbor = neighbor;
    }
    
    public CellInfo getCell() {
        return Cell;
    }
    
    public void setCell(CellInfo cell) {
        Cell = cell;
    }
    
    public List<NbrDump> getNeighbor() {
        return neighbor;
    }
    
    public void setNeighbor(List<NbrDump> neighbor) {
        this.neighbor = neighbor;
    }
    
}
