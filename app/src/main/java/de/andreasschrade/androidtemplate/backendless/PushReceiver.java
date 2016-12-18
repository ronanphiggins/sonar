package de.andreasschrade.androidtemplate.backendless;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.backendless.push.BackendlessBroadcastReceiver;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.peripheral.BidActivity;
import de.andreasschrade.androidtemplate.activities.peripheral.GuestGamingActivity;
import de.andreasschrade.androidtemplate.activities.peripheral.HostGamingActivity;
import de.andreasschrade.androidtemplate.activities.peripheral.InitiatorGamingActivity;
import de.andreasschrade.androidtemplate.activities.peripheral.PlayerGamingActivity;

/**
 * Created by ronanpiercehiggins on 20/09/2016.
 */

public class PushReceiver extends BackendlessBroadcastReceiver {




    private static final int NOTIFICATION_ID = 1;





    @Override
    public boolean onMessage(Context context, Intent intent)

    {

            String trigger = intent.getStringExtra("android-ticker-text");


            if (trigger.equalsIgnoreCase("trigger")) {

                String message = intent.getStringExtra("message");

                String title = intent.getStringExtra("android-content-title");

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent notificationIntent;

                if (trigger.equalsIgnoreCase("game")) {


                    notificationIntent = new Intent(context, HostGamingActivity.class);


                } else {


                    notificationIntent = new Intent(context, BidActivity.class);


                }



                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);


                NotificationCompat.Builder notification = new NotificationCompat.Builder(context)

                        .setContentTitle(title).setContentText(message).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon)).setSmallIcon(R.drawable.plus).setDefaults(
                                Notification.DEFAULT_SOUND
                                        | Notification.DEFAULT_VIBRATE
                                        | Notification.DEFAULT_LIGHTS
                        );

                notification.setContentIntent(contentIntent);


                notification.setAutoCancel(true);

                mNotificationManager.notify(NOTIFICATION_ID, notification.build());





            } else if (trigger.equalsIgnoreCase("question")){


                try {

                    String question = intent.getStringExtra("android-content-title");

                    PlayerGamingActivity .getInstance().updateTheQuestion(question);

                    Log.i("info", "try");

                } catch (Exception e) {


                    Log.i("info", "catch" + e);


                }


            } else if (trigger.equalsIgnoreCase("resumed")) {


                try {

                    String answer = intent.getStringExtra("android-content-title");

                    HostGamingActivity .getInstance().playerStatusResume();

                    Log.i("info", "try");

                } catch (Exception e) {


                    Log.i("info", "catch" + e);


                }




            } else if (trigger.equalsIgnoreCase("paused")) {


                try {

                    String answer = intent.getStringExtra("android-content-title");

                    HostGamingActivity .getInstance().playerStatusPaused();

                    Log.i("info", "try");

                } catch (Exception e) {


                    Log.i("info", "catch" + e);


                }




            } else if (trigger.equalsIgnoreCase("answer")) {


                try {

                    String question = intent.getStringExtra("android-content-title");

                    HostGamingActivity.getInstance().updateTheAnswer(question);

                    Log.i("info", "try");

                } catch (Exception e) {


                    Log.i("info", "catch" + e);


                }


            } else if (trigger.equalsIgnoreCase("questiontrigger")) {


                try {

                    String question = intent.getStringExtra("android-content-title");

                    GuestGamingActivity.getInstance().UpdateTheQuestion(question);

                    Log.i("info", "try");

                } catch (Exception e) {


                    Log.i("info", "catch" + e);


                }

            } else if (trigger.equalsIgnoreCase("answertrigger")) {


                String theanswer = intent.getStringExtra("android-content-title");
                String theobjectId = intent.getStringExtra("android-content-text");

                Answer answerobject = new Answer();

                answerobject.setAnswer(theanswer);
                answerobject.setObjectId(theobjectId);




                try {




                    InitiatorGamingActivity.getInstance().UpdateTheInitiatorAnswer(answerobject);




                    Log.i("info", "try");

                } catch (Exception e) {


                    Log.i("info", "catch" + e);


                } try {


                    GuestGamingActivity.getInstance().UpdateTheAnswer(answerobject);



                } catch (Exception e) {

                }

            } else if (trigger.equalsIgnoreCase("winnertrigger")) {


                try {

                    GuestGamingActivity.getInstance().DeclareWinner();

                    Log.i("info", "try");

                } catch (Exception e) {


                    Log.i("info", "catch" + e);


                }

            }









        return false;

    }

}
