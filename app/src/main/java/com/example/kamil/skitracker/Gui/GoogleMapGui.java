package com.example.kamil.skitracker.Gui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Kamil on 21.03.2018.
 */

public class GoogleMapGui implements OnMapReadyCallback {


    private Context context;

    public GoogleMapGui(MapView mapView, Context context, Bundle savedInstanceState){

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        this.context = context;

    }

    private void setMapGui(GoogleMap map){

        UiSettings settings = map.getUiSettings();

        settings.setZoomControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setScrollGesturesEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        setMapGui(googleMap);


        if (!(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            googleMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location bestLocation = null;
            int string = 0;

            List<String> providers = locationManager.getProviders(true);

            for (String provider : providers) {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location == null) {
                    continue;
                }
                if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = location;
                    string++;
                }
            }

            Location location = bestLocation;

            if (location != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(18).build();

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Toast.makeText(context, string+"", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
