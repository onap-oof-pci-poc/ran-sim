package org.onap.ransim.rest.api.models;

import static org.junit.Assert.*;

import org.junit.Test;
import org.onap.ransim.rest.api.models.CellDetails;
import org.onap.ransim.rest.api.models.ModifyACellReq;
import org.onap.ransim.rest.api.models.NetconfServers;
import org.onap.ransim.rest.api.models.StartSimulationReq;

public class TestApiModels {

    @Test
    public void testsetNewPhysicalCellId() {
        ModifyACellReq mcell = new ModifyACellReq();
        mcell.setNewPhysicalCellId(001L);
        assertTrue(mcell.getNewPhysicalCellId() == 001);
    }
    
    @Test
    public void testsetNewPhysicalCellId1() {
        ModifyACellReq mcell = new ModifyACellReq();
        mcell.setNewPhysicalCellId(000L);
        assertFalse(mcell.getNewPhysicalCellId() == 001);
    }

    @Test
    public void testsetNetworkId() {
        CellDetails cd = new CellDetails();
        cd.setNetworkId("Ns001");
        assertTrue(cd.getNetworkId() == "Ns001");
    }

    @Test
    public void testsetServerId() {
        NetconfServers ns = new NetconfServers();
        ns.setServerId("ns0031");
        assertTrue(ns.getServerId() == "ns0031");
    }

    @Test
    public void testsetNumberOfClusters() {
        StartSimulationReq simulate = new StartSimulationReq();
        simulate.setNumberOfClusters(3);
        assertTrue(simulate.getNumberOfClusters() == 3);
    }


}
