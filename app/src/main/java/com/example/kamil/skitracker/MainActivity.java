package com.example.kamil.skitracker;


import android.graphics.Color;

import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;

import com.example.kamil.skitracker.Permissions.Permissions;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private LocationOptions locationOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Permissions.askForPermissions(this);
        locationOptions = new LocationOptions(this, this);


        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(myToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        final MainFragment mainFragment = new MainFragment();
        locationOptions.setCurrentFragment(mainFragment);
        mainFragment.setLocation(locationOptions.getLocation());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mainFragment);
        transaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_ski:{
                        MainFragment mainFragment = new MainFragment();
                        locationOptions.setCurrentFragment(mainFragment);
                        mainFragment.setLocation(locationOptions.getLocation());
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, mainFragment);
                        transaction.commit();
                        myToolbar.setTitle("Ski Tracker");
                        return true;
                    }
                    case R.id.action_map:{
                        MapFragment mapFragment = new MapFragment();
                        locationOptions.setCurrentFragment(mapFragment);
                        mapFragment.setLocation(locationOptions.getLocation());
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, mapFragment);
                        transaction.commit();
                        myToolbar.setTitle("Mapa");
                        return true;
                    }
                    case R.id.action_history:{
                        HistoryFragment historyFragment = new HistoryFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, historyFragment);
                        transaction.commit();
                        myToolbar.setTitle("Historia");
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationOptions.startUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationOptions.stopUpdates();
    }
}

