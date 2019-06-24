/**
 * 
 */
package org.onap.ransim.rest.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Test;
import org.junit.runner.RunWith;
//import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.ransim.rest.api.models.CellDetails;
import org.onap.ransim.rest.api.models.CellNeighbor;
import org.onap.ransim.rest.api.models.FmAlarmInfo;
import org.onap.ransim.rest.api.models.GetNeighborList;
import org.onap.ransim.rest.api.models.NeighborDetails;
import org.onap.ransim.rest.api.models.NeihborId;
import org.onap.ransim.rest.api.models.NetconfServers;
import org.onap.ransim.rest.client.RestClient;
import org.onap.ransim.websocket.model.CommonEventHeaderFm;
import org.onap.ransim.websocket.model.EventFm;
import org.onap.ransim.websocket.model.FaultFields;
import org.springframework.context.annotation.PropertySource;

import com.google.gson.Gson;

/**
 * @author ubuntu16
 *
 */
@RunWith(MockitoJUnitRunner.class)
@PropertySource("classpath:ransim.properties")
public class TestRansimController {
    @Test
    public void testGetRansimController() {
        
        RansimController rc = RansimController.getRansimController();
        assertNotNull(rc);
    }
        
    @Test
    public void testsetNetconfServers() {
        // fail("Not yet implemented");
        RansimController rscontroller = Mockito.mock(RansimController.class);
        CellDetails cell1 = new CellDetails("Chn01", 1, "nc1");
        CellDetails cell2 = new CellDetails("Chn02", 2, "nc1");
        CellDetails cell3 = new CellDetails("Chn03", 3, "nc1");
        CellDetails cell4 = new CellDetails("Chn04", 4, "nc1");
        
        Set<CellDetails> cells = new HashSet<CellDetails>();
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);
        
        NetconfServers server = new NetconfServers("nc1", null, null, cells);
        
        new MockUp<RansimControllerDatabase>() {
            @Mock
            NetconfServers getNetconfServer(String serverId) {
                
                return server;
            }
        };
        new MockUp<RansimControllerDatabase>() {
            @Mock
            CellDetails getCellDetail(String nodeId) {
                
                return cell4;
            }
        };
        new MockUp<RansimControllerDatabase>() {
            @Mock
            void mergeNetconfServers(NetconfServers netconfServers) {
                
            }
        };
        
        rscontroller.setNetconfServers("Chn04");
        
        boolean check = server.getCells().contains(cell4);
        
