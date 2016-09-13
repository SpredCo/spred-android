package com.eip.roucou_c.spred.Entities;

/**
 * Created by roucou_c on 09/09/2016.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserEntity {

    @SerializedName("id")
    protected String _id = null;
    @SerializedName("email")
    protected String _email = null;
    @SerializedName("first_name")
    protected String _first_name = null;
    @SerializedName("last_name")
    protected String _last_name = null;
    @SerializedName("picture_url")
    protected String _picture_url = null;
    @SerializedName("following")
    protected List<UserEntity> _following = null;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_first_name() {
        return _first_name;
    }

    public void set_first_name(String _first_name) {
        this._first_name = _first_name;
    }

    public String get_last_name() {
        return _last_name;
    }

    public void set_last_name(String _last_name) {
        this._last_name = _last_name;
    }

    public String get_picture_url() {
        return _picture_url;
    }

    public void set_picture_url(String _picture_url) {
        this._picture_url = _picture_url;
    }

    public List<UserEntity> get_following() {
        return _following;
    }

    public void set_following(List<UserEntity> _following) {
        this._following = _following;
    }
}
