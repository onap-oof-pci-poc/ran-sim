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

public class GetNeighborList {
    
    private String nodeId;
    private List<CellDetails> cellsWithNoHo;
    private List<CellDetails> cellsWithHo;
    
    public GetNeighborList() {
        super();
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param nodeId
     *            Node Id of cell
     * @param cellsWithNoHO
     *            List of neighbors with unsuccessful handover.
     * @param cellsWithHO
     *            List of neighbors with successful handover.
     */
    public GetNeighborList(String nodeId, List<CellDetails> cellsWithNoHo,
            List<CellDetails> cellsWithHo) {
        super();
        this.nodeId = nodeId;
        this.cellsWithNoHo = cellsWithNoHo;
        this.cellsWithHo = cellsWithHo;
    }
    
    public String getNodeId() {
        return nodeId;
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    public List<CellDetails> getCellsWithNoHo() {
        return cellsWithNoHo;
    }
    
    public void setCellsWithNoHo(List<CellDetails> cellsWithNoHo) {
        this.cellsWithNoHo = cellsWithNoHo;
    }
    
    public List<CellDetails> getCellsWithHo() {
        return cellsWithHo;
    }
    
    public void setCellsWithHo(List<CellDetails> cellsWithHo) {
        this.cellsWithHo = cellsWithHo;
    }
    
}
