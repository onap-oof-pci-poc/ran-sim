package com.wipro.www.websocket.models;

public class ConfigData {
    
    private String configParameter;
    
    private int configValue;
    
    public String getConfigParameter() {
        return configParameter;
    }
    
    public void setConfigParameter(String configParameter) {
        this.configParameter = configParameter;
    }
    
    public int getConfigValue() {
        return configValue;
    }

    public void setConfigValue(int configValue) {
        this.configValue = configValue;
    }
    
    public ConfigData() {
        
    }
    
    public ConfigData(String configParameter, int configValue) {
        this.configParameter= configParameter;
        this.configValue = configValue;
    }
}
