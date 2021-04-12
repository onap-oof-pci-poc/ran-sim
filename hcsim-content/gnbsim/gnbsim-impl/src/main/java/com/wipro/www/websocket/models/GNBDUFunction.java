package com.wipro.www.websocket.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GNBDUFunction {
    public String idGNBDUFunction;
    public Attributes attributes;
    @JsonProperty("NRCellDU")
    public List<NRCellDU> nRCellDU;

    public int gNBId;    
    public int aggressorSetID;
    public int victimSetID;
    
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

  public int getGNBId() {
     return gNBId;
  }
  
  public void setGNBId(int gNBId) {
     this.gNBId = gNBId;
  }

  public int getAggressorSetID() {
     return aggressorSetID;
  }

  public void setAggressorSetID(int aggressorSetID) {
     this.aggressorSetID = aggressorSetID;
  }

  public int getVictimSetID() {
     return victimSetID;
  }

  public void setVictimSetID(int victimSetID) {
     this.victimSetID = victimSetID;
  }

}

