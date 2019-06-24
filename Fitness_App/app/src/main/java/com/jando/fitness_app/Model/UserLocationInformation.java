package com.jando.fitness_app.Model;

public class UserLocationInformation {
    private String Latitude;
    private String Longitude;

    public UserLocationInformation(){

    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }
    public void setLatitude(String latitude){
        this.Latitude = Latitude;
    }
    public void setLongitude(String longitude){
        this.Longitude = Longitude;
    }
}
