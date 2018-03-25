package com.example.kamil.skitracker;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private TextView textLen;
    private TextView textLon;
    private boolean attach = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        TextView textView = rootView.findViewById(R.id.textViewDate);
        TextView textView1 = rootView.findViewById(R.id.textDystans);

        textLen = rootView.findViewById(R.id.textLen);
        textLon = rootView.findViewById(R.id.textLongitude);

        String title = "0,0 km/h";

        final SpannableString spannableString = new SpannableString(title);
        int position = 0;

        spannableString.setSpan(new RelativeSizeSpan(1.5f), position,  position + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView1.setText(spannableString, TextView.BufferType.SPANNABLE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.mm.yyyy", Locale.getDefault());
        textView.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));

        Log.i("Tag", "Create view");
        if(location != null){
            String[] strings = MathHelper.convertLatLngDecimalToDMS((float)this.location.getLatitude(), (float)this.location.getLongitude());
            textLen.setText(strings[0]);
            textLon.setText(strings[1]);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void Update(Location location) {

        this.location = location;
        String[] strings = MathHelper.convertLatLngDecimalToDMS((float)this.location.getLatitude(), (float)this.location.getLongitude());
        textLen.setText(strings[0]);
        textLon.setText(strings[1]);

        Log.i("LocationMainFragment", "done");
    }

    @Override
    public void setAttached(boolean attach) {
        this.attach = attach;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        Log.i("Tag", "Loc set");
    }

    @Override
    public boolean isAttached() {
        return attach;
    }
}
