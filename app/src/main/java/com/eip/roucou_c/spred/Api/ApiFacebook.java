package com.eip.roucou_c.spred.Api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eip.roucou_c.spred.ISignInSignUpView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * Created by roucou_c on 20/06/2016.
 */
public class ApiFacebook {

    private final Context _context;
    private final ISignInSignUpView _view;

    private CallbackManager _callbackManager;

    public ApiFacebook(Context context, ISignInSignUpView view) {
        _context = context;
        _view = view;

        this.init();
    }

    public void init() {
        FacebookSdk.sdkInitialize(_context);
        AppEventsLogger.activateApp(_context);
    }

    public void launch() {
        _callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = _view.getSignInButtonFacebook();

        if (loginButton != null) {
            loginButton.setReadPermissions("email");

            loginButton.registerCallback(_callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {
                    String access_token_facebook = loginResult.getAccessToken().getToken();

                    Log.d("token facebook", access_token_facebook);
                    _view.onFacebookClicked(access_token_facebook);
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    Log.d("error", error.toString());
                }
            });
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        _callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
