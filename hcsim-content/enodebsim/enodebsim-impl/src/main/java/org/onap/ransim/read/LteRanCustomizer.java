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
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRan;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.LteBuilder;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * Reader for {@link LteRan} list node from our YANG model.
 */
public final class LteRanCustomizer implements
    ReaderCustomizer<LteRan, LteRanBuilder> {

    private final CrudService<LteRan> crudService;
    private static final Logger LOG = LoggerFactory.getLogger(LteRanCustomizer.class);

    public LteRanCustomizer(final CrudService<LteRan> crudService) {
        this.crudService = crudService;
        LOG.info("RANSIM LteRanCustomizer ctor called");
    }

    @Override
        public void merge(Builder<? extends DataObject> parentBuilder, LteRan readValue) {
        LOG.info("RANSIM LteRanCustomizer merge called");
        // merge children data to parent builder
        // used by infrastructure to merge data loaded in separated customizers
        ((LteBuilder) parentBuilder).setLteRan(readValue);
    }

    @Nonnull
    @Override
    public LteRanBuilder getBuilder(@Nonnull final InstanceIdentifier<LteRan> id) {
        LOG.info("RANSIM LteRanCustomizer getBuilder called");
        // return new builder for this data node
        return new LteRanBuilder();
    }

    @Override
    public void readCurrentAttributes(@Nonnull final InstanceIdentifier<LteRan> id,
                                      @Nonnull final LteRanBuilder builder,
                                      @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM LteRanCustomizer readCurrentAttributes called");
        // this stage is used after reading all ids by getAllIds,to read specific details about data

        // perform read of details of data specified by key of LteRan in id
        final LteRan data = crudService.readSpecific(id, ctx);

    }


}