        assertTrue(check);
        
    }
    
    @Test
    public void testGenerateNeighborList() {
        // fail("Not yet implemented");
        RansimController rscontroller = Mockito.mock(RansimController.class);
        Set<NeighborDetails> neighborList = new HashSet<NeighborDetails>();
        NeighborDetails nbr1 = new NeighborDetails(new NeihborId("Chn00", "Chn01"), false);
        NeighborDetails nbr2 = new NeighborDetails(new NeihborId("Chn00", "Chn02"), false);
        NeighborDetails nbr3 = new NeighborDetails(new NeihborId("Chn00", "Chn03"), true);
        
        neighborList.add(nbr1);
        neighborList.add(nbr2);
        neighborList.add(nbr3);
        
        CellDetails cell0 = new CellDetails("Chn00", 4, "nc1");
        CellDetails cell1 = new CellDetails("Chn01", 1, "nc1");
        CellDetails cell2 = new CellDetails("Chn02", 2, "nc1");
        CellDetails cell3 = new CellDetails("Chn03", 3, "nc1");
        
        CellNeighbor cellNbr = new CellNeighbor();
        cellNbr.setNodeId("Chn00");
        cellNbr.setNeighborList(neighborList);
        
        new MockUp<RansimControllerDatabase>() {
            @Mock
            CellNeighbor getCellNeighbor(String nodeId) {
                if (nodeId.equals("Chn00")) {
                    return cellNbr;
                } else {
                    return null;
                }
                
            }
        };
        
        new MockUp<RansimControllerDatabase>() {
            @Mock
            CellDetails getCellDetail(String nodeId) {
                if (nodeId.equals("Chn00")) {
                    return cell0;
                } else if (nodeId.equals("Chn01")) {
                    return cell1;
                } else if (nodeId.equals("Chn02")) {
                    return cell2;
                } else if (nodeId.equals("Chn03")) {
                    return cell3;
                } else {
                    return null;
                }
                
            }
        };
        
        GetNeighborList nbrListFct = rscontroller.generateNeighborList("Chn00");
        
        boolean result = false;
        
        if (nbrListFct.getCellsWithHo().contains(cell1)) {
            if (nbrListFct.getCellsWithHo().contains(cell2)) {
                if (nbrListFct.getCellsWithNoHo().contains(cell3)) {
                    result = true;
                }
            }
        }
        
        assertTrue(result);
        
    }
        
    @Test
    public void testSetEventFm() {
        // fail("Not yet implemented");
        RansimController rscontroller = Mockito.mock(RansimController.class);
        Map<String, String> alarmAdditionalInformation = new HashMap<String, String>();
        alarmAdditionalInformation.put("networkId", "abc");
        alarmAdditionalInformation.put("collisions", "1");
        alarmAdditionalInformation.put("confusions", "0");
        CommonEventHeaderFm commonEventHeader = new CommonEventHeaderFm("Chn00", "", "nc1", 0, 0);
        FaultFields faultFields = new FaultFields("RanPciCollisionConfusionOccurred", "other",
                "Collision", "CRITICAL", alarmAdditionalInformation);
        EventFm checkObj = new EventFm(commonEventHeader, faultFields);
        
        new MockUp<RansimController>() {
            @Mock
            String getUuid() {
                return "";
            }
        };
        new MockUp<System>() {
            @Mock
            public long currentTimeMillis() {
                return (long) 0;
            }
        };
        
        String networkId = "abc";
        String ncServer = "nc1";
        String cellId = "Chn00";
        FmAlarmInfo issue = new FmAlarmInfo("Collision", "1", "0");
        EventFm eventObj = rscontroller.setEventFm(networkId, ncServer, cellId, issue);
        
        boolean result = false;
        
        Gson gson = new Gson();
        String eventStr = gson.toJson(eventObj);
        String checkStr = gson.toJson(checkObj);
        
        System.out.println("eventStr: " + eventStr);
        System.out.println("checkStr: " + checkStr);
        
        if (eventStr.equals(checkStr)) {
            result = true;
        }
        
        assertTrue(result);
    }
    
    @Test
    public void testSetCollisionConfusionFromFile() {
        //fail("Not yet implemented");
        RansimController rscontroller = Mockito.mock(RansimController.class);
        
        CellDetails cell0 = new CellDetails("Chn00", 1, "nc1");
        CellDetails cell1 = new CellDetails("Chn01", 1, "nc1");
        CellDetails cell2 = new CellDetails("Chn02", 2, "nc1");
        CellDetails cell3 = new CellDetails("Chn03", 3, "nc1");
        
        new MockUp<RansimControllerDatabase>() {
            @Mock
            CellDetails getCellDetail(String nodeId) {
                if (nodeId.equals("Chn01")) {
                    return cell1;
                } else {
                    return null;
                }
                
            }
        };
        
        new MockUp<RansimController>() {
            @Mock
            GetNeighborList generateNeighborList(String nodeId) {
                GetNeighborList result = new GetNeighborList();
                List<CellDetails> cellsWithNoHO = new ArrayList<CellDetails>();
                List<CellDetails> cellsWithHO = new ArrayList<CellDetails>();
                
                cellsWithHO.add(cell0);
                cellsWithHO.add(cell2);
                cellsWithHO.add(cell3);
                
                result.setNodeId("Chn01");
                result.setCellsWithHo(cellsWithHO);
                result.setCellsWithNoHo(cellsWithNoHO);
                
                return result;
            }
        };
        
        new MockUp<RansimControllerDatabase>() {
            @Mock
            void mergeCellDetails(CellDetails cellDetail) {
                                
            }
        };
        
        FmAlarmInfo result = rscontroller.setCollisionConfusionFromFile("Chn01");
        
        //System.out.println(result.getProblem() + result.getCollisionCount() + result.getConfusionCount());
        //System.out.println(result);
        
        String collisionCount = "" + 1;
        String confusionCount = "" + 0;
        String problem = "Collision";
        FmAlarmInfo check = new FmAlarmInfo(problem, collisionCount , confusionCount);
        
        //System.out.println(check.getProblem() + check.getCollisionCount() + check.getConfusionCount());
        //System.out.println(check);
        boolean rlt = false;
        
        if (result.getProblem().equals(check.getProblem())) {
            if(result.getCollisionCount().equals(check.getCollisionCount())){
                if(result.getConfusionCount().equals(check.getConfusionCount())){
                    rlt = true;
                    //System.out.println(rlt);
                }
                
            }
            
        }
        
        //System.out.println(rlt);
        assertTrue(rlt);
        //assertTrue(check1.equals(check));
        
    }
       
    
    @Test
    public void testModifyCellFunctionPciChange() {
        //fail("Not yet implemented");
        RansimController rscontroller = Mockito.mock(RansimController.class);
        
        CellDetails cell0 = new CellDetails("Chn00", 1, "nc1");
        
        String nodeId = "Chn00";
        long pci = 10;
        String source = "GUI";
        
        
        NeighborDetails nbr1 = new NeighborDetails(new NeihborId("Chn00", "Chn01"), false);
        NeighborDetails nbr2 = new NeighborDetails(new NeihborId("Chn00", "Chn02"), false);
        NeighborDetails nbr3 = new NeighborDetails(new NeihborId("Chn00", "Chn03"), false);
        
        Set<NeighborDetails> oldNbrs = new HashSet<NeighborDetails>();
        oldNbrs.add(nbr1);
        oldNbrs.add(nbr2);
        oldNbrs.add(nbr3);
        
        List<NeighborDetails> newNbrs = new ArrayList<NeighborDetails>();
        newNbrs.add(nbr1);
        newNbrs.add(nbr2);
        newNbrs.add(nbr3);
        
        new MockUp<RansimControllerDatabase>() {
            @Mock
            CellDetails getCellDetail(String nodeId) {
                System.out.println("getCellDetail");  
                    return cell0;              
            }
        };
        new MockUp<RansimController>() {
            @Mock
            void updatePciOperationsTable(String nodeId, long physicalCellId, long oldPciId) {
                System.out.println("updatePciOperationsTable");             
            }
        };
        new MockUp<RansimControllerDatabase>() {
            @Mock
            void mergeCellDetails(CellDetails cellDetail) {
                System.out.println("mergeCellDetails");
                //cell0 = new CellDetails();
            }
        };
        new MockUp<RansimControllerDatabase>() {
            @Mock
            CellNeighbor getCellNeighbor(String nodeId) {
                System.out.println("getCellNeighbor");
                CellNeighbor cn = new CellNeighbor("Chn00", oldNbrs);
                return cn;          
            }
        };
        new MockUp<RansimControllerDatabase>() {
            @Mock
            void mergeNeighborDetails(NeighborDetails neighborDetails) {
                System.out.println("mergeNeighborDetails");
                            
            }
        };
        
        new MockUp<RansimControllerDatabase>() {
            @Mock
            void mergeCellNeighbor(CellNeighbor cellNeighbor) {
                System.out.println("mergeCellNeighbor");          
            }
        };
        new MockUp<RansimController>() {
            @Mock
            public void generateFmData(String source, CellDetails cell, List<NeighborDetails> newNeighborList) {
                System.out.println("generateFmData");         
            }
        };
        new MockUp<RansimController>() {
            @Mock
            void updateNbrsOperationsTable(String nodeId, String addedNbrs, String deletedNbrs)  {
                System.out.println("updateNbrsOperationsTable");             
            }
        };
        
        int check = -1;
        System.out.println("check = " + check);
        check = rscontroller.modifyCellFunction(nodeId, pci, newNbrs, source);
        
        System.out.println("check = " + check);
        System.out.println("pci: " + cell0.getPhysicalCellId());
        assertTrue(cell0.getPhysicalCellId() == 10);
        
    }
            
    @Test
    public void testStopAllCells() {
        RansimController rscontroller = Mockito.mock(RansimController.class);
         
        new MockUp<RansimControllerDatabase>() {
            @Mock
            List<NetconfServers> getNetconfServersList()  {
                System.out.println("getNetconfServersList");
                List<NetconfServers> ns = new ArrayList<NetconfServers>();
                NetconfServers n1 = new NetconfServers("nc1", null, null, null);
                NetconfServers n2 = new NetconfServers("nc2", null, null, null);
                ns.add(n1);
                ns.add(n2);
                return ns;
            }
        };
        
        new MockUp<RestClient>() {
            @Mock
            public String sendUnmountRequestToSdnr(String serverId, String ip, int port, String sdnrUsername,
                    String sdnrPassword)  {
                System.out.println("sendUnmountRequestToSdnr"); 
                return "";
            }
        };
        
        String result = rscontroller.stopAllCells();
        System.out.println("testStopAllCells: " + result);
        assertEquals("Netconf servers unmounted.", result);
    }
    
    
    
}