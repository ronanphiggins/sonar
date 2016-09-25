package de.andreasschrade.androidtemplate.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.widget.Toast;


import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.backendless.Tender;
import de.andreasschrade.androidtemplate.ui.base.BaseActivity;
import de.andreasschrade.androidtemplate.utilities.bitmapUtil;

/**
 * Activity demonstrates some GUI functionalities from the Android support library.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ViewSamplesActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Bitmap theBitmap;

    private GoogleApiClient mGoogleApiClient;

    Double userLat;
    Double userLng;

    Boolean hasTender = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samples);
        ButterKnife.bind(this);
        setupToolbar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        Backendless.Persistence.of( Tender.class).find(new AsyncCallback<BackendlessCollection<Tender>>() {
            @Override
            public void handleResponse(BackendlessCollection<Tender> foundTenders) {
                // all Contact instances have been found

                Iterator<Tender> iterator = foundTenders.getCurrentPage().iterator();

                while (iterator.hasNext()) {

                    Tender tender = iterator.next();


                    String ownerId = tender.getOwnerId();
                    final Double tenderLat = tender.getLatitude();
                    final Double tenderLong = tender.getLongitude();

                    Log.i("info", "ownderid" + ownerId);
                    Log.i("info", "current user id" + Backendless.UserService.CurrentUser().getObjectId());

                    if (ownerId.equalsIgnoreCase(Backendless.UserService.CurrentUser().getObjectId())) {

                        Log.i("info", "hasTender");

                        hasTender = true;
                    }

                    String[] parts = ownerId.split("-");
                    final String part = parts[4];


                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Looper.prepare();
                            try {
                                theBitmap = Glide.
                                        with(ViewSamplesActivity.this).
                                        load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/" + part + ".jpg").
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

                                LatLng userPosition = new LatLng(tenderLat, tenderLong);

                                Bitmap newBitmap = bitmapUtil.getCircularBitmap(theBitmap);

                                int theColor = Color.parseColor("#C63D0F");

                                Bitmap newnewBitmap = bitmapUtil.addBorderToCircularBitmap(newBitmap, 15, theColor);

                                mMap.addMarker(new MarkerOptions().position(userPosition).title(Backendless.UserService.CurrentUser().getEmail()).icon(BitmapDescriptorFactory.fromBitmap(newnewBitmap)));




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


            AlertDialog.Builder builder = new AlertDialog.Builder(ViewSamplesActivity.this);
            builder.setTitle("Place a date");


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    Tender tender = new Tender();
                    tender.setLongitude(userLng);
                    tender.setLatitude(userLat);
                    tender.setTime("18.00");


                    Backendless.Persistence.of(Tender.class).save(tender, new AsyncCallback<Tender>() {
                        @Override
                        public void handleResponse(Tender tender) {

                            Log.i("info", "success post");


                            String currentId = Backendless.UserService.CurrentUser().getObjectId();
                            String[] parts = currentId.split("-");
                            final String part = parts[4];

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    Looper.prepare();
                                    try {
                                        theBitmap = Glide.
                                                with(ViewSamplesActivity.this).
                                                load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/" + part + ".jpg").
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


                                        mMap.addMarker(new MarkerOptions().position(userPosition).title(Backendless.UserService.CurrentUser().getEmail()).icon(BitmapDescriptorFactory.fromBitmap(theBitmap)));

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
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });


            builder.show();



        } else {

            Log.i("info", "tender already exists");

            Toast.makeText(ViewSamplesActivity.this, "You already have a pending offer",
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
        return R.id.nav_samples;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




        LatLng trinity = new LatLng(53.343262, -6.257071);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(trinity));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));


        Log.i("info", "inside map ready");

        if (ActivityCompat.checkSelfPermission(ViewSamplesActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ViewSamplesActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewSamplesActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

            }
        }

    }






    @Override
    public void onConnected(Bundle bundle) {
        Log.i("info", "connected");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {

            Log.i("info", "null location");



        }
        else {
            Log.i("info", "not null location");


        };


    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i("info", "connection failed");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
