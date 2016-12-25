package de.andreasschrade.androidtemplate.activities.core;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.backendless.DeviceRegistration;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.policy.BackendlessUserAdaptingPolicy;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.services.messaging.MessageStatus;


import org.florescu.android.util.BitmapUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.peripheral.HomeActivity;
import de.andreasschrade.androidtemplate.backendless.Bid;
import de.andreasschrade.androidtemplate.backendless.Callbacks;
import de.andreasschrade.androidtemplate.backendless.Tender;
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




        if (Backendless.UserService.loggedInUser() == "") {

            Log.i("info", "Logged out");
        } else {

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

            //String userId = UserIdStorageFactory.instance().getStorage().get();

            //Backendless.Persistence.of(BackendlessUser.class).findById(userId, Callbacks.callback);

        }


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

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));


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
                    public void handleResponse(final BackendlessUser user )
                    {
                        Log.i("info", "login success");



                        Backendless.Messaging.registerDevice("670988742449", "default", new AsyncCallback<Void>() {
                            @Override
                            public void handleResponse(Void response) {

                                Log.i("info", "registered");

                                //Log.i("info", response.toString());


                                Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
                                    @Override
                                    public void handleResponse(DeviceRegistration deviceRegistration) {

                                        user.setProperty("deviceId", deviceRegistration.getDeviceId());

                                        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                                            @Override
                                            public void handleResponse(BackendlessUser backendlessUser) {

                                                cdd.checkDialog();
                                                SaveSharedPreference.setUserName(LoginActivity.this, user.getObjectId());
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));


                                            }

                                            @Override
                                            public void handleFault(BackendlessFault backendlessFault) {

                                            }
                                        });



                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {

                                    }

                                });



                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {

                                Log.i("info", "failed to register, error is " + " " + backendlessFault);

                            }
                        });












                    }

                    public void handleFault( BackendlessFault fault )
                    {
                        cdd.checkDialog();
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again..",
                                Toast.LENGTH_LONG).show();
                        Log.i("info", "login failed" + fault.getCode());
                    }
                },true);


            }
        });
    }

    public void clearCredentials(View view) {
        mUsernameEditText.setText(null);
        mPasswordEditText.setText(null);
    }





}
