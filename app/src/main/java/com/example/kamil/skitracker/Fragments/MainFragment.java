package com.example.kamil.skitracker.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.kamil.skitracker.LocationFragment;
import com.example.kamil.skitracker.LocationInfo;
import com.example.kamil.skitracker.LocationOptions;
import com.example.kamil.skitracker.MathHelper;
import com.example.kamil.skitracker.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeoutException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LocationFragment {


    public MainFragment() {
        // Required empty public constructor
    }

    private Location location;
    private LocationOptions locationOptions;

    private TextView textLen;
    private TextView textLon;
    private TextView textMaxSpeed;
    private TextView textCurrentSpeed;
    private TextView textAvSpeed;
    private TextView textDistance;

    private Chronometer chronometer;

    //private LocationInfo locationInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        locationOptions.setCurrentFragment(this);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        TextView textView = rootView.findViewById(R.id.textViewDate);

        textMaxSpeed = rootView.findViewById(R.id.textMaxSpeed);
        textCurrentSpeed = rootView.findViewById(R.id.textCurrentSpeed);
        textAvSpeed = rootView.findViewById(R.id.textAvSpeed);
        textLen = rootView.findViewById(R.id.textLen);
        textLon = rootView.findViewById(R.id.textLongitude);
        textDistance = rootView.findViewById(R.id.textDystans);

        chronometer = rootView.findViewById(R.id.chronometer_time);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int)(time / 3600000);
                int m = (int)(time - h * 3600000) / 60000;
                int s = (int)(time - h * 3600000 - m * 60000) / 1000 ;

                cArg.setText(getStringFromTime(h, m, s));
            }
        });

        if (locationOptions.timerStarted()){
            chronometer.setBase(locationOptions.getTimeOfUpdates());
            chronometer.start();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.mm.yyyy", Locale.getDefault());
        textView.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));


        if(location != null){
            String[] strings = MathHelper.convertLatLngDecimalToDMS((float)this.location.getLatitude(), (float)this.location.getLongitude());
            textLen.setText(strings[0]);
            textLen.setTextColor(getResources().getColor(R.color.black));
            textLon.setText(strings[1]);
            textLon.setTextColor(getResources().getColor(R.color.black));
            Log.i("MainFragment", "Location on Create view");

        }
        return rootView;
    }

    @Override
    public void Update(LocationInfo locationInfo) {

        this.location = locationInfo.getCurrentLocation();
        String[] strings = MathHelper.convertLatLngDecimalToDMS((float)this.location.getLatitude(), (float)this.location.getLongitude());
        textLen.setText(strings[0]);
        textLen.setTextColor(Color.BLACK);
        textLon.setText(strings[1]);
        textLon.setTextColor(Color.BLACK);

        textDistance.setText(convertToSpannableString(locationInfo.getDistance(), 1), TextView.BufferType.SPANNABLE);
        textDistance.setTextColor(Color.BLACK);

        textMaxSpeed.setText(convertToSpannableString(locationInfo.getMaxSpeed(), 0), TextView.BufferType.SPANNABLE);
        textMaxSpeed.setTextColor(Color.BLACK);

        textAvSpeed.setText(convertToStringWithUnit(locationInfo.getAverageSpeed(), 0));
        textAvSpeed.setTextColor(Color.BLACK);

        textCurrentSpeed.setText(convertToStringWithUnit(locationInfo.getCurrentSpeed(), 0));
        textCurrentSpeed.setTextColor(Color.BLACK);

        Log.i("MainFragment", "Done Update");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.action_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_play:{
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

                locationOptions.setTimeOfUpdates(SystemClock.elapsedRealtime());
                locationOptions.setStartTimers(true);
                locationOptions.startUpdates();
                Log.i("MainFragment", "Button Clicked");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLocation(LocationOptions locationOptions) {
        this.location = locationOptions.getLocation();
        this.locationOptions = locationOptions;
        Log.i("Tag", "Loc set");
    }

    private SpannableString convertToSpannableString(double number, int unit){

        int end = 1;
        SpannableString spannableString;
        if(unit == 0)
            spannableString = new SpannableString(String.format(Locale.US, "%.1f", number) + " km/h");
        else if(unit == 1)
            spannableString = new SpannableString(String.format(Locale.US, "%.1f", number) + " km");
        else
            spannableString = new SpannableString(String.format(Locale.US, "%.1f", number));
        if(number > 9)
            end = 2;
        else if (number > 99)
            end = 3;

        spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private String convertToStringWithUnit(double number, int unit){
        if(unit == 0)
            return String.format(Locale.US, "%.1f",number) + " km/h";
        else if (unit == 1)
            return String.format(Locale.US, "%.1f",number) + " km";
        else
            return String.format(Locale.US, "%.1f",number);
    }

    private String getStringFromTime(int hours, int minutes, int seconds){
        String hh = hours < 10 ? "0" + hours : hours + "";
        String mm = minutes < 10 ? "0" + minutes : minutes + "";
        String ss = seconds < 10 ? "0" + seconds : seconds + "";
        return hh + ":" + mm + ":" + ss;
    }

}

