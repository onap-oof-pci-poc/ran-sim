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

import java.util.List;

public class SetConfigTopology {

    private String serverId;
    private String uuid;
    private String ip;
    private String netconfPort;
    private List<Topology> topology;

    public SetConfigTopology() {

    }

    /**
     * Cell details for the given netconf server.
     *
     * @param serverId
     *            netconf server id
     * @param ip
     *            ip address
     * @param netconfPort
     *            port number
     * @param topology
     *            cell topology for given server id
     */
    public SetConfigTopology(String serverId, String ip, String netconfPort,
            List<Topology> topology) {
        super();
        this.serverId = serverId;
        this.ip = ip;
        this.netconfPort = netconfPort;
        this.topology = topology;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetconfPort() {
        return netconfPort;
    }

    public void setNetconfPort(String netconfPort) {
        this.netconfPort = netconfPort;
    }

    public List<Topology> getTopology() {
        return topology;
    }

    public void setTopology(List<Topology> topology) {
        this.topology = topology;
    }

}
