package de.andreasschrade.androidtemplate.activities.peripheral;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.Pair;
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
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.services.messaging.MessageStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.base.BaseActivity;
import de.andreasschrade.androidtemplate.activities.core.LoginActivity;
import de.andreasschrade.androidtemplate.backendless.Answer;
import de.andreasschrade.androidtemplate.backendless.SendBroadcastMethods;
import de.andreasschrade.androidtemplate.backendless.Session;
import de.andreasschrade.androidtemplate.utilities.AnswerAdapter;
import de.andreasschrade.androidtemplate.utilities.AnswerTag;
import de.andreasschrade.androidtemplate.utilities.LogUtil;
import de.andreasschrade.androidtemplate.utilities.SaveSharedPreference;
import de.andreasschrade.androidtemplate.utilities.StringUtil;
import de.andreasschrade.androidtemplate.wrapper.BidderContent;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class InitiatorGamingActivity extends BaseActivity {


    private boolean twoPaneMode;

    private static InitiatorGamingActivity instwo;

    final ArrayList<String> players = new ArrayList<>();


    private boolean generateQuestion = false;

    ListView listView;
    //List<String> values;
    //ArrayAdapter<String> adapter;

    List<Answer> values;
    AnswerAdapter adapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamingshell);

        setupToolbar();
        setTitle("");

        instwo = this;

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView = (ListView) findViewById(R.id.list);

        final FloatingActionButton mFloatingActionButton   = (FloatingActionButton)findViewById(R.id.fab8);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        /*values = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_textview, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);*/

        values = new ArrayList<Answer>();

        adapter = new AnswerAdapter(this, R.layout.itemlistrow, values);

        listView.setAdapter(adapter);

        final String[] questions = {"\"What is your favourite Season?\"", "\"What is your favourite movie?\"", "\"What is your favourite food?\""};

        final TextView question = (TextView) findViewById(R.id.thequestion);


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

                    generateQuestion = true;


                } else {

                    generateQuestion = false;
                    question.setText(session.getQuestion());

                }

                mFloatingActionButton.setEnabled(generateQuestion);



                BackendlessUser[] sessionplayers = session.getPlayers();

                for (BackendlessUser player : sessionplayers) {

                    if (!player.getObjectId().equalsIgnoreCase(UserIdStorageFactory.instance().getStorage().get())) {


                        players.add((String) player.getProperty("deviceId"));


                    }
                }

                Log.i("info", "other players =" + players);




                //TextView textView = (TextView) findViewById(R.id.textView2);

                //textView.setText(session.getRound());

                setTitle("Round" + " " + session.getRound());


                Log.i("info", "Round = " + session.getRound());

                int counter = 0;

                ArrayList<Answer> answers = session.getAnswers();

                //Log.i("info", "ans =" + answers.length);

                //Log.i("info", "answers = " + answers.toString());
                for (Answer ans : answers) {


                    Log.i("info", ans.getAnswer());


                    //values.add(ans.getAnswer());
                    values.add(ans);
                    adapter.notifyDataSetChanged();


                }




                BackendlessUser[] players = session.getPlayers();
                for (BackendlessUser user : players) {

                    //Log.i("info", "userid = " + user.getProperty("objectId"));

                    final String url = StringUtil.splitString(user.getProperty("objectId").toString());

                    Log.i("info", url);

                    Log.i("info", IMGS[counter].toString());

                    final ImageView theimage = IMGS[counter];
                    Glide.with(InitiatorGamingActivity.this).load("https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/" + url + ".png").asBitmap().fitCenter().into(new BitmapImageViewTarget(theimage) {
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




                    /*Random rnd = new Random();
                     int rndIndex = rnd.nextInt(questions.length);
                     final String theQuestion = questions[rndIndex];  

                    question.setText(theQuestion);*/


                    //////////////////////////////////////////////////////

                    //Uncomment after message test

                    Random rnd = new Random();
                    int rndIndex = rnd.nextInt(questions.length);
                    final String theQuestion = questions[rndIndex];



                    Backendless.Persistence.of(Session.class).findById("6E66FA4F-C624-FDD7-FFC6-714FCD3EE100", new AsyncCallback<Session>() {
                        @Override
                        public void handleResponse(Session session) {

                            session.setQuestion(theQuestion);

                            Backendless.Persistence.save( session, new AsyncCallback<Session>() {
                                @Override
                                public void handleResponse( Session response )
                                {

                                    generateQuestion = false;
                                    mFloatingActionButton.setEnabled(generateQuestion);
                                    question.setText(theQuestion);

                                    android.util.Pair<DeliveryOptions, PublishOptions> pair = SendBroadcastMethods.PrepareBroadcast(players, "questiontrigger", theQuestion, "null");


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

                                    Log.i("info", "update success");

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


                    //////////////////////////////////////////////////////


                    /*Answer answer = new Answer();
                    answer.setAnswer("programatic set answer");



                    Backendless.Persistence.of(Answer.class).save(answer, new AsyncCallback<Answer>() {
                        @Override
                        public void handleResponse(final Answer answer) {


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

                                            Log.i("info", "update success");

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
                    });*/





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
                //int itemPosition     = position;

                // ListView Clicked item value
                //Answer  itemValue    = (Answer) listView.getItemAtPosition(position);

                AnswerTag thetag = (AnswerTag) view.getTag();

                // Show Alert
                //Toast.makeText(getApplicationContext(),
                //        "Position :"+itemPosition+"  ListItem : " + thetag.returnId(), Toast.LENGTH_LONG)
                //        .show();

                //mFloatingActionButton.setEnabled(true);

                Backendless.Persistence.of(Answer.class).findById(thetag.returnId(), new AsyncCallback<Answer>() {
                    @Override
                    public void handleResponse(Answer answer) {

                        String owner = answer.getOwnerId();


                        Log.i("info", "OwnerId = " + owner);

                        Backendless.UserService.findById(owner, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(final BackendlessUser backendlessUsertwo) {



                                Backendless.Persistence.of(Session.class).findById("6E66FA4F-C624-FDD7-FFC6-714FCD3EE100", new AsyncCallback<Session>() {
                                    @Override
                                    public void handleResponse(Session session) {

                                        //session.setRound_one_winner(backendlessUsertwo);
                                        session.setRound("2");
                                        session.setQuestion(null);




                                        Backendless.Persistence.save( session, new AsyncCallback<Session>() {
                                            @Override
                                            public void handleResponse( final Session responseone )
                                            {


                                                String winnerId = backendlessUsertwo.getProperty("deviceId").toString();

                                                ArrayList<String> winner = new ArrayList<String>();

                                                winner.add(winnerId);

                                                android.util.Pair<DeliveryOptions, PublishOptions> pair = SendBroadcastMethods.PrepareBroadcast(winner, "winnertrigger", "null", "null");


                                                Backendless.Messaging.publish("", pair.second, pair.first, new AsyncCallback<MessageStatus>() {
                                                    @Override
                                                    public void handleResponse(MessageStatus response) {

                                                        Log.i("info", "message sent");


                                                        ArrayList<Answer> answers = responseone.getAnswers();

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


                                                        Log.i("info", backendlessFault.toString());


                                                    }
                                                });



                                                Log.i("info", "session update success ");




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








                                /*Log.i("info", "deviceId = " + backendlessUser.getProperty("deviceId").toString());

                                String winnerId = backendlessUser.getProperty("deviceId").toString();

                                ArrayList<String> winner = new ArrayList<String>();

                                winner.add(winnerId);

                                android.util.Pair<DeliveryOptions, PublishOptions> pair = SendBroadcastMethods.PrepareBroadcast(winner, "winnertrigger", "null", "null");


                                Backendless.Messaging.publish("", pair.second, pair.first, new AsyncCallback<MessageStatus>() {
                                    @Override
                                    public void handleResponse(MessageStatus response) {

                                        Log.i("info", "message sent");



                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {


                                        Log.i("info", backendlessFault.toString());


                                    }
                                });*/



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

        });


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

                                Toast.makeText(InitiatorGamingActivity.this, "refresh success",
                                        Toast.LENGTH_LONG).show();

                                swipeRefreshLayout.setRefreshing(false);



                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {

                                Toast.makeText(InitiatorGamingActivity.this, "refresh failure",
                                        Toast.LENGTH_LONG).show();

                                swipeRefreshLayout.setRefreshing(false);



                            }
                        });



                    }
                }
        );





    }

    public static InitiatorGamingActivity  getInstance(){
        return instwo;
    }


    public void UpdateTheInitiatorAnswer(final Answer answer) {


        Log.i("info", "Update answer");

        //Toast.makeText(InitiatorGamingActivity.this, leAnswer,
        //        Toast.LENGTH_LONG).show();

        values.add(answer);
        adapter.notifyDataSetChanged();


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
