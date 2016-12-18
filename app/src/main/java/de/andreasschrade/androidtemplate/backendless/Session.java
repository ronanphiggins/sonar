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

    private BackendlessUser round_one_winner;

    private BackendlessUser round_two_winner;


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

    public void setRound_one_winner(BackendlessUser round_one_winner) {this.round_one_winner = round_one_winner; }

    public void setRound_two_winner(BackendlessUser round_two_winner) {this.round_two_winner = round_two_winner; }








//public void setAnswers(Answer[] answers) { this.answers = answers; }

    //public String getRound_one_question() { return round_one_question; }

    //public void setRound_one_question(String round_one_question) {this.round_one_question = round_one_question; }


}

