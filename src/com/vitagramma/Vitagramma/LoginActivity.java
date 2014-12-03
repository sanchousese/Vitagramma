package com.vitagramma.Vitagramma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.vitagramma.Vitagramma.Routing.Router;

import java.util.Locale;

public class LoginActivity extends Activity implements View.OnClickListener{

    ImageButton btnRuLocal;
    ImageButton btnUaLocal;
    Button btnLogin;
    Button btnRegister;
    Button btnForgetPass;
    EditText etEmail;
    EditText etPassword;


    boolean isRuLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnRuLocal = (ImageButton) findViewById(R.id.btnRuLocal);
        btnUaLocal = (ImageButton) findViewById(R.id.btnUaLocal);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnForgetPass = (Button) findViewById(R.id.btnForgetPass);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnRuLocal.setOnClickListener(this);
        btnUaLocal.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgetPass.setOnClickListener(this);

        if(Locale.getDefault().getLanguage().equals("ua")){
            setVisualLocalUa();
        }
        else {
            setVisualLocalRu();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRuLocal:
                if (!isRuLocal){
                    setVisualLocalRu();
                    changeLocalization("ru");
                }
                break;

            case R.id.btnUaLocal:
                if (isRuLocal){
                    setVisualLocalUa();
                    changeLocalization("ua");
                }
                break;

            case R.id.btnLogin:
                LoginTask loginTask = new LoginTask(this);
                loginTask.execute();
                break;

        }
    }

    private void changeLocalization(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);


        restartApp();
    }

    private void restartApp() {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void setVisualLocalRu() {
        btnUaLocal.setBackgroundResource(android.R.color.transparent);
        btnRuLocal.setBackgroundResource(R.drawable.localization_active);
        isRuLocal = true;
    }

    private void setVisualLocalUa() {
        btnRuLocal.setBackgroundResource(android.R.color.transparent);
        btnUaLocal.setBackgroundResource(R.drawable.localization_active);
        isRuLocal = false;
    }


    class LoginTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        ProgressDialog progressDialog;
        String response = "";

        public LoginTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(getString(R.string.authorization));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            response = Router.login(etEmail.getText().toString(), etPassword.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (response.contains("profile"))
                activity.startActivity(new Intent(activity, ProfileActivity.class));
            else
                Router.clearAuth();
                activity.startActivity(new Intent(activity, LoginActivity.class));

            activity.finish();
        }

    }
}
