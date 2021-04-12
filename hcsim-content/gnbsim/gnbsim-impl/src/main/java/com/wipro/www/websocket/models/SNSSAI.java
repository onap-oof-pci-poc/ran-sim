package com.wipro.www.websocket.models;

import java.util.*; 

public class SNSSAI {
    
    private String sNssai;
    
    private List<ConfigData> configData;
    
    public String getSNssai() {
     return sNssai;   
    }
    
    public void setSNssai(String sNssai) {
     this.sNssai = sNssai;   
    }
    
    public List<ConfigData> getConfigData() {
        return configData;
    }

    public void setConfigData(List<ConfigData> configData) {
        this.configData = configData;
    }
    
    public SNSSAI() {
        
    }
    
}
