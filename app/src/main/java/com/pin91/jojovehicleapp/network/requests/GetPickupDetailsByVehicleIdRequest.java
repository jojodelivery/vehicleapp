package com.pin91.jojovehicleapp.network.requests;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.R;
import com.pin91.jojovehicleapp.activity.HomeActivity;
import com.pin91.jojovehicleapp.activity.PickUpActivity;
import com.pin91.jojovehicleapp.model.PacketTrackingBean;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by udit on 12/17/2015.
 */
public class GetPickupDetailsByVehicleIdRequest {
    private static final String REQUEST_SUB_URL = "app/getPickupDetailsByVehicleId";
    private static final String KEY_VEHICLE_ID = "vehicleId";
    private static final String KEY_USER_ID = "userId";

    public static PacketTrackingBean reloadData(Context context, String vehicleId, String userId) {
        if (!ConnectionUtil.isNetworkAvailable()) {
            if (ConnectionUtil.NO_INTERNET_MESSAGE_SHOWN == "false")
                Toast.makeText(context,
                        ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE,
                        Toast.LENGTH_LONG).show();
            return null;
        }

        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put(KEY_VEHICLE_ID, vehicleId);
        paramsMap.put(KEY_USER_ID, userId);

        String response = ConnectionUtil.connectToBackEnd(paramsMap,
                REQUEST_SUB_URL);

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
