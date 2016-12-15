package de.andreasschrade.androidtemplate.activities.peripheral;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.services.messaging.MessageStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.nearby.internal.connection.dev.GetLocalDeviceIdParams;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.core.LoginActivity;
import de.andreasschrade.androidtemplate.activities.core.SettingsActivity;
import de.andreasschrade.androidtemplate.backendless.Bid;
import de.andreasschrade.androidtemplate.backendless.Tender;
import de.andreasschrade.androidtemplate.activities.base.BaseActivity;
import de.andreasschrade.androidtemplate.utilities.CustomDialogClass;
import de.andreasschrade.androidtemplate.utilities.Person;
import de.andreasschrade.androidtemplate.utilities.SaveSharedPreference;
import de.andreasschrade.androidtemplate.utilities.StringUtil;
import de.andreasschrade.androidtemplate.utilities.Wrapper;
import de.andreasschrade.androidtemplate.utilities.bitmapUtil;
import de.andreasschrade.androidtemplate.wrapper.BidderContent;

/**
 * Activity demonstrates some GUI functionalities from the Android support library.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class HomeActivity extends BaseActivity implements OnMapReadyCallback, ClusterManager.OnClusterItemClickListener<Person>, ClusterManager.OnClusterClickListener<Person> {

    private GoogleMap mMap;
    private Bitmap theBitmap;

    Double userLat;
    Double userLng;

    Boolean hasTender = false;

    String dateType;


    String dateObjectId;

    private ClusterManager<Person> mClusterManager;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupToolbar();

        //final List<String> hasBidded = new ArrayList<String>();


        int hasFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);

            return;
        }

        afterPermissionCheck();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                afterPermissionCheck();


            } else {


                Toast.makeText(HomeActivity.this, "Location permission denied",
                        Toast.LENGTH_LONG).show();


            }
        }

    }


    protected void afterPermissionCheck() {


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Backendless.Persistence.of( Tender.class).find(new AsyncCallback<BackendlessCollection<Tender>>() {
            @Override
            public void handleResponse(final BackendlessCollection<Tender> foundTenders) {

                Iterator<Tender> iterator = foundTenders.getCurrentPage().iterator();

                while (iterator.hasNext()) {

                    final Tender tender = iterator.next();


                    final String ownerId = tender.getOwnerId();
                    final String tenderId = tender.getObjectId();
                    final Double tenderLat = tender.getLatitude();
                    final Double tenderLong = tender.getLongitude();

                    /*Bid[] bidders = tender.getBidders();

                    //Bid[] bids = (Bid[]) bidders;

                    for (Bid bid : bidders) {

                        Log.i("info", "Bid object ID = " + bid.getObjectId());

                        if (bid.getOwnerId().equalsIgnoreCase(UserIdStorageFactory.instance().getStorage().get())) {


                            Log.i("info", "I am a bidder for tender: " + tender.getObjectId());

                            hasBidded.add(tender.getObjectId());


                        }
                    }*/



                    if (ownerId.equalsIgnoreCase(UserIdStorageFactory.instance().getStorage().get())) {

                        Log.i("info", "hasTender");

                        hasTender = true;

                        dateObjectId = tender.getObjectId();

                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                        Wrapper.tenderId = tenderId;

                        fab.hide();



                    }


                    final String url = StringUtil.splitString(ownerId);


                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if (Looper.myLooper() == null)
                            {
                                Looper.prepare();
                            }
                            try {
                                theBitmap = Glide.
                                        with(HomeActivity.this).
                                        load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/" + url + ".png").
                                        asBitmap().
                                        into(200, 200).
                                        get();
                            } catch (final ExecutionException e) {

                            } catch (final InterruptedException e) {

                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void dummy) {
                            if (null != theBitmap) {

                                Log.i("info", "bitmap success");

                                //if(!hasBidded.contains(tender.getObjectId())) {


                                LatLng userPosition = new LatLng(tenderLat, tenderLong);


                                Bitmap newBitmap = bitmapUtil.getCircularBitmap(theBitmap);
                                int theColor = Color.parseColor("#C63D0F");
                                Bitmap newnewBitmap = bitmapUtil.addBorderToCircularBitmap(newBitmap, 15, theColor);

                                mClusterManager.addItem(new Person(userPosition, "Walter", newnewBitmap, tenderId, ownerId));

                                mClusterManager.cluster();




                                    /*Bitmap newBitmap = bitmapUtil.getCircularBitmap(theBitmap);
                                    int theColor = Color.parseColor("#C63D0F");
                                    Bitmap newnewBitmap = bitmapUtil.addBorderToCircularBitmap(newBitmap, 15, theColor);
                                    mMap.addMarker(new MarkerOptions().position(userPosition).title(tenderId).icon(BitmapDescriptorFactory.fromBitmap(newnewBitmap)).snippet(ownerId));*/

                                //}


                            }

                        }
                    }.execute();


                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });






    }





    @OnClick(R.id.fab)
    public void onFabClicked(View view) {

        if (hasTender != true) {




            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog);

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount=0.9f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
            dialog.getWindow().setAttributes(lp);




            /*final EditText eReminderTime = (EditText) dialog.findViewById(R.id.editText3);
            eReminderTime.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(HomeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            eReminderTime.setText( selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });*/



            final CheckBox chk1 = (CheckBox) dialog.findViewById(R.id.checkBox1);
            final CheckBox chk2 = (CheckBox) dialog.findViewById(R.id.checkBox2);
            final CheckBox chk3 = (CheckBox) dialog.findViewById(R.id.checkBox3);



            chk1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        chk2.setChecked(false);
                        chk3.setChecked(false);
                        dateType = ((CheckBox) v).getTag().toString();
                    }


                }
            });

            chk2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        chk1.setChecked(false);
                        chk3.setChecked(false);
                        dateType = ((CheckBox) v).getTag().toString();
                    }


                }
            });

            chk3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (((CheckBox) v).isChecked()) {
                        chk1.setChecked(false);
                        chk2.setChecked(false);
                        dateType = ((CheckBox) v).getTag().toString();
                    }


                }
            });


            TextView dialogButton = (TextView) dialog.findViewById(R.id.textViewsubmit);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("info", dateType);

                    createTender(dateType);
                    dialog.dismiss();
                }
            });



            dialog.show();






        } else {

            Log.i("info", "tender already exists");

            Toast.makeText(HomeActivity.this, "You already have a pending offer",
                    Toast.LENGTH_LONG).show();
        }


    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_home;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng myLoc;


        Log.i("info", "inside map ready");

        if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            if(!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if(myLocation!=null){
                Log.i("info", "not null location");

                userLat = myLocation.getLatitude();
                userLng = myLocation.getLongitude();

                myLoc = new LatLng(userLat, userLng);


            } else {

                myLoc = new LatLng(53.342842, -6.262122);

            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));

            mClusterManager = new ClusterManager<Person>(this, mMap);
            mClusterManager.setRenderer(new PersonRenderer());
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);
            mClusterManager.setOnClusterItemClickListener(this);
            mClusterManager.setOnClusterClickListener(this);


        }

        //mMap.setOnMarkerClickListener(this);

        //var mc = new MarkerClusterer(map);

    }




    /*@Override
    public boolean onMarkerClick(final Marker marker) {

        Log.i("info", "marker clicked");

        Log.i("info", marker.getId());

        Log.i("info", marker.getSnippet());



        //String part = StringUtil.splitString(Backendless.UserService.CurrentUser().getObjectId());

        if (marker.getSnippet().equalsIgnoreCase(UserIdStorageFactory.instance().getStorage().get())) {

            Log.i("info", "current user clicked");

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog_delete);

            TextView dialogButton = (TextView) dialog.findViewById(R.id.textViewdelete);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Tender tender = new Tender();
                    tender.setObjectId(dateObjectId);

                    Backendless.Persistence.of(Tender.class).remove(tender,
                            new AsyncCallback<Long>() {
                                public void handleResponse(Long response) {
                                    Log.i("info", "delete success");

                                    marker.remove();
                                    hasTender = false;

                                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                                    fab.show();
                                }

                                public void handleFault(BackendlessFault fault) {

                                    Log.i("info", "delete failed" + fault);

                                }
                            } );

                    dialog.dismiss();
                }
            });

            dialog.show();



        } else {


            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog_bid);

            TextView dialogButton = (TextView) dialog.findViewById(R.id.textViewbid);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("info", marker.getTitle());

                    Bid bid = new Bid();
                    bid.setPickupline("Brunch?");
                    bid.setTender(marker.getTitle());


                    Backendless.Persistence.of(Bid.class).save(bid, new AsyncCallback<Bid>() {
                        @Override
                        public void handleResponse(Bid bid) {


                            Backendless.Persistence.of(Tender.class).findById(marker.getTitle(), new AsyncCallback<Tender>() {
                                @Override
                                public void handleResponse(Tender tender) {

                                    DeliveryOptions deliveryOptions = new DeliveryOptions();
                                    deliveryOptions.addPushSinglecast(tender.getDeviceId());

                                    PublishOptions publishOptions = new PublishOptions();
                                    publishOptions.putHeader("android-ticker-text", "");
                                    publishOptions.putHeader("android-content-title", "Sonar");
                                    publishOptions.putHeader("android-content-text", "");

                                    Backendless.Messaging.publish("You have a new bid!", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                                        @Override
                                        public void handleResponse(MessageStatus response) {

                                            Log.i("info", "message sent");


                                        }

                                        @Override
                                        public void handleFault(BackendlessFault backendlessFault) {


                                            Log.i("info", backendlessFault.toString());


                                        }
                                    });

                                    dialog.dismiss();


                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {


                                }
                            });


                            Log.i("info", "success post");


                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                            Log.i("info", "failed post" + backendlessFault);

                            dialog.dismiss();
                        }
                    });


                }
            });

            dialog.show();

        }




        /*Intent detailIntent = new Intent(this, ProfileActivity.class);
        detailIntent.putExtra(ProfileDetailFragment.ARG_ITEM_ID, 0);
        startActivity(detailIntent);

        Wrapper.markerId = marker.getTitle();

        Log.i("info", "get=" + marker.getTitle());*/




        /*Bid bid = new Bid();
        bid.setPickupline("Dinner?");
        bid.setTender(marker.getTitle());


        Backendless.Persistence.of(Bid.class).save(bid, new AsyncCallback<Bid>() {
            @Override
            public void handleResponse(Bid bid) {


                Log.i("info", "success post");


            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

                Log.i("info", "failed post" + backendlessFault);
            }
        });




        return true;
    }*/


    public void createTender(final String type) {



        Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
            @Override
            public void handleResponse(DeviceRegistration deviceRegistration) {


                final Tender tender = new Tender();
                tender.setLongitude(userLng);
                tender.setLatitude(userLat);
                tender.setType(type);
                tender.setDeviceId(deviceRegistration.getDeviceId());

                Log.i("info", "Device ID=" + deviceRegistration.getDeviceId());

                Backendless.Persistence.of(Tender.class).save(tender, new AsyncCallback<Tender>() {
                    @Override
                    public void handleResponse(final Tender tender) {


                        Log.i("info", "success post");


                        String currentId = UserIdStorageFactory.instance().getStorage().get();
                        String[] parts = currentId.split("-");
                        final String part = parts[4];
                        final String tenderId = tender.getObjectId();


                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }
                                try {
                                    theBitmap = Glide.
                                            with(HomeActivity.this).
                                            load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/" + part + ".png").
                                            asBitmap().
                                            into(200, 200).
                                            get();
                                } catch (final ExecutionException e) {

                                } catch (final InterruptedException e) {

                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void dummy) {
                                if (null != theBitmap) {

                                    Log.i("info", "bitmap success");

                                    LatLng userPosition = new LatLng(userLat, userLng);

                                    Bitmap newBitmap = bitmapUtil.getCircularBitmap(theBitmap);
                                    int theColor = Color.parseColor("#C63D0F");
                                    Bitmap newnewBitmap = bitmapUtil.addBorderToCircularBitmap(newBitmap, 15, theColor);

                                    mClusterManager.addItem(new Person(userPosition, "Walter", newnewBitmap, tenderId, UserIdStorageFactory.instance().getStorage().get()));

                                    mClusterManager.cluster();

                                    /*Bitmap newBitmap = bitmapUtil.getCircularBitmap(theBitmap);
                                    int theColor = Color.parseColor("#C63D0F");
                                    Bitmap newnewBitmap = bitmapUtil.addBorderToCircularBitmap(newBitmap, 15, theColor);
                                    mMap.addMarker(new MarkerOptions().position(userPosition).icon(BitmapDescriptorFactory.fromBitmap(newnewBitmap)).title(tenderId).snippet(UserIdStorageFactory.instance().getStorage().get()));*/

                                    dateObjectId = tender.getObjectId();

                                    Wrapper.tenderId = tenderId;

                                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                                    fab.hide();


                                }
                                ;
                            }
                        }.execute();


                        hasTender = true;

                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                        Log.i("info", "failed post" + backendlessFault);
                    }
                });


            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });



    }

    @Override
    public boolean onClusterItemClick(final Person item) {
        Log.i("info", "clicked : " + item.tendId);

        if (item.userId.equalsIgnoreCase(UserIdStorageFactory.instance().getStorage().get())) {

            Log.i("info", "current user clicked");

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog_delete);

            TextView dialogButton = (TextView) dialog.findViewById(R.id.textViewdelete);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Tender tender = new Tender();
                    tender.setObjectId(dateObjectId);

                    Backendless.Persistence.of(Tender.class).remove(tender,
                            new AsyncCallback<Long>() {
                                public void handleResponse(Long response) {
                                    Log.i("info", "delete success");

                                    mClusterManager.removeItem(item);
                                    mClusterManager.cluster();
                                    hasTender = false;

                                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                                    fab.show();
                                }

                                public void handleFault(BackendlessFault fault) {

                                    Log.i("info", "delete failed" + fault);

                                }
                            } );

                    dialog.dismiss();
                }
            });

            dialog.show();



        } else {


            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog_bid);


            TextView dialogButtonNo = (TextView) dialog.findViewById(R.id.textViewbidNo);

            dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });




            TextView dialogButton = (TextView) dialog.findViewById(R.id.textViewbid);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
                        @Override
                        public void handleResponse(DeviceRegistration deviceRegistration) {


                            Log.i("info", item.tendId);

                            Bid bid = new Bid();
                            bid.setPickupline("Brunch?");
                            bid.setTender(item.tendId);
                            bid.setDeviceId(deviceRegistration.getDeviceId());


                            Backendless.Persistence.of(Bid.class).

                                    save(bid, new AsyncCallback<Bid>() {
                                                @Override
                                                public void handleResponse(Bid bid) {


                                                    Backendless.Persistence.of(Tender.class).findById(item.tendId, new AsyncCallback<Tender>() {
                                                        @Override
                                                        public void handleResponse(Tender tender) {

                                                            DeliveryOptions deliveryOptions = new DeliveryOptions();
                                                            deliveryOptions.addPushSinglecast(tender.getDeviceId());

                                                            PublishOptions publishOptions = new PublishOptions();
                                                            publishOptions.putHeader("android-ticker-text", "");
                                                            publishOptions.putHeader("android-content-title", "Sonar");
                                                            publishOptions.putHeader("android-content-text", "");

                                                            Backendless.Messaging.publish("You have a new bid!", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                                                                @Override
                                                                public void handleResponse(MessageStatus response) {

                                                                    Log.i("info", "message sent");


                                                                }

                                                                @Override
                                                                public void handleFault(BackendlessFault backendlessFault) {


                                                                    Log.i("info", backendlessFault.toString());


                                                                }
                                                            });

                                                            dialog.dismiss();


                                                        }

                                                        @Override
                                                        public void handleFault(BackendlessFault backendlessFault) {


                                                        }
                                                    });


                                                    Log.i("info", "success post");


                                                }

                                                @Override
                                                public void handleFault(BackendlessFault backendlessFault) {

                                                    Log.i("info", "failed post" + backendlessFault);

                                                    dialog.dismiss();
                                                }
                                            }

                                    );


                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }

                    });




                    }
                });

                    dialog.show();

            }



            return true;
        }




    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {


        Log.i("info", "cluster clicked");

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    private class PersonRenderer extends DefaultClusterRenderer<Person> {

        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final int mDimension;


        public PersonRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);



            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected int getColor(int clusterSize) {
            return Color.parseColor("#C63D0F");
        }



        @Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            //mImageView.setImageResource(person.profilePhoto);
            //Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(person.profilePhoto));
        }




        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }




}
