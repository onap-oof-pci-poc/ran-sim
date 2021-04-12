package com.wipro.www.websocket.models;

public class NRCellDU {

    public String idNRCellDU;
    public Attributes attributes;
    public enum operationalState {
       DISABLED,
       ENABLED
    }
    public enum cellState {
       IDLE,
       INACTIVE,
       ACTIVE
    }
    public operationalState eoperationalState;
    public cellState ecellState;

    public operationalState getoperationalState() {
        return eoperationalState;
    }

    public void setoperationalState(operationalState valoperationalState) {
       this.eoperationalState = valoperationalState;
    }

    public cellState getcellState() {
        return ecellState;
    }

    public void setcellState(cellState valcellState) {
       this.ecellState = valcellState;
    }
 
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

