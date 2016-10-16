package com.eip.roucou_c.spred.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by roucou_c on 29/09/2016.
 */
public class ConversationEntity {

    @SerializedName("id")
    private String _id;

    @SerializedName("object")
    private String _object;

    @SerializedName("members")
    private List<UserEntity> _members;

    @SerializedName("can_answer")
    private String _can_answer;

    @SerializedName("last_msg")
    private String _last_msg;

    @SerializedName("created_at")
    private String _created_at;

    @SerializedName("msg")
    private List<MessageEntity> _msg;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_object() {
        return _object;
    }

    public void set_object(String _object) {
        this._object = _object;
    }

    public List<UserEntity> get_members() {
        return _members;
    }

    public void set_members(List<UserEntity> _members) {
        this._members = _members;
    }

    public String get_can_answer() {
        return _can_answer;
    }

    public void set_can_answer(String _can_answer) {
        this._can_answer = _can_answer;
    }

    public String get_last_msg() {
        return _last_msg;
    }

    public void set_last_msg(String _last_msg) {
        this._last_msg = _last_msg;
    }

    public String get_created_at() {
        return _created_at;
    }

    public void set_created_at(String _created_at) {
        this._created_at = _created_at;
    }

    public List<MessageEntity> get_msg() {
        return _msg;
    }

    public void set_msg(List<MessageEntity> _msg) {
        this._msg = _msg;
    }
}
