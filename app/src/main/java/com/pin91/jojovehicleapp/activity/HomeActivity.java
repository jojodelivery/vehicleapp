package com.pin91.jojovehicleapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.VehicleBean;
import com.pin91.jojovehicleapp.network.requests.GetVehicleDetailsByContextRequest;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;
import com.pin91.jojovehicleapp.views.widgets.DashboardCell;

public class HomeActivity extends AppCompatActivity {
    public static SharedPreferences sharedpreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        loadVehicleData();
        DashboardCell allocatedPackets = (DashboardCell) findViewById(R.id.allocated_packet);
        DashboardCell pickupPackets = (DashboardCell) findViewById(R.id.pickups);

        allocatedPackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AllocatedPacketActivity.class));
            }
        });

        pickupPackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PickUpActivity.class));
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

        new AsyncTask<Void, VehicleBean, VehicleBean>(){
            @Override
            protected VehicleBean doInBackground(Void... params) {
                return GetVehicleDetailsByContextRequest.getVehicleData(getApplicationContext(),
                        SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext()).getUserId());
            }

            @Override
            protected void onPostExecute(VehicleBean vehicleBean) {
               if(vehicleBean != null){
                   SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext()).saveVehicleId(vehicleBean.getVehicleId());
               }
            }
        }.execute();
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
