package de.andreasschrade.androidtemplate.ui.quote;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.ui.ViewSamplesActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private TextView mButtonCancel;
    private TextView mButtonLogon;
    private ProgressDialog pDialog;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.theme_primary_dark));

        mUsernameEditText = (EditText) findViewById(R.id.editText);
        mPasswordEditText = (EditText) findViewById(R.id.editText2);
        mButtonCancel = (TextView) findViewById(R.id.button2);
        mButtonLogon = (TextView) findViewById(R.id.button);

        String appVersion = "v1";

        Backendless.initApp(this, "A0819152-C875-C222-FF18-0516AB9ACC00", "94E2E030-C1B8-0F27-FFEE-CD829BAE3400", appVersion);


        Log.i("info", "backendless success");


        /*BackendlessUser user = new BackendlessUser();
        user.setProperty( "email", "testing@gmail.com" );
        user.setPassword("testing");

        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser registeredUser) {

                Log.i("info", "register success");
            }

            public void handleFault(BackendlessFault fault) {

                Log.i("info", "register failed" + fault.getCode());
            }
        });*/

        mButtonLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = mPasswordEditText.getText().toString().replaceAll("\\s+", "");
                String username = mUsernameEditText.getText().toString().replaceAll("\\s+","");

                pDialog = new ProgressDialog(LoginActivity.this);
                pDialog.setMessage("Authenticating...");
                pDialog.setCancelable(false);
                pDialog.show();

                Backendless.UserService.login( username, password, new AsyncCallback<BackendlessUser>()
                {
                    public void handleResponse( BackendlessUser user )
                    {
                        Log.i("info", "login success");

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, ViewSamplesActivity.class));


                    }

                    public void handleFault( BackendlessFault fault )
                    {

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.i("info", "login failed" + fault.getCode());
                    }
                });


            }
        });
    }



}
