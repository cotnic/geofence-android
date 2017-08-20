package com.cotnic.thesis.geofencethesis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class LogsFragment extends Fragment {
    private static final String TAG = LogsFragment.class.getSimpleName();

    private TextView mLogView;
    private View mView;
    private Switch mSwitchGeofenceLog;
    private Switch mSwitchLocationLog;
    private Button mButtonShowLogs;


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

        mSwitchGeofenceLog = (Switch) mView.findViewById(R.id.geofence_switch);
        mSwitchGeofenceLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.wtf(TAG, "Geofence is checked!");
                } else {
                    Log.wtf(TAG, "Geofence is unchecked!");
                }
            }
        });

        mSwitchLocationLog = (Switch) mView.findViewById(R.id.location_switch);
        mSwitchLocationLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.wtf(TAG, "Geofence is checked!");
                } else {
                    Log.wtf(TAG, "Geofence is unchecked!");
                }
            }
        });

        mButtonShowLogs = (Button) mView.findViewById(R.id.btnGeofence);
        mButtonShowLogs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.wtf(TAG, "Show data from log file about triggered Geofences");
            }
        });

        return mView;
    }
}
