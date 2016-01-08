package com.pin91.jojovehicleapp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by udit on 1/2/2016.
 */
public class OrderDO implements Serializable{
    String orderName;
    String distributor;
    String retailer;
    ArrayList<PacketTrackingBean> packetList;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public ArrayList<PacketTrackingBean> getPacketList() {
        return packetList;
    }

    public void setPacketList(ArrayList<PacketTrackingBean> packetList) {
        this.packetList = packetList;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }
}
