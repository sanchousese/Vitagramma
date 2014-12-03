package com.vitagramma.Vitagramma.JSONEntities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sancho on 03.12.14.
 */
public class DoctorJson {

    private int id;
    private String firstName;
    private String secondName;
    private String lastName;
    private int age;
    private String city;
    private String email;
    private String phoneNumber;
    private String pass;
    private int imageID;

    public DoctorJson(String json) {
        JSONObject reader = null;
        try {
            reader = new JSONObject(json);
            JSONObject doctor = reader.getJSONObject("doctor");

            id = doctor.getInt("id");
            lastName = doctor.getString("last_name");
            firstName = doctor.getString("first_name");
            secondName = doctor.getString("second_name");
            email = doctor.getString("email");
            phoneNumber = doctor.getString("phone");
            imageID = doctor.getInt("image_id");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getShortedName() {
        return lastName + " " + firstName.charAt(0) + "." + secondName.charAt(0) + ".";
    }

}
