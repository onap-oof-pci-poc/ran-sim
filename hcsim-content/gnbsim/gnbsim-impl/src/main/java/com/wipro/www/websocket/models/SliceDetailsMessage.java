package com.wipro.www.websocket.models;

import java.math.BigDecimal;
import java.util.*; 

public class SliceDetailsMessage {

    private String pnfName;
    private String cellAlias;
    private String sliceName;
    private String timeInterval;
    private BigDecimal bwAllocation;
    private List<String> nssai;
    private boolean deleteFlag;
    
    public List<String> getNssai()
    {
        return nssai;
    }
    
    public void setNssai(List<String> nssai)
    {
        this.nssai = nssai;
    }
    

    public String getCellAlias() {
        return cellAlias;
    }

    public void setCellAlias(String cellAlias) {
        this.cellAlias = cellAlias;
    }

    public String getSliceName() {
        return sliceName;
    }

    public void setSliceName(String sliceName) {
        this.sliceName = sliceName;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public BigDecimal getBwAllocation() {
        return bwAllocation;
    }

    public void setBwAllocation(BigDecimal bwAllocation) {
        this.bwAllocation = bwAllocation;
    }

    public String getPnfName() {
        return pnfName;
    }

    public void setPnfName(String pnfName) {
        this.pnfName = pnfName;
    }

    public void setdeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public boolean getdeleteFlag() {
         return deleteFlag;
    }

}
