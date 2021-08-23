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

import com.wipro.www.websocket.models.nearRTRIC;

import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteFailedException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetwork;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRIC;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunctionBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunctionKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunctionKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.AttributesBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NearRTRICGNBCUUPFunctionCrudService implements CrudService<GNBCUUPFunction> {

    private static final Logger LOG = LoggerFactory.getLogger(NearRTRICGNBCUUPFunctionCrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<GNBCUUPFunction> identifier, @Nonnull GNBCUUPFunction data)
            throws WriteFailedException {
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
    public void deleteData(@Nonnull InstanceIdentifier<GNBCUUPFunction> identifier, @Nonnull GNBCUUPFunction data)
            throws WriteFailedException {
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
    public void updateData(@Nonnull InstanceIdentifier<GNBCUUPFunction> identifier, @Nonnull GNBCUUPFunction dataOld,
            @Nonnull GNBCUUPFunction dataNew) throws WriteFailedException {
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
    public GNBCUUPFunction readSpecific(@Nonnull InstanceIdentifier<GNBCUUPFunction> identifier)
            throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);

        final GNBCUUPFunctionKey key = identifier.firstKeyOf(GNBCUUPFunction.class);

        String gNBCUUPId = null;
        boolean isDataReady = false;

        List<nearRTRIC> nearRTRICList = InMemoryDataTree.getInstance().getNearRTRIC();

        for (int i = 0; i < nearRTRICList.size(); i++) {
            LOG.info("gnbCUUPList: " + (nearRTRICList.get(i)).getgNBCUUPFunction());
            List<com.wipro.www.websocket.models.GNBCUUPFunction> inList = (nearRTRICList.get(i)).getgNBCUUPFunction();

            for (int j = 0; j < inList.size(); j++) {
                // if((inList.get(j)).getIdGNBCUUPFunction() == key.getIdGNBCUUPFunction())
                if ((inList.get(j)).getIdGNBCUUPFunction().equalsIgnoreCase(key.getIdGNBCUUPFunction())) {
                    gNBCUUPId = (inList.get(j)).getAttributes().getgNBCUUPId();
                    isDataReady = true;
                    break;
                }
            }
            if (isDataReady == true) {
                break;
            }

        }
        LOG.info("isDataReady: " + isDataReady);
        LOG.info("Returning data..");

        if (isDataReady) {
            BigInteger bigInteger = new BigInteger(gNBCUUPId);
            LOG.info("BigInteger value:[{}]", bigInteger);
            final Attributes attributes = new AttributesBuilder().setGNBCUUPId(bigInteger).build();
            return new GNBCUUPFunctionBuilder().setIdGNBCUUPFunction(key.getIdGNBCUUPFunction())
                    .setAttributes(attributes).build();
        } else {
            return null;
        }

    }

    @Override
    public List<GNBCUUPFunction> readAll() throws ReadFailedException {

        List<nearRTRIC> listNearRTRIC = InMemoryDataTree.getInstance().getNearRTRIC();
        List<GNBCUUPFunction> outList = new ArrayList<GNBCUUPFunction>();
        String idNearRTRIC = null;
        String idGNBCUUPFunction = null;

        try {
            for (int i = 0; i < listNearRTRIC.size(); i++) {
                idNearRTRIC = (listNearRTRIC.get(i)).getIdNearRTRIC();
                List<com.wipro.www.websocket.models.GNBCUUPFunction> inputList =
                        (listNearRTRIC.get(i)).getgNBCUUPFunction();
                if (!(Objects.isNull(inputList)) && !(inputList.isEmpty())) {
                    idGNBCUUPFunction = (inputList.get(i)).getIdGNBCUUPFunction();

                    LOG.info("GNBCUUPFunction ID:[{}]", idGNBCUUPFunction);

                    outList.add(readSpecific(InstanceIdentifier.create(RanNetwork.class)
                            .child(NearRTRIC.class, new NearRTRICKey(idNearRTRIC))
                            .child(GNBCUUPFunction.class, new GNBCUUPFunctionKey(idGNBCUUPFunction))));
                }
            }
            return outList;
        } catch (Exception e) {
            LOG.info("Exception:[{}]", e);
            return null;
        }

        // return Collections.singletonList(
        // readSpecific(InstanceIdentifier.create(RanNetwork.class).child(NearRTRIC.class, new
        // NearRTRICKey("RTRIC2")).child(GNBCUUPFunction.class, new GNBCUUPFunctionKey("GNBDUFUNUP1"))));

    }
}
