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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Home.TabLayout.ViewPagerAdapter;
import com.eip.roucou_c.spred.Profile.ProfileActivity;
import com.eip.roucou_c.spred.R;

/**
 * Created by roucou_c on 09/09/2016.
 */
public class HomeActivity extends AppCompatActivity implements IHomeView, ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private Manager _manager;
    private HomePresenter _homePresenter;

    private Toolbar _toolbar;
    private TabLayout _tabLayout;
    private ViewPager _viewPager;
    private ViewPagerAdapter _viewPagerAdapter;
    private TabLayout.Tab _home;
    private TabLayout.Tab _abo;
    private DrawerLayout _drawerLayout;
    private ActionBarDrawerToggle _drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        _manager = Manager.getInstance(getApplicationContext());

        _homePresenter = new HomePresenter(this, _manager);

        _tabLayout = (TabLayout) findViewById(R.id.tabs);
        _viewPager = (ViewPager) findViewById(R.id.viewpager);

        _viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        _viewPager.setAdapter(_viewPagerAdapter);

        _home = _tabLayout.newTab();
        _abo = _tabLayout.newTab();

        _tabLayout.addTab(_home, 0);
        _tabLayout.addTab(_abo, 1);

        _viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(_tabLayout));

        _home.setIcon(R.drawable.ic_home_white_24dp);
        _abo.setIcon(R.drawable.ic_subscriptions_black_24dp);

        _viewPager.addOnPageChangeListener(this);
        _tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

            NavigationView navigation = (NavigationView) findViewById(R.id.navigation_view);
            navigation.inflateHeaderView(R.layout.navigation_header);
            navigation.inflateMenu(R.menu.navigation);
            navigation.setNavigationItemSelectedListener(this);
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                _home.setIcon(R.drawable.ic_home_white_24dp);
                _abo.setIcon(R.drawable.ic_subscriptions_black_24dp);
                break;
            case 1:
                _home.setIcon(R.drawable.ic_home_black_24dp);
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
                intent.putExtra("listType", "invitation");
                startActivity(intent);
                break;
            case R.id.navigation_logout:
                _manager._tokenManager.delete();
                this.finish();
                break;
        }
        return true;
    }
}
