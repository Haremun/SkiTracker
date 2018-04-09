package com.example.kamil.skitracker.Fragments;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
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

import com.example.kamil.skitracker.GoogleElevationThread;
import com.example.kamil.skitracker.Gui.ChronometerGui;
import com.example.kamil.skitracker.LocationFragment;
import com.example.kamil.skitracker.LocationInfo;
import com.example.kamil.skitracker.LocationOptions;
import com.example.kamil.skitracker.MathHelper;
import com.example.kamil.skitracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


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
    private TextView textElevation;

    private ChronometerGui chronometerGui;

    private GoogleElevationThread googleElevationThread;

    //private LocationInfo locationInfo;
    private boolean checkElevation = false;

    //Fragment life cycle ----------------------------------------------------------
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
        textElevation = rootView.findViewById(R.id.text_elevation);

        textMaxSpeed.setText(convertToSpannableString(0, 0), TextView.BufferType.SPANNABLE);
        textDistance.setText(convertToSpannableString(0, 1), TextView.BufferType.SPANNABLE);

        chronometerGui = new ChronometerGui(
                (Chronometer) rootView.findViewById(R.id.chronometer_time),
                (Chronometer)rootView.findViewById(R.id.chronoZjazd),
                (Chronometer)rootView.findViewById(R.id.chronoWyjazd),
                (Chronometer)rootView.findViewById(R.id.chronoRest));

        chronometerGui.setChronometers();

        if (locationOptions.timerStarted()){
            chronometerGui.startChronometer(R.id.chronometer_time, locationOptions.getTimeOfUpdates());
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


        googleElevationThread = new GoogleElevationThread(getActivity(), locationOptions, 5);
        googleElevationThread.start();


        return rootView;
    }

    @Override
    public void onDestroyView() {
        try {
            googleElevationThread.interrupt();
            googleElevationThread.stopMe();
            googleElevationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroyView();
    }

    //Interface functions ---------------------------------------------------------
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

        //textElevation.setText(lookingForAltitude(location.getLatitude(), location.getLongitude()));
        //textElevation.setTextColor(Color.BLACK);

        Log.i("MainFragment", "Done Update");
    }

    @Override
    public void setLocation(LocationOptions locationOptions) {
        this.location = locationOptions.getLocation();
        this.locationOptions = locationOptions;
        Log.i("Tag", "Loc set");
    }

    //Menu on action bar -------------------------------------------------------------
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.action_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_play:{
                if(!locationOptions.timerStarted()){
                    chronometerGui.startChronometer(R.id.chronometer_time);
                    locationOptions.setTimeOfUpdates(SystemClock.elapsedRealtime());
                    locationOptions.setStartTimers(true);
                    locationOptions.startUpdates();
                } else {
                    chronometerGui.stopChrometer(R.id.chronometer_time);
                    locationOptions.setStartTimers(false);
                    locationOptions.stopUpdates();
                }
                Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(locationOptions.timerStarted())
            menu.getItem(0).setIcon(R.drawable.ic_pause);
        else
            menu.getItem(0).setIcon(R.drawable.ic_music_player_play);
        super.onPrepareOptionsMenu(menu);
    }

    //private Methods ----------------------------------------------------------------
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

}

