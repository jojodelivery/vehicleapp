package com.pin91.jojovehicleapp.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
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
import com.pin91.jojovehicleapp.network.requests.GetPickupDetailsByVehicleIdRequest;
import com.pin91.jojovehicleapp.network.requests.UpdateVehiclePickUpRequest;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;
import com.pin91.jojovehicleapp.utils.JojoUtils;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;

public class PickUpActivity extends Activity {

    TextView textView;
    ImageView acceptBtn;
    ImageView rejectBtn;
    int packetTrackingId;
    int packetId;
    private final String TEXT_FONT_STYLE = "app_icons.ttf";
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.order_packet_selection_view);
//        mContext = PickUpActivity.this;
//        textView = (TextView) findViewById(R.id.pickupMessage);
//        acceptBtn = (ImageView) findViewById(R.id.acceptBtn);
//        rejectBtn = (ImageView) findViewById(R.id.rejectBtn);
        reloadData();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void acceptClick(View view) {
        PickupStatus pickupStatus = getPickupStatusObject(PickupStatus.STATUS.IN_TRANSIT.getValue());
        UpdateVehiclePickUpRequest.updateVehiclePickupStatus(pickupStatus);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                PickUpActivity.this);
        builder.setTitle("Alert");
        builder.setMessage(
                "Data has been saved.")
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
        PickupStatus pickupStatus = getPickupStatusObject(PickupStatus.STATUS.REJECT.getValue());
        UpdateVehiclePickUpRequest.updateVehiclePickupStatus(pickupStatus);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                PickUpActivity.this);
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

    private void reloadData() {
        if (!ConnectionUtil.isNetworkAvailable()) {
            DTSCommonUtil.closeProgressBar();
            setViewUI(null, View.GONE);
            if (ConnectionUtil.NO_INTERNET_MESSAGE_SHOWN == "false")
                Toast.makeText(PickUpActivity.this,
                        ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE,
                        Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferenceManager preferenceManager = SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext());
        PacketTrackingBean packetTrackingBean = GetPickupDetailsByVehicleIdRequest.reloadData(getApplicationContext(),
                preferenceManager.getVehicleId(), preferenceManager.getUserId());
        if (packetTrackingBean == null) {
            setViewUI(getString(R.string.empty_display), View.GONE);
        } else {
            packetTrackingId = packetTrackingBean.getPacketTrackingId();
            packetId = packetTrackingBean.getPacketId();
        }
    }

    private void setViewUI(String message, int visibility){
        if(JojoUtils.isNullOrEmpty(message)){
            textView.setText(Html.fromHtml(message));
        }
        acceptBtn.setVisibility(visibility);
        rejectBtn.setVisibility(visibility);
    }
    private PickupStatus getPickupStatusObject(String status){
        PickupStatus pickupStatus = new PickupStatus();
        pickupStatus.setPacketId(packetId);
        pickupStatus.setPacketTrackingId(packetTrackingId);
        pickupStatus.setStatus(status);
        pickupStatus.setUserId(SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext()).getUserId());
        return pickupStatus;
    }
}
