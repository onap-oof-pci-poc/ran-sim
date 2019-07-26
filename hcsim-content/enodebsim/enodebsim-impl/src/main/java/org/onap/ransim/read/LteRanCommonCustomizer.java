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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.onap.ransim.CrudService;
import io.fd.honeycomb.translate.read.ReadContext;
import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.spi.read.ReaderCustomizer;

import javax.annotation.Nonnull;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRanBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanCommon;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanCommonBuilder;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * Reader for {@link LteRanCommon} list node from our YANG model.
 */
public final class LteRanCommonCustomizer implements
    ReaderCustomizer<LteRanCommon, LteRanCommonBuilder> {

    private final CrudService<LteRanCommon> crudService;
    private static final Logger LOG = LoggerFactory.getLogger(LteRanCommonCustomizer.class);

    public LteRanCommonCustomizer(final CrudService<LteRanCommon> crudService) {
        this.crudService = crudService;
        LOG.info("RANSIM LteRanCommonCustomizer ctor called");
    }

    @Override
        public void merge(Builder<? extends DataObject> parentBuilder, LteRanCommon readValue) {
        LOG.info("RANSIM LteRanCommonCustomizer merge called");
        // merge children data to parent builder
        // used by infrastructure to merge data loaded in separated customizers
        ((LteRanBuilder) parentBuilder).setLteRanCommon(readValue);
    }

    @Nonnull
    @Override
    public LteRanCommonBuilder getBuilder(@Nonnull final InstanceIdentifier<LteRanCommon> id) {
        LOG.info("RANSIM LteRanCommonCustomizer getBuilder called");
        // return new builder for this data node
        return new LteRanCommonBuilder();
    }

    @Override
    public void readCurrentAttributes(@Nonnull final InstanceIdentifier<LteRanCommon> id,
                                      @Nonnull final LteRanCommonBuilder builder,
                                      @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM LteRanCommonCustomizer readCurrentAttributes called");
        // this stage is used after reading all ids by getAllIds,to read specific details about data

        // perform read of details of data specified by key of LteRanCommon in id
        final LteRanCommon data = crudService.readSpecific(id, ctx);

        // and sets it to builder
        builder.setCellIdentity(data.getCellIdentity());
    }


}
