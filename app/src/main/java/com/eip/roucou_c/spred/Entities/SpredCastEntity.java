package com.eip.roucou_c.spred.Entities;

import android.widget.LinearLayout;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by roucou_c on 07/11/2016.
 */
public class SpredCastEntity implements Serializable {

    @SerializedName("id")
    private String _id = null;

    @SerializedName("name")
    private String _name = null;

    @SerializedName("description")
    private String _description = null;

    @SerializedName("tags")
    private List<TagEntity> _tags = null;

    @SerializedName("date")
    private String _date = null;

    @SerializedName("is_public")
    private boolean _public;

    @SerializedName("members")
    private List<String> _members;

    @SerializedName("duration")
    private int _duration;

    @SerializedName("state")
    private int _state;

    @SerializedName("creator")
    private UserEntity _creator;

    @SerializedName("user_capacity")
    private int _user_capacity;

    @SerializedName("user_count")
    private int _user_count;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public List<TagEntity> get_tags() {
        return _tags;
    }

    public void set_tags(List<TagEntity> _tags) {
        this._tags = _tags;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public boolean is_public() {
        return _public;
    }

    public void set_public(boolean _public) {
        this._public = _public;
    }

    public List<String> get_members() {
        return _members;
    }

    public void set_members(List<String> _members) {
        this._members = _members;
    }

    public int get_duration() {
        return _duration;
    }

    public void set_duration(int _duration) {
        this._duration = _duration;
    }

    public int get_state() {
        return _state;
    }

    public void set_state(int _state) {
        this._state = _state;
    }

    public UserEntity get_creator() {
        return _creator;
    }

    public void set_creator(UserEntity _creator) {
        this._creator = _creator;
    }

    public int get_user_capacity() {
        return _user_capacity;
    }

    public void set_user_capacity(int _user_capacity) {
        this._user_capacity = _user_capacity;
    }

    public int get_user_count() {
        return _user_count;
    }
}

