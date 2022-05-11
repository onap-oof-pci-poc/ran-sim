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
import com.wipro.www.websocket.models.DeviceData;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.RRMPolicyMember;
import com.wipro.www.websocket.models.RRMPolicyRatioModel;

import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteFailedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.rrmpolicyratio.Attributes;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.rrmpolicy_group.RRMPolicyMemberList;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GNBCUUPFunctionRRMPolicyRatioAttrCrudService implements CrudService<Attributes> {

    private static final Logger LOG = LoggerFactory.getLogger(GNBCUUPFunctionRRMPolicyRatioAttrCrudService.class);
    private static final ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();

    @Override
    public void writeData(@Nonnull InstanceIdentifier<Attributes> identifier, @Nonnull Attributes data)
            throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            // InMemoryDataTree.getInstance().setRanNetwork(data);
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
            int idRRMPolicyRatioIndex = identifier.toString().indexOf("RRMPolicyRatioKey{_id=");
            String idRRMPolicyRatioSubStr =
                    identifier.toString().substring(idRRMPolicyRatioIndex + "RRMPolicyRatioKey{_id=".length());
            String idRRMPolicyRatio = idRRMPolicyRatioSubStr.substring(0, idRRMPolicyRatioSubStr.indexOf("}"));

            RRMPolicyRatioModel rrmPolicyRatioModel = new RRMPolicyRatioModel();
            rrmPolicyRatioModel.setRrmPolicyID(idRRMPolicyRatio);
            if (Objects.nonNull(data.getResourceType())) {
                rrmPolicyRatioModel.setResourceType(data.getResourceType());
            }
            List<RRMPolicyMember> rrmPolicyMemberList = new ArrayList<RRMPolicyMember>();
            List<RRMPolicyMemberList> rRMPolicyMemberDataList = data.getRRMPolicyMemberList();
            for (RRMPolicyMemberList rrmp : rRMPolicyMemberDataList) {
                RRMPolicyMember rrmPolicyMember = new RRMPolicyMember();
                String mcc = rrmp.getMcc().toString().substring(11, rrmp.getMcc().toString().length() - 1);
                String mnc = rrmp.getMnc().toString().substring(11, rrmp.getMnc().toString().length() - 1);
                rrmPolicyMember.setpLMNId(mcc + "-" + mnc);
                rrmPolicyMember
                        .setsNSSAI(rrmp.getSNSSAI().toString().substring(14, rrmp.getSNSSAI().toString().length() - 1));
                rrmPolicyMemberList.add(rrmPolicyMember);
            }
            rrmPolicyRatioModel.setrRMPolicyMemberList(rrmPolicyMemberList);
            if (Objects.nonNull(data.getQuotaType())) {
                rrmPolicyRatioModel.setQuotaType(data.getQuotaType().toString());
            }
            if (Objects.nonNull(data.getRRMPolicyMaxRatio())) {
                rrmPolicyRatioModel.setrRMPolicyMaxRatio(data.getRRMPolicyMaxRatio().intValue());
            }
            if (Objects.nonNull(data.getRRMPolicyMinRatio())) {
                rrmPolicyRatioModel.setrRMPolicyMinRatio(data.getRRMPolicyMinRatio().intValue());
            }
            if (Objects.nonNull(data.getRRMPolicyDedicatedRatio())) {
                rrmPolicyRatioModel.setrRMPolicyDedicatedRatio(data.getRRMPolicyDedicatedRatio().intValue());
            }
            // rrmPolicyRatioModel.setResourceID("");
            // rrmPolicyRatioModel.setSliceType("");
            try {
                ObjectMapper Obj = new ObjectMapper();
                String message = Obj.writeValueAsString(rrmPolicyRatioModel);
                LOG.info("parsed message: " + message);
                configurationHandler.sendDatabaseUpdate(message, MessageType.HC_TO_RC_RRM_POLICY);
            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }
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
            int idRRMPolicyRatioIndex = identifier.toString().indexOf("RRMPolicyRatioKey{_id=");
            String idRRMPolicyRatioSubStr =
                    identifier.toString().substring(idRRMPolicyRatioIndex + "RRMPolicyRatioKey{_id=".length());
            String idRRMPolicyRatio = idRRMPolicyRatioSubStr.substring(0, idRRMPolicyRatioSubStr.indexOf("}"));

            RRMPolicyRatioModel rrmPolicyRatioModel = new RRMPolicyRatioModel();
            rrmPolicyRatioModel.setRrmPolicyID(idRRMPolicyRatio);
            if (Objects.nonNull(data.getResourceType())) {
                rrmPolicyRatioModel.setResourceType(data.getResourceType());
            }
            List<RRMPolicyMember> rrmPolicyMemberList = new ArrayList<RRMPolicyMember>();
            List<RRMPolicyMemberList> rRMPolicyMemberDataList = data.getRRMPolicyMemberList();
            for (RRMPolicyMemberList rrmp : rRMPolicyMemberDataList) {
                RRMPolicyMember rrmPolicyMember = new RRMPolicyMember();
                String mcc = rrmp.getMcc().toString().substring(11, rrmp.getMcc().toString().length() - 1);
                String mnc = rrmp.getMnc().toString().substring(11, rrmp.getMnc().toString().length() - 1);
                rrmPolicyMember.setpLMNId(mcc + "-" + mnc);
                rrmPolicyMember
                        .setsNSSAI(rrmp.getSNSSAI().toString().substring(14, rrmp.getSNSSAI().toString().length() - 1));
                rrmPolicyMemberList.add(rrmPolicyMember);
            }
            rrmPolicyRatioModel.setrRMPolicyMemberList(rrmPolicyMemberList);
            if (Objects.nonNull(data.getQuotaType())) {
                rrmPolicyRatioModel.setQuotaType(data.getQuotaType().toString());
            }
            if (Objects.nonNull(data.getRRMPolicyMaxRatio())) {
                rrmPolicyRatioModel.setrRMPolicyMaxRatio(data.getRRMPolicyMaxRatio().intValue());
            }
            if (Objects.nonNull(data.getRRMPolicyMinRatio())) {
                rrmPolicyRatioModel.setrRMPolicyMinRatio(data.getRRMPolicyMinRatio().intValue());
            }
            if (Objects.nonNull(data.getRRMPolicyDedicatedRatio())) {
                rrmPolicyRatioModel.setrRMPolicyDedicatedRatio(data.getRRMPolicyDedicatedRatio().intValue());
            }
            // rrmPolicyRatioModel.setResourceID("");
            // rrmPolicyRatioModel.setSliceType("");
            WebsocketClient websocketClient = configurationHandler.getWebsocketClient();
            DeviceData deviceData = new DeviceData();
            try {
                ObjectMapper Obj = new ObjectMapper();
                String message = Obj.writeValueAsString(rrmPolicyRatioModel);
                LOG.info("parsed message: " + message);
                deviceData.setMessage(message);
            } catch (JsonProcessingException jsonProcessingException) {
                LOG.error("Error parsing json");
            }
            deviceData.setMessageType(MessageType.HC_TO_RC_RRM_POLICY_DEL);
            websocketClient.sendMessage(deviceData);
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
            int idRRMPolicyRatioIndex = identifier.toString().indexOf("RRMPolicyRatioKey{_id=");
            String idRRMPolicyRatioSubStr =
                    identifier.toString().substring(idRRMPolicyRatioIndex + "RRMPolicyRatioKey{_id=".length());
            String idRRMPolicyRatio = idRRMPolicyRatioSubStr.substring(0, idRRMPolicyRatioSubStr.indexOf("}"));

            RRMPolicyRatioModel rrmPolicyRatioModel = new RRMPolicyRatioModel();
            rrmPolicyRatioModel.setRrmPolicyID(idRRMPolicyRatio);
            if (Objects.nonNull(dataNew.getResourceType())) {
                rrmPolicyRatioModel.setResourceType(dataNew.getResourceType());
            }
            List<RRMPolicyMember> rrmPolicyMemberList = new ArrayList<RRMPolicyMember>();
            List<RRMPolicyMemberList> rRMPolicyMemberDataList = dataNew.getRRMPolicyMemberList();
            for (RRMPolicyMemberList rrmp : rRMPolicyMemberDataList) {
                RRMPolicyMember rrmPolicyMember = new RRMPolicyMember();
                String mcc = rrmp.getMcc().toString().substring(11, rrmp.getMcc().toString().length() - 1);
                String mnc = rrmp.getMnc().toString().substring(11, rrmp.getMnc().toString().length() - 1);
                rrmPolicyMember.setpLMNId(mcc + "-" + mnc);
                rrmPolicyMember
                        .setsNSSAI(rrmp.getSNSSAI().toString().substring(14, rrmp.getSNSSAI().toString().length() - 1));
                rrmPolicyMemberList.add(rrmPolicyMember);
            }
            rrmPolicyRatioModel.setrRMPolicyMemberList(rrmPolicyMemberList);
            if (Objects.nonNull(dataNew.getQuotaType())) {
                rrmPolicyRatioModel.setQuotaType(dataNew.getQuotaType().toString());
            }
            if (Objects.nonNull(dataNew.getRRMPolicyMaxRatio())) {
                rrmPolicyRatioModel.setrRMPolicyMaxRatio(dataNew.getRRMPolicyMaxRatio().intValue());
            }
            if (Objects.nonNull(dataNew.getRRMPolicyMinRatio())) {
                rrmPolicyRatioModel.setrRMPolicyMinRatio(dataNew.getRRMPolicyMinRatio().intValue());
            }
            if (Objects.nonNull(dataNew.getRRMPolicyDedicatedRatio())) {
                rrmPolicyRatioModel.setrRMPolicyDedicatedRatio(dataNew.getRRMPolicyDedicatedRatio().intValue());
            }
            // rrmPolicyRatioModel.setResourceID("");
            // rrmPolicyRatioModel.setSliceType("");
            try {
                ObjectMapper Obj = new ObjectMapper();
                String message = Obj.writeValueAsString(rrmPolicyRatioModel);
                LOG.info("parsed message: " + message);
                configurationHandler.sendDatabaseUpdate(message, MessageType.HC_TO_RC_RRM_POLICY);
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
