package com.dev5151.covid_19info.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
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
import com.google.firebase.analytics.FirebaseAnalytics;

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
    List<Country> list;
    androidx.appcompat.widget.SearchView searchView;
    SearchManager searchManager;
    CountryAdapter countryAdapter;
    private FirebaseAnalytics firebaseAnalytics;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        initView(view);
        fetchCountries();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                countryAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        searchView = view.findViewById(R.id.search_view);
        searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

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
                } else {
                    List<Country> countries = response.body();
                    for (Country country : countries) {
                        list.add(country);
                    }
                    countryAdapter = new CountryAdapter(list, getActivity());
                    recyclerView.setAdapter(countryAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    countryAdapter.notifyDataSetChanged();
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
