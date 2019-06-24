package org.onap.ransim.rest.api.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Test;
import org.mockito.Mockito;
import org.onap.ransim.rest.api.models.CellDetails;
import org.onap.ransim.rest.api.models.StartSimulationReq;
import org.springframework.http.ResponseEntity;

public class TestRansimControllerServices {


    @Test
    public void testStartSimulation() throws Exception {
        ResponseEntity<String> rsEntity = Mockito.mock(ResponseEntity.class);
        
        RansimControllerServices rsservices = Mockito.mock(RansimControllerServices.class);
        
        //EntityManagerFactory emfactory = Mockito.mock(EntityManagerFactory.class);
        //EntityManager entityManager = Mockito.mock(EntityManager.class);
        //Mockito.when(emfactory.createEntityManager()).thenReturn(entityManager);
        
        StartSimulationReq req = new StartSimulationReq();
        req.setClusterLevel(0);
        req.setGridSize(0);
        req.setGridType(3);
        req.setNumberOfClusters(0);
        
        new MockUp<RansimControllerDatabase>() {
            @Mock
            List<CellDetails> getCellDetailsList() {
                 return null;
            }
        };
        new MockUp<RansimController>() {
            @Mock
            public void loadProperties() {
                 
            }
        };
        new MockUp<RansimController>() {
            @Mock
            public boolean hasEnoughRansimAgentsRunning(int cellsToBeSimulated) {
                 return true;
            }
        };
        new MockUp<RansimController>() {
            @Mock
            public void generateCluster(int gdSize, int gdType, int numberOfClusters, int clusterLevel) {
                 
            }
        };
        new MockUp<RansimController>() {
            @Mock
            public void sendInitialConfigAll() {
                 
            }
        };
        
        //ResponseEntity<String> result = new ResponseEntity<String>();
        //ResponseEntity<String> result = rsservices.startSimulation(req);
        
        System.out.println(rsservices.startSimulation(req));
        
        //Mockito.verify(rsservices).startSimulation(req);
        //System.out.println("result.getStatusCode(): " + result.getStatusCode());
        
        //assertEquals(result.getStatusCode(), 200);
        //assertNotNull(rsEntity);
        
    }
    
    @Test
    public void testGetOperationLog() {
        ResponseEntity<String> rsEntity = Mockito.mock(ResponseEntity.class);
        
        EntityManagerFactory emfactory = Mockito.mock(EntityManagerFactory.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        Mockito.when(emfactory.createEntityManager()).thenReturn(entityManager);
        
        TypedQuery<CellDetails> query = Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("from CellDetails cd", CellDetails.class)).thenReturn(query);
        
        List<CellDetails> cellDetailList = new ArrayList<CellDetails>();
        Mockito.when(query.getResultList()).thenReturn(cellDetailList);
        assertNotNull(rsEntity);
        
    }
    
    @Test
    public void testModifyACell() {
        
        ResponseEntity<String> rsEntity = Mockito.mock(ResponseEntity.class);
        
        EntityManagerFactory emfactory = Mockito.mock(EntityManagerFactory.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        Mockito.when(emfactory.createEntityManager()).thenReturn(entityManager);
        
        TypedQuery<CellDetails> query = Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("from CellDetails cd", CellDetails.class)).thenReturn(query);
        
        
    }

    
}