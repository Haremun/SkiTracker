package com.example.kamil.skitracker;

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
}
