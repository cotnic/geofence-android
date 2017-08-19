package com.cotnic.thesis.geofencethesis;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cotnic.thesis.geofencethesis.model.GeofenceList;
import com.cotnic.thesis.geofencethesis.model.GeofenceModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class GMapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = GMapFragment.class.getSimpleName();

    LatLng mapCurrentLocation;
    Marker mMarkerLocation;

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_map, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    /**
     * Initializes Google Map Fragment for user
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.wtf(TAG, "onMapReady()");
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void setMapCurrentLocation(Location location) {
        boolean setZoom = false;
        if(mapCurrentLocation == null)
            setZoom = true;
        mapCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMarkerLocation != null) {
            mMarkerLocation.remove();
        }
        mMarkerLocation = mGoogleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(mapCurrentLocation)
                .title("Trenutna pozicija")
                .snippet("Pozicija, kjer se trenutno nahajate!"));
        if(setZoom) {
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(mapCurrentLocation)
                    .zoom(16)
                    .bearing(0)
                    .build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        createGeofenceMarkers();
    }

    private void createGeofenceMarkers() {
        GeofenceList geofences = new GeofenceList();
        ArrayList<GeofenceModel> geofenceList = (ArrayList) geofences.returnGeofences();
        for (GeofenceModel geofence : geofenceList) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(geofence.getLocation())
                    .title(geofence.getREQ_ID())
                    .snippet((geofence.getTransition() == 1) ? "ENTER" : "EXIT"));
            mGoogleMap.addCircle(createGeofenceCircle(geofence.getLocation(), geofence.getRadius()));
        }
    }

    private CircleOptions createGeofenceCircle(LatLng center, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(center);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.BLUE);
        circleOptions.fillColor(0x30ff0000);
        circleOptions.strokeWidth(2);
        return circleOptions;
    }
}
