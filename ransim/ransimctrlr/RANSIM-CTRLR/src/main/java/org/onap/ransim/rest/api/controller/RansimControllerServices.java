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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.onap.ransim.rest.api.models.CellDetails;
import org.onap.ransim.rest.api.models.CellNeighbor;
import org.onap.ransim.rest.api.models.DeleteACellReq;
import org.onap.ransim.rest.api.models.GetACellDetailReq;
import org.onap.ransim.rest.api.models.GetNeighborListReq;
import org.onap.ransim.rest.api.models.GetNetconfServerDetailsReq;
import org.onap.ransim.rest.api.models.ModifyACellReq;
import org.onap.ransim.rest.api.models.NetconfServers;
import org.onap.ransim.rest.api.models.StartSimulationReq;
import org.onap.ransim.rest.api.models.Topology;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Ran Simulator Controller Services")
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class RansimControllerServices {

    static Logger log = Logger.getLogger(RansimControllerServices.class.getName());

    private static boolean isSimulationStarted = false;

    private static RansimControllerServices rscServices = null;

    private RansimControllerServices() {

    }

    /**
     * To accesss variable of this class from another class.
     *
     * @return returns rscServices constructor
     */
    public static synchronized RansimControllerServices getRansimControllerServices() {
        if (rscServices == null) {
            rscServices = new RansimControllerServices();
        }
        return rscServices;
    }

    RansimController rsCtrlr = RansimController.getRansimController();

    /**
     * The function starts the RAN network simulation.
     *
     * @param req
     *            gets the necessary details as a request of class type
     *            StartSimulationReq
     * @return returns Http status
     * @throws Exception
     *             throws exceptions in the functions
     */
    @ApiOperation("Starts the RAN network simulation")
    @RequestMapping(value = "/StartSimulation", method = RequestMethod.POST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Cannot start the simulation") })
    public ResponseEntity<String> startSimulation(@RequestBody StartSimulationReq req) throws Exception {

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        Query query = entitymanager.createQuery("from CellDetails cd", CellDetails.class);
        if (!query.getResultList().isEmpty()) {
            return new ResponseEntity<>("Already simulation is running.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            rsCtrlr.loadProperties();

            if (!rsCtrlr.hasEnoughRansimAgentsRunning(req.getGridSize() * req.getGridSize())) {
                return new ResponseEntity<>("Not sufficient Ransim Agents running.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            long startTimeStartSimulation = System.currentTimeMillis();
            rsCtrlr.generateCluster(req.getGridSize(), req.getGridType(), req.getNumberOfClusters(),
                    req.getClusterLevel());
            rsCtrlr.sendInitialConfigAll();
            long endTimeStartSimulation = System.currentTimeMillis();
            log.info("Time taken for start simulation : " + (endTimeStartSimulation - startTimeStartSimulation));

            // return new ResponseEntity<>("Success", HttpStatus.OK);
            return new ResponseEntity<String>(HttpStatus.OK);

        } catch (Exception eu) {
            log.info("/StartSimulation ", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            // return new ResponseEntity<>("Failure StartSimulatio",
            // HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            entitymanager.close();
            emfactory.close();
        }

    }

    /**
     * The function retrieves RAN simulation network topology.
     *
     * @return returns Http status
     * @throws Exception
     *             throws exceptions in the functions
     *
     */
    @ApiOperation("Retrieves RAN simulation network topology")
    @RequestMapping(value = "/GetTopology", method = RequestMethod.GET)

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Cannot retrieve the RAN simulation network topology details") })
    public ResponseEntity<String> getTopology() throws Exception {
        log.info("Inside getTopology...");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {

            Query query = entitymanager.createQuery("from CellDetails cd", CellDetails.class);
            List<CellDetails> cds = query.getResultList();

            Topology top = new Topology();

            if (cds != null && cds.size() > 0) {
                top.setMinScreenX(cds.get(0).getScreenX());
                top.setMaxScreenX(cds.get(0).getScreenX());
                top.setMinScreenY(cds.get(0).getScreenY());
                top.setMaxScreenY(cds.get(0).getScreenY());

                for (int i = 0; i < cds.size(); i++) {
                    if (cds.get(i).getScreenX() < top.getMinScreenX()) {
                        top.setMinScreenX(cds.get(i).getScreenX());
                    }
                    if (cds.get(i).getScreenY() < top.getMinScreenY()) {
                        top.setMinScreenY(cds.get(i).getScreenY());
                    }

                    if (cds.get(i).getScreenX() > top.getMaxScreenX()) {
                        top.setMaxScreenX(cds.get(i).getScreenX());
                    }
                    if (cds.get(i).getScreenY() > top.getMaxScreenY()) {
                        top.setMaxScreenY(cds.get(i).getScreenY());
                    }
                }
                top.setCellTopology(cds);
            }
            top.setGridSize(rsCtrlr.gridSize);

            Gson gson = new Gson();
            String jsonStr = gson.toJson(top);

            return new ResponseEntity<>(jsonStr, HttpStatus.OK);

        } catch (Exception eu) {
            log.info("/GetTopology", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            return new ResponseEntity<>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            entitymanager.close();
            emfactory.close();
        }
    }

    /**
     * The function retrieves the neighbor list details for the cell with the
     * mentioned nodeId.
     *
     * @param req
     *            gets the necessary details as a request of class type
     *            GetNeighborListReq
     * @return returns Http status
     * @throws Exception
     *             throws exceptions in the functions
     */
    @ApiOperation("Retrieves the neighbor list details for the cell with the mentioned nodeId")
    @RequestMapping(value = "/GetNeighborList", method = RequestMethod.POST)

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Cannot Insert the given parameters") })
    public ResponseEntity<String> getNeighborList(@RequestBody GetNeighborListReq req) throws Exception {
        log.info("Inside getNeighborList...");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            String jsonStr = "";
            CellNeighbor neighborList = entitymanager.find(CellNeighbor.class, req.getNodeId());
            if (neighborList != null) {
                Gson gson = new Gson();
                jsonStr = gson.toJson(neighborList);
            }
            return new ResponseEntity<>(jsonStr, HttpStatus.OK);

        } catch (Exception eu) {
            log.info("/getNeighborList", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            return new ResponseEntity<>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            entitymanager.close();
            emfactory.close();
        }
    }

    /**
     * Changes the pci number or nbr list for the given cell.
     *
     * @param req
     *            gets the necessary details as a request of class type
     *            ModifyACellReq
     * @return returns Http status
     * @throws Exception
     *             throws exceptions in the functions
     */
    @ApiOperation("Changes the pci number or nbr list for the given cell")
    @RequestMapping(value = "/ModifyACell", method = RequestMethod.POST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Cannot update the PCI for the given cell") })
    public ResponseEntity<String> modifyACell(@RequestBody ModifyACellReq req) throws Exception {
        log.info("Inside ModifyCell...");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            long startTimemodifyCell = System.currentTimeMillis();

            String nbrsStr = req.getNewNbrs();
            if (req.getNewNbrs() == null) {
                nbrsStr = "";
            }
            int result = rsCtrlr.modifyCellFunction(req.getNodeId(), req.getNewPhysicalCellId(), nbrsStr);
            log.info("Inside modify cell : " + (startTimemodifyCell));
            log.info("Result:********************" + result);
            rsCtrlr.handleModifyPciFromGui(req.getNodeId(), req.getNewPhysicalCellId());
            long endTimemodifyCell = System.currentTimeMillis();
            log.info("Time taken to modify cell : " + (endTimemodifyCell - startTimemodifyCell));

            if (result == 200) {
                return new ResponseEntity<String>(HttpStatus.OK);
            } else if (result == 400) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception eu) {
            log.info("Exception in modifyACell", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            return new ResponseEntity<>("Cannot update the PCI for the given cell", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            entitymanager.close();
            emfactory.close();
        }

    }

    /**
     * The function changes the PCI number of the cell for the the mentioned nodeId.
     *
     * @param req
     *            gets the necessary details as a request of class type
     *            GetACellDetailReq
     * @return returns Http status
     * @throws Exception
     *             throws exceptions in the functions
     */
    @ApiOperation("Changes the pci number of the cell for the the mentioned nodeId")
    @RequestMapping(value = "/GetACellDetail", method = RequestMethod.POST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Cannot retrive the cell details for the given cell") })
    public ResponseEntity<String> getACellDetail(@RequestBody GetACellDetailReq req) throws Exception {
        log.info("Inside GetACellDetailReq...");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            String jsonStr = null;

            CellDetails cd = entitymanager.find(CellDetails.class, req.getNodeId());

            if (cd != null) {
                Gson gson = new Gson();
                jsonStr = gson.toJson(cd);
            }
            return new ResponseEntity<>(jsonStr, HttpStatus.OK);

        } catch (Exception eu) {
            log.info("Exception in modifyACell", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            return new ResponseEntity<>("Cannot update the PCI for the given cell", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            entitymanager.close();
            emfactory.close();
        }

    }

    /**
     * The function deletes a cell with the mentioned nodeId.
     *
     * @param req
     *            gets the necessary details as a request of class type
     *            DeleteACellReq
     * @return returns Http status
     * @throws Exception
     *             throws exceptions in the functions
     */
    @ApiOperation("Deletes a cell with the mentioned nodeId")
    @RequestMapping(value = "/DeleteACell", method = RequestMethod.POST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Cannot delete the given cell") })
    public ResponseEntity<String> deleteACell(@RequestBody DeleteACellReq req) throws Exception {
        log.info("Inside delete cell...");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            long startTimeDeleteCell = System.currentTimeMillis();
            String result = rsCtrlr.deleteCellFunction(req.getNodeId());
            log.info("deleted in database...." + result);
            // return new ResponseEntity<>(result, HttpStatus.OK);
            long endTimeDeleteCell = System.currentTimeMillis();
            log.info("Time taken to delete cell : " + (endTimeDeleteCell - startTimeDeleteCell));

            return new ResponseEntity<String>(HttpStatus.OK);

        } catch (Exception eu) {
            log.info("Exception in deleteACell", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            // return new ResponseEntity<String>("Cannot delete the given cell",
            // HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            entitymanager.close();
            emfactory.close();
        }

    }

    /**
     * The function stops RAN network simulation and deletes all the cell data from
     * the database.
     *
     * @return returns Http status
     * @throws Exception
     *             throws exceptions in the functions
     */
    @ApiOperation("Stops RAN network simulation and deletes all the cell data from the database")
    @RequestMapping(value = "/StopSimulation", method = RequestMethod.DELETE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Cannot stop simulation") })
    public ResponseEntity<String> stopSimulation() throws Exception {
        log.info("Inside stopSimulation...");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();

        Query query = entitymanager.createQuery("from CellDetails cd", CellDetails.class);
        if (query.getResultList() == null) {
            return new ResponseEntity<>("No simulation is running.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            entitymanager.getTransaction().begin();
            long startTimStopSimulation = System.currentTimeMillis();
            Query q3 = entitymanager.createQuery("DELETE FROM NetconfServers ns");
            q3.executeUpdate();

            Query q2 = entitymanager.createQuery("DELETE FROM CellNeighbor cn");
            q2.executeUpdate();

            log.info("Stop simulation : " + (startTimStopSimulation));
            Query q1 = entitymanager.createQuery("DELETE FROM CellDetails cd");
            q1.executeUpdate();

            String result = rsCtrlr.stopAllCells();
            log.info("All cell simulation are stopped...." + result);

            entitymanager.flush();
            entitymanager.getTransaction().commit();

            long endTimStopSimulation = System.currentTimeMillis();
            log.info("Time taken for stopping simulation : " + (endTimStopSimulation - startTimStopSimulation));

            isSimulationStarted = false;
            return new ResponseEntity<>("Success", HttpStatus.OK);

        } catch (Exception eu) {
            log.info("Exception in stopSimulation", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            return new ResponseEntity<>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            entitymanager.close();
            emfactory.close();
        }

    }

    /**
     * The function returns the details of a Netconf server for the mentioned server
     * id.
     *
     * @param req
     *            gets the necessary details as a request of class type
     *            GetNetconfServerDetailsReq
     * @return returns Http status
     * @throws Exception
     *             throws exceptions in the functions
     */
    @ApiOperation("Returns the details of a Netconf server for the mentioned server id")
    @RequestMapping(value = "/GetNetconfServerDetails", method = RequestMethod.POST)

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Failure in GetNetconfServerDetails API") })
    public ResponseEntity<String> getNetconfServerDetails(@RequestBody GetNetconfServerDetailsReq req)
            throws Exception {

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ransimctrlrdb");
        EntityManager entitymanager = emfactory.createEntityManager();
        try {
            log.info("Inside GetNetconfServerDetails API...");
            entitymanager.getTransaction().begin();
            NetconfServers ns = entitymanager.find(NetconfServers.class, req.getServerId());

            String result;
            if (ns != null) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(ns);
                result = jsonStr;
            } else {
                result = ("Server Id does not exist");
            }

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception eu) {
            log.info("/GetNetconfServers", eu);
            if (entitymanager.getTransaction().isActive()) {
                entitymanager.getTransaction().rollback();
            }
            return new ResponseEntity<>("Failure in GetNetconfServerDetails API", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            entitymanager.close();
            emfactory.close();
        }
    }

}
