package com.pin91.jojovehicleapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.VehicleBean;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;
import com.pin91.jojovehicleapp.views.widgets.DashboardCell;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    public static SharedPreferences sharedpreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        sharedpreferences = getSharedPreferences("user_pref",
                Context.MODE_PRIVATE);
        loadVehicleData();

        RelativeLayout notification = (RelativeLayout) findViewById(R.id.notification);
        notification.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                        break;
                }
                return true;
            }
        });

        RelativeLayout allocatedPacket = (RelativeLayout) findViewById(R.id.allocated_packet);
        allocatedPacket.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startActivity(new Intent(HomeActivity.this, AllocatedPacketActivity.class));
                        break;
                }
                return true;
            }
        });

        RelativeLayout pickup = (RelativeLayout) findViewById(R.id.pickup);
        pickup.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startActivity(new Intent(HomeActivity.this, PickUpActivity.class));
                        break;
                }
                return true;
            }
        });

        RelativeLayout drop = (RelativeLayout) findViewById(R.id.drop_in_transit);
        drop.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startActivity(new Intent(HomeActivity.this, DropActivity.class));
                        break;
                }
                return true;
            }
        });
    }


    public void loadVehicleData() {
        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put("userId", HomeActivity.sharedpreferences.getString("userId",
                ""));
        String response = ConnectionUtil.connectToBackEnd(
                paramsMap, "app/getVehicleDetailsByContext");

        if (response == null || response == "") {
            DTSCommonUtil.closeProgressBar();
            if (ConnectionUtil.NO_INTERNET_MESSAGE_SHOWN == "false")
                Toast.makeText(
                        HomeActivity.this,
                        ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE,
                        Toast.LENGTH_LONG).show();
            return;
        }

        VehicleBean vehicleBean = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            vehicleBean = objectMapper.readValue(response,
                    VehicleBean.class);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("vehicleId", "" + vehicleBean.getVehicleId());
            editor.commit();

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                HomeActivity.this);

        alertDialogBuilder.setTitle("ALERT");

        alertDialogBuilder
                .setMessage("Do you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent launchNextActivity = null;
                                launchNextActivity = new Intent(
                                        Intent.ACTION_MAIN);
                                launchNextActivity
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                launchNextActivity
                                        .addCategory(Intent.CATEGORY_HOME);

                                startActivity(launchNextActivity);
                                finish();
                                System.exit(0);
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }
}
