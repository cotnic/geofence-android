package com.cotnic.thesis.geofencethesis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LogsFragment extends Fragment {

    private TextView mLogView;
    private View mView;
    private Button mButtonGeofences;
    private Button mButtonLocations;
    private Button mButtonClearGeofence;
    private Button mButtonClearLocation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_logs, container, false);

        mLogView = (TextView) mView.findViewById(R.id.tvLogging);
        mLogView.setText("Start logging");

        return mView;
    }
}
