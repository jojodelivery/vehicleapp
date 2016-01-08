package com.pin91.jojovehicleapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.LoginDO;
import com.pin91.jojovehicleapp.model.User;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.network.ErrorMessages;
import com.pin91.jojovehicleapp.network.requests.LoginRequest;
import com.pin91.jojovehicleapp.network.requests.SaveGCMKeyRequest;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;
import com.pin91.jojovehicleapp.utils.HttpAsyncTask;
import com.pin91.jojovehicleapp.utils.JojoUtils;
import com.pin91.jojovehicleapp.utils.MySQLiteHelper;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;


public class LoginActivity extends Activity {

    public static Integer userId;
    SharedPreferences sharedpreferences = null;
    private User user;
    boolean superBackPressed = false;

    static final String TAG = "Register Activity";

    @Override
    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ConnectionUtil.context = getApplicationContext();
        superBackPressed = false;

        // Version check for > 9 is removed as min API supported
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        user = MySQLiteHelper.fetchUser(getApplicationContext());
        sharedpreferences = getSharedPreferences("user_pref",
                Context.MODE_PRIVATE);
        if (user != null) {
            if (ConnectionUtil.isNetworkAvailable()) {
                autoLogin();
            } else {
                setContentView(R.layout.login);
                addListener();
                setUserCredentials();
                Toast.makeText(getApplicationContext(), ErrorMessages.UNABLE_TO_CONNECT, Toast.LENGTH_SHORT).show();
            }
        } else {
            setContentView(R.layout.login);
            addListener();
        }
    }

    public void login(View view) {
      //  DTSCommonUtil.showProgressBar(this);
        View inputView = this.getCurrentFocus();
        if (inputView != null) {
            InputMethodManager inputManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(inputView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if (isUserInputValid()) {
            new HttpAsyncTask<Void, LoginDO, LoginDO>() {

                @Override
                protected LoginDO doInBackground(Void... params) {
                    return LoginRequest.loginUser(getApplicationContext(), getUserNameText().trim(), getPasswordText().trim());
                }

                @Override
                protected void onPostExecute(LoginDO loginDO) {
                    if (loginDO != null) {
                        DTSCommonUtil.closeProgressBar();
                        userId = loginDO.getUserId();
                        User user = new User();
                        user.setUserName(getUserNameText());
                        user.setPassword(getPasswordText());
                        MySQLiteHelper.insertUser(user, getApplicationContext());
                        SharedPreferenceManager preferenceManager = SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext());
                        preferenceManager.saveUserName(getUserNameText());
                        preferenceManager.savePassword(getPasswordText());
                        preferenceManager.saveUserId(loginDO.getUserId());
                        registerGCM();
                        Intent dashboardIntent = new Intent(
                                LoginActivity.this, HomeActivity.class);
                        startActivity(dashboardIntent);
                    }
                    DTSCommonUtil.closeProgressBar();
                }
            }.execute();
        } else {
            DTSCommonUtil.closeProgressBar();
        }
    }

    public String registerGCM() {
        String regId = getRegistrationId(getApplicationContext());
        if (TextUtils.isEmpty(regId)) {
            registerInBackground();
        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }


    @SuppressWarnings("unchecked")
    private void registerInBackground() {
        SaveGCMKeyRequest.registerInBackground(getApplicationContext());
//        new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object... params) {
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(mContext);
//                    }
//                    regId = gcm.register("1078992195391");
//                    Map<String, String> gcmParam = new HashMap<String, String>();
//
//                    gcmParam.put("userId", sharedpreferences.getString("userId",
//                            ""));
//                    gcmParam.put("key", regId);
//                    ConnectionUtil.connectToBackEnd(gcmParam, "app/saveGCMKey");
//                    Editor editor = sharedpreferences.edit();
//                    editor.putString(REG_ID, regId);
//                    editor.commit();
//                } catch (IOException ex) {
//                    Log.d("Error", "Error: " + ex.getMessage());
//                }
//                return "";
//            }
//
//        }.execute(null, null, null);
    }

    private String getRegistrationId(Context context) {
        String registrationId = SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext()).getRegId();
        if (registrationId.isEmpty()) {
            return "";
        }
        return registrationId;
    }

    public void addListener() {

        final TextView forgetBtn = (TextView) findViewById(R.id.btnforgetPassword);

        if (forgetBtn != null)
            forgetBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setContentView(R.layout.forget_password);
                    superBackPressed = true;
                    addListener();

                    if (forgetBtn.getLinksClickable() == true) {
                        forgetBtn.setLinkTextColor(Color.BLUE);
                    }

                }
            });

        final TextView regBackButton = (TextView) findViewById(R.id.regBackBtnId);

        if (regBackButton != null)
            regBackButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setContentView(R.layout.login);
                    superBackPressed = false;
                    addListener();
                }
            });
    }

    public void autoLogin() {
       // DTSCommonUtil.showProgressBar(this);
        new AsyncTask<Void, LoginDO, LoginDO>(){
            @Override
            protected LoginDO doInBackground(Void... params) {
                return LoginRequest.loginUser(getApplicationContext(), user.getUserName(), user.getPassword());
            }

            @Override
            protected void onPostExecute(LoginDO loginDO) {
                DTSCommonUtil.closeProgressBar();
                if (loginDO == null) {
                    setContentView(R.layout.login);
                    addListener();
                    setUserCredentials();
                } else if (loginDO.isSuccess()) {
                    userId = loginDO.getUserId();
                    SharedPreferenceManager preferenceManager = SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext());
                    preferenceManager.saveUserId(loginDO.getUserId());
                    preferenceManager.savePassword(user.getPassword());
                    preferenceManager.saveUserName(user.getUserName());
                    Intent dashboardIntent = new Intent(
                            LoginActivity.this, HomeActivity.class);
                    startActivity(dashboardIntent);
                } else {
                    Toast.makeText(LoginActivity.this, ErrorMessages.LOGIN_FAILURE,
                            Toast.LENGTH_LONG).show();
                    setContentView(R.layout.login);
                    addListener();
                    setUserCredentials();
                }
            }
        }.execute();
    }

    public void setUserCredentials() {
        TextView userName = (TextView) findViewById(R.id.usernameId);
        TextView password = (TextView) findViewById(R.id.passwordId);

        userName.setText(user.getUserName());
        password.setText(user.getPassword());

    }

    private boolean isUserInputValid() {
        String usernameText = getUserNameText();
        String passwordText = getPasswordText();
        if (JojoUtils.isNullOrEmpty(usernameText)) {
            DTSCommonUtil.closeProgressBar();
            Toast.makeText(LoginActivity.this, "Enter UserName",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (JojoUtils.isNullOrEmpty(passwordText)) {
            DTSCommonUtil.closeProgressBar();
            Toast.makeText(LoginActivity.this, "Enter Password",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getUserNameText() {
        Editable userNameEditText = ((EditText) findViewById(R.id.usernameId)).getText();
        return JojoUtils.getStringFromEditable(userNameEditText);
    }


    private String getPasswordText() {
        Editable passwordEditText = ((EditText) findViewById(R.id.passwordId)).getText();
        return JojoUtils.getStringFromEditable(passwordEditText);
    }
}
