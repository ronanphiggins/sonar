package de.andreasschrade.androidtemplate.activities.peripheral;

import android.opengl.EGLSurface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.services.messaging.MessageStatus;

import de.andreasschrade.androidtemplate.R;

public class PlayerGamingActivity extends AppCompatActivity {

    private static PlayerGamingActivity ins;
    EditText answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playergaming);

        ins = this;

        answer = (EditText) findViewById(R.id.answer);
        answer.setEnabled(false);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Button submitAnswer = (Button) findViewById(R.id.submitAnswer);

        submitAnswer.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                DeliveryOptions deliveryOptions = new DeliveryOptions();
                deliveryOptions.addPushSinglecast("04157df43901b531");

                PublishOptions publishOptions = new PublishOptions();
                publishOptions.putHeader("android-ticker-text", "answer");
                publishOptions.putHeader("android-content-title", answer.getText().toString());
                publishOptions.putHeader("android-content-text", "");

                Backendless.Messaging.publish("", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
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
        });


    }


    public static PlayerGamingActivity  getInstance(){
        return ins;
    }


    public void updateTheQuestion(final String question) {
        PlayerGamingActivity.this.runOnUiThread(new Runnable() {
            public void run() {

                Log.i("info", question);
                TextView theQuestion = (TextView) findViewById(R.id.question);
                theQuestion.setText(question);
                answer.setEnabled(true);


            }
        });
    }

    public void onResume() {
        super.onResume();


        Log.i("info", "resumed");

        DeliveryOptions deliveryOptions = new DeliveryOptions();
        deliveryOptions.addPushSinglecast("04157df43901b531");

        PublishOptions publishOptions = new PublishOptions();
        publishOptions.putHeader("android-ticker-text", "resumed");
        publishOptions.putHeader("android-content-title", "");
        publishOptions.putHeader("android-content-text", "");

        Backendless.Messaging.publish("", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
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

    public void onPause() {
        super.onResume();


        Log.i("info", "paused");

        DeliveryOptions deliveryOptions = new DeliveryOptions();
        deliveryOptions.addPushSinglecast("04157df43901b531");

        PublishOptions publishOptions = new PublishOptions();
        publishOptions.putHeader("android-ticker-text", "paused");
        publishOptions.putHeader("android-content-title", "");
        publishOptions.putHeader("android-content-text", "");

        Backendless.Messaging.publish("", publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
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

}
