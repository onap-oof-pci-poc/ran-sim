package org.onap.ransim.websocket.model;

import java.util.List;

public class FmMessage {

    List<EventFm> fmEventList;
    
    public void setFmEventList(List<EventFm> data) {
        this.fmEventList = data;
    }

    public List<EventFm> getFmEventList() {
        return fmEventList;
    }
    
}
