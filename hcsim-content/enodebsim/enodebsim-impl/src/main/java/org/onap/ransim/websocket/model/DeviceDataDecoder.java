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

package org.onap.ransim.websocket.model;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class DeviceDataDecoder implements Decoder.Text<DeviceData> {

    // public class DeviceDataDecoder implements Decoder.Text<DeviceData>
    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig arg0) {
    }

    @Override
    public DeviceData decode(String msgInfo) throws DecodeException {
        DeviceData data = new DeviceData();
        String[] strInfo = msgInfo.split(":", 2);
        if (strInfo.length < 2) {
            data.setMessage("");
        } else {
            data.setMessage(strInfo[1]);
        }
        data.setType(strInfo[0]);
        return data;
    }

    @Override
    public boolean willDecode(String arg0) {
        // TODO Auto-generated method stub
        return true;
    }

}
