package com.dev5151.covid_19info.Models;

import com.google.gson.annotations.SerializedName;

public class CountryInfo {

    @SerializedName("flag")
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public CountryInfo() {
    }

    public CountryInfo(String flag) {
        this.flag = flag;
    }
}
