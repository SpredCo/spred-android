package com.eip.roucou_c.spred.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Home.HomeActivity;
import com.eip.roucou_c.spred.ISignInSignUpView;
import com.eip.roucou_c.spred.IntroActivity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.Api.ApiLogin;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by roucou-c on 07/12/15.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ISignInView, ISignInSignUpView {

    private MaterialEditText _signin_step1_email;
    private MaterialEditText _signin_step1_password;
    private MaterialEditText _signin_step2_pseudo;

    private FancyButton _signin_step1_submit;

    private String _token_facebook;
    private String _token_google;
    private String _step;
    private String _pseudo;

    public SignInPresenter _signInPresenter;
    private Manager _manager;
    private ApiLogin _apiLogin;
    private CoordinatorLayout _coordinatorLayout;
    private FancyButton _signin_step2_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());

        this._signInPresenter = new SignInPresenter(this, this, _manager);

        _apiLogin = new ApiLogin(getApplicationContext(), this);
        _apiLogin.launch();


        changeStep("step1");

//        this._signInPresenter.isLoginWithRefreshToken();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _apiLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_step1_submit:
                this._signInPresenter.onLoginClicked();
                break;
            case R.id.signin_step2_submit:
                _pseudo = _signin_step2_pseudo.getText().toString();

                this._signInPresenter.onSignUpClicked();
                break;

            case R.id.signin_google:
                _apiLogin = new ApiLogin(getApplicationContext(), this);
                _apiLogin.launch();
                _apiLogin.login("google");
                break;
        }
    }

    public void changeStep(String step) {
        _step = step;
        switch (step) {
            case "step1":
                setContentView(R.layout.signin_step1);

                LoginManager.getInstance().logOut();

                _coordinatorLayout = (CoordinatorLayout) findViewById(R.id.display_snackbar);

                _signin_step1_email = (MaterialEditText) findViewById(R.id.signin_step1_email);
                _signin_step1_password = (MaterialEditText) findViewById(R.id.signin_step1_password);

                _signin_step1_email.setText("clement.roucour@gmail.com");
                _signin_step1_password.setText("1234");

                _signin_step1_submit = (FancyButton) findViewById(R.id.signin_step1_submit);
                _signin_step1_submit.setOnClickListener(this);

                SignInButton signin_google = (SignInButton) findViewById(R.id.signin_google);
                signin_google.setOnClickListener(this);

                break;
            case "step2":
                setContentView(R.layout.signin_step2);

                _signin_step2_pseudo = (MaterialEditText) findViewById(R.id.signin_step2_pseudo);

                _signin_step2_submit = (FancyButton) findViewById(R.id.signin_step2_submit);
                _signin_step2_submit.setOnClickListener(this);

                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (v.getId() == R.id.signin_password_editText)
//        {
//            this._signInPresenter.onLoginClicked();
//        }
//        return false;
        return false;
    }

    @Override
    public String getEmail() {
        return this._signin_step1_email.getText().toString();
    }

    @Override
    public String getPassword() {
        return this._signin_step1_password.getText().toString();
    }

    @Override
    public void setErrorEmail(int resId) {
        _signin_step1_email.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorPassword(int resId) {
        _signin_step1_password.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void signInSuccess() {
        this.finish();
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent intent = new Intent(SignInActivity.this, IntroActivity.class);
        startActivity(intent);
    }

    @Override
    public LoginButton getSignInButtonFacebook() {
        return (LoginButton) findViewById(R.id.signin_facebook);
    }

    @Override
    public void onFacebookClicked(String access_token_facebook) {
        _token_facebook = access_token_facebook;
        _signInPresenter.onLoginFacebookClicked(access_token_facebook);
    }

    @Override
    public void onGoogleClicked(String token) {
        _token_google = token;
        _signInPresenter.onLoginGoogleClicked(token);
    }

    @Override
    public AppCompatActivity getActivity() {
        return (AppCompatActivity) this;
    }

    @Override
    public void setErrorPseudo(int resId) {
        _signin_step2_pseudo.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public String getPseudo() {

        return _pseudo;
    }

    @Override
    public void setError(int resId) {
        if (_coordinatorLayout != null) {
            Snackbar snackbar = Snackbar
                    .make(_coordinatorLayout, resId, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    public MaterialEditText get_signin_step1_password() {
        return _signin_step1_password;
    }

    public String get_token_facebook() {
        return _token_facebook;
    }

    public String get_token_google() {
        return _token_google;
    }
}