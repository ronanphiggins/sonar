package de.andreasschrade.androidtemplate.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import de.andreasschrade.androidtemplate.R;

/**
 * Created by ronan.p.higgins on 26/09/2016.
 */
public class WindowUtil {

    public static void changeWindowBarColour(Activity activity) {

        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.theme_primary_dark));

    }
}
