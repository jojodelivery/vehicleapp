package com.pin91.jojovehicleapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Debdutta Bhattacharya
 */
public class ConnectionUtil {

    public static String CONNECTION_BACKEND = "http://52.76.116.161:8080/DTS";
    public static String NO_INTERNET_MESSAGE_SHOWN = "true";
    public static String CONNECTION_NO_INTERNET = "No Internet.Check Your Connection.";
    public static String CONNECTION_SERVER_DOWN_MESSAGE = "Service down for maintainance. Please try after some time.";
    public static Map<String, String> paramsListMap;
    public static String servletUrl = "";
    public static Context context;

    public static String connectToBackEnd(Map<String, String> paramsMap,
                                          String url) {
        paramsListMap = paramsMap;
        servletUrl = url;
        String response = getResult(url);
        return response;
    }

    private static String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
//            Toast.makeText(context, "Stream Exception",
//                    Toast.LENGTH_SHORT).show();
        }
        return total.toString();
    }

    public class HttpAsyncTaskGetTestToken extends
            AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return getResult(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        }
    }


    public static String getResult(String url) {
        Log.d("Connection", url);
        HttpClient httpclient = new DefaultHttpClient();
        String line = "";
        if (url.isEmpty())
            url = servletUrl;

        String connectionUrl = CONNECTION_BACKEND.concat("/").concat(url);

        HttpPost httpPost = new HttpPost(connectionUrl);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

        if (paramsListMap != null)
            for (Map.Entry<String, String> entry : paramsListMap.entrySet()) {
                urlParameters.add(new BasicNameValuePair(entry.getKey(),
                        entry.getValue()));
            }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 404) {

                InputStream inputstream = response.getEntity().getContent();
                line = convertStreamToString(inputstream);
                return line;
            }
        } catch (ClientProtocolException e) {
           // Toast.makeText(context, "Unable to connect. " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
//            Toast.makeText(context,
//                    "No Internet.Check Your Connection.", Toast.LENGTH_LONG)
//                    .show();
//            NO_INTERNET_MESSAGE_SHOWN = "true";
        } catch (Exception e) {
            //Toast.makeText(context, "Unable to connect. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return ErrorMessages.UNABLE_TO_CONNECT;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
