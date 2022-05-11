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

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.snssaiconfig.ConfigData;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.snssaiconfig.ConfigDataBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.snssaiconfig.ConfigDataKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NearRTRICConfigCrudService implements CrudService<ConfigData> {

    private static final Logger LOG = LoggerFactory.getLogger(NearRTRICConfigCrudService.class);
    private static final ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();

    @Override
    public void writeData(@Nonnull InstanceIdentifier<ConfigData> identifier, @Nonnull ConfigData data)
            throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
            int idNearRTRICIndex = identifier.toString().indexOf("_idNearRTRIC=");
            String idNearRTRICSubStr = identifier.toString().substring(idNearRTRICIndex + "_idNearRTRIC=".length());
            String idNearRTRIC = idNearRTRICSubStr.substring(0, idNearRTRICSubStr.indexOf("}"));

            int mccIndex = identifier.toString().indexOf("_mcc=Mcc{_value=");
            String mccSubStr = identifier.toString().substring(mccIndex + "_mcc=Mcc{_value=".length());
            String mcc = mccSubStr.substring(0, mccSubStr.indexOf("}"));

            int mncIndex = identifier.toString().indexOf("mnc=Mnc{_value=");
            String mncSubStr = identifier.toString().substring(mncIndex + "mnc=Mnc{_value=".length());
            String mnc = mncSubStr.substring(0, mncSubStr.indexOf("}"));

            int snssaiIndex = identifier.toString().indexOf("_sNssai=");
            String snssaiSubStr = identifier.toString().substring(snssaiIndex + "_sNssai=".length());
            String snssai = snssaiSubStr.substring(0, snssaiSubStr.indexOf("}"));

            PLMNInfoModel plmnInfoModel = new PLMNInfoModel();
            plmnInfoModel.setpLMNId(mcc + "-" + mnc);
            plmnInfoModel.setNearrtricid(idNearRTRIC);
            // plmnInfoModel.setGnbId("");
            List<com.wipro.www.websocket.models.ConfigData> dataList =
                    new ArrayList<com.wipro.www.websocket.models.ConfigData>();
            /*
             * List<ConfigData> configDataList = data.getConfigData();
             * for (ConfigData c : configDataList) {
             */
            com.wipro.www.websocket.models.ConfigData configData = new com.wipro.www.websocket.models.ConfigData();
            if (Objects.nonNull(data.getConfigValue())) {
                configData.setConfigParameter(data.getConfigParameter());
                configData.setConfigValue(Integer.valueOf(data.getConfigValue().intValue()));
                dataList.add(configData);
            }

            plmnInfoModel.setConfigData(dataList);
            plmnInfoModel.setSnssai(snssai);

            try {
                ObjectMapper obj = new ObjectMapper();
                String message = obj.writeValueAsString(plmnInfoModel);

                if (Objects.nonNull(data.getConfigValue())
                        && !(data.getConfigParameter().equalsIgnoreCase("maxNumberOfConns"))
                        && !(data.getConfigParameter().equalsIgnoreCase("dLThptPerSlice"))
                        && !(data.getConfigParameter().equalsIgnoreCase("uLThptPerSlice"))) {
                    String[] nrCellString = data.getConfigParameter().split("-", 2);
                    plmnInfoModel.setNrCellId(Integer.parseInt(nrCellString[0]));
                    plmnInfoModel.setGnbType("gnbcucp");
                    message = obj.writeValueAsString(plmnInfoModel);
                    configurationHandler.sendDatabaseUpdate(message, MessageType.HC_TO_RC_PLMN);
                }

            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }

        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull InstanceIdentifier<ConfigData> identifier, @Nonnull ConfigData data)
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
    public void updateData(@Nonnull InstanceIdentifier<ConfigData> identifier, @Nonnull ConfigData dataOld,
            @Nonnull ConfigData dataNew) throws WriteFailedException {
        if (dataOld != null && dataNew != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Update path[{}] from [{}] to [{}]", identifier, dataOld, dataNew);
            int idNearRTRICIndex = identifier.toString().indexOf("_idNearRTRIC=");
            String idNearRTRICSubStr = identifier.toString().substring(idNearRTRICIndex + "_idNearRTRIC=".length());
            String idNearRTRIC = idNearRTRICSubStr.substring(0, idNearRTRICSubStr.indexOf("}"));

            int mccIndex = identifier.toString().indexOf("_mcc=Mcc{_value=");
            String mccSubStr = identifier.toString().substring(mccIndex + "_mcc=Mcc{_value=".length());
            String mcc = mccSubStr.substring(0, mccSubStr.indexOf("}"));

            int mncIndex = identifier.toString().indexOf("mnc=Mnc{_value=");
            String mncSubStr = identifier.toString().substring(mncIndex + "mnc=Mnc{_value=".length());
            String mnc = mncSubStr.substring(0, mncSubStr.indexOf("}"));

            int snssaiIndex = identifier.toString().indexOf("_sNssai=");
            String snssaiSubStr = identifier.toString().substring(snssaiIndex + "_sNssai=".length());
            String snssai = snssaiSubStr.substring(0, snssaiSubStr.indexOf("}"));

            PLMNInfoModel plmnInfoModel = new PLMNInfoModel();
            plmnInfoModel.setpLMNId(mcc + "-" + mnc);
            plmnInfoModel.setNearrtricid(idNearRTRIC);
            // plmnInfoModel.setGnbId("");
            List<com.wipro.www.websocket.models.ConfigData> dataList =
                    new ArrayList<com.wipro.www.websocket.models.ConfigData>();
            /*
             * List<ConfigData> configDataList = data.getConfigData();
             * for (ConfigData c : configDataList) {
             */
            com.wipro.www.websocket.models.ConfigData configData = new com.wipro.www.websocket.models.ConfigData();
            if (Objects.nonNull(dataNew.getConfigValue())) {
                configData.setConfigParameter(dataNew.getConfigParameter());
                configData.setConfigValue(Integer.valueOf(dataNew.getConfigValue().intValue()));
                dataList.add(configData);
            }

            plmnInfoModel.setConfigData(dataList);
            plmnInfoModel.setSnssai(snssai);

            try {
                ObjectMapper obj = new ObjectMapper();
                String message = obj.writeValueAsString(plmnInfoModel);

                if (Objects.nonNull(dataNew.getConfigValue())
                        && !(dataNew.getConfigParameter().equalsIgnoreCase("maxNumberOfConns"))
                        && !(dataNew.getConfigParameter().equalsIgnoreCase("dLThptPerSlice"))
                        && !(dataNew.getConfigParameter().equalsIgnoreCase("uLThptPerSlice"))) {
                    String[] nrCellString = dataNew.getConfigParameter().split("-", 2);
                    plmnInfoModel.setNrCellId(Integer.parseInt(nrCellString[0]));
                    plmnInfoModel.setGnbType("gnbcucp");
                    message = obj.writeValueAsString(plmnInfoModel);
                    configurationHandler.sendDatabaseUpdate(message, MessageType.HC_TO_RC_PLMN);
                }

                LOG.info("parsed message: " + message);
            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }
        } else {
            throw new WriteFailedException.DeleteFailedException(identifier,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public ConfigData readSpecific(@Nonnull InstanceIdentifier<ConfigData> identifier) throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);
        return null;
    }

    @Override
    public List<ConfigData> readAll() throws ReadFailedException {
        return null;
    }
}
