package org.onap.ransim.rest.api.models;

public class Abc {
     float screenX;
     float screenY;
     float sum;
     String latitude;
     String longitude;
     
    public Abc(float screenX, float screenY, String latitude, String longitude) {
        super();
        this.screenX = screenX;
        this.screenY = screenY;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Abc() {
        super();
        // TODO Auto-generated constructor stub
    }
    public Abc(float screenX, float screenY) {
        super();
        this.screenX = screenX;
        this.screenY = screenY;
    }
    public float getScreenX() {
        return screenX;
    }
    public void setScreenX(float screenX) {
        this.screenX = screenX;
    }
    public float getScreenY() {
        return screenY;
    }
    public void setScreenY(float screenY) {
        this.screenY = screenY;
    }
    public float getSum() {
        //sum = screenX*screenX + screenY*screenY;
        return sum;
    }
    public void setSum() {
        this.sum = screenX*screenX + screenY*screenY;;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
     
    
     
}
