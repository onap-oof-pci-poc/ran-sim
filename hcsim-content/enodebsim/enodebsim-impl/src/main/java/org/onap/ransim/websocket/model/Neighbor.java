/*
 * Copyright (C) 2018 Wipro Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.ransim.websocket.model;

public class Neighbor {

    private String plmnId;
    private String nodeId;
    private long physicalCellId;
    private String serverId;
    private String pnfName;
    private boolean blacklisted;

    public String getPlmnId() {
        return plmnId;
    }

    public void setPlmnId(String plmnId) {
        this.plmnId = plmnId;
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

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPnfName() {
        return pnfName;
    }

    public void setPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    @Override
    public String toString() {
        return "Neighbor [nodeId=" + nodeId + ", physicalCellId="
                + physicalCellId + ", serverId=" + serverId + ", pnfName="
                + pnfName + ", isBlacklisted=" + blacklisted + "]";
    }

    public Neighbor(String plmnId, String nodeId, long physicalCellId,
            String serverId, String pnfName, boolean blacklisted) {
        super();
        this.plmnId = plmnId;
        this.nodeId = nodeId;
        this.physicalCellId = physicalCellId;
        this.serverId = serverId;
        this.pnfName = pnfName;
        this.blacklisted = blacklisted;
    }

    public Neighbor() {
        // TODO Auto-generated constructor stub
    }

}
