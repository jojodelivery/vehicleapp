package com.pin91.jojovehicleapp.network.requests;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.model.OrderDO;
import com.pin91.jojovehicleapp.network.ConnectionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by udit on 12/17/2015.
 */
public class GetInTransitOrderDetailsRequest {
    private static final String REQUEST_SUB_URL = "app/getInTransitOrder";
    private static final String KEY_VEHICLE_ID = "vehicleId";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_STATUS = "status";

    public static ArrayList<OrderDO> reloadData(String vehicleId, String userId, String status) {

        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put(KEY_VEHICLE_ID, vehicleId);
        paramsMap.put(KEY_USER_ID, userId);
        paramsMap.put(KEY_STATUS, status);
        String response = ConnectionUtil.connectToBackEnd(paramsMap,
                REQUEST_SUB_URL);

        ArrayList<OrderDO> pickUpOrders = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            pickUpOrders = objectMapper.readValue(response,
                    new TypeReference<List<OrderDO>>(){});
            return pickUpOrders;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pickUpOrders;
    }
}
