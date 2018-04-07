package com.example.kamil.skitracker;

import android.location.Location;
import android.util.Log;

public class LocationInfo {

    private Location oldLocation;
    private Location newLocation;

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

        if(newLocation != null)
            oldLocation = newLocation;
        newLocation = location;

        long tempDistance = 0;
        if(newLocation != null && oldLocation != null)
            tempDistance = (long) MathHelper.calculateDistance(oldLocation.getLatitude(), oldLocation.getLongitude(), newLocation.getLatitude(), newLocation.getLongitude());

        oldSpeed += currentSpeed;
        distance += tempDistance / 1000;

        double timeInHours = time / (1000 * 60 * 60);
        if(timeInHours > 0)
            currentSpeed = (tempDistance / 1000) / timeInHours;
        //currentSpeed = 24044.04;

        averageSpeed = (currentSpeed + oldSpeed)/numberOfUpdates;

        if(currentSpeed > maxSpeed)
            maxSpeed = currentSpeed;

        if(oldLocation != null)
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
        return newLocation;
    }
}
