package com.eip.roucou_c.spred.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.IntroActivity;
import com.eip.roucou_c.spred.R;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by roucou_c on 09/09/2016.
 */
public class HomeActivity extends AppCompatActivity implements IHomeView{

    private Manager _manager;
    private HomePresenter _homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        _manager = Manager.getInstance(getApplicationContext());

        _homePresenter = new HomePresenter(this, _manager);

//        _homePresenter.deleteUser();
    }

    @Override
    public void userDeleted() {
        this.finish();
        Intent intent = new Intent(HomeActivity.this, IntroActivity.class);
        startActivity(intent);
    }
}
