package com.pin91.jojovehicleapp.network.requests;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.model.NotificationsCounter;
import com.pin91.jojovehicleapp.model.PacketTrackingBean;
import com.pin91.jojovehicleapp.network.ConnectionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by udit on 12/27/2015.
 */
public class DashboardCount {
    private static final String REQUEST_SUB_URL =   "app/dashboardCount";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_VEHICLE_ID = "vehicleId";

    public static NotificationsCounter getNotificationsCount(String userId, String vehicleId){
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(KEY_VEHICLE_ID, vehicleId);
        paramsMap.put(KEY_USER_ID, userId);
        String response = ConnectionUtil.connectToBackEnd(paramsMap,
                REQUEST_SUB_URL);
        if (response == null || response == "") {
            return null;
        }
        NotificationsCounter counter = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            counter = objectMapper.readValue(response,
                    NotificationsCounter.class);
            return counter;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counter;
    }
}
