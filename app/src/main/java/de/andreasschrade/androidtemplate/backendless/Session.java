package de.andreasschrade.androidtemplate.backendless;

import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.Objects;


public class Session {



    private String objectId;
    private String round;
    private BackendlessUser[] players;

    private String question;

    private ArrayList<Answer> answers;

    private ArrayList<BackendlessUser> roundonewinner;

    private ArrayList<BackendlessUser> roundtwowinner;

    private ArrayList<BackendlessUser> roundthreewinner;


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

    //public Answer[] getAnswers() { return answers; }

    //public void setAnswers(Answer[] answers) { this.answers = answers; }


    public ArrayList<Answer> getAnswers() { return answers; }


    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }


    public String getQuestion() { return  question; }


    public void setQuestion(String question) {this.question = question; }

    public ArrayList<BackendlessUser> getRoundonewinner() { return roundonewinner; }

    public void setRoundonewinner(ArrayList<BackendlessUser> roundonewinner) {this.roundonewinner = roundonewinner; }

    public ArrayList<BackendlessUser> getRoundtwowinner() { return roundtwowinner; }

    public void setRoundtwowinner(ArrayList<BackendlessUser> roundtwowinner) {this.roundtwowinner = roundtwowinner; }

    public ArrayList<BackendlessUser> getRoundthreewinner() { return roundthreewinner; }

    public void setRoundthreewinner(ArrayList<BackendlessUser> roundthreewinner) {this.roundthreewinner = roundthreewinner; }
}

