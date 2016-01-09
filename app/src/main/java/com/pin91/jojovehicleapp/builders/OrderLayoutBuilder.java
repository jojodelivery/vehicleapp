package com.pin91.jojovehicleapp.builders;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.activity.OrderPacketDetailActivity;
import com.pin91.jojovehicleapp.model.OrderDO;
import com.pin91.jojovehicleapp.model.PickupStatus;

/**
 * Created by udit on 1/9/2016.
 */
public class OrderLayoutBuilder {
    private static OrderLayoutBuilder orderLayoutBuilder;

    private OrderLayoutBuilder(){
    }

    public static OrderLayoutBuilder getInstance(){
        if(orderLayoutBuilder == null){
            orderLayoutBuilder = new OrderLayoutBuilder();
        }
        return orderLayoutBuilder;
    }

    public void buildOrderLayout(View rootView, OrderDO orderDO, View.OnClickListener detailButtonListener){
        TextView distributorName = (TextView)rootView.findViewById(R.id.distributorName);
        TextView distributorAddress = (TextView)rootView.findViewById(R.id.distributorAddress);
        TextView orderName = (TextView)rootView.findViewById(R.id.orderName);
        distributorName.setText(orderDO.getDistributor());
        distributorAddress.setText(orderDO.getDestinationAddress());
        orderName.setText(orderDO.getOrderName());
        distributorAddress.setMovementMethod(new ScrollingMovementMethod());
        Button detailOrderInfoBtn = (Button)rootView.findViewById(R.id.detailOrderInfoButton);
        detailOrderInfoBtn.setOnClickListener(detailButtonListener);
    }
}
