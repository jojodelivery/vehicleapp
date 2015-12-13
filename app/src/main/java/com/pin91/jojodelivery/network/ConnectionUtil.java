package com.pin91.jojodelivery.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.pin91.jojodelivery.activity.LoginActivity;

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
 *
 */

/**
 * @author Debdutta Bhattacharya
 */
public class ConnectionUtil {

    public static String CONNECTION_BACKEND = "http://52.74.13.230:8080/DTS/";
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
        String response = new ConnectionUtil().new HttpAsyncTaskGetTestToken()
                .doInBackground(url);

        return response;
    }

    public static String connectToiAssessmentEnd(Map<String, String> paramsMap,
                                                 String url) {
        paramsListMap = paramsMap;
        servletUrl = url;
        String response = new ConnectionUtil().new HttpAsyncTaskGetTestToken()
                .doInBackground(url);

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
            Toast.makeText(LoginActivity.mContext, "Stream Exception",
                    Toast.LENGTH_SHORT).show();
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

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches();
    }

    public static String getResult(String url) {

        String failureReturnText = "";
        NO_INTERNET_MESSAGE_SHOWN = "false";
        if (isNetworkAvailable()) {

            HttpClient httpclient = new DefaultHttpClient();
            String line = "";
            if (url.isEmpty())
                url = servletUrl;

            String connectionUrl = CONNECTION_BACKEND.concat("/").concat(url);

            HttpPost httpPost = new HttpPost(connectionUrl);

            String USER_AGENT = "Mozilla/5.0";

            httpPost.setHeader("User-Agent", USER_AGENT);

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

                    if (servletUrl.equalsIgnoreCase("saveTestAnswers")) {
                        failureReturnText = "saved";
                        return "saved";
                    } else if (servletUrl
                            .equalsIgnoreCase("UploadVideoInterviewServlet")) {
                        failureReturnText = "saved";
                        return "Saved";
                    } else if (servletUrl.equalsIgnoreCase("jobApply")) {

                        failureReturnText = "saved";
                        return "Saved";
                    }

                    line = convertStreamToString(inputstream);
                    return line;

                }
            } catch (ClientProtocolException e) {
                /*
				 * Toast.makeText(MainActivity.mContext,
				 * "Caught ClientProtocolException", Toast.LENGTH_SHORT)
				 * .show();
				 */
            } catch (IOException e) {
                Toast.makeText(LoginActivity.mContext,
                        "No Internet.Check Your Connection.", Toast.LENGTH_LONG)
                        .show();
                NO_INTERNET_MESSAGE_SHOWN = "true";
            } catch (Exception e) {
            }

        } else {
            Toast.makeText(LoginActivity.mContext,
                    "No Internet.Check Your Connection.", Toast.LENGTH_LONG)
                    .show();
            NO_INTERNET_MESSAGE_SHOWN = "true";

        }

        return failureReturnText;

    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
