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

package com.wipro.www.websocket;

import com.wipro.www.websocket.models.DeviceData;
import com.wipro.www.websocket.models.MessageType;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class DeviceDataDecoder implements Decoder.Text<DeviceData> {

    @Override
    public DeviceData decode(String s) throws DecodeException {
        DeviceData deviceData = new DeviceData();
        String[] msg = s.split(":",2);
        if (msg.length < 2){
            deviceData.setMessage("");
        }
        else{
            deviceData.setMessage(msg[1]);
        }
        deviceData.setMessageType(MessageType.valueOf(msg[0]));
        return deviceData;
    }


    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
