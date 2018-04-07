package com.example.kamil.skitracker;

import android.location.Location;
import android.util.Log;

public class LocationInfo {

    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private Location currentLocation;

    private double currentSpeed = 0;
    private double maxSpeed = 0;
    private double averageSpeed = 0;
    private double oldSpeed = 0;
    private double distance = 0;

    private int numberOfUpdates = 0;

    public LocationInfo(){

    }

    public void Update(Location location, double time){
        numberOfUpdates++;

        this.currentLocation = location;

        double tempDistance = 0;
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
}
