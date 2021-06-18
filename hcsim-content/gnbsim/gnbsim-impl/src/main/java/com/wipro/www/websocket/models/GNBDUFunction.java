
package com.wipro.www.websocket.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GNBDUFunction {
    
    private String idGNBDUFunction;
    
    private Attributes attributes;
    
    public List<NRCellDU> nRCellDU;
    
    public String getIdGNBDUFunction() {
     return idGNBDUFunction;
    }
    public void setIdGNBDUFunction(String idGNBDUFunction) {
     this.idGNBDUFunction = idGNBDUFunction;
    }
    public Attributes getAttributes() {
     return attributes;
    }
    public void setAttributes(Attributes attributes) {
     this.attributes = attributes;
    }
    public List<NRCellDU> getnRCellDU() {
     return nRCellDU;
    }
    public void setnRCellDU(List<NRCellDU> nRCellDU) {
     this.nRCellDU = nRCellDU;
    } 
    public GNBDUFunction()
    {
    
    }
}

