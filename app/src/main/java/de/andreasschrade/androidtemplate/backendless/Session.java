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







//public void setAnswers(Answer[] answers) { this.answers = answers; }

    //public String getRound_one_question() { return round_one_question; }

    //public void setRound_one_question(String round_one_question) {this.round_one_question = round_one_question; }


}

