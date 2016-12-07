package com.eip.roucou_c.spred.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Home.TabLayout.ViewPagerAdapter;
import com.eip.roucou_c.spred.Inbox.InboxActivity;
import com.eip.roucou_c.spred.Profile.ProfileActivity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SpredCast.SpredCastActivity;

import java.util.List;

/**
 * Created by roucou_c on 09/09/2016.
 */
public class HomeActivity extends AppCompatActivity implements IHomeView, ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private Manager _manager;
    public HomePresenter _homePresenter;

    private Toolbar _toolbar;
    private TabLayout _tabLayout;
    private ViewPager _viewPager;
    private ViewPagerAdapter _viewPagerAdapter;
    private TabLayout.Tab _spredcast_live;
    private TabLayout.Tab _spredcast_come;
    private TabLayout.Tab _abo;
    private DrawerLayout _drawerLayout;
    private ActionBarDrawerToggle _drawerToggle;
    private UserEntity _userEntity;
    private NavigationView _navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _homePresenter = new HomePresenter(this, _manager, tokenEntity);

        _tabLayout = (TabLayout) findViewById(R.id.tabs);
        _viewPager = (ViewPager) findViewById(R.id.viewpager);

        _viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        _viewPager.setAdapter(_viewPagerAdapter);

        _spredcast_live = _tabLayout.newTab();
        _spredcast_come = _tabLayout.newTab();
        _abo = _tabLayout.newTab();

        _tabLayout.addTab(_spredcast_live, 0);
        _tabLayout.addTab(_spredcast_come, 1);
        _tabLayout.addTab(_abo, 2);

        _viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(_tabLayout));

        _spredcast_live.setIcon(R.drawable.ic_home_white_24dp);
        _spredcast_come.setIcon(R.drawable.ic_home_black_24dp);
        _abo.setIcon(R.drawable.ic_subscriptions_black_24dp);

        _viewPager.addOnPageChangeListener(this);
        _tabLayout.setOnTabSelectedListener(this);

        _homePresenter.getProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _homePresenter.getAbo();
        _homePresenter.getSpredCasts();
    }

    @Override
    protected void onStop() {
        if (_drawerLayout != null) {
            _drawerLayout.closeDrawers();
        }
        super.onStop();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.onCreateDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {

        }
        return super.onOptionsItemSelected(item);
    }

    protected void onCreateDrawer() {
        _toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (_toolbar != null) {
            setSupportActionBar(_toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            _drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

            _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, R.string.app_name, R.string.app_name) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };
            _drawerToggle.syncState();

            _drawerLayout.addDrawerListener(_drawerToggle);

            _navigation = (NavigationView) findViewById(R.id.navigation_view);
            _navigation.inflateHeaderView(R.layout.navigation_header);
            _navigation.inflateMenu(R.menu.navigation);
            _navigation.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                _spredcast_live.setIcon(R.drawable.ic_home_white_24dp);
                _spredcast_come.setIcon(R.drawable.ic_home_black_24dp);
                _abo.setIcon(R.drawable.ic_subscriptions_black_24dp);
                break;
            case 1:
                _spredcast_live.setIcon(R.drawable.ic_home_black_24dp);
                _spredcast_come.setIcon(R.drawable.ic_home_white_24dp);
                _abo.setIcon(R.drawable.ic_subscriptions_black_24dp);
                break;
            case 2:
                _spredcast_live.setIcon(R.drawable.ic_home_black_24dp);
                _spredcast_come.setIcon(R.drawable.ic_home_black_24dp);
                _abo.setIcon(R.drawable.ic_subscriptions_white_24dp);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        _viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_spredcast:
                Intent intent2 = new Intent(this, SpredCastActivity.class);
                intent2.putExtra("userEntity", _userEntity);
                startActivity(intent2);
                break;
            case R.id.navigation_inbox:
                Intent intent3 = new Intent(this, InboxActivity.class);
                intent3.putExtra("userEntity", _userEntity);
                startActivity(intent3);
                break;
            case R.id.navigation_logout:
                _manager._tokenManager.delete();
                this.finish();
                break;
        }
        return true;
    }

    @Override
    public void setProfile(UserEntity userEntity) {
        _userEntity = userEntity;

        TextView name = (TextView) _navigation.getHeaderView(0).findViewById(R.id.user_profile_name);
        TextView pseudo = (TextView) _navigation.getHeaderView(0).findViewById(R.id.user_profile_pseudo);

        if (name != null && pseudo != null) {
            name.setText(userEntity.get_last_name() + " " + userEntity.get_first_name());
            pseudo.setText("@" + userEntity.get_pseudo());
        }
    }

    @Override
    public void populateSpredCasts(List<SpredCastEntity> spredCastEntities) {
        ViewPagerAdapter.TabFragment fragment = _viewPagerAdapter.getItem(0);
        fragment.populateSpredCasts(spredCastEntities);
        fragment = _viewPagerAdapter.getItem(1);
        fragment.populateSpredCasts(spredCastEntities);
    }

    @Override
    public void cancelRefresh() {
        ViewPagerAdapter.TabFragment fragment = _viewPagerAdapter.getItem(0);
        fragment.cancelRefresh();
        fragment = _viewPagerAdapter.getItem(1);
        fragment.cancelRefresh();
        fragment = _viewPagerAdapter.getItem(2);
        fragment.cancelRefresh();
    }

    @Override
    public void getSpredCasts() {
        _homePresenter.getSpredCasts();
    }

    @Override
    public void getAbo() {
        _homePresenter.getAbo();
    }

    @Override
    public void setAbo(List<UserEntity> followingUserEntity) {
        ViewPagerAdapter.TabFragment fragment = _viewPagerAdapter.getItem(2);
        fragment.populateAbo(followingUserEntity);
    }
}
