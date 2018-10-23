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

package org.onap.ransim.read;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.onap.ransim.CrudService;
import io.fd.honeycomb.translate.read.ReadContext;
import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.spi.read.ReaderCustomizer;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.Lte;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.LteBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.cell.config.lte.LteRanBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.FapServiceBuilder;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.CellConfig;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.wireless.yang.radio.access.rev180920.radio.access.fap.service.CellConfigBuilder;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * Reader for {@link CellConfig} list node from our YANG model.
 */
public final class CellConfigCustomizer implements
    ReaderCustomizer<CellConfig, CellConfigBuilder> {

    private final CrudService<CellConfig> crudService;
    private static final Logger LOG = LoggerFactory.getLogger(CellConfigCustomizer.class);

    public CellConfigCustomizer(final CrudService<CellConfig> crudService) {
        this.crudService = crudService;
        LOG.info("RANSIM CellConfigCustomizer ctor called");
    }

    @Override
        public void merge(Builder<? extends DataObject> parentBuilder, CellConfig readValue) {
        LOG.info("RANSIM CellConfigCustomizer merge called");
        // merge children data to parent builder
        // used by infrastructure to merge data loaded in separated customizers
        ((FapServiceBuilder) parentBuilder).setCellConfig(readValue);
    }

    @Nonnull
    @Override
    public CellConfigBuilder getBuilder(@Nonnull final InstanceIdentifier<CellConfig> id) {
        LOG.info("RANSIM CellConfigCustomizer getBuilder called");
        // return new builder for this data node
        return new CellConfigBuilder();
    }

    @Override
    public void readCurrentAttributes(@Nonnull final InstanceIdentifier<CellConfig> id,
                                      @Nonnull final CellConfigBuilder builder,
                                      @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM CellConfigCustomizer readCurrentAttributes called");
        // this stage is used after reading all ids by getAllIds,to read specific details about data

        // perform read of details of data specified by key of CellConfig in id
        final CellConfig data = crudService.readSpecific(id, ctx);

        // and sets it to builder
        builder.setLte(new LteBuilder().build());
    }


}
