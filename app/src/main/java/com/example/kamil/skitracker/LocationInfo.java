package com.example.kamil.skitracker;

import android.location.Location;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class LocationInfo {

    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private Location currentLocation;

    private double currentSpeed = 0;
    private double maxSpeed = 0;
    private double averageSpeed = 0;
    private double oldSpeed = 0;
    private double distance = 0;

    private double tempDistance;
    private double tempTime;

    private int numberOfUpdates = 0;

    public void Update(Location location, double time){
        numberOfUpdates++;

        this.currentLocation = location;

        tempTime = time;
        tempDistance = 0;
        if(currentLatitude > 0)
            tempDistance = MathHelper.calculateDistance(currentLatitude, currentLongitude, location.getLatitude(), location.getLongitude());

        oldSpeed += currentSpeed;
        distance += tempDistance / 1000;

        double timeInHours = time / (1000 * 60 * 60);
        if(timeInHours > 0)
            currentSpeed = (tempDistance / 1000) / timeInHours;
        //currentSpeed = 24044.04;

        averageSpeed = (currentSpeed + oldSpeed)/numberOfUpdates; //To think about

        if(currentSpeed > maxSpeed)
            maxSpeed = currentSpeed;

        if(location != null){
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }
        Log.i("LocInfo", "Updated! currentSpeed: " + currentSpeed + " distance: " + tempDistance + " time: " + timeInHours);
    }

    public double getCurrentSpeed(){
        return currentSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public double getTempDistance() {
        return tempDistance;
    }

    public double getTempTime() {
        return tempTime;
    }
    public String getLatLon(){
        return currentLatitude + " " + currentLongitude;
    }

    @Override
    public String toString() {
        return "[" + "current speed: " + currentSpeed + " lat: " + currentLatitude + " lon: " + currentLongitude +
                " temp distance: " + tempDistance + " time: " + tempTime + "]";
    }
}
