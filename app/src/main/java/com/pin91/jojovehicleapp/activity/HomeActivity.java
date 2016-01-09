package com.pin91.jojovehicleapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.NotificationsCounter;
import com.pin91.jojovehicleapp.model.VehicleBean;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.network.ErrorMessages;
import com.pin91.jojovehicleapp.network.requests.DashboardCount;
import com.pin91.jojovehicleapp.network.requests.GetVehicleDetailsByContextRequest;
import com.pin91.jojovehicleapp.utils.HttpAsyncTask;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;
import com.pin91.jojovehicleapp.views.widgets.DashboardCell;

public class HomeActivity extends AppCompatActivity {
    public static SharedPreferences sharedpreferences = null;

    DashboardCell allocatedPackets;
    DashboardCell pickupPackets;
    DashboardCell inVehiclePackets;
 //   DashboardCell deliveredPackets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        loadVehicleData();
        allocatedPackets = (DashboardCell) findViewById(R.id.allocated_packet);
        pickupPackets = (DashboardCell) findViewById(R.id.pickups);
        inVehiclePackets = (DashboardCell) findViewById(R.id.drop_in_transit);
     //   deliveredPackets = (DashboardCell) findViewById(R.id.delivered);

        allocatedPackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AssignedOrdersActivity.class));
            }
        });

        pickupPackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PickUpActivity.class));
            }
        });
        inVehiclePackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, InTransitVehicleActivity.class));
            }
        });
//        deliveredPackets.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, PickUpActivity.class));
//            }
//        });
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
                   renderNotificationCount();
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

    private void renderNotificationCount(){
        new HttpAsyncTask<Void, NotificationsCounter, NotificationsCounter>(){


            @Override
            protected NotificationsCounter doInBackground(Void... params) {
                SharedPreferenceManager preferenceManager = SharedPreferenceManager.
                        getSharedPreferenceManager(getApplicationContext());
                return DashboardCount.getNotificationsCount(preferenceManager.getUserId(), preferenceManager.getVehicleId());
            }

            @Override
            protected void onPostExecute(NotificationsCounter notificationsCounter) {
                if(notificationsCounter != null) {
                    setNotificationCount(notificationsCounter);
                }
            }
        }.execute();
    }

    private void setNotificationCount(NotificationsCounter counter){
        allocatedPackets.setNotificationCircleText(counter.getTodoOrderCount());
        pickupPackets.setNotificationCircleText(counter.getPickupOrderCount());
        inVehiclePackets.setNotificationCircleText(counter.getIntransitOrderCount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderNotificationCount();
    }
}
