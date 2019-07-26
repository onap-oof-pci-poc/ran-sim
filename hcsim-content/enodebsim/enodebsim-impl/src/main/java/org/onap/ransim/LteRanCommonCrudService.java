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

import org.onap.ransim.websocket.model.ModifyPci;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRan;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanCommon;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanCommonBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Simple example of class handling Crud operations for plugin.
 * <p/>
 * No real handling, serves just as an illustration.
 *
 * TODO update javadoc
 */
final class LteRanCommonCrudService implements CrudService<LteRanCommon> {

    private static final Logger LOG = LoggerFactory.getLogger(LteRanCommonCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<LteRanCommon> identifier
            , @Nonnull final LteRanCommon data
            , @Nonnull final WriteContext writeContext)
                    throws WriteFailedException {
        LOG.info("RANSIM LteRanCommonCrudService writeData called");
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to
            // identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
            if(!ConfigJsonHandler.getConfigJsonHandler(null).isConfigTopoInitialized)
                return;
            ModifyPci modifyPci = null;
            if(writeContext.getModificationCache().containsKey("modify-pci-object")) {
                modifyPci = (ModifyPci)writeContext.getModificationCache().get("modify-pci-object");
            } else {
                modifyPci = new ModifyPci();
            }

            String cId = ""+data.getCellIdentity();
            modifyPci.setCellId(cId);

            if(modifyPci.isAllSet()) {
                String jsonStr = new Gson().toJson(modifyPci, ModifyPci.class);
                ConfigJsonHandler.getConfigJsonHandler(null).handleModifyCell(jsonStr);
                writeContext.getModificationCache().close();
            } else {
                writeContext.getModificationCache().put("modify-pci-object", modifyPci);
            }

        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull final InstanceIdentifier<LteRanCommon> identifier, @Nonnull final LteRanCommon data)
            throws WriteFailedException {
        LOG.info("RANSIM LteRanCommonCrudService deleteData called");
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to
            // identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Removing path[{}] / data [{}]", identifier, data);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void updateData(@Nonnull final InstanceIdentifier<LteRanCommon> identifier,
            @Nonnull final LteRanCommon dataOld, @Nonnull final LteRanCommon dataNew) throws WriteFailedException {
        LOG.info("RANSIM LteRanCommonCrudService updateData called");
        if (dataOld != null && dataNew != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to
            // identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Update path[{}] from [{}] to [{}]", identifier, dataOld, dataNew);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public LteRanCommon readSpecific(@Nonnull final InstanceIdentifier<LteRanCommon> identifier,
            @Nonnull final ReadContext ctx) throws ReadFailedException {
        String ci = "";
        // load data by this key
        // *Key class will always contain key of entity, in this case long value
        if(ctx != null) {
            JsonObject lrcObj = (JsonObject) ctx.getModificationCache().get("lte-ran-common");
            if(lrcObj != null)
                ci = lrcObj.getString("cell-identity");
        }
        return new LteRanCommonBuilder().setCellIdentity(ci).build();
    }

    @Override
    public List<LteRanCommon> readAll() throws ReadFailedException {
        // read all data under parent node,in this case {@link ModuleState}
        return Collections
                .singletonList(readSpecific(InstanceIdentifier.create(LteRan.class).child(LteRanCommon.class), null));
    }
}
