package com.pin91.jojovehicleapp.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.builders.OrderLayoutBuilder;
import com.pin91.jojovehicleapp.model.OrderDO;
import com.pin91.jojovehicleapp.model.PickupStatus;
import com.pin91.jojovehicleapp.network.ErrorMessages;
import com.pin91.jojovehicleapp.network.requests.GetInTransitOrderDetailsRequest;
import com.pin91.jojovehicleapp.utils.HttpAsyncTask;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;
import com.pin91.jojovehicleapp.views.action.Pager;

import java.util.List;

public class InTransitVehicleActivity extends AppCompatActivity {


    Pager<OrderDO> orderList;
    public static final String STATUS_TRANSIT = "IN_TRANSIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_packet_view);
        reloadData();
    }

    private void reloadData() {
        new HttpAsyncTask<Void, List<OrderDO>, List<OrderDO>>(){

            @Override
            protected List<OrderDO> doInBackground(Void... voids) {
                SharedPreferenceManager preferenceManager = SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext());
                return GetInTransitOrderDetailsRequest.reloadData(preferenceManager.getVehicleId(), preferenceManager.getUserId(), STATUS_TRANSIT);
            }

            @Override
            protected void onPostExecute(List<OrderDO> orderDOs) {
                if(orderDOs != null){
                    TextView progressIndicator = (TextView)findViewById(R.id.progressIndicator);
                    progressIndicator.setVisibility(View.GONE);
                    orderList = new Pager<OrderDO>(getWindow().getDecorView().getRootView(), orderDOs) {
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
                } else {
                    TextView progressIndicator = (TextView)findViewById(R.id.progressIndicator);
                    progressIndicator.setText(ErrorMessages.NO_DATA_TO_DISPLAY);
                }
            }
        }.execute();
    }


    private void setView(final OrderDO orderDO){
//        TextView distributorName = (TextView)findViewById(R.id.distributorName);
//        TextView distributorAddress = (TextView)findViewById(R.id.distributorAddress);
//        TextView orderName = (TextView)findViewById(R.id.orderName);
//        distributorName.setText(orderDO.getDistributor());
//        distributorAddress.setText(orderDO.getDestinationAddress());
//        orderName.setText(orderDO.getOrderName());
//
//        Button detailOrderInfoBtn = (Button)findViewById(R.id.detailOrderInfoButton);
//        detailOrderInfoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        OrderLayoutBuilder.getInstance().
                buildOrderLayout(getWindow().getDecorView().getRootView(), orderDO, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent orderPacketDetail = new Intent(InTransitVehicleActivity.this, OrderPacketDetailActivity.class);
                        orderPacketDetail.putExtra(OrderPacketDetailActivity.ORDER, orderDO);
                        orderPacketDetail.putExtra(OrderPacketDetailActivity.PACKET_DELIVERY_STATUS,
                                PickupStatus.STATUS.IN_TRANSIT.getValue());
                        startActivity(orderPacketDetail);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }
}
