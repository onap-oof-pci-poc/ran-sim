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

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.SliceProfilesList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.SliceProfilesListKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.SliceProfilesListBuilder;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.www.websocket.WebsocketClient;
import com.wipro.www.websocket.models.DeviceData;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.SliceProfile;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearRTRICSliceProfilesListCrudService implements CrudService<SliceProfilesList> {

    private static final Logger LOG = LoggerFactory.getLogger(NearRTRICSliceProfilesListCrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<SliceProfilesList> identifier, @Nonnull SliceProfilesList data) throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
            SliceProfile sliceProfile = new SliceProfile();
            sliceProfile.setSliceProfileId(data.getSliceProfileId());
            String snssaiString = data.getSNSSAI().toString();
            sliceProfile.setsNSSAI(snssaiString.substring(14,snssaiString.length()));
            //sliceProfile.setpLMNIdList();
            sliceProfile.setMaxNumberofUEs(data.getMaxNumberofUEs().intValue());
            sliceProfile.setCoverageareaList(data.getCoverageAreaList().toString());
            sliceProfile.setLatency(data.getLatency().intValue());
            sliceProfile.setdLThptPerSlice(data.getDLThptPerSlice().intValue());
            sliceProfile.setuLThptPerSlice(data.getULThptPerSlice().intValue());
            sliceProfile.setMaxNumberofConns(data.getMaxNumberofConns().intValue());
            sliceProfile.setResourcesharinglevel(data.getResourceSharingLevel());
            sliceProfile.setUemobilitylevel(data.getUEMobilityLevel()); 
            WebsocketClient websocketClient = ConfigurationHandler.getInstance().getWebsocketClient();
            DeviceData deviceData = new DeviceData();
            try {
                ObjectMapper obj = new ObjectMapper();
                String message = obj.writeValueAsString(sliceProfile);
                LOG.info("parsed message: " + message);
                deviceData.setMessage(message);
            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }
           deviceData.setMessageType(MessageType.HC_TO_RC_SLICE_PROFILE);
           websocketClient.sendMessage(deviceData); 
        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull InstanceIdentifier<SliceProfilesList> identifier, @Nonnull SliceProfilesList data) throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Removing path[{}] / data [{}]", identifier, data);
            SliceProfile sliceProfile = new SliceProfile();
            sliceProfile.setSliceProfileId(data.getSliceProfileId());
            String snssaiString = data.getSNSSAI().toString();
            sliceProfile.setsNSSAI(snssaiString.substring(14,snssaiString.length()));
            //sliceProfile.setpLMNIdList();
            sliceProfile.setMaxNumberofUEs(data.getMaxNumberofUEs().intValue());
            sliceProfile.setCoverageareaList(data.getCoverageAreaList().toString());
            sliceProfile.setLatency(data.getLatency().intValue());
            sliceProfile.setdLThptPerSlice(data.getDLThptPerSlice().intValue());
            sliceProfile.setuLThptPerSlice(data.getULThptPerSlice().intValue());
            sliceProfile.setMaxNumberofConns(data.getMaxNumberofConns().intValue());
            sliceProfile.setResourcesharinglevel(data.getResourceSharingLevel());
            sliceProfile.setUemobilitylevel(data.getUEMobilityLevel());
            WebsocketClient websocketClient = ConfigurationHandler.getInstance().getWebsocketClient();
            DeviceData deviceData = new DeviceData();
            try {
                ObjectMapper obj = new ObjectMapper();
                String message = obj.writeValueAsString(sliceProfile);
                LOG.info("parsed message: " + message);
                deviceData.setMessage(message);
            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }
           deviceData.setMessageType(MessageType.HC_TO_RC_SLICE_PROFILE_DEL);
           websocketClient.sendMessage(deviceData);
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void updateData(@Nonnull InstanceIdentifier<SliceProfilesList> identifier, @Nonnull SliceProfilesList dataOld, @Nonnull SliceProfilesList dataNew) throws WriteFailedException {
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
    public SliceProfilesList readSpecific(@Nonnull InstanceIdentifier<SliceProfilesList> identifier) throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);
        return null;
    }

    @Override
    public List<SliceProfilesList> readAll() throws ReadFailedException {
        return null;
    }
}
