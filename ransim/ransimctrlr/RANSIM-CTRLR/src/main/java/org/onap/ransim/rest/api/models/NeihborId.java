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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class NeihborId implements Serializable {
    
    @Column(name = "sourceCellNodeId")
    private String sourceCellNodeId;
    
    @Column(name = "neighborCellNodeId")
    private String neighborCell;
    
    public NeihborId() {
    }
    
    public NeihborId(String sourceCellNodeId, String neighborCell) {
        this.sourceCellNodeId = sourceCellNodeId;
        this.neighborCell = neighborCell;
    }
    
    public String getSourceCellNodeId() {
        return sourceCellNodeId;
    }
    
    public String getNeighborCell() {
        return neighborCell;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((neighborCell == null) ? 0 : neighborCell.hashCode());
        result = prime
                * result
                + ((sourceCellNodeId == null) ? 0 : sourceCellNodeId.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NeihborId other = (NeihborId) obj;
        if (neighborCell == null) {
            if (other.neighborCell != null) {
                return false;
            }
        } else if (!neighborCell.equals(other.neighborCell))
            return false;
        if (sourceCellNodeId == null) {
            if (other.sourceCellNodeId != null) {
                return false;
            }
        } else if (!sourceCellNodeId.equals(other.sourceCellNodeId)) {
            return false;
        }
        return true;
    }
}