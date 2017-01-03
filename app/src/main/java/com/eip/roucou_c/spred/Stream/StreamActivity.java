package com.eip.roucou_c.spred.Stream;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.CastTokenEntity;
import com.eip.roucou_c.spred.Entities.ChatEntity;
import com.eip.roucou_c.spred.Entities.QuestionEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Home.HomeActivity;
import com.eip.roucou_c.spred.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by roucou_c on 20/12/2016.
 */

public class StreamActivity extends AppCompatActivity implements IStreamView, View.OnClickListener{

    private Manager _manager;
    private StreamPresenter _streamPresenter;
    private SpredCastEntity _spredCast;
    private CastTokenEntity _castToken;
    private ServiceWeb _serviceWeb;
    private UserEntity _userEntity;

    private ViewPager _viewPager;
    private ViewPagerAdapter _viewPagerAdapter;
    private DisplayImageOptions _displayImageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stream);

            _manager = Manager.getInstance(getApplicationContext());
            TokenEntity tokenEntity = _manager._tokenManager.select();
            _streamPresenter = new StreamPresenter(this, _manager, tokenEntity);

            _spredCast = (SpredCastEntity) getIntent().getSerializableExtra("spredCast");
            _userEntity = (UserEntity) getIntent().getSerializableExtra("userEntity");

            if (_spredCast == null) {
                this.finish();
            }

            _streamPresenter.getCastToken(_spredCast.get_id());

            _viewPager = (ViewPager) findViewById(R.id.viewpager);

            _viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            _viewPagerAdapter.addTab(ViewPagerAdapter.TabFragment.newInstance(1, this, _userEntity, _spredCast));
            _viewPagerAdapter.addTab(ViewPagerAdapter.TabFragment.newInstance(2, this, _userEntity, _spredCast));
            _viewPagerAdapter.addTab(ViewPagerAdapter.TabFragment.newInstance(3, this, _userEntity, _spredCast));
            _viewPager.setOffscreenPageLimit(3);
            _viewPager.setAdapter(_viewPagerAdapter);

            _viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            setTitle("Chat");
                            break;
                        case 1:
                            setTitle("Stream");
                            break;
                        case 2:
                            setTitle("Questions");
                            break;

                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        HomeActivity.SSLCertificateHandler.nuke();
        _displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setCastToken(CastTokenEntity castTokenEntity) {
        _castToken = castTokenEntity;

        this._serviceWeb = ServiceWeb.getInstance(this, castTokenEntity, this);

        ViewPagerAdapter.TabFragment tabFragment = _viewPagerAdapter.getItem(1);
        tabFragment.setUpWebView(castTokenEntity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stream_chat_send:
                break;
        }
    }

    @Override
    public void sendMessage(String message) {
        this._serviceWeb.sendMessage(message);
    }

    @Override
    public void setMessage(ChatEntity chatEntity) {
        ViewPagerAdapter.TabFragment tabFragment = _viewPagerAdapter.getItem(0);

        tabFragment.setMessage(chatEntity);
    }

    @Override
    public void sendQuestion(String question) {
        this._serviceWeb.sendQuestion(question);
    }


    @Override
    public void updateQuestions(QuestionEntity questionEntity) {
        ViewPagerAdapter.TabFragment tabFragment = _viewPagerAdapter.getItem(2);

        tabFragment.updateQuestions(questionEntity);
    }

    @Override
    public void downQuestion(int id) {
        this._serviceWeb.downQuestion(id);
    }

    @Override
    public void upQuestion(int id) {
        this._serviceWeb.upQuestion(id);
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
    public void getImageProfile(String url, ImageView profile) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(url, profile, _displayImageOptions);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();


        ViewPagerAdapter.TabFragment fragment = _viewPagerAdapter.getItem(1);

        fragment.stopWebviewLoading();

        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
