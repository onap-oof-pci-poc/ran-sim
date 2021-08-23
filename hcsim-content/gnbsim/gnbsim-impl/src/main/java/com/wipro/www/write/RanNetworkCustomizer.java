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

package com.wipro.www.write;

import com.wipro.www.CrudService;

import io.fd.honeycomb.translate.spi.write.WriterCustomizer;
import io.fd.honeycomb.translate.write.WriteContext;
import io.fd.honeycomb.translate.write.WriteFailedException;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetwork;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class RanNetworkCustomizer implements WriterCustomizer<RanNetwork> {

    private final CrudService<RanNetwork> crudService;

    public RanNetworkCustomizer(@Nonnull final CrudService<RanNetwork> crudService) {
        this.crudService = crudService;
    }

    @Override
    public void writeCurrentAttributes(@Nonnull InstanceIdentifier<RanNetwork> instanceIdentifier,
            @Nonnull RanNetwork rannetwork, @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.writeData(instanceIdentifier, rannetwork);
    }

    @Override
    public void deleteCurrentAttributes(@Nonnull InstanceIdentifier<RanNetwork> instanceIdentifier,
            @Nonnull RanNetwork rannetwork, @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.deleteData(instanceIdentifier, rannetwork);
    }

    @Override
    public void updateCurrentAttributes(@Nonnull InstanceIdentifier<RanNetwork> id, @Nonnull RanNetwork dataBefore,
            @Nonnull RanNetwork dataAfter, @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.updateData(id, dataBefore, dataAfter);
    }
}
