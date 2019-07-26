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

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.json.JsonObject;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.CellConfig;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.Lte;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.LteBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fd.honeycomb.translate.read.ReadContext;
import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteContext;
import io.fd.honeycomb.translate.write.WriteFailedException;

/**
 * Simple example of class handling Crud operations for plugin.
 * <p/>
 * No real handling, serves just as an illustration.
 *
 * TODO update javadoc
 */
final class LteCrudService implements CrudService<Lte> {

    private static final Logger LOG = LoggerFactory.getLogger(LteCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<Lte> identifier
            , @Nonnull final Lte data
            , @Nonnull final WriteContext writeContext)
                    throws WriteFailedException {
        LOG.info("RANSIM LteCrudService writeData called");
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
    public void deleteData(@Nonnull final InstanceIdentifier<Lte> identifier, @Nonnull final Lte data)
            throws WriteFailedException {
        LOG.info("RANSIM LteCrudService deleteData called");
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
    public void updateData(@Nonnull final InstanceIdentifier<Lte> identifier, @Nonnull final Lte dataOld,
            @Nonnull final Lte dataNew) throws WriteFailedException {
        LOG.info("RANSIM LteCrudService updateData called");
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
    public Lte readSpecific(@Nonnull final InstanceIdentifier<Lte> identifier
            , @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM LteCrudService readSpecific called");

        int tnoe = 0;
        // load data by this key
        // *Key class will always contain key of entity, in this case long value
        if(ctx != null) {
            JsonObject lObj = (JsonObject) ctx.getModificationCache().get("lte");
            if((lObj != null) && (lObj.getJsonObject("lte-ran") != null)) {
                ctx.getModificationCache().put("lte-ran", lObj.getJsonObject("lte-ran"));
                tnoe = lObj.getInt("tunnel-number-of-entries");
            }
        }

        return new LteBuilder()
                .setTunnelNumberOfEntries(BigInteger.valueOf(tnoe))
                .build();
    }

    @Override
    public List<Lte> readAll() throws ReadFailedException {
        LOG.info("RANSIM LteCrudService readAll called");
        // read all data under parent node,in this case {@link ModuleState}
        return Collections.singletonList(
                readSpecific(InstanceIdentifier.create(CellConfig.class).child(Lte.class), null));
    }
}
