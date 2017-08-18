package com.cotnic.thesis.geofencethesis.logging;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cotnic on 18. 08. 2017.
 */

public class EventLogging {

    private Context context;

    private String TAG;

    public EventLogging(Context ctx, String TAG) {
        this.context = ctx;
        this.TAG = TAG;
    }

    public void loggingLocationUpdates(String msg) {
        Log.wtf(TAG, "LocationUpdate: " + msg);
        String fileName = "location_update.log";
        writeToLogFile(msg, fileName);
    }

    public void loggingGeofenceTriggers(String msg) {
        Log.wtf(TAG, "Geofence triggered" + msg);
        String fileName = "geofence_trigger.log";
        writeToLogFile(msg, fileName);
    }

    private void writeToLogFile(String msg, String fileName) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE | Context.MODE_APPEND);
            outputStream.write((timeStamp() + " << " + msg + " >> " + System.getProperty("line.separator")).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String timeStamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
