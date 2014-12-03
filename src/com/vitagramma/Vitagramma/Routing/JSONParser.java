package com.vitagramma.Vitagramma.Routing;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    static private List<Cookie> cookies = new ArrayList<>();
    static private String sessionID = null;

    public static String getSessionID() {
        return sessionID;
    }

    public static void setSessionID(String sessionID) {
        JSONParser.sessionID = sessionID;
    }

    private static void getSessionIDFromCookies() {
        if (cookies.isEmpty()) {
            Log.d("Cookies", "None");
        } else {
            for (Cookie cooky : cookies)
                if (cooky.getName().equals("PHPSESSID"))
                    sessionID = cooky.getValue();
        }
    }

    public static String makePostRequest(String url, List<NameValuePair> nameValuePairs){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        HttpResponse response = null;

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpClient.execute(httppost);

        } catch (IOException e) {
            e.printStackTrace();
        }

        cookies = httpClient.getCookieStore().getCookies();

        getSessionIDFromCookies();

        try {
            return String.valueOf(inputStreamToString(response.getEntity().getContent()));
        } catch (IOException e) {
            return null;
        }

    }

    public static String makeGetRequest(String url) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        InputStream content = null;
        try {
            HttpResponse response = httpClient.execute(new HttpGet(url));
            content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.d("[GET REQUEST]", "Network exception", e);
        }

        getSessionIDFromCookies();

        try {
            return String.valueOf(inputStreamToString(content));
        } catch (IOException e) {
            return null;
        }
    }

    private static StringBuilder inputStreamToString(InputStream is) throws IOException {
        String line = "";
        StringBuilder total = new StringBuilder();

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        while ((line = rd.readLine()) != null) {
            total.append(line);
        }

        return total;
    }
}