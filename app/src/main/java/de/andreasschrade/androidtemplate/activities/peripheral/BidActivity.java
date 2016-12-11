package de.andreasschrade.androidtemplate.activities.peripheral;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.wrapper.BidderContent;
import de.andreasschrade.androidtemplate.activities.base.BaseActivity;
import de.andreasschrade.androidtemplate.utilities.LogUtil;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class BidActivity extends BaseActivity implements BidListFragment.Callback {
    /**
     * Whether or not the activity is running on a device with a large screen
     */
    private boolean twoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

        setupToolbar();

        setTitle("");

        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST","TWO POANE TASDFES");
            enableActiveItemState();
        }

        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }


        FloatingActionButton mFloatingActionButton   = (FloatingActionButton)findViewById(R.id.fab8);

        mFloatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("info", "clicked!!!!");


                    Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
                        @Override
                        public void handleResponse(DeviceRegistration deviceRegistration) {


                            if (deviceRegistration.getDeviceId().equalsIgnoreCase("04157df43901b531")) {

                                /*Intent i = new Intent(BidActivity.this, HostGamingActivity.class);
                                i.putExtra("deviceId", deviceRegistration.getDeviceId());
                                startActivity(i);*/

                                Intent i = new Intent(BidActivity.this, InitiatorGamingActivity.class);
                                i.putExtra("deviceId", deviceRegistration.getDeviceId());
                                startActivity(i);

                            } else {

                                /*Intent i = new Intent(BidActivity.this, PlayerGamingActivity.class);
                                i.putExtra("deviceId", deviceRegistration.getDeviceId());
                                startActivity(i);*/

                                Intent i = new Intent(BidActivity.this, GuestGamingActivity.class);
                                i.putExtra("deviceId", deviceRegistration.getDeviceId());
                                startActivity(i);

                            }


                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }

                    });





                    /*for (int i = 0; i < BidderContent.count(); i ++) {

                        Log.i("info", BidderContent.ITEMS.get(i).deviceId);

                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.addPushSinglecast(BidderContent.ITEMS.get(i).deviceId);

                        PublishOptions publishOptions = new PublishOptions();
                        publishOptions.putHeader("android-ticker-text", "game");
                        publishOptions.putHeader("android-content-title", "Sonar");
                        publishOptions.putHeader("android-content-text", "");

                        Backendless.Messaging.publish("You have been invited to a session? Click to start!", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                            @Override
                            public void handleResponse(MessageStatus response) {

                                Log.i("info", "message sent");


                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {


                                Log.i("info", backendlessFault.toString());


                            }
                        });

                    }*/



                    return true;
                }
                return true; // consume the event
            }
        });




        //Glide.with(this).load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/ECC5B9F0.jpg").into(img);
    }

    /**
     * Called when an item has been selected
     *
     * @param id the selected quote ID
     */
    @Override
    public void onItemSelected(String id) {
        if (twoPaneMode) {
            // Show the quote detail information by replacing the DetailFragment via transaction.
            ProfileDetailFragment fragment = ProfileDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
        } else {
            // Start the detail activity in single pane mode.
            Intent detailIntent = new Intent(this, ProfileActivity.class);
            detailIntent.putExtra(ProfileDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDetailFragment() {
        ProfileDetailFragment fragment =  ProfileDetailFragment.newInstance(BidderContent.ITEMS.get(0).id);
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    /**
     * Enables the functionality that selected items are automatically highlighted.
     */
    private void enableActiveItemState() {
        BidListFragment fragmentById = (BidListFragment) getFragmentManager().findFragmentById(R.id.article_list);
        fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Is the container present? If so, we are using the two-pane layout.
     *
     * @return true if the two pane layout is used.
     */
    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.article_detail_container) != null;
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
        return R.id.nav_bids;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}
