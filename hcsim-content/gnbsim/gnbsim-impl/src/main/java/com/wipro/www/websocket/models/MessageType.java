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


public enum MessageType {
    INITIAL_CONFIG, RC_TO_HC_PMFILEDATA, ALLOCATION_UPDATE, PING, SLICE_DETAILS, NSSAI_DETAILS, HC_TO_RC_RRM_POLICY,
    HC_TO_RC_PLMN, HC_TO_RC_SLICE_PROFILE, HC_TO_RC_RRM_POLICY_DEL, HC_TO_RC_PLMN_DEL, HC_TO_RC_SLICE_PROFILE_DEL, RTRIC_CONFIG 
}
