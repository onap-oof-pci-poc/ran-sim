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

package org.onap.ransim;

import io.fd.honeycomb.translate.read.ReadContext;
import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteContext;
import io.fd.honeycomb.translate.write.WriteFailedException;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRan;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanRf;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanRfBuilder;

/**
 * Simple example of class handling Crud operations for plugin.
 * <p/>
 * No real handling, serves just as an illustration.
 *
 * TODO update javadoc
 */
final class LteRanRfCrudService implements CrudService<LteRanRf> {

    private static final Logger LOG = LoggerFactory.getLogger(LteRanRfCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<LteRanRf> identifier
            , @Nonnull final LteRanRf data
            , @Nonnull final WriteContext writeContext)
            throws WriteFailedException {
        LOG.info("RANSIM LteRanRfCrudService writeData called");
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull final InstanceIdentifier<LteRanRf> identifier, @Nonnull final LteRanRf data)
            throws WriteFailedException {
        LOG.info("RANSIM LteRanRfCrudService deleteData called");
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Removing path[{}] / data [{}]", identifier, data);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void updateData(@Nonnull final InstanceIdentifier<LteRanRf> identifier, @Nonnull final LteRanRf dataOld,
                           @Nonnull final LteRanRf dataNew) throws WriteFailedException {
        LOG.info("RANSIM LteRanRfCrudService updateData called");
        if (dataOld != null && dataNew != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Update path[{}] from [{}] to [{}]", identifier, dataOld, dataNew);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public LteRanRf readSpecific(@Nonnull final InstanceIdentifier<LteRanRf> identifier
            , @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM LteRanRfCrudService readSpecific called");

        // load data by this key
        // *Key class will always contain key of entity, in this case long value

        return new LteRanRfBuilder()
                .build();
    }

    @Override
    public List<LteRanRf> readAll() throws ReadFailedException {
        LOG.info("RANSIM LteRanRfCrudService readAll called");
        // read all data under parent node,in this case {@link ModuleState}
        return Collections.singletonList(
                readSpecific(InstanceIdentifier.create(LteRan.class).child(LteRanRf.class), null));
    }
}
