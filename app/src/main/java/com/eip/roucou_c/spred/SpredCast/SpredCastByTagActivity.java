package com.eip.roucou_c.spred.SpredCast;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TagEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Home.*;
import com.eip.roucou_c.spred.Home.SpredCastsAdapter;
import com.eip.roucou_c.spred.R;

import java.util.List;

/**
 * Created by roucou_c on 15/12/2016.
 */

public class SpredCastByTagActivity extends AppCompatActivity implements IHomeSpredCastView, ISpredCastByTagView {

    private SwipeRefreshLayout _spredCast_swipeRefreshLayout;
    private SpredCastsAdapter _spredCast_adapter;
    private TagEntity _tagEntity;
    private SpredCastPresenter _spredCastPresenter;
    private FloatingActionButton _spredCast_floatActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Manager _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _spredCastPresenter = new SpredCastPresenter(null, null, null, this, _manager, tokenEntity);

        setContentView(R.layout.spredcast_by_tag);

        _spredCast_swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        _spredCast_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _spredCastPresenter.getSpredCastsByTag(_tagEntity.get_name());
            }
        });
        RecyclerView _spredCast_recycler_view = (RecyclerView) findViewById(R.id.spredcast_recycler_view);

        _spredCast_adapter = new com.eip.roucou_c.spred.Home.SpredCastsAdapter(this, 2, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        _spredCast_recycler_view.setLayoutManager(mLayoutManager);
        _spredCast_recycler_view.setItemAnimator(new DefaultItemAnimator());
        _spredCast_recycler_view.setAdapter(_spredCast_adapter);
        _spredCast_floatActionButton = (FloatingActionButton) findViewById(R.id.spredcast_floatActionButton);

        String tag_name = getIntent().getStringExtra("tag_name");
        if (tag_name != null) {
            _spredCastPresenter.getTag(tag_name);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        Toolbar _toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void populateSpredCasts(List<SpredCastEntity> spredCastEntities) {
        if (_spredCast_adapter != null) {
            _spredCast_adapter.set_spredCastEntities(spredCastEntities);
            _spredCast_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void cancelRefresh() {
        _spredCast_swipeRefreshLayout.setRefreshing(false);
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
    public void setTag(TagEntity tagEntity) {
        _tagEntity = tagEntity;

        getSupportActionBar().setTitle("SpredCast by #"+tagEntity.get_name());

        _spredCastPresenter.getSpredCastsByTag(tagEntity.get_name());
        _spredCastPresenter.getIsSubscriptionTag(tagEntity.get_id());
    }

    @Override
    public void setIsSub(final boolean isSub) {
        if (isSub) {
            _spredCast_floatActionButton.setImageDrawable(getDrawable(R.drawable.ic_star_white_24dp));
        }
        else {
            _spredCast_floatActionButton.setImageDrawable(getDrawable(R.drawable.ic_star_border_white_24dp));
        }

        _spredCast_floatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _spredCastPresenter.subscriptionTag(!isSub, _tagEntity.get_id());
            }
        });
    }
}
