package com.pin91.jojovehicleapp.network.requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.model.VehicleBean;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.utils.DTSCommonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by udit on 12/17/2015.
 */
public class GetVehicleDetailsByContextRequest {

    private static final String USER_ID = "userId";
    private static final String REQUEST_SUB_URL = "app/getVehicleDetailsByContext";

    public static VehicleBean loadVehicleData(Context context, String userId) {
        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put(USER_ID, userId);
        String response = ConnectionUtil.connectToBackEnd(
                paramsMap,REQUEST_SUB_URL);

        if (response == null || response == "") {
            Toast.makeText(context, ConnectionUtil.CONNECTION_SERVER_DOWN_MESSAGE, Toast.LENGTH_LONG).show();
            return null;
        }

        VehicleBean vehicleBean = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            vehicleBean = objectMapper.readValue(response,
                    VehicleBean.class);
            return vehicleBean;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
