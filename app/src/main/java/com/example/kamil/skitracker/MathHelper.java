package com.example.kamil.skitracker;

import android.util.Log;

/**
 * Created by Kamil on 24.03.2018.
 */

public class MathHelper {

    public static String[] convertLatLngDecimalToDMS(float lat, float lng){
        String[] string = new String[2];
        char directionLat;
        char directionLng;

        if (lat < 0) {
            lat *= -1;
            directionLat = 'S';
        } else { directionLat = 'N'; }

        if (lng < 0) {
            lng *= -1;
            directionLng = 'W';
        } else { directionLng = 'E';}

        int latIntPart = (int) lat;
        int lngIntPart = (int) lng;

        float vFraction = (lat - latIntPart) * 3600;
        int vMinutes = Math.round(vFraction / 60);
        int vSeconds = Math.round(vFraction % 60);

        string[0] = latIntPart + "°" + vMinutes + "\'" + vSeconds + "\"" + directionLat;

        vFraction = (lng - lngIntPart) * 3600;
        vMinutes = Math.round(vFraction / 60);
        vSeconds = Math.round(vFraction % 60);

        string[1] = lngIntPart + "°" + vMinutes + "\'" + vSeconds + "\"" + directionLng;

        return string;
    }
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        Log.i("Math:", dLon+"");
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        Log.i("Math:", a+" a");
        double c = 2 * Math.asin(Math.sqrt(a));
        Log.i("Math:", c+" c");
        double distanceInMeters = Math.round(6371000 * c);
        Log.i("Math:", distanceInMeters+" :distance");
        return distanceInMeters;

        /*double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lng2 * Math.PI / 180 - lng1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters*/
    }
}
