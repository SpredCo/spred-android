package com.eip.roucou_c.spred.Entities;

/**
 * Created by roucou_c on 09/09/2016.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UserEntity implements Serializable{
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
    @SerializedName("created_at")
    protected String _created_at = null;
    @SerializedName("updated_at")
    protected String _updated_at = null;

    @SerializedName("pseudo")
    private String _pseudo = null;

    @SerializedName("following") // todo gere le probleme de serialzage du foolowing
    private List<Object> _following = new ArrayList<>();

    public String get_pseudo() {
        return _pseudo;
    }

    public void set_pseudo(String _pseudo) {
        this._pseudo = _pseudo;
    }

    public List<Object> get_following() {
        return _following;
    }

    public List<UserEntity> get_followingUserEntity() {
        List<UserEntity> userEntities = new ArrayList<>();

        for (Object user : _following) {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            UserEntity userEntity = gson.fromJson(json, UserEntity.class);
            userEntities.add(userEntity);
        }
        return userEntities;
    }

    public static void main(Object user) {
    }

    public void set_following(List<Object> _following) {
        this._following = _following;
    }

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

    public String get_created_at() {
        return _created_at;
    }

    public void set_created_at(String _created_at) {
        this._created_at = _created_at;
    }

    public String get_updated_at() {
        return _updated_at;
    }

    public void set_updated_at(String _updated_at) {
        this._updated_at = _updated_at;
    }
}
