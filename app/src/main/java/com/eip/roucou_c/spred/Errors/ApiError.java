package com.eip.roucou_c.spred.Errors;

import com.eip.roucou_c.spred.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by roucou_c on 14/09/2016.
 */
public class ApiError {

    private int _httpCode = 0;
    private int _code = 0;
    private int _sub_code = 0;
    private String _message = null;
    private String _scope;

    private String _target;
    private int _target_message;


    public ApiError(ResponseBody responseBody, int httpCode, String scope) {
        _scope = scope;
        _httpCode = httpCode;

        try {
            JSONObject obj = new JSONObject(responseBody.string());

            _code = obj.has("code") ? obj.getInt("code") : 0;
            _sub_code = obj.has("sub_code") ? obj.getInt("sub_code") : 0;
            _message = obj.has("message") ? obj.getString("message") : null;

            this.searchMessage();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void searchMessage() {
        switch (_httpCode) {
            case 400:
                searchMessageBy400();
                break;
            case 401:
                break;
            case 402:
                break;
            case 403:
                searchMessageBy403();
                break;
            case 404:
                break;
        }
    }

    private void searchMessageBy403() {
        switch (_code) {
            case 1:
                break;
            case 2:
                searchMessageBy400Code2();
                break;
            default:
                searchMessageBy400WithoutCode();
                break;
        }
    }

    private void searchMessageBy400WithoutCode() {
        switch (_scope) {
            case "signIn":
                _target = "fail";
                _target_message = R.string.wrong_email_or_password;
                break;
        }
    }

    private void searchMessageBy400Code2() {
        switch (_scope) {
            case "signUp":
                switch (_sub_code) {
                    case 1:
                        _target = "email";
                        _target_message = R.string.email_address_already_use;
                        break;
                    case 2:
                        _target = "pseudo";
                        _target_message = R.string.pseudo_already_use;
                        break;
                }
                break;
        }
    }

    private void searchMessageBy400() {
        switch (_code) {
            case 1:
                searchMessageBy400Code1();
                break;
            case 2:
                break;
        }

    }

    private void searchMessageBy400Code1() {
        switch (_scope) {
            case "signUp":
                switch (_sub_code) {
                    case 1:
                        break;
                    case 2:
                        break;
                }
                break;
        }
    }


    public String get_target() {
        return _target;
    }

    public int get_target_message() {
        return _target_message;
    }

    public int get_httpCode() {
        return _httpCode;
    }
}
