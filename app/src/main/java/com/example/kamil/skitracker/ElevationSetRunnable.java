package com.example.kamil.skitracker;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

public class ElevationSetRunnable implements Runnable {

    private TextView textCurrentElevation;
    private String elevation;

    public ElevationSetRunnable(Activity activity, String elevation){
        textCurrentElevation = activity.findViewById(R.id.text_elevation);
        this.elevation = elevation;
    }
    @Override
    public void run() {
        if(textCurrentElevation != null){
            textCurrentElevation.setText(elevation);
            textCurrentElevation.setTextColor(Color.BLACK);
        }
    }
}
