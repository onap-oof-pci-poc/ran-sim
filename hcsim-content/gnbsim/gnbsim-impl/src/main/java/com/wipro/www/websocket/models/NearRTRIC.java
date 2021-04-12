package com.wipro.www.websocket.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

public class NearRTRIC {
    
     String idNearRTRIC;
     public Attributes attributes;
     String locationName;
     String nearRTRICgNBId;
     String managedBy;

     @JsonInclude(JsonInclude.Include.NON_NULL)
     @JsonProperty("GNBDUFunction")
     public List<GNBDUFunction> gNBDUFunction;
     @JsonInclude(JsonInclude.Include.NON_NULL)
     @JsonProperty("GNBCUUPFunction")
     public List<GNBCUUPFunction> gNBCUUPFunction;

     public String getIdNearRTRIC () {
       return idNearRTRIC;
     }
     public void setIdNearRTRIC(String idNearRTRIC) {
       this.idNearRTRIC = idNearRTRIC;
     }
     public Attributes getAttributes() {
       return attributes;
     }
     public void setAttributes(Attributes attributes) {
       this.attributes = attributes;
     }
     public List<GNBDUFunction> getgNBDUFunction() {
       return gNBDUFunction;
     }
     public void setgNBDUFunction(List<GNBDUFunction> gNBDUFunction) {
       this.gNBDUFunction = gNBDUFunction;
     }
     public List<GNBCUUPFunction> getgNBCUUPFunction() {
       return gNBCUUPFunction;
     }
     public void setgNBCUUPFunction(List<GNBCUUPFunction> gNBCUUPFunction) {
       this.gNBCUUPFunction = gNBCUUPFunction;
     }
     public NearRTRIC()
     {
   
     }

  public String getLocationName() {
      return locationName;
  }

  public void setLocationName(String locationName) {
      this.locationName = locationName;
  }

  public String getNearRTRICgNBId() {
     return nearRTRICgNBId;
  }

  public void setNearRTRICgNBId(String nearRTRICgNBId) {
      this.nearRTRICgNBId = nearRTRICgNBId;
  }

  public String getManagedBy() {
     return managedBy;
  }
  public void setManagedBy(String managedBy) {
     this.managedBy = managedBy;
  }
}
