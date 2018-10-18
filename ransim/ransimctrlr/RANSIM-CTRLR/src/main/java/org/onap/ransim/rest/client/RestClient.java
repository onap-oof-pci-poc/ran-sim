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

package org.onap.ransim.rest.client;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.persistence.internal.oxm.conversion.Base64;
import org.onap.ransim.rest.api.controller.RansimControllerServices;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {

    static Logger log = Logger.getLogger(RansimControllerServices.class.getName());

    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.base64Encode(auth.getBytes(Charset.forName("US-ASCII")));

                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
                set("Content-Type", "application/xml");
                set("Accept", "application/xml");
            }
        };
    }

    /**
     * Sends mount request to sdnr.
     *
     * @param serverId
     *            netconf server id name
     * @param ip
     *            server ip address
     * @param port
     *            port number
     * @param agentIp
     *            agent ip address
     * @param agentPort
     *            agent port number
     * @param agentUsername
     *            agent username
     * @param agentPassword
     *            agent password
     * @return returns the message to be passed
     */
    public String sendMountRequestToSdnr(String serverId, String ip, int port, String agentIp, String agentPort,
            String agentUsername, String agentPassword) {
        String requestBody = "<node\r\n" + "    xmlns=\"urn:TBD:params:xml:ns:yang:network-topology\">\r\n"
                + "    <node-id>" + serverId + "</node-id>\r\n" + "    <host\r\n"
                + "        xmlns=\"urn:opendaylight:netconf-node-topology\">" + agentIp + "\r\n" + "    </host>\r\n"
                + "    <port\r\n" + "        xmlns=\"urn:opendaylight:netconf-node-topology\">" + agentPort + "\r\n"
                + "    </port>\r\n" + "    <username\r\n"
                + "        xmlns=\"urn:opendaylight:netconf-node-topology\">admin\r\n" + "    </username>\r\n"
                + "    <password\r\n" + "        xmlns=\"urn:opendaylight:netconf-node-topology\">admin\r\n"
                + "    </password>\r\n" + "    <tcp-only\r\n"
                + "        xmlns=\"urn:opendaylight:netconf-node-topology\">false\r\n" + "    </tcp-only>\r\n"
                + "    <!-- non-mandatory fields with default values, you can safely remove these "
                + "if you do not wish to override any of these values-->\r\n" + "    <reconnect-on-changed-schema\r\n"
                + "        xmlns=\"urn:opendaylight:netconf-node-topology\">false\r\n"
                + "    </reconnect-on-changed-schema>\r\n" + "    <connection-timeout-millis\r\n"
                + "        xmlns=\"urn:opendaylight:netconf-node-topology\">20000\r\n"
                + "    </connection-timeout-millis>\r\n" + "    <max-connection-attempts\r\n"
                + "        xmlns=\"urn:opendaylight:netconf-node-topology\">0\r\n"
                + "    </max-connection-attempts>\r\n" + "    <between-attempts-timeout-millis\r\n"
                + "        xmlns=\"urn:opendaylight:netconf-node-topology\">2000\r\n"
                + "    </between-attempts-timeout-millis>\r\n" + "    <sleep-factor\r\n"
                + "        xmlns=\"urn:opendaylight:netconf-node-topology\">1.5\r\n" + "    </sleep-factor>\r\n"
                + "    <keepalive-delay\r\n" + "        xmlns=\"urn:opendaylight:netconf-node-topology\">120\r\n"
                + "    </keepalive-delay>\r\n" + "</node> '";
        HttpHeaders headers = createHeaders(agentUsername, agentPassword);

        log.info("request : " + requestBody);
        log.info("headers : " + headers);
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            log.info("Key:" + entry.getKey() + "  , Value:" + entry.getValue());
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://" + ip + ":" + port
                + "/restconf/config/network-topology:network-topology/topology/topology-netconf/node/" + serverId;

        HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        log.info("request sent, result: " + result);
        return result.toString();
    }

    /**
     * Sends an unmount request to sdnr.
     *
     * @param serverId
     *            netconf server id name
     * @param ip
     *            ip address
     * @param port
     *            port number
     * @param sdnrUsername
     *            sdnr username
     * @param sdnrPassword
     *            sdnr password
     * @return returns the message to be passed
     */
    public String sendUnmountRequestToSdnr(String serverId, String ip, int port, String sdnrUsername,
            String sdnrPassword) {
        String url = "http://" + ip + ":" + port
                + "/restconf/config/network-topology:network-topology/topology/topology-netconf/node/" + serverId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = createHeaders(sdnrUsername, sdnrPassword);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
        log.info("request sent, result: " + result);
        return result.toString();
    }
}
