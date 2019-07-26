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
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.onap.ransim.websocket.model.Neighbor;
import org.onap.ransim.websocket.model.Topology;
import org.onap.ransim.websocket.model.UpdateCell;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRan;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanNeighborListInUse;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanNeighborListInUseBuilder;
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
final class LteRanNeighborListInUseCrudService implements
CrudService<LteRanNeighborListInUse> {

    private static final Logger LOG = LoggerFactory
            .getLogger(LteRanNeighborListInUseCrudService.class);

    @Override
    public void writeData(
            @Nonnull final InstanceIdentifier<LteRanNeighborListInUse> identifier,
            @Nonnull final LteRanNeighborListInUse data,
            @Nonnull final WriteContext writeContext)
                    throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService writeData called");
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

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be
            // used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            UpdateCell updateCell = new UpdateCell();
            Topology topology = new Topology();
            topology.setCellId(cid);

            int ngbrSize = data.getLteCellNumberOfEntries().intValue();
            List<Neighbor> updatedNeighborList = new ArrayList<>();
            List<LteRanNeighborListInUseLteCell> ngbrList = data.getLteRanNeighborListInUseLteCell();
            for (LteRanNeighborListInUseLteCell neighbr : ngbrList) {
                LOG.info("LteRanNeighborListInUseLteCell  {} ", neighbr);
                Neighbor updatedNbr = new Neighbor();
                updatedNbr.setNodeId(neighbr.getCid());
                if(neighbr.getPhyCellId() != null)
                    updatedNbr
                    .setPhysicalCellId(neighbr.getPhyCellId().longValue());
                updatedNbr.setPlmnId(neighbr.getPlmnid());
                updatedNbr.setPnfName(neighbr.getPnfName());
                if(neighbr.getBlacklisted() != null)
                    updatedNbr.setBlacklisted(Boolean.parseBoolean(neighbr.getBlacklisted()));
                updatedNeighborList.add(updatedNbr);
            }
            topology.setNeighborList(updatedNeighborList);
            updateCell.setOneCell(topology);

            LOG.info("Sending to Ransim Ctrlr.");
            ConfigJsonHandler.getConfigJsonHandler(null).handleUpdateTopology(
                    updateCell);

        } else {
            throw new WriteFailedException.CreateFailedException(identifier,
                    data, new NullPointerException("Provided data are null"));
        }
        } catch(Exception e) {
            LOG.error("Error in writeData {}", e.toString());
        }
    }

    @Override
    public void deleteData(
            @Nonnull final InstanceIdentifier<LteRanNeighborListInUse> identifier,
            @Nonnull final LteRanNeighborListInUse data)
                    throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService deleteData called");
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be
            // used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Removing path[{}] / data [{}]", identifier, data);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void updateData(
            @Nonnull final InstanceIdentifier<LteRanNeighborListInUse> identifier,
            @Nonnull final LteRanNeighborListInUse dataOld,
            @Nonnull final LteRanNeighborListInUse dataNew)
                    throws WriteFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService updateData called");
        if (dataOld != null && dataNew != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be
            // used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Update path[{}] from [{}] to [{}]", identifier, dataOld,
                    dataNew);
            if(!ConfigJsonHandler.getConfigJsonHandler(null).isConfigTopoInitialized)
                return;
            int stIndex = identifier.toString().indexOf("[_alias=");
            String subStr = identifier.toString().substring(
                    stIndex + "[_alias=".length());
            String cid = subStr.substring(0, subStr.indexOf("]"));

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be
            // used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            UpdateCell updateCell = new UpdateCell();
            Topology topology = new Topology();
            topology.setCellId(cid);

            int ngbrSize = dataNew.getLteCellNumberOfEntries().intValue();
            List<Neighbor> updatedNeighborList = new ArrayList<>();
            List<LteRanNeighborListInUseLteCell> ngbrList = dataNew.getLteRanNeighborListInUseLteCell();
            for (LteRanNeighborListInUseLteCell neighbr : ngbrList) {
                Neighbor updatedNbr = new Neighbor();
                updatedNbr.setNodeId(neighbr.getCid());
                if(neighbr.getPhyCellId() != null)
                    updatedNbr
                    .setPhysicalCellId(neighbr.getPhyCellId().longValue());
                updatedNbr.setPlmnId(neighbr.getPlmnid());
                updatedNbr.setPnfName(neighbr.getPnfName());
                if(neighbr.getBlacklisted() != null)
                    updatedNbr.setBlacklisted(Boolean.parseBoolean(neighbr.getBlacklisted()));
                updatedNeighborList.add(updatedNbr);
            }
            topology.setNeighborList(updatedNeighborList);
            updateCell.setOneCell(topology);

            LOG.info("Sending to Ransim Ctrlr.");
            ConfigJsonHandler.getConfigJsonHandler(null).handleUpdateTopology(
                    updateCell);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public LteRanNeighborListInUse readSpecific(
            @Nonnull final InstanceIdentifier<LteRanNeighborListInUse> identifier,
            @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService readSpecific called");
        List<LteRanNeighborListInUseLteCell> ngbrList = new ArrayList<LteRanNeighborListInUseLteCell>();
        // load data by this key
        // *Key class will always contain key of entity, in this case long value
        if (ctx != null) {
            JsonObject lrnllObj = (JsonObject) ctx.getModificationCache().get(
                    "lte-ran-neighbor-list-in-use");
            if (lrnllObj != null) {
                JsonArray jsArr = lrnllObj
                        .getJsonArray("lte-ran-neighbor-list-in-use-lte-cell");
                if (jsArr != null) {
                    for (int i = 0; i < jsArr.size(); i++) {
                        JsonObject nlObj = jsArr.getJsonObject(i);
                        LteRanNeighborListInUseLteCell ngbrEntry = new LteRanNeighborListInUseLteCellBuilder()
                        .setPlmnid(nlObj.getString("plmnid"))
                        .setCid(nlObj.getString("cid"))
                        .setPhyCellId(
                                BigInteger.valueOf(nlObj
                                        .getInt("phy-cell-id")))
                                        .setBlacklisted(""+nlObj.getBoolean("blacklisted"))
                                        .build();
                        ngbrList.add(ngbrEntry);
                    }
                }
            }
        }

        return new LteRanNeighborListInUseBuilder()
        .setLteRanNeighborListInUseLteCell(ngbrList).build();
    }

    @Override
    public List<LteRanNeighborListInUse> readAll() throws ReadFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseCrudService readAll called");
        // read all data under parent node,in this case {@link ModuleState}
        return Collections.singletonList(readSpecific(InstanceIdentifier
                .create(LteRan.class).child(LteRanNeighborListInUse.class),
                null));
    }
}
