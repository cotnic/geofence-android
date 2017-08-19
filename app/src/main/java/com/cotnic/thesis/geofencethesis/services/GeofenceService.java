package com.cotnic.thesis.geofencethesis.services;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.cotnic.thesis.geofencethesis.R;
import com.cotnic.thesis.geofencethesis.logging.EventLogging;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cotnic on 18. 08. 2017.
 */

public class GeofenceService extends IntentService {

    private static final String TAG = GeofenceService.class.getSimpleName();

    private EventLogging eventLogging;

    public GeofenceService() {
        super(TAG);
        eventLogging = new EventLogging(this.getApplicationContext(), TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = geofencingEvent.getErrorCode() + "";
            Log.wtf(TAG, errorMessage);
            return;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            Log.wtf(TAG, "Triggered geofence was: " + triggeringGeofences.get(0));

            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Log.wtf(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.wtf(TAG, "An error occured");
        }
    }

    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);
        eventLogging.loggingLocationUpdates("Triggered: " + triggeringGeofencesIdsString);

        return triggeringGeofencesIdsString;
    }

    private void sendNotification(String notificationDetails) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_notifications_black_24dp))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Geofence event has been triggered!")
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }
}
