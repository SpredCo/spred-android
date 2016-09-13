package com.eip.roucou_c.spred.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Home.HomeActivity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.Api.ApiLogin;
import com.facebook.login.widget.LoginButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by roucou-c on 07/12/15.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ISignInView {

    private MaterialEditText _signin_email;
    private MaterialEditText _signin_password;

    private FancyButton _signin_submit;

//    EditText signin_email = null;
//    EditText signin_password = null;
//
//    ActionProcessButton signin_submitLogin = null;
//    Button signin_signup = null;
//
    public SignInPresenter _signInPresenter;
    private Manager _manager;
    private ApiLogin _apiLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());

        this._signInPresenter = new SignInPresenter(this, _manager);

        _apiLogin = new ApiLogin(getApplicationContext(), this);

        setContentView(R.layout.signin);

        _signin_email = (MaterialEditText) findViewById(R.id.signin_email);
        _signin_password = (MaterialEditText) findViewById(R.id.signin_password);

        _signin_email.setText("clement.roucour@gmail.com");
        _signin_password.setText("1234");

        _signin_submit = (FancyButton) findViewById(R.id.signin_submit);
        _signin_submit.setOnClickListener(this);

//        SignInButton signin_google = (SignInButton) findViewById(R.id.signin_google);
//        signin_google.setOnClickListener(this);


//        this._signInPresenter.isLoginWithRefreshToken();

//        _apiLogin.launch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _apiLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_submit:
                this._signInPresenter.onLoginClicked();
                break;
            case R.id.signin_google:
               _apiLogin.login("google");
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
        return this._signin_email.getText().toString();
    }

    @Override
    public String getPassword() {
        return this._signin_password.getText().toString();
    }

    @Override
    public void setErrorEmail(int resId) {

    }

    @Override
    public void setErrorPassword(int resId) {
//        TextInputLayout signin_password_inputLayout = (TextInputLayout) findViewById(R.id.signin_password_inputLayout);
//        signin_password_inputLayout.setError((resId == 0 ? null : getString(resId)));
    }

//    @Override
//    public void setProcessLoadingButton(int process) {
//        ActionProcessButton actionProcessButton = (ActionProcessButton) findViewById(R.id.signin_submitLogin);
//        actionProcessButton.setProgress(process);
//    }

//    @Override
//    public void initializeInputLayout() {
//        this.setErrorEmail(0);
//        this.setErrorPassword(0);
//    }

//    @Override
//    public CoordinatorLayout getCoordinatorLayout() {
////        return (CoordinatorLayout) findViewById(R.id.display_snackbar);
//        return null;
//
//    }

//    @Override
//    public ActionProcessButton getActionProcessButton() {
//        return (ActionProcessButton) findViewById(R.id.signin_submitLogin);
//    }

//    @Override
//    public void startRoomActivity() {
////        this.finish();
////        Intent intent = new Intent(SignInActivity.this, RoomSettingsActivity.class);
////        startActivity(intent);
//    }

//    @Override
//    public void startMainActivity() {
////        this.finish();
////        Intent intent = new Intent(SignInActivity.this, MyActivityDrawer.class);
////        startActivity(intent);
//    }

    @Override
    public void signinSuccess() {
        this.finish();
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        Intent i = new Intent(Intent.ACTION_MAIN);
//        i.addCategory(Intent.CATEGORY_HOME);
//        startActivity(i);
    }

    @Override
    public LoginButton getSignInButtonFacebook() {
        return (LoginButton) findViewById(R.id.signin_facebook);
    }

    @Override
    public void onFacebookClicked(String access_token_facebook) {
        _signInPresenter.onLoginFacebookClicked(access_token_facebook);
    }

    @Override
    public void onGoogleClicked(String token) {

    }

    @Override
    public AppCompatActivity getActivity() {
        return null;
    }
}