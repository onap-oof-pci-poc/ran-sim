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

package org.onap.ransim.rest.api.controller;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.onap.ransim.rest.api.models.CellData;
import org.onap.ransim.rest.api.models.CellDetails;
import org.onap.ransim.rest.api.models.CellNeighbor;
import org.onap.ransim.rest.api.models.NetconfServers;
import org.onap.ransim.rest.api.models.TopologyDump;
import org.onap.ransim.rest.client.RestClient;
import org.onap.ransim.websocket.model.ModifyPci;
import org.onap.ransim.websocket.model.Neighbor;
import org.onap.ransim.websocket.model.SetConfigTopology;
import org.onap.ransim.websocket.model.Topology;
import org.onap.ransim.websocket.model.UpdateCell;
import org.onap.ransim.websocket.server.RansimWebSocketServer;

public class RansimController {

    static Logger log = Logger.getLogger(RansimControllerServices.class.getName());

    private static RansimController rsController = null;

    Properties netconfConstants = new Properties();
    int gridSize = 10;
    GridType gridType = GridType.RANDOM;
    boolean collision = false;
    String serverIdPrefix = "";
    int numberOfCellsPerNcServer = 15;
    int numberOfMachines = 1;
    int numberOfProcessPerMc = 5;
    boolean strictValidateRansimAgentsAvailability = false;
    public Map<String, Session> webSocketSessions = new HashMap<String, Session>();
    Map<String, String> serverIdIpPortMapping = new HashMap<String, String>();
    List<String> unassignedServerIds = new ArrayList<String>();
    Map<String, List<String>> serverIdIpNodeMapping = new HashMap<String, List<String>>();
    int nextServerIdNumber = 1001;
    String sdnrServerIp = "";
    int sdnrServerPort = 0;
    String sdnrServerUserid = "";
    String sdnrServerPassword = "";
    String dumpFileName = "";
    long maxPciValueAllowed = 503;

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

    /**
     * Add web socket sessions.
     *
     * @param ipPort
     *            ip address for the session
     * @param wsSession
     *            session details
     */
    public synchronized void addWebSocketSessions(String ipPort, Session wsSession) { // javadoc
        loadProperties();
        if (webSocketSessions.containsKey(ipPort)) {
            log.info("addWebSocketSessions: Client session " + wsSession.getId() + " for " + ipPort
                    + " already exist. Removing old session.");
            webSocketSessions.remove(ipPort);
        }

        log.info("addWebSocketSessions: Adding Client session " + wsSession.getId() + " for " + ipPort);
        webSocketSessions.put(ipPort, wsSession);

        if (!serverIdIpPortMapping.containsValue(ipPort)) {
            // String serverId = serverIdPrefix + nextServerIdNumber;
            // nextServerIdNumber++;
            if (unassignedServerIds.size() > 0) {
                String serverId = unassignedServerIds.remove(0);
                log.info("addWebSocketSessions: Adding serverId " + serverId + " for " + ipPort);
                serverIdIpPortMapping.put(serverId, ipPort);

                mapServerIdToNodes(serverId);
            } else {
                log.info("addWebSocketSessions: No serverIds pending to assign for " + ipPort);
            }

        } else {
            for (String key : serverIdIpPortMapping.keySet()) {
                if (serverIdIpPortMapping.get(key).equals(ipPort)) {
                    log.info("addWebSocketSessions: ServerId " + key + " for " + ipPort + " is exist already");
                    break;
                }
            }

        }
    }

