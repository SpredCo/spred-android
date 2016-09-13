package com.eip.roucou_c.spred.Entities;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cleme_000 on 29/02/2016.
 */
public class TokenEntity {

    @SerializedName("access_token")
    private String _access_token = null;
    @SerializedName("refresh_token")
    private String _refresh_token = null;
    @SerializedName("expires_in")
    private String _expire_in = null;
    @SerializedName("token_type")
    private String _token_type = null;

    private transient long _id = 0;
    private transient String _expire_access_token = null;
    private transient String _expire_refresh_token = null;

    public TokenEntity(Cursor c) {
        this._id = c.getLong(0);
        this. _access_token = c.getString(1);
        this._refresh_token = c.getString(2);
        this._expire_in = c.getString(3);
        this._token_type = c.getString(4);
        this._expire_access_token = c.getString(5);
        this._expire_refresh_token = c.getString(6);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_access_token() {
        return _access_token;
    }

    public void set_access_token(String _access_token) {
        this._access_token = _access_token;
    }

    public String get_refresh_token() {
        return _refresh_token;
    }

    public void set_refresh_token(String _refresh_token) {
        this._refresh_token = _refresh_token;
    }

    public String get_expire_in() {
        return _expire_in;
    }

    public void set_expire_in(String _expire_in) {
        this._expire_in = _expire_in;
    }

    public String get_token_type() {
        return _token_type;
    }

    public void set_token_type(String _token_type) {
        this._token_type = _token_type;
    }

    public String get_expire_access_token() {
        return _expire_access_token;
    }

    public void set_expire_access_token(String _expire_access_token) {
        this._expire_access_token = _expire_access_token;
    }

    public String get_expire_refresh_token() {
        return _expire_refresh_token;
    }

    public void set_expire_refresh_token(String _expire_refresh_token) {
        this._expire_refresh_token = _expire_refresh_token;
    }

    @Override
    public String toString() {
        String string = "access_token = " + this.get_access_token();
        string += " refresh_token = " + this.get_refresh_token();
        string += " expire_in = " + this.get_expire_in();
        string += " token_type = " + this.get_token_type();
        return string;
    }
}
