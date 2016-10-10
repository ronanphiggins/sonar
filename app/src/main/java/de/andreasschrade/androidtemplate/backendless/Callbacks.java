package de.andreasschrade.androidtemplate.backendless;

import android.util.Log;

import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by ronan.p.higgins on 05/10/2016.
 */
public class Callbacks {

    public static AsyncCallback<BackendlessUser> callback = new AsyncCallback<BackendlessUser>() {
        @Override
        public void handleResponse(BackendlessUser response) {

            Log.i("info", "=+=callback worked = " + response.getObjectId());

            Object sex = response.getProperty("sex");

            int gender = (int) sex;

            if (gender == 1) {

                Log.i("info", "male");

            } else if (gender == 2) {

                Log.i("info", "female");
            }
        }


        @Override
        public void handleFault(BackendlessFault fault) {
        }
    };
}
