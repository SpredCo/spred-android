package com.eip.roucou_c.spred.Home.TabLayout;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Home.HomeActivity;
import com.eip.roucou_c.spred.Home.IHomeView;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SpredCast.ISpredCastView;
import com.eip.roucou_c.spred.SpredCast.SpredCastsAdapter;

import java.util.List;

/**
 * Created by roucou_c on 22/09/2016.
 */
//public class FragmentHome extends android.support.v4.app.Fragment implements ISpredCastView{
//    private HomeActivity _homeActivity;
//    private SwipeRefreshLayout _spredCast_swipeRefreshLayout;
//    private RecyclerView _spredCast_recycler_view;
//    private SpredCastsAdapter _spredCast_adapter;
//
//
//    public FragmentHome() {
//    }
//
//    public FragmentHome(HomeActivity homeActivity) {
//        _homeActivity = homeActivity;
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.tab_home, container, false);
//
//        _spredCast_swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
//        _spredCast_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                _homeActivity._homePresenter.getSpredCasts();
//            }
//        });
//        _spredCast_recycler_view = (RecyclerView) view.findViewById(R.id.spredcast_recycler_view);
//
//        _spredCast_adapter = new SpredCastsAdapter(_homeActivity);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        _spredCast_recycler_view.setLayoutManager(mLayoutManager);
//        _spredCast_recycler_view.setItemAnimator(new DefaultItemAnimator());
//        _spredCast_recycler_view.setAdapter(_spredCast_adapter);
//
//
//        _homeActivity._homePresenter.getSpredCasts();
//
//        return view;
//    }
//
//    @Override
//    public void populateSpredCasts(List<SpredCastEntity> spredCastEntities) {
//        if (_spredCast_adapter != null) {
//            _spredCast_adapter.set_spredCastEntities(spredCastEntities);
//            _spredCast_adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void cancelRefresh() {
//        _spredCast_swipeRefreshLayout.setRefreshing(false);
//    }
//}
