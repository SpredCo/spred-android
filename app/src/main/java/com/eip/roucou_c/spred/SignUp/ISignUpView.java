package com.eip.roucou_c.spred.SignUp;

import com.eip.roucou_c.spred.ISignInSignUpView;
import com.rengwuxian.materialedittext.MaterialEditText;

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

    void changeStep(String step);

    MaterialEditText get_signup_step1_password();

    MaterialEditText get_signup_step1_email();
}
