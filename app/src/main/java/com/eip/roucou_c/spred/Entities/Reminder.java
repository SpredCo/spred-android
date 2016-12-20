package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roucou_c on 19/12/2016.
 */

public class Reminder implements Serializable {
    @SerializedName("id")
    private String _id = null;

    @SerializedName("result")
    private boolean result;

    public String get_id() {
        return _id;
    }

    public boolean isResult() {
        return result;
    }
}
