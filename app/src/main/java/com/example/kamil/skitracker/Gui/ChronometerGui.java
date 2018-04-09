package com.example.kamil.skitracker.Gui;

import android.graphics.Color;
import android.os.SystemClock;
import android.widget.Chronometer;

import java.util.Arrays;
import java.util.List;

public class ChronometerGui {

    private List<Chronometer> chronometerList;

    public ChronometerGui(Chronometer... chronometers){

        chronometerList = Arrays.asList(chronometers);

    }

    public void setChronometers(){
        for (Chronometer chronometer: chronometerList) {
            chronometer.setText("00:00:00");
            chronometer.setTextColor(Color.GRAY);
            chronometer.setOnChronometerTickListener(chronometerTickListener());
        }
    }

    public void startChronometer(int id){
        for (Chronometer chronometer:
             chronometerList) {
            chronometer.setTextColor(Color.BLACK);
            if(chronometer.getId() == id){
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        }
    }
    public void startChronometer(int id, long baseTime){
        for (Chronometer chronometer:
                chronometerList) {
            chronometer.setTextColor(Color.BLACK);
            if(chronometer.getId() == id){
                chronometer.setBase(baseTime);
                chronometer.start();
            }
        }
    }

    public void stopChrometer(int id){
        for (Chronometer chronometer:
                chronometerList) {
            chronometer.setTextColor(Color.BLACK);
            if(chronometer.getId() == id){
                chronometer.stop();
            }
        }
    }

    private Chronometer.OnChronometerTickListener chronometerTickListener(){
        return new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h = (int)(time / 3600000);
                int m = (int)(time - h * 3600000) / 60000;
                int s = (int)(time - h * 3600000 - m * 60000) / 1000 ;

                chronometer.setText(getStringFromTime(h, m, s));
            }
        };
    }

    private String getStringFromTime(int hours, int minutes, int seconds){
        String hh = hours < 10 ? "0" + hours : hours + "";
        String mm = minutes < 10 ? "0" + minutes : minutes + "";
        String ss = seconds < 10 ? "0" + seconds : seconds + "";
        return hh + ":" + mm + ":" + ss;
    }
}
