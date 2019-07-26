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

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.json.JsonObject;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.Lte;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRan;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRanBuilder;
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
final class LteRanCrudService implements CrudService<LteRan> {

    private static final Logger LOG = LoggerFactory.getLogger(LteRanCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<LteRan> identifier
            , @Nonnull final LteRan data
            , @Nonnull final WriteContext writeContext)
                    throws WriteFailedException {
        LOG.info("RANSIM LteRanCrudService writeData called");
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
    public void deleteData(@Nonnull final InstanceIdentifier<LteRan> identifier, @Nonnull final LteRan data)
            throws WriteFailedException {
        LOG.info("RANSIM LteRanCrudService deleteData called");
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
    public void updateData(@Nonnull final InstanceIdentifier<LteRan> identifier, @Nonnull final LteRan dataOld,
            @Nonnull final LteRan dataNew) throws WriteFailedException {
        LOG.info("RANSIM LteRanCrudService updateData called");
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
    public LteRan readSpecific(@Nonnull final InstanceIdentifier<LteRan> identifier
            , @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.info("RANSIM LteRanCrudService readSpecific called");

        // load data by this key
        // *Key class will always contain key of entity, in this case long value
        if(ctx != null) {
            JsonObject lrObj = (JsonObject) ctx.getModificationCache().get("lte-ran");
            if(lrObj != null) {
            if(lrObj.getJsonObject("lte-ran-common") != null)
                ctx.getModificationCache().put("lte-ran-common", lrObj.getJsonObject("lte-ran-common"));
            if(lrObj.getJsonObject("lte-ran-rf") != null)
                ctx.getModificationCache().put("lte-ran-rf", lrObj.getJsonObject("lte-ran-rf"));
            if(lrObj.getJsonObject("lte-ran-neighbor-list-in-use") != null)
                ctx.getModificationCache().put("lte-ran-neighbor-list-in-use", lrObj.getJsonObject("lte-ran-neighbor-list-in-use"));
            }
        }
        return new LteRanBuilder()
                .build();
    }

    @Override
    public List<LteRan> readAll() throws ReadFailedException {
        LOG.info("RANSIM LteRanCrudService readAll called");
        // read all data under parent node,in this case {@link ModuleState}
        return Collections.singletonList(
                readSpecific(InstanceIdentifier.create(Lte.class).child(LteRan.class), null));
    }
}
