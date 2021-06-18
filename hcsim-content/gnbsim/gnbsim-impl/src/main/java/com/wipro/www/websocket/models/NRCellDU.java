package com.wipro.www.websocket.models;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NRCellDU {

    private String idNRCellDU;
    
    private Attributes attributes;

    public String getIdNRCellDU() {
      return idNRCellDU;
    }
    public void setIdNRCellDU(String idNRCellDU) {
      this.idNRCellDU = idNRCellDU;
    }
    public Attributes getAttributes() {
      return attributes;
    }
    public void setAttributes(Attributes attributes) {
      this.attributes = attributes;
    }
    public NRCellDU(Attributes attributes) {
      super();
      this.attributes = attributes;
    }
    public NRCellDU()
    {
        
    }
}

