package com.dev5151.covid_19info.Interfaces;

import com.dev5151.covid_19info.Models.Country;
import com.dev5151.covid_19info.Models.Data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CovidApi {

    @GET("all")
    Call<Data> getData();

    @GET("countries")
    Call<List<Country>> getCountries();

    @GET("countries/{country}")
    Call<Country> getCountryData(@Path("country") String country);

}
