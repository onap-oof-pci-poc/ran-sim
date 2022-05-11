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

package com.wipro.www;

import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteFailedException;

import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.NRCellRel;
import com.wipro.www.websocket.models.NRCellRelAttributes;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrcellrelation.Attributes;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NRCellCUNRCellRelationAttrCrudService implements CrudService<Attributes> {

    private static final Logger LOG = LoggerFactory.getLogger(NRCellCUNRCellRelationAttrCrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<Attributes> identifier, @Nonnull Attributes data)
            throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            // InMemoryDataTree.getInstance().setRanNetwork(data);
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull InstanceIdentifier<Attributes> identifier, @Nonnull Attributes data)
            throws WriteFailedException {
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
    public void updateData(@Nonnull InstanceIdentifier<Attributes> identifier, @Nonnull Attributes dataOld,
            @Nonnull Attributes dataNew) throws WriteFailedException {
        if (dataOld != null && dataNew != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Update path[{}] from [{}] to [{}]", identifier, dataOld, dataNew);
	    int idNRCellCUIndex = identifier.toString().indexOf("_idNRCellCU=");
            String idNRCellCUSubStr = identifier.toString().substring(idNRCellCUIndex + "_idNRCellCU=".length());
            String idNRCellCU = idNRCellCUSubStr.substring(0, idNRCellCUSubStr.indexOf("}"));
	    NRCellRel nRCellRelation = new NRCellRel();
	    int idNRCellRelationIndex = identifier.toString().indexOf("NRCellRelationKey{_idNRCellRelation=");
            String idNRCellRelationSubStr = identifier.toString().substring(idNRCellRelationIndex + "NRCellRelationKey{_idNRCellRelation=".length());
            String idNRCellRelation = idNRCellRelationSubStr.substring(0, idNRCellRelationSubStr.indexOf("}"));
	    nRCellRelation.setIdNRCellCU(idNRCellCU);
            nRCellRelation.setIdNRCellRelation(idNRCellRelation);
            NRCellRelAttributes nRCellRelAttributes =  new NRCellRelAttributes();
            nRCellRelAttributes.setNRTCI(dataOld.getNRTCI().intValue());
            nRCellRelAttributes.setHOAllowed(dataNew.isIsHOAllowed());
            nRCellRelation.setAttributes(nRCellRelAttributes);

	    ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
            try {
                ObjectMapper Obj = new ObjectMapper();
                String message = Obj.writeValueAsString(nRCellRelation);
                LOG.info("parsed message: " + message);
                configurationHandler.sendDatabaseUpdate(message, MessageType.ModifyAnr);
            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public Attributes readSpecific(@Nonnull InstanceIdentifier<Attributes> identifier) throws ReadFailedException {
        return null;
    }

    @Override
    public List<Attributes> readAll() throws ReadFailedException {
        return null;
    }
}
