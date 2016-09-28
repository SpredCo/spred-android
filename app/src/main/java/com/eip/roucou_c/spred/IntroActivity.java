package com.eip.roucou_c.spred;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.eip.roucou_c.spred.Api.ApiLogin;
import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Home.HomeActivity;
import com.eip.roucou_c.spred.SignIn.SignInActivity;
import com.eip.roucou_c.spred.SignUp.SignUpActivity;

import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private FancyButton _intro_signUp;
    private FancyButton _intro_signIn;
    private Manager _manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());
        autoLogin();

        // todo optimiser le autologin pour afficher directement la home

        setContentView(R.layout.intro);

        _intro_signUp = (FancyButton) findViewById(R.id.intro_signUp);
        _intro_signUp.setOnClickListener(this);

        _intro_signIn = (FancyButton) findViewById(R.id.intro_signIn);
        _intro_signIn.setOnClickListener(this);
    }

    private void autoLogin() {
        TokenEntity tokenEntity = _manager._tokenManager.select();

//        this.finish();
//        Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);

        if (tokenEntity != null) {
            if (isValidAccessToken(tokenEntity.get_expire_access_token())) {
                this.finish();
                Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } else {

            }
        }
    }

    public boolean isValidAccessToken(String expireAccess_token) {
        Date date = new Date();

        if (date.getTime() > Long.parseLong(expireAccess_token)) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intro_signIn:
                Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.intro_signUp:
                Intent intent2 = new Intent(IntroActivity.this, SignUpActivity.class);
                startActivity(intent2);
                break;
            case R.id.intro_skip:
                break;
        }
    }
}
