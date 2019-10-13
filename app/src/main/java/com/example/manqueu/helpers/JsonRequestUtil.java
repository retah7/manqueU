package com.example.manqueu.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonRequestUtil {

    private static final String TAG = "JsonRequestUtil";

    public static JsonObjectRequest getJsonRequest(final Context context, JSONObject payload) {

        JSONObject notification = new JSONObject();
        try {
            notification.put("to", Config.getConfigValue(context, "TOPIC"));
            notification.put("data", payload);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }

        return new JsonObjectRequest(Config.getConfigValue(context,"FCM_API"), notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "key="+Config.getConfigValue(context, "SERVER_KEY"));
                params.put("Content-Type", Config.getConfigValue(context, "CONTENT_TYPE"));
                return params;
            }
        };
    }
}
