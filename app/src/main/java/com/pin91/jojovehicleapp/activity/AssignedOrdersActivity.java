package com.pin91.jojovehicleapp.activity;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.OrderDO;
import com.pin91.jojovehicleapp.model.PickupStatus;
import com.pin91.jojovehicleapp.network.requests.GetAssignedOrdersRequest;
import com.pin91.jojovehicleapp.network.requests.UpdateVehiclePickUpRequest;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;
import com.pin91.jojovehicleapp.utils.JojoUtils;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;
import com.pin91.jojovehicleapp.views.action.Pager;


public class AssignedOrdersActivity extends AppCompatActivity {


	TextView textView;
	ImageView acceptBtn;
	ImageView rejectBtn;
	int packetTrackingId;
	int packetId;
	Pager pager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_packet_view);
		reloadData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void acceptClick(View view) {
        PickupStatus pickupStatus = getPickupStatusObject(PickupStatus.STATUS.ACCEPT.getValue());
        UpdateVehiclePickUpRequest.updateVehiclePickupStatus(pickupStatus);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				AssignedOrdersActivity.this);
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
				AssignedOrdersActivity.this);
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
        new AsyncTask<Void, ArrayList<OrderDO>, ArrayList<OrderDO>>(){

            @Override
            protected ArrayList<OrderDO> doInBackground(Void... params) {
                SharedPreferenceManager preferenceManager =
                        SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext());
                return GetAssignedOrdersRequest.reloadData(preferenceManager.getVehicleId(), preferenceManager.getUserId());
            }

            @Override
            protected void onPostExecute(ArrayList<OrderDO> assignedOrders) {
                if (assignedOrders == null) {
                   // showPacketStatusView(View.GONE, getString(R.string.empty_display));
                } else {
                    initiailizeViewAndPager(assignedOrders);
                }
                DTSCommonUtil.closeProgressBar();
            }
        }.execute();
	}

	private void initiailizeViewAndPager(ArrayList<OrderDO> assignedOrders){
		pager = new Pager<OrderDO>(getWindow().getDecorView().getRootView(), assignedOrders) {

			@Override
			public void onLeftActionHandler(OrderDO orderDO) {
				setView(orderDO);
			}

			@Override
			public void onRightActionHandler(OrderDO orderDO) {
                setView(orderDO);
            }

            @Override
            public void initialView(OrderDO orderDO) {
                setView(orderDO);
            }
        };
	}

    private void setView(final OrderDO orderDO){
        TextView distributorName = (TextView)findViewById(R.id.distributorName);
        TextView distributorAddress = (TextView)findViewById(R.id.retailerName);
        TextView orderName = (TextView)findViewById(R.id.orderName);
        distributorName.setText(orderDO.getDistributor());
        distributorAddress.setText(orderDO.getRetailer());
        orderName.setText(orderDO.getOrderName());

        Button detailOrderInfoBtn = (Button)findViewById(R.id.detailOrderInfoButton);
        detailOrderInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderPacketDetail = new Intent(AssignedOrdersActivity.this, OrderPacketDetailActivity.class);
                orderPacketDetail.putExtra(OrderPacketDetailActivity.ORDER, orderDO);
                orderPacketDetail.putExtra(OrderPacketDetailActivity.PACKET_DELIVERY_STATUS,
                        PickupStatus.STATUS.ACCEPT.getValue());
                startActivity(orderPacketDetail);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
