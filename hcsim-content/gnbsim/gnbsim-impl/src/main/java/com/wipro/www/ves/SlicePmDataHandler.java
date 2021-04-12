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

package com.wipro.www.ves;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.StringReader;
import java.io.ByteArrayOutputStream;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.www.InMemoryDataTree;
import com.wipro.www.ves.model.CommonEventHeader;
import com.wipro.www.ves.model.Event;
import com.wipro.www.ves.model.EventMessage;
import com.wipro.www.ves.model.Measurement;
import com.wipro.www.ves.model.VesMessage;
import com.wipro.www.ves.restclient.HttpRequester;
import com.wipro.www.websocket.models.AdditionalMeasurements;
import com.wipro.www.ves.model.NotificationFields;
import com.wipro.www.ves.model.NamedHashMap;
import com.wipro.www.websocket.models.EventSlicePm;
import com.wipro.www.websocket.models.SlicingPmMessage;
import com.wipro.www.ves.model.xml.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SlicePmDataHandler {

    private static final Logger LOG = LoggerFactory
            .getLogger(SlicePmDataHandler.class);

    private String vesEventListenerUrl;

    public SlicePmDataHandler(String vesEventListenerUrl){
        this.vesEventListenerUrl = vesEventListenerUrl;
    }

    public void handleSlicePmData(SlicingPmMessage slicePmdata) throws Exception {

        VesMessage vesMsg = new VesMessage();
        ObjectMapper mapper = new ObjectMapper();

//        List<EventMessage> evtMsgs = new ArrayList<EventMessage>();
//        List<EventSlicePm> EventList = slicePmdata.getEventPmList();


//        if(EventList.size() == 0) {
          if(slicePmdata == null) {
            LOG.info("Empty Slice PM Message");
            return;
          }
          LOG.info("PM Message :{} ", slicePmdata.getPmData());

        //for (EventSlicePm pmEvent : slicePmdata.getEventPmList()) {
            
            Event event = convertToVesFormatSlicePmData(slicePmdata);
            EventMessage evMsg = new EventMessage();
            evMsg.setEvent(event);
            //evtMsgs.add(evMsg);

            String requestBody = mapper.writeValueAsString(evMsg);
            LOG.info("SlicePmDataHandler requestBody: {}", requestBody);

            LOG.info("Slice Pm url: {}", vesEventListenerUrl);
            String response = HttpRequester.sendPostRequest(vesEventListenerUrl, requestBody);
            // "http://10.143.125.158:8080/eventListener/v5"
            LOG.info("SlicePmDataHandler Response: {}", response);
        //}
        //vesMsg.setEvents(evtMsgs);

        //String requestBody = mapper.writeValueAsString(vesMsg);
        //LOG.info("SlicePmDataHandler requestBody: {}", requestBody);

        //String url = ConfigJsonHandler.getConfigJsonHandler(null)
        //        .getModuleConfiguration().vesEventListenerUrl;
        //String url = "url";
        //LOG.info("url: {}", url);
        //String response = HttpRequester.sendPostRequest(url, requestBody);
        // "http://10.143.125.158:8080/eventListener/v5"
        //LOG.info("SlicePmDataHandler Response: {}", response);

    }


  //  Event convertToVesFormatSlicePmData(EventSlicePm slicePmdata) {
      Event convertToVesFormatSlicePmData(SlicingPmMessage slicePmdata) {


	try{
	//Prepare pmfile content
	String pmMessage = slicePmdata.getPmData();
	
	//String pmMessage = "{\"sourceName\":\"cucpserver1\",\"fileName\":\"./A2021-01-05T09-32-02.757-2021-01-05T09-32-02.758-3360-cucpserver1.xml\",\"startEpochMicrosec\":1609839122757000,\"lastEpochMicrosec\":1609839122758000,\"pmData\":\"MeasCollecFile [fileHeader\\u003dFileHeader [dnPrefix\\u003dPrefix, vendorName\\u003dAcme Ltd, fileFormatVersion\\u003d32.435 V10.0, measCollec\\u003dMeasCollec [beginTime\\u003d2021-01-05T09:32:02.757], fileSender\\u003dFileSender [localDn\\u003dcucpserver1]], measData\\u003d[MeasData [managedElement\\u003dManagedElement [swVersion\\u003dr0.1, localDn\\u003dcucpserver1], measInfo\\u003d[MeasInfo [measInfoId\\u003dmeasInfoIsVal, job\\u003dJob [jobId\\u003d3360], granPeriod\\u003dGranularityPeriod [endTime\\u003d2021-01-05T09:32:02.758, duration\\u003dPT900S], repPeriod\\u003dReportingPeriod [duration\\u003dPT900S], measType\\u003d[MeasType [measType\\u003dSM.PDUSessionSetupReq.0011-0010, p\\u003d1], MeasType [measType\\u003dSM.PDUSessionSetupSucc.0011-0010, p\\u003d2], MeasType [measType\\u003dSM.PDUSessionSetupFail.0, p\\u003d3], MeasType [measType\\u003dSM.PDUSessionSetupReq.0010-1110, p\\u003d4], MeasType [measType\\u003dSM.PDUSessionSetupSucc.0010-1110, p\\u003d5]], measValue\\u003d[MeasValue [measObjLdn\\u003d13999, r\\u003d[Result [p\\u003d4, measValue\\u003d3244], Result [p\\u003d5, measValue\\u003d2038], Result [p\\u003d1, measValue\\u003d1676], Result [p\\u003d2, measValue\\u003d1235]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d14000, r\\u003d[Result [p\\u003d4, measValue\\u003d2560], Result [p\\u003d5, measValue\\u003d1563], Result [p\\u003d1, measValue\\u003d1539], Result [p\\u003d2, measValue\\u003d1051]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15155, r\\u003d[Result [p\\u003d4, measValue\\u003d6122], Result [p\\u003d5, measValue\\u003d4344], Result [p\\u003d1, measValue\\u003d4694], Result [p\\u003d2, measValue\\u003d3514]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15174, r\\u003d[Result [p\\u003d4, measValue\\u003d8366], Result [p\\u003d5, measValue\\u003d6029], Result [p\\u003d1, measValue\\u003d5650], Result [p\\u003d2, measValue\\u003d3566]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15175, r\\u003d[Result [p\\u003d4, measValue\\u003d7150], Result [p\\u003d5, measValue\\u003d4609], Result [p\\u003d1, measValue\\u003d6204], Result [p\\u003d2, measValue\\u003d3915]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15176, r\\u003d[Result [p\\u003d4, measValue\\u003d3418], Result [p\\u003d5, measValue\\u003d2094], Result [p\\u003d1, measValue\\u003d1761], Result [p\\u003d2, measValue\\u003d1230]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15289, r\\u003d[Result [p\\u003d4, measValue\\u003d7239], Result [p\\u003d5, measValue\\u003d5196], Result [p\\u003d1, measValue\\u003d4886], Result [p\\u003d2, measValue\\u003d2949]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15290, r\\u003d[Result [p\\u003d4, measValue\\u003d8145], Result [p\\u003d5, measValue\\u003d5956], Result [p\\u003d1, measValue\\u003d5766], Result [p\\u003d2, measValue\\u003d4125]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15296, r\\u003d[Result [p\\u003d4, measValue\\u003d6221], Result [p\\u003d5, measValue\\u003d3831], Result [p\\u003d1, measValue\\u003d5880], Result [p\\u003d2, measValue\\u003d4028]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15425, r\\u003d[Result [p\\u003d4, measValue\\u003d2181], Result [p\\u003d5, measValue\\u003d1334], Result [p\\u003d1, measValue\\u003d1441], Result [p\\u003d2, measValue\\u003d978]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15426, r\\u003d[Result [p\\u003d4, measValue\\u003d2665], Result [p\\u003d5, measValue\\u003d1651], Result [p\\u003d1, measValue\\u003d2537], Result [p\\u003d2, measValue\\u003d1632]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15687, r\\u003d[Result [p\\u003d4, measValue\\u003d7653], Result [p\\u003d5, measValue\\u003d5562], Result [p\\u003d1, measValue\\u003d5695], Result [p\\u003d2, measValue\\u003d3947]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15689, r\\u003d[Result [p\\u003d4, measValue\\u003d6939], Result [p\\u003d5, measValue\\u003d4592], Result [p\\u003d1, measValue\\u003d4764], Result [p\\u003d2, measValue\\u003d3018]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15825, r\\u003d[Result [p\\u003d4, measValue\\u003d3318], Result [p\\u003d5, measValue\\u003d2323], Result [p\\u003d1, measValue\\u003d2396], Result [p\\u003d2, measValue\\u003d1727]], suspect\\u003dfalse], MeasValue [measObjLdn\\u003d15826, r\\u003d[Result [p\\u003d4, measValue\\u003d3025], Result [p\\u003d5, measValue\\u003d2235], Result [p\\u003d1, measValue\\u003d1460], Result [p\\u003d2, measValue\\u003d950]], suspect\\u003dfalse]]]]]], fileFooter\\u003dFileFooter [measCollec\\u003dorg.onap.ransim.rest.xml.models.MeasCollecEnd@55de39ba]]\"}";

	LOG.info("Received PM message : {} " , pmMessage);
	//JAXBContext jaxbContext = JAXBContext.newInstance(MeasCollecFile.class);
	//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	//MeasCollecFile measCollecFile = (MeasCollecFile) jaxbUnmarshaller.unmarshal(new StringReader(pmMessage));
        MeasCollecFile measCollecFile = new Gson().fromJson(pmMessage, MeasCollecFile.class);	

        JAXBContext jaxbContext = JAXBContext.newInstance(MeasCollecFile.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
	jaxbMarshaller.setProperty("com.sun.xml.bind.xmlHeaders","<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	jaxbMarshaller.marshal(measCollecFile, new File("/tmp/pmDataFile"));
        jaxbMarshaller.marshal(measCollecFile, System.out);
	LOG.info("PM data in measCollectFile : {} ", measCollecFile.toString());

	} catch (Exception e) {
		LOG.info("Exception occurred : {} " + e);
	}

        Event pmEvent = new Event();
        CommonEventHeader eventHeader = new CommonEventHeader();
	String pmFileName = slicePmdata.getFileName();
            
        eventHeader.setDomain("notification");

        String evId = "FileReady_";
        eventHeader.setEventId(evId);
        eventHeader.setPriority("Normal");
        eventHeader.setSequence((long) 0);
        eventHeader.setSourceName(slicePmdata.getSourceName());
        eventHeader.setStartEpochMicrosec(slicePmdata.getStartEpochMicrosec());
        eventHeader.setLastEpochMicrosec(slicePmdata.getLastEpochMicrosec());      
        eventHeader.setVersion("4.0.1");
        eventHeader.setEventName("Notification_RnNode-Slicing_FileReady");
        eventHeader.setVesEventListenerVersion("7.0.1");
        eventHeader.setTimeZoneOffset("UTC+05:30");

        NotificationFields notificationfields = new NotificationFields();
        notificationfields.setChangeIdentifier("PM_MEAS_FILES");
        notificationfields.setChangeType("FileReady");
        notificationfields.setNotificationFieldsVersion("2.0");

        List<NamedHashMap> arrayOfNamedHashMap = new ArrayList<NamedHashMap>();
        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("location", "ftpes://192.168.0.101:22/ftp/rop/"+pmFileName+".bin.gz");
        hashMap.put("compression","gzip");
        hashMap.put("fileFormatType","org.3GPP.32.435#measCollec");
        hashMap.put("fileFormatVersion","V10");
        NamedHashMap namedHashMap = new NamedHashMap(); 
        namedHashMap.setName("A20161224.1045-1100.bin.gz");
        namedHashMap.setHashMap(hashMap);
        arrayOfNamedHashMap.add(namedHashMap); 
        notificationfields.setArrayOfNamedHashMap(arrayOfNamedHashMap); 
        pmEvent.setCommonEventHeader(eventHeader);
        pmEvent.setNotificationFields(notificationfields);
        return pmEvent;
    }

}
