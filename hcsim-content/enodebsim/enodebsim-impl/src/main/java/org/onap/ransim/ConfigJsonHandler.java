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

package org.onap.ransim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.onap.ransim.websocket.client.RansimClientWebSocket;
import org.onap.ransim.websocket.model.Neighbor;
import org.onap.ransim.websocket.model.SetConfigTopology;
import org.onap.ransim.websocket.model.Topology;
import org.onap.ransim.websocket.model.UpdateCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ConfigJsonHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigJsonHandler.class);

    private static ConfigJsonHandler configJsonHandlerInstance = null;
    public JsonObject radioAccessObj = null;
    public SetConfigTopology ncServerTopology = null;

    public String peristConfigPath;
    private RansimClientWebSocket ransimAgentWebSocket = null;
    private String PnfName = "";

    private ConfigJsonHandler(String peristConfigPath) {
        this.peristConfigPath = peristConfigPath;
        loadJsonObject();
        ransimAgentWebSocket = new RansimClientWebSocket();
        ModuleConfiguration modCfgn = new ModuleConfiguration();
        modCfgn.loadConfig();
        LOG.info("ModuleConfiguration : peristConfigPath {}", modCfgn.peristConfigPath);
        LOG.info("ModuleConfiguration : enodebsimIp {} {}", modCfgn.enodebsimIp, modCfgn.enodebsimPort);
        LOG.info("ModuleConfiguration3 : ransimCtrlrIp {} {}", modCfgn.ransimCtrlrIp, modCfgn.ransimCtrlrPort);
        ransimAgentWebSocket.initWebsocketClient(modCfgn.ransimCtrlrIp, modCfgn.ransimCtrlrPort,
            modCfgn.enodebsimIp, modCfgn.enodebsimPort);
    }

    public static ConfigJsonHandler getConfigJsonHandler(String peristConfigPath) {
        if (configJsonHandlerInstance == null) {
            configJsonHandlerInstance = new ConfigJsonHandler(peristConfigPath);
        }
        return configJsonHandlerInstance;
    }

    public void loadJsonObject() {
        LOG.info("ConfigJsonHandler.getJsonObject is called with peristConfigPath of {}", peristConfigPath);
        if (peristConfigPath == null)
            peristConfigPath = "var/lib/honeycomb/persist/config/data.json";
        File jsonInputFile = new File(peristConfigPath);
        InputStream is = null;
        JsonReader reader = null;
        try {
            is = new FileInputStream(jsonInputFile);
            // Create JsonReader from Json.
            reader = Json.createReader(is);
            // Get the JsonObject structure from JsonReader.
            radioAccessObj = reader.readObject().getJsonObject("enodebsim:radio-access");
            LOG.info("radioAccessObj is {}", radioAccessObj);
            LOG.info("radioAccessObj str is {}", radioAccessObj.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error("Exception in getJsonObject ", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (is != null)
                    is.close();
            } catch (Exception e) {
            }
        }
    }

    public void handleUpdateTopology(String jsonStr) {
        LOG.info("ConfigJsonHandler.handleUpdateTopology jsonStr:{}", jsonStr);
        if(jsonStr.startsWith("SetConfigTopology:")) {
            jsonStr = jsonStr.substring("SetConfigTopology:".length());
            LOG.info("ConfigJsonHandler.handleUpdateTopology jsonStr1:{}",  jsonStr);
            SetConfigTopology updTopo = new Gson().fromJson(jsonStr, SetConfigTopology.class);
            writeJsonObject(updTopo);
            loadJsonObject();
        } else if(jsonStr.startsWith("UpdateCell:")) {
            jsonStr = jsonStr.substring("UpdateCell:".length());
            LOG.info("ConfigJsonHandler.handleUpdateTopology jsonStr2:{}", jsonStr);
            UpdateCell updCell = new Gson().fromJson(jsonStr, UpdateCell.class);
            updateJsonObject(ncServerTopology, updCell);
            NbrListChangeNotifnSender.sendNotification(updCell);
        } else {
            LOG.info("ConfigJsonHandler.handleUpdateTopology jsonStr3:{}", jsonStr);
        }
    }

    private void updateJsonObject(SetConfigTopology updTopo, UpdateCell updCell) {
        LOG.info("ConfigJsonHandler.updateJsonObject is called with peristConfigPath of {}", peristConfigPath);

        PnfName = updTopo.getServerId();
        StringBuilder sb = new StringBuilder();
        sb.append("{\n" +
                "  \"enodebsim:radio-access\": {");
            sb.append("    \"fap-service\": [\n");
        for(int i=0; i<updTopo.getTopology().size(); i++) {
            Topology aCell = updTopo.getTopology().get(i);
            if(aCell.getCellId().equals(updCell.getOneCell().getCellId())) {
                aCell = updCell.getOneCell();
                updTopo.getTopology().remove(i);
                updTopo.getTopology().add(i, aCell);
            }
            sb.append("      {\n" +
                    "      \"alias\": \"");
            sb.append(aCell.getCellId());
            sb.append("\",\n" +
                    "      \"x-0005b9-lte\": {\n" +
                    "        \"pnf-name\": \"");
            sb.append(aCell.getPnfName());
            sb.append("\",\n" +
                    "        \"phy-cell-id-in-use\": ");
            sb.append(aCell.getPciId());
            sb.append("\n" +
                    "      },\n" +
                    "      \"device-type\": \"standalone\",\n" +
                    "      \"capabilities\": {\n" +
                    "        \"gps-equipped\": \"true\",\n" +
                    "        \"max-tx-power\": \"12345\"\n" +
                    "      },\n" +
                    "      \"cell-config\": {\n" +
                    "        \"lte\": {\n" +
                    "          \"tunnel-number-of-entries\":5,\n" +
                    "          \"lte-ran\": {\n" +
                    //"            \"lte-ran-common\": { \"cell-identity\": \"");
            //sb.append(aCell.getCellId());
            //sb.append("\" },\n" +
            "            \"lte-ran-neighbor-list-in-use\": {\n" +
            "              \"max-lte-cell-entries\" : 4,\n" +
            "              \"lte-cell-number-of-entries\" : ");
    sb.append(aCell.getNeighborList().size());
    sb.append(",\n" +
            "              \"lte-ran-neighbor-list-in-use-lte-cell\" : [\n");

    for(int j=0; j<aCell.getNeighborList().size(); j++) {
        Neighbor aNeighbor = aCell.getNeighborList().get(j);

        sb.append("              {\n");
        sb.append("                  \"plmnid\":\"").append(aNeighbor.getNodeId()).append("\", \n");
        sb.append("                  \"cid\":\"").append(aNeighbor.getNodeId()).append("\", \n");
        sb.append("                  \"phy-cell-id\":").append(aNeighbor.getPhysicalCellId()).append(" \n");
        sb.append("              }\n");
        if(j<aCell.getNeighborList().size()-1)
            sb.append(",\n");

    }

    sb.append("              ]\n" +
            "            }\n" +
            "          }\n");
    sb.append("      }\n" +
            "      }\n" +
            "    }\n");
    if(i<updTopo.getTopology().size()-1)
        sb.append(",\n");

        }
        sb.append("    ]\n");


        sb.append("  }\n" +
                "}");


        LOG.info("NEW TOPOLOGY IS:{}",  sb.toString());

        if (peristConfigPath == null)
            peristConfigPath = "var/lib/honeycomb/persist/config/data.json";

        FileWriter fw = null;
        // try-with-resources statement based on post comment below :)
        try {
            fw = new FileWriter(peristConfigPath);
            fw.write(sb.toString());
            LOG.info("Successfully written JSON Object to File...");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error("Exception in updateJsonObject ", e);
        } finally {
            try {
                if (fw != null)
                    fw.close();
            } catch (Exception e) {
            }
        }
    }
    public void handleModifyCell(String jsonStr) {
        ransimAgentWebSocket.sendMessage(jsonStr);
    }

    public void writeJsonObject(SetConfigTopology updTopo) {
        LOG.info("ConfigJsonHandler.writeJsonObject is called with peristConfigPath of {}", peristConfigPath);

        PnfName = updTopo.getServerId();
        StringBuilder sb = new StringBuilder();
        sb.append("{\n" +
                "  \"enodebsim:radio-access\": {");
            sb.append("    \"fap-service\": [\n");
        for(int i=0; i<updTopo.getTopology().size(); i++) {
            Topology aCell = updTopo.getTopology().get(i);
            sb.append("      {\n" +
                    "      \"alias\": \"");
            sb.append(aCell.getCellId());
            sb.append("\",\n" +
                    "      \"x-0005b9-lte\": {\n" +
                    "        \"pnf-name\": \"");
            sb.append(aCell.getPnfName());
            sb.append("\",\n" +
                    "        \"phy-cell-id-in-use\": ");
            sb.append(aCell.getPciId());
            sb.append("\n" +
                    "      },\n" +
                    "      \"device-type\": \"standalone\",\n" +
                    "      \"capabilities\": {\n" +
                    "        \"gps-equipped\": \"true\",\n" +
                    "        \"max-tx-power\": \"12345\"\n" +
                    "      },\n" +
                    "      \"cell-config\": {\n" +
                    "        \"lte\": {\n" +
                    "          \"tunnel-number-of-entries\":5,\n" +
                    "          \"lte-ran\": {\n" +
                    //"            \"lte-ran-common\": { \"cell-identity\": \"");
            //sb.append(aCell.getCellId());
            //sb.append("\" },\n" +
                    "            \"lte-ran-neighbor-list-in-use\": {\n" +
                    "              \"max-lte-cell-entries\" : 4,\n" +
                    "              \"lte-cell-number-of-entries\" : ");
            sb.append(aCell.getNeighborList().size());
            sb.append(",\n" +
                    "              \"lte-ran-neighbor-list-in-use-lte-cell\" : [\n");

            for(int j=0; j<aCell.getNeighborList().size(); j++) {
                Neighbor aNeighbor = aCell.getNeighborList().get(j);

                sb.append("              {\n");
                sb.append("                  \"plmnid\":\"").append(aNeighbor.getNodeId()).append("\", \n");
                sb.append("                  \"cid\":\"").append(aNeighbor.getNodeId()).append("\", \n");
                sb.append("                  \"phy-cell-id\":").append(aNeighbor.getPhysicalCellId()).append(" \n");
                sb.append("              }\n");
                if(j<aCell.getNeighborList().size()-1)
                    sb.append(",\n");

            }

            sb.append("              ]\n" +
                    "            }\n" +
                    "          }\n");
            sb.append("      }\n" +
                    "      }\n" +
                    "    }\n");
            if(i<updTopo.getTopology().size()-1)
                sb.append(",\n");

        }
        sb.append("    ]\n");


        sb.append("  }\n" +
                "}");


        LOG.info("NEW TOPOLOGY IS:{}",  sb.toString());

        if (peristConfigPath == null)
            peristConfigPath = "var/lib/honeycomb/persist/config/data.json";

        FileWriter fw = null;
        // try-with-resources statement based on post comment below :)
        try {
            fw = new FileWriter(peristConfigPath);
            fw.write(sb.toString());
            fw.flush();
            LOG.info("Successfully written JSON Object to File...");
            ncServerTopology = updTopo;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error("Exception in writeJsonObject ", e);
        } finally {
            try {
                if (fw != null)
                    fw.close();
            } catch (Exception e) {
            }
        }
    }


    public static void testSample() {
        SetConfigTopology updTopo = new SetConfigTopology();
        updTopo.setServerId("CU1");
        List<Topology> topology = new ArrayList<Topology>();

        List<Neighbor> neighborList = new ArrayList<Neighbor>();
        Neighbor aNb1 = new Neighbor("jio", "5", 54, "CU1", "CU1");
        neighborList.add(aNb1 );
        Neighbor aNb2 = new Neighbor("jio", "6", 55, "CU2", "CU2");
        neighborList.add(aNb2 );
        Topology aCell = new Topology("CU1", 45, "1", neighborList );
        topology.add(aCell );

        List<Neighbor> neighborList2 = new ArrayList<Neighbor>();
        Neighbor aNb3 = new Neighbor("jio", "1", 45, "CU1", "CU1");
        neighborList2.add(aNb3 );
        Neighbor aNb4 = new Neighbor("jio", "2", 46, "CU2", "CU2");
        neighborList2.add(aNb4 );
        Topology aCell2 = new Topology("CU1", 54, "5", neighborList2 );
        topology.add(aCell2 );

        updTopo.setTopology(topology );
        ConfigJsonHandler.getConfigJsonHandler(null).writeJsonObject(updTopo);
    }

}
