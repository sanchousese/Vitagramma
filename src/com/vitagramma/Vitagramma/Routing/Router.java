package com.vitagramma.Vitagramma.Routing;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by sancho on 03.12.14.
 */
public class Router {

    public static final String LOGIN_URL = "https://vitagramma.com/ua/doctor/login_process?is_mobile=1";
    public static final String PROFILE_URL = "https://vitagramma.com/ua/doctor/profile?is_mobile=1&PHPSESSID=";


    public static String login(String email, String pass) {
        List<NameValuePair> information = new ArrayList<>(2);

        information.add(new BasicNameValuePair("doctor_email", email));
        information.add(new BasicNameValuePair("doctor_password", pass));

        String redirect = null;

        try {
            JSONObject response = new JSONObject(JSONParser.makePostRequest(LOGIN_URL, information));
            redirect = response.getString("redirect");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return redirect;
    }

    public static String getProfile() throws InterruptedException {
        return JSONParser.makeGetRequest(PROFILE_URL + JSONParser.getSessionID());
    }

    public static void logOut() {
        JSONParser.setSessionID(null);
    }

    public static boolean isAuthorized(){ return JSONParser.getSessionID() != null; }

    public static void clearAuth(){JSONParser.setSessionID(null);}
}
