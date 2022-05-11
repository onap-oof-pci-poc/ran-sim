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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.www.websocket.models.NRCellDU;
import com.wipro.www.websocket.models.DUAttributes;
import com.wipro.www.websocket.models.MessageType;

import java.util.List;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NRCellDUAttributesCrudService implements CrudService<Attributes> {

    private static final Logger LOG = LoggerFactory.getLogger(NRCellDUAttributesCrudService.class);

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
            com.wipro.www.websocket.models.NRCellDU nRCellDU = new com.wipro.www.websocket.models.NRCellDU();
            int idNRCellDUIndex = identifier.toString().indexOf("NRCellDUKey{_idNRCellDU=");
            String idNRCellDUSubStr = identifier.toString().substring(idNRCellDUIndex + "NRCellDUKey{_idNRCellDU=".length());
            String idNRCellDU = idNRCellDUSubStr.substring(0, idNRCellDUSubStr.indexOf("}"));
            nRCellDU.setIdNRCellDU(idNRCellDU);
            com.wipro.www.websocket.models.DUAttributes dUAttributes =  new com.wipro.www.websocket.models.DUAttributes();
            dUAttributes.setNRPCI(dataNew.getNRPCI());
            //dUAttributes.setOperationalState(data.getOperationalState().toString());
            //dUAttributes.setCellState(data.getCellState().toString());
            nRCellDU.setAttributes(dUAttributes);

            ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
            try {
                    ObjectMapper obj = new ObjectMapper();
                    String message = obj.writeValueAsString(nRCellDU);
                    LOG.info("parsed message: " + message);
                    configurationHandler.sendDatabaseUpdate(message, MessageType.ModifyPci);
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
