package de.andreasschrade.androidtemplate.backendless;

import com.backendless.BackendlessUser;

import java.util.Objects;

/**
 * Created by ronanpiercehiggins on 13/11/2016.
 */

public class Session {



    private String objectId;
    private String round;
    private BackendlessUser[] players;
    private Answer[] round_one_answers;


    public Session()
    {
    }


    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId( String objectId )
    {
        this.objectId = objectId;
    }

    public String getRound()
    {
        return round;
    }

    public void setRound( String round )
    {
        this.round = round;
    }

    public BackendlessUser[] getPlayers() {return players; }

    public Answer[] getAnswers() { return round_one_answers; }






}

