package com.dev5151.covid_19info.Fragments;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFragment extends Fragment {

    Retrofit retrofit;
    private TextView cases, deaths, recovered, active;
    PieChart pieChart;
    int[] colorArray;

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
        pieChart = view.findViewById(R.id.pieChart);

        colorArray = new int[]{getResources().getColor(R.color.purple), Color.RED, Color.GREEN, getResources().getColor(R.color.lightBlue)};

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

                    PieDataSet pieDataSet = new PieDataSet(dataValues(data.getCases(), data.getDeaths(), data.getRecovered(), data.getActive()), "");
                    pieDataSet.setColors(colorArray);
                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.setDrawEntryLabels(true);
                    pieChart.setUsePercentValues(false);
                    pieChart.setHoleColor(30);
                    pieChart.setNoDataTextColor(Color.LTGRAY);
                    pieChart.setEntryLabelColor(Color.WHITE);
                    pieChart.setEntryLabelTextSize(15);

                    pieChart.invalidate();


                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("FAIL", "" + t.getMessage());
            }
        });
    }

    private ArrayList<PieEntry> dataValues(int cases, int deaths, int recovered, int active) {
        ArrayList<PieEntry> dataVals = new ArrayList<>();

        dataVals.add(new PieEntry(cases, "Cases"));
        dataVals.add(new PieEntry(deaths, "deaths"));
        dataVals.add(new PieEntry(recovered, "recovered"));
        dataVals.add(new PieEntry(active, "active"));

        return dataVals;
    }
}
