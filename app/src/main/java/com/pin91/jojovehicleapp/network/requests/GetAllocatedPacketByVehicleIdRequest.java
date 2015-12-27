package com.pin91.jojovehicleapp.network.requests;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.model.PacketTrackingBean;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by udit on 12/17/2015.
 */
public class GetAllocatedPacketByVehicleIdRequest {

    private static final String REQUEST_SUB_URL = "app/getAllocatedPacketByVehicleId";
    private static final String KEY_VEHICLE_ID = "vehicleId";
    private static final String KEY_USER_ID = "userId";


    public static PacketTrackingBean reloadData(String vehicleId, String userId) {
        Map<String, String> paramsMap = new HashMap<String, String>();
       paramsMap.put(KEY_VEHICLE_ID, vehicleId);
        paramsMap.put(KEY_USER_ID, userId);
        String response = ConnectionUtil.connectToBackEnd(paramsMap,
                REQUEST_SUB_URL);
        if (response == null || response == "") {
           return null;
        }

        PacketTrackingBean packetTrackingBean = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            packetTrackingBean = objectMapper.readValue(response,
                    PacketTrackingBean.class);
            return packetTrackingBean;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packetTrackingBean;
    }
}
