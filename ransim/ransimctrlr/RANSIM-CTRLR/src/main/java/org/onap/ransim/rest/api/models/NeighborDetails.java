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

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NeighborDetails")
public class NeighborDetails {
    
    @EmbeddedId
    private NeihborId neigbor;
    
    private boolean blacklisted;
    
    public NeighborDetails() {
        
    }

    public NeighborDetails(NeihborId neigbor, boolean blacklisted) {
        super();
        this.neigbor = neigbor;
        this.blacklisted = blacklisted;
    }


    public NeihborId getNeigbor() {
        return neigbor;
    }

    public void setNeigbor(NeihborId neigbor) {
        this.neigbor = neigbor;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }
    
    
}
