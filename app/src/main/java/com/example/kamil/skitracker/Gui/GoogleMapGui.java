package com.example.kamil.skitracker.Gui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.kamil.skitracker.R;
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
    private Location location;

    public GoogleMapGui(MapView mapView, Context context, Bundle savedInstanceState){

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        this.context = context;

    }

    public void setLocation(Location location){
        this.location = location;
    }

    private void setMapGui(GoogleMap map){

        UiSettings settings = map.getUiSettings();

        settings.setZoomControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setScrollGesturesEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        setMapGui(googleMap);


        if (!(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            googleMap.setMyLocationEnabled(true);


            if (location != null) {
                if (location.getLongitude() != 0.0){
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(13).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    //string += " Speed: " + location.hasSpeed()+" " + location.getSpeed() + " Altitude: " + location.hasAltitude()+" "  + location.getAltitude() + " ";
                    Toast.makeText(context, location.getLongitude()+"", Toast.LENGTH_SHORT).show();
                } else {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(52, 21)).zoom(5).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    Toast.makeText(context, "no gps", Toast.LENGTH_SHORT).show();
                }

            } else {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(52, 21)).zoom(5).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Toast.makeText(context, "no gps", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
