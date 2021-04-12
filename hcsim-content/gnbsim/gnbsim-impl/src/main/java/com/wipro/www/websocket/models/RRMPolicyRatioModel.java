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
package com.wipro.www.websocket.models;

import java.util.List;

public class RRMPolicyRatioModel{

        private Integer rrmPolicyID;
        private String resourceID;
        private String resourceType;
        private String sliceType;
        private List<RRMPolicyMember> rRMPolicyMemberList;
        private String quotaType;
        private Integer rRMPolicyMaxRatio;
        private Integer rRMPolicyMinRatio;
        private Integer rRMPolicyDedicatedRatio;
        public Integer getRrmPolicyID() {
                return rrmPolicyID;
        }
        public void setRrmPolicyID(Integer rrmPolicyID) {
                this.rrmPolicyID = rrmPolicyID;
        }
        public String getResourceID() {
                return resourceID;
        }
        public void setResourceID(String resourceID) {
                this.resourceID = resourceID;
        }
        public String getResourceType() {
                return resourceType;
        }
        public void setResourceType(String resourceType) {
                this.resourceType = resourceType;
        }
        public String getSliceType() {
                return sliceType;
        }
        public void setSliceType(String sliceType) {
                this.sliceType = sliceType;
        }
        public List<RRMPolicyMember> getrRMPolicyMemberList() {
                return rRMPolicyMemberList;
        }
        public void setrRMPolicyMemberList(List<RRMPolicyMember> rRMPolicyMemberList) {
                this.rRMPolicyMemberList = rRMPolicyMemberList;
        }
        public String getQuotaType() {
                return quotaType;
        }
        public void setQuotaType(String quotaType) {
                this.quotaType = quotaType;
        }
        public Integer getrRMPolicyMaxRatio() {
                return rRMPolicyMaxRatio;
        }
        public void setrRMPolicyMaxRatio(Integer rRMPolicyMaxRatio) {
                this.rRMPolicyMaxRatio = rRMPolicyMaxRatio;
        }
        public Integer getrRMPolicyMinRatio() {
                return rRMPolicyMinRatio;
        }
        public void setrRMPolicyMinRatio(Integer rRMPolicyMinRatio) {
                this.rRMPolicyMinRatio = rRMPolicyMinRatio;
        }
        public Integer getrRMPolicyDedicatedRatio() {
                return rRMPolicyDedicatedRatio;
        }
        public void setrRMPolicyDedicatedRatio(Integer rRMPolicyDedicatedRatio) {
                this.rRMPolicyDedicatedRatio = rRMPolicyDedicatedRatio;
        }
}
