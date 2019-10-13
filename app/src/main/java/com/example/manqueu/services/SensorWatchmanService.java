package com.example.manqueu.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.JsonRequest;
import com.example.manqueu.helpers.JsonRequestUtil;
import com.example.manqueu.helpers.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class SensorWatchmanService extends Service implements SensorEventListener {
    public static final String TAG = SensorWatchmanService.class.getName();

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;

    public SensorWatchmanService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Invoke background service onCreate method.", Toast.LENGTH_LONG).show();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null)
            stopSelf();
        else
            sensorManager
                    .registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        super.onCreate();
    }

    public void playNotificationSound(Context context) {
        // This method is only for debugging purpose
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            Log.i(TAG, "Playing notification");
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "onSensorChanged().");
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.i(TAG, "TYPE_PROXIMITY");
            if (event.values[0] < event.sensor.getMaximumRange()) {
                Log.i(TAG, "Sensor detected something");
                sendNotification();

                playNotificationSound(this);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "onAccuracyChanged().");
        Log.i(TAG+",New Accuracy Value: ", String.valueOf(accuracy));
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Invoke background service onDestroy method.", Toast.LENGTH_LONG).show();
    }

    private void sendNotification() {

        NOTIFICATION_TITLE = "Some Title";
        NOTIFICATION_MESSAGE = "Some message";

        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }

        JsonRequest jsonObjectRequest = JsonRequestUtil.getJsonRequest(this, notifcationBody);

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
