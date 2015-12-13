package com.pin91.jojovehicleapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.PacketTrackingBean;
import com.pin91.jojovehicleapp.network.ConnectionUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class DropActivity extends Activity {
	List<PacketTrackingBean> finalNotificationList = new ArrayList<PacketTrackingBean>();
	Button prevBtn;
	Button nextBtn;
	TextView notificationMessage;
	Spinner dropNotificationSpinner = null;
	public static Context mContext;
	int currentNotificationNumber = 1;
	int totalCount = 0;
	List<String> spinnerList = new ArrayList<String>();
	ArrayAdapter<String> dataAdapter;
	boolean notificationCreated;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.drop);
		mContext = DropActivity.this;
		notificationMessage = (TextView) findViewById(R.id.notificationMessage);
		prevBtn = (Button) findViewById(R.id.prevButtonId);
		nextBtn = (Button) findViewById(R.id.nextButtonId);
		dropNotificationSpinner = (Spinner) findViewById(R.id.notificationId);
		
		dataAdapter = new ArrayAdapter<String>(
				DropActivity.this,
				android.R.layout.simple_spinner_item, spinnerList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropNotificationSpinner.setAdapter(dataAdapter);
		addListener();
		(new DropActivityViewLoader()).execute();
	}

	public void prevClick(View view) {
		notificationCreated = false;
		nextBtn.setEnabled(true);
		nextBtn.setBackgroundColor(Color.parseColor("#0099CC"));

		if (currentNotificationNumber != 1) {
			currentNotificationNumber = currentNotificationNumber - 1;
			dropNotificationSpinner.setSelection(currentNotificationNumber - 1);
			notificationCreated = true;
			createNotificationMessage();
		} else {
			prevBtn.setEnabled(false);
			prevBtn.setBackgroundColor(Color.parseColor("#eeeeee"));
		}

	}

	private void addListener() {
		OnItemSelectedListener countrySelectedListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> spinner, View container,
					int position, long id) {

				if (!notificationCreated) {
					currentNotificationNumber = position + 1;
					createNotificationMessage();
				} else {
					notificationCreated = false;
				}

				if (currentNotificationNumber == 1) {
					prevBtn.setEnabled(false);
					prevBtn.setBackgroundColor(Color.parseColor("#eeeeee"));
				} else {
					prevBtn.setEnabled(true);
					prevBtn.setBackgroundColor(Color.parseColor("#0099CC"));
				}

				if (!(currentNotificationNumber < totalCount)) {
					nextBtn.setEnabled(false);
					nextBtn.setBackgroundColor(Color.parseColor("#eeeeee"));
				} else {
					nextBtn.setEnabled(true);
					nextBtn.setBackgroundColor(Color.parseColor("#0099CC"));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		};

		dropNotificationSpinner
				.setOnItemSelectedListener(countrySelectedListener);
	}
	
	public void createNotificationMessage(){
		PacketTrackingBean packetTrackingBean=finalNotificationList.get(currentNotificationNumber-1); 
		notificationMessage.setText(packetTrackingBean.getMessage());
	}
	
	public void nextClick(View view) {
		notificationCreated = false;
		if (currentNotificationNumber < totalCount) {

			dropNotificationSpinner.setSelection(currentNotificationNumber);
			currentNotificationNumber = currentNotificationNumber + 1;
			notificationCreated = true;
			createNotificationMessage();
			prevBtn.setEnabled(true);
			prevBtn.setBackgroundColor(Color.parseColor("#0099CC"));
		} else {
			nextBtn.setEnabled(false);
			nextBtn.setBackgroundColor(Color.parseColor("#eeeeee"));
		}
	}
	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

	private class DropActivityViewLoader extends
			AsyncTask<String, Void, List<PacketTrackingBean>> {
		private final ProgressDialog dialog = new ProgressDialog(mContext);
		boolean isErrorOccured = false;

		@Override
		protected void onPostExecute(List<PacketTrackingBean> result) {
			prevBtn.setVisibility(View.VISIBLE);
			nextBtn.setVisibility(View.VISIBLE);
			dropNotificationSpinner.setVisibility(View.VISIBLE);
			dialog.dismiss();
			if (result != null && result.size() > 0) {
				finalNotificationList.addAll(result);
				super.onPostExecute(finalNotificationList);
				totalCount=result.size();
				spinnerList.clear();
				for (int i = 1; i <= result.size(); i++) {
					spinnerList.add("" + i);
				}
				dataAdapter.notifyDataSetChanged();
			} else {
				super.onPostExecute(finalNotificationList);
			}

			if (isErrorOccured) {
				if (!ConnectionUtil.isNetworkAvailable())
					Toast.makeText(
							DropActivity.mContext,
							"Check Your Internet Connection",
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(DropActivity.mContext,
							ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE,
							Toast.LENGTH_LONG).show();
			} else if (finalNotificationList.size() == 0) {
				notificationMessage.setText("No data to Show");
				prevBtn.setVisibility(View.GONE);
				nextBtn.setVisibility(View.GONE);
				dropNotificationSpinner.setVisibility(View.GONE);
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Loading Data...");
			dialog.show();
		}

		@Override
		protected List<PacketTrackingBean> doInBackground(String... params) {
			isErrorOccured = false;
			List<PacketTrackingBean> result = new ArrayList<PacketTrackingBean>();
			final int REGISTRATION_TIMEOUT = 3 * 1000;
			final int WAIT_TIMEOUT = 30 * 1000;
			try {
				//
				HttpClient httpclient = new DefaultHttpClient();
				HttpParams httpParam = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParam,
						REGISTRATION_TIMEOUT);
				HttpConnectionParams.setSoTimeout(httpParam, WAIT_TIMEOUT);
				ConnManagerParams.setTimeout(httpParam, WAIT_TIMEOUT);
				String url = ConnectionUtil.CONNECTION_BACKEND
						+ "app/getVehcilePacketList";
				HttpPost httpPost = new HttpPost(url);

				// add name value pair for the country code
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("vehicleId", HomeActivity.sharedpreferences.getString("vehicleId",
						"")));
				nameValuePairs.add(new BasicNameValuePair("status",
						"IN_TRANSIT"));
				nameValuePairs.add(new BasicNameValuePair("userId",
						HomeActivity.sharedpreferences.getString("userId",
								"")));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httpPost);
				int status = response.getStatusLine().getStatusCode();

				if (status == 200) {
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);
					JSONObject jsono = new JSONObject(data);
					JSONArray jarray = jsono.getJSONArray("notificationDetail");
					if (jarray != null && jarray.length() > 0) {
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject object = jarray.getJSONObject(i);
							PacketTrackingBean packetTrackingBean = new PacketTrackingBean();
							packetTrackingBean.setCreatedDate(object
									.getString("createdDate"));
							packetTrackingBean.setMessage(object
									.getString("message"));
							packetTrackingBean.setPacketName(object
									.getString("packetName"));
							packetTrackingBean.setPacketTrackingId(object
									.getInt("packetTrackingId"));
							packetTrackingBean.setStatus(object
									.getString("status"));

							result.add(packetTrackingBean);
						}
					}
				}
				return result;
			} catch (Throwable t) {
				isErrorOccured = true;
				t.printStackTrace();
			}
			return null;
		}

	}

}
