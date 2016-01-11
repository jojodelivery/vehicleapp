package com.pin91.jojovehicleapp.network.requests;

import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pin91.jojovehicleapp.model.LoginDO;
import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.network.ErrorMessages;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by udit on 12/16/2015.
 */
public class LoginRequest {

    private static final String REQUEST_SUB_URL = "appLogin";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_PASSWORD = "password";

    public static LoginDO loginUser(Context context, String userName, String password) {
        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put(KEY_USERNAME, userName);
        paramsMap.put(KEY_PASSWORD, password);

        String response = ConnectionUtil.connectToBackEnd(
                paramsMap, REQUEST_SUB_URL);

        if (response == null || response == "") {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response,
                    LoginDO.class);
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