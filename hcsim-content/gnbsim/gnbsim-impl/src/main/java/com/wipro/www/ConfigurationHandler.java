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
import com.wipro.www.websocket.models.nearRTRIC;
import com.wipro.www.websocket.models.RanNetwork;
import com.wipro.www.websocket.models.GNBDUFunction;
import com.wipro.www.websocket.models.NRCellDU;
import com.wipro.www.websocket.models.Attributes;
import com.wipro.www.websocket.models.GNBCUUPFunction;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.SliceDetailsMessage;
import com.wipro.www.websocket.models.NearRTRIC;
import com.wipro.www.websocket.models.PLMNInfoModel;
import com.wipro.www.websocket.models.ConfigPLMNInfo;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteFailedException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    

/*    public void handleInitialConfig(String message){
        ObjectMapper mapper = new ObjectMapper();
        try {
            InitialConfig initialConfig = mapper.readValue(message, InitialConfig.class);
            InMemoryDataTree.getInstance().setPnfName(initialConfig.getServerId());
            InMemoryDataTree.getInstance().setPnfUuid(initialConfig.getUuid());
            LOG.info("Initial configuration is set successfully");
        } catch (Exception e) {
            LOG.error("Exception occurred while parsing {}", e.getMessage());
        }
    }
*/

    public void handleInitialConfig(String message){
        ObjectMapper mapper = new ObjectMapper();

        try {

       /*     if(mapper.readValue(message,RanNetwork.class) instanceof 
               RanNetwork) {*/

                Gson gson = new Gson();
                JsonObject nearRTRICJsonObject = gson.fromJson(message, JsonObject.class);
                LOG.info("nearRTRICJsonArray:" + nearRTRICJsonObject.getAsJsonArray("nearRTRIC"));
                JsonObject nearRTRIC = nearRTRICJsonObject.getAsJsonArray("nearRTRIC").get(0).getAsJsonObject();
                LOG.info("nearRTRIC: " + nearRTRIC);


//my change               RanNetwork initialConfig = mapper.readValue(message,RanNetwork.class);

	      // List<nearRTRIC> nearRTRICInst = initialConfig.getNearRTRIC();
	       //mychange
	       List<nearRTRIC> nearRTRICInst = new ArrayList<nearRTRIC>();
	       for (JsonElement je : nearRTRICJsonObject.getAsJsonArray("nearRTRIC")) {
			nearRTRICInst.add(gson.fromJson(nearRTRIC, nearRTRIC.class));
		}
	       //
	       List<GNBDUFunction> gNBDUFunction = new ArrayList<GNBDUFunction>();
	       List<NRCellDU> nRCellDU = new ArrayList<NRCellDU>();
	       List<GNBCUUPFunction> gNBCUUPFunction = new ArrayList<GNBCUUPFunction>();

	       InMemoryDataTree.getInstance().setNearRTRIC(nearRTRICInst);
	       LOG.info("Inmemory location: " + InMemoryDataTree.getInstance().getNearRTRIC().get(0).getAttributes().getLocationName());
	       LOG.info("Inmememory gnbId: " + InMemoryDataTree.getInstance().getNearRTRIC().get(0).getAttributes().getgNBId());

	       for(int i=0; i < nearRTRICInst.size(); i++) {

	       LOG.info("idNearRTRIC value:[{}]",(nearRTRICInst.get(i)).getIdNearRTRIC());
	       //InMemoryDataTree.getInstance().setIdNearRTRIC((nearRTRICInst.get(i)).getIdNearRTRIC());
	       

	       LOG.info("LocationName value:[{}]",(nearRTRICInst.get(i)).getAttributes().getLocationName());
	       //InMemoryDataTree.getInstance().setLocationName(
		//	       (nearRTRICInst.get(i)).getAttributes().getLocationName());

               LOG.info("gNBID value:[{}]",(nearRTRICInst.get(i)).getAttributes().getgNBId());
	       //InMemoryDataTree.getInstance().setNearRTRICgNBId(
		//	       (nearRTRICInst.get(i)).getAttributes().getgNBId());

	       gNBDUFunction = (nearRTRICInst.get(i)).getgNBDUFunction();
	       //InMemoryDataTree.getInstance().

	       for(int j=0; j < gNBDUFunction.size(); j++) {
         	       LOG.info("GNBDUFunction ID:[{}]", (gNBDUFunction.get(j)).getIdGNBDUFunction());	

		       LOG.info("GNBDUFunction gnBID:[{}]",(gNBDUFunction.get(j)).getAttributes().getgNBId());

		       InMemoryDataTree.getInstance().hashMapgNBDUFunc.put((gNBDUFunction.get(j)).getIdGNBDUFunction(),0);

		       nRCellDU = (gNBDUFunction.get(j)).getnRCellDU();

		       for(int k=0; k < nRCellDU.size(); k++) {
     		           LOG.info("NRCellDUID:[{}]", (nRCellDU.get(k)).getIdNRCellDU());
			   LOG.info("OperState:[{}]",(nRCellDU.get(k)).getAttributes().getOperationalState());
			   LOG.info("CellState:[{}]",(nRCellDU.get(k)).getAttributes().getCellState());
		       }
	       }

	       gNBCUUPFunction = (nearRTRICInst.get(i)).getgNBCUUPFunction();

	       for(int m=0; m < gNBCUUPFunction.size(); m++) {

		       LOG.info("IDGNBCUUPFunction:[{}]", (gNBCUUPFunction.get(m)).getIdGNBCUUPFunction());
		       LOG.info("gNBCUUPId:[{}]",(gNBCUUPFunction.get(m)).getAttributes().getgNBCUUPId());

	       }

	       }

               LOG.info("Initial configuration is set successfully for nearRTRIC");
/*            } else if (mapper.readValue(message,GNBDUFunction.class) instanceof
                       GNBDUFunction) {
               GNBDUFunction initialConfig = mapper.readValue(message,
                                                              GNBDUFunction.class);
               //InMemoryDataTree.getInstance().setGNBId(
               //                           initialConfig.getAttributes().getgNBId());
                
               //InMemoryDataTree.getInstance().set
               LOG.info("Initial configuration is set successfully for GNBDUFunction");               
            } else if (mapper.readValue(message,NRCellDU.class) instanceof 
		       NRCellDU) {

	     

	    }	    */
	    LOG.info("Initial configuration is set successfully for GNBDUFunction");
               
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

    public void sendRTRICConfig(PLMNInfoModel pLMNInfoModel) {
         ObjectMapper mapper = new ObjectMapper();
        try {
            String updateMessage = mapper.writeValueAsString(pLMNInfoModel);
            DeviceData deviceData = new DeviceData();
            deviceData.setMessageType(MessageType.RTRIC_CONFIG);
            deviceData.setMessage(updateMessage);
            websocketClient.sendMessage(deviceData);
        } catch (JsonProcessingException e){
            LOG.error("JSON processing exception while sending allocation update");
        }
    }

}

