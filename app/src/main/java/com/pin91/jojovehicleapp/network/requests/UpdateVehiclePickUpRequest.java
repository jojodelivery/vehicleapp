package com.pin91.jojovehicleapp.network.requests;

import com.pin91.jojovehicleapp.activity.HomeActivity;
import com.pin91.jojovehicleapp.model.PickupStatus;
import com.pin91.jojovehicleapp.network.ConnectionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by udit on 12/17/2015.
 */
public class UpdateVehiclePickUpRequest {

    private static final String PACKET_TRACKING_ID = "packetTrackingId";
    private static final String STATUS = "status";
    private static final String PACKET_ID = "packetId";
    private static final String USER_ID = "userId";
    private static final String REQUEST_SUB_URL = "app/updateVehiclePickUp";

    public static String updateVehiclePickupStatus(PickupStatus pickupStatus){
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(PACKET_TRACKING_ID,pickupStatus.getPacketTrackingId());
        paramsMap.put(STATUS, pickupStatus.getStatus());
        paramsMap.put(PACKET_ID, pickupStatus.getPacketId());
        paramsMap.put(USER_ID, pickupStatus.getUserId());
        return ConnectionUtil.connectToBackEnd(paramsMap,
                REQUEST_SUB_URL);
    }
}
