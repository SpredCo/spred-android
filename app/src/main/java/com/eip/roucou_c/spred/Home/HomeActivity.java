package com.eip.roucou_c.spred.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Home.TabLayout.ViewPagerAdapter;
import com.eip.roucou_c.spred.Inbox.InboxActivity;
import com.eip.roucou_c.spred.Profile.ProfileActivity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.ServiceGeneratorApi;
import com.eip.roucou_c.spred.SignIn.SignInActivity;
import com.eip.roucou_c.spred.SpredCast.*;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.facebook.share.internal.ShareConstants.IMAGE_URL;

/**
 * Created by roucou_c on 09/09/2016.
 */
public class HomeActivity extends AppCompatActivity implements IHomeView, ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {

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


    private Client client;
    private CompletionHandler completionHandler;
    private MaterialSearchBar searchBar;
    private RecyclerView _search_recycler_view;
    private SearchAdapter _search_adapter;
    private CoordinatorLayout _search_coordinatorLayout;
    private DisplayImageOptions _displayImageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

//        if (savedInstanceState == null) {

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        if (tokenEntity == null) {
            _userEntity = null;
        }

        _homePresenter = new HomePresenter(this, _manager, tokenEntity);

        _tabLayout = (TabLayout) findViewById(R.id.tabs);
        _viewPager = (ViewPager) findViewById(R.id.viewpager);

        _viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        _viewPagerAdapter.addTab(ViewPagerAdapter.TabFragment.newInstance("1", this, tokenEntity == null), "En cours");
        _viewPagerAdapter.addTab(ViewPagerAdapter.TabFragment.newInstance("2", this, tokenEntity == null), "Prévus");
        if (tokenEntity != null){
            _viewPagerAdapter.addTab(ViewPagerAdapter.TabFragment.newInstance("3", this, false), "Abonnements");
            _viewPager.setOffscreenPageLimit(3);
        }
        else {
            _viewPager.setOffscreenPageLimit(2);
        }

        _viewPager.setAdapter(_viewPagerAdapter);

        _tabLayout.setupWithViewPager(_viewPager);

        initSearch();

        if (_userEntity == null)
        {
            _homePresenter.getProfile();
            setHeaderNavigation(null);
        }

