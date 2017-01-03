package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roucou_c on 20/12/2016.
 */

public class ChatEntity implements Serializable {
    @SerializedName("id")
    private int _id;

    @SerializedName("text")
    private String _text;

    @SerializedName("sender")
    private String _sender;

    @SerializedName("date")
    private String _date;

    @SerializedName("user_picture")
    private String _user_picture;

    public int get_id() {
        return _id;
    }

    public String get_text() {
        return _text;
    }

    public String get_sender() {
        return _sender;
    }

    public String get_date() {
        return _date;
    }

    public String get_user_picture() {
        return _user_picture;
    }
}
