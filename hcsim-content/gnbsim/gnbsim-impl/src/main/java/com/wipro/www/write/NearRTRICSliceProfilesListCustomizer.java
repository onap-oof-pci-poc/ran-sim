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

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.SliceProfilesList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.SliceProfilesListKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class NearRTRICSliceProfilesListCustomizer
        implements ListWriterCustomizer<SliceProfilesList, SliceProfilesListKey> {

    private final CrudService<SliceProfilesList> crudService;

    public NearRTRICSliceProfilesListCustomizer(@Nonnull final CrudService<SliceProfilesList> crudService) {
        this.crudService = crudService;
    }

    @Override
    public void writeCurrentAttributes(@Nonnull InstanceIdentifier<SliceProfilesList> instanceIdentifier,
            @Nonnull SliceProfilesList sliceProfileList, @Nonnull WriteContext writeContext)
            throws WriteFailedException {
        crudService.writeData(instanceIdentifier, sliceProfileList);
    }

    @Override
    public void updateCurrentAttributes(@Nonnull InstanceIdentifier<SliceProfilesList> id,
            @Nonnull SliceProfilesList dataBefore, @Nonnull SliceProfilesList dataAfter,
            @Nonnull WriteContext writeContext) throws WriteFailedException {
        crudService.updateData(id, dataBefore, dataAfter);
    }

    @Override
    public void deleteCurrentAttributes(@Nonnull InstanceIdentifier<SliceProfilesList> instanceIdentifier,
            @Nonnull SliceProfilesList sliceProfileList, @Nonnull WriteContext writeContext)
            throws WriteFailedException {
        crudService.deleteData(instanceIdentifier, sliceProfileList);
    }
}
