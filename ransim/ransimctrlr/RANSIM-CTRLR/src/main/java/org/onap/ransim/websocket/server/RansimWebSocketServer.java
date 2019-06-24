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

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.EncodeException;

import org.apache.log4j.Logger;
import org.onap.ransim.rest.api.controller.RansimController;
import org.onap.ransim.websocket.model.DeviceData;
import org.onap.ransim.websocket.model.DeviceDataDecoder;
import org.onap.ransim.websocket.model.DeviceDataEncoder;
import org.onap.ransim.websocket.model.MessageTypes;

@ServerEndpoint(value = "/RansimAgent/{IpPort}", encoders = { DeviceDataEncoder.class }, decoders = { DeviceDataDecoder.class })
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
        log.info("WSS Ransim client(" + ipPort + ") opened a connection with id " + session.getId());
        try {
            String serverId = RansimController.getRansimController().addWebSocketSessions(ipPort,
                    session);
            if (serverId != null) {
                log.info("New websocket session added for " + serverId);
                RansimController.getRansimController().sendInitialConfigForNewAgent(ipPort,
                        serverId);
            } else {
                log.info("RansimWebSocketServer: No assigned ServerId found - No intial configuration sent to New Agent "
                        + ipPort);
            }
        } catch (Exception e) {
            log.info("Exception in onOpen:", e);
        }
    }
    
    /*
    @OnMessage
    public void onMessage(String messageStr, Session session, @PathParam("IpPort") String ipPort) {
        log.info("WSS Str Message received from client(" + ipPort + ") with id " + session.getId());
        try {
            if (messageStr != null) {
                DeviceData message = new DeviceDataDecoder().decode(messageStr);
                if (message.getMessage() == null || message.getMessage().trim().equals("")) {
                    log.debug("Periodic ping message.... ignore");
                    return;
                } else {
                    
                    if (message.getType().equals(MessageTypes.HC_TO_RC_MODPCI)) {
                        RansimController.getRansimController().handleModifyPciFromSdnr(
                                message.getMessage(), session, ipPort);
                    } else if (message.getType().equals(MessageTypes.HC_TO_RC_MODANR)) {
                        RansimController.getRansimController().handleModifyNeighborFromSdnr(
                                message.getMessage(), session, ipPort);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Exception in onMessage:", e);
        }
    }
    */
    
    
    /**
     * Handles the message sent from the agent.
     *
     * @param message
     *            message sent from the agent
     * @param session
     *            session details
     * @param ipPort
     *            ip address
     * public void onMessage(DeviceData message, Session session, @PathParam("IpPort") String ipPort) {
     */
    @OnMessage
    public void onMessage(final DeviceData message, final Session session, @PathParam("IpPort") String ipPort) 
        throws IOException, EncodeException {
        log.info("WSS Obj Message received from client(" + ipPort + ") with id " + session.getId());
        try {
            if (message != null) {
                if (message.getMessage() == null || message.getMessage().trim().equals("")) {
                    log.debug("Periodic ping message.... ignore");
                    return;
                } else {
                    
                    if (message.getType().equals(MessageTypes.HC_TO_RC_MODPCI)) {
                        log.info("Modify pci message received");
                        RansimController.getRansimController().handleModifyPciFromSdnr(
                                message.getMessage(), session, ipPort);
                    } else if (message.getType().equals(MessageTypes.HC_TO_RC_MODANR)) {
                        log.info("Modify anr message received");
                        RansimController.getRansimController().handleModifyNeighborFromSdnr(
                                message.getMessage(), session, ipPort);
                    }
                }
            }
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
            log.info("WSS Closing client(" + ipPort + ") cxn with id " + session.getId() + "due to " + reason.getReasonPhrase());
            RansimController.getRansimController().removeWebSocketSessions(ipPort);
        } catch (Exception e) {
            log.info("Exception in onClose:", e);
        }
    }
    
    public static void sendUpdateCellMessage(String str, Session session) {
        DeviceData data = new DeviceData();
        data.setType(MessageTypes.RC_TO_HC_UPDCELL);
        data.setMessage(str);
        try {
            sendMessage(data, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendPmMessage(String str, Session session) {
        DeviceData data = new DeviceData();
        data.setType(MessageTypes.RC_TO_HC_PMDATA);
        data.setMessage(str);
        log.info("data.setMessage: " + data.getMessage());
        try {
            sendMessage(data, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendFmMessage(String str, Session session) {
        DeviceData data = new DeviceData();
        data.setType(MessageTypes.RC_TO_HC_FMDATA);
        data.setMessage(str);
        try {
            sendMessage(data, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendSetConfigTopologyMessage(String str, Session session) {
        DeviceData data = new DeviceData();
        data.setType(MessageTypes.RC_TO_HC_SETCONFIGTOPO);
        data.setMessage(str);
        try {
            sendMessage(data, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendPingMessage(Session session) {
        DeviceData data = new DeviceData();
        data.setType(MessageTypes.RC_TO_HC_PING);
        data.setMessage("");
        try {
            sendMessage(data, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void sendMessage(DeviceData data, Session session) {
        try {
            session.getBasicRemote().sendObject(data);
        } catch (Exception e) {
            log.info("Exception in sendMessage:", e);
        }
    }
}
