package com.example.timer;

import android.util.Log;
import android.webkit.CookieManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {

    static final String COOKIES_HEADER = "Set-Cookie";
    CookieManager cookieManager;

    public HttpHandler(){

    }

    public JSONObject getHandler(String serverUrl){

        JSONObject jsonObject=null;

        try {

            URL url = new URL(serverUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(httpURLConnection.getURL().toString());
            if (cookie != null) {
                httpURLConnection.setRequestProperty("Cookie", cookie);
            }

            httpURLConnection.setRequestMethod("GET");


            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

            StringBuilder sb = new StringBuilder();

            String line;

            while ((line=bufferedReader.readLine()) != null){
                sb.append(line);
            }

            jsonObject = new JSONObject(sb.toString());
            bufferedReader.close();
            inputStream.close();

        } catch (ProtocolException e) {
            Log.d("countdown", "ProtocolException  "+e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.d("countdown", "UnsupportedEncodingException  "+e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.d("countdown", "MalformedURLException  "+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("countdown", "IOException  "+e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("countdown", "JSONException  "+e.getMessage());
            e.printStackTrace();
        }
        return jsonObject;
    }
}
