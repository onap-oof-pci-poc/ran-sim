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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MeasCollecFile {
    @XmlAttribute
    private String xmlns;
    @XmlElement
    private FileHeader fileHeader;
    @XmlElement
    private List<MeasData> measData;
    @XmlElement
    private FileFooter fileFooter;

    public MeasCollecFile() {

    }

    public MeasCollecFile(FileHeader fileHeader, List<MeasData> measData, FileFooter fileFooter, String xmlns) {
        super();
        this.fileHeader = fileHeader;
        this.measData = measData;
        this.fileFooter = fileFooter;
        this.xmlns = xmlns;
    }

    // public FileHeader getFileHeader() {
    // return fileHeader;
    // }
    // public void setFileHeader(FileHeader fileHeader) {
    // this.fileHeader = fileHeader;
    // }
    // public List<MeasData> getMeasData() {
    // return measData;
    // }
    // public void setMeasData(List<MeasData> measData) {
    // this.measData = measData;
    // }
    // public FileFooter getFileFooter() {
    // return fileFooter;
    // }
    // public void setFileFooter(FileFooter fileFooter) {
    // this.fileFooter = fileFooter;
    // }
    @Override
    public String toString() {
        return "MeasCollecFile [fileHeader=" + fileHeader + ", measData=" + measData + ", fileFooter=" + fileFooter
                + "]";
    }
}
