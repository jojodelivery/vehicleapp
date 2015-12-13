package com.pin91.jojovehicleapp.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;

public class PickUpActivity extends Activity {

    TextView textView;
    ImageView acceptBtn;
    ImageView rejectBtn;
    int packetTrackingId;
    int packetId;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pickup);
        mContext = PickUpActivity.this;
        textView = (TextView) findViewById(R.id.pickupMessage);
        acceptBtn = (ImageView) findViewById(R.id.acceptBtn);
        rejectBtn = (ImageView) findViewById(R.id.rejectBtn);
        reloadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void acceptClick(View view) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("packetTrackingId", "" + packetTrackingId);
        paramsMap.put("status", "IN_TRANSIT");
        paramsMap.put("packetId", "" + packetId);
        paramsMap.put("userId", HomeActivity.sharedpreferences.getString("userId",
                ""));
        ConnectionUtil.connectToBackEnd(paramsMap,
                "app/updateVehiclePickUp");
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
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("packetTrackingId", "" + packetTrackingId);
        paramsMap.put("status", "REJECT");
        paramsMap.put("packetId", "" + packetId);
        paramsMap.put("userId", HomeActivity.sharedpreferences.getString("userId",
                ""));
        ConnectionUtil.connectToBackEnd(paramsMap,
                "app/updateVehiclePickUp");
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
            acceptBtn.setVisibility(View.GONE);
            rejectBtn.setVisibility(View.GONE);
            if (ConnectionUtil.NO_INTERNET_MESSAGE_SHOWN == "false")
                Toast.makeText(PickUpActivity.this,
                        ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE,
                        Toast.LENGTH_LONG).show();
            return;
        }
        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put("vehicleId", HomeActivity.sharedpreferences.getString("vehicleId",
                ""));
        paramsMap.put("userId", HomeActivity.sharedpreferences.getString("userId",
                ""));
        String response = ConnectionUtil.connectToBackEnd(paramsMap,
                "app/getPickupDetailsByVehicleId");

        if (response == null || response == "") {
            textView.setText(R.string.empty_display);
            acceptBtn.setVisibility(View.GONE);
            rejectBtn.setVisibility(View.GONE);
        }

        PacketTrackingBean packetTrackingBean = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            packetTrackingBean = objectMapper.readValue(response,
                    PacketTrackingBean.class);
            packetTrackingId = packetTrackingBean.getPacketTrackingId();
            packetId = packetTrackingBean.getPacketId();
            textView.setText(Html.fromHtml(packetTrackingBean.getMessage()));
            acceptBtn.setVisibility(View.VISIBLE);
            rejectBtn.setVisibility(View.VISIBLE);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
