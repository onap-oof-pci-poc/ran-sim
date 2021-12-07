/*
 * Copyright (C) 2018 Wipro Limited.
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

package org.onap.ransim.ves.restclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequester {

    private static final String ACCEPT = "Accept";
    private static final String JSON = "application/json";
    private static final String CONTENT = "Content-Type";
    private static final String UTF = "UTF-8";
    private static final String FAILMSG = "Post failed";
    private static final String AUTH = "Authorization";

    private static final Logger LOG = LoggerFactory
            .getLogger(HttpRequester.class);

    /**
     * Send Post Request.
     */
    public static String sendPostRequest(String requestUrl, String requestBody) {
        String response = "";
        HttpURLConnection connection = null;
        BufferedReader br = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty(ACCEPT, JSON);
            connection.setRequestProperty(CONTENT, JSON);
            OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream(), UTF);
            writer.write(requestBody);
            writer.close();
            br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String temp;
            int responseCode = connection.getResponseCode();
            LOG.info("response code: {}", responseCode);
            response = br.readLine();
            while ((temp = br.readLine()) != null) {
                response = response.concat(temp);
            }

            if (response == null) {
                response = String.valueOf(responseCode);
            }

        } catch (Exception e) {
            LOG.error("Exc in post {}", e.toString());
            response = FAILMSG;
        } finally {
            try{
                if(br != null)
                    br.close();
                if(connection != null)
                    connection.disconnect();
            }catch(Exception exToIgnore) {
                LOG.debug("Exc in finally block {}", exToIgnore.toString());
            }
        }

        return response;
    }

    /**
     * Send Get Request.
     */
    public static String sendGetRequest(String requestUrl) {
        String response = "";
        int returnCode = 0;
        HttpURLConnection connection = null;
        BufferedReader br = null;
        LOG.debug("Request URL is {}", requestUrl);
        try {
            URL url = new URL(requestUrl);
            connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty(ACCEPT, JSON);
            returnCode = connection.getResponseCode();
            LOG.info("response code: {}", returnCode);
            InputStream connectionIn = null;
            if (returnCode == 200) {
                connectionIn = connection.getInputStream();
                br = new BufferedReader(
                        new InputStreamReader(connectionIn));
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response = response.concat(inputLine);
                }
                br.close();
            }

            else {
                response = "";
                LOG.debug("return code: {}", returnCode);
            }
        } catch (Exception e) {
            LOG.debug("Get failed,Exception : {}", e.toString());
            LOG.error("Get failed,Exception : {}", e.toString());
            response = "";
        } finally {
            try{
                if(br != null)
                    br.close();
                if(connection != null)
                    connection.disconnect();
            }catch(Exception exToIgnore) {
                LOG.debug("Exc in finally block {}", exToIgnore.toString());
            }
        }
        return response;

    }
}
