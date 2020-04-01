package com.dev5151.covid_19info.Models;

import com.google.gson.annotations.SerializedName;

public class CountryInfo {

    @SerializedName("flag")
    private String flag;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("long")
    private double longitude;

    public CountryInfo() {
    }

    public CountryInfo(String flag, double latitude, double longitude) {
        this.flag = flag;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
