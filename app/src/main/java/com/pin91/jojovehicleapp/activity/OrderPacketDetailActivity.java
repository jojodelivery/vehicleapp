package com.pin91.jojovehicleapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.model.OrderDO;
import com.pin91.jojovehicleapp.model.PacketTrackingBean;
import com.pin91.jojovehicleapp.model.PickupStatus;
import com.pin91.jojovehicleapp.network.requests.UpdateVehiclePickUpRequest;
import com.pin91.jojovehicleapp.utils.SharedPreferenceManager;
import com.pin91.jojovehicleapp.views.action.Pager;
import com.pin91.jojovehicleapp.views.action.SelectionPanel;

/**
 * Created by udit on 12/27/2015.
 */
public class OrderPacketDetailActivity extends AppCompatActivity {

    Pager<PacketTrackingBean> pager;
    SelectionPanel acceptRejectPanel;
    public static final String ORDER ="order";
    public static final String PACKET_DELIVERY_STATUS="status";
    OrderDO orderDO;
    String packetDeliveryStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_packet_selection_view);
        orderDO = (OrderDO)getIntent().getExtras().get(ORDER);
        packetDeliveryStatus = getIntent().getExtras().getString(PACKET_DELIVERY_STATUS);
        initializeViews();
    }

    private void initializeViews(){

       pager = new Pager<PacketTrackingBean>(getWindow().getDecorView().getRootView(), orderDO.getPacketList()) {
           @Override
           public void onLeftActionHandler(PacketTrackingBean packetTrackingBean) {
               setView(packetTrackingBean);
           }

           @Override
           public void onRightActionHandler(PacketTrackingBean packetTrackingBean) {
               setView(packetTrackingBean);
           }

           @Override
           public void initialView(PacketTrackingBean packetTrackingBean) {
               setView(packetTrackingBean);
           }
       };
    }

    private void setView(PacketTrackingBean packetTrackingBean){
        TextView packetText =  (TextView)findViewById(R.id.packet_text);
        TextView packetDistributionText =  (TextView)findViewById(R.id.packet_description);
        packetText.setText(packetTrackingBean.getPacketName());
        packetDistributionText.setText(packetTrackingBean.getMessage());
        acceptRejectPanel = new SelectionPanel<PacketTrackingBean>(getWindow().getDecorView().getRootView(), packetTrackingBean) {
            @Override
            public void onAcceptClick(PacketTrackingBean bean) {
                acceptClick(bean);
            }

            @Override
            public void onRejectClick(PacketTrackingBean bean) {
                rejectClick(bean);
            }
        };
    }

    public void acceptClick(PacketTrackingBean bean) {
        PickupStatus pickupStatus =
                getPickupStatusObject(packetDeliveryStatus, bean.getPacketId(), bean.getPacketTrackingId());
        UpdateVehiclePickUpRequest.updateVehiclePickupStatus(pickupStatus);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                OrderPacketDetailActivity.this);
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
                                acceptRejectPanel.disablePanel();
                               // reloadData();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void rejectClick(PacketTrackingBean bean) {
        PickupStatus pickupStatus =
                getPickupStatusObject(PickupStatus.STATUS.REJECT.getValue(), bean.getPacketId(), bean.getPacketTrackingId());
        UpdateVehiclePickUpRequest.updateVehiclePickupStatus(pickupStatus);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                OrderPacketDetailActivity.this);
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
                                acceptRejectPanel.disablePanel();
                              //  reloadData();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private PickupStatus getPickupStatusObject(String status, int packetId, int packetTrackingId){
        PickupStatus pickupStatus = new PickupStatus();
        pickupStatus.setPacketId(packetId);
        pickupStatus.setPacketTrackingId(packetTrackingId);
        pickupStatus.setStatus(status);
        pickupStatus.setUserId(SharedPreferenceManager.getSharedPreferenceManager(getApplicationContext()).getUserId());
        return pickupStatus;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
