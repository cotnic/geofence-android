package com.cotnic.thesis.geofencethesis.model;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cotnic on 18. 08. 2017.
 */

public class GeofenceList {
    private List<GeofenceModel> geofenceList;

    public GeofenceList() {
        geofenceList = new ArrayList<>();
        geofenceList.add(new GeofenceModel("BF", new LatLng(46.050043, 14.473011), 200,
                        Geofence.GEOFENCE_TRANSITION_ENTER, 1000, Geofence.NEVER_EXPIRE));
        geofenceList.add(new GeofenceModel("Dom 1 - SDL", new LatLng(46.050412, 14.487991), 30,
                Geofence.GEOFENCE_TRANSITION_EXIT, 1000, Geofence.NEVER_EXPIRE));
    }

    public List<GeofenceModel> returnGeofences() {
        return geofenceList;
    }
}
