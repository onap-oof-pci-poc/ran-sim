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

public class Configuration {

    public String getRansimIp() {
        return ransimIp;
    }

    public void setRansimIp(String ransimIp) {
        this.ransimIp = ransimIp;
    }

    public int getRansimPort() {
        return ransimPort;
    }

    public void setRansimPort(int ransimPort) {
        this.ransimPort = ransimPort;
    }

    public String getHcIp() {
        return hcIp;
    }

    public void setHcIp(String hcIp) {
        this.hcIp = hcIp;
    }

    public int getHcPort() {
        return hcPort;
    }

    public void setHcPort(int hcPort) {
        this.hcPort = hcPort;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getVesEventListenerUrl() {
        return vesEventListenerUrl;
    }

    public void setVesEventListenerUrl(String vesEventListenerUrl) {
        this.vesEventListenerUrl = vesEventListenerUrl;
    }

    private Configuration(){

    }

    private static Configuration instance;

    public static Configuration getInstance() {
        if (instance ==null){
            instance = new Configuration();
        }
        return instance;
    }

    private String ransimIp;

    private int ransimPort;

    private String hcIp;

    private int hcPort;

    private String endpoint;

    private String vesEventListenerUrl;
}
