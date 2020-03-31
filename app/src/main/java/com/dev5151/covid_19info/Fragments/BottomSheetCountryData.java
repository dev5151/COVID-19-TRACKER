package com.dev5151.covid_19info.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.dev5151.covid_19info.Interfaces.CovidApi;
import com.dev5151.covid_19info.Models.Country;
import com.dev5151.covid_19info.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetCountryData extends BottomSheetDialogFragment {

    private TextView tvCountry, cases, todayCases, deaths, todayDeaths, recovered;
    private ImageView flag;
    private Activity mActivity;
    Retrofit retrofit;
    String country;
    ProgressBar progressBar;


    public BottomSheetCountryData(String country) {
        this.country = country;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_country_data, container, false);
        initView(view);
        fetchData(country);
        return view;
    }


    private void initView(View view) {
        flag = view.findViewById(R.id.flag);
        tvCountry = view.findViewById(R.id.country);
        cases = view.findViewById(R.id.cases);
        todayCases = view.findViewById(R.id.today_cases);
        deaths = view.findViewById(R.id.deaths);
        todayDeaths = view.findViewById(R.id.today_deaths);
        recovered = view.findViewById(R.id.recovered);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void fetchData(String country) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovidApi covidApi = retrofit.create(CovidApi.class);

        final Call<Country> countryCall = covidApi.getCountryData(country);

        progressBar.setVisibility(View.VISIBLE);

        countryCall.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("Retrofit", "Code: " + response.code());
                } else {
                    Country newCountry = response.body();
                    progressBar.setVisibility(View.INVISIBLE);
                    tvCountry.setText(String.valueOf(newCountry.getCountry()));
                    cases.setText(String.valueOf(newCountry.getCases()));
                    todayCases.setText(String.valueOf(newCountry.getTodayCases()));
                    deaths.setText(String.valueOf(newCountry.getDeaths()));
                    todayDeaths.setText(String.valueOf(newCountry.getTodayDeaths()));
                    recovered.setText(String.valueOf(newCountry.getRecovered()));
                    if (mActivity == null) {
                        return;
                    }
                    Glide.with(mActivity).load(newCountry.getCountryFlag().getFlag()).into(flag);
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("FAIL", "" + t.getMessage());
            }
        });
    }
}

