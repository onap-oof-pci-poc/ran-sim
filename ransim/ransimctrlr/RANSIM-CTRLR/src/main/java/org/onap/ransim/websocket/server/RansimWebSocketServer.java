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

package org.onap.ransim.websocket.server;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.onap.ransim.rest.api.controller.RansimController;
import org.onap.ransim.websocket.model.Neighbor;
import org.onap.ransim.websocket.model.SetConfigTopology;
import org.onap.ransim.websocket.model.Topology;

@ServerEndpoint("/RansimAgent/{IpPort}")
public class RansimWebSocketServer {

    static Logger log = Logger.getLogger(RansimWebSocketServer.class.getName());

    /**
     * Set of actions to be done when connection is opened.
     *
     * @param session
     *            Session details
     * @param ipPort
     *            ip address of the agent
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("IpPort") String ipPort) {
        log.info("Ransim client(" + ipPort + ") opened a connection");
        try {
            RansimController.getRansimController().addWebSocketSessions(ipPort, session);
            RansimController.getRansimController().sendInitialConfigForNewAgent(ipPort);
        } catch (Exception e) {
            log.info("Exception in onOpen:", e);
        }
    }

    /**
     * Handles the message sent from the agent.
     *
     * @param message
     *            message sent from the agent
     * @param session
     *            session details
     * @param ipPort
     *            ip address
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("IpPort") String ipPort) {
        try {
            if(message == null || message.trim().equals("")) {
                log.debug("Periodic ping message.... ignore");
                return;
            }
            log.info("Message received from client(" + ipPort + "):" + message);
            RansimController.getRansimController().handleModifyPciFromSdnr(message, session, ipPort);
        } catch (Exception e) {
            log.info("Exception in onMessage:", e);
        }
    }

    /**
     * Set of actions to be done when connection is closed.
     *
     * @param reason
     *            reason the session was closed
     * @param session
     *            session details
     * @param ipPort
     *            ip address
     */
    @OnClose
    public void onClose(CloseReason reason, Session session, @PathParam("IpPort") String ipPort) {
        try {
            log.info("Closing client(" + ipPort + ") cxn due to " + reason.getReasonPhrase());
            RansimController.getRansimController().removeWebSocketSessions(ipPort);
        } catch (Exception e) {
            log.info("Exception in onClose:", e);
        }
    }

    public static void sendUpdateCellMessage(String str, Session session) {
        sendMessage("UpdateCell:" + str, session);
    }

    public static void sendSetConfigTopologyMessage(String str, Session session) {
        sendMessage("SetConfigTopology:" + str, session);
    }

    public static void sendPingMessage(Session session) {
        sendMessage("", session);
    }

    private static void sendMessage(String str, Session session) {
        try {
            session.getBasicRemote().sendText(str);
        } catch (IOException e) {
            log.info("Exception in sendMessage:", e);
        }
    }
}
