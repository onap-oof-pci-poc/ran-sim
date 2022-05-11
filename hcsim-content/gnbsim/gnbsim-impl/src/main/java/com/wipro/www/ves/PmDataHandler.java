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

import com.wipro.www.ConfigurationHandler;

import com.wipro.www.ves.NumberUtil;
import com.wipro.www.ves.model.CommonEventHeader;
import com.wipro.www.ves.model.Event;
import com.wipro.www.ves.model.EventMessage;
import com.wipro.www.ves.model.FaultFields;
import com.wipro.www.ves.model.VesMessage;
import com.wipro.www.ves.model.Measurement;
import com.wipro.www.ves.restclient.HttpRequester;
import com.wipro.www.websocket.models.AdditionalMeasurements;
import com.wipro.www.websocket.models.EventPm;
import com.wipro.www.websocket.models.PmMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PmDataHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PmDataHandler.class);
    private String vesEventListenerUrl;

    public PmDataHandler(String vesEventListenerUrl) {
	    this.vesEventListenerUrl = vesEventListenerUrl;
    }

    public void handlePmData(PmMessage pmdata) throws Exception {

        VesMessage vesMsg = new VesMessage();
        List<EventMessage> evtMsgs = new ArrayList<EventMessage>();
        List<EventPm> EventList = pmdata.getEventPmList();

        if(EventList.size() == 0) {
            LOG.info("Empty PM Message");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        for (EventPm pmEvent : EventList) {
            Event event = convertToVesFormatPmData(pmEvent);
            EventMessage evMsg = new EventMessage();
            evMsg.setEvent(event);
            evtMsgs.add(evMsg);

            String requestBody = mapper.writeValueAsString(evMsg);
            LOG.info("PmDataHandler requestBody: {}", requestBody);
            LOG.info("Pm url: {}", vesEventListenerUrl);
            String response = HttpRequester.sendPostRequest(vesEventListenerUrl, requestBody);
            // "http://10.143.125.158:8080/eventListener/v5"
            LOG.info("PmDataHandler Response: {}", response);

        }
        vesMsg.setEvents(evtMsgs);

        // ObjectMapper mapper = new ObjectMapper();
        /*
        String requestBody = mapper.writeValueAsString(vesMsg);
        LOG.info("PmDataHandler requestBody: {}", requestBody);

        String url = ConfigJsonHandler.getConfigJsonHandler(null)
                .getModuleConfiguration().vesEventListenerUrl;
        LOG.info("url: {}", url);
        String response = HttpRequester.sendPostRequest(url, requestBody);
        // "http://10.143.125.158:8080/eventListener/v5"
        LOG.info("PmDataHandler Response: {}", response);
         */
    }


    Event convertToVesFormatPmData(EventPm pmdata) {
        Event pmEvent = new Event();
        CommonEventHeader eventHeader = new CommonEventHeader();

        eventHeader.setDomain("measurement");
        String evId = "measurement" + NumberUtil.intToString(NumberUtil.getAndIncreamentMeasEventIdSequence(), 6);
        eventHeader.setEventId(evId);
        eventHeader.setPriority("Normal");
        eventHeader.setReportingEntityName(pmdata.getCommonEventHeader().getReportingEntityName());
        //eventHeader.setReportingEntityId(ConfigJsonHandler.PnfUuid);
        eventHeader.setSequence((long) 0);
        eventHeader.setSourceId(pmdata.getCommonEventHeader().getSourceUuid());
        eventHeader.setSourceName(pmdata.getCommonEventHeader().getSourceName());
        eventHeader.setStartEpochMicrosec((double) pmdata.getCommonEventHeader().getStartEpochMicrosec());
        eventHeader.setLastEpochMicrosec((double) pmdata.getCommonEventHeader().getLastEpochMicrosec());
        eventHeader.setVersion("4.0.1");
        eventHeader.setEventName("Measurement_RansimAgent-Wipro_HandoffMetric");
        eventHeader.setNfNamingCode("RansimAgent");
        eventHeader.setNfVendorName("Wipro");
        eventHeader.setVesEventListenerVersion("7.2");
        eventHeader.setTimeZoneOffset("UTC+05:30");

        Measurement measurement = new Measurement();
        measurement.setMeasurementInterval(pmdata.getMeasurement().getMeasurementInterval());
        measurement.setMeasurementFieldsVersion("4.0");
        List<AdditionalMeasurements> rcAddlMeass = pmdata.getMeasurement().getAdditionalMeasurements();
        List<com.wipro.www.ves.model.AdditionalMeasurements> vesAddlMeas = new ArrayList<com.wipro.www.ves.model.AdditionalMeasurements>();
        for (AdditionalMeasurements rcAddlMeasurement : rcAddlMeass) {
            com.wipro.www.ves.model.AdditionalMeasurements addlMeas = new com.wipro.www.ves.model.AdditionalMeasurements();

            addlMeas.setHashMap(rcAddlMeasurement.getHashMap());
            addlMeas.setName(rcAddlMeasurement.getName());

            vesAddlMeas.add(addlMeas);
        }
        measurement.setAdditionalMeasurements(vesAddlMeas);

        pmEvent.setMeasurement(measurement);
        pmEvent.setCommonEventHeader(eventHeader);
        return pmEvent;
    }

}
