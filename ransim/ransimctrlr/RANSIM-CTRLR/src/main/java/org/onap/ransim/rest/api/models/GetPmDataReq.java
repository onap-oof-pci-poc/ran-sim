/*-
 * ============LICENSE_START=======================================================
 * Ran Simulator Controller
 * ================================================================================
 * Copyright (C) 2018 Wipro Limited.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.ransim.rest.api.models;

public class GetPmDataReq {
    
    private String nodeIdBad;
    private String nodeIdPoor;
    
    public GetPmDataReq() {
        super();
    }
    
    /**
     * Constructor with all fields.
     * 
     * @param nodeIdBad
     *            Cells with bad PM value.
     * @param nodeIdPoor
     *            Cells with poor PM value.
     */
    public GetPmDataReq(String nodeIdBad, String nodeIdPoor) {
        super();
        this.nodeIdBad = nodeIdBad;
        this.nodeIdPoor = nodeIdPoor;
    }
    
    public String getNodeIdBad() {
        return nodeIdBad;
    }
    
    public void setNodeIdBad(String nodeIdBad) {
        this.nodeIdBad = nodeIdBad;
    }
    
    public String getNodeIdPoor() {
        return nodeIdPoor;
    }
    
    public void setNodeIdPoor(String nodeIdPoor) {
        this.nodeIdPoor = nodeIdPoor;
    }
    
}
