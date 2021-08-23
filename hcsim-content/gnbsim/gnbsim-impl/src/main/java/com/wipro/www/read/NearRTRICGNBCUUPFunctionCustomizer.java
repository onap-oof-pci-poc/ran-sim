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

package com.wipro.www.read;

import com.wipro.www.CrudService;

import io.fd.honeycomb.translate.read.ReadContext;
import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.spi.read.Initialized;
import io.fd.honeycomb.translate.spi.read.InitializingListReaderCustomizer;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetworkBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunctionBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunctionKey;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NearRTRICGNBCUUPFunctionCustomizer
        implements InitializingListReaderCustomizer<GNBCUUPFunction, GNBCUUPFunctionKey, GNBCUUPFunctionBuilder> {

    private final CrudService<GNBCUUPFunction> crudService;

    private static final Logger LOG = LoggerFactory.getLogger(NearRTRICGNBCUUPFunctionCustomizer.class);

    public NearRTRICGNBCUUPFunctionCustomizer(final CrudService<GNBCUUPFunction> crudService) {
        this.crudService = crudService;
    }

    @Nonnull
    @Override
    public List<GNBCUUPFunctionKey> getAllIds(@Nonnull final InstanceIdentifier<GNBCUUPFunction> id,
            @Nonnull final ReadContext context) throws ReadFailedException {
        // perform read operation and extract keys from data
        try {
            return crudService.readAll().stream().map(a -> new GNBCUUPFunctionKey(a.getIdGNBCUUPFunction()))
                    .collect(Collectors.toList());
        } catch (Exception E) {
            LOG.info("Exception in getAllIds:[{}]", E);
            return null;
        }
    }

    @Override
    public void merge(@Nonnull final Builder<? extends DataObject> builder,
            @Nonnull final List<GNBCUUPFunction> readData) {
        // merge children data to parent builder
        // used by infrastructure to merge data loaded in separated customizers
        ((NearRTRICBuilder) builder).setGNBCUUPFunction(readData);
    }

    @Nonnull
    @Override
    public GNBCUUPFunctionBuilder getBuilder(@Nonnull final InstanceIdentifier<GNBCUUPFunction> id) {
        // return new builder for this data node
        return new GNBCUUPFunctionBuilder();
    }

    @Override
    public void readCurrentAttributes(@Nonnull final InstanceIdentifier<GNBCUUPFunction> id,
            @Nonnull final GNBCUUPFunctionBuilder builder, @Nonnull final ReadContext ctx) throws ReadFailedException {
        // this stage is used after reading all ids by getAllIds,to read specific details about data

        // perform read of details of data specified by key of Element in id
        final GNBCUUPFunction data = crudService.readSpecific(id);

        // and sets it to builder
        builder.setIdGNBCUUPFunction(data.getIdGNBCUUPFunction());
        // builder.setKey(data.getKey());
        builder.setAttributes(data.getAttributes());
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
    public Initialized<? extends DataObject> init(@Nonnull final InstanceIdentifier<GNBCUUPFunction> id,
            @Nonnull final GNBCUUPFunction readValue, @Nonnull final ReadContext ctx) {
        return Initialized.create(id, readValue);
    }
}
