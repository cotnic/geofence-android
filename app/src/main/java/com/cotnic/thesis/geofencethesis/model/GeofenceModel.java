package com.cotnic.thesis.geofencethesis.model;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by cotnic on 18. 08. 2017.
 */

public class GeofenceModel {
    private String REQ_ID;
    private LatLng location;
    private float radius;
    private int transition;
    private int responsivenes;
    private long expire;

    public GeofenceModel(String REQ_ID, LatLng location, float radius,
                         int transition, int responsivenes, long expire) {
        this.REQ_ID = REQ_ID;
        this.location = location;
        this.radius = radius;
        this.transition = transition;
        this.responsivenes = responsivenes;
        this.expire = Geofence.NEVER_EXPIRE;
    }

    public String getREQ_ID() {
        return REQ_ID;
    }

    public void setREQ_ID(String REQ_ID) {
        this.REQ_ID = REQ_ID;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getTransition() {
        return transition;
    }

    public void setTransition(int transition) {
        this.transition = transition;
    }

    public int getResponsivenes() {
        return responsivenes;
    }

    public void setResponsivenes(int responsivenes) {
        this.responsivenes = responsivenes;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