    private void mapServerIdToNodes(String serverId) {
        dumpSessionDetails();
        if (serverIdIpNodeMapping.containsKey(serverId)) {
            // already mapped.RansimController Do nothing.
        } else {
            List<String> nodeIds = new ArrayList<String>();
            EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
            EntityManager entitymanager = emfactory.createEntityManager();
            try {
                TypedQuery<CellDetails> query = entitymanager
                        .createQuery("SELECT n FROM CellDetails WHERE n.serverId is null", CellDetails.class);
                List<CellDetails> nodes = query.getResultList();
                entitymanager.getTransaction().begin();
                for (CellDetails cell : nodes) {
                    cell.setServerId(serverId);
                    nodeIds.add(cell.getNodeId());
                    entitymanager.merge(cell);
                    entitymanager.flush();
                }
                entitymanager.getTransaction().commit();
                serverIdIpNodeMapping.put(serverId, nodeIds);
            } catch (Exception e1) {
                log.info("Exception mapServerIdToNodes :", e1);
                if (entitymanager.getTransaction().isActive()) {
                    entitymanager.getTransaction().rollback();
                }
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
        if (webSocketSessions.containsKey(ipPort)) {
            for (String serverId : serverIdIpPortMapping.keySet()) {
                String ipPortVal = serverIdIpPortMapping.get(serverId);
                if (ipPortVal.equals(ipPort)) {
                    unassignedServerIds.add(serverId);
                    break;
                }
            }
            Session wsSession = webSocketSessions.remove(ipPort);
            log.info(
                    "removeWebSocketSessions: Client session " + wsSession.getId() + " for " + ipPort + " is removed.");
        } else {
            log.info("addWebSocketSessions: Client session for " + ipPort + " not exist");
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

        log.info("hasEnoughRansimAgentsRunning: numberOfCellsPerNCServer " + numberOfCellsPerNcServer
                + " , webSocketSessions.size:" + webSocketSessions.size() + " , cellsToBeSimulated:"
                + cellsToBeSimulated);
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
            numberOfCellsPerNcServer = Integer.parseInt(netconfConstants.getProperty("numberOfCellsPerNCServer"));
            numberOfMachines = Integer.parseInt(netconfConstants.getProperty("numberOfMachines"));
            numberOfProcessPerMc = Integer.parseInt(netconfConstants.getProperty("numberOfProcessPerMc"));
            strictValidateRansimAgentsAvailability = Boolean
                    .parseBoolean(netconfConstants.getProperty("strictValidateRansimAgentsAvailability"));
            sdnrServerIp = netconfConstants.getProperty("sdnrServerIp");
            sdnrServerPort = Integer.parseInt(netconfConstants.getProperty("sdnrServerPort"));
            sdnrServerUserid = netconfConstants.getProperty("sdnrServerUserid");
            sdnrServerPassword = netconfConstants.getProperty("sdnrServerPassword");
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
     * The function adds the cell(with nodeId passed as an argument) to its netconf
     * server list if the netconf server already exists. Else it will create a new
     * netconf server in the NetconfServers Table and the cell into its list.
     *
     * @param nodeId
     *            node Id of the cell
     */
    private void setNetconfServers(String nodeId) {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();

        entitymanager.getTransaction().begin();

        CellDetails currentCell = entitymanager.find(CellDetails.class, nodeId);

        Set<CellDetails> newList = new HashSet<CellDetails>();
        try {
            // long startTimeSetNetconfServers = System.currentTimeMillis();
            if (currentCell != null) {
                NetconfServers server = entitymanager.find(NetconfServers.class, currentCell.getServerId());

                if (server == null) {

                    server = new NetconfServers();
                    server.setServerId(currentCell.getServerId());
                } else {
                    newList.addAll(server.getCells());
                }

                newList.add(currentCell);
                server.setCells(newList);
                log.info("setNetconfServers: nodeId: " + nodeId + ", X:" + currentCell.getGridX() + ", Y:"
                        + currentCell.getGridY() + ", ip: " + server.getIp() + ", portNum: " + server.getNetconfPort()
                        + ", serverId:" + currentCell.getServerId());

                entitymanager.merge(server);

            }
            entitymanager.flush();
            entitymanager.getTransaction().commit();
            // long endTimeSetNetconfServers = System.currentTimeMillis();
            // log.info("Time taken for reading from file : " + (endTimeSetNetconfServers -
            // startTimeSetNetconfServers));

        } catch (Exception eu) {
            log.info("/setNetconfServers Function Error", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
        } finally {
            entitymanager.close();
            emfactory.close();
        }

    }

    /**
     * The function assigns the neighbor list for the cell(with node id passed as an
     * argument) based on the xy co-ordinate system.
     *
     * @param nodeId
     *            node Id of the cell for which the neighbor list is to be generated
     */
    private void generateNeighbors(String nodeId) {

        log.info("Inside generateNeighbor Function");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        int posX;
        int posY;
        String cellNodeId;
        String neighborNodeId;
        cellNodeId = nodeId;

        entitymanager.getTransaction().begin();

        // neighbor list with the corresponding node id
        CellNeighbor neighborList = entitymanager.find(CellNeighbor.class, cellNodeId);
        // cell with the corresponding nodeId
        CellDetails currentCell = entitymanager.find(CellDetails.class, cellNodeId);

        Set<CellDetails> newList = new HashSet<CellDetails>();

        CellDetails neighbor;

        int neighborCollisionCount = 0;
        List<Long> neighborPci = new ArrayList<Long>();

        if (currentCell != null) {
            if (neighborList == null) {
                neighborList = new CellNeighbor();
                neighborList.setNodeId(cellNodeId);

            } else {
                newList.addAll(neighborList.getNeighborList());
            }
            posX = (int) currentCell.getGridX();
            posY = (int) currentCell.getGridY();
            for (int x = (posX - 1); x <= (posX + 1); x++) {
                for (int y = (posY - 1); y <= (posY + 1); y++) {

                    neighborNodeId = "" + x * gridSize + y;
                    if (!currentCell.getNodeId().equals(neighborNodeId)) {
                        neighbor = entitymanager.find(CellDetails.class, neighborNodeId);

                        if (gridType == GridType.HONEYCOMB) {
                            log.info("Inside generateNeighbor Function : Hexogonal grid");
                            // Neighbor allotment for a hexagonal grid

                            if ((posY) % 2 == 0) {
                                if (!((x == (posX - 1) && y == (posY - 1)) || (x == (posX - 1) && y == (posY + 1)))) {
                                    if (neighbor != null && (neighbor.getNodeId() != cellNodeId)) {
                                        newList.add(neighbor);
                                        neighborPci.add(neighbor.getPhysicalCellId());
                                        if (currentCell.getPhysicalCellId() == neighbor.getPhysicalCellId()) {
                                            currentCell.setPciCollisionDetected(true);
                                        } else {
                                            neighborCollisionCount++;
                                        }

                                    }

                                }
                            } else {
                                if (!((x == (posX + 1) && y == (posY - 1)) || (x == (posX + 1) && y == (posY + 1)))) {
                                    if (neighbor != null && (neighbor.getNodeId() != cellNodeId)) {
                                        newList.add(neighbor);
                                        neighborPci.add(neighbor.getPhysicalCellId());
                                        if (currentCell.getPhysicalCellId() == neighbor.getPhysicalCellId()) {
                                            currentCell.setPciCollisionDetected(true);
                                        } else {
                                            neighborCollisionCount++;
                                        }

                                    }

                                }
                            }

                        } else if (gridType == GridType.CIRCULAR) {
                            log.info("Inside generateNeighbor Function square grid");
                            // Neighbor allotment for a square grid

                            if (neighbor != null && (neighbor.getNodeId() != cellNodeId)) {
                                newList.add(neighbor);
                                neighborPci.add(neighbor.getPhysicalCellId());
                                if (currentCell.getPhysicalCellId() == neighbor.getPhysicalCellId()) {
                                    currentCell.setPciCollisionDetected(true);
                                } else {
                                    neighborCollisionCount++;
                                }

                            }

                        }

                    }
                }
            }

            if (neighborCollisionCount == newList.size()) {
                currentCell.setPciCollisionDetected(false);
            }

            int confusionCount = 0;
            for (int i = 0; i < newList.size(); i++) {
                for (int j = i + 1; j < newList.size(); j++) {
                    if ((i >= neighborPci.size()) || (j >= neighborPci.size())) {
                        continue;
                    }
                    if (neighborPci.get(i).equals(neighborPci.get(j))) {
                        confusionCount++;
                    }
                }
            }

            if (confusionCount == 0) {
                currentCell.setPciConfusionDetected(false);
            } else {
                currentCell.setPciConfusionDetected(true);
            }

            neighborList.setNeighborList(newList);

            entitymanager.merge(neighborList);

            entitymanager.flush();
            entitymanager.getTransaction().commit();
        }
    }

    /**
     * The function Updates or assigns the neighbor list for the entire database.
     *
     */
    private void updateNeighborList() {
        log.info("SVC3AS Inside getNeighborList...");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {

            String cellNodeId;
            entitymanager.getTransaction().begin();

            for (int i = 1; i <= gridSize; i++) {
                for (int j = 1; j <= gridSize; j++) {
                    cellNodeId = "" + i * gridSize + j;

                    generateNeighbors(cellNodeId);
                }

            }

            entitymanager.flush();
            entitymanager.getTransaction().commit();

        } catch (Exception eu) {
            log.info("/updateNeighborList", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }

        } finally {
            entitymanager.close();
            emfactory.close();
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
    public void generateCluster(int gdSize, int gdType, int numberOfClusters, int clusterLevel) throws Exception {
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

    private double degToRadians(double angle) {
        double radians = 57.2957795;
        return (angle / radians);
    }

    private double metersDeglon(double angle) {

        double d2r = degToRadians(angle);
        return ((111415.13 * Math.cos(d2r)) - (94.55 * Math.cos(3.0 * d2r)) + (0.12 * Math.cos(5.0 * d2r)));

    }

    private double metersDeglat(double angle) {

        double d2r = degToRadians(angle);
        return (111132.09 - (566.05 * Math.cos(2.0 * d2r)) + (1.20 * Math.cos(4.0 * d2r))
                - (0.002 * Math.cos(6.0 * d2r)));

    }

    private void generateClusterFromFile() throws IOException {

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
            log.info("Time taken for string builder : " + (endTimeStringBuilder - startTimeStringBuilder));

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
            for (int i = 0; i < dumpTopo.getCellList().size(); i++) {
                log.info("Creating Cell:" + dumpTopo.getCellList().get(i).getCell().getNodeId());

                long startTimedatabase = System.currentTimeMillis();
                log.info(startTimeTopologyDumpToDatabase);
                log.info(startTimedatabase);
                cellsDb = new CellDetails();
                entitymanager.getTransaction().begin();
                cellsDb.setNodeId(dumpTopo.getCellList().get(i).getCell().getNodeId());
                cellsDb.setPhysicalCellId(dumpTopo.getCellList().get(i).getCell().getPhysicalCellId());
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
                log.info("Attaching Cell:" + dumpTopo.getCellList().get(i).getCell().getNodeId() + " to "
                        + cellsDb.getServerId());
                if (attachedNoeds == null) {
                    attachedNoeds = new ArrayList<String>();
                }
                attachedNoeds.add(cellsDb.getNodeId());
                serverIdIpNodeMapping.put(cellsDb.getServerId(), attachedNoeds);
                if (attachedNoeds.size() > numberOfCellsPerNcServer) {
                    log.warn("Attaching Cell:" + dumpTopo.getCellList().get(i).getCell().getNodeId() + " to "
                            + cellsDb.getServerId() + ", But it is exceeding numberOfCellsPerNcServer "
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
            log.info("Time taken for reading from file : " + (endTimeReadingFromFile - startTimeReadingFromFile));

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

                    Set<CellDetails> newCell = new HashSet<CellDetails>();

                    if (currentCell != null) {
                        long startTimeNeighborList = System.currentTimeMillis();
                        log.info(startTimeNeighborList);
                        if (neighborList == null) {
                            neighborList = new CellNeighbor();
                            neighborList.setNodeId(cellNodeId);
                        }
                        List<String> neighboursFromFile = dumpTopo.getCellList().get(i).getNeighbor();
                        log.info("Creating Neighbor for Cell :" + cellNodeId);
                        for (String a : neighboursFromFile) {
                            a = a.trim();
                            String neighborNodeId = (a);
                            CellDetails neighborCell = entitymanager.find(CellDetails.class, neighborNodeId);
                            newCell.add(neighborCell);
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
                 * setCellColor(dumpTopo.getCellList().get(i).getCell().getNodeId()); }
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
                    CellDetails icell = entitymanager.find(CellDetails.class, icellData.getCell().getNodeId());

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
                            if (icellData.getCell().getLatitude().equals(jcellData.getCell().getLatitude())) {
                                if (icellData.getCell().getLongitude().equals(jcellData.getCell().getLongitude())) {

                                    if (icount == 0) {
                                        icount++;
                                        jcount = icount + 1;
                                    }

                                    CellDetails jcell = entitymanager.find(CellDetails.class,
                                            dumpTopo.getCellList().get(j).getCell().getNodeId());
                                    // jcount = jcell.getSectorNumber();

                                    jcell.setSectorNumber(jcount);
                                    log.info("Setting sectorNumber for Cell(j) :" + jcell.getNodeId() + " icell: "
                                            + icell.getNodeId() + " Sector number: " + jcount);
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
                log.info("Time taken for setting sector number: " + (endTimeSectorNumber - startTimeSectorNumber));

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

    static void setCollisionConfusionFromFile(String cellNodeId) {

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();

        try {
            entitymanager.getTransaction().begin();
            boolean collisionDetected = false;
            boolean confusionDetected = false;
            List<Long> nbrPcis = new ArrayList<Long>();
            // neighbor list with the corresponding node id
            CellNeighbor neighborList = entitymanager.find(CellNeighbor.class, cellNodeId);
            // cell with the corresponding nodeId
            CellDetails currentCell = entitymanager.find(CellDetails.class, cellNodeId);
            List<CellDetails> firstLevelNbrs = new ArrayList<CellDetails>(neighborList.getNeighborList());
            log.info("Setting confusion/collision for Cell :" + cellNodeId);

            for (CellDetails firstLevelNbrCellDetails : firstLevelNbrs) {

                if (nbrPcis.contains(new Long(firstLevelNbrCellDetails.getPhysicalCellId()))) {
                    confusionDetected = true;

                } else {
                    nbrPcis.add(new Long(firstLevelNbrCellDetails.getPhysicalCellId()));
                }

                if (currentCell.getPhysicalCellId() == firstLevelNbrCellDetails.getPhysicalCellId()) {
                    collisionDetected = true;
                }
            }

            currentCell.setPciCollisionDetected(collisionDetected);
            currentCell.setPciConfusionDetected(confusionDetected);

            log.info("collision :" + currentCell.isPciCollisionDetected());

            if (!currentCell.isPciCollisionDetected() && !currentCell.isPciConfusionDetected()) {
                currentCell.setColor("#BFBFBF"); // GREY - No Issues
            } else if (currentCell.isPciCollisionDetected() && currentCell.isPciConfusionDetected()) {
                currentCell.setColor("#C30000"); // BROWN - Cell has both collision & confusion
            } else if (currentCell.isPciCollisionDetected()) {
                currentCell.setColor("#FF0000"); // RED - Cell has collision
            } else if (currentCell.isPciConfusionDetected()) {
                currentCell.setColor("#E88B00"); // ORANGE - Cell has confusion
            } else {
                currentCell.setColor("#BFBFBF"); // GREY - No Issues
            }

            entitymanager.merge(currentCell);
            entitymanager.flush();

            entitymanager.getTransaction().commit();

        } catch (Exception e2) {
            log.info("setCollisionConfusionFromFile :", e2);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
        }

    }

    static void setCellColor(String cellNodeId) {

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();

        try {
            entitymanager.getTransaction().begin();

            CellDetails currentCell = entitymanager.find(CellDetails.class, cellNodeId);
            if (currentCell != null) {

                if (currentCell.isPciCollisionDetected() && currentCell.isPciConfusionDetected()) {
                    currentCell.setColor("#C30000"); // BROWN - Cell has both collision & confusion
                } else if (currentCell.isPciCollisionDetected()) {
                    currentCell.setColor("#FF0000"); // RED - Cell has collision
                } else if (currentCell.isPciConfusionDetected()) {
                    currentCell.setColor("#E88B00"); // ORANGE - Cell has confusion
                } else {
                    currentCell.setColor("#BFBFBF"); // GREY - No Issues
                }

                log.info("setCellColor :" + currentCell.getColor() + " for cell id:" + currentCell.getNodeId());
                entitymanager.merge(currentCell);
                entitymanager.flush();

                entitymanager.getTransaction().commit();
            }
        } catch (Exception e2) {
            log.info("Exception generateClusterFromFile :", e2);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
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
                            newCell.setPhysicalCellId(((int) newCell.getGridX() % clusterSize) * clusterSize
                                    + ((int) newCell.getGridY() % clusterSize));
                        } else {
                            newCell.setPhysicalCellId(rand.nextInt(10));
                        }
                        log.info("generateHexagonCluster Adding Cell at X:" + gridX + ",Y:" + gridY + "nodeId:" + nodeId
                                + " serverid:" + serverid);

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
                            newCell.setPhysicalCellId(((int) newCell.getGridX() % clusterSize) * clusterSize
                                    + ((int) newCell.getGridY() % clusterSize));
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
     * The function deletes the cell from the database with the nodeId passed in the
     * arguments. It removes the cell from its neighbor's neighbor list and the
     * netconf server list.
     *
     * @param nodeId
     *            node Id of the cell to be deleted.
     * @return returns success or failure message
     */
    public String deleteCellFunction(String nodeId) {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        String result = "failure node dosent exist";

        try {

            CellDetails deleteCelldetail = entitymanager.find(CellDetails.class, nodeId);
            CellNeighbor deleteCellNeighbor = entitymanager.find(CellNeighbor.class, nodeId);

            log.info("CellDetails and CellNeighbor obtained for " + nodeId);
            List<CellDetails> nbrList = new ArrayList<CellDetails>(deleteCellNeighbor.getNeighborList());
            if (deleteCelldetail != null && deleteCellNeighbor != null) {
                List<CellDetails> currentCellNeighbors = new ArrayList<CellDetails>(
                        deleteCellNeighbor.getNeighborList());
                entitymanager.getTransaction().begin();

                for (int i = 0; i < currentCellNeighbors.size(); i++) {
                    CellNeighbor nextCellNbr = entitymanager.find(CellNeighbor.class,
                            currentCellNeighbors.get(i).getNodeId());

                    if (nextCellNbr != null) {
                        log.info("nextCellNbr " + currentCellNeighbors.get(i).getNodeId());
                        if (nextCellNbr.getNeighborList().contains(deleteCelldetail)) {
                            nextCellNbr.getNeighborList().remove(deleteCelldetail);
                            entitymanager.merge(nextCellNbr);

                        }
                    }

                }

                deleteCellNeighbor.getNeighborList().clear();

                entitymanager.remove(deleteCellNeighbor);

                if (deleteCelldetail.getServerId() != null) {
                    log.info("inside NetconfServers handling ....");
                    NetconfServers ns = entitymanager.find(NetconfServers.class, deleteCelldetail.getServerId());
                    ns.getCells().remove(deleteCelldetail);
                    entitymanager.merge(ns);

                }
                entitymanager.remove(deleteCelldetail);
                entitymanager.flush();
                entitymanager.getTransaction().commit();

                for (int i = 0; i < nbrList.size(); i++) {
                    if (gridType == GridType.HONEYCOMB || gridType == GridType.CIRCULAR) {
                        generateNeighbors(nbrList.get(i).getNodeId());
                    } else if (gridType == GridType.RANDOM) {
                        setCollisionConfusionFromFile(nbrList.get(i).getNodeId());
                        setCellColor(nbrList.get(i).getNodeId());
                    }
                }
                result = "cell has been deleted from the database";

            } else {
                log.info("cell id does not exist");
                result = "failure nodeId dosent exist";
                return result;
            }
        } catch (Exception eu) {
            log.info("Exception deleteCellFunction :", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }

            result = "exception in function";
        }

        return result;

    }

    private int modifyCellFunction(String nodeId, long physicalCellId) {
        return modifyCellFunction(nodeId, physicalCellId, null);
    }

    /**
     * The function modifies the cell with the pci number passed as an argument. It
     * removes the cell from its neighbor's neighbor list and the netconf server
     * list. It updates the cell's neighbor list with the new list.
     *
     * @param nodeId
     *            node Id of the cell to be modified
     * @param physicalCellId
     *            The new PCI number for the cell
     * @param newNbrs
     *            new Neighbor list of the cell
     * @return It returns success or failure message
     */
    public int modifyCellFunction(String nodeId, long physicalCellId, String newNbrs) {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();

        int result;
        log.info("modifyCellFunction nodeId:" + nodeId + ", physicalCellId:" + physicalCellId);
        CellDetails modifyCell = entitymanager.find(CellDetails.class, nodeId);
        if (modifyCell != null) {
            if (physicalCellId < 0 || physicalCellId > maxPciValueAllowed) {
                log.info("NewPhysicalCellId is empty or invalid");
                result = 400;
            } else {

                modifyCell.setPhysicalCellId(physicalCellId);
                entitymanager.merge(modifyCell);
                entitymanager.getTransaction().begin();
                if (modifyCell.getServerId() != null) {
                    NetconfServers ns = entitymanager.find(NetconfServers.class, modifyCell.getServerId());
                    ns.getCells().add(modifyCell);

                }

                log.info("updated in database....");
                entitymanager.flush();
                entitymanager.getTransaction().commit();
                CellNeighbor neighbors = entitymanager.find(CellNeighbor.class, nodeId);
                Set<CellDetails> affectedNbrList = new HashSet<CellDetails>(neighbors.getNeighborList());

                if (gridType == GridType.HONEYCOMB || gridType == GridType.CIRCULAR) {
                    generateNeighbors(nodeId);
                    for (CellDetails cd : affectedNbrList) {
                        generateNeighbors(cd.getNodeId());
                    }

                } else if (gridType == GridType.RANDOM) {

                    entitymanager.getTransaction().begin();
                    if (newNbrs != null) {
                        String[] newNbrsArr = newNbrs.split(",");
                        List<String> nbrList = new ArrayList<>();

                        for (String nbr : newNbrsArr) {
                            nbrList.add(nbr.trim());
                        }

                        neighbors.getNeighborList().clear();
                        Set<CellDetails> newNbrList = new HashSet<CellDetails>();
                        for (int i = 0; i < nbrList.size(); i++) {
                            CellDetails nbrCell = entitymanager.find(CellDetails.class, nbrList.get(i));
                            if (nbrCell != null) {
                                newNbrList.add(nbrCell);
                                affectedNbrList.add(nbrCell);
                            }
                        }
                        neighbors.setNeighborList(newNbrList);
                        entitymanager.merge(neighbors);
                        entitymanager.flush();
                        entitymanager.getTransaction().commit();
                    }

                    setCollisionConfusionFromFile(nodeId);
                    // setCellColor(nodeId);

                    for (CellDetails cd : affectedNbrList) {
                        setCollisionConfusionFromFile(cd.getNodeId());
                        // setCellColor(cd.getNodeId());
                    }
                }

                result = 200;
            }

        } else {
            result = 400;
        }

        return result;
    }

    /**
     * Send configuration details to all the netconf server.
     */
    public void sendInitialConfigAll() {

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            dumpSessionDetails();
            TypedQuery<NetconfServers> query = entitymanager.createQuery("from NetconfServers e", NetconfServers.class);
            List<NetconfServers> ncServers = query.getResultList();
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
                            new RestClient().sendMountRequestToSdnr(netconfServers.getServerId(), sdnrServerIp,
                                    sdnrServerPort, agentDetails[0], agentDetails[1], sdnrServerUserid,
                                    sdnrServerPassword);
                        } catch (Exception ex1) {
                            log.info("Ignoring exception", ex1);
                        }

                    } else {
                        log.info("No session for " + ipPortKey);
                    }
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
     * Sends initial configuration details of the cells for a new netconf server
     * that has been started.
     *
     * @param ipPortKey
     *            ip address details of the netconf server
     */
    public void sendInitialConfigForNewAgent(String ipPortKey) {
        try {
            dumpSessionDetails();
            String serverId = "";
            if (ipPortKey != null && !ipPortKey.trim().equals("")) {
                for (String key : serverIdIpPortMapping.keySet()) {
                    if (serverIdIpPortMapping.get(key).equals(ipPortKey)) {
                        serverId = key;
                        break;
                    }
                }
                if (serverId != null && !serverId.trim().equals("")) {
                    Session clSess = webSocketSessions.get(ipPortKey);
                    if (clSess != null) {
                        String[] agentDetails = ipPortKey.split(":");
                        sendInitialConfig(serverId);
                        new RestClient().sendMountRequestToSdnr(serverId, sdnrServerIp, sdnrServerPort, agentDetails[0],
                                agentDetails[1], sdnrServerUserid, sdnrServerPassword);
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

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            entitymanager.getTransaction().begin();
            NetconfServers server = entitymanager.find(NetconfServers.class, serverId);
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
                CellDetails currentCell = entitymanager.find(CellDetails.class, cellList.get(i).getNodeId());
                CellNeighbor neighbor = entitymanager.find(CellNeighbor.class, cellList.get(i).getNodeId());

                cell.setCellId("" + currentCell.getNodeId());
                cell.setPciId(currentCell.getPhysicalCellId());
                cell.setPnfName(serverId);

                List<Neighbor> nbrList = new ArrayList<Neighbor>();
                Set<CellDetails> nbList = neighbor.getNeighborList();
                for (CellDetails cellDetails : nbList) {
                    Neighbor nbr = new Neighbor();
                    nbr.setNodeId(cellDetails.getNodeId());
                    nbr.setPhysicalCellId(cellDetails.getPhysicalCellId());
                    nbr.setPnfName(cellDetails.getServerId());
                    nbr.setServerId(cellDetails.getServerId());
                    nbr.setPlmnId(cellDetails.getNetworkId());
                    nbrList.add(nbr);
                }
                cell.setNeighborList(nbrList);
                config.add(i, cell);

            }

            SetConfigTopology topo = new SetConfigTopology();

            topo.setServerId(server.getServerId());

            topo.setTopology(config);

            Gson gson = new Gson();
            String jsonStr = gson.toJson(topo);
            Session clSess = webSocketSessions.get(ipPortKey);
            RansimWebSocketServer.sendSetConfigTopologyMessage(jsonStr, clSess);

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
     * The function alters the database information based on the modifications made
     * in the SDNR.
     *
     * @param message
     *            message received from the SDNR
     * @param session
     *            sends the session details
     * @param ipPort
     *            ip address of the netconf server
     */
    public void handleModifyPciFromSdnr(String message, Session session, String ipPort) {
        log.info("handleModifyPciFromSDNR: message:" + message + " session:" + session + " ipPort:" + ipPort);
        ModifyPci modifyPci = new Gson().fromJson(message, ModifyPci.class);
        log.info("handleModifyPciFromSDNR: modifyPci:" + modifyPci);
        modifyCellFunction(modifyPci.getCellId(), modifyPci.getPciId());
    }

    /**
     * The function sends the modification made in the GUI to the netconf server.
     *
     * @param cellId
     *            node Id of the cell which was modified
     * @param pciId
     *            PCI number of the cell which was modified
     */
    public void handleModifyPciFromGui(String cellId, long pciId) {
        log.info("handleModifyPciFromGUI: cellId:" + cellId + " pciId:" + pciId);

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            CellDetails currentCell = entitymanager.find(CellDetails.class, cellId);
            CellNeighbor neighborList = entitymanager.find(CellNeighbor.class, cellId);
            List<Neighbor> nbrList = new ArrayList<Neighbor>();
            Iterator<CellDetails> iter = neighborList.getNeighborList().iterator();
            while (iter.hasNext()) {
                CellDetails nbCell = iter.next();
                Neighbor nbr = new Neighbor();
                nbr.setNodeId(nbCell.getNodeId());
                nbr.setPhysicalCellId(nbCell.getPhysicalCellId());
                nbr.setPnfName(nbCell.getNodeName());
                nbr.setServerId(nbCell.getServerId());
                nbr.setPlmnId(nbCell.getNetworkId());
                nbrList.add(nbr);
            }
            // ModifyPci modifyPci = new ModifyPci(currentCell.getServerId(), pciId, cellId,
            // nbrList);
            String pnfName = currentCell.getServerId();
            String ipPort = serverIdIpPortMapping.get(pnfName);
            log.info("handleModifyPciFromGui:ipPort >>>>>>> " + ipPort);

            if (ipPort != null && !ipPort.trim().equals("")) {

                String[] ipPortArr = ipPort.split(":");
                Topology oneCell = new Topology(pnfName, pciId, cellId, nbrList);
                UpdateCell updatedPci = new UpdateCell(currentCell.getServerId(), ipPortArr[0], ipPortArr[1], oneCell);
                Gson gson = new Gson();
                String jsonStr = gson.toJson(updatedPci);
                if (ipPort != null && !ipPort.trim().equals("")) {
                    Session clSess = webSocketSessions.get(ipPort);
                    if (clSess != null) {
                        RansimWebSocketServer.sendUpdateCellMessage(jsonStr, clSess);
                    } else {
                        log.info("No client session for " + ipPort);
                    }
                } else {
                    log.info("No client for " + currentCell.getServerId());
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
     * The function converts the cell details data corresponding to the given server
     * Id into a json string format.
     *
     * @param serverId
     *            server Id of the netconf server for which the details is required
     * @return It returns the json String
     */
    public String netconfServersList(String serverId) {

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("clientRansim");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            NetconfServers ns = entitymanager.find(NetconfServers.class, serverId);

            if (ns != null) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(ns);

                return jsonStr;
            } else {
                return ("Invalid Input");
            }

        } catch (Exception eu) {
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            return ("Failure");
        } finally {
            entitymanager.close();
            emfactory.close();
        }
    }

    /**
     * The function unmounts the connection with SDNR.
     *
     * @return returns null value
     */
    public String stopAllCells() {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            TypedQuery<NetconfServers> query = entitymanager.createQuery("from NetconfServers e", NetconfServers.class);
            List<NetconfServers> ncServers = query.getResultList();
            for (NetconfServers netconfServers : ncServers) {
                try {
                    log.info("Unmount " + netconfServers.getServerId());
                    new RestClient().sendUnmountRequestToSdnr(netconfServers.getServerId(), sdnrServerIp,
                            sdnrServerPort, sdnrServerUserid, sdnrServerPassword);
                } catch (Exception e) {
                    log.info("Ignore Exception:", e);
                }
                serverIdIpNodeMapping.clear();
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
        return null;
    }

    /**
     * Used to dump session details.
     */
    public void dumpSessionDetails() {
        log.info("serverIdIpPortMapping.size:" + serverIdIpPortMapping.size() + "webSocketSessions.size"
                + webSocketSessions.size());
        for (String key : serverIdIpPortMapping.keySet()) {
            String val = serverIdIpPortMapping.get(key);
            Session sess = webSocketSessions.get(val);
            log.info("ServerId:" + key + " IpPort:" + val + " Session:" + sess);
        }
        for (String serverId : unassignedServerIds) {
            log.info("Unassigned ServerId:" + serverId);
        }
        for (String serverId : serverIdIpPortMapping.keySet()) {
            List<String> attachedNoeds = serverIdIpNodeMapping.get(serverId);
            if (attachedNoeds != null) {
                log.info("ServerId:" + serverId + " attachedNoeds.size:" + attachedNoeds.size() + " nodes:"
                        + attachedNoeds.toArray());
            } else {
                log.info("ServerId:" + serverId + " attachedNoeds:" + null);
            }
        }

    }

}

class KeepWebsockAliveThread extends Thread {
    static Logger log = Logger.getLogger(KeepWebsockAliveThread.class.getName());
	RansimController rsCtrlr = null;
        KeepWebsockAliveThread(RansimController ctrlr) {
		rsCtrlr = ctrlr;
        }
	public void run() {
                log.info("Inside KeepWebsockAliveThread run method");
		while(true) {
        	for (String ipPort : rsCtrlr.webSocketSessions.keySet()) {
			try{
            		Session sess = rsCtrlr.webSocketSessions.get(ipPort);
            		RansimWebSocketServer.sendPingMessage(sess);
                	log.debug("Sent ping message to Client ipPort:" + ipPort);
			}catch(Exception ex1){}
		}
		try { Thread.sleep(10000); } catch(Exception ex){}
		}
        }
}
