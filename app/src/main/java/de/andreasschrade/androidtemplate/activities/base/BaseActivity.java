package de.andreasschrade.androidtemplate.activities.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.Date;
import java.util.Iterator;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.core.SettingsActivity;
import de.andreasschrade.androidtemplate.activities.peripheral.HomeActivity;
import de.andreasschrade.androidtemplate.activities.peripheral.BidActivity;
import de.andreasschrade.androidtemplate.activities.core.LoginActivity;
import de.andreasschrade.androidtemplate.backendless.Bid;
import de.andreasschrade.androidtemplate.utilities.CustomDialogClass;
import de.andreasschrade.androidtemplate.utilities.SaveSharedPreference;
import de.andreasschrade.androidtemplate.utilities.StringUtil;
import de.andreasschrade.androidtemplate.utilities.Wrapper;
import de.andreasschrade.androidtemplate.utilities.bitmapUtil;
import de.andreasschrade.androidtemplate.wrapper.BidderContent;

import static de.andreasschrade.androidtemplate.utilities.LogUtil.logD;
import static de.andreasschrade.androidtemplate.utilities.LogUtil.makeLogTag;

/**
 * The base class for all Activity classes.
 * This class creates and provides the navigation drawer and toolbar.
 * The navigation logic is handled in {@link BaseActivity#goToNavDrawerItem(int)}
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String TAG = makeLogTag(BaseActivity.class);

    protected static final int NAV_DRAWER_ITEM_INVALID = -1;

    private DrawerLayout drawerLayout;
    private Toolbar actionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("infobase", "base created");

        Log.i("info", "git change two");




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();



        Wrapper.fa = this;

    }

    /**
     * Sets up the navigation drawer.
     */
    private void setupNavDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            // current activity does not have a drawer.
            return;
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerSelectListener(navigationView);
            setSelectedItem(navigationView);

        }

        Log.i("infobase", "setup navbar executed");

        String url = UserIdStorageFactory.instance().getStorage().get();

        /*Object sex = Backendless.UserService.CurrentUser().getProperty("sex");

        int gender = (int) sex;

        if (gender == 1) {

            Log.i("info", "male");

        } else if (gender == 2) {

            Log.i("info", "female");
        }*/



        Log.i("info", url);

        final String part = StringUtil.splitString(url);

        Log.i("info", part);

        String finalurl = "https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/" + part + ".png";




        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        final ImageView img = (ImageView)hView.findViewById(R.id.navbarpic);
        Glide.with(this).load(finalurl).asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {

                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });

        logD(TAG, "navigation drawer setup finished");

        img.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                Log.i("info", "nav img clicked");


            }
        });


    }

    /**
     * Updated the checked item in the navigation drawer
     * @param navigationView the navigation view
     */
    private void setSelectedItem(NavigationView navigationView) {
        // Which navigation item should be selected?
        int selectedItem = getSelfNavDrawerItem(); // subclass has to override this method
        navigationView.setCheckedItem(selectedItem);
    }

    /**
     * Creates the item click listener.
     * @param navigationView the navigation view
     */
    private void setupDrawerSelectListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        onNavigationItemClicked(menuItem.getItemId());
                        return true;
                    }
                });
    }

    /**
     * Handles the navigation item click.
     * @param itemId the clicked item
     */
    private void onNavigationItemClicked(final int itemId) {
        if(itemId == getSelfNavDrawerItem()) {
            // Already selected
            closeDrawer();
            return;
        }

        goToNavDrawerItem(itemId);
    }

    /**
     * Handles the navigation item click and starts the corresponding activity.
     * @param item the selected navigation item
     */
    private void goToNavDrawerItem(int item) {
        switch (item) {
            case R.id.nav_bids:

                BidderContent.clear();



                String whereClause = "tender = " + "'" + Wrapper.tenderId + "'";
                BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                dataQuery.setWhereClause(whereClause);




                Backendless.Persistence.of( Bid.class ).find(dataQuery,
                        new AsyncCallback<BackendlessCollection<Bid>>() {
                            @Override
                            public void handleResponse(BackendlessCollection<Bid> bid) {


                                Iterator<Bid> iterator = bid.getCurrentPage().iterator();

                                int counter = 1;

                                while (iterator.hasNext()) {


                                    final Bid bids = iterator.next();

                                    final String objId = bids.getObjectId();

                                    final String deviceId = bids.getDeviceId();


                                    final String convert = String.valueOf(counter);

                                    String[] parts = bids.getOwnerId().split("-");
                                    final String part = parts[4];


                                    Backendless.Persistence.of(BackendlessUser.class).findById(bids.getOwnerId(), new AsyncCallback<BackendlessUser>() {
                                        @Override
                                        public void handleResponse(BackendlessUser backendlessUser) {


                                            //Object name = backendlessUser.getProperty("firstname");

                                            Log.i("info", "bisuser success");

                                            String name = backendlessUser.getProperty("firstname").toString();


                                            Object dob = backendlessUser.getProperty("dob");


                                            Date dateof = (Date) dob;

                                            LocalDate birthdate = new LocalDate(dateof);
                                            LocalDate now = new LocalDate();
                                            Years age = Years.yearsBetween(birthdate, now);


                                            Log.i("dob", String.valueOf(age.getYears()));

                                            BidderContent.addItem(new BidderContent.BidderItem(convert, part, name, String.valueOf(age.getYears()), bids.getPickupline(), objId, deviceId));

                                            startActivity(new Intent(BaseActivity.this, BidActivity.class));
                                            finish();

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault backendlessFault) {

                                            Log.i("info", "bisuser failed" + backendlessFault.toString());

                                            startActivity(new Intent(BaseActivity.this, BidActivity.class));
                                            finish();

                                        }
                                    });


                                    counter++;
                                }


                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                                Log.i("info", "fault" + fault.toString());

                                startActivity(new Intent(BaseActivity.this, BidActivity.class));
                                finish();

                            }
                        });

                    break;
                    case R.id.nav_home:

                    Log.i("info", "home");

                    startActivity(new Intent(this, HomeActivity.class));
                    break;
                    case R.id.nav_settings:

                    startActivity(new Intent(this, SettingsActivity.class));

                    Log.i("info", "settings");

                    //finish();

                    break;
                    case R.id.nav_logout:
                    final CustomDialogClass cdd = new CustomDialogClass(BaseActivity.this);
                    cdd.progressDialog("Signing off..");
                    Backendless.UserService.logout(new AsyncCallback<Void>()

                    {
                        public void handleResponse (Void response)
                        {
                            cdd.checkDialog();
                            Log.i("info", "logged out");
                            SaveSharedPreference.setUserName(BaseActivity.this, "");
                            startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                        }

                    public void handleFault(BackendlessFault fault) {
                        cdd.checkDialog();
                        Log.i("info", "logout failed");

                    }
                });
                break;
        }
    }

    /**
     * Provides the action bar instance.
     * @return the action bar.
     */
    protected ActionBar getActionBarToolbar() {
        if (actionBarToolbar == null) {
            actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (actionBarToolbar != null) {
                setSupportActionBar(actionBarToolbar);
            }
        }
        return getSupportActionBar();
    }


    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * have to override this method.
     */
    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_ITEM_INVALID;
    }

    protected void openDrawer() {
        if(drawerLayout == null)
            return;

        drawerLayout.openDrawer(GravityCompat.START);
    }

    protected void closeDrawer() {
        if(drawerLayout == null)
            return;

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public abstract boolean providesActivityToolbar();

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
