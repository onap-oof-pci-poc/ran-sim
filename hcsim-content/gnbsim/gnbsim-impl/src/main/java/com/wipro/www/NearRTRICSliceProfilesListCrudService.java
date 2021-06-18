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
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.SliceProfile;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
	    if(!(Objects.isNull(data.getSNSSAI()))){
            String snssaiString = data.getSNSSAI().toString();
	    
	    int snssaiIndex = snssaiString.indexOf("{_value=");
            String snssaiSubStr = snssaiString.substring(snssaiIndex + "{_value=".length());
            String snssai = snssaiSubStr.substring(0, snssaiSubStr.indexOf("}"));
            sliceProfile.setsNSSAI(snssai);
	    }
            //sliceProfile.setsNSSAI(snssaiString.substring(14,snssaiString.length()));
            sliceProfile.setMaxNumberofUEs(data.getMaxNumberofUEs().intValue());
	    if(!(Objects.isNull(data.getCoverageAreaList()))){
            sliceProfile.setCoverageareaList(data.getCoverageAreaList().toString());
	    }
            sliceProfile.setLatency(data.getLatency().intValue());
            sliceProfile.setdLThptPerSlice(data.getDLThptPerSlice().intValue());
            sliceProfile.setuLThptPerSlice(data.getULThptPerSlice().intValue());
            sliceProfile.setMaxNumberofConns(data.getMaxNumberofConns().intValue());
            if(!(Objects.isNull(data.getResourceSharingLevel()))){
	    sliceProfile.setResourcesharinglevel(data.getResourceSharingLevel());
	    }
	    if(!(Objects.isNull(data.getUEMobilityLevel()))){
            sliceProfile.setUemobilitylevel(data.getUEMobilityLevel()); 
	    }
            ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
            try {
                ObjectMapper obj = new ObjectMapper();
                String message = obj.writeValueAsString(sliceProfile);
                LOG.info("parsed message: " + message);
		configurationHandler.sendDatabaseUpdate(message,MessageType.HC_TO_RC_SLICE_PROFILE);
            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }
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
	    int snssaiIndex = snssaiString.indexOf("{_value=");
            String snssaiSubStr = snssaiString.substring(snssaiIndex + "{_value=".length());
            String snssai = snssaiSubStr.substring(0, snssaiSubStr.indexOf("}"));
            sliceProfile.setsNSSAI(snssai);
            sliceProfile.setMaxNumberofUEs(data.getMaxNumberofUEs().intValue());
            if(!(Objects.isNull(data.getCoverageAreaList()))){
            sliceProfile.setCoverageareaList(data.getCoverageAreaList().toString());
            }
            sliceProfile.setLatency(data.getLatency().intValue());
            sliceProfile.setdLThptPerSlice(data.getDLThptPerSlice().intValue());
            sliceProfile.setuLThptPerSlice(data.getULThptPerSlice().intValue());
            sliceProfile.setMaxNumberofConns(data.getMaxNumberofConns().intValue());
            if(!(Objects.isNull(data.getResourceSharingLevel()))){
            sliceProfile.setResourcesharinglevel(data.getResourceSharingLevel());
            }
            if(!(Objects.isNull(data.getUEMobilityLevel()))){
            sliceProfile.setUemobilitylevel(data.getUEMobilityLevel());
            }

            ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
            try {
                ObjectMapper obj = new ObjectMapper();
                String message = obj.writeValueAsString(sliceProfile);
                LOG.info("parsed message: " + message);
		configurationHandler.sendDatabaseUpdate(message,MessageType.HC_TO_RC_SLICE_PROFILE_DEL);
            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }
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
