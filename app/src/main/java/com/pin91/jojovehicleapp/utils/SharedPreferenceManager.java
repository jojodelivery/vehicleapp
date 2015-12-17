package com.pin91.jojovehicleapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by udit on 12/17/2015.
 */
public class SharedPreferenceManager {

    public SharedPreferences sharedpreferences = null;
    private static final String SHARED_PREF_NAMESPACE = "user_pref";
    private static SharedPreferenceManager sharedPreferenceManager;

    private final String KEY_VEHICLE_ID = "vehicleId";
    private final String KEY_USERNAME  = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USERID = "userId";
    private final String KEY_REG_ID = "regId";
    private final String EMPTY_STRING = "";

    private SharedPreferenceManager(Context context){
        sharedpreferences = context.getSharedPreferences( SHARED_PREF_NAMESPACE,
                Context.MODE_PRIVATE);
    }

    public static SharedPreferenceManager getSharedPreferenceManager(Context context){
        if(sharedPreferenceManager == null){
            sharedPreferenceManager = new SharedPreferenceManager(context);
        }
        return sharedPreferenceManager;
    }

    public String getVehicleId() {
        return sharedpreferences.getString(KEY_VEHICLE_ID, EMPTY_STRING);
    }

    public String getUseraName() {
        return sharedpreferences.getString(KEY_USERNAME, EMPTY_STRING);
    }

    public String getPassword() {
        return sharedpreferences.getString(KEY_PASSWORD, EMPTY_STRING);
    }

    public String getUserId() {
        return sharedpreferences.getString(KEY_USERID, EMPTY_STRING);
    }

    public void saveUserName(String userName){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KEY_USERNAME, userName).commit();
    }
    public void savePassword(String password){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KEY_PASSWORD, password).commit();
    }

    public void saveUserId(int userId){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KEY_USERID, String.valueOf(userId)).commit();
    }
    public void saveVehicleId(int vehicleId){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KEY_USERID, String.valueOf(vehicleId)).commit();
    }

    public String getRegId(){
        return sharedpreferences.getString(KEY_REG_ID, EMPTY_STRING);
    }
    public void saveRegId(String regId){
        SharedPreferences.Editor  editor= sharedpreferences.edit();
        editor.putString(KEY_REG_ID, regId).commit();
    }
}