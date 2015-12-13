package com.pin91.jojovehicleapp.activity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.LoginDO;
import com.pin91.jojovehicleapp.model.User;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;
import com.pin91.jojovehicleapp.utils.MySQLiteHelper;


public class LoginActivity extends Activity {
	
	GoogleCloudMessaging gcm;
	public static Context mContext = null;
	public static Integer userId;
	private MySQLiteHelper mySQLiteHelper = null;
	SharedPreferences sharedpreferences = null;
	private User user;
	boolean superBackPressed = false;
	String regId;

	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	static final String TAG = "Register Activity";
	
	@Override
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = LoginActivity.this;
		ConnectionUtil.context = LoginActivity.this;
		superBackPressed = false;

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		File database = getApplicationContext().getDatabasePath(
				"databasename.db");

		if (!database.exists()) {
			mySQLiteHelper = new MySQLiteHelper(getApplicationContext());
		} else {

		}

		user = MySQLiteHelper.fetchUser(getApplicationContext());

		sharedpreferences = getSharedPreferences("user_pref",
				Context.MODE_PRIVATE);
		if (user != null) {

			if (ConnectionUtil.isNetworkAvailable()) {
				// new HttpAsyncTask().execute("");
				autoLogin1();
			} else {
				setContentView(R.layout.login);
				addListener();
				if (Build.VERSION.SDK_INT >= 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}

				TextView userName = (TextView) findViewById(R.id.usernameId);
				TextView password = (TextView) findViewById(R.id.passwordId);

				userName.setText(user.getUserName());
				password.setText(user.getPassword());

				Toast.makeText(LoginActivity.mContext,
						"No Internet.Check Your Connection.",
						Toast.LENGTH_SHORT).show();

			}

		} else {
			setContentView(R.layout.login);
			addListener();

		}

	}

	public void login(View view) {
		DTSCommonUtil.showProgressBar(LoginActivity.this);
		View inputView = this.getCurrentFocus();
		if (inputView != null) {
			InputMethodManager inputManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(inputView.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				EditText userNameEditText = (EditText) findViewById(R.id.usernameId);
				EditText passwordEditText = (EditText) findViewById(R.id.passwordId);

				if (userNameEditText.getText().length() == 0) {
					DTSCommonUtil.closeProgressBar();
					Toast.makeText(LoginActivity.this, "Enter UserName",
							Toast.LENGTH_SHORT).show();
				} else if (passwordEditText.getText().length() == 0) {
					DTSCommonUtil.closeProgressBar();
					Toast.makeText(LoginActivity.this, "Enter Password",
							Toast.LENGTH_SHORT).show();
				} else {

					Map<String, String> paramsMap = new HashMap<String, String>();

					paramsMap.put("userName", ""
							+ userNameEditText.getText().toString().trim());
					paramsMap.put("password", ""
							+ passwordEditText.getText().toString().trim());

					String response = ConnectionUtil.connectToBackEnd(
							paramsMap, "appLogin");

					if (response == null || response == "") {
						DTSCommonUtil.closeProgressBar();
						if (ConnectionUtil.NO_INTERNET_MESSAGE_SHOWN == "false")
							Toast.makeText(
									LoginActivity.this,
									ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE,
									Toast.LENGTH_LONG).show();
						return;
					}

					LoginDO loginDO = null;

					ObjectMapper objectMapper = new ObjectMapper();
					try {
						loginDO = objectMapper.readValue(response,
								LoginDO.class);

						if (loginDO.isSuccess()==true) {

							userId = loginDO.getUserId();

							User u = new User();
							u.setUserName(userNameEditText.getText().toString()
									.trim());
							u.setPassword(passwordEditText.getText().toString()
									.trim());

							MySQLiteHelper.insertUser(u,
									getApplicationContext());

							Editor editor = sharedpreferences.edit();
							editor.putString("userName", userNameEditText
									.getText().toString().trim());
							editor.putString("password", passwordEditText
									.getText().toString().trim());
							editor.putString("userId", ""+loginDO.getUserId());
							editor.commit();
							
							registerGCM();

							Intent dashboardIntent = new Intent(
									LoginActivity.this, HomeActivity.class);
							startActivity(dashboardIntent);
							DTSCommonUtil.closeProgressBar();

						} else {
							DTSCommonUtil.closeProgressBar();
							Toast.makeText(
									LoginActivity.this,
									"Login Failure.Check UserName/Password Incorrect",
									Toast.LENGTH_LONG).show();
						}
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}, 1000);

	}

	//
	public String registerGCM() {
	
		regId = getRegistrationId(mContext);

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
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params)  {
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(mContext);
					}
					regId = gcm.register("1078992195391");
					Map<String, String> gcmParam = new HashMap<String, String>();

					gcmParam.put("userId",sharedpreferences.getString("userId",
							""));
					gcmParam.put("key", regId);
					ConnectionUtil.connectToBackEnd(gcmParam, "app/saveGCMKey");
					Editor editor = sharedpreferences.edit();
					editor.putString(REG_ID, regId);
					editor.commit();
				} catch (IOException ex) {
					Log.d("Error", "Error: " + ex.getMessage());
				}
				return "";
			}
			
		}.execute(null, null, null);
	}

	private String getRegistrationId(Context context) {
		String registrationId = sharedpreferences.getString(REG_ID, "");
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

	public String autoLogin1() {
		autoLogin();

		return "";
	}

	public void autoLogin() {

		Map<String, String> paramsMap = new HashMap<String, String>();

		paramsMap.put("userName", "" + user.getUserName());
		paramsMap.put("password", "" + user.getPassword());

		String response = ConnectionUtil.connectToBackEnd(paramsMap, "appLogin");

		if (response == null || response == "") {
			setContentView(R.layout.login);
			addListener();

			TextView userName = (TextView) findViewById(R.id.usernameId);
			TextView password = (TextView) findViewById(R.id.passwordId);

			userName.setText(user.getUserName());
			password.setText(user.getPassword());
			if (ConnectionUtil.NO_INTERNET_MESSAGE_SHOWN.equals("false"))
				Toast.makeText(LoginActivity.mContext,
						ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE,
						Toast.LENGTH_LONG).show();
			return;
		}

		LoginDO loginDO = null;

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			loginDO = objectMapper.readValue(response, LoginDO.class);

			if (loginDO.isSuccess()==true) {
				userId = loginDO.getUserId();
				Editor editor = sharedpreferences.edit();
				editor.putString("userName", user.getUserName());
				editor.putString("password", user.getPassword());
				editor.putString("userId",""+ loginDO.getUserId());
				editor.commit();

				/*Intent dashboardIntent = new Intent(this,
						DashboardActivity.class);
				startActivity(dashboardIntent);*/
				Intent landingIntent = new Intent(this,
						HomeActivity.class);
				startActivity(landingIntent);

			} else {
				Toast.makeText(LoginActivity.this, "Login Failure",
						Toast.LENGTH_LONG).show();
				setContentView(R.layout.login);
				addListener();
				setUserCredentials();
			}
		} catch (JsonParseException e) {
			Toast.makeText(this, "Login Failure", Toast.LENGTH_LONG).show();
			setContentView(R.layout.login);
			addListener();
			setUserCredentials();
			// setContentView(R.layout.login);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			Toast.makeText(this, "Login Failure", Toast.LENGTH_LONG).show();
			setContentView(R.layout.login);
			addListener();
			setUserCredentials();
			// setContentView(R.layout.login);
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "Login Failure", Toast.LENGTH_LONG).show();
			setContentView(R.layout.login);
			addListener();
			setUserCredentials();
			// setContentView(R.layout.login);
			e.printStackTrace();
		}

	}

	public void setUserCredentials() {
		TextView userName = (TextView) findViewById(R.id.usernameId);
		TextView password = (TextView) findViewById(R.id.passwordId);

		userName.setText(user.getUserName());
		password.setText(user.getPassword());

	}
}
