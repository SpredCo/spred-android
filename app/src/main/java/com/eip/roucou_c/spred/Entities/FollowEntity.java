package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by roucou_c on 16/12/2016.
 */
public class FollowEntity implements Serializable {

    @SerializedName("user")
    private String _user = null;

    @SerializedName("following")
    private UserEntity _following = null;

    public String get_user() {
        return _user;
    }

    public void set_user(String _user) {
        this._user = _user;
    }

    public UserEntity get_following() {
        return _following;
    }

    public void set_following(UserEntity _following) {
        this._following = _following;
    }
}
