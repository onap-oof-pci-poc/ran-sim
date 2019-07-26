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
import org.onap.ransim.websocket.model.DeviceData;
import org.onap.ransim.websocket.model.MessageTypes;
import org.onap.ransim.websocket.model.ModifyNeighbor;
import org.onap.ransim.websocket.model.Neighbor;
import org.onap.ransim.websocket.model.NeighborHo;
import org.onap.ransim.websocket.model.SetConfigTopology;
import org.onap.ransim.websocket.model.Topology;
import org.onap.ransim.websocket.model.UpdateCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ConfigJsonHandler {

    private static final Logger LOG = LoggerFactory
            .getLogger(ConfigJsonHandler.class);

    private static ConfigJsonHandler configJsonHandlerInstance = null;
    public JsonObject radioAccessObj = null;
    public SetConfigTopology ncServerTopology = null;

    public String peristConfigPath;
    private RansimClientWebSocket ransimAgentWebSocket = null;
    public static String PnfName = "";
    public static String PnfUuid = "";
    private ModuleConfiguration modCfgn = null;
    public boolean isConfigTopoInitialized = false;

    private ConfigJsonHandler(String peristConfigPath) {
        this.peristConfigPath = peristConfigPath;
        ransimAgentWebSocket = new RansimClientWebSocket();
        modCfgn = new ModuleConfiguration();
        modCfgn.loadConfig();
        LOG.info("ModuleConfiguration : peristConfigPath {}",
                modCfgn.peristConfigPath);
        LOG.info("ModuleConfiguration : enodebsimIp {} {}",
                modCfgn.enodebsimIp, modCfgn.enodebsimPort);
        LOG.info("ModuleConfiguration : ransimCtrlrIp {} {}",
                modCfgn.ransimCtrlrIp, modCfgn.ransimCtrlrPort);
        LOG.info("ModuleConfiguration : vesEventListenerUrl {}",
                modCfgn.vesEventListenerUrl);
        loadJsonObject();
        ransimAgentWebSocket.initWebsocketClient(modCfgn.ransimCtrlrIp,
                modCfgn.ransimCtrlrPort, modCfgn.enodebsimIp,
                modCfgn.enodebsimPort);
    }

    public static ConfigJsonHandler getConfigJsonHandler(String peristConfigPath) {
        if (configJsonHandlerInstance == null) {
            configJsonHandlerInstance = new ConfigJsonHandler(peristConfigPath);
        }
        return configJsonHandlerInstance;
    }

    public void loadJsonObject() {
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
            radioAccessObj = reader.readObject().getJsonObject(
                    "oofpcipoc:radio-access");
            if(radioAccessObj == null) {
            	LOG.debug("radioAccessObj is null");
	    } else {
            	LOG.debug("radioAccessObj str is {}", radioAccessObj.toString());
            }

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

    public void handleSetConfigTopology(SetConfigTopology updTopo) {
        writeJsonObject(updTopo);
        loadJsonObject();
        //isConfigTopoInitialized = true;
/*
        try {
            LOG.info("Reinitializing configuration 1...");
            ConfigJsonHandler.ignoreEvents();
            LOG.info("Reinitializing configuration 2...");
            io.fd.honeycomb.infra.distro.Main.main(null);
            LOG.info("Reinitializing configuration 3...");
            ConfigJsonHandler.monitorEvents();
            LOG.info("Configuration reinitialized successfully");
        } catch (Exception e) {
            LOG.error("Unable to reinitialize configuration", e);
        }
*/
    }

    public void handleUpdateCell(UpdateCell updCell) {
        updateJsonObject(ncServerTopology, updCell);
        if (modCfgn.useNetconfDataChangeNotifn == false) {
            NbrListChangeNotifnSender.sendNotification(updCell);
        } else {
            DataChangeNotifnSender.sendNotification(updCell);
        }
    }

    public void handleUpdateTopology(UpdateCell updCell) {
        ModifyNeighbor modNbr = new ModifyNeighbor();
        modNbr.setCellId(updCell.getOneCell().getCellId());
        modNbr.setPnfName(PnfName);
        List<Neighbor> nbrs = updCell.getOneCell().getNeighborList();
        List<NeighborHo> nbrsWs = new ArrayList<NeighborHo>();
        for (Neighbor neighbor : nbrs) {
            NeighborHo aNbr = new NeighborHo();
            aNbr.setBlacklisted(neighbor.isBlacklisted());
            aNbr.setNodeId(neighbor.getNodeId());
            aNbr.setPciId(neighbor.getPhysicalCellId());
            nbrsWs.add(aNbr);
        }
        modNbr.setNeighborList(nbrsWs);

        String jsonStr = new Gson().toJson(modNbr, ModifyNeighbor.class);
        DeviceData wsMsg = new DeviceData();
        wsMsg.setType(MessageTypes.HC_TO_RC_MODANR);
        wsMsg.setMessage(jsonStr);
        ransimAgentWebSocket.sendMessage(wsMsg);
    }

    public void handleModifyCell(String jsonStr) {
        DeviceData wsMsg = new DeviceData();
        wsMsg.setType(MessageTypes.HC_TO_RC_MODPCI);
        wsMsg.setMessage(jsonStr);
        ransimAgentWebSocket.sendMessage(wsMsg);
    }

    private void updateJsonObject(SetConfigTopology updTopo, UpdateCell updCell) {
        PnfName = updTopo.getServerId();
        PnfUuid = updTopo.getUuid();
        StringBuilder sb = new StringBuilder("");
        sb.append("{\n" + "  \"oofpcipoc:radio-access\": {");
        sb.append("    \"fap-service\": [\n");
        for (int i = 0; i < updTopo.getTopology().size(); i++) {
            Topology aCell = updTopo.getTopology().get(i);
            if (updCell.getOneCell() != null) {
                if (aCell.getCellId().equals(updCell.getOneCell().getCellId())) {
                    aCell = updCell.getOneCell();
                    updTopo.getTopology().remove(i);
                    updTopo.getTopology().add(i, aCell);
                }
            }
            sb.append("      {\n" + "      \"alias\": \"");
            sb.append(aCell.getCellId());
            sb.append("\",\n" + "      \"x-0005b9-lte\": {\n"
                    + "        \"pnf-name\": \"");
            sb.append(aCell.getPnfName());
            sb.append("\",\n" + "        \"phy-cell-id-in-use\": ");
            sb.append(aCell.getPciId());
            sb.append("\n"
                    + "      },\n"
                    + "      \"cell-config\": {\n"
                    + "        \"lte\": {\n"
                    + "          \"tunnel-number-of-entries\":5,\n"
                    + "          \"lte-ran\": {\n"
                    + "            \"lte-ran-neighbor-list-in-use\": {\n"
                    + "              \"lte-ran-neighbor-list-in-use-lte-cell\" : [\n");

            for (int j = 0; j < aCell.getNeighborList().size(); j++) {
                Neighbor aNeighbor = aCell.getNeighborList().get(j);

                sb.append("              {\n");
                sb.append("                  \"plmnid\":\"")
                .append(aNeighbor.getPlmnId()).append("\", \n");
                sb.append("                  \"cid\":\"")
                .append(aNeighbor.getNodeId()).append("\", \n");
                sb.append("                  \"phy-cell-id\":")
                .append(aNeighbor.getPhysicalCellId()).append(", \n");
                sb.append("                  \"blacklisted\":")
                .append(aNeighbor.isBlacklisted()).append(" \n");
                sb.append("              }");
                if (j < aCell.getNeighborList().size() - 1)
                    sb.append(",\n");

            }

            sb.append("              ]\n");

            sb.append("            }\n" + "          }\n");
            sb.append("      }\n" + "      }\n" + "    }\n");
            if (i < updTopo.getTopology().size() - 1)
                sb.append(",\n");

        }
        sb.append("    ]\n");

        sb.append("  }\n" + "}");

        LOG.debug("NEW TOPOLOGY IS:{}", sb.toString());

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
        try {
            LOG.info("Reinitializing configuration 1...");
            ConfigJsonHandler.ignoreEvents();
            LOG.info("Reinitializing configuration 2...");
            io.fd.honeycomb.infra.distro.Main.main(null);
            LOG.info("Reinitializing configuration 3...");
            ConfigJsonHandler.monitorEvents();
            LOG.info("Configuration reinitialized successfully");
        } catch (Exception e) {
            LOG.error("Unable to reinitialize configuration", e);
        }
    }


    public void writeJsonObject(SetConfigTopology updTopo) {
        PnfName = updTopo.getServerId();
        PnfUuid = updTopo.getUuid();
        StringBuilder sb = new StringBuilder();
        sb.append("{\n" + "  \"oofpcipoc:radio-access\": {");
        sb.append("    \"fap-service\": [\n");
        for (int i = 0; i < updTopo.getTopology().size(); i++) {
            Topology aCell = updTopo.getTopology().get(i);
            sb.append("      {\n" + "      \"alias\": \"");
            sb.append(aCell.getCellId());
            sb.append("\",\n" + "      \"x-0005b9-lte\": {\n"
                    + "        \"pnf-name\": \"");
            sb.append(aCell.getPnfName());
            sb.append("\",\n" + "        \"phy-cell-id-in-use\": ");
            sb.append(aCell.getPciId());
            sb.append("\n"
                    + "      },\n"
                    + "      \"cell-config\": {\n"
                    + "        \"lte\": {\n"
                    + "          \"tunnel-number-of-entries\":5,\n"
                    + "          \"lte-ran\": {\n"
                    + "            \"lte-ran-neighbor-list-in-use\": {\n"
                    + "              \"lte-ran-neighbor-list-in-use-lte-cell\" : [\n");

            for (int j = 0; j < aCell.getNeighborList().size(); j++) {
                Neighbor aNeighbor = aCell.getNeighborList().get(j);

                sb.append("              {\n");
                sb.append("                  \"plmnid\":\"")
                .append(aNeighbor.getPlmnId()).append("\", \n");
                sb.append("                  \"cid\":\"")
                .append(aNeighbor.getNodeId()).append("\", \n");
                sb.append("                  \"phy-cell-id\":")
                .append(aNeighbor.getPhysicalCellId()).append(", \n");
                sb.append("                  \"blacklisted\":")
                .append(aNeighbor.isBlacklisted()).append(" \n");
                sb.append("              }");
                if (j < aCell.getNeighborList().size() - 1)
                    sb.append(",\n");

            }

            sb.append("              ]\n");

            sb.append("            }\n" + "          }\n");
            sb.append("      }\n" + "      }\n" + "    }\n");
            if (i < updTopo.getTopology().size() - 1)
                sb.append(",\n");

        }
        sb.append("    ]\n");

        sb.append("  }\n" + "}");

        LOG.debug("NEW TOPOLOGY IS:{}", sb.toString());

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
    public static void ignoreEvents() {
        LOG.info("ConfigJsonHandler ignoreEvents");
        ConfigJsonHandler.getConfigJsonHandler(null).isConfigTopoInitialized = false;
    }
    public static void monitorEvents() {
        LOG.info("ConfigJsonHandler monitorEvents");
        ConfigJsonHandler.getConfigJsonHandler(null).isConfigTopoInitialized = true;
    }


    public ModuleConfiguration getModuleConfiguration() {
        return modCfgn;
    }
}
