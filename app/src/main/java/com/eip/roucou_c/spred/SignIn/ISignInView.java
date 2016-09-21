package com.eip.roucou_c.spred.SignIn;

import com.eip.roucou_c.spred.ISignInSignUpView;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by cleme_000 on 23/02/2016.
 */
public interface ISignInView extends ISignInSignUpView {

    String getEmail();

    String getPassword();

    void setErrorEmail(int resId);

    void setErrorPassword(int resId);

    void signinSuccess();

    LoginButton getSignInButtonFacebook();

    MaterialEditText get_signin_step1_password();

    void setError(int resId);

    void changeStep(String step);

    String get_token_google();

    String get_token_facebook();
}
