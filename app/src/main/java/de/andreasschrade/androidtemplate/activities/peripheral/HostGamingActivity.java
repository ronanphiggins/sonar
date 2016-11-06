package de.andreasschrade.androidtemplate.activities.peripheral;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.services.messaging.MessageStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.andreasschrade.androidtemplate.R;

public class HostGamingActivity extends AppCompatActivity {

    ListView listView;
    Boolean questionAsked = false;

    private static HostGamingActivity ins;

    List<String> values;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        ins = this;


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.theme_primary_light));

        final String[] questions = new String[] {"What is your favourite colour?", "What is your favourite animal?", "What is your favourite season?", "What is your favourite drink?"};


        Button randomQuestion = (Button) findViewById(R.id.randomQuestion);

        randomQuestion.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(!questionAsked) {

                    Log.i("info", "random quesiton button clicked");
                    Random rnd = new Random();
                    int rndIndex = rnd.nextInt(questions.length);
                    final String theQuestion = questions[rndIndex];

                    DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addPushSinglecast("ECPBBCB691102290");

                    PublishOptions publishOptions = new PublishOptions();
                    publishOptions.putHeader("android-ticker-text", "question");
                    publishOptions.putHeader("android-content-title", theQuestion);
                    publishOptions.putHeader("android-content-text", "");

                    Backendless.Messaging.publish("", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                        @Override
                        public void handleResponse(MessageStatus response) {

                            Log.i("info", "message sent");


                            TextView question = (TextView) findViewById(R.id.question);
                            question.setText(theQuestion);

                            //questionAsked = true;


                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {


                            Log.i("info", backendlessFault.toString());


                        }
                    });






                }


            }
        });



        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        values = new ArrayList<String>();

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

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

            }

        });





    }


    public static HostGamingActivity  getInstance(){
        return ins;
    }


    public void updateTheAnswer(final String answer) {
        HostGamingActivity.this.runOnUiThread(new Runnable() {
            public void run() {


                Log.i("info", answer);
                values.add(answer);
                adapter.notifyDataSetChanged();


            }
        });
    }
}
