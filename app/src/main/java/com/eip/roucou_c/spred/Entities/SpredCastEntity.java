package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by roucou_c on 07/11/2016.
 */
public class SpredCastEntity {

    @SerializedName("id")
    protected String _id = null;

    @SerializedName("name")
    protected String _name = null;

    @SerializedName("description")
    protected String _description = null;

    @SerializedName("tags")
    protected List<String> _tags = null;

    @SerializedName("date")
    protected String _date = null;

    @SerializedName("is_public")
    protected boolean _public;

    @SerializedName("members")
    protected List<String> _members;

    @SerializedName("duration")
    protected int _duration;

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

    public List<String> get_tags() {
        return _tags;
    }

    public void set_tags(List<String> _tags) {
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
}