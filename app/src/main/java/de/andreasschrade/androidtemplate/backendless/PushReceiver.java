package de.andreasschrade.androidtemplate.backendless;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.backendless.push.BackendlessBroadcastReceiver;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.peripheral.HomeActivity;

/**
 * Created by ronanpiercehiggins on 20/09/2016.
 */

public class PushReceiver extends BackendlessBroadcastReceiver {




    private static final int NOTIFICATION_ID = 1;





    @Override
    public boolean onMessage(Context context, Intent intent)

    {





            String message = intent.getStringExtra("message");

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


            Intent notificationIntent = new Intent(context, HomeActivity.class);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);


            NotificationCompat.Builder notification = new NotificationCompat.Builder(context)

                    .setContentTitle("Hello").setContentText(message).setSmallIcon(R.drawable.home).setDefaults(
            Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS
    );

            notification.setContentIntent(contentIntent);


            notification.setAutoCancel(true);

            mNotificationManager.notify(NOTIFICATION_ID, notification.build());


        return false;



    }

}
