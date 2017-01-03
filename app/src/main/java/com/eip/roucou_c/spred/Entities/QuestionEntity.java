package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roucou_c on 29/12/2016.
 */
public class QuestionEntity implements Serializable{

    @SerializedName("id")
    private int _id;

    @SerializedName("sender")
    private String _sender;

    @SerializedName("text")
    private String _text;

    @SerializedName("nbVote")
    private int _nbVote;

    @SerializedName("user_picture")
    private String _user_picture;

    public int get_id() {
        return _id;
    }

    public String get_sender() {
        return _sender;
    }

    public String get_text() {
        return _text;
    }

    public int get_nbVote() {
        return _nbVote;
    }

    public String get_user_picture() {
        return _user_picture;
    }
}
