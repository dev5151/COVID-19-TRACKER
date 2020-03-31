package com.dev5151.covid_19info.Models;

public class Data {
    private int cases;
    private int deaths;
    private int recovered;
    private int active;

    public Data() {
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
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

    public Data(int cases, int deaths, int recovered, int active) {
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
    }

}

