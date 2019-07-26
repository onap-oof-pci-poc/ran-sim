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

package org.onap.ransim.write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.onap.ransim.CrudService;
import io.fd.honeycomb.translate.spi.write.ListWriterCustomizer;
import io.fd.honeycomb.translate.spi.write.WriterCustomizer;
import io.fd.honeycomb.translate.write.WriteContext;
import io.fd.honeycomb.translate.write.WriteFailedException;
import javax.annotation.Nonnull;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.Lte;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.LteBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * Writer for {@link Lte} list node from our YANG model.
 */
public final class LteCustomizer implements WriterCustomizer<Lte> {

    private final CrudService<Lte> crudService;
    private static final Logger LOG = LoggerFactory.getLogger(LteCustomizer.class);

    public LteCustomizer(@Nonnull final CrudService<Lte> crudService) {
        LOG.info("RANSIM LteCustomizer ctor called");
        this.crudService = crudService;
    }

    @Override
    public void writeCurrentAttributes(@Nonnull final InstanceIdentifier<Lte> id, @Nonnull final Lte dataAfter,
                                       @Nonnull final WriteContext writeContext) throws WriteFailedException {
        LOG.info("RANSIM LteCustomizer writeCurrentAttributes called");
        //perform write of data,or throw exception
        //invoked by PUT operation,if provided data doesn't exist in Config data
        crudService.writeData(id, dataAfter, writeContext);
    }

    @Override
    public void updateCurrentAttributes(@Nonnull final InstanceIdentifier<Lte> id,
                                        @Nonnull final Lte dataBefore,
                                        @Nonnull final Lte dataAfter, @Nonnull final WriteContext writeContext)
            throws WriteFailedException {
        LOG.info("RANSIM LteCustomizer updateCurrentAttributes called");
        //perform update of data,or throw exception
        //invoked by PUT operation,if provided data does exist in Config data
        crudService.updateData(id, dataBefore, dataAfter);
    }

    @Override
    public void deleteCurrentAttributes(@Nonnull final InstanceIdentifier<Lte> id,
                                        @Nonnull final Lte dataBefore,
                                        @Nonnull final WriteContext writeContext) throws WriteFailedException {
        LOG.info("RANSIM LteCustomizer deleteCurrentAttributes called");
        //perform delete of data,or throw exception
        //invoked by DELETE operation
        crudService.deleteData(id, dataBefore);
    }
}
