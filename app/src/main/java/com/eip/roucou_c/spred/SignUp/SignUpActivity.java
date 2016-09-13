package com.eip.roucou_c.spred.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.eip.roucou_c.spred.Api.ApiLogin;
import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Home.HomeActivity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SignIn.SignInActivity;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by roucou_c on 01/09/2016.
 */
public final class SignUpActivity extends AppCompatActivity implements View.OnClickListener, ISignUpView {

    private String _email;
    private String _prenom;
    private String _nom;
    private String _password;
    private String _confirm_password;
    private String _pseudo;

    private Manager _manager;
    private SignUpPresenter _signupPresenter;

    private FancyButton _signup_step1_submit;
    private FancyButton _signup_step2_submit;
    private FancyButton _signup_step3_submit;

    private MaterialEditText _signup_step1_email;
    private MaterialEditText _signup_step1_nom;
    private MaterialEditText _signup_step1_prenom;
    private MaterialEditText _signup_step1_password;
    private MaterialEditText _signup_step2_confirm_password;
    private MaterialEditText _signup_step3_pseudo;

    private ApiLogin _apiLogin;
    private String _token_facebook;
    private String _token_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _apiLogin = new ApiLogin(getApplicationContext(), this);

        _manager = Manager.getInstance(getApplicationContext());
        _signupPresenter = new SignUpPresenter(this, _manager);

        changeStep("step1");
        _apiLogin.launch();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_step1_submit:

                clearError("step1");

                _email = _signup_step1_email.getText().toString();
                _password = _signup_step1_password.getText().toString();
                _prenom = _signup_step1_prenom.getText().toString();
                _nom = _signup_step1_nom.getText().toString();

                _signupPresenter.step1();

                break;
            case R.id.signup_step2_submit:
                _signupPresenter.step2();

                break;
            case R.id.signup_step3_submit:

                _pseudo = _signup_step3_pseudo.getText().toString();

                if (_token_facebook != null) {
                    _signupPresenter.onFacebookClicked(_token_facebook);
                }
                else if (_token_google != null) {
                    _signupPresenter.onGoogleClicked(_token_google);
                }
                else {
                    _signupPresenter.step3();
                }
                break;
            case R.id.signin_google:
                _apiLogin.login("google");
                break;
        }
    }



    private void clearError(String step) {
        switch (step) {
            case "step1":
                setErrorEmail(0);
                break;
        }
    }


    @Override
    public String getFirstName() {
        return _prenom;
    }

    @Override
    public String getLastName() {
        return _nom;
    }

    @Override
    public String getEmail() {
        return _email;
    }

    @Override
    public String getPassword() {
        return _password;
    }

    @Override
    public String getConfirmPassword() {
        return _confirm_password;
    }

    @Override
    public String getPseudo() {
        return _pseudo;
    }

    @Override
    public void setErrorEmail(int resId) {
        _signup_step1_email.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorPassword(int resId) {
        _signup_step1_password.setError(resId == 0 ? null : getString(resId));

    }

    @Override
    public void setErrorLastName(int resId) {
        _signup_step1_nom.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorFirstName(int resId) {
        _signup_step1_prenom.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorConfirmPassword(int resId) {
        _signup_step2_confirm_password.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorPseudo(int resId) {
        _signup_step3_pseudo.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void changeStep(String step) {
        switch (step) {
            case "step1":
                setContentView(R.layout.signup_step1);

                SignInButton signin_google = (SignInButton) findViewById(R.id.signin_google);
                signin_google.setOnClickListener(this);

                _signup_step1_submit = (FancyButton) findViewById(R.id.signup_step1_submit);
                _signup_step1_submit.setOnClickListener(this);

                _signup_step1_email = (MaterialEditText) findViewById(R.id.signup_step1_email);
                _signup_step1_nom = (MaterialEditText) findViewById(R.id.signup_step1_nom);
                _signup_step1_prenom = (MaterialEditText) findViewById(R.id.signup_step1_prenom);
                _signup_step1_password = (MaterialEditText) findViewById(R.id.signup_step1_password);

                _signup_step1_email.setText("clement.roucour2@gmail.com");
                _signup_step1_nom.setText("Roucour");
                _signup_step1_prenom.setText("clement");
                _signup_step1_password.setText("1234");

                break;
            case "step2":
                setContentView(R.layout.signup_step2);
                _signup_step2_submit = (FancyButton) findViewById(R.id.signup_step2_submit);
                _signup_step2_submit.setOnClickListener(this);


                _signup_step2_confirm_password = (MaterialEditText) findViewById(R.id.signup_step2_confirm_password);

                _signup_step2_confirm_password.setText("1234");

                _confirm_password = _signup_step2_confirm_password.getText().toString();

                break;
            case "step3":
                setContentView(R.layout.signup_step3);

                _signup_step3_submit = (FancyButton) findViewById(R.id.signup_step3_submit);
                _signup_step3_submit.setOnClickListener(this);
                _signup_step3_pseudo = (MaterialEditText) findViewById(R.id.signup_step3_pseudo);
                _signup_step3_pseudo.setText("roucou_c2");

                break;
        }
    }

    @Override
    public void signUpSuccess() {
        this.finish();
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public LoginButton getSignInButtonFacebook() {
        return (LoginButton) findViewById(R.id.signin_facebook);
    }

    @Override
    public void onFacebookClicked(String access_token_facebook) {
        _token_facebook = access_token_facebook;
        changeStep("step3");
//        _signupPresenter.onFacebookClicked(access_token_facebook);
    }

    @Override
    public void onGoogleClicked(String token) {
        _token_google = token;
        changeStep("step3");
//        _signupPresenter.onGoogleClicked(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _apiLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public AppCompatActivity getActivity() {
        return (AppCompatActivity) this;
    }
}