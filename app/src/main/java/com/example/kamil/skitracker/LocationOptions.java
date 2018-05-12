package com.example.kamil.skitracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.skitracker.Permissions.Permissions;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kamil on 23.03.2018.
 */

public class LocationOptions {

    private LocationFragment currentFragment;
    private LocationInfo locationInfo;
    private Location myLocation;
    private Context context;

    private LocationRequest locationRequest;
    private LocationCallback callback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private long timeOfUpdates = 0;
    private boolean startTimers = false;

    private double newTime = 0;

    LocationOptions(Context context, Activity activity) {

        this.context = context;

        this.locationInfo = new LocationInfo();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        callback = createLocationCallback();
        locationRequest = createLocationRequest(500, 500, LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        requestToTurnOnLocation(activity);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                myLocation = location;
            }
        });

    }

    public Location getLocation() {
        return myLocation;
    }

    public void setCurrentFragment(LocationFragment locationFragment) {

        this.currentFragment = locationFragment;

    }

    @SuppressLint("RestrictedApi")
    private LocationRequest createLocationRequest(int interval, int fastesInterval, int priority) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(fastesInterval);
        locationRequest.setPriority(priority);
        return locationRequest;
    }

    private LocationCallback createLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double oldTime = newTime;
                    newTime = System.currentTimeMillis();
                    locationInfo.Update(location, newTime - oldTime);
                    currentFragment.Update(locationInfo);
                    myLocation = location;
                    Log.i("LocationOptions", locationInfo.toString());
                }
            }
        };
    }

    public void startUpdates() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null);

    }

    public void stopUpdates(){

        fusedLocationProviderClient.removeLocationUpdates(callback);

    }

    public void setTimeOfUpdates(long timeOfUpdates) {
        this.timeOfUpdates = timeOfUpdates;
    }

    public long getTimeOfUpdates() {
        return timeOfUpdates;
    }

    public void setStartTimers(boolean startTimers) {
        this.startTimers = startTimers;
    }

    public boolean timerStarted() {
        return startTimers;
    }

    private void requestToTurnOnLocation(final Activity activity){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity,
                                0x1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }

            }
        });
    }


}
