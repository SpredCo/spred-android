package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roucou_c on 20/12/2016.
 */
public class ReminderEntity implements Serializable{
    @SerializedName("cast")
    private String _cast_id;

    @SerializedName("user")
    private UserEntity _user;

    public String get_cast_id() {
        return _cast_id;
    }

    public UserEntity get_user() {
        return _user;
    }
}
