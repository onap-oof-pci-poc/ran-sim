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

package com.wipro.www.ves.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "hashMap",
        // "location",
        // "compression",
        // "fileFormatType",
        // "fileFormatVersion"

})

public class NamedHashMap {

    @JsonProperty("hashMap")
    private Map<String, String> hashMap;
    @JsonProperty("name")
    private String name;

    /*
     * @JsonProperty("location")
     * private String location;
     * 
     * @JsonProperty("compression")
     * private String compression;
     * 
     * @JsonProperty("fileFormatType")
     * private String fileFormatType;
     * 
     * @JsonProperty("fileFormatVersion")
     * private String fileFormatVersion;
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("hashMap")
    public Map<String, String> getHashMap() {
        return hashMap;
    }

    @JsonProperty("hashMap")
    public void setHashMap(Map<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    @Override
    public String toString() {
        return "NotificationFields [name=" + name + ", hashMap=" + hashMap + "]";
    }

    /*
     * @JsonProperty("location")
     * public String getLocation() {
     * if(hashMap.containsKey("location")) {
     * return hashMap.get("location");
     * }
     * return "";
     * }
     * 
     * 
     * @JsonProperty("location")
     * public void setLocation(String location) {
     * hashMap.put("location", location);
     * }
     * 
     * @JsonProperty("compression")
     * public String getCompression() {
     * if(hashMap.containsKey("compression")) {
     * return hashMap.get("compression");
     * }
     * return "";
     * }
     * 
     * @JsonProperty("compression")
     * public void setCompression(String compression) {
     * hashMap.put("compression",compression);
     * }
     * 
     * @JsonProperty("fileFormatType")
     * public String getFileFormatType() {
     * if(hashMap.containsKey("fileFormatType")) {
     * return hashMap.get("fileFormatType");
     * }
     * return "";
     * }
     * 
     * @JsonProperty("fileFormatType")
     * public void setFileFormatType(String fileFormatType) {
     * hashMap.put("fileFormatType",fileFormatType);
     * }
     * 
     * @JsonProperty("fileFormatVersion")
     * public String getFileFormatVersion() {
     * if(hashMap.containsKey("fileFormatVersion")) {
     * return hashMap.get("fileFormatVersion");
     * }
     * return "";
     * }
     * 
     * @JsonProperty("fileFormatVersion")
     * public void setFileFormatVersion(String fileFormatVersion) {
     * hashMap.put("fileFormatVersion",fileFormatVersion);
     * }
     */
}
