package com.eip.roucou_c.spred.Home.TabLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Home.AboAdapter;
import com.eip.roucou_c.spred.Home.IHomeAboView;
import com.eip.roucou_c.spred.Home.IHomeSpredCastView;
import com.eip.roucou_c.spred.Home.IHomeView;
import com.eip.roucou_c.spred.Home.SpredCastsAdapter;
import com.eip.roucou_c.spred.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roucou_c on 21/09/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    List<TabFragment> fragmentList = new ArrayList<>();

    private IHomeView _iHomeView;

    public ViewPagerAdapter(FragmentManager fm, IHomeView iHomeView) {
        super(fm);
        _iHomeView = iHomeView;
    }

    public TabFragment getItem(int position) {
        TabFragment fragment = fragmentList.size() > position ? fragmentList.get(position) : null;

        if (fragment == null) {
            String step = String.valueOf((position + 1));
            fragment = TabFragment.newInstance(step, _iHomeView);
            fragmentList.add(fragment);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "to";
    }

    public static class TabFragment extends Fragment implements IHomeSpredCastView, IHomeAboView{

        private String step;
        public IHomeView _iHomeView;

        /**
         * Step 1 SpredCast
         */
        private SwipeRefreshLayout _spredCast_swipeRefreshLayout;
        private RecyclerView _spredCast_recycler_view;
        private SpredCastsAdapter _spredCast_adapter;
        private SwipeRefreshLayout _abo_swipeRefreshLayout;
        private RecyclerView _abo_recycler_view;
        private AboAdapter _abo_adapter;

        public static TabFragment newInstance(String step, IHomeView iHomeView) {
            TabFragment fragment = new TabFragment();
            fragment.step = step;
            fragment._iHomeView = iHomeView;

            return fragment;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = null;

            switch (this.step) {
                case "1":
                    rootView = inflater.inflate(R.layout.tab_spredcast, container, false);

                    _spredCast_swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
                    _spredCast_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            _iHomeView.getSpredCasts(1);
                        }
                    });
                    _spredCast_recycler_view = (RecyclerView) rootView.findViewById(R.id.spredcast_recycler_view);

                    _spredCast_adapter = new SpredCastsAdapter(this, 1, getContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    _spredCast_recycler_view.setLayoutManager(mLayoutManager);
                    _spredCast_recycler_view.setItemAnimator(new DefaultItemAnimator());
                    _spredCast_recycler_view.setAdapter(_spredCast_adapter);

                    _iHomeView.getSpredCasts(1);

                    break;
                case "2":
                    rootView = inflater.inflate(R.layout.tab_spredcast, container, false);

                    _spredCast_swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
                    _spredCast_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            _iHomeView.getSpredCasts(0);
                        }
                    });
                    _spredCast_recycler_view = (RecyclerView) rootView.findViewById(R.id.spredcast_recycler_view);

                    _spredCast_adapter = new SpredCastsAdapter(this, 0, getContext());
                    RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
                    _spredCast_recycler_view.setLayoutManager(mLayoutManager2);
                    _spredCast_recycler_view.setItemAnimator(new DefaultItemAnimator());
                    _spredCast_recycler_view.setAdapter(_spredCast_adapter);

                    _iHomeView.getSpredCasts(0);
                    break;
                case "3":
                    rootView = inflater.inflate(R.layout.tab_abo, container, false);

                    _abo_swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
                    _abo_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            _iHomeView.getAbo();
                        }
                    });
                    _abo_recycler_view = (RecyclerView) rootView.findViewById(R.id.abo_recycler_view);

                    _abo_adapter = new AboAdapter(getContext());
                    RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getContext());
                    _abo_recycler_view.setLayoutManager(mLayoutManager3);
                    _abo_recycler_view.setItemAnimator(new DefaultItemAnimator());
                    _abo_recycler_view.setAdapter(_abo_adapter);

                    _iHomeView.getAbo();
                    break;
            }
            return rootView;
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
            if (_spredCast_swipeRefreshLayout != null) {
                _spredCast_swipeRefreshLayout.setRefreshing(false);
            }
            if (_abo_swipeRefreshLayout != null) {
                _abo_swipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void populateAbo(List<FollowEntity> followEntities) {
            if (_abo_adapter != null) {
                _abo_adapter.set_followEntities(followEntities);
                _abo_adapter.notifyDataSetChanged();
            }
        }
    }


}
