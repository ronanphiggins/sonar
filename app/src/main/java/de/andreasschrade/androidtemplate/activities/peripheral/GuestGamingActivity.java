package de.andreasschrade.androidtemplate.activities.peripheral;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.base.BaseActivity;
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



        /*final ImageView img = (ImageView) findViewById(R.id.playerpic1);
        Glide.with(this).load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/1072A8E44200.png").asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {

                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });

        final ImageView img2 = (ImageView) findViewById(R.id.playerpic2);
        Glide.with(this).load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/806D9415B500.png").asBitmap().fitCenter().into(new BitmapImageViewTarget(img2) {
            @Override
            protected void setResource(Bitmap resource) {

                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img2.setImageDrawable(circularBitmapDrawable);
            }
        });

        final ImageView img3 = (ImageView) findViewById(R.id.playerpic3);
        Glide.with(this).load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/D9C54F37AD00.png").asBitmap().fitCenter().into(new BitmapImageViewTarget(img3) {
            @Override
            protected void setResource(Bitmap resource) {

                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img3.setImageDrawable(circularBitmapDrawable);
            }
        });

        final ImageView img4 = (ImageView) findViewById(R.id.playerpic4);
        Glide.with(this).load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/F357AC7A6400.png").asBitmap().fitCenter().into(new BitmapImageViewTarget(img4) {
            @Override
            protected void setResource(Bitmap resource) {

                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img4.setImageDrawable(circularBitmapDrawable);
            }
        });*/


        final FloatingActionButton mFloatingActionButton   = (FloatingActionButton)findViewById(R.id.fab8);

        mFloatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("info", "clicked!!!!");







                    return true;
                }
                return true; // consume the event
            }
        });



        listView = (ListView) findViewById(R.id.list);

        values = new ArrayList<String>();

        values.add("Answer 1");
        values.add("Answer 2");
        values.add("Answer 3");
        values.add("Answer 4");



        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_textview, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

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
