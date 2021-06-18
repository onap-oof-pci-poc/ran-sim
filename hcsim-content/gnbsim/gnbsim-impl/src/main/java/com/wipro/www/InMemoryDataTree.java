/*
 * Copyright (c) 2016 Cisco and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wipro.www;

import com.wipro.www.websocket.models.*;

import io.fd.honeycomb.translate.read.ReadFailedException;
import io.fd.honeycomb.translate.write.WriteFailedException;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRIC;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRICBuilder;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

public class InMemoryDataTree {

 /* private String locationName;
  private String nearRTRICgNBId;
  private String managedBy;

  private String idNearRTRIC;
  private String gNBId;
  private int aggressorSetID;
  private int victimSetID;

  private enum operationalState {
       DISABLED,
       ENABLED
  }
  private enum cellState {
       IDLE,
       INACTIVE,
       ACTIVE
  }
  private operationalState eoperationalState;
  private cellState ecellState;
  private List<ConfigNRCellDU> nrCellDu;
  
  public operationalState getoperationalState() {
     return eoperationalState;
  }

  public void setoperationalState(operationalState valoperationalState) {
     this.eoperationalState = valoperationalState;
  }*/
 
  private static InMemoryDataTree instance;
  
  private InMemoryDataTree(){
  }

  public static InMemoryDataTree getInstance(){   
   if (instance == null){
       instance = new InMemoryDataTree();
   }
   return instance;
  }

  Map<String, Integer> hashMapgNBDUFunc 
	              = new HashMap<String, Integer>(); 
  /*List<NearRTRIC> nearRTRIC;

  public List<NearRTRIC> getNearRTRIC() {
    return nearRTRIC;
  }

  public void setNearRTRIC(List<NearRTRIC> nearRTRIC) {
     this.nearRTRIC = nearRTRIC;
  }

  public String getIdNearRTRIC() {
	  return idNearRTRIC;
  }

  public void setIdNearRTRIC(String idNearRTRIC) {
	  this.idNearRTRIC = idNearRTRIC;
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

  public String getGNBId() {
     return gNBId;
  }
  
  public void setGNBId(String gNBId) {
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
 
  public List<ConfigNRCellDU> getNRCellDU() {
      return nrCellDu;
  }

  public void setNrCellDU(List<ConfigNRCellDU> nrCellDu) {
      this.nrCellDu = nrCellDu;
  }*/

   private List<nearRTRIC> nearRTRIC;

   public List<nearRTRIC> getNearRTRIC() {
          return nearRTRIC;
   }

   public void setNearRTRIC(List<nearRTRIC> nearRTRIC) {
          this.nearRTRIC = nearRTRIC;
   }

    
/*    List<nearRTRIC> nearRTRIC;
    String idNearRTRIC;
    private Attributes attributes;
    private List<GNBDUFunction> gNBDUFunction;
    private List<GNBCUUPFunction> gNBCUUPFunction;
    private String idGNBCUUPFunction;
    private String idGNBDUFunction;
    private List<NRCellDU> nRCellDU;
    private String idNRCellDU;
     
    public List<NearRTRIC> getNearRTRIC() {
      return nearRTRIC;
    }

    public void setNearRTRIC(List<NearRTRIC> nearRTRIC) {
       this.nearRTRIC = nearRTRIC;
    }

     public void setIdNearRTRIC(String idNearRTRIC) {
	     this.idNearRTRIC = idNearRTRIC;
     }
     public String getIdNearRTRIC () {
       return idNearRTRIC;
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

    public String getIdGNBCUUPFunction() {
        return idGNBCUUPFunction;
    }
    public void setIdGNBCUUPFunction(String idGNBCUUPFunction) {
        this.idGNBCUUPFunction = idGNBCUUPFunction;
    } 

    public String getIdGNBDUFunction() {
     return idGNBDUFunction;
    }
    public void setIdGNBDUFunction(String idGNBDUFunction) {
     this.idGNBDUFunction = idGNBDUFunction;
    }

    public String getIdNRCellDU() {
      return idNRCellDU;
    }
    public void setIdNRCellDU(String idNRCellDU) {
      this.idNRCellDU = idNRCellDU;
    }

*/
}
