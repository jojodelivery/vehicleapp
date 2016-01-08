package com.pin91.jojovehicleapp.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pin91.jojovehicleapp.network.ConnectionUtil;
import com.pin91.jojovehicleapp.network.ErrorMessages;

/**
 * Created by udit on 1/8/2016.
 */
public abstract class HttpAsyncTask<Params, Progress, Result> extends AsyncTask<Params,Progress,Result> {

    public Context context;

    @Override
    protected void onPostExecute(Result o) {
        if(o == null){
            if(!ConnectionUtil.isNetworkAvailable()){
                Toast.makeText(context, ErrorMessages.UNABLE_TO_CONNECT, Toast.LENGTH_LONG).show();
            }
        }
    }
}
