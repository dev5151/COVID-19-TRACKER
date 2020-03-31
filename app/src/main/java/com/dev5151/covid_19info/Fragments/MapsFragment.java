package com.dev5151.covid_19info.Fragments;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment {

    private GoogleMap gMap;
    private MapView mapView;
    FragmentManager fragmentManager;
    LatLng latLng;

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

            }
        });

        return view;
    }

    private void initView(View view) {
        mapView = view.findViewById(R.id.mapView);
        fragmentManager = getActivity().getSupportFragmentManager();
        latLng = new LatLng(20.5937, 78.9629);
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


}
