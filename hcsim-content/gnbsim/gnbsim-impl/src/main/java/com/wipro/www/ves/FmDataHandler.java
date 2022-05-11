/*
 * Copyright (C) 2022 Wipro Limited.
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

package com.wipro.www.ves;

import java.util.ArrayList;
import java.util.List;

import com.wipro.www.ves.NumberUtil;
import com.wipro.www.ves.model.CommonEventHeader;
import com.wipro.www.ves.model.Event;
import com.wipro.www.ves.model.EventMessage;
import com.wipro.www.ves.model.FaultFields;
import com.wipro.www.ves.model.VesMessage;
import com.wipro.www.ves.restclient.HttpRequester;
import com.wipro.www.websocket.models.EventFm;
import com.wipro.www.websocket.models.FmMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FmDataHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FmDataHandler.class);
    private String vesEventListenerUrl;

    public FmDataHandler(String vesEventListenerUrl) {
	    this.vesEventListenerUrl = vesEventListenerUrl;
    }

    public void handleFmData(FmMessage fmdata) throws Exception {

        VesMessage vesMsg = new VesMessage();
        List<EventMessage> evtMsgs = new ArrayList<EventMessage>();
        List<EventFm> FmList = fmdata.getFmEventList();

        if(FmList.size() == 0) {
            LOG.info("FM Message is empty");
            return;
        }
        ObjectMapper mapper = new ObjectMapper();

        for (EventFm fmEvent : FmList) {
            Event event = convertToVesFormatFmData(fmEvent);
            EventMessage evMsg = new EventMessage();
            evMsg.setEvent(event);
            evtMsgs.add(evMsg);
            
            
	    String requestBody = mapper.writeValueAsString(evMsg);
            LOG.info("FmDataHandler for loop");
            if(requestBody.isEmpty()){
                LOG.info("FmDataHandler requestBody: {}", requestBody);
                LOG.info("Fm request body not null");
            }
            else
            {
                  LOG.info("Fm request body in null");
            } 
            LOG.info("Fm url: {}", vesEventListenerUrl);
            String response = HttpRequester.sendPostRequest(vesEventListenerUrl, requestBody);
            // "http://10.143.125.158:8080/eventListener/v5"
            LOG.info("FmDataHandler Response: {}", response);
            
        }
        vesMsg.setEvents(evtMsgs);
        /*
        //ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(vesMsg);
        LOG.info("FmDataHandler requestBody: {}", requestBody);

        String url = ConfigJsonHandler.getConfigJsonHandler(null)
                .getModuleConfiguration().vesEventListenerUrl;
        LOG.info("url: {}", url);
        String response = HttpRequester.sendPostRequest(url, requestBody);
        // "http://10.143.125.158:8080/eventListener/v5"
        LOG.info("FmDataHandler Response: {}", response);
        */
    }

    Event convertToVesFormatFmData(EventFm fmdata) {
        Event fmEvent = new Event();
        CommonEventHeader eventHeader = new CommonEventHeader();

        eventHeader.setDomain("fault");
        String evId = "fault" + NumberUtil.intToString(NumberUtil.getAndIncreamentFaultEventIdSequence(), 6);
        eventHeader.setEventId(evId);
        eventHeader.setPriority("High");
        eventHeader.setReportingEntityName(fmdata.getCommonEventHeader().getReportingEntityName());
        //eventHeader.setReportingEntityId(ConfigJsonHandler.PnfUuid);
        eventHeader.setSequence((long) 1);
        eventHeader.setSourceId(fmdata.getCommonEventHeader().getSourceUuid());
        eventHeader.setSourceName(fmdata.getCommonEventHeader().getSourceName());
        eventHeader.setStartEpochMicrosec((double) fmdata.getCommonEventHeader().getStartEpochMicrosec());
        eventHeader.setLastEpochMicrosec((double) fmdata.getCommonEventHeader().getLastEpochMicrosec());
        eventHeader.setVersion("4.0.1");
        eventHeader.setEventName("Fault_RansimAgent-Wipro_RanPCIProblem");
        eventHeader.setNfNamingCode("RansimAgent");
        eventHeader.setNfVendorName("Wipro");
        eventHeader.setVesEventListenerVersion("7.2");
        eventHeader.setTimeZoneOffset("UTC+05:30");


        FaultFields faultfields = new FaultFields();
        faultfields
        .setAlarmCondition(fmdata.getFaultFields().getAlarmCondition());
        faultfields.setEventSeverity(fmdata.getFaultFields().getEventSeverity());
        faultfields.setEventSourceType(fmdata.getFaultFields().getEventSourceType());
        faultfields.setFaultFieldsVersion("4.0");
        faultfields
        .setSpecificProblem(fmdata.getFaultFields().getSpecificProblem());
        faultfields.setVfStatus("Active");
        faultfields.setAlarmAdditionalInformation(fmdata.getFaultFields().getAlarmAdditionalInformation());

        fmEvent.setFaultFields(faultfields);
        fmEvent.setCommonEventHeader(eventHeader);
        return fmEvent;
    }

}
