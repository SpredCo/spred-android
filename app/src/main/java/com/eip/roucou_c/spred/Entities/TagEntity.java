package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roucou_c on 11/12/2016.
 */
public class TagEntity implements Serializable{

    @SerializedName("id")
    private String _id = null;

    @SerializedName("name")
    private String _name = null;

    @SerializedName("description")
    private String _description = null;

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
}
