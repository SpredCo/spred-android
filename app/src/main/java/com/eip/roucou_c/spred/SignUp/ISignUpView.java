package com.eip.roucou_c.spred.SignUp;

import com.eip.roucou_c.spred.ISignInSignUpView;

/**
 * Created by cleme_000 on 27/02/2016.
 */
public interface ISignUpView extends ISignInSignUpView{

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPassword();

    String getConfirmPassword();

    String getPseudo();

    void setErrorEmail(int resId);

    void setErrorPassword(int resId);

    void setErrorLastName(int resId);

    void setErrorFirstName(int resId);

    void setErrorConfirmPassword(int resId);

    void setErrorPseudo(int resId);

    void changeStep(String step);

    void signUpSuccess();
}
