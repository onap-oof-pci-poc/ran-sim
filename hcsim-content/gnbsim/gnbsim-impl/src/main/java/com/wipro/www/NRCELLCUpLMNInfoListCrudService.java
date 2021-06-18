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
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.PLMNInfoList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.PLMNInfoListKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.PLMNInfoListBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.plmninfo.SNSSAIList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.snssaiconfig.ConfigData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.www.websocket.WebsocketClient;
import com.wipro.www.websocket.models.DeviceData;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.PLMNInfoModel;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NRCELLCUpLMNInfoListCrudService implements CrudService<PLMNInfoList> {

    private static final Logger LOG = LoggerFactory.getLogger(NRCELLCUpLMNInfoListCrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<PLMNInfoList> identifier, @Nonnull PLMNInfoList data) throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
	    ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
	    int idNearRTRICIndex = identifier.toString().indexOf("_idNearRTRIC=");
	    String idNearRTRICSubStr = identifier.toString().substring(idNearRTRICIndex + "_idNearRTRIC=".length());
	    String idNearRTRIC = idNearRTRICSubStr.substring(0, idNearRTRICSubStr.indexOf("}"));

	    int idGNBCUCPFunctionIndex = identifier.toString().indexOf("_idGNBCUCPFunction=");
	    String idGNBCUCPFunctionSubStr = identifier.toString()
		    .substring(idGNBCUCPFunctionIndex + "_idGNBCUCPFunction=".length());
	    String idGNBCUCPFunction = idGNBCUCPFunctionSubStr.substring(0, idGNBCUCPFunctionSubStr.indexOf("}"));
			
	    int idNRCellCUIndex = identifier.toString().indexOf("_idNRCellCU=");
	    String idNRCellCUSubStr = identifier.toString()
		    .substring(idNRCellCUIndex + "_idNRCellCU=".length());
	    String idNRCellCU = idNRCellCUSubStr.substring(0, idNRCellCUSubStr.indexOf("}"));

	    int mccIndex = identifier.toString().indexOf("_mcc=Mcc{_value=");
	    String mccSubStr = identifier.toString().substring(mccIndex + "_mcc=Mcc{_value=".length());
	    String mcc = mccSubStr.substring(0, mccSubStr.indexOf("}"));

	    int mncIndex = identifier.toString().indexOf("mnc=Mnc{_value=");
	    String mncSubStr = identifier.toString().substring(mncIndex + "mnc=Mnc{_value=".length());
	    String mnc = mncSubStr.substring(0, mncSubStr.indexOf("}"));
			
	    PLMNInfoModel plmnInfoModel = new PLMNInfoModel();
	    plmnInfoModel.setpLMNId(mcc + "-" + mnc);
	    plmnInfoModel.setNearrtricid(idNearRTRIC);
	    plmnInfoModel.setGnbType("gnbcucp");
	    plmnInfoModel.setGnbId(idGNBCUCPFunction);
	    plmnInfoModel.setNrCellId(Integer.parseInt(idNRCellCU));
	    List<SNSSAIList> snssaiList = data.getSNSSAIList();
	    for (SNSSAIList s : snssaiList) {
		    List<com.wipro.www.websocket.models.ConfigData> dataList = new ArrayList<com.wipro.www.websocket.models.ConfigData>();
                    List<ConfigData> configDataList = s.getConfigData();
                    for (ConfigData c : configDataList) {
			    if(!(Objects.isNull(c.getConfigValue()))){
                            com.wipro.www.websocket.models.ConfigData configData = new com.wipro.www.websocket.models.ConfigData();
                            configData.setConfigParameter(c.getConfigParameter());
                            configData.setConfigValue(Integer.valueOf(c.getConfigValue().intValue()));
                            dataList.add(configData);
			    }

                    }
                    plmnInfoModel.setConfigData(dataList);
		    plmnInfoModel.setSnssai(s.getSNssai());
		    plmnInfoModel.setStatus(s.getStatus());
		    try {
			    ObjectMapper obj = new ObjectMapper();
			    String message = obj.writeValueAsString(plmnInfoModel);
			    LOG.info("parsed message: " + message);
			    configurationHandler.sendDatabaseUpdate(message,MessageType.HC_TO_RC_PLMN);
		    } catch (JsonProcessingException jsonProcessingException) {
			    LOG.error("Error parsing json");
		    }
		   

	    }
	} else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull InstanceIdentifier<PLMNInfoList> identifier, @Nonnull PLMNInfoList data) throws WriteFailedException {
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
    public void updateData(@Nonnull InstanceIdentifier<PLMNInfoList> identifier, @Nonnull PLMNInfoList dataOld, @Nonnull PLMNInfoList dataNew) throws WriteFailedException {
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
    public PLMNInfoList readSpecific(@Nonnull InstanceIdentifier<PLMNInfoList> identifier) throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);
        return null;
    }

    @Override
    public List<PLMNInfoList> readAll() throws ReadFailedException {
        return null;
    }
}
