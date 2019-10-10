package com.example.kamil.skitracker.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kamil.skitracker.Logger;
import com.example.kamil.skitracker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    TextView textView;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        textView = view.findViewById(R.id.textLogger);

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        textView.setText("");
        Logger.logsToTextView(textView);
    }
}
