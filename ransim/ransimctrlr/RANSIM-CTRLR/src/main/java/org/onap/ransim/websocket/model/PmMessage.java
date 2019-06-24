package org.onap.ransim.websocket.model;

import java.util.List;

public class PmMessage {

    List<EventPm> eventPmList;
    
    public void setEventPmList(List<EventPm> data) {
        this.eventPmList = data;
    }

    public List<EventPm> getEventPmList() {
        return eventPmList;
    }
    
}
