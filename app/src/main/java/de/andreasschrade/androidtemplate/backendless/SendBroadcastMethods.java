package de.andreasschrade.androidtemplate.backendless;


import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.services.messaging.MessageStatus;

import java.util.ArrayList;
import java.util.Random;

import de.andreasschrade.androidtemplate.R;

public class SendBroadcastMethods {


    public static Pair<DeliveryOptions, PublishOptions> PrepareBroadcast(ArrayList<String> deviceId, String trigger, String question) {


        DeliveryOptions deliveryOptions = new DeliveryOptions();

        for (String ID : deviceId) {

            deliveryOptions.addPushSinglecast(ID);

        }

        PublishOptions publishOptions = new PublishOptions();
        publishOptions.putHeader("android-ticker-text", trigger);
        publishOptions.putHeader("android-content-title", question);
        publishOptions.putHeader("android-content-text", "");

        return Pair.create(deliveryOptions, publishOptions);
    }


}
