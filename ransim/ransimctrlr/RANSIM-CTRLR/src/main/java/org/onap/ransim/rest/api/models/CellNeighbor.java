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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CellNeighbor")
public class CellNeighbor {

    @Id
    @Column(name = "nodeId", unique = true, nullable = false, length = 52)
    private String nodeId;

    @OneToMany(targetEntity = CellDetails.class)
    private Set<CellDetails> neighborList;

    public CellNeighbor() {

    }

    /**
     * A constructor for CellNeighbor(database for storing the neighbor list of the
     * cells).
     *
     * @param nodeId
     *            node Id of the cell
     * @param neighborList
     *            details of the neighbors for the given cell
     */
    public CellNeighbor(String nodeId, Set<CellDetails> neighborList) {
        super();
        this.nodeId = nodeId;
        this.neighborList = neighborList;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Set<CellDetails> getNeighborList() {
        return neighborList;
    }

    public void setNeighborList(Set<CellDetails> neighborList) {
        this.neighborList = neighborList;
    }
}
