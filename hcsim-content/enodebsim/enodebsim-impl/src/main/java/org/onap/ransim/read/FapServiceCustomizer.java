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

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.onap.ransim.CrudService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.RadioAccessBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapServiceBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapServiceKey;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fd.honeycomb.translate.read.ReadContext;
import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.spi.read.Initialized;
import io.fd.honeycomb.translate.spi.read.InitializingListReaderCustomizer;

/**
 * Reader for {@link FapService} list node from our YANG model.
 */
public final class FapServiceCustomizer implements
InitializingListReaderCustomizer<FapService, FapServiceKey, FapServiceBuilder> {

    private final CrudService<FapService> crudService;
    private static final Logger LOG = LoggerFactory.getLogger(FapServiceCustomizer.class);

    public FapServiceCustomizer(final CrudService<FapService> crudService) {
        this.crudService = crudService;
        LOG.info("RANSIM FapServiceCustomizer ctor called");
    }

    @Nonnull
    @Override
    public List<FapServiceKey> getAllIds(@Nonnull final InstanceIdentifier<FapService> id, @Nonnull final ReadContext context)
            throws ReadFailedException {
        LOG.info("RANSIM FapServiceCustomizer getAllIds called");
        // perform read operation and extract keys from data
        return crudService.readAll()
                .stream()
                .map(a -> new FapServiceKey(a.getAlias()))
                .collect(Collectors.toList());
    }

    @Override
    public void merge(@Nonnull final Builder<? extends DataObject> builder, @Nonnull final List<FapService> readData) {
        LOG.info("RANSIM FapServiceCustomizer merge called");
        // merge children data to parent builder
        // used by infrastructure to merge data loaded in separated customizers
        ((RadioAccessBuilder) builder).setFapService(readData);
    }

    @Nonnull
    @Override
    public FapServiceBuilder getBuilder(@Nonnull final InstanceIdentifier<FapService> id) {
        LOG.info("RANSIM FapServiceCustomizer getBuilder called");
        // return new builder for this data node
        return new FapServiceBuilder();
    }

    @Override
    public void readCurrentAttributes(@Nonnull final InstanceIdentifier<FapService> id,
            @Nonnull final FapServiceBuilder builder,
            @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM FapServiceCustomizer readCurrentAttributes called");
        // this stage is used after reading all ids by getAllIds,to read specific details about data

        // perform read of details of data specified by key of FapService in id
        final FapService data = crudService.readSpecific(id, ctx);

        // and sets it to builder
        builder.setAlias(data.getAlias());
    }
    /**
     *
     * Initialize configuration data based on operational data.
     * <p/>
     * Very useful when a plugin is initiated but the underlying layer already contains some operation state.
     * Deriving the configuration from existing operational state enables reconciliation in case when
     * Honeycomb's persistence is not available to do the work for us.
     */
    @Nonnull
    @Override
    public Initialized<? extends DataObject> init(@Nonnull final InstanceIdentifier<FapService> id,
            @Nonnull final FapService readValue,
            @Nonnull final ReadContext ctx) {
        LOG.info("RANSIM FapServiceCustomizer init called");
        return Initialized.create(id, readValue);
    }
}
