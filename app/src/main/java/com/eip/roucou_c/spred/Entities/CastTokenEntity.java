package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roucou_c on 20/12/2016.
 */
public class CastTokenEntity implements Serializable{
    @SerializedName("cast_token")
    private String _token;

    @SerializedName("spredcast")
    private String _castId;

    @SerializedName("presenter")
    private boolean _presenter;

    @SerializedName("pseudo")
    private String _pseudo;

    public String get_token() {
        return _token;
    }

    public String get_castId() {
        return _castId;
    }

    public boolean is_presenter() {
        return _presenter;
    }

    public String get_pseudo() {
        return _pseudo;
    }
}
