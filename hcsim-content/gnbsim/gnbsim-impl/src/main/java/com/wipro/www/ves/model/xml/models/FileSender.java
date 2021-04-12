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
public class FileSender {
private String localDn;
//private String elementType;
public FileSender() {

}
public FileSender(String localDn) {
super();
this.localDn = localDn;
}
@XmlAttribute
public String getLocalDn() {
return localDn;
}
public void setLocalDn(String localDn) {
this.localDn = localDn;
}
/*@XmlAttribute
public String getElementType() {
return elementType;
}
public void setElementType(String elementType) {
this.elementType = elementType;
}*/
@Override
public String toString() {
return "FileSender [localDn=" + localDn + "]";
}
}


