package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roucou_c on 16/12/2016.
 */
public class FollowerEntity implements Serializable{

    @SerializedName("user")
    private UserEntity _user = null;

    @SerializedName("following")
    private String _following = null;

    public UserEntity get_user() {
        return _user;
    }

    public void set_user(UserEntity _user) {
        this._user = _user;
    }

    public String get_following() {
        return _following;
    }

    public void set_following(String _following) {
        this._following = _following;
    }
}
