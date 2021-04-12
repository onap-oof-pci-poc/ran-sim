package com.wipro.www.websocket.models;

import java.util.*; 

public class ConfigPLMNInfo {

    private String mcc;
    private String mnc;
    private List<SNSSAI> sNSSAI;

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }
    
    public List<SNSSAI> getSNSSAI() {
        return sNSSAI;
    }

    public void setSNSSAI(List<SNSSAI> sNSSAI) {
        this.sNSSAI = sNSSAI;
    }
    
    public ConfigPLMNInfo() {
        
    }
}
