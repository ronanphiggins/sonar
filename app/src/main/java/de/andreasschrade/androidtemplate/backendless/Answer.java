package de.andreasschrade.androidtemplate.backendless;

import android.util.Log;

import com.backendless.BackendlessUser;



public class Answer {

    private String answer;
    private String objectId;
    private String ownerId;


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

    public void setObjectId (String objectId) { this.objectId = objectId; }

    public String getOwnerId() { return ownerId; }



}
