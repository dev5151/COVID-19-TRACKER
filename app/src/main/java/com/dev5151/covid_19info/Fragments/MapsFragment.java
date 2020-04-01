package com.dev5151.covid_19info.Fragments;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dev5151.covid_19info.Interfaces.CovidApi;
import com.dev5151.covid_19info.Models.Country;
import com.dev5151.covid_19info.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsFragment extends Fragment {

    private GoogleMap gMap;
    private MapView mapView;
    FragmentManager fragmentManager;
    LatLng latLng;
    FirebaseAnalytics firebaseAnalytics;
    Button heatMap;
    List<LatLng> list;
    TileOverlay overlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        initView(view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.new_style));

                    if (!success) {
                        Log.e("MAP", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("MAP", "Can't find style. Error: ", e);
                }

                moveCamera(latLng, 3f);

                gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses.size() > 0) {
                            String country = addresses.get(0).getCountryName();
                            //Toast.makeText(getApplicationContext(), "Country: " + country, Toast.LENGTH_LONG).show();
                            if (addresses.get(0).getCountryName().equals(null)) {
                                Toast.makeText(getActivity(), "Country not in Database", Toast.LENGTH_LONG).show();
                            } else {
                                addMarker(googleMap, latLng, country);
                                BottomSheetCountryData bottomSheetCountryData = new BottomSheetCountryData(country);
                                bottomSheetCountryData.show(fragmentManager, bottomSheetCountryData.getTag());
                            }
                        }
                    }
                });

                heatMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        generateData();
                    }
                });

            }
        });

        return view;
    }

    private void initView(View view) {
        mapView = view.findViewById(R.id.mapView);
        fragmentManager = getActivity().getSupportFragmentManager();
        latLng = new LatLng(20.5937, 78.9629);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        heatMap = view.findViewById(R.id.heatMap);
        list = new ArrayList<LatLng>();
    }

    private void moveCamera(LatLng latLng, float zoom) {
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void addMarker(GoogleMap mMap, LatLng latLng, String title) {
        gMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker()).title(title));
        moveCamera(latLng, 3f);

    }

    private void generateData() {
        Retrofit retrofit = new Retrofit.Builder()
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
                        list = Collections.nCopies(country.getCases()
                                , new LatLng(country.getCountryFlag().getLatitude(), country.getCountryFlag().getLongitude()));
                        //list.add(new LatLng(country.getCountryFlag().getLatitude(), country.getCountryFlag().getLongitude()),country.getCases());
                        //list.add(new LatLng(country.getCountryFlag().getLatitude(), country.getCountryFlag().getLongitude()));
                    }

                    generateHeatMap(list, gMap);
                }

            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {

            }

        });
    }

    private void generateHeatMap(List<LatLng> list, GoogleMap googleMap) {
        HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();

        overlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatmapTileProvider));
    }
}