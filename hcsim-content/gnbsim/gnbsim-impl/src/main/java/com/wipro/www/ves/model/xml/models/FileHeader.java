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
public class FileHeader {
private String dnPrefix;
private String vendorName;
private String fileFormatVersion;
private MeasCollec measCollec;
private FileSender fileSender;
public FileHeader() {

}
public FileHeader(String dnPrefix, String vendorName, String fileFormatVersion, MeasCollec measCollec,
FileSender fileSender) {
super();
this.dnPrefix = dnPrefix;
this.vendorName = vendorName;
this.fileFormatVersion = fileFormatVersion;
this.measCollec = measCollec;
this.fileSender = fileSender;
}
public MeasCollec getMeasCollec() {
return measCollec;
}
public void setMeasCollec(MeasCollec measCollec) {
this.measCollec = measCollec;
}
@XmlAttribute
public String getDnPrefix() {
return dnPrefix;
}
public void setDnPrefix(String dnPrefix) {
this.dnPrefix = dnPrefix;
}
@XmlAttribute
public String getVendorName() {
return vendorName;
}
public void setVendorName(String vendorName) {
this.vendorName = vendorName;
}
@XmlAttribute
public String getFileFormatVersion() {
return fileFormatVersion;
}
public void setFileFormatVersion(String fileFormatVersion) {
this.fileFormatVersion = fileFormatVersion;
}
public FileSender getFileSender() {
return fileSender;
}
public void setFileSender(FileSender fileSender) {
this.fileSender = fileSender;
}
@Override
public String toString() {
return "FileHeader [dnPrefix=" + dnPrefix + ", vendorName=" + vendorName + ", fileFormatVersion="
+ fileFormatVersion + ", measCollec=" + measCollec + ", fileSender=" + fileSender + "]";
}
}
