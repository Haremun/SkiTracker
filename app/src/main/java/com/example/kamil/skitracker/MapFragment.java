package com.example.kamil.skitracker;


import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kamil.skitracker.Gui.GoogleMapGui;
import com.example.kamil.skitracker.Permissions.Permissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements LocationFragment {

    private Location location;
    private boolean attach = false;

    public MapFragment() {
        // Required empty public constructor
    }


    MapView mapView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = viewRoot.findViewById(R.id.mapview);

        GoogleMapGui googleMapGui = new GoogleMapGui(mapView, getContext(), savedInstanceState);
        googleMapGui.setLocation(location);


        return viewRoot;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void Update(Location location) {
        this.location = location;
    }
    @Override
    public void setAttached(boolean attach) {
        this.attach = attach;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean isAttached() {
        return attach;
    }
}
