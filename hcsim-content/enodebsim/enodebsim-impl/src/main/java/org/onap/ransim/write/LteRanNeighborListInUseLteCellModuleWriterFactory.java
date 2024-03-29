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

package org.onap.ransim.write;

import static org.onap.ransim.ModuleConfiguration.LRNLIULC_SERVICE_NAME;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.onap.ransim.CrudService;
import io.fd.honeycomb.translate.impl.write.GenericWriter;
import io.fd.honeycomb.translate.write.WriterFactory;
import io.fd.honeycomb.translate.write.registry.ModifiableWriterRegistryBuilder;
import javax.annotation.Nonnull;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.RadioAccess;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.CellConfig;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.Lte;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRan;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanNeighborListInUse;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCell;

/**
 * Factory producing writers for enodebsim plugin's data.
 */
public final class LteRanNeighborListInUseLteCellModuleWriterFactory implements WriterFactory {

    private static final Logger LOG = LoggerFactory.getLogger(LteRanNeighborListInUseLteCellModuleWriterFactory.class);
    public static final InstanceIdentifier<RadioAccess> FSROOT_STATE_CONTAINER_ID =
            InstanceIdentifier.create(RadioAccess.class);
    /**
     * Injected crud service to be passed to customizers instantiated in this factory.
     */
    @Inject
    @Named(LRNLIULC_SERVICE_NAME)
    private CrudService<LteRanNeighborListInUseLteCell> crudService;

    @Override
    public void init(@Nonnull final ModifiableWriterRegistryBuilder registry) {
        LOG.info("RANSIM LteRanNeighborListInUseLteCellModuleWriterFactory init called");

        //adds writer for child node
        //no need to add writers for empty nodes
        registry.add(new GenericWriter<>(FSROOT_STATE_CONTAINER_ID.child(FapService.class).child(CellConfig.class).child(Lte.class).child(LteRan.class).child(LteRanNeighborListInUse.class).child(LteRanNeighborListInUseLteCell.class), new LteRanNeighborListInUseLteCellCustomizer(crudService)));
    }
}
