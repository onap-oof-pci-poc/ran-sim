/*
 * ============LICENSE_START=======================================================
 * RAN Simulator - HoneyComb
 * ================================================================================
 * Copyright (C) 2018 Wipro Limited.
 * ================================================================================
 *
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
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.json.JsonObject;

import org.onap.ransim.websocket.model.ModifyPci;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev181127.radio.access.FapService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev181127.radio.access.fap.service.X0005b9Lte;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev181127.radio.access.fap.service.X0005b9LteBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier.PathArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
//import com.jayway.jsonpath.JsonPath;

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
final class X0005b9LteCrudService implements CrudService<X0005b9Lte> {

    private static final Logger LOG = LoggerFactory.getLogger(X0005b9LteCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<X0005b9Lte> identifier
            , @Nonnull final X0005b9Lte data
            , @Nonnull final WriteContext writeContext)
                    throws WriteFailedException {
        LOG.info("RANSIM X0005b9LteCrudService writeData called");
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to
            // identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
            int stIndex  = identifier.toString().indexOf("[_alias=");
            String subStr = identifier.toString().substring(stIndex+"[_alias=".length());
            String cid = subStr.substring(0, subStr.indexOf("]"));

            Iterable<PathArgument> paths = identifier.getPathArguments();
            Iterator<PathArgument> pathIter = paths.iterator();
            while (pathIter.hasNext()) {
                PathArgument aPath = pathIter.next();

                if(aPath.getType().getSimpleName().equals("FapService")) {
                    LOG.info("TODO . aPath     : {}", aPath);
                }
            }
            ModifyPci modifyPci = null;
            if(writeContext.getModificationCache().containsKey("modify-pci-object")) {
                LOG.debug("Get from context");
                modifyPci = (ModifyPci)writeContext.getModificationCache().get("modify-pci-object");
            } else {
                modifyPci = new ModifyPci();
                LOG.debug("Create new");
            }

            modifyPci.setPnfName(data.getPnfName());
            modifyPci.setPciId(data.getPhyCellIdInUse().longValue());
            modifyPci.setCellId(cid);

            if(modifyPci.isAllSet()) {
                String jsonStr = new Gson().toJson(modifyPci, ModifyPci.class);
                LOG.info("Sending to Ransim Ctrlr.");
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
    public void deleteData(@Nonnull final InstanceIdentifier<X0005b9Lte> identifier, @Nonnull final X0005b9Lte data)
            throws WriteFailedException {
        LOG.info("RANSIM X0005b9LteCrudService deleteData called");
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
    public void updateData(@Nonnull final InstanceIdentifier<X0005b9Lte> identifier, @Nonnull final X0005b9Lte dataOld,
            @Nonnull final X0005b9Lte dataNew) throws WriteFailedException {
        LOG.info("RANSIM X0005b9LteCrudService updateData called");
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
    public X0005b9Lte readSpecific(@Nonnull final InstanceIdentifier<X0005b9Lte> identifier,
            @Nonnull final ReadContext ctx) throws ReadFailedException {
        LOG.debug("RANSIM X0005b9LteCrudService readSpecific called");
        int pcid = 0;
        String pn = "";
        // load data by this key
        // *Key class will always contain key of entity, in this case long value
        if (ctx != null) {
            JsonObject xlObj = (JsonObject) ctx.getModificationCache().get("x-0005b9-lte");
            if(xlObj != null) {
                pcid = xlObj.getInt("phy-cell-id-in-use");
                pn = xlObj.getString("pnf-name");
            }
        }
        return new X0005b9LteBuilder().setPnfName(pn).setPhyCellIdInUse(BigInteger.valueOf(pcid)).build();
    }

    @Override
    public List<X0005b9Lte> readAll() throws ReadFailedException {
        // read all data under parent node,in this case {@link ModuleState}
        return Collections
                .singletonList(readSpecific(InstanceIdentifier.create(FapService.class).child(X0005b9Lte.class), null));
    }
}
