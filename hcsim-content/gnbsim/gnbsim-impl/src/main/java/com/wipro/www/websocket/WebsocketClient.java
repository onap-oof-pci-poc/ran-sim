/*
 * Copyright (c) 2016 Cisco and/or its affiliates.
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

package com.wipro.www.websocket;

import com.google.gson.Gson;
import com.wipro.www.ConfigurationHandler;
import com.wipro.www.InMemoryDataTree;
import com.wipro.www.ves.SlicePmDataHandler;
import com.wipro.www.websocket.models.DeviceData;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.SlicingPmMessage;

import java.net.URI;

import javax.websocket.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientEndpoint(encoders = {DeviceDataEncoder.class}, decoders = {DeviceDataDecoder.class})
public class WebsocketClient {

    private static final Logger LOG = LoggerFactory.getLogger(WebsocketClient.class);

    private Session clientSession;

    private String url;

    private String vesEventListenerUrl;

    public WebsocketClient(String ransimIp, int ransimPort, String hcIp, int hcPort, String endpoint,
            String vesEventListenerUrl) {

        this.url = "ws://" + ransimIp + ":" + ransimPort + "/" + endpoint + "/" + hcIp + ":" + hcPort;
        this.vesEventListenerUrl = vesEventListenerUrl;
    }

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("WebSocket opened (jetty server) Session Id: {}", session.getId());
    }

    @OnClose
    public void OnClose(CloseReason reason, Session session) {
        LOG.info("Websocket {} closed. Close reason {}. If closed unintentionally, Retrying...", session.getId(),
                reason.getReasonPhrase());
        clientSession = null;
        new RetryWebsocket(this).start();
    }

    @OnMessage
    public void onMessage(DeviceData deviceData, Session session) {
        try {
            if (deviceData != null) {
                MessageType type = deviceData.getMessageType();
                LOG.info("Message Received: Type:{}, msg:{}, Session Id:{}", type, deviceData.getMessage(),
                        session.getId());

                switch (type) {
                    case RC_TO_HC_PMFILEDATA:
                        LOG.info("Processing PM message");
                        SlicingPmMessage pmData = new Gson().fromJson(deviceData.getMessage(), SlicingPmMessage.class);
                        SlicePmDataHandler slicePmDataHandler = new SlicePmDataHandler(vesEventListenerUrl);
                        slicePmDataHandler.handleSlicePmData(pmData);
                        break;
                    case PING:
                        LOG.debug("Periodic ping mesage, Ignoring...");
                        break;
                    case INITIAL_CONFIG:
                        LOG.info("Initial config from ransim ctlr");
                        ConfigurationHandler.getInstance().handleInitialConfig(deviceData.getMessage());
                        break;
                    default:
                        LOG.info("Invalid message type {}. Ignoring", type);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in processing message {}", e.getMessage());
        }
    }

    public void sendMessage(DeviceData deviceData) {
        try {
            LOG.info("Message to Send: Type:{}, msg:{}, Session Id:{}", deviceData.getMessageType(),
                    deviceData.getMessage(), clientSession.getId());
            if (clientSession != null) {
                clientSession.getBasicRemote().sendObject(deviceData);
            } else {
                LOG.error("Could not get websocket client session!!");
            }
        } catch (Exception e) {
            LOG.error("Exception while sending message {}", e.getMessage());
        }
    }

    public void initWebsocketClient() {
        LOG.info("Initializing web socket client");
        new RetryWebsocket(this).start();
    }

    public void connectWebsocket() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        LOG.info("Connecting to Server");
        clientSession = container.connectToServer(this, new URI(url));
        LOG.info("Connected to server, Session id: {}", clientSession.getId());
        LOG.info("Sending connection request...");
    }

    class RetryWebsocket extends Thread {

        WebsocketClient client;

        public RetryWebsocket(WebsocketClient client) {
            this.client = client;
        }

        public void run() {

            boolean toContinue = true;
            while (toContinue) {
                try {
                    client.connectWebsocket();
                    toContinue = false;
                    LOG.info("Connected to server, Stopping RetryWebsocket");
                } catch (Exception e) {
                    LOG.info("Failed to connect to server, Retrying...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
