package com.eip.roucou_c.spred.Profile;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.FollowerEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Home.AboAdapter;
import com.eip.roucou_c.spred.R;

import java.util.List;
import java.util.Objects;

/**
 * Created by roucou_c on 16/12/2016.
 */

public class FollowersActivity extends AppCompatActivity implements IFollowersView {

    private Manager _manager;
    private ProfilePresenter _profilePresenter;
    private SwipeRefreshLayout _profile_followers_swipeRefreshLayout;
    private RecyclerView _profile_followers_recycler_view;
    private FollowersAdapter _profile_followers_adapter;
    private Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _profilePresenter = new ProfilePresenter(null, this, _manager, tokenEntity);

        setContentView(R.layout.profile_followers);

        _profile_followers_swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        _profile_followers_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _profilePresenter.getFollowers();
            }
        });
        _profile_followers_recycler_view = (RecyclerView) findViewById(R.id.profile_followers_recycler_view);

        _profile_followers_adapter = new FollowersAdapter(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getApplicationContext());
        _profile_followers_recycler_view.setLayoutManager(mLayoutManager3);
        _profile_followers_recycler_view.setItemAnimator(new DefaultItemAnimator());
        _profile_followers_recycler_view.setAdapter(_profile_followers_adapter);

        _profilePresenter.getFollowers();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _toolbar.setTitle("Vos followers");
    }

    @Override
    public void setFollowers(List<FollowerEntity> followerEntities) {
        _profile_followers_adapter.set_followerEntities(followerEntities);
        _profile_followers_adapter.notifyDataSetChanged();

        _profile_followers_swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
