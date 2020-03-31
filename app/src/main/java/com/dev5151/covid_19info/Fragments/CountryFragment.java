package com.dev5151.covid_19info.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev5151.covid_19info.Adapters.CountryAdapter;
import com.dev5151.covid_19info.Interfaces.CovidApi;
import com.dev5151.covid_19info.Models.Country;
import com.dev5151.covid_19info.Models.Data;
import com.dev5151.covid_19info.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryFragment extends Fragment {

    private RecyclerView recyclerView;
    Retrofit retrofit;
    List<Country>list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        initView(view);
        fetchCountries();
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        list=new ArrayList<>();
    }

    private void fetchCountries() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovidApi covidApi = retrofit.create(CovidApi.class);

        final Call<List<Country>> countryList = covidApi.getCountries();

        countryList.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("Retrofit", "Code: " + response.code());
                }else{
                    List<Country>countries=response.body();
                    for(Country country:countries){
                        list.add(country);
                    }
                    CountryAdapter countryAdapter=new CountryAdapter(list,getActivity());
                    recyclerView.setAdapter(countryAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("FAIL", "" + t.getMessage());
            }
        });
    }

}
