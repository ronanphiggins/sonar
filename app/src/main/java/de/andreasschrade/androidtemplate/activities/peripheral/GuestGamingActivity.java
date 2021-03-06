package de.andreasschrade.androidtemplate.activities.peripheral;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.services.messaging.MessageStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.base.BaseActivity;
import de.andreasschrade.androidtemplate.backendless.Answer;
import de.andreasschrade.androidtemplate.backendless.Bid;
import de.andreasschrade.androidtemplate.backendless.SendBroadcastMethods;
import de.andreasschrade.androidtemplate.backendless.Session;
import de.andreasschrade.androidtemplate.backendless.Tender;
import de.andreasschrade.androidtemplate.backendless.Users;
import de.andreasschrade.androidtemplate.utilities.AnswerAdapter;
import de.andreasschrade.androidtemplate.utilities.AnswerTag;
import de.andreasschrade.androidtemplate.utilities.CountDownAnimation;
import de.andreasschrade.androidtemplate.utilities.CountdownUtility;
import de.andreasschrade.androidtemplate.utilities.StringUtil;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class GuestGamingActivity extends BaseActivity implements CountDownAnimation.CountDownListener {
    /**
     * Whether or not the activity is running on a device with a large screen
     */
    private boolean twoPaneMode;

    private static GuestGamingActivity ins;

    private boolean hasnotAnswered = false;

    EditText answeredittext;

    final ArrayList<String> players = new ArrayList<>();

    private Dialog dialog;

    private CountDownAnimation.CountDownListener countDlist;




    ListView listView;
    //List<String> values;
    //ArrayAdapter<String> adapter;

    List<Answer> values;
    AnswerAdapter adapter;

    FloatingActionButton mFloatingActionButton;


    View v;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        countDlist = this;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            setContentView(R.layout.activity_gaming_guest_huwai);

        } else {

            setContentView(R.layout.activity_gaming_guest);

        }


        v = getWindow().getDecorView().getRootView();

        ins = this;

        setupToolbar();
        setTitle("");

        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.fab8);
        answeredittext = (EditText) findViewById(R.id.answerText);

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView = (ListView) findViewById(R.id.list);



        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        /*values = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_textview, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);*/

        values = new ArrayList<Answer>();

        adapter = new AnswerAdapter(this, R.layout.itemlistrow, values);

        SwingRightInAnimationAdapter animationAdapter = new SwingRightInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);

        listView.setAdapter(animationAdapter);


        final ImageView img1 = (ImageView) findViewById(R.id.playerpic1);
        final ImageView img2 = (ImageView) findViewById(R.id.playerpic2);
        final ImageView img3 = (ImageView) findViewById(R.id.playerpic3);
        final ImageView img4 = (ImageView) findViewById(R.id.playerpic4);

        final ImageView[] IMGS = {img1, img2, img3, img4};

        Backendless.Persistence.of(Session.class).findById("6E66FA4F-C624-FDD7-FFC6-714FCD3EE100", new AsyncCallback<Session>() {
            @Override
            public void handleResponse(Session session) {


                if (session.getQuestion() == null) {

                    Log.i("info", "question is emtpy");

                    Snackbar.make(v, "Waiting for question...", Snackbar.LENGTH_INDEFINITE).show();




                } else {

                    Snackbar.make(v, session.getQuestion(), Snackbar.LENGTH_INDEFINITE).show();

                }



                BackendlessUser[] sessionplayers = session.getPlayers();

                for (BackendlessUser player : sessionplayers) {

                    if (!player.getObjectId().equalsIgnoreCase(UserIdStorageFactory.instance().getStorage().get())) {


                        players.add((String) player.getProperty("deviceId"));


                    }
                }

                Log.i("info", "other players =" + players);


                setTitle("Round" + " " + session.getRound());

                Log.i("info", "Round = " + session.getRound());

                int counter = 0;

                ArrayList<Answer> answers = session.getAnswers();

                hasnotAnswered = true;

                for (Answer ans : answers) {


                    if (ans.getOwnerId().equalsIgnoreCase(UserIdStorageFactory.instance().getStorage().get())) {

                        Log.i("info", "user has answered already");

                        hasnotAnswered = false;
                        mFloatingActionButton.setVisibility(View.INVISIBLE);
                        answeredittext.setVisibility(View.INVISIBLE);

                    }

                    Log.i("info", ans.getAnswer());


                    //values.add(ans.getAnswer());
                    values.add(ans);
                    adapter.notifyDataSetChanged();


                }



                mFloatingActionButton.setEnabled(hasnotAnswered);




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






        mFloatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("info", "clicked!!!!");


                    EditText theAnswer = (EditText) findViewById(R.id.answerText);

                    final String answerText = theAnswer.getText().toString();



                    Answer answer = new Answer();
                    answer.setAnswer(answerText);

                    Backendless.Persistence.of(Answer.class).save(answer, new AsyncCallback<Answer>() {
                        @Override
                        public void handleResponse(final Answer answer) {

                            final String answerId = answer.getObjectId();


                                Backendless.Persistence.of(Session.class).findById("6E66FA4F-C624-FDD7-FFC6-714FCD3EE100", new AsyncCallback<Session>() {
                                @Override
                                public void handleResponse(Session session) {

                                    ArrayList<Answer> answers = session.getAnswers();

                                    answers.add(answer);

                                    session.setAnswers(answers);

                                    //Answer[] answers = {answer};

                                    //session.setAnswers(answers);



                                    Backendless.Persistence.save( session, new AsyncCallback<Session>() {
                                        @Override
                                        public void handleResponse( Session response )
                                        {

                                            values.add(answer);
                                            adapter.notifyDataSetChanged();

                                            Log.i("info", "update success");


                                            hasnotAnswered = false;

                                            mFloatingActionButton.setEnabled(hasnotAnswered);

                                            mFloatingActionButton.setVisibility(View.INVISIBLE);

                                            answeredittext.setVisibility(View.INVISIBLE);

                                            android.util.Pair<DeliveryOptions, PublishOptions> pair = SendBroadcastMethods.PrepareBroadcast(players, "answertrigger", answerText, answerId);


                                            Backendless.Messaging.publish("", pair.second, pair.first, new AsyncCallback<MessageStatus>() {
                                                @Override
                                                public void handleResponse(MessageStatus response) {

                                                    Log.i("info", "message sent");



                                                }

                                                @Override
                                                public void handleFault(BackendlessFault backendlessFault) {


                                                    Log.i("info", backendlessFault.toString());


                                                }
                                            });







                                        }
                                        @Override
                                        public void handleFault( BackendlessFault fault )
                                        {

                                            Log.i("info", "update failed = " + fault.toString());

                                        }
                                    } );

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


                    /*Backendless.Persistence.of(Session.class).findById("207CBAA5-A0B5-72F9-FFE4-B52E9D3CB700", new AsyncCallback<Session>() {
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
                    });*/


                    return true;
                }
                return true; // consume the event
            }
        });










        // ListView Item Click Listener
        // Guest cannot click on list.
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                Answer  itemValue    = (Answer) listView.getItemAtPosition(position);

                AnswerTag thetag = (AnswerTag) view.getTag();

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " + thetag.returnId(), Toast.LENGTH_LONG)
                        .show();

                mFloatingActionButton.setEnabled(true);

            }

        });*/


        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {


                        Backendless.Persistence.of(Session.class).findById("6E66FA4F-C624-FDD7-FFC6-714FCD3EE100", new AsyncCallback<Session>() {
                            @Override
                            public void handleResponse(Session session) {

                                ArrayList<Answer> answers = session.getAnswers();

                                ArrayList<String> objectId = new ArrayList<String>();

                                for (Answer value : values) {

                                    objectId.add(value.getObjectId());

                                }

                                for (Answer ans : answers) {

                                    if (!objectId.contains(ans.getObjectId())) {


                                        values.add(ans);
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

    public static GuestGamingActivity  getInstance(){
        return ins;
    }


    public void UpdateTheQuestion(final String question) {


        Snackbar.make(v, question, Snackbar.LENGTH_INDEFINITE).show();

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.iphonesound);
        mp.start();

    }

    public void UpdateTheAnswer(final Answer answer) {


        //Toast.makeText(GuestGamingActivity.this, answer,
        //Toast.LENGTH_LONG).show();

        values.add(answer);
        adapter.notifyDataSetChanged();




    }

    public void DeclareWinner() {

        Toast.makeText(GuestGamingActivity.this, "Congrats you have Won the date!!!!",
                Toast.LENGTH_LONG).show();

    }


    public void refreshActivity() {

        dialog = CountdownUtility.CountdownHandler(GuestGamingActivity.this, countDlist);

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


    @Override
    public void onCountDownEnd(CountDownAnimation animation) {



        dialog.dismiss();
        finish();
        startActivity(getIntent());



    }
}
