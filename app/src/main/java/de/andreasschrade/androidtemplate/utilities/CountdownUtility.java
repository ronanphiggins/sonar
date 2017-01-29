package de.andreasschrade.androidtemplate.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import de.andreasschrade.androidtemplate.R;

/**
 * Created by ronanpiercehiggins on 29/01/2017.
 */

public class CountdownUtility {




    public static Dialog CountdownHandler(Context c, CountDownAnimation.CountDownListener listener) {



        Dialog dialog;


        dialog = new Dialog(c);
        dialog.setContentView(R.layout.countdown_dialog);

        TextView dialogButton = (TextView) dialog.findViewById(R.id.countdowntextview);


        CountDownAnimation countDownAnimation = new CountDownAnimation(dialogButton, 5);
        countDownAnimation.setCountDownListener(listener);
        Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        countDownAnimation.setStartCount(5);
        countDownAnimation.setAnimation(animationSet);



        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.9f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);

        countDownAnimation.start();


        return dialog;



    }











}
