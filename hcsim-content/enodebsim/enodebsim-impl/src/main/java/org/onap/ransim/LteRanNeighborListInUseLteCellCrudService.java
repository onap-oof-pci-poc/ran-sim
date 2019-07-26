/*
 * Copyright (C) 2018 Wipro Limited.
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

package org.onap.ransim;

import io.fd.honeycomb.translate.read.ReadContext;
import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteContext;
import io.fd.honeycomb.translate.write.WriteFailedException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCell;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCellBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple example of class handling Crud operations for plugin.
 * <p/>
 * No real handling, serves just as an illustration.
 *
 * TODO update javadoc
 */
final class LteRanNeighborListInUseLteCellCrudService implements CrudService<LteRanNeighborListInUseLteCell> {

    private static final Logger LOG = LoggerFactory.getLogger(LteRanNeighborListInUseLteCellCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<LteRanNeighborListInUseLteCell> identifier
            , @Nonnull final LteRanNeighborListInUseLteCell data
            , @Nonnull final WriteContext writeContext)
                    throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCrudService writeData called");
        try {
            if (data != null) {
                LOG.info("Writing path[{}] / data [{}]", identifier, data);
                if(!ConfigJsonHandler.getConfigJsonHandler(null).isConfigTopoInitialized)
                    return;
                int stIndex = identifier.toString().indexOf("[_alias=");
                String subStr = identifier.toString().substring(
                        stIndex + "[_alias=".length());
                String cid = subStr.substring(0, subStr.indexOf("]"));
                LOG.info("cid {} ", cid);
                ServerCidNbrListHandler sclh = new ServerCidNbrListHandler();
                sclh.CheckandAdd(cid, data);
                // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
                // relationships such as to which parent these data are related to

                // Performs any logic needed for persisting such data
                LOG.info("Writing path[{}] / data [{}]", identifier, data);
            } else {
                throw new WriteFailedException.CreateFailedException(identifier, data,
                        new NullPointerException("Provided data are null"));
            }
        } catch(Exception e) {
            LOG.error("Error in writeData {}", e.toString());
        }

    }

    @Override
    public void deleteData(@Nonnull final InstanceIdentifier<LteRanNeighborListInUseLteCell> identifier, @Nonnull final LteRanNeighborListInUseLteCell data)
            throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCrudService deleteData called");
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
    public void updateData(@Nonnull final InstanceIdentifier<LteRanNeighborListInUseLteCell> identifier, @Nonnull final LteRanNeighborListInUseLteCell dataOld,
            @Nonnull final LteRanNeighborListInUseLteCell dataNew) throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCrudService updateData called");
        if (dataOld != null && dataNew != null) {
            LOG.info("Update path[{}] from [{}] to [{}]", identifier, dataOld,
                    dataNew);
            if(!ConfigJsonHandler.getConfigJsonHandler(null).isConfigTopoInitialized)
                return;
            int stIndex = identifier.toString().indexOf("[_alias=");
            String subStr = identifier.toString().substring(
                    stIndex + "[_alias=".length());
            String cid = subStr.substring(0, subStr.indexOf("]"));
            LOG.info("cid {} ", cid);
            ServerCidNbrListHandler sclh = new ServerCidNbrListHandler();
            sclh.CheckandAdd(cid, dataNew);
            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
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
    public LteRanNeighborListInUseLteCell readSpecific(@Nonnull final InstanceIdentifier<LteRanNeighborListInUseLteCell> identifier
            , @Nonnull final ReadContext ctx) throws ReadFailedException {
        LteRanNeighborListInUseLteCell fs = new LteRanNeighborListInUseLteCellBuilder()
        .setCid("a")
        .setPhyCellId(BigInteger.ONE)
        .setPlmnid("a")
        .build();
        return fs;
    }

    @Override
    public List<LteRanNeighborListInUseLteCell> readAll() throws ReadFailedException {
        List<LteRanNeighborListInUseLteCell> fsList = new ArrayList<LteRanNeighborListInUseLteCell>();
        // read all data under parent node,in this case {@link ModuleState}
        return fsList;
    }
}
