package com.dev5151.covid_19info.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dev5151.covid_19info.Interfaces.CovidApi;
import com.dev5151.covid_19info.Models.Data;
import com.dev5151.covid_19info.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFragment extends Fragment {

    Retrofit retrofit;
    private TextView cases, deaths, recovered, active;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        initView(view);
        fetchData();
        return view;
    }

    private void initView(View view) {
        cases = view.findViewById(R.id.cases);
        deaths = view.findViewById(R.id.deaths);
        recovered = view.findViewById(R.id.recovered);
        active = view.findViewById(R.id.active);
    }

    private void fetchData() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovidApi covidApi = retrofit.create(CovidApi.class);

        final Call<Data> dataCall = covidApi.getData();

        dataCall.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("Retrofit", "Code: " + response.code());
                } else {
                    Data data = response.body();
                    cases.setText(String.valueOf(data.getCases()));
                    deaths.setText(String.valueOf(data.getDeaths()));
                    recovered.setText(String.valueOf(data.getRecovered()));
                    active.setText(String.valueOf(data.getActive()));
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("FAIL", "" + t.getMessage());
            }
        });
    }
}
