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

package org.onap.ransim.read;

import io.fd.honeycomb.translate.read.ReadContext;
import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.spi.read.Initialized;
import io.fd.honeycomb.translate.spi.read.InitializingListReaderCustomizer;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.onap.ransim.CrudService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanNeighborListInUseBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCell;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCellBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCellKey;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reader for {@link LteRanNeighborListInUseLteCell} list node from our YANG
 * model.
 */
public final class LteRanNeighborListInUseLteCellCustomizer
        implements
        InitializingListReaderCustomizer<LteRanNeighborListInUseLteCell, LteRanNeighborListInUseLteCellKey, LteRanNeighborListInUseLteCellBuilder> {

    private final CrudService<LteRanNeighborListInUseLteCell> crudService;
    private static final Logger LOG = LoggerFactory
            .getLogger(LteRanNeighborListInUseLteCellCustomizer.class);

    public LteRanNeighborListInUseLteCellCustomizer(
            final CrudService<LteRanNeighborListInUseLteCell> crudService) {
        this.crudService = crudService;
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCustomizer ctor called");
    }

    @Nonnull
    @Override
    public LteRanNeighborListInUseLteCellBuilder getBuilder(
            @Nonnull final InstanceIdentifier<LteRanNeighborListInUseLteCell> id) {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCustomizer getBuilder called");
        // return new builder for this data node
        return new LteRanNeighborListInUseLteCellBuilder();
    }

    @Override
    public void readCurrentAttributes(
            @Nonnull final InstanceIdentifier<LteRanNeighborListInUseLteCell> id,
            @Nonnull final LteRanNeighborListInUseLteCellBuilder builder,
            @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCustomizer readCurrentAttributes called");
        // this stage is used after reading all ids by getAllIds,to read
        // specific details about data

        // perform read of details of data specified by key of
        // LteRanNeighborListInUseLteCell in id
        final LteRanNeighborListInUseLteCell data = crudService.readSpecific(
                id, ctx);

        // and sets it to builder
        builder.setPhyCellId(data.getPhyCellId());
        builder.setCid(data.getCid());
        builder.setPlmnid(data.getPlmnid());
    }

    @Override
    public List<LteRanNeighborListInUseLteCellKey> getAllIds(
            InstanceIdentifier<LteRanNeighborListInUseLteCell> arg0,
            ReadContext arg1) throws ReadFailedException {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCustomizer getAllIds called");
        // TODO Auto-generated method stub
        return crudService
                .readAll()
                .stream()
                .map(a -> new LteRanNeighborListInUseLteCellKey(a.getCid(), a
                        .getPlmnid())).collect(Collectors.toList());

    }

    @Override
    public void merge(Builder<? extends DataObject> parentBuilder,
            List<LteRanNeighborListInUseLteCell> arg1) {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCustomizer merge called");
        // TODO Auto-generated method stub
        ((LteRanNeighborListInUseBuilder) parentBuilder)
                .setLteRanNeighborListInUseLteCell(arg1);

    }

    @Override
    public Initialized<? extends DataObject> init(
            @Nonnull final InstanceIdentifier<LteRanNeighborListInUseLteCell> id,
            @Nonnull final LteRanNeighborListInUseLteCell readValue,
            @Nonnull final ReadContext arg2) {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellCustomizer init called");
        // TODO Auto-generated method stub
        return Initialized.create(id, readValue);
    }

}
