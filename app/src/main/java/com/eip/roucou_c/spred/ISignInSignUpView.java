package com.eip.roucou_c.spred;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.facebook.login.widget.LoginButton;

/**
 * Created by cleme_000 on 27/02/2016.
 */
public interface ISignInSignUpView {

    LoginButton getSignInButtonFacebook();

    void onFacebookClicked(String access_token_facebook);

    void onGoogleClicked(String token);

    void startActivityForResult(Intent intent, int requestCodeGoogle);

    AppCompatActivity getActivity();

    void setErrorPseudo(int resId);

    String getPseudo();

    void changeStep(String step);

    String get_token_google();

    String get_token_facebook();

    void signInSuccess();
}
