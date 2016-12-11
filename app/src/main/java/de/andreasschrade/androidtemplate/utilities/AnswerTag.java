package de.andreasschrade.androidtemplate.utilities;


public class AnswerTag {



    private String userId;


    public AnswerTag()
    {
        userId=null;

    }

    public AnswerTag(String id)
    {
        userId=id;

    }

    public String returnId() {

        return userId;
    }
}
