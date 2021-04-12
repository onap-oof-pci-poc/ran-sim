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
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class MeasValue {
private Integer measObjLdn;
private List<Result> r;
public MeasValue() {

}
public MeasValue(Integer measObjLdn, List<Result> r, boolean suspect) {
super();
this.measObjLdn = measObjLdn;
this.r = r;
this.suspect = suspect;
}
private boolean suspect;
@XmlAttribute
public Integer getMeasObjLdn() {
return measObjLdn;
}
public void setMeasObjLdn(Integer measObjLdn) {
this.measObjLdn = measObjLdn;
}
public List<Result> getR() {
return r;
}
public void setR(List<Result> r) {
this.r = r;
}
public boolean isSuspect() {
return suspect;
}
public void setSuspect(boolean suspect) {
this.suspect = suspect;
}
@Override
public String toString() {
return "MeasValue [measObjLdn=" + measObjLdn + ", r=" + r + ", suspect=" + suspect + "]";
}

}


