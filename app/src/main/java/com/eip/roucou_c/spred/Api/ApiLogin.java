package com.eip.roucou_c.spred.Api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eip.roucou_c.spred.ISignInSignUpView;

import java.io.IOException;


/**
 * Created by roucou_c on 17/06/2016.
 */
public class ApiLogin {

    static final int REQUEST_AUTHORIZATION = 42;
    static final int REQUEST_CODE_GOOGLE = 1000;
    static final int REQUEST_CODE_FACEBOOK = 64206;

    private final Context _context;
    private final ISignInSignUpView _view;


    ApiFacebook _apiFacebook;
    ApiGoogle _apiGoogle;

    public ApiLogin(Context context, ISignInSignUpView view) {
        this._context = context;
        this._view = view;

        _apiFacebook = new ApiFacebook(_context, view);
        _apiGoogle = new ApiGoogle(_context, view);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("result", String.valueOf(requestCode));
        if (requestCode == REQUEST_CODE_GOOGLE) {
            _apiGoogle.onActivityResult(requestCode, resultCode, data);
        }
        else if (requestCode == REQUEST_AUTHORIZATION) {
            try {
                _apiGoogle.fetchToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_CODE_FACEBOOK){
            _apiFacebook.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void launch() {
        this._apiFacebook.launch();
        this._apiGoogle.launch();
    }

    public void login(String api) {
        switch (api) {
            case "google" :
                _apiGoogle.getToken();
                break;
        }
    }
}
