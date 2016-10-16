package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by roucou_c on 29/09/2016.
 */
public class MessageEntity {

    @SerializedName("conversation")
    private String _conversation;

    @SerializedName("from")
    private String _from;

    @SerializedName("content")
    private String _content;

    @SerializedName("read")
    private String _read;

    @SerializedName("created_at")
    private String _created_at;

    public String get_conversation() {
        return _conversation;
    }

    public void set_conversation(String _conversation) {
        this._conversation = _conversation;
    }

    public String get_from() {
        return _from;
    }

    public void set_from(String _from) {
        this._from = _from;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public String get_read() {
        return _read;
    }

    public void set_read(String _read) {
        this._read = _read;
    }

    public String get_created_at() {
        return _created_at;
    }

    public void set_created_at(String _created_at) {
        this._created_at = _created_at;
    }
}
