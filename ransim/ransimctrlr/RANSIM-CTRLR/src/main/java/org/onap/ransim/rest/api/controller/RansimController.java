/*-
 * ============LICENSE_START=======================================================
 * Ran Simulator Controller
 * ================================================================================
 * Copyright (C) 2019 Wipro Limited.
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

package org.onap.ransim.rest.api.controller;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.onap.ransim.websocket.model.*;
import org.onap.ransim.rest.api.models.CellData;
import org.onap.ransim.rest.api.models.CellDetails;
import org.onap.ransim.rest.api.models.CellNeighbor;
import org.onap.ransim.rest.api.models.FmAlarmInfo;
import org.onap.ransim.rest.api.models.GetNeighborList;
import org.onap.ransim.rest.api.models.NbrDump;
import org.onap.ransim.rest.api.models.NeighborDetails;
import org.onap.ransim.rest.api.models.NeighborPmDetails;
import org.onap.ransim.rest.api.models.NeihborId;
import org.onap.ransim.rest.api.models.NetconfServers;
import org.onap.ransim.rest.api.models.OperationLog;
import org.onap.ransim.rest.api.models.PmDataDump;
import org.onap.ransim.rest.api.models.PmParameters;
import org.onap.ransim.rest.api.models.TopologyDump;
import org.onap.ransim.rest.client.RestClient;
import org.onap.ransim.websocket.model.FmMessage;
import org.onap.ransim.websocket.model.ModifyNeighbor;
import org.onap.ransim.websocket.model.ModifyPci;
import org.onap.ransim.websocket.model.Neighbor;
import org.onap.ransim.websocket.model.PmMessage;
import org.onap.ransim.websocket.model.SetConfigTopology;
import org.onap.ransim.websocket.model.Topology;
import org.onap.ransim.websocket.model.UpdateCell;
import org.onap.ransim.websocket.server.RansimWebSocketServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RansimController {
    
    static Logger log = Logger.getLogger(RansimControllerServices.class.getName());
    
    private static RansimController rsController = null;
    // private static RansimControllerDatabase rsDb = null;
    
    Properties netconfConstants = new Properties();
    int gridSize = 10;
    GridType gridType = GridType.RANDOM;
    boolean collision = false;
    String serverIdPrefix = "";
    static int numberOfCellsPerNcServer = 15;
    int numberOfMachines = 1;
    int numberOfProcessPerMc = 5;
    boolean strictValidateRansimAgentsAvailability = false;
    static public Map<String, Session> webSocketSessions = new ConcurrentHashMap<String, Session>();
    static Map<String, String> serverIdIpPortMapping = new ConcurrentHashMap<String, String>();
    static Map<String, String> globalFmCellIdUuidMap = new ConcurrentHashMap<String, String>();
    static Map<String, String> globalPmCellIdUuidMap = new ConcurrentHashMap<String, String>();
    static Map<String, String> globalNcServerUuidMap = new ConcurrentHashMap<String, String>();
    static List<String> unassignedServerIds = Collections.synchronizedList(new ArrayList<String>());
    static Map<String, List<String>> serverIdIpNodeMapping = new ConcurrentHashMap<String, List<String>>();
    int nextServerIdNumber = 1001;
    String sdnrServerIp = "";
    int sdnrServerPort = 0;
    static String sdnrServerUserid = "";
    static String sdnrServerPassword = "";
    static String dumpFileName = "";
    static long maxPciValueAllowed = 503;
    List<PmParameters> pmParameters = new ArrayList<PmParameters>();
    int next = 0;
    Set<String> cellsWithIssues = new HashSet<>();
    
    private RansimController() {
        
    }
    
    /**
     * To accesss variable of this class from another class.
     *
     * @return returns rscontroller constructor
     */
    public static synchronized RansimController getRansimController() {
        if (rsController == null) {
            rsController = new RansimController();
            new KeepWebsockAliveThread(rsController).start();
        }
        return rsController;
    }
    
    private String checkIpPortAlreadyExists(String ipPort, Map<String, String> serverIdIpPortMapping) {
        String serverId = null;
        for (String key : serverIdIpPortMapping.keySet()) {
            String value = serverIdIpPortMapping.get(key);
            if (value.equals(ipPort)) {
                serverId = key;
                break;
            }
        }
        return serverId;
    }
    
    /**
     * Add web socket sessions.
     *
     * @param ipPort
     *            ip address for the session
     * @param wsSession
     *            session details
     */
    public synchronized String addWebSocketSessions(String ipPort, Session wsSession) { // javadoc
        loadProperties();
        if (webSocketSessions.containsKey(ipPort)) {
            log.info("addWebSocketSessions: Client session " + wsSession.getId() + " for " + ipPort
                    + " already exist. Removing old session.");
            webSocketSessions.remove(ipPort);
        }
        
        log.info("addWebSocketSessions: Adding Client session " + wsSession.getId() + " for "
                + ipPort);
        webSocketSessions.put(ipPort, wsSession);
        String serverId = null;
        if (!serverIdIpPortMapping.containsValue(ipPort)) {
            if (unassignedServerIds.size() > 0) {
                log.info("addWebSocketSessions: No serverIds pending to assign for " + ipPort);
                serverId = checkIpPortAlreadyExists(ipPort, serverIdIpPortMapping);
                if (serverId == null) {
                    serverId = unassignedServerIds.remove(0);
                } else {
                    if (unassignedServerIds.contains(serverId)) {
                        unassignedServerIds.remove(serverId);
                    }
                }
                log.info("RansCtrller = Available unassigned ServerIds :" + unassignedServerIds);
                log.info("RansCtrller = addWebSocketSessions: Adding serverId " + serverId
                        + " for " + ipPort);
                serverIdIpPortMapping.put(serverId, ipPort);
                log.debug("RansCtrller = serverIdIpPortMapping >>>> :" + serverIdIpPortMapping);
                mapServerIdToNodes(serverId);
                RansimControllerDatabase rsDb = new RansimControllerDatabase();
                try {
                    
                    NetconfServers server = rsDb.getNetconfServer(serverId);
                    if (server != null) {
                        server.setIp(ipPort.split(":")[0]);
                        server.setNetconfPort(ipPort.split(":")[1]);
                        rsDb.mergeNetconfServers(server);
                    }
             
                } catch (Exception e1) {
                    log.info("Exception mapServerIdToNodes :", e1);
                }
            } else {
                log.info("addWebSocketSessions: No serverIds pending to assign for " + ipPort);
            }
        } else {
            for (String key : serverIdIpPortMapping.keySet()) {
                if (serverIdIpPortMapping.get(key).equals(ipPort)) {
                    log.info("addWebSocketSessions: ServerId " + key + " for " + ipPort
                            + " is exist already");
                    serverId = key;
                    break;
                }
            }
        }
        return serverId;
    }
    
    /**
     * Map server ID to the cells
     * 
     * @param serverId
     *            Server ID
     */
    private void mapServerIdToNodes(String serverId) {
        dumpSessionDetails();
        if (serverIdIpNodeMapping.containsKey(serverId)) {
            // already mapped.RansimController Do nothing.
        } else {
            List<String> nodeIds = new ArrayList<String>();
            RansimControllerDatabase rsDb = new RansimControllerDatabase();
            try {
                List<CellDetails> nodes = rsDb.getCellsWithNoServerIds();
                for (CellDetails cell : nodes) {
                    cell.setServerId(serverId);
                    nodeIds.add(cell.getNodeId());
                    rsDb.mergeCellDetails(cell);
                }
                serverIdIpNodeMapping.put(serverId, nodeIds);
            } catch (Exception e1) {
                log.info("Exception mapServerIdToNodes :", e1);
                
            }
        }
    }
    
    /**
     * It removes the web socket sessions.
     *
     * @param ipPort
     *            ip address of the netconf server
     */
    public synchronized void removeWebSocketSessions(String ipPort) {
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        log.info("remove websocket session request received for: " + ipPort);
        try{
            if (webSocketSessions.containsKey(ipPort)) {
                String removedServerId = null;
                for (String serverId : serverIdIpPortMapping.keySet()) {
                    String ipPortVal = serverIdIpPortMapping.get(serverId);
                    if (ipPortVal.equals(ipPort)) {
                        if (!unassignedServerIds.contains(serverId)) {
                            unassignedServerIds.add(serverId);
                            log.info(serverId + "added in unassignedServerIds");
                        }
                        NetconfServers ns =  rsDb.getNetconfServer(serverId);
                        ns.setIp(null);
                        ns.setNetconfPort(null);
                        log.info(serverId + " ip and Port set as null ");
                        rsDb.mergeNetconfServers(ns);
                        removedServerId = serverId;
                        break;
                    }
                }
                serverIdIpPortMapping.remove(removedServerId);
                
                Session wsSession = webSocketSessions.remove(ipPort);
                log.info("removeWebSocketSessions: Client session " + wsSession.getId() + " for "
                        + ipPort + " is removed. Server Id : " + removedServerId);
            } else {
                log.info("addWebSocketSessions: Client session for " + ipPort + " not exist");
            }
        }catch(Exception e){
            log.info("Exception in removeWebSocketSessions. e: " + e);
        }
        
    }
    
    /**
     * Checks the number of ransim agents running.
     *
     * @param cellsToBeSimulated
     *            number of cells to be simulated
     * @return returns true if there are enough ransim agents running
     */
    public boolean hasEnoughRansimAgentsRunning(int cellsToBeSimulated) {
        
        log.info("hasEnoughRansimAgentsRunning: numberOfCellsPerNCServer "
                + numberOfCellsPerNcServer + " , webSocketSessions.size:"
                + webSocketSessions.size() + " , cellsToBeSimulated:" + cellsToBeSimulated);
        log.info(strictValidateRansimAgentsAvailability);
        
        if (strictValidateRansimAgentsAvailability) {
            if (numberOfCellsPerNcServer * webSocketSessions.size() < cellsToBeSimulated) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * It updates the constant values in the properties file.
     */
    public void loadProperties() {
        InputStream input = null;
        try {
            input = new FileInputStream("ransim.properties");
            netconfConstants.load(input);
            serverIdPrefix = netconfConstants.getProperty("serverIdPrefix");
            numberOfCellsPerNcServer = Integer.parseInt(netconfConstants
                    .getProperty("numberOfCellsPerNCServer"));
            numberOfMachines = Integer.parseInt(netconfConstants.getProperty("numberOfMachines"));
            numberOfProcessPerMc = Integer.parseInt(netconfConstants
                    .getProperty("numberOfProcessPerMc"));
            strictValidateRansimAgentsAvailability = Boolean.parseBoolean(netconfConstants
                    .getProperty("strictValidateRansimAgentsAvailability"));
            //sdnrServerIp = netconfConstants.getProperty("sdnrServerIp");
            //sdnrServerPort = Integer.parseInt(netconfConstants.getProperty("sdnrServerPort"));
            //sdnrServerUserid = netconfConstants.getProperty("sdnrServerUserid");
            //sdnrServerPassword = netconfConstants.getProperty("sdnrServerPassword");
            sdnrServerIp = System.getenv("SDNR_IP");
            sdnrServerPort = Integer.parseInt(System.getenv("SDNR_PORT"));
            sdnrServerUserid = System.getenv("SDNR_USER");
            sdnrServerPassword = System.getenv("SDNR_PASSWORD");
            dumpFileName = netconfConstants.getProperty("dumpFileName");
            maxPciValueAllowed = Long.parseLong(netconfConstants.getProperty("maxPciValueAllowed"));
            
        } catch (Exception e) {
            log.info("Properties file error", e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception ex) {
                log.info("Properties file error", ex);
            }
        }
    }
    
    /**
     * The function adds the cell(with nodeId passed as an argument) to its
     * netconf server list if the netconf server already exists. Else it will
     * create a new netconf server in the NetconfServers Table and the cell into
     * its list.
     *
     * @param nodeId
     *            node Id of the cell
     */
    static void setNetconfServers(String nodeId) {
        
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        CellDetails currentCell = rsDb.getCellDetail(nodeId);
        
        Set<CellDetails> newList = new HashSet<CellDetails>();
        try {
            if (currentCell != null) {
                NetconfServers server = rsDb.getNetconfServer(currentCell.getServerId());
                
                if (server == null) {
                    
                    server = new NetconfServers();
                    server.setServerId(currentCell.getServerId());
                } else {
                    newList.addAll(server.getCells());
                }
                
                newList.add(currentCell);
                server.setCells(newList);
                log.info("setNetconfServers: nodeId: " + nodeId + ", X:" + currentCell.getGridX()
                        + ", Y:" + currentCell.getGridY() + ", ip: " + server.getIp()
                        + ", portNum: " + server.getNetconfPort() + ", serverId:"
                        + currentCell.getServerId());
                
                rsDb.mergeNetconfServers(server);
                
            }
            
        } catch (Exception eu) {
            log.info("/setNetconfServers Function Error", eu);
            
        }
    }
    
    /**
     * The function assigns the neighbor list for the cell(with node id passed
     * as an argument) based on the xy co-ordinate system.
     *
     * @param nodeId
     *            node Id of the cell for which the neighbor list is to be
     *            generated
     */
    private void generateNeighbors(String nodeId) {
        
        /*
         * log.info("Inside generateNeighbor Function"); EntityManagerFactory
         * emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
         * EntityManager entitymanager = emfactory.createEntityManager(); int
         * posX; int posY; String cellNodeId; String neighborNodeId; cellNodeId
         * = nodeId;
         * 
         * entitymanager.getTransaction().begin();
         * 
         * // neighbor list with the corresponding node id CellNeighbor
         * neighborList = entitymanager.find(CellNeighbor.class, cellNodeId); //
         * cell with the corresponding nodeId CellDetails currentCell =
         * entitymanager.find(CellDetails.class, cellNodeId);
         * 
         * Set<CellDetails> newList = new HashSet<CellDetails>();
         * 
         * CellDetails neighbor;
         * 
         * int neighborCollisionCount = 0; List<Long> neighborPci = new
         * ArrayList<Long>();
         * 
         * if (currentCell != null) { if (neighborList == null) { neighborList =
         * new CellNeighbor(); neighborList.setNodeId(cellNodeId);
         * 
         * } else { newList.addAll(neighborList.getNeighborList()); } posX =
         * (int) currentCell.getGridX(); posY = (int) currentCell.getGridY();
         * for (int x = (posX - 1); x <= (posX + 1); x++) { for (int y = (posY -
         * 1); y <= (posY + 1); y++) {
         * 
         * neighborNodeId = "" + x * gridSize + y; if
         * (!currentCell.getNodeId().equals(neighborNodeId)) { neighbor =
         * entitymanager.find(CellDetails.class, neighborNodeId);
         * 
         * if (gridType == GridType.HONEYCOMB) {
         * log.info("Inside generateNeighbor Function : Hexogonal grid"); //
         * Neighbor allotment for a hexagonal grid
         * 
         * if ((posY) % 2 == 0) { if (!((x == (posX - 1) && y == (posY - 1)) ||
         * (x == (posX - 1) && y == (posY + 1)))) { if (neighbor != null &&
         * (neighbor.getNodeId() != cellNodeId)) { newList.add(neighbor);
         * neighborPci.add(neighbor.getPhysicalCellId()); if
         * (currentCell.getPhysicalCellId() == neighbor.getPhysicalCellId()) {
         * currentCell.setPciCollisionDetected(true); } else {
         * neighborCollisionCount++; }
         * 
         * }
         * 
         * } } else { if (!((x == (posX + 1) && y == (posY - 1)) || (x == (posX
         * + 1) && y == (posY + 1)))) { if (neighbor != null &&
         * (neighbor.getNodeId() != cellNodeId)) { newList.add(neighbor);
         * neighborPci.add(neighbor.getPhysicalCellId()); if
         * (currentCell.getPhysicalCellId() == neighbor.getPhysicalCellId()) {
         * currentCell.setPciCollisionDetected(true); } else {
         * neighborCollisionCount++; }
         * 
         * }
         * 
         * } }
         * 
         * } else if (gridType == GridType.CIRCULAR) {
         * log.info("Inside generateNeighbor Function square grid"); // Neighbor
         * allotment for a square grid
         * 
         * if (neighbor != null && (neighbor.getNodeId() != cellNodeId)) {
         * newList.add(neighbor); neighborPci.add(neighbor.getPhysicalCellId());
         * if (currentCell.getPhysicalCellId() == neighbor.getPhysicalCellId())
         * { currentCell.setPciCollisionDetected(true); } else {
         * neighborCollisionCount++; }
         * 
         * }
         * 
         * }
         * 
         * } } }
         * 
         * if (neighborCollisionCount == newList.size()) {
         * currentCell.setPciCollisionDetected(false); }
         * 
         * int confusionCount = 0; for (int i = 0; i < newList.size(); i++) {
         * for (int j = i + 1; j < newList.size(); j++) { if ((i >=
         * neighborPci.size()) || (j >= neighborPci.size())) { continue; } if
         * (neighborPci.get(i).equals(neighborPci.get(j))) { confusionCount++; }
         * } }
         * 
         * if (confusionCount == 0) {
         * currentCell.setPciConfusionDetected(false); } else {
         * currentCell.setPciConfusionDetected(true); }
         * 
         * neighborList.setNeighborList(newList);
         * 
         * entitymanager.merge(neighborList);
         * 
         * entitymanager.flush(); entitymanager.getTransaction().commit();
         * 
         * }
         */
    }
    
    /**
     * Generates separaate list of neighbors with and without hand-off for a
     * cell.
     * 
     * @param nodeId
     *            Node Id of cell for which the neighbor list is generated
     * @return Returns GetNeighborList object
     */
    static GetNeighborList generateNeighborList(String nodeId) {
        
        EntityManagerFactory emfactory = Persistence
                .createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            //log.info("inside generateNeighborList for: " + nodeId);
            long startTimeTotal = System.currentTimeMillis();
            long startTime1 = System.currentTimeMillis();
            RansimControllerDatabase rsDb = new RansimControllerDatabase();
            //CellNeighbor neighborList = rsDb.getCellNeighbor(nodeId);
            CellNeighbor neighborList = entitymanager.find(CellNeighbor.class, nodeId);
            GetNeighborList result = new GetNeighborList();
            
            List<CellDetails> cellsWithNoHO = new ArrayList<CellDetails>();
            List<CellDetails> cellsWithHO = new ArrayList<CellDetails>();
            
            List<NeighborDetails> nbrList = new ArrayList<NeighborDetails>(
                    neighborList.getNeighborList());
            long endTime1 = System.currentTimeMillis();
            //log.info("time taken for reading details: " + (endTime1 - startTime1) );
            long readCellDetail = 0;
            long checkBlacklisted = 0;
            
            long startTime2 = System.currentTimeMillis();
            for (int i = 0; i < nbrList.size(); i++) {
                
                //log.info("i:" + i);
                long startTimeFl1 = System.currentTimeMillis();
                CellDetails nbr = entitymanager.find(CellDetails.class,nbrList.get(i).getNeigbor().getNeighborCell());
                //CellDetails nbr = rsDb.getCellDetail(nbrList.get(i).getNeigbor().getNeighborCell());
                long endTimeFl1 = System.currentTimeMillis();
                //log.info("time taken for i, read cell detail: " + (endTimeFl1 - startTimeFl1) );
                readCellDetail += (endTimeFl1 - startTimeFl1);
                
                long startTimeFl2 = System.currentTimeMillis();
                if (nbrList.get(i).isBlacklisted()) {
                    cellsWithNoHO.add(nbr);
                } else {
                    cellsWithHO.add(nbr);
                }
                long endTimeFl2 = System.currentTimeMillis();
                //log.info("time taken for i, check blacklisted: " + (endTimeFl2 - startTimeFl2) );
                checkBlacklisted += (endTimeFl2 - startTimeFl2);
            }
            long endTime2 = System.currentTimeMillis();
            //log.info("time taken for-loop: " + (endTime2 - startTime2) );
            //log.info("readCellDetail" + readCellDetail);
            //log.info("checkBlacklisted" + checkBlacklisted);
            
            long startTime3 = System.currentTimeMillis();
            result.setNodeId(nodeId);
            result.setCellsWithHo(cellsWithHO);
            result.setCellsWithNoHo(cellsWithNoHO);
            long endTime3 = System.currentTimeMillis();
            //log.info("time taken to set result: " + (endTime3 - startTime3) );
            
            long endTimeTotal = System.currentTimeMillis();
            //log.info("time taken for getNeighborList: " + (endTimeTotal - startTimeTotal) );
            return result;
            
        } catch (Exception eu) {
            log.info("/getNeighborList", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            return null;
        }
        finally {
            entitymanager.close();
            emfactory.close();
        }
    }
    
    /**
     * Updates or assigns the neighbor list for the entire database.
     *
     */
    private void updateNeighborList() {
        log.info("SVC3AS Inside getNeighborList...");
        try {
            
            String cellNodeId;
            
            for (int i = 1; i <= gridSize; i++) {
                for (int j = 1; j <= gridSize; j++) {
                    cellNodeId = "" + i * gridSize + j;
                    
                    generateNeighbors(cellNodeId);
                }
                
            }
            
            
        } catch (Exception eu) {
            log.info("/updateNeighborList", eu);
        }
    }
    
    /**
     * 
     * Reads the values PM parameter values from a dump file.
     * 
     */
    public void readPmParameters() {
        
        File dumpFile = null;
        String kpiName = "";
        PmDataDump pmDump = null;
        String jsonString = "";
        int next = 0;
        dumpFile = new File("PM_Kpi_Data.json");
        
        BufferedReader br = null;
        
        try {
            log.info("Reading dump file");
            br = new BufferedReader(new FileReader(dumpFile));
            
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            jsonString = sb.toString();
            
            pmDump = new Gson().fromJson(jsonString, PmDataDump.class);
            log.info("Dump value: " + pmDump.getKpiDump().get(0).getParameter1());
            pmParameters = new ArrayList<PmParameters>(pmDump.getKpiDump());
            
        } catch (Exception eu) {
            log.info("Exception: ", eu);
        }
    }
    
    private static String getUuid() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Sets the value for all fields in the FM data for individual cell.
     * 
     * @param networkId Network Id of the cell
     * @param ncServer Server Id of the cell
     * @param cellId Node Id of the cell
     * @param issue Contains the collision/confusion details of the cess
     * @return returns EventFm object, with all the necessary parameters.
     */
    public static EventFm setEventFm(String networkId, String ncServer, String cellId,
            FmAlarmInfo issue) {
        
        log.info("Inside generate FmData");
        EventFm event = new EventFm();
        
        try {
            
            CommonEventHeaderFm commonEventHeader = new CommonEventHeaderFm();
            FaultFields faultFields = new FaultFields();
            
            commonEventHeader.setStartEpochMicrosec(System.currentTimeMillis() * 1000);
            commonEventHeader.setSourceName(cellId);
            commonEventHeader.setReportingEntityName(ncServer);
            
            String uuid = globalFmCellIdUuidMap.get(cellId);
            if (uuid == null) {
                uuid = getUuid();
                globalFmCellIdUuidMap.put(cellId, uuid);
            }
            commonEventHeader.setSourceUuid(uuid);
            
            if (issue.getProblem().equals("Collision") || issue.getProblem().equals("Confusion")
                    || issue.getProblem().equals("CollisionAndConfusion")) {
                faultFields.setAlarmCondition("RanPciCollisionConfusionOccurred");
                faultFields.setEventSeverity("CRITICAL");
                faultFields.setEventSourceType("other");
                faultFields.setSpecificProblem(issue.getProblem());
                
                Map<String, String> alarmAdditionalInformation = new HashMap<String, String>();
                alarmAdditionalInformation.put("networkId", networkId);
                alarmAdditionalInformation.put("collisions", issue.getCollisionCount());
                alarmAdditionalInformation.put("confusions", issue.getConfusionCount());
                
                faultFields.setAlarmAdditionalInformation(alarmAdditionalInformation);
                
            }
            commonEventHeader.setLastEpochMicrosec(System.currentTimeMillis() * 1000);
            
            event.setCommonEventHeader(commonEventHeader);
            event.setFaultFields(faultFields);
            
        } catch (Exception e) {
            log.info("Exception: ", e);
        }
        
        return event;
        
    }
    
    /**
     * It checks if the cell or any of its neighbors have collision/confusion
     * issue. If there are any issues it generates the FM data for the entire
     * cluster
     * 
     * @param source
     *            The source from which the cell modification has been
     *            triggered.
     * @param cell
     *            Details of the given cell.
     * @param newNeighborList
     *            Neighbor list of the given cell.
     */
    public void generateFmData(String source, CellDetails cell,
            List<NeighborDetails> newNeighborList) {
        
        List<EventFm> listCellIssue = new ArrayList<EventFm>();
        Set<String> ncs = new HashSet<>();
        log.info("Generating Fm data");
        
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        
        FmAlarmInfo op1 = setCollisionConfusionFromFile(cell.getNodeId());
        
        if (source.equals("GUI")) {
            if (op1.getProblem().equals("CollisionAndConfusion")
                    || op1.getProblem().equals("Collision") || op1.getProblem().equals("Confusion")) {
                log.info("op1: " + op1);
                EventFm lci = setEventFm(cell.getNetworkId(), cell.getServerId(), cell.getNodeId(),
                        op1);
                //cellsWithIssues.add(cell.getNodeId());
                listCellIssue.add(lci);
                ncs.add(cell.getServerId());
                log.info("Generating Fm data for: " + cell.getNodeId());
            }
        }
        
        for (NeighborDetails cd : newNeighborList) {
            FmAlarmInfo op2 = setCollisionConfusionFromFile(cd.getNeigbor().getNeighborCell());
            CellDetails nbrCell = rsDb.getCellDetail(cd.getNeigbor().getNeighborCell());
            
            if (source.equals("GUI")) {
                if (op2.getProblem().equals("CollisionAndConfusion")
                        || op2.getProblem().equals("Collision")
                        || op2.getProblem().equals("Confusion")) {
                    EventFm lci = setEventFm(nbrCell.getNetworkId(), nbrCell.getServerId(),
                            nbrCell.getNodeId(), op2);
                    //cellsWithIssues.add(nbrCell.getNodeId());
                    log.info("FmData added:" + nbrCell.getNodeId());
                    listCellIssue.add(lci);
                    ncs.add(nbrCell.getServerId());
                    log.info("Generating Fm data for: " + nbrCell.getNodeId());
                }
            }
            
        }
        
        if (source.equals("GUI")) {
            for (String nc : ncs) {
                
                FmMessage fmDataMessage = new FmMessage();
                List<EventFm> data = new ArrayList<EventFm>();
                log.info("listCellIssue.size(): " + listCellIssue.size());
                for (EventFm cellIssue : listCellIssue) {
                    if (cellIssue.getCommonEventHeader().getReportingEntityName().equals(nc)) {
                    	data.add(cellIssue);
                        if(!cellsWithIssues.contains(cellIssue.getCommonEventHeader().getSourceName())){
                            //data.add(cellIssue);
                            cellsWithIssues.add(cellIssue.getCommonEventHeader().getSourceName());
                        }
                        
                    }
                }
                log.info("data.size(): " + data.size());
                
                if (data.size() > 0) {
                    fmDataMessage.setFmEventList(data);
                    log.info("Sending FM message: ");
                    sendFmData(nc, fmDataMessage);
                }
                
            }
        }
                
    }
    
    /**
     * Sends the FM data message to the netconf agent through the ransim
     * websocket server.
     * 
     * @param serverId
     *            server id of the netconf agent
     * @param fmDataMessage
     *            FM message to be sent
     */
    public void sendFmData(String serverId, FmMessage fmDataMessage) {
        
        log.info("inside sendFmData");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(fmDataMessage);
        
        log.info("Fm Data jsonStr: " + jsonStr);
        
        String ipPort = serverIdIpPortMapping.get(serverId);
        
        if (ipPort != null && !ipPort.trim().equals("")) {
            
            String[] ipPortArr = ipPort.split(":");
            log.info("Connection estabilished with ip: " + ipPort);
            if (ipPort != null && !ipPort.trim().equals("")) {
                Session clSess = webSocketSessions.get(ipPort);
                if (clSess != null) {
                    log.info("FM message sent.");
                    RansimWebSocketServer.sendFmMessage(jsonStr, clSess);
                } else {
                    log.info("No client session for " + ipPort);
                }
            } else {
                log.info("No client for " + serverId);
            }
        } else {
            log.info("No client for ");
        }
        
    }
    
    /**
     * Sets values for all the parameters in the PM message 
     * 
     * @param nodeIdBad List of node Ids with bad performance values
     * @param nodeIdPoor List of node Ids with poor performance values
     * @return It returns the pm message
     */
    public List<String> generatePmData(String nodeIdBad, String nodeIdPoor) {
        
        List<String> result = new ArrayList<>();
        RansimControllerDatabase rcDb = new RansimControllerDatabase();
        
        String parameter1 = "";
        String successValue1 = "";
        String badValue1 = "";
        String poorValue1 = "";
        String parameter2 = "";
        String successValue2 = "";
        String badValue2 = "";
        String poorValue2 = "";
        
        try {
            
            if (next >= pmParameters.size()) {
                next = 0;
                log.info("next : " + next);
            }
            try {
                log.info("next : " + next);
                parameter1 = pmParameters.get(next).getParameter1();
                successValue1 = pmParameters.get(next).getSuccessValue1();
                badValue1 = pmParameters.get(next).getBadValue1();
                poorValue1 = pmParameters.get(next).getPoorValue1();
                parameter2 = pmParameters.get(next).getParameter2();
                successValue2 = pmParameters.get(next).getSuccessValue2();
                badValue2 = pmParameters.get(next).getBadValue2();
                poorValue2 = pmParameters.get(next).getPoorValue2();
                next++;
            } catch (Exception e) {
                log.info("Exception: ", e);
            }
            
            List<NetconfServers> cnl = rcDb.getNetconfServersList();
            log.info("obtained data from db");
            String[] cellIdsBad = null;
            String[] cellIdsPoor = null;
            Set<String> nodeIdsBad = new HashSet<String>();
            Set<String> nodeIdsPoor = new HashSet<String>();
            
            if (nodeIdBad != null) { 
                cellIdsBad = nodeIdBad.split(",");
                for (int a = 0; a < cellIdsBad.length; a++) {
                    nodeIdsBad.add(cellIdsBad[a].trim());
                }   
            }
            if (nodeIdPoor != null) {
                cellIdsPoor = nodeIdPoor.split(",");
                for (int a = 0; a < cellIdsPoor.length; a++) {
                    nodeIdsPoor.add(cellIdsPoor[a].trim());
                }
            }
            
            for (int i = 0; i < cnl.size(); i++) {
                
                long startTime = System.currentTimeMillis();
                List<CellDetails> cellList = new ArrayList<CellDetails>(cnl.get(i).getCells());
                List<EventPm> data = new ArrayList<EventPm>();
                
                for (int j = 0; j < cellList.size(); j++) {
                    
                    long startTimeCell = System.currentTimeMillis();
                    String nodeId = cellList.get(j).getNodeId();
                    //CellNeighbor cellNeighborList = rcDb.getCellNeighbor(nodeId);
                    EventPm event = new EventPm();
                    //log.info("setting commonEventHandler");
                    CommonEventHeaderPm commonEventHeader = new CommonEventHeaderPm();
                    commonEventHeader.setSourceName(nodeId);
                    commonEventHeader.setStartEpochMicrosec(System.currentTimeMillis() * 1000);
                    String uuid = globalPmCellIdUuidMap.get(nodeId);
                    if (uuid == null) {
                        uuid = getUuid();
                        globalPmCellIdUuidMap.put(nodeId, uuid);
                    }
                    commonEventHeader.setSourceUuid(uuid);
                    
                    Measurement measurement = new Measurement();
                    measurement.setMeasurementInterval(180);
                    
                    GetNeighborList cellNbrList = generateNeighborList(nodeId);
                    
                    long startTimeCheckBadPoor = System.currentTimeMillis();
                    
                    boolean checkBad = false;
                    boolean checkPoor = false;
                    int countBad = 0;
                    int countPoor = 0;
                    
                    if (nodeIdsBad.contains(nodeId)) {
                        checkBad = true;
                        countBad = (int) (cellNbrList.getCellsWithHo().size() * 0.2);
                    }
                    if (nodeIdsPoor.contains(nodeId)) {
                        checkPoor = true;
                        countPoor = (int) (cellNbrList.getCellsWithHo().size() * 0.2);
                    }
                    
                    long endTimeCheckBadPoor = System.currentTimeMillis();
                    //log.info("CheckBadPoor: " + ( endTimeCheckBadPoor - startTimeCheckBadPoor ) );
                    
                    List<AdditionalMeasurements> additionalMeasurements = new ArrayList<AdditionalMeasurements>();
                    if (checkPoor || checkBad ) {
                        
                        //log.info("checkPoor: " + checkPoor);
                        //log.info("checkBad: " + checkBad);
                        
                        CellDetails[] orderedList = new CellDetails[(int) maxPciValueAllowed];
                        for (int l = 0; l < cellNbrList.getCellsWithHo().size(); l++) {
                            int pos = (int) cellNbrList.getCellsWithHo().get(l).getPhysicalCellId();
                            orderedList[pos] = cellNbrList.getCellsWithHo().get(l);
                        }
                        
                        for (int k = 0; k < orderedList.length; k++) {
                            if (orderedList[k] != null) {
                                AdditionalMeasurements addMsmnt = new AdditionalMeasurements();
                                addMsmnt.setName(orderedList[k].getNodeId());
                                Map<String, String> hashMap = new HashMap<String, String>();
                                
                                //Map<String, String> obj0 = new HashMap<>();
                                hashMap.put("networkId", orderedList[k].getNetworkId());
                                //arrayOfNamedHashMap.add(obj0);
                                //Map<String, String> obj1 = new HashMap<>();
                                hashMap.put(parameter1, successValue1);
                                //arrayOfNamedHashMap.add(obj1);
                                
                                //Map<String, String> obj2 = new HashMap<>();
                                if (checkBad == true) {
                                    
                                    if (countBad > 0) {
                                        log.info("countBad: " + countBad);
                                        hashMap.put(parameter2, badValue2);
                                        countBad--;
                                    } else {
                                        hashMap.put(parameter2, successValue2);
                                    }
                                    
                                } else if (checkPoor == true) {
                                    if (countPoor > 0) {
                                        log.info("countBad: " + countPoor);
                                        hashMap.put(parameter2, poorValue2);
                                        countPoor--;
                                    } else {
                                        hashMap.put(parameter2, successValue2);
                                    }
                                    
                                }
                                //arrayOfNamedHashMap.add(obj2);
                                
                                addMsmnt.setHashMap(hashMap);
                                
                                additionalMeasurements.add(addMsmnt);
                            }
                            
                        }
                    } else {
                        for (int k = 0; k < cellNbrList.getCellsWithHo().size(); k++) {
                            AdditionalMeasurements addMsmnt = new AdditionalMeasurements();
                            addMsmnt.setName(cellNbrList.getCellsWithHo().get(k).getNodeId());
                            Map<String, String> hashMap = new HashMap<String, String>();
                            
                            //Map<String, String> obj0 = new HashMap<>();
                            hashMap.put("networkId", cellNbrList.getCellsWithHo().get(k)
                                    .getNetworkId());
                            //arrayOfNamedHashMap.add(obj0);
                            //Map<String, String> obj1 = new HashMap<>();
                            hashMap.put(parameter1, successValue1);
                            //arrayOfNamedHashMap.add(obj1);
                            //Map<String, String> obj2 = new HashMap<>();
                            hashMap.put(parameter2, successValue2);
                            //arrayOfNamedHashMap.add(obj2);
                            //addMsmnt.setArrayOfNamedHashMap(arrayOfNamedHashMap);
                            addMsmnt.setHashMap(hashMap);
                            additionalMeasurements.add(addMsmnt);
                        }
                    }
                    
                    commonEventHeader.setLastEpochMicrosec(System.currentTimeMillis() * 1000);
                    //log.info("LastEpoch - StartEpoch" + ( commonEventHeader.getLastEpochMicrosec() - commonEventHeader.getStartEpochMicrosec() ) );
                    
                    measurement.setAdditionalMeasurements(additionalMeasurements);
                    
                    event.setCommonEventHeader(commonEventHeader);
                    event.setMeasurement(measurement);
                    
                    data.add(event);
                    long endTimeCell = System.currentTimeMillis();
                    //log.info("Time taken for cell "+ nodeId + " : " + (endTimeCell - startTimeCell) );
                }
                
                long endTime = System.currentTimeMillis();
                log.info("Time taken for generating PM data for "+ cnl.get(i).getServerId()+ " : " + (endTime - startTime));
                PmMessage msg = new PmMessage();
                
                if(data.size() > 0){
                    msg.setEventPmList(data);
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(msg);
                    sendPmdata(cnl.get(i).getServerId(), jsonStr);
                    
                    //log.info(": " + jsonStr);
                    result.add(jsonStr);
                }
                
            }
        } catch (Exception e) {
            log.info("Exception in string builder", e);
        }
        
        return result;
    }
    
    /**
     * Sends PM message to the netconf agent through the websocket server.
     * 
     * @param serverId
     *            Netconf agent - Server ID to which the message is sent.
     * @param pmMessage
     *            PM message to be sent.
     */
    void sendPmdata(String serverId, String pmMessage) {
        
        log.info("Sending PM message to netconf agent");
        
        String ipPort = serverIdIpPortMapping.get(serverId);
        
        if (ipPort != null && !ipPort.trim().equals("")) {
            
            String[] ipPortArr = ipPort.split(":");
            if (ipPort != null && !ipPort.trim().equals("")) {
                
                Session clSess = webSocketSessions.get(ipPort);
                log.info("PM message. Netconf agent IP:" + ipPort);
                if (clSess != null) {
                    RansimWebSocketServer.sendPmMessage(pmMessage, clSess);
                    log.info("Pm Data jsonStr: " + pmMessage);
                    //log.info("jsonStr: " + pmMessage);
                    log.info("PM message sent to netconf agent");
                } else {
                    log.info("No client session for " + ipPort);
                }
            } else {
                log.info("Pm message not sent, ipPort is null");
            }
        } else {
            
            log.info("Pm message not sent, ipPort is null. Server Id: " + serverId);
        }
        
    }
    
    /**
     * The function generates a new cluster in the database based on the input
     * parameters.
     *
     * @param gdSize
     *            number of cells along a side
     * @param gdType
     *            honeycomb, circular or generate from file
     * @param numberOfClusters
     *            number of clusters
     * @param clusterLevel
     *            cluster level
     * @throws Exception
     *             throws exception in the function
     */
    public void generateCluster(int gdSize, int gdType, int numberOfClusters, int clusterLevel)
            throws Exception {
        gridSize = gdSize;
        gridType = GridType.fromInteger(gdType);
        
        if (gridType == GridType.HONEYCOMB) {
            log.info("Inside StartSimulatios...gridType HONEYCOMB");
            for (int repeat = 0; repeat < numberOfClusters; repeat++) {
                
                generateHexagonCluster(clusterLevel);
                updateNeighborList();
                
            }
        } else if (gridType == GridType.CIRCULAR) {
            log.info("Inside StartSimulatios...gridType CIRCULAR");
            for (int repeat = 0; repeat < numberOfClusters; repeat++) {
                generateSquareCluster(clusterLevel);
                updateNeighborList();
                
            }
        } else if (gridType == GridType.RANDOM) {
            log.info("Inside StartSimulatios...gridType RANDOM");
            generateClusterFromFile();
            
        } else {
            log.info("Inside StartSimulatios...Unknown gridType " + gridType);
        }
    }
    
    private static double degToRadians(double angle) {
        double radians = 57.2957795;
        return (angle / radians);
    }
    
    private static double metersDeglon(double angle) {
        
        double d2r = degToRadians(angle);
        return ((111415.13 * Math.cos(d2r)) - (94.55 * Math.cos(3.0 * d2r)) + (0.12 * Math
                .cos(5.0 * d2r)));
        
    }
    
    private static double metersDeglat(double angle) {
        
        double d2r = degToRadians(angle);
        return (111132.09 - (566.05 * Math.cos(2.0 * d2r)) + (1.20 * Math.cos(4.0 * d2r)) - (0.002 * Math
                .cos(6.0 * d2r)));
        
    }
    
    /**
     * generateClusterFromFile()
     * @throws IOException
     */
    static void generateClusterFromFile() throws IOException {
        
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        log.info("Inside generateClusterFromFile");
        File dumpFile = null;
        String cellDetailsString = "";
        
        dumpFile = new File(dumpFileName);
        
        BufferedReader br = null;
        try {
            long startTimeReadingFromFile = System.currentTimeMillis();
            log.info(startTimeReadingFromFile);
            long startTimeStringBuilder = System.currentTimeMillis();
            log.info("Reading dump file");
            br = new BufferedReader(new FileReader(dumpFile));
            
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            log.info(startTimeStringBuilder);
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            cellDetailsString = sb.toString();
            
            long endTimeStringBuilder = System.currentTimeMillis();
            log.info("Time taken for string builder : "
                    + (endTimeStringBuilder - startTimeStringBuilder));
            
            long startTimeJsontoTopologyDump = System.currentTimeMillis();
            TopologyDump dumpTopo = new Gson().fromJson(cellDetailsString, TopologyDump.class);
            CellDetails cellsDb = new CellDetails();
            long endTimeJsontoTopologyDump = System.currentTimeMillis();
            log.info("Time taken for Json to TopologyDump : "
                    + (endTimeJsontoTopologyDump - startTimeJsontoTopologyDump));
            
            long startTimeTopologyDumpToDatabase = System.currentTimeMillis();
            long totalTimeDatabase = 0;
            long totalTimeServerIdMapping = 0;
            long totalTimeSetServers = 0;
            log.info("dumpTopo.getCellList().size():" + dumpTopo.getCellList().size());
            for (int i = 0; i < dumpTopo.getCellList().size(); i++) {
                Gson g = new Gson();
                String pnt = g.toJson(dumpTopo.getCellList().get(i));
                log.info("Creating Cell:" + pnt);
                log.info("Creating Cell:" + dumpTopo.getCellList().get(i).getCell().getNodeId());
                
                long startTimedatabase = System.currentTimeMillis();
                log.info(startTimeTopologyDumpToDatabase);
                log.info(startTimedatabase);
                cellsDb = new CellDetails();
                entitymanager.getTransaction().begin();
                cellsDb.setNodeId(dumpTopo.getCellList().get(i).getCell().getNodeId());
                cellsDb.setPhysicalCellId(dumpTopo.getCellList().get(i).getCell()
                        .getPhysicalCellId());
                cellsDb.setLongitude(dumpTopo.getCellList().get(i).getCell().getLongitude());
                cellsDb.setLatitude(dumpTopo.getCellList().get(i).getCell().getLatitude());
                cellsDb.setServerId(dumpTopo.getCellList().get(i).getCell().getPnfName());
                if (!unassignedServerIds.contains(cellsDb.getServerId())) {
                    unassignedServerIds.add(cellsDb.getServerId());
                }
                cellsDb.setNetworkId(dumpTopo.getCellList().get(i).getCell().getNetworkId());
                
                double lon = Float.valueOf(dumpTopo.getCellList().get(i).getCell().getLongitude());
                double lat = Float.valueOf(dumpTopo.getCellList().get(i).getCell().getLatitude());
                
                double xx = (lon - 0) * metersDeglon(0);
                double yy = (lat - 0) * metersDeglat(0);
                
                double rad = Math.sqrt(xx * xx + yy * yy);
                
                if (rad > 0) {
                    double ct = xx / rad;
                    double st = yy / rad;
                    xx = rad * ((ct * Math.cos(0)) + (st * Math.sin(0)));
                    yy = rad * ((st * Math.cos(0)) - (ct * Math.sin(0)));
                }
                
                cellsDb.setScreenX((float) (xx));
                cellsDb.setScreenY((float) (yy));
                
                long endTimedatabase = System.currentTimeMillis();
                totalTimeDatabase += (endTimedatabase - startTimedatabase);
                log.info("time converting database: " + (endTimedatabase - startTimedatabase));
                log.info("total time converting database: " + totalTimeDatabase);
                
                long startTimeServerIdMapping = System.currentTimeMillis();
                log.info(startTimeServerIdMapping);
                List<String> attachedNoeds = serverIdIpNodeMapping.get(cellsDb.getServerId());
                log.info("Attaching Cell:" + dumpTopo.getCellList().get(i).getCell().getNodeId()
                        + " to " + cellsDb.getServerId());
                if (attachedNoeds == null) {
                    attachedNoeds = new ArrayList<String>();
                }
                attachedNoeds.add(cellsDb.getNodeId());
                serverIdIpNodeMapping.put(cellsDb.getServerId(), attachedNoeds);
                if (attachedNoeds.size() > numberOfCellsPerNcServer) {
                    log.warn("Attaching Cell:"
                            + dumpTopo.getCellList().get(i).getCell().getNodeId() + " to "
                            + cellsDb.getServerId()
                            + ", But it is exceeding numberOfCellsPerNcServer "
                            + numberOfCellsPerNcServer);
                }
                
                entitymanager.merge(cellsDb);
                entitymanager.flush();
                entitymanager.getTransaction().commit();
                
                long endTimeServerIdMapping = System.currentTimeMillis();
                totalTimeServerIdMapping += (endTimeServerIdMapping - startTimeServerIdMapping);
                
                log.info("Total Time taken for server id mapping : " + totalTimeServerIdMapping);
                
                long startTimeSetServers = System.currentTimeMillis();
                log.info(startTimeServerIdMapping);
                setNetconfServers(cellsDb.getNodeId());
                
                long endTimeSetServers = System.currentTimeMillis();
                totalTimeSetServers += (endTimeSetServers - startTimeSetServers);
                
                log.info("Total Time taken to set netconf servers : " + totalTimeSetServers);
                
            }
            long endTimeTopologyDumpToDatabase = System.currentTimeMillis();
            log.info("Time taken for Topology Dump To Database : "
                    + (endTimeTopologyDumpToDatabase - startTimeTopologyDumpToDatabase));
            long endTimeReadingFromFile = System.currentTimeMillis();
            log.info("Time taken for reading from file : "
                    + (endTimeReadingFromFile - startTimeReadingFromFile));
            
            dumpSessionDetails();
            
            try {
                long timeSetColloision = 0;
                long timeNeighborList = 0;
                long timeEntityManagercommit = 0;
                long timeEntityManagerflush = 0;
                long startTimeReadingNeighbors = System.currentTimeMillis();
                for (int i = 0; i < dumpTopo.getCellList().size(); i++) {
                    
                    String cellNodeId = dumpTopo.getCellList().get(i).getCell().getNodeId();
                    entitymanager.getTransaction().begin();
                    
                    // neighbor list with the corresponding node id
                    CellNeighbor neighborList = entitymanager.find(CellNeighbor.class, cellNodeId);
                    // cell with the corresponding nodeId
                    CellDetails currentCell = entitymanager.find(CellDetails.class, cellNodeId);
                    
                    Set<NeighborDetails> newCell = new HashSet<NeighborDetails>();
                    
                    if (currentCell != null) {
                        long startTimeNeighborList = System.currentTimeMillis();
                        log.info(startTimeNeighborList);
                        if (neighborList == null) {
                            neighborList = new CellNeighbor();
                            neighborList.setNodeId(cellNodeId);
                        }
                        List<NbrDump> neighboursFromFile = dumpTopo.getCellList().get(i)
                                .getNeighbor();
                        log.info("Creating Neighbor for Cell :" + cellNodeId);
                        for (NbrDump a : neighboursFromFile) {
                            String id = a.getNodeId().trim();
                            boolean noHo = Boolean.parseBoolean(a.getBlacklisted().trim());
                            // String neighborNodeId = (a);
                            CellDetails neighborCell = entitymanager.find(CellDetails.class, id);
                            NeighborDetails neighborDetails = new NeighborDetails(new NeihborId(
                                    currentCell.getNodeId(), neighborCell.getNodeId()), noHo);
                            
                            newCell.add(neighborDetails);
                        }
                        long endTimeNeighborList = System.currentTimeMillis();
                        timeNeighborList += (endTimeNeighborList - startTimeNeighborList);
                        log.info("Total time for seting neighbor list: " + timeNeighborList);
                        
                        neighborList.setNeighborList(newCell);
                        entitymanager.merge(neighborList);
                        
                        long startTimeEntityManagerflush = System.currentTimeMillis();
                        entitymanager.flush();
                        long endTimeEntityManagerflush = System.currentTimeMillis();
                        timeEntityManagerflush += (endTimeEntityManagerflush - startTimeEntityManagerflush);
                        log.info("Total time for entity manager flush: " + timeEntityManagerflush);
                        
                        long startTimeEntityManagercommit = System.currentTimeMillis();
                        log.info(startTimeEntityManagercommit);
                        entitymanager.getTransaction().commit();
                        
                        long endTimeEntityManagercommit = System.currentTimeMillis();
                        timeEntityManagercommit += (endTimeEntityManagercommit - startTimeEntityManagercommit);
                        log.info("Total time for entity manager commit: " + timeEntityManagercommit);
                        
                        long startTimeSetCollision = System.currentTimeMillis();
                        setCollisionConfusionFromFile(cellNodeId);
                        // setCellColor(dumpTopo.getCellList().get(i).getCell().getNodeId());
                        long endTimeSetCollision = System.currentTimeMillis();
                        timeSetColloision += (endTimeSetCollision - startTimeSetCollision);
                        
                    }
                    
                }
                log.info("Time taken for setting collision: " + (timeSetColloision));
                
                long endTimeReadingNeighbors = System.currentTimeMillis();
                log.info("Time taken for reading neighbors from file and setting confusion/collision: "
                        + (endTimeReadingNeighbors - startTimeReadingNeighbors));
                
                /*
                 * for (int i = 0; i < dumpTopo.getCellList().size(); i++) {
                 * setCellColor
                 * (dumpTopo.getCellList().get(i).getCell().getNodeId()); }
                 */
            } catch (Exception e1) {
                log.info("Exception generateClusterFromFile :", e1);
                if (entitymanager.getTransaction().isActive()) {
                    entitymanager.getTransaction().rollback();
                }
            }
            
            try {
                
                long startTimeSectorNumber = System.currentTimeMillis();
                for (int i = 0; i < dumpTopo.getCellList().size(); i++) {
                    
                    CellData icellData = dumpTopo.getCellList().get(i);
                    CellDetails icell = entitymanager.find(CellDetails.class, icellData.getCell()
                            .getNodeId());
                    
                    int icount = icell.getSectorNumber();
                    /*
                     * if (icount > 0) { continue; }
                     */
                    
                    if (icount == 0) {
                        entitymanager.getTransaction().begin();
                        log.info("Setting sectorNumber for Cell(i) :" + icell.getNodeId());
                        int jcount = 0;
                        for (int j = (i + 1); j < dumpTopo.getCellList().size(); j++) {
                            
                            CellData jcellData = dumpTopo.getCellList().get(j);
                            if (icellData.getCell().getLatitude()
                                    .equals(jcellData.getCell().getLatitude())) {
                                if (icellData.getCell().getLongitude()
                                        .equals(jcellData.getCell().getLongitude())) {
                                    
                                    if (icount == 0) {
                                        icount++;
                                        jcount = icount + 1;
                                    }
                                    
                                    CellDetails jcell = entitymanager.find(CellDetails.class,
                                            dumpTopo.getCellList().get(j).getCell().getNodeId());
                                    // jcount = jcell.getSectorNumber();
                                    
                                    jcell.setSectorNumber(jcount);
                                    log.info("Setting sectorNumber for Cell(j) :"
                                            + jcell.getNodeId() + " icell: " + icell.getNodeId()
                                            + " Sector number: " + jcount);
                                    entitymanager.merge(jcell);
                                    // entitymanager.flush();
                                    jcount++;
                                    if (jcount > 3) {
                                        break;
                                    }
                                }
                            }
                        }
                        icell.setSectorNumber(icount);
                        entitymanager.merge(icell);
                        entitymanager.flush();
                        entitymanager.getTransaction().commit();
                    }
                    
                }
                
                long endTimeSectorNumber = System.currentTimeMillis();
                log.info("Time taken for setting sector number: "
                        + (endTimeSectorNumber - startTimeSectorNumber));
                
            } catch (Exception e3) {
                log.info("Exception generateClusterFromFile :", e3);
                if (entitymanager.getTransaction().isActive()) {
                    entitymanager.getTransaction().rollback();
                }
            }
            
        } catch (Exception e) {
            log.info("Exception generateClusterFromFile :", e);
            e.printStackTrace();
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
        } finally {
            br.close();
            entitymanager.close();
            emfactory.close();
        }
    }
    
    static FmAlarmInfo setCollisionConfusionFromFile(String cellNodeId) {
        
        FmAlarmInfo result = new FmAlarmInfo();
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        
        try {
            
            boolean collisionDetected = false;
            boolean confusionDetected = false;
            List<Long> nbrPcis = new ArrayList<Long>();
            List<Long> ConfusionPcis = new ArrayList<Long>();
            int collisionCount = 0;
            int confusionCount = 0;
            
            // neighbor list with the corresponding node id
            //CellNeighbor neighborList = rsDb.getCellNeighbor(cellNodeId);
            
            // cell with the corresponding nodeId
            CellDetails currentCell = rsDb.getCellDetail(cellNodeId);
            
            log.info("Setting confusion/collision for Cell :" + cellNodeId);
            
            GetNeighborList cellNbrDetails = generateNeighborList(cellNodeId);
            
            for (CellDetails firstLevelNbr : cellNbrDetails.getCellsWithHo()) {
                
                /*for (CellDetails secondLevelNbr : cellNbrDetails.getCellsWithHo()) {
                    if (!firstLevelNbr.getNodeId().equals(secondLevelNbr.getNodeId())) {
                        if (firstLevelNbr.getPhysicalCellId() == secondLevelNbr.getPhysicalCellId()) {
                            confusionDetected = true;
                            confusionCount++;
                        }
                    }
                    
                }*/
                
               if (nbrPcis.contains(new Long(firstLevelNbr.getPhysicalCellId()))) {
                    confusionDetected = true;
                    if(ConfusionPcis.contains(firstLevelNbr.getPhysicalCellId())){
                        confusionCount++;
                    }
                    else{
                        ConfusionPcis.add(firstLevelNbr.getPhysicalCellId());
                        confusionCount = confusionCount + 2;
                    }
                    
                } else {
                    nbrPcis.add(new Long(firstLevelNbr.getPhysicalCellId()));
                }
                 
                
                if (currentCell.getPhysicalCellId() == firstLevelNbr.getPhysicalCellId()) {
                    collisionDetected = true;
                    collisionCount++;
                }
            }
            
            currentCell.setPciCollisionDetected(collisionDetected);
            currentCell.setPciConfusionDetected(confusionDetected);
                        
            if (!currentCell.isPciCollisionDetected() && !currentCell.isPciConfusionDetected()) {
                currentCell.setColor("#BFBFBF"); // GREY - No Issues
                result.setProblem("No Issues");
            } else if (currentCell.isPciCollisionDetected() && currentCell.isPciConfusionDetected()) {
                currentCell.setColor("#C30000"); // BROWN - Cell has both
                                                 // collision & confusion
                result.setProblem("CollisionAndConfusion");
                
            } else if (currentCell.isPciCollisionDetected()) {
                currentCell.setColor("#FF0000"); // RED - Cell has collision
                result.setProblem("Collision");
                
            } else if (currentCell.isPciConfusionDetected()) {
                currentCell.setColor("#E88B00"); // ORANGE - Cell has confusion
                result.setProblem("Confusion");
                
            } else {
                currentCell.setColor("#BFBFBF"); // GREY - No Issues
                result.setProblem("No Issues");
            }
            
            result.setCollisionCount("" + collisionCount);
            result.setConfusionCount("" + confusionCount);
            
            rsDb.mergeCellDetails(currentCell);
            
            return result;
            
        } catch (Exception e2) {
            log.info("setCollisionConfusionFromFile :", e2);
            
            return null;
        }
        
    }
    
    static void checkCollisionAfterModify() {
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        try {
            List<CellDetails> checkCollisionConfusion = rsDb.getCellsWithCollisionOrConfusion();
            
            for (int i = 0; i < checkCollisionConfusion.size(); i++) {
                log.info(checkCollisionConfusion.get(i).getNodeId());
                setCollisionConfusionFromFile(checkCollisionConfusion.get(i).getNodeId());
            }
        } catch (Exception eu) {
            log.info("checkCollisionAfterModify", eu);
        }
    }
        
    /**
     * Generate Hexagonal cluster.
     *
     * @param clusterLevel
     *            cluster level for the simulation
     */
    private void generateHexagonCluster(int clusterLevel) {
        Random rand = new Random();
        String nodeId;
        CellDetails check;
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        
        int gridX = rand.nextInt(gridSize) + 1;
        int gridY = rand.nextInt(gridSize) + 1;
        boolean collision = false;
        log.info("Inside generateHexagonCluster function ..." + gridX + ".." + gridY);
        
        int clusterSize = (clusterLevel * 2) + 1;
        
        int count = clusterLevel + 1;
        int localX = gridX - ((clusterLevel + 1) / 2);
        int iteration = 0;
        
        for (int j = gridY - clusterLevel; j <= (gridY + clusterLevel); j++) {
            
            for (int i = 0; i < count; i++) {
                int posY = j;
                int posX = localX + i;
                
                nodeId = "" + ((posX) * gridSize) + (posY);
                check = entitymanager.find(CellDetails.class, nodeId);
                if (check == null) {
                    
                    if (posX <= gridSize && posY <= gridSize && posX >= 1 && posY >= 1) {
                        entitymanager.getTransaction().begin();
                        int id = (((posX - 1) * gridSize + posY) / numberOfCellsPerNcServer) + 1;
                        String serverid = serverIdPrefix + Integer.toString(1000 + id);
                        CellDetails newCell = new CellDetails();
                        newCell.setGridX(posX);
                        newCell.setGridY(posY);
                        newCell.setServerId(serverid);
                        newCell.setNodeId(nodeId);
                        if (collision == false) {
                            newCell.setPhysicalCellId(((int) newCell.getGridX() % clusterSize)
                                    * clusterSize + ((int) newCell.getGridY() % clusterSize));
                        } else {
                            newCell.setPhysicalCellId(rand.nextInt(10));
                        }
                        log.info("generateHexagonCluster Adding Cell at X:" + gridX + ",Y:" + gridY
                                + "nodeId:" + nodeId + " serverid:" + serverid);
                        
                        entitymanager.merge(newCell);
                        entitymanager.flush();
                        entitymanager.getTransaction().commit();
                        
                        setNetconfServers(nodeId);
                        
                    }
                }
                
            }
            
            // check if column position is odd or even
            if (gridY % 2 == 0) {
                if (clusterLevel % 2 == 0) {
                    if (j < gridY) {
                        if (iteration % 2 == 1) {
                            localX--;
                        }
                    } else {
                        if (iteration % 2 == 0) {
                            localX++;
                        }
                    }
                } else {
                    if (j < gridY) {
                        if (iteration % 2 == 0) {
                            localX--;
                        }
                    } else {
                        if (iteration % 2 == 1) {
                            localX++;
                        }
                    }
                }
            } else {
                if (clusterLevel % 2 == 0) {
                    if (j < gridY) {
                        if (iteration % 2 == 0) {
                            localX--;
                        }
                    } else {
                        if (iteration % 2 == 1) {
                            localX++;
                        }
                    }
                } else {
                    if (j < gridY) {
                        if (iteration % 2 == 1) {
                            localX--;
                        }
                    } else {
                        if (iteration % 2 == 0) {
                            localX++;
                        }
                    }
                }
            }
            
            if (j < gridY) {
                count++;
            } else {
                count--;
            }
            
            iteration++;
        }
    }
    
    /**
     * Generate Square cluster.
     *
     * @param clusterLevel
     *            cluster level for the simulation
     */
    private void generateSquareCluster(int clusterLevel) {
        boolean collision = false;
        Random rand = new Random();
        String nodeId;
        CellDetails check;
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        
        int gridX = rand.nextInt(gridSize) + 1;
        int gridY = rand.nextInt(gridSize) + 1;
        int clusterSize = (clusterLevel * 2) + 1;
        log.info("Inside generateSquareCluster function ..." + gridX + ".." + gridY);
        
        for (int i = (gridX - clusterLevel); i <= (gridX + clusterLevel); i++) {
            for (int j = (gridY - clusterLevel); j <= (gridY + clusterLevel); j++) {
                nodeId = "" + ((i) * gridSize) + (j);
                check = entitymanager.find(CellDetails.class, nodeId);
                if (check == null) {
                    
                    if (i <= gridSize && j <= gridSize && i >= 1 && j >= 1) {
                        entitymanager.getTransaction().begin();
                        int id = (((i - 1) * gridSize + j) / numberOfCellsPerNcServer) + 1;
                        String serverid = (serverIdPrefix + Integer.toString(1000 + id));
                        CellDetails newCell = new CellDetails();
                        newCell.setGridX(i);
                        newCell.setGridY(j);
                        newCell.setNodeId(nodeId);
                        newCell.setServerId(serverid);
                        if (collision == false) {
                            newCell.setPhysicalCellId(((int) newCell.getGridX() % clusterSize)
                                    * clusterSize + ((int) newCell.getGridY() % clusterSize));
                        } else {
                            newCell.setPhysicalCellId(rand.nextInt(10));
                        }
                        
                        entitymanager.merge(newCell);
                        entitymanager.flush();
                        entitymanager.getTransaction().commit();
                        
                        setNetconfServers(nodeId);
                        
                    }
                }
                
            }
            
        }
    }
    
    /**
     * The function deletes the cell from the database with the nodeId passed in
     * the arguments. It removes the cell from its neighbor's neighbor list and
     * the netconf server list.
     *
     * @param nodeId
     *            node Id of the cell to be deleted.
     * @return returns success or failure message
     */
    public static String deleteCellFunction(String nodeId) {
        //EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        //EntityManager entitymanager = emfactory.createEntityManager();
        String result = "failure node dosent exist";
        log.info("deleteCellFunction called with nodeId :" + nodeId);
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        
        try {
            CellDetails deleteCelldetail = rsDb.getCellDetail(nodeId);
            
            CellNeighbor deleteCellNeighbor = rsDb.getCellNeighbor(nodeId);
            
            if (deleteCelldetail != null) {
                if (deleteCellNeighbor != null) {
                    
                    //TypedQuery<CellNeighbor> query = entitymanager.createQuery("from CellNeighbor cn", CellNeighbor.class);
                    List<CellNeighbor> cellNeighborList = rsDb.getCellNeighborList();
                    //entitymanager.getTransaction().begin();
                    for (CellNeighbor cellNeighbors : cellNeighborList) {
                        Set<NeighborDetails> currentCellNeighbors = new HashSet<NeighborDetails>(
                                cellNeighbors.getNeighborList());
                        
                        NeihborId deleteNeighborDetail = new NeihborId(cellNeighbors.getNodeId(),
                                deleteCelldetail.getNodeId());
                        
                        if (currentCellNeighbors.contains(deleteNeighborDetail)) {
                            log.info("Deleted Cell is Neighbor of NodeId : "
                                    + cellNeighbors.getNodeId());
                            currentCellNeighbors.remove(deleteNeighborDetail);
                            cellNeighbors.setNeighborList(currentCellNeighbors);
                            rsDb.mergeCellNeighbor(cellNeighbors);
                        }
                    }
                    
                    deleteCellNeighbor.getNeighborList().clear();
                    rsDb.deleteCellNeighbor(deleteCellNeighbor);
                    //entitymanager.remove(deleteCellNeighbor);
                }
                
                /*if (deleteCelldetail.getServerId() != null) {
                    log.info("inside NetconfServers handling ....");
                    NetconfServers ns = rsDb.getNetconfServer(deleteCelldetail.getServerId());
                    ns.getCells().remove(deleteCelldetail);
                    //entitymanager.merge(ns);
                    rsDb.mergeNetconfServers(ns);
                }*/
                //entitymanager.remove(deleteCelldetail);
                rsDb.deleteCellDetails(deleteCelldetail);
                //entitymanager.flush();
                //entitymanager.getTransaction().commit();
                                
                result = "cell has been deleted from the database";
            } else {
                log.info("cell id does not exist");
                result = "failure nodeId dosent exist";
                return result;
            }
        } catch (Exception eu) {
            log.info("Exception deleteCellFunction :", eu);
            
            result = "exception in function";
        }
        return result;
    }
    
    /*
     * private int modifyCellFunction(String nodeId, long physicalCellId, String
     * source) { return modifyCellFunction(nodeId, physicalCellId, null,
     * source); }
     */
    
    void updatePciOperationsTable(String nodeId, String source, long physicalCellId, long oldPciId) {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        OperationLog operationLog = new OperationLog();
        
        entitymanager.getTransaction().begin();
        operationLog.setNodeId(nodeId);
        operationLog.setFieldName("PCID");
        operationLog.setOperation("Modify");
        operationLog.setSource(source);
        operationLog.setTime(System.currentTimeMillis());
        operationLog.setMessage("PCID value changed from " + oldPciId + " to " + physicalCellId);
        entitymanager.merge(operationLog);
        entitymanager.flush();
        entitymanager.getTransaction().commit();
    }
    
    void updateNbrsOperationsTable(String nodeId, String source, String addedNbrs, String deletedNbrs) {
        
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        entitymanager.getTransaction().begin();
        OperationLog operationLogNbrChng = new OperationLog();
        operationLogNbrChng.setNodeId(nodeId);
        operationLogNbrChng.setFieldName("Neighbors");
        operationLogNbrChng.setOperation("Modify");
        operationLogNbrChng.setSource(source);
        
        log.info(" Neighbors added " + addedNbrs + ".");
        log.info(" Neighbors removed " + deletedNbrs + ".");
        String message = "";
        if(!addedNbrs.equals("")){
        	message += " Neighbors added " + addedNbrs + "." ;
        }
        
        if(!deletedNbrs.equals("")){
        	message += " Neighbors removed " + deletedNbrs + "." ;
        }
        
        operationLogNbrChng.setMessage(message);
        operationLogNbrChng.setTime(System.currentTimeMillis());
        entitymanager.merge(operationLogNbrChng);
        entitymanager.flush();
        entitymanager.getTransaction().commit();
        
    }
    
    
    /**
     * It updates the cell with its new neighbor list and PCI and updates the
     * change in a database.
     * 
     * @param nodeId
     *            node Id of the cell
     * @param physicalCellId
     *            PCI number of the cell
     * @param newNbrs
     *            List of new neighbors for the cell
     * @param source
     *            The source from which cell modification has been triggered
     * @return returns success or failure message
     */
    public int modifyCellFunction(String nodeId, long physicalCellId,
            List<NeighborDetails> newNbrs, String source) {
        
        int result = 111;
                
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        log.info("modifyCellFunction nodeId:" + nodeId + ", physicalCellId:" + physicalCellId);
        CellDetails modifyCell = rsDb.getCellDetail(nodeId);
        
        if (modifyCell != null) {
            if (physicalCellId < 0 || physicalCellId > maxPciValueAllowed) {
                log.info("NewPhysicalCellId is empty or invalid");
                result = 400;
            } else {
                long oldPciId = modifyCell.getPhysicalCellId();
                if (physicalCellId != oldPciId) {
                    updatePciOperationsTable(nodeId, source, physicalCellId, oldPciId);
                    
                    modifyCell.setPhysicalCellId(physicalCellId);
                    rsDb.mergeCellDetails(modifyCell);
                }
                
                /*
                 * if (modifyCell.getServerId() != null) { NetconfServers ns =
                 * rsDb.getNetconfServer(modifyCell.getServerId());
                 * ns.getCells().add(modifyCell); }
                 */
                CellNeighbor neighbors = rsDb.getCellNeighbor(nodeId);
                List<NeighborDetails> oldNbrList = new ArrayList<NeighborDetails>(
                        neighbors.getNeighborList());
                List<NeighborDetails> oldNbrListWithHo = new ArrayList<NeighborDetails>();
                
                for (NeighborDetails cell : oldNbrList) {
                    if (!cell.isBlacklisted()) {
                        oldNbrListWithHo.add(cell);
                    }
                }
                
                boolean flag = false;
                
                List<NeighborDetails> addedNbrs = new ArrayList<NeighborDetails>();
                List<NeighborDetails> deletedNbrs = new ArrayList<NeighborDetails>();
                
                String nbrsDel = "";
                
                List<String> oldNbrsArr = new ArrayList<String>();
                for (NeighborDetails cell : oldNbrListWithHo) {
                    oldNbrsArr.add(cell.getNeigbor().getNeighborCell());
                }
                
                List<String> newNbrsArr = new ArrayList<String>();
                for (NeighborDetails cell : newNbrs) {
                    newNbrsArr.add(cell.getNeigbor().getNeighborCell());
                }
                
                for (NeighborDetails cell : oldNbrListWithHo) {
                    
                    if (!newNbrsArr.contains(cell.getNeigbor().getNeighborCell())) {
                        if (!flag) {
                            flag = true;
                        }
                        deletedNbrs.add(cell);
                        if (nbrsDel == "") {
                            nbrsDel = cell.getNeigbor().getNeighborCell();
                        } else {
                            nbrsDel += "," + cell.getNeigbor().getNeighborCell();
                        }
                        log.info("deleted cell: " + cell.getNeigbor().getNeighborCell()
                                + cell.isBlacklisted());
                        
                    }
                }
                
                String nbrsAdd = "";
                
                for (NeighborDetails cell : newNbrs) {
                    if(cell.isBlacklisted()){
                        addedNbrs.add(cell);
                    }
                    else{
                        if (!oldNbrsArr.contains(cell.getNeigbor().getNeighborCell())) {
                            addedNbrs.add(cell);
                            if (nbrsAdd == "") {
                                nbrsAdd = cell.getNeigbor().getNeighborCell();
                            } else {
                                nbrsAdd += "," + cell.getNeigbor().getNeighborCell();
                            }
                            log.info("added cell: " + cell.getNeigbor().getNeighborCell()
                                    + cell.isBlacklisted());
                        }
                    }
                    
                }
                List<NeighborDetails> newNeighborList = new ArrayList<NeighborDetails>(oldNbrList);
                for (NeighborDetails cell : deletedNbrs) {
                    NeighborDetails removeHo = new NeighborDetails(cell.getNeigbor(), true);
                    rsDb.mergeNeighborDetails(removeHo);
                    newNeighborList.add(removeHo);
                }
                
                for (NeighborDetails cell : addedNbrs) {
                    rsDb.mergeNeighborDetails(cell);
                    newNeighborList.add(cell);
                }
                
                if (!flag) {
                    if (newNbrs.size() != oldNbrList.size()) {
                        flag = true;
                    }
                }
                
                if (flag) {
                    updateNbrsOperationsTable(nodeId, source, nbrsAdd, nbrsDel);
                }
                
                /*if (gridType == GridType.HONEYCOMB || gridType == GridType.CIRCULAR) {
                    generateNeighbors(nodeId);
                    for (NeighborDetails cd : oldNbrList) {
                        generateNeighbors(cd.getNeigbor().getNeighborCell());
                    }
                } else if (gridType == GridType.RANDOM) {
                } */  
                    if (newNbrs != null) {
                        neighbors.getNeighborList().clear();
                        Set<NeighborDetails> updatedNbrList = new HashSet<NeighborDetails>(
                                newNeighborList);
                        neighbors.setNeighborList(updatedNbrList);
                        rsDb.mergeCellNeighbor(neighbors);
                    }
                    
                    generateFmData(source, modifyCell, newNeighborList);
                
                result = 200;
            }
            
            //checkCellsWithIssue();
            
        } else {
            result = 400;
        }
        
        return result;
    }
    
    public void checkCellsWithIssue() {
        
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            
            for (String id : cellsWithIssues) {
                // TODO SARAN
                CellDetails currentCell = entitymanager.find(CellDetails.class, id);
                FmMessage fmDataMessage = new FmMessage();
                List<EventFm> data = new ArrayList<EventFm>();
                
                if (!currentCell.isPciCollisionDetected()) {
                    if (!currentCell.isPciConfusionDetected()) {
                        
                        cellsWithIssues.remove(id);
                        CommonEventHeaderFm commonEventHeader = new CommonEventHeaderFm();
                        FaultFields faultFields = new FaultFields();
                        
                        commonEventHeader.setStartEpochMicrosec(System.currentTimeMillis() * 1000);
                        commonEventHeader.setSourceName(currentCell.getNodeId());
                        commonEventHeader.setReportingEntityName(currentCell.getServerId());
                        String uuid = globalFmCellIdUuidMap.get(currentCell.getNodeId());
                        commonEventHeader.setSourceUuid(uuid);
                        
                        faultFields.setAlarmCondition("RanPciCollisionConfusionOccurred");
                        faultFields.setEventSeverity("NORMAL");
                        faultFields.setEventSourceType("other");
                        faultFields.setSpecificProblem("Problem Solved");
                        
                        commonEventHeader.setLastEpochMicrosec(System.currentTimeMillis() * 1000);
                        
                        EventFm event = new EventFm();
                        event.setCommonEventHeader(commonEventHeader);
                        event.setFaultFields(faultFields);
                        
                        data.add(event);
                    }
                }
                
                fmDataMessage.setFmEventList(data);
                
                if(!data.isEmpty()){
                    sendFmData(currentCell.getServerId(), fmDataMessage); 
                }
                
            }
            
        } catch (Exception eu) {
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            log.info("Exception:", eu);
        } finally {
            entitymanager.close();
            emfactory.close();
        }
        
    }
    
    /**
     * Send configuration details to all the netconf server.
     */
    public void sendInitialConfigAll() {
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        try {
            dumpSessionDetails();
            List<NetconfServers> ncServers = rsDb.getNetconfServersList();
            for (NetconfServers netconfServers : ncServers) {
                String ipPortKey = serverIdIpPortMapping.get(netconfServers.getServerId());
                if (ipPortKey == null || ipPortKey.trim().equals("")) {
                    log.info("No client for " + netconfServers.getServerId());
                    for (String ipPortKeyStr : webSocketSessions.keySet()) {
                        if (!serverIdIpPortMapping.containsValue(ipPortKeyStr)) {
                            serverIdIpPortMapping.put(netconfServers.getServerId(), ipPortKeyStr);
                            ipPortKey = ipPortKeyStr;
                            break;
                        }
                    }
                }
                if (ipPortKey != null && !ipPortKey.trim().equals("")) {
                    Session clSess = webSocketSessions.get(ipPortKey);
                    if (clSess != null) {
                        sendInitialConfig(netconfServers.getServerId());
                        try {
                            String[] agentDetails = ipPortKey.split(":");
                            new RestClient().sendMountRequestToSdnr(netconfServers.getServerId(),
                                    sdnrServerIp, sdnrServerPort, agentDetails[0], agentDetails[1],
                                    sdnrServerUserid, sdnrServerPassword);
                        } catch (Exception ex1) {
                            log.info("Ignoring exception", ex1);
                        }
                        
                    } else {
                        log.info("No session for " + ipPortKey);
                    }
                }
            }
        } catch (Exception eu) {
            log.info("Exception:", eu);
        }         
    }
    
    /**
     * Sends initial configuration details of the cells for a new netconf server
     * that has been started.
     *
     * @param ipPortKey
     *            ip address details of the netconf server
     */
    public void sendInitialConfigForNewAgent(String ipPortKey, String serverId) {
        try {
            dumpSessionDetails();
            if (ipPortKey != null && !ipPortKey.trim().equals("")) {
                if (serverId != null && !serverId.trim().equals("")) {
                    Session clSess = webSocketSessions.get(ipPortKey);
                    if (clSess != null) {
                        String[] agentDetails = ipPortKey.split(":");
                        sendInitialConfig(serverId);
                        new RestClient().sendMountRequestToSdnr(serverId, sdnrServerIp,
                                sdnrServerPort, agentDetails[0], agentDetails[1], sdnrServerUserid,
                                sdnrServerPassword);
                    } else {
                        log.info("No session for " + ipPortKey);
                    }
                } else {
                    log.info("No serverid for " + ipPortKey);
                }
            } else {
                log.info("Invalid ipPortKey " + ipPortKey);
            }
        } catch (Exception eu) {
            log.info("Exception:", eu);
        }
    }
    
    /**
     * To send the initial configration to the netconf server.
     *
     * @param serverId
     *            ip address details of the netconf server
     */
    public void sendInitialConfig(String serverId) {
        
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        try {
            NetconfServers server = rsDb.getNetconfServer(serverId);
            log.info("sendInitialConfig: serverId:" + serverId + ", server:" + server);
            if (server == null) {
                return;
            }
            
            String ipPortKey = serverIdIpPortMapping.get(serverId);
            
            log.info("sendInitialConfig: ipPortKey:" + ipPortKey);
            
            List<CellDetails> cellList = new ArrayList<CellDetails>(server.getCells());
            
            List<Topology> config = new ArrayList<Topology>();
            
            for (int i = 0; i < server.getCells().size(); i++) {
                Topology cell = new Topology();
                CellDetails currentCell = rsDb.getCellDetail(cellList.get(i).getNodeId());
                CellNeighbor neighbor = rsDb.getCellNeighbor(cellList.get(i).getNodeId());
                
                cell.setCellId("" + currentCell.getNodeId());
                cell.setPciId(currentCell.getPhysicalCellId());
                cell.setPnfName(serverId);
                
                List<Neighbor> nbrList = new ArrayList<Neighbor>();
                Set<NeighborDetails> nbrsDet = neighbor.getNeighborList();
                for (NeighborDetails cellDetails : nbrsDet) {
                    Neighbor nbr = new Neighbor();
                    CellDetails nbrCell = rsDb.getCellDetail(cellDetails.getNeigbor().getNeighborCell());
                    nbr.setNodeId(nbrCell.getNodeId());
                    nbr.setPhysicalCellId(nbrCell.getPhysicalCellId());
                    nbr.setPnfName(nbrCell.getServerId());
                    nbr.setServerId(nbrCell.getServerId());
                    nbr.setPlmnId(nbrCell.getNetworkId());
                    nbr.setBlacklisted(cellDetails.isBlacklisted());
                    nbrList.add(nbr);
                }
                cell.setNeighborList(nbrList);
                config.add(i, cell);
            }
            
            SetConfigTopology topo = new SetConfigTopology();
            
            topo.setServerId(server.getServerId());
            String uuid = globalNcServerUuidMap.get(server.getServerId());
            if (uuid == null) {
                uuid = getUuid();
                globalNcServerUuidMap.put(server.getServerId(), uuid);
            }
            topo.setUuid(uuid);
            
            topo.setTopology(config);
            
            Gson gson = new Gson();
            String jsonStr = gson.toJson(topo);
            log.info("ConfigTopologyMessage: " + jsonStr);
            Session clSess = webSocketSessions.get(ipPortKey);
            RansimWebSocketServer.sendSetConfigTopologyMessage(jsonStr, clSess);
            
        } catch (Exception eu) {
            log.info("Exception:", eu);
        } 
        
    }
    
    /**
     * The function alters the database information based on the modifications
     * made in the SDNR.
     *
     * @param message
     *            message received from the SDNR
     * @param session
     *            sends the session details
     * @param ipPort
     *            ip address of the netconf server
     */
    public void handleModifyPciFromSdnr(String message, Session session, String ipPort) {
        log.info("handleModifyPciFromSDNR: message:" + message + " session:" + session + " ipPort:"
                + ipPort);
        RansimControllerDatabase rcDb = new RansimControllerDatabase();
        ModifyPci modifyPci = new Gson().fromJson(message, ModifyPci.class);
        log.info("handleModifyPciFromSDNR: modifyPci:" + modifyPci.getCellId() + "; pci: " + modifyPci.getPciId());
        String source = "Netconf";
        
        //CellNeighbor cn = rcDb.getCellNeighbor(modifyPci.getCellId());
        CellDetails cd = rcDb.getCellDetail(modifyPci.getCellId());
        long pci = cd.getPhysicalCellId();
        //List<NeighborDetails> neighborList = new ArrayList<NeighborDetails>(cn.getNeighborList());
        
        //GetNeighborList getNeighborList = generateNeighborList(modifyPci.getCellId());
        cd.setPhysicalCellId(modifyPci.getPciId());
        rcDb.mergeCellDetails(cd);
        updatePciOperationsTable(modifyPci.getCellId(), source, pci, modifyPci.getPciId());

        //checkCellsWithIssue();
        //log.info("neighbor list: " + neighborList);
        
        //modifyCellFunction(modifyPci.getCellId(), modifyPci.getPciId(), neighborList, source);
    }
    
    /**
     * The function alters the database information based on the modifications
     * made in the SDNR.
     *
     * @param message
     *            message received from the SDNR
     * @param session
     *            sends the session details
     * @param ipPort
     *            ip address of the netconf server
     */
    public void handleModifyNeighborFromSdnr(String message, Session session, String ipPort) {
        log.info("handleModifyAnrFromSDNR: message:" + message + " session:" + session + " ipPort:"
                + ipPort);
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        ModifyNeighbor modifyNeighbor = new Gson().fromJson(message, ModifyNeighbor.class);
        log.info("handleModifyAnrFromSDNR: modifyPci:" + modifyNeighbor.getCellId());
        //String neighborList = "";
        CellDetails currentCell = rsDb.getCellDetail(modifyNeighbor.getCellId());
        List<NeighborDetails> neighborList = new ArrayList<NeighborDetails>();
        List<String> cellList = new ArrayList<String>();
        cellList.add(modifyNeighbor.getCellId());
        String nbrsAdd = "";
        String nbrsDel = "";
        String source = "Netconf";
        
        for (int i = 0; i < modifyNeighbor.getNeighborList().size(); i++) {
            if (modifyNeighbor.getNeighborList().get(i).isBlacklisted()) {
                NeighborDetails nd = new NeighborDetails(new NeihborId(modifyNeighbor.getCellId(),
                        modifyNeighbor.getNeighborList().get(i).getNodeId()), true);
                rsDb.mergeNeighborDetails(nd);
                cellList.add(modifyNeighbor.getNeighborList().get(i).getNodeId());
                if(nbrsAdd.equals("")){
                    nbrsDel = modifyNeighbor.getNeighborList().get(i).getNodeId();
                }
                else
                {
                    nbrsDel += "," + modifyNeighbor.getNeighborList().get(i).getNodeId();
                }
                //neighborList.add(nd);
            } else {
                NeighborDetails nd = new NeighborDetails(new NeihborId(modifyNeighbor.getCellId(),
                        modifyNeighbor.getNeighborList().get(i).getNodeId()), false);
                rsDb.mergeNeighborDetails(nd);
                cellList.add(modifyNeighbor.getNeighborList().get(i).getNodeId());
                if(nbrsDel.equals("")){
                    nbrsAdd = modifyNeighbor.getNeighborList().get(i).getNodeId();
                }
                else
                {
                    nbrsAdd += "," + modifyNeighbor.getNeighborList().get(i).getNodeId();
                }
                //neighborList.add(nd);    
            }
            
        }
        
        for (String cl : cellList) {
            setCollisionConfusionFromFile(cl);
        }
        
        log.info("neighbor list: " + neighborList);
        
        updateNbrsOperationsTable(modifyNeighbor.getCellId(), source, nbrsAdd, nbrsDel);
        //checkCellsWithIssue();
        
        //modifyCellFunction(modifyNeighbor.getCellId(), currentCell.getPhysicalCellId(),neighborList, source);
    }
    
    /**
     * The function sends the modification made in the GUI to the netconf
     * server.
     *
     * @param cellId
     *            node Id of the cell which was modified
     * @param pciId
     *            PCI number of the cell which was modified
     */
    public void handleModifyPciFromGui(String cellId, long pciId) {
        log.info("handleModifyPciFromGUI: cellId:" + cellId + " pciId:" + pciId);
        RansimControllerDatabase rsDb = new RansimControllerDatabase();
        
        try {
            CellDetails currentCell = rsDb.getCellDetail(cellId);
            CellNeighbor neighborList = rsDb.getCellNeighbor(cellId);
            List<Neighbor> nbrList = new ArrayList<Neighbor>();
            Iterator<NeighborDetails> iter = neighborList.getNeighborList().iterator();
            while (iter.hasNext()) {
                NeighborDetails nbCell = iter.next();
                Neighbor nbr = new Neighbor();
                CellDetails nbrCell = rsDb.getCellDetail(nbCell.getNeigbor().getNeighborCell());
                
                nbr.setNodeId(nbrCell.getNodeId());
                nbr.setPhysicalCellId(nbrCell.getPhysicalCellId());
                nbr.setPnfName(nbrCell.getNodeName());
                nbr.setServerId(nbrCell.getServerId());
                nbr.setPlmnId(nbrCell.getNetworkId());
                nbrList.add(nbr);
            }
            // ModifyPci modifyPci = new ModifyPci(currentCell.getServerId(),
            // pciId, cellId,
            // nbrList);
            String pnfName = currentCell.getServerId();
            String ipPort = serverIdIpPortMapping.get(pnfName);
            log.info("handleModifyPciFromGui:ipPort >>>>>>> " + ipPort);
            
            if (ipPort != null && !ipPort.trim().equals("")) {
                
                String[] ipPortArr = ipPort.split(":");
                Topology oneCell = new Topology(pnfName, pciId, cellId, nbrList);
                UpdateCell updatedPci = new UpdateCell(currentCell.getServerId(), ipPortArr[0],
                        ipPortArr[1], oneCell);
                Gson gson = new Gson();
                String jsonStr = gson.toJson(updatedPci);
                if (ipPort != null && !ipPort.trim().equals("")) {
                    Session clSess = webSocketSessions.get(ipPort);
                    if (clSess != null) {
                        RansimWebSocketServer.sendUpdateCellMessage(jsonStr, clSess);
                        log.info("handleModifyPciFromGui, message: " + jsonStr);
                    } else {
                        log.info("No client session for " + ipPort);
                    }
                } else {
                    log.info("No client for " + currentCell.getServerId());
                }
            }
            
        } catch (Exception eu) {
            
            log.info("Exception:", eu);
        }
    }
    
    /**
     * The function converts the cell details data corresponding to the given
     * server Id into a json string format.
     *
     * @param serverId
     *            server Id of the netconf server for which the details is
     *            required
     * @return It returns the json String
     */
    /*public static String netconfServersList(String serverId) {
        
        try {
            RansimControllerDatabase rcDb = new RansimControllerDatabase();
            NetconfServers ns = rcDb.getNetconfServer(serverId);
            
            if (ns != null) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(ns);
                
                return jsonStr;
            } else {
                return ("Invalid Input");
            }
            
        } catch (Exception eu) {
            return ("Failure");
        }
    }*/
    
    /**
     * The function unmounts the connection with SDNR.
     *
     * @return returns null value
     */
    public String stopAllCells() {
        RansimControllerDatabase rcDb = new RansimControllerDatabase();
        try {
            List<NetconfServers> ncServers = rcDb.getNetconfServersList();
            for (NetconfServers netconfServers : ncServers) {
                try {
                    log.info("Unmount " + netconfServers.getServerId());
                    new RestClient().sendUnmountRequestToSdnr(netconfServers.getServerId(),
                            sdnrServerIp, sdnrServerPort, sdnrServerUserid, sdnrServerPassword);
                } catch (Exception e) {
                    log.info("Ignore Exception:", e);
                }
                serverIdIpNodeMapping.clear();
            }
            return "Netconf servers unmounted.";
        } catch (Exception eu) {
            
            log.info("Exception:", eu);
            return "Error";
        } 
        
    }
    
    /**
     * Used to dump session details.
     */
    synchronized public static void dumpSessionDetails() {

		try {

			log.info("serverIdIpPortMapping.size:"
					+ serverIdIpPortMapping.size() + "webSocketSessions.size"
					+ webSocketSessions.size());
			for (String key : serverIdIpPortMapping.keySet()) {
				String val = serverIdIpPortMapping.get(key);
				Session sess = webSocketSessions.get(val);
				log.info("ServerId:" + key + " IpPort:" + val + " Session:"
						+ sess);
			}
			
			//ArrayList<String> usi = (ArrayList<String>) unassignedServerIds.clone();
			for (String serverId : unassignedServerIds) {
				log.info("Unassigned ServerId:" + serverId);
			}
			for (String serverId : serverIdIpPortMapping.keySet()) {
				List<String> attachedNoeds = serverIdIpNodeMapping
						.get(serverId);
				if (attachedNoeds != null) {
					log.info("ServerId:" + serverId + " attachedNoeds.size:"
							+ attachedNoeds.size() + " nodes:"
							+ attachedNoeds.toArray());
				} else {
					log.info("ServerId:" + serverId + " attachedNoeds:" + null);
				}
			}
		} catch (Exception e) {
			log.info("Exception:", e);
		}
	}
    
}

class KeepWebsockAliveThread extends Thread {
    static Logger log = Logger.getLogger(KeepWebsockAliveThread.class.getName());
    RansimController rsCtrlr = null;
    
    KeepWebsockAliveThread(RansimController ctrlr) {
        rsCtrlr = ctrlr;
    }
    
    @Override
    public void run() {
        log.info("Inside KeepWebsockAliveThread run method");
        while (true) {
            for (String ipPort : rsCtrlr.webSocketSessions.keySet()) {
                try {
                    Session sess = rsCtrlr.webSocketSessions.get(ipPort);
                    RansimWebSocketServer.sendPingMessage(sess);
                    log.debug("Sent ping message to Client ipPort:" + ipPort);
                } catch (Exception ex1) {
                }
            }
            try {
                Thread.sleep(10000);
            } catch (Exception ex) {
            }
        }
    }
}
