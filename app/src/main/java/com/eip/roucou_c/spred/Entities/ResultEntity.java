package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roucou_c on 19/12/2016.
 */

public class ResultEntity implements Serializable {
    @SerializedName("result")
    private boolean _result;

    public boolean get_result() {
        return _result;
    }
}
