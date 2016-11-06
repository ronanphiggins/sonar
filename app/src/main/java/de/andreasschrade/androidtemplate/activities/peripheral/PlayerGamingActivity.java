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


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.theme_primary_light));

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
}
