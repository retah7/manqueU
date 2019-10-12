package com.example.manqueu;

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
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class SensorWatchman extends Service implements SensorEventListener {
    public static final String TAG = SensorWatchman.class.getName();

    public SensorWatchman() {
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
}
