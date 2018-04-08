package com.example.kamil.skitracker;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public class GoogleElevationThread extends Thread {

    private Activity activity;
    private long updateTime;
    private boolean update = true;
    private LocationOptions locationOptions;

    public GoogleElevationThread(Activity activity, LocationOptions locationOptions, int updateTimeInSeconds){

        this.activity = activity;
        this.locationOptions = locationOptions;
        this.updateTime = updateTimeInSeconds * 1000;

    }
    public void run() {
        while (update){
            try {
                double lon;
                double lat;
                String elevation = "0";
                if(locationOptions.getLocation() != null){
                    lat = locationOptions.getLocation().getLatitude();
                    lon = locationOptions.getLocation().getLongitude();
                    elevation = lookingForAltitude(lon, lat);
                    Log.i("GoogleElevation", elevation);
                }
                else
                    Log.i("MainFragment", "Null");
                Objects.requireNonNull(activity).runOnUiThread(new ElevationSetRunnable(activity, elevation));
                Thread.sleep(updateTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public void stopMe(){
        update = false;
    }
    private String lookingForAltitude(double latitude, double longitude){

        String result = "";

        try {
            final String path = "https://maps.googleapis.com/maps/api/elevation/"
                    + "xml?locations=" + String.valueOf(longitude)
                    + "," + String.valueOf(latitude)
                    + "&sensor=true&key=AIzaSyDbvSINEzXGOYYDwaXsRMeJmqFlP7H93Ps";
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader isReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isReader);
            String line;
            while ( (line = bufferedReader.readLine()) != null){
                result += line;
            }
            String tagOpen = "<elevation>";
            String tagClose = "</elevation>";
            int start = result.indexOf(tagOpen) + tagOpen.length();
            int end = result.indexOf(tagClose);
            result = result.substring(start, end);

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        double temp;
        if(!result.isEmpty())
            temp = Double.parseDouble(result);
        else
            temp = 0;

        return String.format(Locale.US, "%.0f", temp);
    }

}
