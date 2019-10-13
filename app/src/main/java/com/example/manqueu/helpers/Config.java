package com.example.manqueu.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.manqueu.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String TAG = "Config";

    public static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file.");
        }

        return null;
    }
}
