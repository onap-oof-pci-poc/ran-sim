/*
 * ============LICENSE_START=======================================================
 * RAN Simulator - HoneyComb
 * ================================================================================
 * Copyright (C) 2018 Wipro Limited.
 * ================================================================================
 *
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
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.lte.LteRan;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.lte.lte.ran.LteRanNeighborListInUse;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.lte.lte.ran.LteRanNeighborListInUseBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCell;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCellBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCellKey;

/**
 * Simple example of class handling Crud operations for plugin.
 * <p/>
 * No real handling, serves just as an illustration.
 *
 * TODO update javadoc
 */
final class LteRanNeighborListInUseCrudService implements CrudService<LteRanNeighborListInUse> {

    private static final Logger LOG = LoggerFactory.getLogger(LteRanNeighborListInUseCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<LteRanNeighborListInUse> identifier
            , @Nonnull final LteRanNeighborListInUse data
            , @Nonnull final WriteContext writeContext)
            throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService writeData called");
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
    public void deleteData(@Nonnull final InstanceIdentifier<LteRanNeighborListInUse> identifier, @Nonnull final LteRanNeighborListInUse data)
            throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService deleteData called");
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
    public void updateData(@Nonnull final InstanceIdentifier<LteRanNeighborListInUse> identifier, @Nonnull final LteRanNeighborListInUse dataOld,
                           @Nonnull final LteRanNeighborListInUse dataNew) throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService updateData called");
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
    public LteRanNeighborListInUse readSpecific(@Nonnull final InstanceIdentifier<LteRanNeighborListInUse> identifier
            , @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService readSpecific called");
        int mlce = 0;
        int lcnoe = 0;
        List<LteRanNeighborListInUseLteCell> ngbrList = new ArrayList<LteRanNeighborListInUseLteCell>();
        // load data by this key
        // *Key class will always contain key of entity, in this case long value
        if(ctx != null) {
            JsonObject lrnllObj = (JsonObject) ctx.getModificationCache().get("lte-ran-neighbor-list-in-use");
            if(lrnllObj != null) {
            mlce = lrnllObj.getInt("max-lte-cell-entries");
            lcnoe = lrnllObj.getInt("lte-cell-number-of-entries");
            JsonArray jsArr = lrnllObj.getJsonArray("lte-ran-neighbor-list-in-use-lte-cell");
            if(jsArr != null) {
            for (int i = 0; i < jsArr.size(); i++) {
                JsonObject nlObj = jsArr.getJsonObject(i);
                LteRanNeighborListInUseLteCell ngbrEntry = new LteRanNeighborListInUseLteCellBuilder()
                        .setPlmnid(nlObj.getString("plmnid"))
                        .setCid(nlObj.getString("cid"))
                        .setPhyCellId(BigInteger.valueOf(nlObj.getInt("phy-cell-id")))
                        .build();
                ngbrList.add(ngbrEntry );
            }
            }
            }
        }


        return new LteRanNeighborListInUseBuilder()
                .setLteCellNumberOfEntries(BigInteger.valueOf(lcnoe))
                .setMaxLteCellEntries(BigInteger.valueOf(mlce))
                .setLteRanNeighborListInUseLteCell(ngbrList)
                .build();
    }

    @Override
    public List<LteRanNeighborListInUse> readAll() throws ReadFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService readAll called");
        // read all data under parent node,in this case {@link ModuleState}
        return Collections.singletonList(
                readSpecific(InstanceIdentifier.create(LteRan.class).child(LteRanNeighborListInUse.class), null));
    }
}
