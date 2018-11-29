/*
 * ============LICENSE_START=======================================================
 * RAN Simulator - HoneyComb
 * ================================================================================
 * Copyright (C) 2018 Wipro Limited.
 * ================================================================================
 *
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientEndpoint
public class RansimClientWebSocket {

    private Session clientSession = null;

    String wsSrvIp = null;
    int wsSrvPort = 0;
    String wsClientIp = null;
    int wsClientPort = 0;

    private static final Logger LOG = LoggerFactory.getLogger(RansimClientWebSocket.class);

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("WebSocket opened (jetty server) Session Id: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String jsonStr, Session session) throws IOException {
        try {
            clientSession = session;
	    if(jsonStr == null || jsonStr.trim().equals('')) {
                LOG.debug("Periodic ping message.... ignore");
		return;
	    }
            LOG.info("Message received: {}, Session Id: {}", jsonStr, session.getId());
            ConfigJsonHandler cfgHandler = ConfigJsonHandler.getConfigJsonHandler(null);
            cfgHandler.handleUpdateTopology(jsonStr);
        } catch(Exception e) {
            LOG.info("Exception in onMessage:", e);
        }
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        LOG.info("Closing a WebSocket Session Id {} due to {}", session.getId(), reason.getReasonPhrase());
	clientSession = null;
        new RetryWebsocket(this).start();
    }

    public void sendMessage(String jsonStr) {
        try {
	    int retryCount = 0;
            while(clientSession == null) {
		    try{ Thread.sleep(5000); } catch(Exception ex){}
		    if(retryCount++ > 4)
			    break;
            }
	    if(clientSession != null) {
                clientSession.getBasicRemote().sendText(jsonStr);
	    } else {
                LOG.error("Could not get websock client session!!");
	    }
        } catch (Exception e) {
            LOG.debug("Exception in sendMessage:", e);
            LOG.error("Exception in sendMessage:" + e);
        }
    }

    public void initWebsocketClient(String wsSrvIp, int wsSrvPort, String wsClientIp, int wsClientPort) {
        LOG.info("initWebsocketClient wsSrvIp:{} wsSrvPort:{} wsClientIp:{} wsClientPort:{}", wsSrvIp, wsSrvPort, wsClientIp, wsClientPort);

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
        String dest = "ws://"+wsSrvIp+":"+wsSrvPort+"/ransim/RansimAgent/"+wsClientIp+":"+wsClientPort;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        LOG.info("Connecting to websocket server   :{}", dest);
        clientSession = container.connectToServer(this, new URI(dest));
        LOG.info("WebSocket opened with (server) Session Id: {}", clientSession.getId());
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
            while(toContinue) {
                try {
                    client.connectWebsocketClient();
                    toContinue = false;
                    LOG.info("RetryWebsocket can be stopped");
                } catch(Exception e) {
                    LOG.info("RetryWebsocket to be continued");
                    try {Thread.sleep(5000); } catch(Exception ex) {}
                }
            }
        }
    }

}
