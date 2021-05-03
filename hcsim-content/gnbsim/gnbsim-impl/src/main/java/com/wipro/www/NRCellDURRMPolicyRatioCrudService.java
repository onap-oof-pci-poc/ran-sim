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

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.RRMPolicyRatio;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.RRMPolicyRatioKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.RRMPolicyRatioBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.rrmpolicy_group.RRMPolicyMemberList;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.www.websocket.WebsocketClient;
import com.wipro.www.websocket.models.DeviceData;
import com.wipro.www.websocket.models.MessageType;
import com.wipro.www.websocket.models.RRMPolicyMember;
import com.wipro.www.websocket.models.RRMPolicyRatioModel;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NRCellDURRMPolicyRatioCrudService implements CrudService<RRMPolicyRatio> {

    private static final Logger LOG = LoggerFactory.getLogger(NRCellDURRMPolicyRatioCrudService.class);

    @Override
    public void writeData(@Nonnull InstanceIdentifier<RRMPolicyRatio> identifier, @Nonnull RRMPolicyRatio data) throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Writing path[{}] / data [{}]", identifier, data);
	    RRMPolicyRatioModel rrmPolicyRatioModel = new RRMPolicyRatioModel();
            rrmPolicyRatioModel.setRrmPolicyID(Integer.parseInt(data.getId()));
            rrmPolicyRatioModel.setResourceType(data.getAttributes().getResourceType());
            List<RRMPolicyMember> rrmPolicyMemberList = new ArrayList<RRMPolicyMember>();
            List<RRMPolicyMemberList> rRMPolicyMemberDataList = data.getAttributes().getRRMPolicyMemberList();
            for (RRMPolicyMemberList rrmp : rRMPolicyMemberDataList) {
                  RRMPolicyMember rrmPolicyMember = new RRMPolicyMember();
                  String mcc = rrmp.getMcc().toString().substring(11,rrmp.getMcc().toString().length()-1);
                  String mnc = rrmp.getMnc().toString().substring(11,rrmp.getMnc().toString().length()-1);
                  rrmPolicyMember.setpLMNId(mcc + "-" + mnc);
                  rrmPolicyMember.setsNSSAI(
                  rrmp.getSNSSAI().toString().substring(14, rrmp.getSNSSAI().toString().length() - 1));
                  rrmPolicyMemberList.add(rrmPolicyMember);
            }
            rrmPolicyRatioModel.setrRMPolicyMemberList(rrmPolicyMemberList);
            rrmPolicyRatioModel.setQuotaType(data.getAttributes().getQuotaType().toString());
            rrmPolicyRatioModel.setrRMPolicyMaxRatio(data.getAttributes().getRRMPolicyMaxRatio().intValue());
            rrmPolicyRatioModel.setrRMPolicyMinRatio(data.getAttributes().getRRMPolicyMinRatio().intValue());
            rrmPolicyRatioModel.setrRMPolicyDedicatedRatio(data.getAttributes().getRRMPolicyDedicatedRatio().intValue());
            rrmPolicyRatioModel.setResourceID("");
            rrmPolicyRatioModel.setSliceType("");
            ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
            try{
                 ObjectMapper Obj = new ObjectMapper();
                 String message = Obj.writeValueAsString(rrmPolicyRatioModel);
                 LOG.info("parsed message: " + message);
		 configurationHandler.sendDatabaseUpdate(message,MessageType.HC_TO_RC_RRM_POLICY);
            }catch(JsonProcessingException jsonProcessingException){
                 LOG.error("Error parsing json");
            }

        } else {
            throw new WriteFailedException.CreateFailedException(identifier, data,
                    new NullPointerException("Provided data are null"));
        }
    }

    @Override
    public void deleteData(@Nonnull InstanceIdentifier<RRMPolicyRatio> identifier, @Nonnull RRMPolicyRatio data) throws WriteFailedException {
        if (data != null) {

            // identifier.firstKeyOf(SomeClassUpperInHierarchy.class) can be used to identify
            // relationships such as to which parent these data are related to

            // Performs any logic needed for persisting such data
            LOG.info("Removing path[{}] / data [{}]", identifier, data);
            RRMPolicyRatioModel rrmPolicyRatioModel = new RRMPolicyRatioModel();
            rrmPolicyRatioModel.setRrmPolicyID(Integer.parseInt(data.getId()));
            rrmPolicyRatioModel.setResourceType(data.getAttributes().getResourceType());
            List<RRMPolicyMember> rrmPolicyMemberList = new ArrayList<RRMPolicyMember>();
            List<RRMPolicyMemberList> rRMPolicyMemberDataList = data.getAttributes().getRRMPolicyMemberList();
            for (RRMPolicyMemberList rrmp : rRMPolicyMemberDataList) {
                  RRMPolicyMember rrmPolicyMember = new RRMPolicyMember();
                  String mcc = rrmp.getMcc().toString().substring(11,rrmp.getMcc().toString().length()-1);
                  String mnc = rrmp.getMnc().toString().substring(11,rrmp.getMnc().toString().length()-1);
                  rrmPolicyMember.setpLMNId(mcc + "-" + mnc);
                  rrmPolicyMember.setsNSSAI(
                  rrmp.getSNSSAI().toString().substring(14, rrmp.getSNSSAI().toString().length() - 1));
                  rrmPolicyMemberList.add(rrmPolicyMember);
            }
            rrmPolicyRatioModel.setrRMPolicyMemberList(rrmPolicyMemberList);
            rrmPolicyRatioModel.setQuotaType(data.getAttributes().getQuotaType().toString());
            rrmPolicyRatioModel.setrRMPolicyMaxRatio(data.getAttributes().getRRMPolicyMaxRatio().intValue());
            rrmPolicyRatioModel.setrRMPolicyMinRatio(data.getAttributes().getRRMPolicyMinRatio().intValue());
            rrmPolicyRatioModel.setrRMPolicyDedicatedRatio(data.getAttributes().getRRMPolicyDedicatedRatio().intValue());
            rrmPolicyRatioModel.setResourceID("");
            rrmPolicyRatioModel.setSliceType("");
            WebsocketClient websocketClient = ConfigurationHandler.getInstance().getWebsocketClient();
            DeviceData deviceData = new DeviceData();
            try{
                 ObjectMapper Obj = new ObjectMapper();
                 String message = Obj.writeValueAsString(rrmPolicyRatioModel);
                 LOG.info("parsed message: " + message);
                 deviceData.setMessage(message);
            }catch(JsonProcessingException jsonProcessingException){
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
    public void updateData(@Nonnull InstanceIdentifier<RRMPolicyRatio> identifier, @Nonnull RRMPolicyRatio dataOld, @Nonnull RRMPolicyRatio dataNew) throws WriteFailedException {
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
    public RRMPolicyRatio readSpecific(@Nonnull InstanceIdentifier<RRMPolicyRatio> identifier) throws ReadFailedException {

        LOG.info("Read path[{}] ", identifier);
        return null;
    }

    @Override
    public List<RRMPolicyRatio> readAll() throws ReadFailedException {
        return null;
    }
}