        SSLCertificateHandler.nuke();
        _displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    private void initSearch() {
        _search_recycler_view = (RecyclerView) findViewById(R.id.search_recyclerView);
        _search_coordinatorLayout = (CoordinatorLayout) findViewById(R.id.search_coordinatorLayout);
        _search_adapter = new SearchAdapter(this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        _search_recycler_view.setLayoutManager(mLayoutManager);
        _search_recycler_view.setAdapter(_search_adapter);

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);

        client = new Client("KGZYQKI2SD", "a8583e100dbd3bb6e5a64d76462d1f5b");

        final Index index = client.initIndex("global");

        EditText searchEdit = (EditText) searchBar.findViewById(com.mancj.materialsearchbar.R.id.mt_editText);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    Query query = new Query(String.valueOf(charSequence));
                    index.searchAsync(query, completionHandler);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    _search_coordinatorLayout.setVisibility(View.GONE);
                }
            }
        });

        completionHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                try {
                    Log.d("json", String.valueOf(content.getJSONArray("hits")));

                    JSONArray jsonArray = content.getJSONArray("hits");
                    ArrayList<String> listdata = new ArrayList<String>();
                    if (jsonArray != null) {
                        for (int i=0;i<jsonArray.length();i++){
                            listdata.add(jsonArray.getString(i));
                        }
                    }
                    if (listdata.size() != 0) {
                        _search_coordinatorLayout.setVisibility(View.VISIBLE);
                    }
                    _search_adapter.set_searchList(listdata);
                    _search_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
//        _homePresenter.getAbo();
//        _homePresenter.getSpredCasts(1);
    }

    @Override
    protected void onStop() {
        if (_drawerLayout != null) {
            _drawerLayout.closeDrawers();
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search, menu);
//        // Retrieve the SearchView and plug it into SearchManager
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
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

            if (_userEntity == null) {
                _navigation.inflateMenu(R.menu.navigation_deco);
            }

            _navigation.inflateHeaderView(R.layout.navigation_header);
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
            case R.id.navigation_signIn:
                this.finish();
                Intent intent4 = new Intent(this, SignInActivity.class);
                startActivity(intent4);
                break;
            case R.id.navigation_signUp:
                this.finish();
                Intent intent5 = new Intent(this, SignInActivity.class);
                startActivity(intent5);
                break;

        }
        return true;
    }

    @Override
    public void setProfile(UserEntity userEntity) {
        _userEntity = userEntity;
        this.setHeaderNavigation(userEntity);
        _navigation.getMenu().clear();
        _navigation.inflateMenu(R.menu.navigation);
        _navigation.setNavigationItemSelectedListener(this);
    }

    @Override
    public void populateSpredCasts(List<SpredCastEntity> spredCastEntities, int state) {
        ViewPagerAdapter.TabFragment fragment;
        if (state == 1) {
            fragment = _viewPagerAdapter.getItem(0);
            fragment.populateSpredCasts(spredCastEntities);
        }
        else if (state == 0) {
            fragment = _viewPagerAdapter.getItem(1);
            fragment.populateSpredCasts(spredCastEntities);
        }
    }

    @Override
    public void cancelRefresh() {
        ViewPagerAdapter.TabFragment fragment = _viewPagerAdapter.getItem(0);
        fragment.cancelRefresh();
        fragment = _viewPagerAdapter.getItem(1);
        fragment.cancelRefresh();
        if (_userEntity != null) {
            fragment = _viewPagerAdapter.getItem(2);
        }
        fragment.cancelRefresh();
    }

    @Override
    public void getSpredCasts(int state) {
        _homePresenter.getSpredCasts(state);
    }

    @Override
    public void getAbo() {
        _homePresenter.getAbo();
    }

    @Override
    public void setAbo(List<FollowEntity> followEntities) {
        ViewPagerAdapter.TabFragment fragment = _viewPagerAdapter.getItem(2);
        fragment.populateAbo(followEntities);
    }

    @Override
    public void onSearchStateChanged(boolean b) {
    }

    @Override
    public void onSearchConfirmed(CharSequence charSequence) {
    }

    @Override
    public void onButtonClicked(int i) {
        switch (i) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                _drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public void getSpredCastsAndShow(String url) {
        _homePresenter.getSpredCastsAndShow(url);
    }

    @Override
    public void getUserAndShow(String objectID) {
        _homePresenter.getUserAndShow(objectID);

    }

    @Override
    public void startProfileActivity(UserEntity userEntity) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userEntityProfile", userEntity);
        this.startActivity(intent);
    }

    @Override
    public void startSpredCastDetailActivity(SpredCastEntity spredCastEntity) {
        Intent intent = new Intent(this, SpredCastDetailsActivity.class);
        intent.putExtra("spredCast", spredCastEntity);
        this.startActivity(intent);
    }

    @Override
    public void startSpredCastByTagActivity(String tag_name) {
        Intent intent = new Intent(this, SpredCastByTagActivity.class);
        intent.putExtra("tag_name", tag_name);
        this.startActivity(intent);
    }

    public void setHeaderNavigation(UserEntity userEntity) {
        TextView name = (TextView) _navigation.getHeaderView(0).findViewById(R.id.user_profile_name);
        TextView pseudo = (TextView) _navigation.getHeaderView(0).findViewById(R.id.user_profile_pseudo);
        final ImageView profile = (ImageView) _navigation.getHeaderView(0).findViewById(R.id.photo_profile);

        String url_profile = "https://spred.tv/img/profile.jpg";

        if (userEntity != null){
            if (name != null && pseudo != null) {
                name.setText(userEntity.get_last_name() + " " + userEntity.get_first_name());
                pseudo.setText("@" + userEntity.get_pseudo());
            }

            String url = _userEntity.get_picture_url().contains("http") ? _userEntity.get_picture_url() : "https://"+ServiceGeneratorApi.API_BASE_URL+_userEntity.get_picture_url();

            url_profile = url;
        }

        this.getImageProfile(url_profile, profile);
    }

    public static class SSLCertificateHandler {

        /**
         * Enables https connections
         */
        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                        return myTrustedAnchors;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                } };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void getImageProfile(String url, ImageView photo) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(url, photo, _displayImageOptions);
    }

    @Override
    public void isRemind(String id, LinearLayout reminder) {
        _homePresenter.isRemind(id, reminder);
    }
}
