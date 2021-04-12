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

package com.wipro.www;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.wipro.www.websocket.WebsocketClient;
import com.wipro.www.websocket.models.AllocationUpdateMessage;
import com.wipro.www.websocket.models.DeviceData;
import com.wipro.www.websocket.models.InitialConfig;
import com.wipro.www.websocket.models.NearRTRIC;
import com.wipro.www.websocket.models.GNBDUFunction;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.SliceDetailsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationHandler.class);

    private WebsocketClient websocketClient;

    public WebsocketClient getWebsocketClient() {
        return websocketClient;
    }

    public void setWebsocketClient(WebsocketClient websocketClient) {
        this.websocketClient = websocketClient;
    }

    private static ConfigurationHandler instance;

    private ConfigurationHandler(){

    }

    public static ConfigurationHandler getInstance(){
        if (instance == null){
            instance = new ConfigurationHandler();
        }
        return instance;
    }

    public void handleInitialConfig(String message){
        ObjectMapper mapper = new ObjectMapper();
        try {
            if(mapper.readValue(message,NearRTRIC.class) instanceof 
               NearRTRIC) {
               NearRTRIC initialConfig = mapper.readValue(message,NearRTRIC.class); 
               InMemoryDataTree.getInstance().setLocationName(
                                          initialConfig.getLocationName());
               InMemoryDataTree.getInstance().setNearRTRICgNBId(
                                          initialConfig.getNearRTRICgNBId());
               InMemoryDataTree.getInstance().setManagedBy(
                                          initialConfig.getManagedBy());
               LOG.info("Initial configuration is set successfully for nearRTRIC");
            } else if (mapper.readValue(message,GNBDUFunction.class) instanceof
                       GNBDUFunction) {
               GNBDUFunction initialConfig = mapper.readValue(message,
                                                              GNBDUFunction.class);
               InMemoryDataTree.getInstance().setGNBId(
                                          initialConfig.getGNBId());
               InMemoryDataTree.getInstance().setAggressorSetID(
                                          initialConfig.getAggressorSetID());
               InMemoryDataTree.getInstance().setVictimSetID(
                                          initialConfig.getVictimSetID());
               //InMemoryDataTree.getInstance().setNrCellDu(
               //                           initialConfig.getnRCellDU());
               LOG.info("Initial configuration is set successfully                                           for GNBDUFunction");               
            } 
               
        } catch (Exception e) {
            LOG.error("Exception occurred while parsing {}", e.getMessage());
        }
    }

    public void sendAllocationUpdate(AllocationUpdateMessage allocationUpdateMessage){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String updateMessage = mapper.writeValueAsString(allocationUpdateMessage);
            DeviceData deviceData = new DeviceData();
            deviceData.setMessageType(MessageType.ALLOCATION_UPDATE);
            deviceData.setMessage(updateMessage);
            websocketClient.sendMessage(deviceData);
        } catch (JsonProcessingException e){
            LOG.error("JSON processing exception while sending allocation update");
        }
    }
    
    public void sendSliceDetails(SliceDetailsMessage sliceDetailsMessage){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String updateMessage = mapper.writeValueAsString(sliceDetailsMessage);
            DeviceData deviceData = new DeviceData();
            deviceData.setMessageType(MessageType.SLICE_DETAILS);
            deviceData.setMessage(updateMessage);
            websocketClient.sendMessage(deviceData);
        } catch (JsonProcessingException e){
            LOG.error("JSON processing exception while sending allocation update");
        }
    }
    
    public void sendSNssaiDetails(SliceDetailsMessage sliceDetailsMessage){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String updateMessage = mapper.writeValueAsString(sliceDetailsMessage);
            DeviceData deviceData = new DeviceData();
            deviceData.setMessageType(MessageType.NSSAI_DETAILS);
            deviceData.setMessage(updateMessage);
            websocketClient.sendMessage(deviceData);
        } catch (JsonProcessingException e){
            LOG.error("JSON processing exception while sending allocation update");
        }
    }

    public void sendDatabaseUpdate(String message, MessageType messageType){
        try {
            DeviceData deviceData = new DeviceData();
            deviceData.setMessage(message);
            deviceData.setMessageType(messageType);
            websocketClient.sendMessage(deviceData);
        } catch (Exception e){
            LOG.error("Error while sending messge");
        }
    }

}
