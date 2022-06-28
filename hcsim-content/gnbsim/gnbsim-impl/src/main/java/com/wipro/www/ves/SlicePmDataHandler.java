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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.wipro.www.Configuration;
import com.wipro.www.InMemoryDataTree;
import com.wipro.www.ves.model.CommonEventHeader;
import com.wipro.www.ves.model.Event;
import com.wipro.www.ves.model.EventMessage;
import com.wipro.www.ves.model.Measurement;
import com.wipro.www.ves.model.NamedHashMap;
import com.wipro.www.ves.model.NotificationFields;
import com.wipro.www.ves.model.VesMessage;
import com.wipro.www.ves.model.xml.models.*;
import com.wipro.www.ves.restclient.HttpRequester;
import com.wipro.www.websocket.models.AdditionalMeasurements;
import com.wipro.www.websocket.models.EventSlicePm;
import com.wipro.www.websocket.models.SlicingPmMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlicePmDataHandler {

    // private static String SFTPHOST = (Configuration.getInstance().getHcIp()).trim();
    private static String SFTPHOST = (System.getenv("enodebsimIp")).trim();;
    private static int SFTPPORT = 2222;
    private static String SFTPUSER = "ubuntu";
    private static String SFTPPASS = "pass";
    private static String SFTPWORKINGDIR = "/sftptest/upload/";
    private static final Logger LOG = LoggerFactory.getLogger(SlicePmDataHandler.class);

    private String vesEventListenerUrl;

    public SlicePmDataHandler(String vesEventListenerUrl) {
        this.vesEventListenerUrl = vesEventListenerUrl;
    }

    public void handleSlicePmData(SlicingPmMessage slicePmdata) throws Exception {

        VesMessage vesMsg = new VesMessage();
        ObjectMapper mapper = new ObjectMapper();

        // List<EventMessage> evtMsgs = new ArrayList<EventMessage>();
        // List<EventSlicePm> EventList = slicePmdata.getEventPmList();

        // if(EventList.size() == 0) {
        if (slicePmdata == null) {
            LOG.info("Empty Slice PM Message");
            return;
        }
        LOG.info("PM Message :{} ", slicePmdata.getPmData());
        // LOG.info("SFTPhost: " + Configuration.getInstance().getHcIp().trim());
        // for (EventSlicePm pmEvent : slicePmdata.getEventPmList()) {

        Event event = convertToVesFormatSlicePmData(slicePmdata);
        EventMessage evMsg = new EventMessage();
        evMsg.setEvent(event);
        // evtMsgs.add(evMsg);

        String requestBody = mapper.writeValueAsString(evMsg);
        LOG.info("SlicePmDataHandler requestBody: {}", requestBody);

        LOG.info("Slice Pm url: {}", vesEventListenerUrl);
        String response = HttpRequester.sendPostRequest(vesEventListenerUrl, requestBody);
        // "http://10.143.125.158:8080/eventListener/v5"
        LOG.info("SlicePmDataHandler Response: {}", response);
        // }
        // vesMsg.setEvents(evtMsgs);

        // String requestBody = mapper.writeValueAsString(vesMsg);
        // LOG.info("SlicePmDataHandler requestBody: {}", requestBody);

        // String url = ConfigJsonHandler.getConfigJsonHandler(null)
        // .getModuleConfiguration().vesEventListenerUrl;
        // String url = "url";
        // LOG.info("url: {}", url);
        // String response = HttpRequester.sendPostRequest(url, requestBody);
        // "http://10.143.125.158:8080/eventListener/v5"
        // LOG.info("SlicePmDataHandler Response: {}", response);

    }

    public boolean pmFileTransfer(String fileName) {

        /*
         * int SFTPPORT = 2222;
         * String SFTPUSER = "ubuntu";
         * String SFTPPASS = "pass";
         * String SFTPWORKINGDIR = "/sftptest/upload/";
         */
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        LOG.info("preparing the host information for sftp.");

        try {
            JSch jsch = new JSch();
            InetAddress localhost = InetAddress.getLocalHost();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            LOG.info("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            File f = new File(fileName);
            String inputFile = "/tmp/" + fileName;
            String outputFile = fileName;
            LOG.info("inputFile: " + inputFile);
            LOG.info("outputFile: " + outputFile);
            channelSftp.put(inputFile, outputFile);
            LOG.info("File transfered successfully to host.");
        } catch (Exception ex) {
            LOG.info("Exception found while tranfer the response. " + ex);
            return false;
        } finally {
            channelSftp.exit();
            LOG.info("sftp Channel exited.");
            channel.disconnect();
            LOG.info("Channel disconnected.");
            session.disconnect();
            LOG.info("Host Session disconnected.");

        }
        return true;
    }

    Event convertToVesFormatSlicePmData(SlicingPmMessage slicePmdata) {
        String inputFile = slicePmdata.getFileName();
        String compressedFileName = inputFile + ".gz";
        try {
            // Prepare pmfile content
            String pmMessage = slicePmdata.getPmData();
            boolean pmFileTransferStatus = false;

            LOG.info("Received PM message : {} ", pmMessage);
            MeasCollecFile measCollecFile = new Gson().fromJson(pmMessage, MeasCollecFile.class);

            JAXBContext jaxbContext = JAXBContext.newInstance(MeasCollecFile.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty("jaxb.encoding", "UTF-8");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            jaxbMarshaller.marshal(measCollecFile, new File("/tmp/" + inputFile));
            jaxbMarshaller.marshal(measCollecFile, System.out);

            LOG.info("PM data in measCollectFile : {} ", measCollecFile.toString());
            LOG.info("Compressing file");

            FileInputStream fileInput = new FileInputStream("/tmp/" + inputFile);
            FileOutputStream fileOutputStream = new FileOutputStream("/tmp/" + compressedFileName);
            GZIPOutputStream gzipOuputStream = new GZIPOutputStream(fileOutputStream);

            byte[] buffer = new byte[1024];
            int b;
            while ((b = fileInput.read(buffer)) > 0) {
                gzipOuputStream.write(buffer, 0, b);
            }
            fileInput.close();
            gzipOuputStream.finish();
            gzipOuputStream.close();
            LOG.info("Initiate SFTP transfer for PM data file");
            // String fileData = measCollecFile.toString();

            // byte b[]=fileData.getBytes();
            // gos.write(b);
            // gos.close();

            pmFileTransferStatus = pmFileTransfer(compressedFileName);

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
        eventHeader.setReportingEntityName(slicePmdata.getSourceName());
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
        Map<String, String> hashMap = new HashMap<String, String>();
        // hashMap.put("location", "ftpes://10.31.4.14:22/upload/" + compressedFileName);
        hashMap.put("location", "sftp://" + SFTPUSER + ":" + SFTPPASS + "@" + SFTPHOST + ":" + String.valueOf(SFTPPORT)
                + SFTPWORKINGDIR + compressedFileName);
        hashMap.put("compression", "gzip");
        hashMap.put("fileFormatType", "org.3GPP.32.435#measCollec");
        hashMap.put("fileFormatVersion", "V10");
        NamedHashMap namedHashMap = new NamedHashMap();
        namedHashMap.setName(compressedFileName);
        namedHashMap.setHashMap(hashMap);
        arrayOfNamedHashMap.add(namedHashMap);
        notificationfields.setArrayOfNamedHashMap(arrayOfNamedHashMap);
        pmEvent.setCommonEventHeader(eventHeader);
        pmEvent.setNotificationFields(notificationfields);
        LOG.info("compressed: " + compressedFileName);
        return pmEvent;
    }

}
