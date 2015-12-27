package com.pin91.jojovehicleapp.network.requests;

import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.model.LoginDO;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.network.ErrorMessages;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by udit on 12/18/2015.
 */
public class BaseRequest {

    private String REQUEST_SUB_URL;

    private String connectToBackEnd(HashMap<String, String> paramsMap){
        String response = ConnectionUtil.connectToBackEnd(
                paramsMap, REQUEST_SUB_URL);
        return response;
    }

    private Object parseNetworkResponse(String networkResponse, Class classType){

        Object responseObject = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            responseObject = objectMapper.readValue(networkResponse,
                    classType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseObject;
    }
}
