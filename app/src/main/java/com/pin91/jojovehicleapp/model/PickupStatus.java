package com.pin91.jojovehicleapp.model;

/**
 * Created by udit on 12/17/2015.
 */
public class PickupStatus {
    private String packetTrackingId;
    private String status;
    private String packetId;
    private String userId;

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = String.valueOf(packetId);
    }

    public String getPacketTrackingId() {
        return packetTrackingId;
    }

    public void setPacketTrackingId(int packetTrackingId) {
        this.packetTrackingId = String.valueOf(packetTrackingId);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public enum STATUS {
        ACCEPT("ACCEPT"),
        REJECT("REJECT"),
        IN_TRANSIT("IN_TRANSIT"),
        PENDING("PENDING");

        private String status;

        private STATUS(String status){
            this.status = status;
        }

        public String getValue(){
            return this.status;
        }
    }

}