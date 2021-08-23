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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.www.websocket.WebsocketClient;
import com.wipro.www.websocket.models.ConfigPLMNInfo;
import com.wipro.www.websocket.models.DeviceData;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.PLMNInfoModel;
import com.wipro.www.websocket.models.SNSSAI;

import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteFailedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.plmninfo.SNSSAIList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.plmninfo.SNSSAIListBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.plmninfo.SNSSAIListKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.snssaiconfig.ConfigData;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NearRTRICpLMNSNSSAICrudService implements CrudService<SNSSAIList> {

    private static final Logger LOG = LoggerFactory.getLogger(NearRTRICpLMNSNSSAICrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<SNSSAIList> identifier, @Nonnull SNSSAIList data)
            throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
            ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
            int idNearRTRICIndex = identifier.toString().indexOf("_idNearRTRIC=");
            String idNearRTRICSubStr = identifier.toString().substring(idNearRTRICIndex + "_idNearRTRIC=".length());
            String idNearRTRIC = idNearRTRICSubStr.substring(0, idNearRTRICSubStr.indexOf("}"));

            int mccIndex = identifier.toString().indexOf("_mcc=Mcc{_value=");
            String mccSubStr = identifier.toString().substring(mccIndex + "_mcc=Mcc{_value=".length());
            String mcc = mccSubStr.substring(0, mccSubStr.indexOf("}"));

            int mncIndex = identifier.toString().indexOf("mnc=Mnc{_value=");
            String mncSubStr = identifier.toString().substring(mncIndex + "mnc=Mnc{_value=".length());
            String mnc = mncSubStr.substring(0, mncSubStr.indexOf("}"));

            /*
             * ConfigPLMNInfo configPLMNInfo = new ConfigPLMNInfo();
             * configPLMNInfo.setMcc(mnc);
             * configPLMNInfo.setMnc(mcc);
             * List<SNSSAI> nSSAIList = new ArrayList<>();
             * //for(SNSSAIList sNSSAIList : data.getSNSSAIList())
             * //{
             * SNSSAI sNSSAI = new SNSSAI();
             * sNSSAI.setSNssai(data.getSNssai());
             * List<ConfigData> configDataList = new ArrayList<>();
             * for(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.
             * snssaiconfig.ConfigData configData : data.getConfigData())
             * {
             * if(!(Objects.isNull(configData.getConfigValue()))){
             * ConfigData info = new ConfigData(configData.getConfigParameter(),configData.getConfigValue().intValue());
             * configDataList.add(info);
             * }
             * }
             * sNSSAI.setConfigData(configDataList);
             * nSSAIList.add(sNSSAI);
             * configPLMNInfo.setSNSSAI(nSSAIList);
             * // }
             * 
             * // ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
             */

            PLMNInfoModel plmnInfoModel = new PLMNInfoModel();
            plmnInfoModel.setpLMNId(mcc + "-" + mnc);
            plmnInfoModel.setNearrtricid(idNearRTRIC);
            plmnInfoModel.setGnbId("");
            List<com.wipro.www.websocket.models.ConfigData> dataList =
                    new ArrayList<com.wipro.www.websocket.models.ConfigData>();
            List<ConfigData> configDataList = data.getConfigData();
            for (ConfigData c : configDataList) {
                com.wipro.www.websocket.models.ConfigData configData = new com.wipro.www.websocket.models.ConfigData();
                if (!(Objects.isNull(c.getConfigValue()))) {
                    configData.setConfigParameter(c.getConfigParameter());
                    configData.setConfigValue(Integer.valueOf(c.getConfigValue().intValue()));
                    dataList.add(configData);
                }
            }
            plmnInfoModel.setConfigData(dataList);
            if (!(Objects.isNull(data.getSNssai()))) {
                plmnInfoModel.setSnssai(data.getSNssai());
            }
            if (!(Objects.isNull(data.getStatus()))) {
                plmnInfoModel.setStatus(data.getStatus());
            }

            if (!(identifier.toString().contains("_idNRCell")) && Objects.isNull(data.getStatus())) {
                configurationHandler.sendRTRICConfig(plmnInfoModel);
            }

        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull InstanceIdentifier<SNSSAIList> identifier, @Nonnull SNSSAIList data)
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
    public void updateData(@Nonnull InstanceIdentifier<SNSSAIList> identifier, @Nonnull SNSSAIList dataOld,
            @Nonnull SNSSAIList dataNew) throws WriteFailedException {
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
    public SNSSAIList readSpecific(@Nonnull InstanceIdentifier<SNSSAIList> identifier) throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);
        return null;
    }

    @Override
    public List<SNSSAIList> readAll() throws ReadFailedException {
        return null;
    }
}
