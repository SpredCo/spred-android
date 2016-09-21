package com.eip.roucou_c.spred.SignUp;

import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by cleme_000 on 27/02/2016.
 */
public class SignUpPresenter {

    private ISignUpView _view;
    protected SignUpService _signUpService;

    public SignUpPresenter(ISignUpView view, Manager manager) {
        this._view = view;
        this._signUpService = new SignUpService(view, manager);
    }

    public void onSignUpClicked() {
//        boolean submit = true;
//        String firstName = this._view.getFirstName();
//        String lastName = this._view.getLastName();
//        String email = this._view.getEmail();
//        MyString myStringEmail = new MyString(email);
//        String password = this._view.getPassword();
//        String confirmPassword = this._view.getConfirmPassword();
//
//        if (firstName.isEmpty()) {
//            this._view.setErrorFirstName(R.string.signup_firstnameEmpty);
//            submit = false;
//        }
//        if (lastName.isEmpty()) {
//            this._view.setErrorLastName(R.string.signup_lastnameEmpty);
//            submit = false;
//        }
//        if (email.isEmpty()) {
//            this._view.setErrorEmail(R.string.signup_emailEmpty);
//            submit = false;
//        }
//        else if (!myStringEmail.isEmailValid()) {
//            this._view.setErrorEmail(R.string.signup_emailNotValid);
//            submit = false;
//        }
//        if (password.isEmpty()) {
//            this._view.setErrorPassword(R.string.signup_passwordEmpty);
//            submit = false;
//        }
//        if (confirmPassword.isEmpty()) {
//            this._view.setErrorConfirmPassword(R.string.signup_confirmPasswordEmpty);
//            submit = false;
//        }
//        else if (!password.equals(confirmPassword)) {
//            this._view.setErrorConfirmPassword(R.string.signup_confirmPasswordNotMatch);
//            submit = false;
//        }
//
//        if (submit) {
//            this._view.setProcessLoadingButton(1);
//
//            HashMap<String, String> userParams = new HashMap<>();
//
//            userParams.put("first_name", firstName);
//            userParams.put("last_name", lastName);
//            userParams.put("password", password);
//            userParams.put("email", email);
//
//            this._signUpService.createUser(userParams);
//        }
    }

    public void step1() {
        String firstName = this._view.getFirstName();
        String lastName = this._view.getLastName();
        String email = this._view.getEmail();
        String password = this._view.getPassword();

        boolean isError = false;

        if (firstName.isEmpty()) {
            _view.setErrorFirstName(R.string.empty_input);
            isError = true;
        }
        if (lastName.isEmpty()) {
            _view.setErrorLastName(R.string.empty_input);
            isError = true;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (email.isEmpty()) {
                _view.setErrorEmail(R.string.empty_input);
            }
            else {
                _view.setErrorEmail(R.string.invalid_email);
            }
            isError = true;
        }
        if (password.isEmpty()) {
            _view.setErrorPassword(R.string.empty_input);
            isError = true;
        }

        MaterialEditText materialEditTextPassword = _view.get_signup_step1_password();
        if (!materialEditTextPassword.isCharactersCountValid()) {
            _view.setErrorPassword(R.string.wrong_length_password);
            isError = true;
        }

        MaterialEditText materialEditTextEmail = _view.get_signup_step1_email();

        if (materialEditTextEmail.getError() == null && !isError) {
            _view.changeStep("step2");
        }
    }

    public void step2() {
        String password = this._view.getPassword();
        String confirmPassword = this._view.getConfirmPassword();

        if (!Objects.equals(password, confirmPassword)) {
            _view.setErrorConfirmPassword(R.string.signup_step2_confirm_password_error);
        }
        else  {
            _view.changeStep("step3");
        }
    }

    public void step3() {
        String pseudo = this._view.getPseudo();

        if (pseudo.isEmpty()) {
            _view.setErrorPseudo(R.string.empty_input);
        }
        else {
            HashMap<String, String> userParams = new HashMap<>();

            userParams.put("email", _view.getEmail());
            userParams.put("password", _view.getPassword());
            userParams.put("pseudo", _view.getPseudo());
            userParams.put("first_name", _view.getFirstName());
            userParams.put("last_name", _view.getLastName());

            _signUpService.signUp(userParams);
        }
    }

    private String getPseudo() {
        String pseudo = this._view.getPseudo();

        if (pseudo.isEmpty()) {
            _view.setErrorPseudo(R.string.empty_input);
            return null;
        }
        return pseudo;
    }

    public void onGoogleClicked(String token) {
        String pseudo = this.getPseudo();

        if (pseudo != null) {
            HashMap<String, String> params = new HashMap<>();

            params.put("access_token", token);
            params.put("pseudo", pseudo);

            this._signUpService.signUpExternalApi(params, "google");
        }
    }

    public void onFacebookClicked(String access_token_facebook) {
        String pseudo = this.getPseudo();

        if (pseudo != null) {
            HashMap<String, String> params = new HashMap<>();

            params.put("access_token", access_token_facebook);
            params.put("pseudo", pseudo);

            this._signUpService.signUpExternalApi(params, "facebook");
        }
    }

    public void checkEmail() {
        String email = _view.getEmail();

        if (email != null && !email.isEmpty()) {
            this._signUpService.checkEmail(email);
        }
    }
}
