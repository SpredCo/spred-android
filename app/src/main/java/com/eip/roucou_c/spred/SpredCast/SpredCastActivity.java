package com.eip.roucou_c.spred.SpredCast;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Home.HomeActivity;
import com.eip.roucou_c.spred.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by roucou_c on 07/11/2016.
 */
public class SpredCastActivity extends AppCompatActivity implements ISpredCastView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Manager _manager;
    private SpredCastPresenter _spredCastPresenter;
    private UserEntity _userEntity;
    private Toolbar _toolbar;
    private String _curentStep;
    private SwipeRefreshLayout _spredCast_swipeRefreshLayout;
    private RecyclerView _spredCast_recycler_view;
    private FloatingActionButton _spredCast_floatActionButton;
    private Menu _menu;
    private SpredCastsAdapter _spredCast_adapter;
    private DisplayImageOptions _displayImageOptions;
    private TextView _empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _spredCastPresenter = new SpredCastPresenter(this, null, null, null, _manager, tokenEntity);

        _userEntity = (UserEntity) getIntent().getSerializableExtra("userEntity");

        setContentView(R.layout.spredcast);

        _spredCast_swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        _spredCast_swipeRefreshLayout.setOnRefreshListener(this);
        _spredCast_recycler_view = (RecyclerView) findViewById(R.id.spredcast_recycler_view);

        _spredCast_adapter = new SpredCastsAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        _spredCast_recycler_view.setLayoutManager(mLayoutManager);
        _spredCast_recycler_view.setItemAnimator(new DefaultItemAnimator());
        _spredCast_recycler_view.setAdapter(_spredCast_adapter);

        _spredCast_floatActionButton = (FloatingActionButton) findViewById(R.id.spredcast_floatActionButton);
        _spredCast_floatActionButton.setOnClickListener(this);

        HomeActivity.SSLCertificateHandler.nuke();
        _displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        _empty_view = (TextView) findViewById(R.id.empty_view);

        _spredCastPresenter.getSpredCast();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        _spredCastPresenter.getSpredCast();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Mes SpredCasts");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spredcast_floatActionButton:
                Intent intent = new Intent(SpredCastActivity.this, SpredCastNewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("userEntity", _userEntity);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        _spredCastPresenter.getSpredCast();
    }

    @Override
    public void cancelRefresh() {
        _spredCast_swipeRefreshLayout.setRefreshing(false);
    }



    @Override
    public void populateSpredCasts(List<SpredCastEntity> spredCastEntities) {
        if (_spredCast_adapter != null) {
            _spredCast_adapter.set_spredCastEntities(spredCastEntities);
            _spredCast_adapter.notifyDataSetChanged();
            if (spredCastEntities.size() == 0) {
                _spredCast_recycler_view.setVisibility(View.GONE);
                _empty_view.setVisibility(View.VISIBLE);
            }
            else {
                _spredCast_recycler_view.setVisibility(View.VISIBLE);
                _empty_view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void startSpredCastDetailActivity(SpredCastEntity spredCastEntity) {
        Intent intent = new Intent(this, SpredCastDetailsActivity.class);
        intent.putExtra("spredCast", spredCastEntity);
        this.startActivity(intent);
    }

    @Override
    public void getImageProfile(String url, ImageView photo_profile) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(url, photo_profile, _displayImageOptions);
    }
}