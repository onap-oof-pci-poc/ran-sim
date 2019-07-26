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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.RadioAccess;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapServiceBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapServiceKey;
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
final class FapServiceCrudService implements CrudService<FapService> {

    private static final Logger LOG = LoggerFactory.getLogger(FapServiceCrudService.class);

    @Override
    public void writeData(@Nonnull final InstanceIdentifier<FapService> identifier
            , @Nonnull final FapService data
            , @Nonnull final WriteContext writeContext)
                    throws WriteFailedException {
        LOG.info("RANSIM FapServiceCrudService writeData called");
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to
            // identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull final InstanceIdentifier<FapService> identifier, @Nonnull final FapService data)
            throws WriteFailedException {
        LOG.info("RANSIM FapServiceCrudService deleteData called");
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
    public void updateData(@Nonnull final InstanceIdentifier<FapService> identifier, @Nonnull final FapService dataOld,
            @Nonnull final FapService dataNew) throws WriteFailedException {
        LOG.info("RANSIM     FapServiceCrudService updateData called");
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
    public FapService readSpecific(@Nonnull final InstanceIdentifier<FapService> identifier
            , @Nonnull final ReadContext ctx)
                    throws ReadFailedException {
        FapService fs = null;
        // read key specified in path identifier
        final FapServiceKey key = identifier.firstKeyOf(FapService.class);
        ConfigJsonHandler cfg = ConfigJsonHandler.getConfigJsonHandler(null);
        if(cfg == null)
            return null;
        JsonArray fsArr = cfg.radioAccessObj.getJsonArray("fap-service");
        if(fsArr != null){
        for (int i = 0; i < fsArr.size(); i++) {
            JsonObject fsObj = fsArr.getJsonObject(i);
            if(fsObj.getString("alias").equals(key.getAlias())) {
                if(ctx!=null) {
                    if(fsObj.getJsonObject("cell-config")!=null)
                        ctx.getModificationCache().put("cell-config", fsObj.getJsonObject("cell-config"));
                if(fsObj.getJsonObject("x-0005b9-lte")!=null)
                    ctx.getModificationCache().put("x-0005b9-lte", fsObj.getJsonObject("x-0005b9-lte"));
                }
                fs = new FapServiceBuilder()
                        .setAlias(key.getAlias())
                        .build();
                break;
            }
        }
        }
        return fs;
    }

    @Override
    public List<FapService> readAll() throws ReadFailedException {
        ConfigJsonHandler cfg = ConfigJsonHandler.getConfigJsonHandler(null);
        List<FapService> fsList = new ArrayList<FapService>();
        if(cfg ==null)
            return fsList;
        if(cfg.radioAccessObj ==null)
            return fsList;
        JsonArray fsArr = cfg.radioAccessObj.getJsonArray("fap-service");
        for (int i = 0; i < fsArr.size(); i++) {
            JsonObject fsObj = fsArr.getJsonObject(i);
            FapService fs = readSpecific(InstanceIdentifier.create(RadioAccess.class).child(FapService.class,
                    new FapServiceKey(fsObj.getString("alias"))), null);
            fsList.add(fs);
        }
        // read all data under parent node,in this case {@link ModuleState}
        return fsList;
    }
}
