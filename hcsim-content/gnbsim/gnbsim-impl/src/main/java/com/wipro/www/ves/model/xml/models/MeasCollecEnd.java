
package com.wipro.www.ves.model.xml.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MeasCollecEnd {
@XmlAttribute
private String endTime;
public MeasCollecEnd() {

}

public MeasCollecEnd(String endTime) {
super();
this.endTime = endTime;
}
}

