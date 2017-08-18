package com.cotnic.thesis.geofencethesis;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.cotnic.thesis.geofencethesis.logging.EventLogging;
import com.cotnic.thesis.geofencethesis.model.GeofenceList;
import com.cotnic.thesis.geofencethesis.model.GeofenceModel;
import com.cotnic.thesis.geofencethesis.services.GeofenceService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private EventLogging eventLogging;

    private static final int PERMISSIONS_CODE = 20002;

    private GeofencingClient mGeofencingClient;

    private PendingIntent mGeofencePendingIntent;

    private ArrayList<Geofence> mGeofenceList;

    private Fragment selectedFragment = null;

    private GoogleApiClient mGoogleApiClient;

    private FusedLocationProviderApi mFusedLocationApi;

    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        eventLogging = new EventLogging(this.getApplicationContext(), TAG);

        setContentView(R.layout.activity_main);

        checkLocationPermission();

        mGeofenceList = new ArrayList<>();

        mGeofencePendingIntent = null;

        mFusedLocationApi = LocationServices.FusedLocationApi;

        getGoogleApiClient();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        selectedFragment = new GMapFragment();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();

        createGeofenceList();

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        addGeofences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Call GoogleApiClient connection when starting the Activity
        mGoogleApiClient.reconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect GoogleApiClient when stopping Activity
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    selectedFragment = new GMapFragment();
                    break;
                case R.id.navigation_geo_list:
                    selectedFragment = new GeoListFragment();
                    break;
                case R.id.navigation_log:
                    selectedFragment = new LogsFragment();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    /**
     * Method that gets a googleAPIClient Instance for Location service to get current location and updating it
     */
    public void getGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {                 // Callback that handles connections
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.wtf(TAG, "Connected to GoogleApiClient");
                        createLocationRequest();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.wtf(TAG, "Suspended connection to GoogleApiClient");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {   // Callback that handles failed connections
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.wtf(TAG, "Failed to connect to GoogleApiClient - " +
                                connectionResult.getErrorMessage());
                    }
                })
                .build();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    @SuppressWarnings("MissingPermission")
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;
                eventLogging.loggingLocationUpdates("Lat: " + mCurrentLocation.getLatitude() + ", Lng: " + mCurrentLocation.getLongitude());
                if (selectedFragment instanceof GMapFragment)
                    ((GMapFragment) selectedFragment).setMapCurrentLocation(mCurrentLocation);
            }
        });

    }

    public void createGeofenceList() {
        GeofenceList geofences = new GeofenceList();
        ArrayList<GeofenceModel> geofenceList = (ArrayList) geofences.returnGeofences();
        for (GeofenceModel geofence : geofenceList) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(geofence.getREQ_ID())
                    .setCircularRegion(
                            geofence.getLocation().latitude,
                            geofence.getLocation().longitude,
                            geofence.getRadius()
                    )
                    .setExpirationDuration(geofence.getExpire())
                    .setTransitionTypes(geofence.getTransition())
                    .build());
        }
    }

    @SuppressWarnings("MissingPermission")
    private void addGeofences() {
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.wtf(TAG, "addGeofences() - succesfully added");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.wtf(TAG, "addGeofences() - failed to add");
                    }
                });
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.wtf(TAG, "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.wtf(TAG, "Permission was granted");
                } else {
                    Log.wtf(TAG, "Permission was denied!");
                }
                return;
            }
        }
    }
}
