package com.pin91.jojovehicleapp.network.requests;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by udit on 12/17/2015.
 */
public class SaveGCMKeyRequest {

    private static final String REQUEST_SUB_URL = "app/saveGCMKey";
    private static final String GCM_REGISTRATION_KEY = "1078992195391";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_KEY = "key";

    public static void registerInBackground(final Context context) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                GoogleCloudMessaging gcm;
                try {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    String regId = gcm.register(GCM_REGISTRATION_KEY);
                    Map<String, String> gcmParam = new HashMap<String, String>();

                    SharedPreferenceManager preferenceManager = SharedPreferenceManager.getSharedPreferenceManager(context);
                    gcmParam.put(KEY_USER_ID, preferenceManager.getUserId());
                    gcmParam.put(KEY_KEY, regId);
                    ConnectionUtil.connectToBackEnd(gcmParam, REQUEST_SUB_URL);
                    preferenceManager.saveRegId(regId);
                } catch (IOException ex) {
                    Log.d("Error", "Error: " + ex.getMessage());
                }
                return "";
            }

        }.execute(null, null, null);
    }
}
