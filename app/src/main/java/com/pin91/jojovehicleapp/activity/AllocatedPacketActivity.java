package com.pin91.jojovehicleapp.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.PacketTrackingBean;
import com.pin91.jojovehicleapp.model.PickupStatus;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.network.requests.GetAllocatedPacketByVehicleIdRequest;
import com.pin91.jojovehicleapp.network.requests.UpdateVehiclePickUpRequest;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;
import com.pin91.jojovehicleapp.utils.JojoUtils;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;


public class AllocatedPacketActivity extends AppCompatActivity {


	TextView textView;
	ImageView acceptBtn;
	ImageView rejectBtn;
	int packetTrackingId;
	int packetId;
	private final String TEXT_FONT_STYLE = "app_icons.ttf";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.order_view);
		initializeViews();
		//mContext = AllocatedPacketActivity.this;
//		textView = (TextView) findViewById(R.id.pickupMessage);
//		acceptBtn = (ImageView) findViewById(R.id.acceptBtn);
//		rejectBtn = (ImageView) findViewById(R.id.rejectBtn);
//		reloadData();
	}

	private void initializeViews(){
		TextView locationView = (TextView)findViewById(R.id.locationIcon);
		TextView nextIcon = (TextView)findViewById(R.id.next_arrow);
		TextView previousIcon = (TextView)findViewById(R.id.previous_arrow);
		TextView acceptBtn = (TextView)findViewById(R.id.acceptBtn);
		TextView rejectBtn = (TextView)findViewById(R.id.rejectBtn);
		Typeface tf = Typeface.createFromAsset(getAssets(), TEXT_FONT_STYLE);
		nextIcon.setTypeface(tf);
		previousIcon.setTypeface(tf);
		locationView.setTypeface(tf);
		acceptBtn.setTypeface(tf);
		rejectBtn.setTypeface(tf);
	}
//	private void addDummyPacketsForView(){
//		View orderPacketCard = getLayoutInflater().inflate(R.layout.order_packet_card, null);
//		View rootView = findViewById(R.id.or)
//	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void acceptClick(View view) {
        PickupStatus pickupStatus = getPickupStatusObject(PickupStatus.STATUS.ACCEPT.getValue());
        UpdateVehiclePickUpRequest.updateVehiclePickupStatus(pickupStatus);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				AllocatedPacketActivity.this);
		builder.setTitle("Alert");
		builder.setMessage(
				"You have accepted the packet.")
				.setCancelable(false)
				.setPositiveButton(
						"OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int id) {
								dialog.cancel();
								reloadData();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void rejectClick(View view) {
        PickupStatus pickupStatus =getPickupStatusObject(PickupStatus.STATUS.REJECT.getValue());
		UpdateVehiclePickUpRequest.updateVehiclePickupStatus(pickupStatus);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				AllocatedPacketActivity.this);
		builder.setTitle("Alert");
		builder.setMessage(
				"You have rejected the packet.")
				.setCancelable(false)
				.setPositiveButton(
						"OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int id) {
								dialog.cancel();
								reloadData();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

    private PickupStatus getPickupStatusObject(String status){
        PickupStatus pickupStatus = new PickupStatus();
        pickupStatus.setPacketId(packetId);
        pickupStatus.setPacketTrackingId(packetTrackingId);
        pickupStatus.setStatus(status);
        pickupStatus.setUserId(SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext()).getUserId());
        UpdateVehiclePickUpRequest.updateVehiclePickupStatus(pickupStatus);
        return pickupStatus;
    }

	private void reloadData() {
		if(!ConnectionUtil.isNetworkAvailable()){
			DTSCommonUtil.closeProgressBar();
            showPacketStatusView(View.GONE, null);
			if (ConnectionUtil.NO_INTERNET_MESSAGE_SHOWN == "false")
				Toast.makeText(getApplicationContext(),
						ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE,
						Toast.LENGTH_LONG).show();
			return;
		}
        new AsyncTask<Void, PacketTrackingBean, PacketTrackingBean>(){

            @Override
            protected PacketTrackingBean doInBackground(Void... params) {
                SharedPreferenceManager preferenceManager =
                        SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext());
                return GetAllocatedPacketByVehicleIdRequest.reloadData(preferenceManager.getVehicleId(), preferenceManager.getUserId());
            }

            @Override
            protected void onPostExecute(PacketTrackingBean packetTrackingBean) {
                if (packetTrackingBean == null) {
                    showPacketStatusView(View.GONE, getString(R.string.empty_display));
                } else {
                    packetTrackingId = packetTrackingBean.getPacketTrackingId();
                    packetId = packetTrackingBean.getPacketId();
                    showPacketStatusView(View.VISIBLE, packetTrackingBean.getMessage());
                }
                DTSCommonUtil.closeProgressBar();
            }
        }.execute();
	}

    private void showPacketStatusView(int visibility, String message){
        if(!JojoUtils.isNullOrEmpty(message)){
            textView.setText(Html.fromHtml(message));
        }
        acceptBtn.setVisibility(visibility);
        rejectBtn.setVisibility(visibility);
    }
}
