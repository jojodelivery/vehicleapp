package com.pin91.jojovehicleapp.model;

/**
 * Created by udit on 12/27/2015.
 */
public class NotificationsCounter {

    private int pickupPacketCount;
    private int acceptPacketCount;
    private int dropPacketCount;
    private int messageCount;

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getAcceptPacketCount() {
        return acceptPacketCount;
    }

    public void setAcceptPacketCount(int acceptPacketCount) {
        this.acceptPacketCount = acceptPacketCount;
    }

    public int getDropPacketCount() {
        return dropPacketCount;
    }

    public void setDropPacketCount(int dropPacketCount) {
        this.dropPacketCount = dropPacketCount;
    }

    public int getPickupPacketCount() {
        return pickupPacketCount;
    }

    public void setPickupPacketCount(int pickupPacketCount) {
        this.pickupPacketCount = pickupPacketCount;
    }
}
