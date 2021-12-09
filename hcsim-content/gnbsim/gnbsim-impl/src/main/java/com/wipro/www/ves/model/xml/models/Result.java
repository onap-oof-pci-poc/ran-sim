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
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Result {
    private int p;
    private int measValue;

    // private String measValue;
    public Result() {
    }

    public Result(int p, int measValue) {
        // public Result(int p, String measValue) {
        super();
        this.p = p;
        this.measValue = measValue;
    }

    @XmlAttribute
    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    @XmlValue
    public int getMeasValue() {
        return measValue;
    }

    public void setMeasValue(int measValue) {
        this.measValue = measValue;
    }

    @Override
    public String toString() {
        return "Result [p=" + p + ", measValue=" + measValue + "]";
    }

}