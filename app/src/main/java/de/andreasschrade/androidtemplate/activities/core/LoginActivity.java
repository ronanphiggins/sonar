package de.andreasschrade.androidtemplate.activities.core;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;


import org.florescu.android.util.BitmapUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.peripheral.HomeActivity;
import de.andreasschrade.androidtemplate.utilities.CustomDialogClass;
import de.andreasschrade.androidtemplate.utilities.SaveSharedPreference;
import de.andreasschrade.androidtemplate.utilities.WindowUtil;
import de.andreasschrade.androidtemplate.utilities.bitmapUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private TextView mButtonCancel;
    private TextView mButtonLogon;
    private TextView mButtonRegister;
    private final int SELECT_PHOTO = 1;





    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WindowUtil.changeWindowBarColour(this);

        mUsernameEditText = (EditText) findViewById(R.id.editText);
        mPasswordEditText = (EditText) findViewById(R.id.editText2);
        mButtonCancel = (TextView) findViewById(R.id.button2);
        mButtonLogon = (TextView) findViewById(R.id.button);
        mButtonRegister = (TextView) findViewById(R.id.register);

        Backendless.initApp(this, "A0819152-C875-C222-FF18-0516AB9ACC00", "94E2E030-C1B8-0F27-FFEE-CD829BAE3400", "v1");
        Log.i("info", "backendless success");


        /*if(SaveSharedPreference.getUserName(LoginActivity.this).length() != 0)
        {
            Log.i("info", "divert to login page, already login");

            String currentUserObjectId = Backendless.UserService.loggedInUser();
            Backendless.UserService.findById(currentUserObjectId, asyncCallback );


            Log.i("info", "user logged in =" + Backendless.UserService.CurrentUser().getEmail());
        }

        Log.i("info", "Login Saved share = " + " " + SaveSharedPreference.getUserName(this));*/


        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCredentials(v);
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mButtonLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = mPasswordEditText.getText().toString().replaceAll("\\s+", "");
                String username = mUsernameEditText.getText().toString().replaceAll("\\s+","");

                final CustomDialogClass cdd = new CustomDialogClass(LoginActivity.this);
                cdd.progressDialog("Authenticating..");

                Backendless.UserService.login( username, password, new AsyncCallback<BackendlessUser>()
                {
                    public void handleResponse( BackendlessUser user )
                    {
                        Log.i("info", "login success");
                        cdd.checkDialog();
                        SaveSharedPreference.setUserName(LoginActivity.this, user.getObjectId());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }

                    public void handleFault( BackendlessFault fault )
                    {
                        cdd.checkDialog();
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again..",
                                Toast.LENGTH_LONG).show();
                        Log.i("info", "login failed" + fault.getCode());
                    }
                });


            }
        });
    }

    public void clearCredentials(View view) {
        mUsernameEditText.setText(null);
        mPasswordEditText.setText(null);
    }





}
