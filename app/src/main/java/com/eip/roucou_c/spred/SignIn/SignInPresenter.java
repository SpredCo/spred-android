package com.eip.roucou_c.spred.SignIn;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.ISignInSignUpView;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SignUp.SignUpService;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Date;
import java.util.HashMap;


/**
 * Created by roucou-c on 07/12/15.
 */
public class SignInPresenter{

    private ISignInView _view;
    private ISignInSignUpView _iSignInSignUpView;
    private SignInService _signInService;
    private SignUpService _signUpService;


    public SignInPresenter(ISignInView view, ISignInSignUpView iSignInSignUpView, Manager manager) {
        this._view = view;
        _iSignInSignUpView = iSignInSignUpView;
        this._signInService = new SignInService(view, iSignInSignUpView, manager);

        if (view != null) {
            this._signUpService = new SignUpService(null, iSignInSignUpView, manager);
        }
    }

    public void onLoginClicked() {
        boolean isError = false;
        String email = _view.getEmail();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (email.isEmpty()) {
                _view.setErrorEmail(R.string.empty_input);
            }
            else {
                _view.setErrorEmail(R.string.invalid_email);
            }
            isError = true;
        }

        String password = _view.getPassword();
        if (password.isEmpty()) {
            _view.setErrorPassword(R.string.empty_input);
            isError = true;
        }

        MaterialEditText materialEditTextPassword = _view.get_signin_step1_password();
        if (!materialEditTextPassword.isCharactersCountValid()) {
            _view.setErrorPassword(R.string.wrong_length_password);
            isError = true;
        }

        if (!isError) {
            HashMap<String, String> params = new HashMap<>();

            params.put("username", email);
            params.put("password", password);

            this._signInService.signIn(params);
        }
    }

    public void onLoginFacebookClicked(String access_token_facebook) {
        HashMap<String, String> params = new HashMap<>();

        params.put("access_token", access_token_facebook);

        this._signInService.signInExternalApi(params, "facebook");
    }

    public void onLoginGoogleClicked(String access_token_google) {
        HashMap<String, String> params = new HashMap<>();

        params.put("access_token", access_token_google);

        this._signInService.signInExternalApi(params, "google");
    }

    public boolean isLoginWithRefreshToken() {

//        ProfileEntity profileLogged = _signInService._userEntity._profileEntity;
//        TokenEntity tokenEntity = _signInService._userEntity._tokenEntity;

//        if (profileLogged == null || tokenEntity == null) {
//            return false;
//        }
//
//        String refresh_token = tokenEntity.get_refresh_token();
//        String expireAccess_token = tokenEntity.get_expire_access_token();

//        if (refresh_token == null || expireAccess_token == null) {
//            return false;
//        }
//
//        // TODO : faire le test sur l'expiration du token refresh
//
//        if (isValidAccessToken(expireAccess_token)) {
//            this._signInService.signInWithAccessTokenValid();
//        }
//        else {
//            this._signInService.refreshToken();
//        }
//        return true;
        return false;
    }

    public boolean isValidAccessToken(String expireAccess_token) {
        Date date = new Date();

        if (date.getTime() > Long.parseLong(expireAccess_token)) {
            return false;
        }
        return true;
    }

    private String getPseudo() {
        String pseudo = this._iSignInSignUpView.getPseudo();

        if (pseudo.isEmpty()) {
            _iSignInSignUpView.setErrorPseudo(R.string.empty_input);
            return null;
        }
        return pseudo;
    }

    public void onSignUpClicked() {
        String token = _iSignInSignUpView.get_token_facebook() != null ? _iSignInSignUpView.get_token_facebook() : _iSignInSignUpView.get_token_google();
        String token_type = _iSignInSignUpView.get_token_facebook() != null ? "facebook" : "google";

        String pseudo = this.getPseudo();

        if (token != null && pseudo != null) {

            HashMap<String, String> params = new HashMap<>();

            params.put("access_token", token);
            params.put("pseudo", pseudo);

            _signInService.signUpExternalApi(params, token_type);
        }
    }
}
