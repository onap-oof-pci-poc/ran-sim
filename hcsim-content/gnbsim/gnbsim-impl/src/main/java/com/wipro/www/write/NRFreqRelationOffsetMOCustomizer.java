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
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrfreqrelationgroup.OffsetMO;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import javax.annotation.Nonnull;

public class NRFreqRelationOffsetMOCustomizer implements WriterCustomizer<OffsetMO> {

    private final CrudService<OffsetMO> crudService;

    public  NRFreqRelationOffsetMOCustomizer(@Nonnull final CrudService<OffsetMO> crudService){
        this.crudService = crudService;
    }

    @Override
    public void writeCurrentAttributes(@Nonnull InstanceIdentifier<OffsetMO> instanceIdentifier, @Nonnull OffsetMO offsetmo, @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.writeData(instanceIdentifier, offsetmo);
    }

    @Override
    public void deleteCurrentAttributes(@Nonnull InstanceIdentifier<OffsetMO> instanceIdentifier, @Nonnull OffsetMO offsetmo, @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.deleteData(instanceIdentifier, offsetmo);
    }

    @Override
    public void updateCurrentAttributes(@Nonnull InstanceIdentifier<OffsetMO> id, @Nonnull OffsetMO dataBefore, @Nonnull OffsetMO dataAfter, @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.updateData(id, dataBefore, dataAfter);
    }
}
