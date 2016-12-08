package de.andreasschrade.androidtemplate.activities.peripheral;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.services.messaging.MessageStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.base.BaseActivity;
import de.andreasschrade.androidtemplate.backendless.Answer;
import de.andreasschrade.androidtemplate.backendless.Session;
import de.andreasschrade.androidtemplate.backendless.Tender;
import de.andreasschrade.androidtemplate.backendless.Users;
import de.andreasschrade.androidtemplate.utilities.StringUtil;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class GuestGamingActivity extends BaseActivity {
    /**
     * Whether or not the activity is running on a device with a large screen
     */
    private boolean twoPaneMode;

    ListView listView;
    List<String> values;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming_guest);

        setupToolbar();
        setTitle("");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView = (ListView) findViewById(R.id.list);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        values = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_textview, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);



        final ImageView img1 = (ImageView) findViewById(R.id.playerpic1);
        final ImageView img2 = (ImageView) findViewById(R.id.playerpic2);
        final ImageView img3 = (ImageView) findViewById(R.id.playerpic3);
        final ImageView img4 = (ImageView) findViewById(R.id.playerpic4);

        final ImageView[] IMGS = {img1, img2, img3, img4};

        Backendless.Persistence.of(Session.class).findById("207CBAA5-A0B5-72F9-FFE4-B52E9D3CB700", new AsyncCallback<Session>() {
            @Override
            public void handleResponse(Session session) {


                //TextView textView = (TextView) findViewById(R.id.textView2);

                //textView.setText(session.getRound());

                setTitle("Round" + " " + session.getRound());


                Log.i("info", "Round = " + session.getRound());

                int counter = 0;

                Answer[] answers = session.getAnswers();

                Log.i("info", "ans =" + answers.length);

                //Log.i("info", "answers = " + answers.toString());
                for (Answer ans : answers) {


                    Log.i("info", ans.getAnswer());


                    values.add(ans.getAnswer());
                    adapter.notifyDataSetChanged();


                }




                BackendlessUser[] players = session.getPlayers();
                for (BackendlessUser user : players) {

                    //Log.i("info", "userid = " + user.getProperty("objectId"));

                    final String url = StringUtil.splitString(user.getProperty("objectId").toString());

                    Log.i("info", url);

                    Log.i("info", IMGS[counter].toString());

                    final ImageView theimage = IMGS[counter];
                    Glide.with(GuestGamingActivity.this).load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/" + url + ".png").asBitmap().fitCenter().into(new BitmapImageViewTarget(theimage) {
                        @Override
                        protected void setResource(Bitmap resource) {

                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            theimage.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    counter ++;

                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {


            }
        });




        final FloatingActionButton mFloatingActionButton   = (FloatingActionButton)findViewById(R.id.fab8);

        mFloatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("info", "clicked!!!!");


                    Backendless.Persistence.of(Session.class).findById("207CBAA5-A0B5-72F9-FFE4-B52E9D3CB700", new AsyncCallback<Session>() {
                        @Override
                        public void handleResponse(Session session) {



                            Answer[] answers = session.getAnswers();

                            for (Answer ans : answers) {

                                Backendless.Persistence.of( Answer.class ).remove(ans,
                                        new AsyncCallback<Long>()
                                        {
                                            public void handleResponse( Long response )
                                            {

                                            }
                                            public void handleFault( BackendlessFault fault )
                                            {

                                            }
                                        } );

                            }

                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {


                        }
                    });




                    return true;
                }
                return true; // consume the event
            }
        });










        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

                mFloatingActionButton.setEnabled(true);

            }

        });


        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {


                        Backendless.Persistence.of(Session.class).findById("207CBAA5-A0B5-72F9-FFE4-B52E9D3CB700", new AsyncCallback<Session>() {
                            @Override
                            public void handleResponse(Session session) {

                                Answer[] answers = session.getAnswers();

                                for (Answer ans : answers) {

                                    if (!values.contains(ans.getAnswer())) {

                                        values.add(ans.getAnswer());
                                        adapter.notifyDataSetChanged();


                                    }




                                }

                                Toast.makeText(GuestGamingActivity.this, "refresh success",
                                        Toast.LENGTH_LONG).show();

                                swipeRefreshLayout.setRefreshing(false);



                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {

                                Toast.makeText(GuestGamingActivity.this, "refresh failure",
                                        Toast.LENGTH_LONG).show();

                                swipeRefreshLayout.setRefreshing(false);



                            }
                        });



                    }
                }
        );





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

    /*@Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_bids;
    }*/

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}
