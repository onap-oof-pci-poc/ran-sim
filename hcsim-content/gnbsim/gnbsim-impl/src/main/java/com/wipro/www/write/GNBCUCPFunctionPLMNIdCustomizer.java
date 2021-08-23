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

import io.fd.honeycomb.translate.spi.write.ListWriterCustomizer;
import io.fd.honeycomb.translate.write.WriteContext;
import io.fd.honeycomb.translate.write.WriteFailedException;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.PLMNId;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.PLMNIdKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class GNBCUCPFunctionPLMNIdCustomizer implements ListWriterCustomizer<PLMNId, PLMNIdKey> {

    private final CrudService<PLMNId> crudService;

    public GNBCUCPFunctionPLMNIdCustomizer(@Nonnull final CrudService<PLMNId> crudService) {
        this.crudService = crudService;
    }

    @Override
    public void writeCurrentAttributes(@Nonnull InstanceIdentifier<PLMNId> instanceIdentifier, @Nonnull PLMNId plmnid,
            @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.writeData(instanceIdentifier, plmnid);
    }

    @Override
    public void updateCurrentAttributes(@Nonnull InstanceIdentifier<PLMNId> id, @Nonnull PLMNId dataBefore,
            @Nonnull PLMNId dataAfter, @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.updateData(id, dataBefore, dataAfter);
    }

    @Override
    public void deleteCurrentAttributes(@Nonnull InstanceIdentifier<PLMNId> instanceIdentifier, @Nonnull PLMNId plmnid,
            @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.deleteData(instanceIdentifier, plmnid);
    }
}
