
package com.wipro.www.websocket.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class nearRTRIC {
    
     private String idNearRTRIC;
     
     private Attributes attributes;

     private List<GNBDUFunction> gNBDUFunction;

     private List<GNBCUUPFunction> gNBCUUPFunction;

     public String getIdNearRTRIC() {
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
     public nearRTRIC()
     {
   
     }
}
