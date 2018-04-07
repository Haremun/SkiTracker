package com.example.kamil.skitracker;


import android.graphics.Color;

import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuInflater;
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
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    private LocationOptions locationOptions;

    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permissions.askForPermissions(this);
        locationOptions = new LocationOptions(this, this);


        SetActionBar();

        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        SetNavigationViewListener();
        SetBottomNavigationViewListener();

        final MainFragment mainFragment = new MainFragment();
        locationOptions.setCurrentFragment(mainFragment);
        mainFragment.setLocation(locationOptions.getLocation());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mainFragment);
        transaction.commit();

    }

    private void SetActionBar(){
        myToolbar = findViewById(R.id.my_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(myToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void SetNavigationViewListener(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for(int i = 0; i < navigationView.getMenu().size(); i++){
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
                item.setChecked(true);

                switch (item.getItemId()){

                    case R.id.action_ski:{
                        MainFragment mainFragment = new MainFragment();
                        locationOptions.setCurrentFragment(mainFragment);
                        mainFragment.setLocation(locationOptions.getLocation());
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, mainFragment);
                        transaction.commit();
                        bottomNavigationView.setSelectedItemId(R.id.action_ski);
                        break;
                    }
                    case R.id.action_map:{
                        MapFragment mapFragment = new MapFragment();
                        locationOptions.setCurrentFragment(mapFragment);
                        mapFragment.setLocation(locationOptions.getLocation());
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, mapFragment);
                        transaction.commit();
                        bottomNavigationView.setSelectedItemId(R.id.action_map);
                        break;
                    }
                    case R.id.action_history:{
                        HistoryFragment historyFragment = new HistoryFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, historyFragment);
                        transaction.commit();
                        bottomNavigationView.setSelectedItemId(R.id.action_history);
                        break;
                    }


                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void SetBottomNavigationViewListener(){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for(int i = 0; i < navigationView.getMenu().size(); i++){
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
                switch (item.getItemId()){
                    case R.id.action_ski:{
                        MainFragment mainFragment = new MainFragment();
                        locationOptions.setCurrentFragment(mainFragment);
                        mainFragment.setLocation(locationOptions.getLocation());
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, mainFragment);
                        transaction.commit();
                        myToolbar.setTitle("Ski Tracker");
                        navigationView.getMenu().getItem(1).setChecked(true);
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
                        navigationView.getMenu().getItem(2).setChecked(true);
                        return true;
                    }
                    case R.id.action_history:{
                        HistoryFragment historyFragment = new HistoryFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, historyFragment);
                        transaction.commit();
                        myToolbar.setTitle("Historia");
                        navigationView.getMenu().getItem(3).setChecked(true);
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
            case R.id.action_play:
                locationOptions.startUpdates();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //locationOptions.startUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //locationOptions.stopUpdates();
    }
}

