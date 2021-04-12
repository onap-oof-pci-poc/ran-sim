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

package com.wipro.www.ves.model.xml.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ManagedElement {
    private String swVersion;
    private String localDn;
//    private String userLabel;
   
    public ManagedElement() {

}
    public ManagedElement(String swVersion, String localDn) {
super();
this.swVersion = swVersion;
this.localDn = localDn;
//this.userLabel = userLabel;
}
@XmlAttribute
public String getSwVersion() {
return swVersion;
}
public void setSwVersion(String swVersion) {
this.swVersion = swVersion;
}
@XmlAttribute
public String getLocalDn() {
return localDn;
}
public void setLocalDn(String localDn) {
this.localDn = localDn;
}
/*
@XmlAttribute
public String getUserLabel() {
return userLabel;
}
public void setUserLabel(String userLabel) {
this.userLabel = userLabel;
}*/
@Override
public String toString() {
return "ManagedElement [swVersion=" + swVersion + ", localDn=" + localDn + "]";
}

}

