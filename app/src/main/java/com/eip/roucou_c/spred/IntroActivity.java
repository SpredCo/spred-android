package com.eip.roucou_c.spred;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.eip.roucou_c.spred.Api.ApiLogin;
import com.eip.roucou_c.spred.SignIn.SignInActivity;
import com.eip.roucou_c.spred.SignUp.SignUpActivity;

import mehdi.sakout.fancybuttons.FancyButton;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private FancyButton _intro_signUp;
    private FancyButton _intro_signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.intro);

        _intro_signUp = (FancyButton) findViewById(R.id.intro_signUp);
        _intro_signUp.setOnClickListener(this);

        _intro_signIn = (FancyButton) findViewById(R.id.intro_signIn);
        _intro_signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intro_signIn:
                this.finish();
                Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.intro_signUp:
                this.finish();
                Intent intent2 = new Intent(IntroActivity.this, SignUpActivity.class);
                startActivity(intent2);
                break;
            case R.id.intro_skip:
                break;
        }
    }
}
