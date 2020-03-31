package com.dev5151.covid_19info.Models;

import com.google.gson.annotations.SerializedName;

public class Country {

    public Country() {
    }

    @SerializedName("country")
    private String country;

    @SerializedName("countryInfo")
    private CountryInfo countryFlag;

    @SerializedName("cases")
    private int cases;

    @SerializedName("todayCases")
    private int todayCases;

    @SerializedName("deaths")
    private int deaths;

    @SerializedName("todayDeaths")
    private int todayDeaths;

    @SerializedName("recovered")
    private int recovered;

    @SerializedName("active")
    private int active;

    public Country(String country, CountryInfo countryFlag, int cases, int todayCases, int deaths, int todayDeaths, int recovered, int active) {
        this.country = country;
        this.countryFlag = countryFlag;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.active = active;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CountryInfo getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(CountryInfo countryFlag) {
        this.countryFlag = countryFlag;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(int todayCases) {
        this.todayCases = todayCases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(int todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
