
package com.wipro.www.websocket.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GNBCUUPFunction {

    private String idGNBCUUPFunction;
    
    private Attributes attributes;
    
    public String getIdGNBCUUPFunction() {
        return idGNBCUUPFunction;
    }
    public void setIdGNBCUUPFunction(String idGNBCUUPFunction) {
        this.idGNBCUUPFunction = idGNBCUUPFunction;
    }
    public Attributes getAttributes() {
        return attributes;
    }
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
    
    public  GNBCUUPFunction()
    {
        
    }
    
}

