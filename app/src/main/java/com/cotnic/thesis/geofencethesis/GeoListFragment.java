package com.cotnic.thesis.geofencethesis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cotnic.thesis.geofencethesis.adapter.GeofencesAdapter;
import com.cotnic.thesis.geofencethesis.model.GeofenceList;
import com.cotnic.thesis.geofencethesis.model.GeofenceModel;

import java.util.ArrayList;


public class GeoListFragment extends Fragment {
    private static final String TAG = GeoListFragment.class.getSimpleName();

    View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_geo_list, container, false);
        GeofenceList geofenceList = new GeofenceList();
        ArrayList<GeofenceModel> geofences = (ArrayList) geofenceList.returnGeofences();
        GeofencesAdapter adapter = new GeofencesAdapter(getActivity(), geofences);
        ListView listView = (ListView) mView.findViewById(R.id.lvGeofences);
        listView.setAdapter(adapter);

        return mView;
    }
}
