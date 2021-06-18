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

import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteFailedException;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetwork;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRIC;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunctionKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.NRCellDU;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.NRCellDUKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.NRCellDUBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.AttributesBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.OperationalState;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

public class GNBDUFunctionNRCellDUCrudService implements CrudService<NRCellDU> {

    enum OperationalState{
       DISABLED,
       ENABLED
    }; 
    private static final Logger LOG = LoggerFactory.getLogger(GNBDUFunctionNRCellDUCrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<NRCellDU> identifier, @Nonnull NRCellDU data) throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull InstanceIdentifier<NRCellDU> identifier, @Nonnull NRCellDU data) throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Removing path[{}] / data [{}]", identifier, data);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void updateData(@Nonnull InstanceIdentifier<NRCellDU> identifier, @Nonnull NRCellDU dataOld, @Nonnull NRCellDU dataNew) throws WriteFailedException {
        if (dataOld != null && dataNew != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Update path[{}] from [{}] to [{}]", identifier, dataOld, dataNew);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public NRCellDU readSpecific(@Nonnull InstanceIdentifier<NRCellDU> identifier) throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);
	
	final NRCellDUKey key = identifier.firstKeyOf(NRCellDU.class);

	boolean isDataReady = false;
	String operationalState = null;
	String cellState = null;

        List<com.wipro.www.websocket.models.nearRTRIC> nearRTRICList = InMemoryDataTree.getInstance().getNearRTRIC();

	for(int i=0; i < nearRTRICList.size() ; i++) 
	{
		List<com.wipro.www.websocket.models.GNBDUFunction> inList = (nearRTRICList.get(i)).getgNBDUFunction();
                
		for(int j=0; j < inList.size(); j++) 
		{
		    List<com.wipro.www.websocket.models.NRCellDU> inNRCellDUList = (inList.get(j)).getnRCellDU();

		    for(int m=0; m < inNRCellDUList.size(); m++)
	            {
			    //if((inNRCellDUList.get(m)).getIdNRCellDU() == key.getIdNRCellDU())
			    if((inNRCellDUList.get(m)).getIdNRCellDU().equalsIgnoreCase(key.getIdNRCellDU()))
			    {
			        operationalState = (inNRCellDUList.get(m)).getAttributes().getOperationalState();
				cellState = (inNRCellDUList.get(m)).getAttributes().getCellState();

				LOG.info("OperationalState from InMemory:[{}]",operationalState);
				LOG.info("CellState from InMemory:[{}]",cellState);

				isDataReady = true;
				break;
			    }
		    }

		    if(isDataReady==true)
		    {
		       break;
		    }
		}

		if(isDataReady==true)
		{
			break;
		}

	}
        LOG.info("isDataReady: " + isDataReady);
	if(isDataReady==true)
	{
	    int iOpValue = 0;
	    int iClValue = 0;

	    if(operationalState == "ENABLED") {
		    iOpValue = 1; 
	    } 

	    if(cellState == "INACTIVE") {
		    iClValue = 1;
	    } else if(cellState == "ACTIVE") {
		    iClValue = 2;
	    }

	    LOG.info("iOpValue:[{}]",iOpValue);
	    LOG.info("iClValue:[{}]",iClValue);

	    org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.OperationalState opState = org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.OperationalState.DISABLED;
            org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.CellState clState = org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.CellState.IDLE;

	    switch(iOpValue)
            {
		    case 0:
			 opState = org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.OperationalState.DISABLED;
			 break;
	            case 1:
			 opState = org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.OperationalState.ENABLED;
			 break;
	    }

	    switch(iClValue)
	    {
		    case 0:
			 clState = org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.CellState.IDLE;
			 break;
		    case 1:
			 clState = org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.CellState.INACTIVE;
			 break;
		    case 2:
			 clState = org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.CellState.ACTIVE;
			 break;
	    }

	    LOG.info("OperationalState value to be set:[{}]", opState);
	    LOG.info("CellState value to be set:[{}]", clState);

            final Attributes attributes = new AttributesBuilder().setOperationalState(opState).setCellState(clState).build();

            return new NRCellDUBuilder()
                .setIdNRCellDU(key.getIdNRCellDU())
                .setAttributes(attributes)
                .build(); 
	} else {

	    return null;
	}

    }

    @Override
    public List<NRCellDU> readAll() throws ReadFailedException {

        List<com.wipro.www.websocket.models.nearRTRIC> listNearRTRIC = InMemoryDataTree.getInstance().getNearRTRIC();
        List<GNBDUFunction> gnbDUFunctionList = new ArrayList<GNBDUFunction>();
        List<NRCellDU> outList = new ArrayList<NRCellDU>();
        String idNearRTRIC=null;
        String idGNBDUFunction=null;
        String idNRCellDU=null;
	boolean isDataReady = false;

        try {
                for(int i=0; i < listNearRTRIC.size(); i++)
                {
                        idNearRTRIC = (listNearRTRIC.get(i)).getIdNearRTRIC();                                                                                                                    List<com.wipro.www.websocket.models.GNBDUFunction> inputList = (listNearRTRIC.get(i)).getgNBDUFunction();
                        List<com.wipro.www.websocket.models.NRCellDU> inputNRCellDUList = new ArrayList<com.wipro.www.websocket.models.NRCellDU>();

                        //LOG.info("GNBDUFunction ID:[{}]",idGNBDUFunction);
                        
                        for(int j=0; j < inputList.size(); j++)
                        {
			     idGNBDUFunction = (inputList.get(j)).getIdGNBDUFunction();
                         
			     LOG.info("GNBDUFunction ID in NRCellDU:[{}]",idGNBDUFunction);

                             inputNRCellDUList = (inputList.get(j)).getnRCellDU();

			     for (Map.Entry<String, Integer> hm : (InMemoryDataTree.getInstance().hashMapgNBDUFunc).entrySet()) { 
				                         			    
                             if(hm.getKey() == idGNBDUFunction) {
				     if(hm.getValue() == 0) {

           			     for(int m=0; m < inputNRCellDUList.size(); m++)
	                             {
		         		     idNRCellDU = (inputNRCellDUList.get(m)).getIdNRCellDU();
 
         				     LOG.info("NRCellDU ID:[{}]",idNRCellDU);

                                             outList.add(readSpecific(InstanceIdentifier.create(RanNetwork.class).child(NearRTRIC.class, new NearRTRICKey(idNearRTRIC)).child(GNBDUFunction.class, new GNBDUFunctionKey(idGNBDUFunction)).child(NRCellDU.class, new NRCellDUKey(idNRCellDU))));

		         		     InMemoryDataTree.getInstance().hashMapgNBDUFunc.put(idGNBDUFunction, 1);
					     isDataReady = true;
			            } //end for
			            } //end if
			      } //end if
                              
			      if(isDataReady == true) {
				      break;
			      }
			     }
                             
			     if(isDataReady == true) {
				     break;
			     }
                        }

                        //outList.add(readSpecific(InstanceIdentifier.create(RanNetwork.class).child(NearRTRIC.class, new NearRTRICKey(idNearRTRIC)).child(GNBDUFunction.class, new GNBDUFunctionKey(idGNBDUFunction))));
			if(isDataReady == true) {
				break;
			}

                }
                return outList;
        } catch (Exception e) {                                                                                                                                                           LOG.info("Exception:[{}]", e);
                return null;
        }
	
        //return Collections.singletonList(
	//		readSpecific(InstanceIdentifier.create(RanNetwork.class).child(NearRTRIC.class, new NearRTRICKey("RTRIC2")).child(GNBDUFunction.class, new GNBDUFunctionKey("GNBDUFUN2")).child(NRCellDU.class, new NRCellDUKey("NRCELL1"))));
    }

}
