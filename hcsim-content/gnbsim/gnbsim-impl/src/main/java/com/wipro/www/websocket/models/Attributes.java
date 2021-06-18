
package com.wipro.www.websocket.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attributes {

    @JsonProperty("locationName")
    private String locationName;
    private String gNBId;    
    private String operationalState;
    private String cellState;
    private String gNBCUUPId;
   
    public String getLocationName() {
      return locationName;
    }
    public void setLocationName(String locationName) {
      this.locationName = locationName;
    }
    public String getgNBId() {
      return gNBId;
    }
    public void setgNBId(String gNBId) {
      this.gNBId = gNBId;
    }
    public String getOperationalState() {
      return operationalState;
    }
    public void setOperationalState(String operationalState) {
      this.operationalState = operationalState;
    }
    public String getCellState() {
      return cellState;
    }
    public void setCellState(String cellState) {
      this.cellState = cellState;
    } 
    public String getgNBCUUPId() {
      return gNBCUUPId;
    }
    public void setgNBCUUPId(String gNBCUUPId) {
      this.gNBCUUPId = gNBCUUPId;
   }          
}

