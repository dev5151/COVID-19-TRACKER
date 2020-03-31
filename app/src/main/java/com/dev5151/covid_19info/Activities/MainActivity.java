package com.dev5151.covid_19info.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.dev5151.covid_19info.Fragments.CountryFragment;
import com.dev5151.covid_19info.Fragments.DataFragment;
import com.dev5151.covid_19info.Fragments.MeasuresFragment;
import com.dev5151.covid_19info.Interfaces.NavigationInterface;
import com.dev5151.covid_19info.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    public static NavigationInterface navigationInterface;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FrameLayout frame;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        frame = findViewById(R.id.frame);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        fragmentManager = getSupportFragmentManager();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fragmentTransition(new DataFragment());

        navigationInterface = new NavigationInterface() {
            @Override
            public void switchToData() {
                fragmentTransition(new DataFragment());
            }

            @Override
            public void switchToCountries() {
                fragmentTransition(new CountryFragment());
            }

            @Override
            public void switchToMeasures() {

            }
        };

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_data:
                fragmentTransition(new DataFragment());
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_country:
                fragmentTransition(new CountryFragment());
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_measures:
                fragmentTransition(new MeasuresFragment());
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_map_view:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                break;
        }
        return true;
    }

    private void fragmentTransition(Fragment fragment) {
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    public static NavigationInterface getNavigationInterface() {
        return navigationInterface;
    }
}