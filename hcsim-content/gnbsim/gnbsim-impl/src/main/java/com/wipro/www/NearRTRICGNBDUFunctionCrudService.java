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
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunctionBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.AttributesBuilder;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearRTRICGNBDUFunctionCrudService implements CrudService<GNBDUFunction> {

    private static final Logger LOG = LoggerFactory.getLogger(NearRTRICGNBDUFunctionCrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<GNBDUFunction> identifier, @Nonnull GNBDUFunction data) throws WriteFailedException {
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
    public void deleteData(@Nonnull InstanceIdentifier<GNBDUFunction> identifier, @Nonnull GNBDUFunction data) throws WriteFailedException {
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
    public void updateData(@Nonnull InstanceIdentifier<GNBDUFunction> identifier, @Nonnull GNBDUFunction dataOld, @Nonnull GNBDUFunction dataNew) throws WriteFailedException {
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
    public GNBDUFunction readSpecific(@Nonnull InstanceIdentifier<GNBDUFunction> identifier) throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);

        final GNBDUFunctionKey key = identifier.firstKeyOf(GNBDUFunction.class);

	String gNBId = null;
	boolean isDataReady = false;

	List<com.wipro.www.websocket.models.nearRTRIC> nearRTRICList = InMemoryDataTree.getInstance().getNearRTRIC();

        for(int i=0; i < nearRTRICList.size() ; i++)
        {
                List<com.wipro.www.websocket.models.GNBDUFunction> inList = (nearRTRICList.get(i)).getgNBDUFunction();

                for(int j=0; j < inList.size(); j++)
                {                                                                                                                                                                                 if((inList.get(j)).getIdGNBDUFunction() == key.getIdGNBDUFunction())
                        {
                                gNBId = (inList.get(j)).getAttributes().getgNBId();
                                isDataReady = true;
                                break;
                        }
                }
		if(isDataReady == true)
		{
			break;
		}
        }
		
		
       if(isDataReady)                                                                                                                                          {
             LOG.info("gNBID value:[{}]", gNBId);
             final Attributes attributes = new AttributesBuilder().setGNBId(Long.parseLong(gNBId)).build();
             return new GNBDUFunctionBuilder()
                .setIdGNBDUFunction(key.getIdGNBDUFunction())
                .setAttributes(attributes)
                .build();
        } else {
             return null;
        }

        //final Attributes attributes = new AttributesBuilder().setGNBId(2L).build();

        //return new GNBDUFunctionBuilder()
        //        .setIdGNBDUFunction(key.getIdGNBDUFunction())
        //        .setAttributes(attributes)
        //        .build();

    }

    @Override
    public List<GNBDUFunction> readAll() throws ReadFailedException {

        List<com.wipro.www.websocket.models.nearRTRIC> listNearRTRIC = InMemoryDataTree.getInstance().getNearRTRIC();
        List<GNBDUFunction> outList = new ArrayList<GNBDUFunction>();
        String idNearRTRIC=null;
        String idGNBDUFunction=null;

        try {
                for(int i=0; i < listNearRTRIC.size(); i++)
                {
                        idNearRTRIC = (listNearRTRIC.get(i)).getIdNearRTRIC();
                        List<com.wipro.www.websocket.models.GNBDUFunction> inputList = (listNearRTRIC.get(i)).getgNBDUFunction();

			for(int j=0; j < inputList.size(); j++)
			{
                            idGNBDUFunction = (inputList.get(j)).getIdGNBDUFunction();
     
                            LOG.info("GNBDUFunction ID:[{}]",idGNBDUFunction);

                            outList.add(readSpecific(InstanceIdentifier.create(RanNetwork.class).child(NearRTRIC.class, new NearRTRICKey(idNearRTRIC)).child(GNBDUFunction.class, new GNBDUFunctionKey(idGNBDUFunction))));
			}

                }
                return outList;
        } catch (Exception e) {
                LOG.info("Exception:[{}]", e);
                return null;
        }
        
        //return Collections.singletonList(
        //                       readSpecific(InstanceIdentifier.create(RanNetwork.class).child(NearRTRIC.class, new NearRTRICKey("RTRIC2")).child(GNBDUFunction.class, new GNBDUFunctionKey("GNBDUFUN2"))));
    }
}
