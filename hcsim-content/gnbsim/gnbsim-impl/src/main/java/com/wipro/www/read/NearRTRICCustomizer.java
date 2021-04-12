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
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRIC;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICBuilder;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class NearRTRICCustomizer implements InitializingListReaderCustomizer<NearRTRIC, NearRTRICKey, NearRTRICBuilder> {

    private final CrudService<NearRTRIC> crudService;

    public NearRTRICCustomizer(final CrudService<NearRTRIC> crudService) {
        this.crudService = crudService;
    }

    @Nonnull
    @Override
    public Initialized<? extends DataObject> init(@Nonnull InstanceIdentifier<NearRTRIC> instanceIdentifier, @Nonnull NearRTRIC nearrtric, @Nonnull ReadContext readContext) {
        return Initialized.create(instanceIdentifier, nearrtric);
    }

    @Nonnull
    @Override
    public List<NearRTRICKey> getAllIds(@Nonnull InstanceIdentifier<NearRTRIC> instanceIdentifier, @Nonnull ReadContext readContext) throws ReadFailedException {
        //return crudService.readAll();
                //.stream()
                //.map(a -> new NearRTRICKey(a.getAlias()))
                //.collect(Collectors.toList());
        return null;
    }

    @Override
    public void merge(@Nonnull Builder<? extends DataObject> builder, @Nonnull List<NearRTRIC> list) {
        //((RadioAccessBuilder) builder).setCells(list);
    }

    @Nonnull
    @Override
    public NearRTRICBuilder getBuilder(@Nonnull InstanceIdentifier<NearRTRIC> instanceIdentifier) {
        return new NearRTRICBuilder();
    }

    @Override
    public void readCurrentAttributes(@Nonnull InstanceIdentifier<NearRTRIC> instanceIdentifier, @Nonnull NearRTRICBuilder nearrtricBuilder, @Nonnull ReadContext readContext) throws ReadFailedException {
        final NearRTRIC nearrtric = crudService.readSpecific(instanceIdentifier);

        //nearrtricBuilder.setSlices(cells.getSlices());
        //nearrtricBuilder.setAlias(cells.getAlias());
        //nearrtricBuilder.setLocationName("GERMANY");
    }
}
