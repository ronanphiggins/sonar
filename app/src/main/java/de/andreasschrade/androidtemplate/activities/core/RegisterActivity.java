package de.andreasschrade.androidtemplate.activities.core;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.backendless.Users;
import de.andreasschrade.androidtemplate.utilities.CustomDialogClass;
import de.andreasschrade.androidtemplate.utilities.WindowUtil;

public class RegisterActivity extends AppCompatActivity {


    EditText dob;
    EditText password;
    EditText gender;
    EditText confirmpassword;
    EditText firstname;
    EditText username;
    Calendar myCalendar;
    Button signup;

    private ProgressBar progressBar;
    private Boolean userExists;

    String sex;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        WindowUtil.changeWindowBarColour(this);

        userExists = true;

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        firstname = (EditText) findViewById(R.id.firstname);
        dob = (EditText) findViewById(R.id.dob);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        gender = (EditText) findViewById(R.id.gender);
        username = (EditText) findViewById(R.id.username);
        progressBar = (ProgressBar) findViewById(R.id.checkusernameprogress);
        signup = (Button) findViewById(R.id.signup);


        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!userExists && confirmfirstname() && confirmdob() && confirmgender() && confirmpassword()) {


                    //Toast.makeText(RegisterActivity.this, "Register Success",
                    //        Toast.LENGTH_LONG).show();



                    BackendlessUser user = new BackendlessUser();
                    user.setProperty( "username", username.getText().toString() );
                    user.setProperty("firstname", firstname.getText().toString());
                    //user.setProperty("dob", date);
                    user.setPassword( confirmpassword.getText().toString() );

                    final CustomDialogClass cdd = new CustomDialogClass(RegisterActivity.this);
                    cdd.progressDialog("Registering..");

                    Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
                    {
                        public void handleResponse( BackendlessUser registeredUser )
                        {

                            cdd.checkDialog();

                            Log.i("info", "register success");

                            Toast.makeText(RegisterActivity.this, "Register Success",
                                    Toast.LENGTH_LONG).show();

                        }

                        public void handleFault( BackendlessFault fault )
                        {

                            cdd.checkDialog();

                            Toast.makeText(RegisterActivity.this, "Register Failed",
                                    Toast.LENGTH_LONG).show();

                            Log.i("info", fault.toString());

                        }
                    } );


                } else {


                    Toast.makeText(RegisterActivity.this, "Please complete all fields",
                            Toast.LENGTH_LONG).show();


                }

            }
        });




        gender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.setContentView(R.layout.custom_dialog_gender);

                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.dimAmount=0.9f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
                dialog.getWindow().setAttributes(lp);

                final CheckBox chk1 = (CheckBox) dialog.findViewById(R.id.checkBox1);
                final CheckBox chk2 = (CheckBox) dialog.findViewById(R.id.checkBox2);


                chk1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            chk2.setChecked(false);
                            sex = ((CheckBox) v).getTag().toString();
                        }


                    }
                });

                chk2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            chk1.setChecked(false);
                            sex = ((CheckBox) v).getTag().toString();
                        }


                    }
                });

                TextView dialogButton = (TextView) dialog.findViewById(R.id.textViewsubmit);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        gender.setText(sex);
                        if (confirmgender()) {


                            gender.setBackgroundResource(R.drawable.my_button_bg_confirm);
                            gender.setTextColor(getResources().getColor(R.color.theme_primary_accent));


                        } else {


                            gender.setBackgroundResource(R.drawable.my_button_bg);
                            gender.setTextColor(getResources().getColor(R.color.theme_primary_dark));


                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });

        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                String whereClause = "username = " + "'" + username.getText().toString() + "'";

                BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                dataQuery.setWhereClause( whereClause );

                progressBar.setVisibility(View.VISIBLE);


                Backendless.Persistence.of( Users.class ).find( dataQuery,
                        new AsyncCallback<BackendlessCollection<Users>>(){
                            @Override
                            public void handleResponse( BackendlessCollection<Users> founduser )
                            {

                                if (founduser.getTotalObjects() > 0) {

                                    Log.i("info", "user exists");

                                    progressBar.setVisibility(View.INVISIBLE);

                                    userExists = true;

                                    username.setBackgroundResource(R.drawable.my_button_bg);
                                    username.setTextColor(getResources().getColor(R.color.theme_primary_dark));


                                } else {

                                    if (username.getText().toString().matches("")) {

                                        progressBar.setVisibility(View.INVISIBLE);

                                        userExists = true;

                                        username.setBackgroundResource(R.drawable.my_button_bg);
                                        username.setTextColor(getResources().getColor(R.color.theme_primary_dark));


                                    } else {


                                        Log.i("info", "user does not exist");

                                        progressBar.setVisibility(View.INVISIBLE);

                                        userExists = false;

                                        username.setBackgroundResource(R.drawable.my_button_bg_confirm);
                                        username.setTextColor(getResources().getColor(R.color.theme_primary_accent));

                                    }


                                }


                            }
                            @Override
                            public void handleFault( BackendlessFault fault )
                            {

                                Log.i("info", fault.toString());

                            }
                        });



            }
        });


        confirmpassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                Log.i("info", "text changed");
                if (confirmpassword()) {

                    Log.i("info", "passwords match");

                    confirmpassword.setBackgroundResource(R.drawable.my_button_bg_confirm);
                    confirmpassword.setTextColor(getResources().getColor(R.color.theme_primary_accent));

                    password.setBackgroundResource(R.drawable.my_button_bg_confirm);
                    password.setTextColor(getResources().getColor(R.color.theme_primary_accent));

                } else {

                    Log.i("info", "passwords do not match");
                    confirmpassword.setBackgroundResource(R.drawable.my_button_bg);
                    confirmpassword.setTextColor(getResources().getColor(R.color.theme_primary_dark));

                    password.setBackgroundResource(R.drawable.my_button_bg);
                    password.setTextColor(getResources().getColor(R.color.theme_primary_dark));
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                Log.i("info", "text changed");
                if (confirmpassword()) {

                    Log.i("info", "passwords match");

                    confirmpassword.setBackgroundResource(R.drawable.my_button_bg_confirm);
                    confirmpassword.setTextColor(getResources().getColor(R.color.theme_primary_accent));

                    password.setBackgroundResource(R.drawable.my_button_bg_confirm);
                    password.setTextColor(getResources().getColor(R.color.theme_primary_accent));

                } else {

                    Log.i("info", "passwords do not match");
                    confirmpassword.setBackgroundResource(R.drawable.my_button_bg);
                    confirmpassword.setTextColor(getResources().getColor(R.color.theme_primary_dark));

                    password.setBackgroundResource(R.drawable.my_button_bg);
                    password.setTextColor(getResources().getColor(R.color.theme_primary_dark));
                }
            }
        });

        firstname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (confirmfirstname()) {


                    firstname.setBackgroundResource(R.drawable.my_button_bg_confirm);
                    firstname.setTextColor(getResources().getColor(R.color.theme_primary_accent));


                } else {


                    firstname.setBackgroundResource(R.drawable.my_button_bg);
                    firstname.setTextColor(getResources().getColor(R.color.theme_primary_dark));


                }


            }
        });

        dob.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (confirmdob()) {


                    dob.setBackgroundResource(R.drawable.my_button_bg_confirm);
                    dob.setTextColor(getResources().getColor(R.color.theme_primary_accent));


                } else {


                    dob.setBackgroundResource(R.drawable.my_button_bg);
                    dob.setTextColor(getResources().getColor(R.color.theme_primary_dark));


                }


            }
        });







    }


    private boolean confirmgender() {

        if (!gender.getText().toString().matches("")) {

            return true;

        } else {

            return false;

        }

    }


    private boolean confirmdob() {

        if (!dob.getText().toString().matches("")) {

            return true;

        } else {

            return false;

        }

    }


    private boolean confirmfirstname() {

        if (!firstname.getText().toString().matches("")) {

            return true;

        } else {

            return false;

        }

    }





    private boolean confirmpassword() {

        String pass = password.getText().toString();
        String confirm = confirmpassword.getText().toString();

        if (pass.equals(confirm) && !pass.matches("") && !confirm.matches("")) {

            return true;

        } else {


            return false;
        }


    }



    private void updateLabel() {

        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        dob.setText(sdf.format(myCalendar.getTime()));
    }







}


