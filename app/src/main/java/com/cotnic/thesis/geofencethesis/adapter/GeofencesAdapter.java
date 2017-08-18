package com.cotnic.thesis.geofencethesis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cotnic.thesis.geofencethesis.R;
import com.cotnic.thesis.geofencethesis.model.GeofenceModel;

import java.util.ArrayList;

/**
 * Created by cotnic on 18. 08. 2017.
 */

public class GeofencesAdapter extends ArrayAdapter<GeofenceModel> {
    public GeofencesAdapter(Context context, ArrayList<GeofenceModel> geofences) {
        super(context, 0, geofences);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GeofenceModel geofence = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_geofence, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvLat = (TextView) convertView.findViewById(R.id.tvLat);
        TextView tvLng = (TextView) convertView.findViewById(R.id.tvLng);
        TextView tvRadius = (TextView) convertView.findViewById(R.id.tvRadius);
        TextView tvTran = (TextView) convertView.findViewById(R.id.tvTran);
        // Populate the data into the template view using the data object
        tvName.setText(geofence.getREQ_ID());
        tvLat.setText("" + geofence.getLocation().latitude);
        tvLng.setText("" + geofence.getLocation().longitude);
        tvRadius.setText("" + geofence.getRadius());
        tvTran.setText((geofence.getTransition() == 1) ? "ENTER" : "EXIT");
        // Return the completed view to render on screen
        return convertView;
    }
}
