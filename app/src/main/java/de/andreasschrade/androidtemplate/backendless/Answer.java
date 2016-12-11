package de.andreasschrade.androidtemplate.backendless;

import android.util.Log;

import com.backendless.BackendlessUser;



public class Answer {

    private String answer;
    private String objectId;


    public Answer()
    {
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }



}
