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
import javax.json.JsonObject;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.CellConfig;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.CellConfigBuilder;

/**
 * Simple example of class handling Crud operations for plugin.
 * <p/>
 * No real handling, serves just as an illustration.
 *
 * TODO update javadoc
 */
final class CellConfigCrudService implements CrudService<CellConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(CellConfigCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<CellConfig> identifier
            , @Nonnull final CellConfig data
            , @Nonnull final WriteContext writeContext)
            throws WriteFailedException {
        LOG.info("RANSIM CellConfigCrudService writeData called");
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
    public void deleteData(@Nonnull final InstanceIdentifier<CellConfig> identifier, @Nonnull final CellConfig data)
            throws WriteFailedException {
        LOG.info("RANSIM CellConfigCrudService deleteData called");
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
    public void updateData(@Nonnull final InstanceIdentifier<CellConfig> identifier, @Nonnull final CellConfig dataOld,
                           @Nonnull final CellConfig dataNew) throws WriteFailedException {
        LOG.info("RANSIM CellConfigCrudService updateData called");
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
    public CellConfig readSpecific(@Nonnull final InstanceIdentifier<CellConfig> identifier
            , @Nonnull final ReadContext ctx) throws ReadFailedException {

        // load data by this key
        // *Key class will always contain key of entity, in this case long value
        if(ctx!=null) {
            JsonObject ccObj = (JsonObject) ctx.getModificationCache().get("cell-config");
            if((ccObj != null) && (ccObj.getJsonObject("lte") != null)){
            ctx.getModificationCache().put("lte", ccObj.getJsonObject("lte"));
            }
        }
        return new CellConfigBuilder()
                .build();
    }

    @Override
    public List<CellConfig> readAll() throws ReadFailedException {
        // read all data under parent node,in this case {@link ModuleState}
        return Collections.singletonList(
                readSpecific(InstanceIdentifier.create(FapService.class).child(CellConfig.class), null));
    }
}
