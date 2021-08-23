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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetwork;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRIC;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.AttributesBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunctionBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NearRTRICCrudService implements CrudService<NearRTRIC> {

    private static final Logger LOG = LoggerFactory.getLogger(NearRTRICCrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<NearRTRIC> identifier, @Nonnull NearRTRIC data)
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
    public void deleteData(@Nonnull InstanceIdentifier<NearRTRIC> identifier, @Nonnull NearRTRIC data)
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
    public void updateData(@Nonnull InstanceIdentifier<NearRTRIC> identifier, @Nonnull NearRTRIC dataOld,
            @Nonnull NearRTRIC dataNew) throws WriteFailedException {
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
    public NearRTRIC readSpecific(@Nonnull InstanceIdentifier<NearRTRIC> identifier) throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);

        final NearRTRICKey key = identifier.firstKeyOf(NearRTRIC.class);

        // LOG.info("Location Name:[{}]", InMemoryDataTree.getInstance().getLocationName());
        // LOG.info("GNBID:[{}]", InMemoryDataTree.getInstance().getGNBId());
        // LOG.info("Long GNBID:[{}]", Long.parseLong(InMemoryDataTree.getInstance().getNearRTRICgNBId()));

        String locationName = null;
        String gNBId = null;
        List<nearRTRIC> nearRTRICList = InMemoryDataTree.getInstance().getNearRTRIC();
        boolean isDataReady = false;

        for (int i = 0; i < nearRTRICList.size(); i++) {
            LOG.info("getIDNearRTRIC from InMemory:[{}]", (nearRTRICList.get(i)).getIdNearRTRIC());
            LOG.info("getIDNearRTRIC from identifer:[{}]", key.getIdNearRTRIC());

            boolean a = ((nearRTRICList.get(i)).getIdNearRTRIC()).equalsIgnoreCase(key.getIdNearRTRIC());
            LOG.info("is equal: " + a);
            // if(((nearRTRICList.get(i)).getIdNearRTRIC()) == (key.getIdNearRTRIC()))
            if (((nearRTRICList.get(i)).getIdNearRTRIC()).equalsIgnoreCase(key.getIdNearRTRIC())) {
                LOG.info("if condition satisfied");
                LOG.info("inmem to string: " + nearRTRICList.get(i).toString());
                LOG.info("Inmem location: "
                        + InMemoryDataTree.getInstance().getNearRTRIC().get(0).getAttributes().getLocationName());
                LOG.info("Inmem gnbId: "
                        + InMemoryDataTree.getInstance().getNearRTRIC().get(0).getAttributes().getgNBId());
                LOG.info("Inmem gnbDUID: "
                        + InMemoryDataTree.getInstance().getNearRTRIC().get(0).getgNBDUFunction().toString());
                LOG.info("Inmem gnbcuup: " + InMemoryDataTree.getInstance().getNearRTRIC().get(0).getgNBCUUPFunction());

                locationName = (nearRTRICList.get(i)).getAttributes().getLocationName();
                gNBId = (nearRTRICList.get(i)).getAttributes().getgNBId();

                isDataReady = true;
                break;
            }

        }

        LOG.info("LocationName:[{}]", locationName);
        LOG.info("gNBID:[{}]", gNBId);

        if (isDataReady) {
            final Attributes attributes =
                    new AttributesBuilder().setLocationName(locationName).setGNBId(Long.parseLong(gNBId)).build();
            return new NearRTRICBuilder().setIdNearRTRIC(key.getIdNearRTRIC()).setAttributes(attributes).build();
        } else {
            return null;
        }
    }

    @Override
    public List<NearRTRIC> readAll() throws ReadFailedException {

        List<nearRTRIC> listNearRTRIC = InMemoryDataTree.getInstance().getNearRTRIC();
        List<NearRTRIC> outListNearRTRIC = new ArrayList<NearRTRIC>();
        String IdNearRTRIC;
        try {

            // if(!listNearRTRIC.isEmpty())
            if (!(Objects.isNull(listNearRTRIC)) && (!listNearRTRIC.isEmpty())) {
                for (int i = 0; i < listNearRTRIC.size(); i++) {
                    LOG.info("NearRTRIC Element:[{}]", listNearRTRIC.get(i));
                    IdNearRTRIC = (listNearRTRIC.get(i)).getIdNearRTRIC();

                    outListNearRTRIC.add(readSpecific(InstanceIdentifier.create(RanNetwork.class).child(NearRTRIC.class,
                            new NearRTRICKey((IdNearRTRIC)))));
                }
            }

            // return Collections.singletonList(
            // readSpecific(InstanceIdentifier.create(RanNetwork.class).child(NearRTRIC.class,
            // new NearRTRICKey(InMemoryDataTree.getInstance().getIdNearRTRIC()))));
            return outListNearRTRIC;
        } catch (Exception e) {
            LOG.info("Exception - [{}]", e);
            return null;
        }
    }
}
