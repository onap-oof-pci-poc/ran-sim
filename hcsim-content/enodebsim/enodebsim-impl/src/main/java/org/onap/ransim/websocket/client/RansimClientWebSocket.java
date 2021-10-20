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

package org.onap.ransim.websocket.client;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.onap.ransim.ConfigJsonHandler;
import org.onap.ransim.ves.fm.FmDataHandler;
import org.onap.ransim.ves.pm.PmDataHandler;
import org.onap.ransim.websocket.model.DeviceData;
import org.onap.ransim.websocket.model.DeviceDataDecoder;
import org.onap.ransim.websocket.model.DeviceDataEncoder;
import org.onap.ransim.websocket.model.FmMessage;
import org.onap.ransim.websocket.model.MessageTypes;
import org.onap.ransim.websocket.model.PmMessage;
import org.onap.ransim.websocket.model.SetConfigTopology;
import org.onap.ransim.websocket.model.UpdateCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@ClientEndpoint(encoders = { DeviceDataEncoder.class }, decoders = { DeviceDataDecoder.class })
public class RansimClientWebSocket {

    private Session clientSession = null;

    String wsSrvIp = null;
    int wsSrvPort = 0;
    String wsClientIp = null;
    int wsClientPort = 0;

    private static final Logger LOG = LoggerFactory
            .getLogger(RansimClientWebSocket.class);

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("WebSocket opened (jetty server) Session Id: {}",
                session.getId());
    }

    @OnMessage
    public void onMessage(DeviceData msgObject, Session session)
            throws IOException {
        try {
            clientSession = session;
            String type = "";
            if (msgObject != null) {
                if (msgObject.getMessage() == null
                        || msgObject.getMessage().trim().equals("")) {
                    LOG.debug("Periodic ping message.... ignore");
                    return;
                }
                type = msgObject.getType();
                LOG.info("Message Received with: Type:{}, msg:{}, Session Id:{}",
                        type, msgObject.getMessage(), session.getId());
                if (type.equals(MessageTypes.RC_TO_HC_PMDATA)) {
                    PmMessage pmdata = new Gson().fromJson(
                            msgObject.getMessage(), PmMessage.class);
                    LOG.info("RAJESH: PmData retrieved");
                    PmDataHandler pmDataHandler = new PmDataHandler();
                    pmDataHandler.handlePmData(pmdata);
                } else if (type.equals(MessageTypes.RC_TO_HC_FMDATA)) {
                    FmMessage fmdata = new Gson().fromJson(
                            msgObject.getMessage(), FmMessage.class);
                    LOG.info("RAJESH: FmData retrieved");
                    FmDataHandler fmDataHandler = new FmDataHandler();
                    fmDataHandler.handleFmData(fmdata);
                } else if (type.equals(MessageTypes.RC_TO_HC_SETCONFIGTOPO)) {
                    SetConfigTopology updTopo = new Gson().fromJson(
                            msgObject.getMessage(), SetConfigTopology.class);
                    ConfigJsonHandler cfgHandler = ConfigJsonHandler
                            .getConfigJsonHandler(null);
                    cfgHandler.handleSetConfigTopology(updTopo);
                } else if (type.equals(MessageTypes.RC_TO_HC_UPDCELL)) {
                    UpdateCell updCell = new Gson().fromJson(
                            msgObject.getMessage(), UpdateCell.class);
                    ConfigJsonHandler cfgHandler = ConfigJsonHandler
                            .getConfigJsonHandler(null);
                    cfgHandler.handleUpdateCell(updCell);
                } else {
                    LOG.info("Invalid type {} retrieved", type);
                }
            } else {
                LOG.debug("onMessage: MSG Object is Null");
            }
        } catch (Exception e) {
            LOG.info("Exception in onMessage:", e);
        }
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        LOG.info("Closing a WebSocket Session Id {} due to {}",
                session.getId(), reason.getReasonPhrase());
        clientSession = null;
        new RetryWebsocket(this).start();
    }

    public void sendMessage(DeviceData wsMsg) {
        try {
            int retryCount = 0;
            while (clientSession == null) {
                try {
                    Thread.sleep(5000);
                } catch (Exception ex) {
                }
                if (retryCount++ > 4)
                    break;
            }
            LOG.info("Message to Send: Type:{}, msg:{}, Session Id:{}",
                    wsMsg.getType(), wsMsg.getMessage(), clientSession.getId());

            if (clientSession != null) {
                clientSession.getBasicRemote().sendObject(wsMsg);
            } else {
                LOG.error("Could not get websock client session!!");
            }
        } catch (Exception e) {
            LOG.debug("Exception in sendMessage:", e);
            LOG.error("Exception in sendMessage: {}", e.toString());
        }
    }

    public void initWebsocketClient(String wsSrvIp, int wsSrvPort,
            String wsClientIp, int wsClientPort) {
        LOG.info(
                "initWebsocketClient wsSrvIp:{} wsSrvPort:{} wsClientIp:{} wsClientPort:{}",
                wsSrvIp, wsSrvPort, wsClientIp, wsClientPort);

        this.wsSrvIp = wsSrvIp;
        this.wsSrvPort = wsSrvPort;
        this.wsClientIp = wsClientIp;
        this.wsClientPort = wsClientPort;

        try {
            connectWebsocketClient();
        } catch (Exception e) {
            LOG.info("Exception in initWebsocketClient : ", e);
            new RetryWebsocket(this).start();
            LOG.info("RetryWebsocket thread started");
        }
    }

    private void connectWebsocketClient() throws Exception {
        String dest = "ws://" + wsSrvIp + ":" + wsSrvPort
                + "/ransim/RansimAgent/" + wsClientIp + ":" + wsClientPort;
        WebSocketContainer container = ContainerProvider
                .getWebSocketContainer();
        LOG.info("Connecting to websocket server   :{}", dest);
        clientSession = container.connectToServer(this, new URI(dest));
        LOG.info("WebSocket opened with (server) Session Id: {}",
                clientSession.getId());
    }

    class RetryWebsocket extends Thread {

        RansimClientWebSocket client = null;

        public RetryWebsocket(RansimClientWebSocket client) {
            this.client = client;
            LOG.info("RetryWebsocket initialized");
        }

        @Override
        public void run() {
            LOG.info("RetryWebsocket started");
            boolean toContinue = true;
            while (toContinue) {
                try {
                    client.connectWebsocketClient();
                    toContinue = false;
                    LOG.info("RetryWebsocket can be stopped");
                } catch (Exception e) {
                    LOG.info("RetryWebsocket to be continued");
                    try {
                        Thread.sleep(5000);
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }
}
