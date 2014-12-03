package com.vitagramma.Vitagramma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vitagramma.Vitagramma.JSONEntities.DoctorJson;
import com.vitagramma.Vitagramma.Routing.Router;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends Activity {

    TextView profile_name;
    TextView profile_city_and_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_city_and_name = (TextView) findViewById(R.id.profile_city_and_age);

        if (!Router.isAuthorized()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else {
            GetterProfileInfo getterProfileInfo = new GetterProfileInfo(this);
            getterProfileInfo.execute();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class GetterProfileInfo extends AsyncTask<Void, Void, Void> {

        Activity activity;
        ProgressDialog progressDialog;
        DoctorJson doctor;
        boolean isAuthorized;

        public GetterProfileInfo(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(getString(R.string.synchronization));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String json = null;
            try {
                json = Router.getProfile();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            doctor = new DoctorJson(json);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                profile_name.setText(doctor.getShortedName());
            } catch (NullPointerException e) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
                finish();
            }
            progressDialog.dismiss();

        }

    }
}
