package com.eip.roucou_c.spred.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.FollowerEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Home.HomeActivity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.ServiceGeneratorApi;
import com.eip.roucou_c.spred.SpredCast.SpredCastActivity;
import com.eip.roucou_c.spred.SpredCast.SpredCastNewActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by roucou_c on 27/09/2016.
 */
public class ProfileActivity extends AppCompatActivity implements IProfileView, View.OnClickListener, View.OnFocusChangeListener, IFollowersView{

    private Toolbar _toolbar;
    private Manager _manager;
    private ProfilePresenter _profilePresenter;
    private TextView _profile_email_textView;
    private TextView _profile_name_textView;
    private TextView _profile_pseudo_textView;
    private MaterialEditText _profile_edit_email;
    private MaterialEditText _profile_edit_lastName;
    private MaterialEditText _profile_edit_firstName;
    private UserEntity _userEntity;
    private String _curentStep;
    private boolean _ownUser;
    private ImageView _follow;
    private UserEntity _userEntityProfile;
    private ImageView _profile_edit;
    private List<FollowEntity> _followEntities;
    private TextView _profile_nbFollowers;
    private LinearLayout _profile_linearLayout_followers;
    private DisplayImageOptions _displayImageOptions;
    private ImageView _profile_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _profilePresenter = new ProfilePresenter(this, this, _manager, tokenEntity);


        _userEntityProfile = (UserEntity) getIntent().getSerializableExtra("userEntityProfile");

        changeStep("profile");

        if (_userEntityProfile != null) {
            _ownUser = false;
            this.populateProfile(_userEntityProfile);
        }
        else {
            _ownUser = true;
            _profilePresenter.getFollowers();
        }
        _profilePresenter.getProfile(_ownUser);
        _profilePresenter.getFollowing();

        _displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }

    @Override
    public void changeStep(String step) {
        _curentStep = step;
        switch (step) {
            case "profile":
                setContentView(R.layout.profile);

                _profile_email_textView = (TextView) findViewById(R.id.profile_email_textView);
                _profile_name_textView = (TextView) findViewById(R.id.profile_name_textView);
                _profile_pseudo_textView = (TextView) findViewById(R.id.profile_pseudo_textView);

                _profile_nbFollowers = (TextView) findViewById(R.id.profile_nbfollowers);
                _profile_linearLayout_followers = (LinearLayout) findViewById(R.id.profile_linearLayout_followers);
                _profile_linearLayout_followers.setOnClickListener(this);

                _profile_photo = (ImageView) findViewById(R.id.profile_photo);

                if (_userEntity != null) {
                    _profile_edit = (ImageView) findViewById(R.id.profile_edit);
                    _profile_edit.setOnClickListener(this);
                }

                getSupportActionBar().setTitle("Profil");
                _profile_linearLayout_followers.setVisibility(View.GONE);

                _profilePresenter.getProfile(_ownUser);
                break;
            case "editProfile":
                setContentView(R.layout.profile_edit);

                FancyButton  profile_edit_submit = (FancyButton) findViewById(R.id.profile_edit_submit);

                _profile_edit_email = (MaterialEditText) findViewById(R.id.profile_edit_email);
                _profile_edit_lastName = (MaterialEditText) findViewById(R.id.profile_edit_lastname);
                _profile_edit_firstName = (MaterialEditText) findViewById(R.id.profile_edit_firstname);

                _profile_edit_email.setOnFocusChangeListener(this);

                _profile_edit_email.setText(_userEntity.get_email());
                _profile_edit_lastName.setText(_userEntity.get_last_name());
                _profile_edit_firstName.setText(_userEntity.get_first_name());

                if (profile_edit_submit != null) {
                    profile_edit_submit.setOnClickListener(this);
                }

                getSupportActionBar().setTitle(getString(R.string.profile_edit_title));
                break;
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profil");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Objects.equals(_curentStep, "profile")) {
                    this.finish();
                }
                else if (Objects.equals(_curentStep, "editProfile")) {
                    changeStep("profile");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void populateProfile(UserEntity userEntity) {
        _profile_email_textView.setText(userEntity.get_email());
        String name = userEntity.get_last_name()+" "+userEntity.get_first_name();
        _profile_name_textView.setText(name);
        _profile_pseudo_textView.setText(userEntity.get_pseudo());

        String url = userEntity.get_picture_url().contains("http") ? userEntity.get_picture_url() : "https://"+ ServiceGeneratorApi.API_BASE_URL+userEntity.get_picture_url();

        getImageProfile(url, _profile_photo);
    }

    @Override
    public String getEmail() {
        return _profile_edit_email.getText().toString();
    }

    @Override
    public String getFirstName() {
        return _profile_edit_firstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return _profile_edit_lastName.getText().toString();
    }

    @Override
    public void setErrorEmail(int resId) {
        _profile_edit_email.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorFirstName(int resId) {
        _profile_edit_firstName.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorLastName(int resId) {
        _profile_edit_lastName.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public MaterialEditText getMaterialEditTextEmail() {
        return _profile_edit_email;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_edit:
                changeStep("editProfile");
                break;
            case R.id.profile_follow:
                if (_userEntity != null) {
                    boolean contain = false;
                    for (FollowEntity followEntity : _followEntities) {
                        if (Objects.equals(followEntity.get_following().get_id(), _userEntityProfile.get_id())) {
                            contain = true;
                        }
                    }
                    _profilePresenter.follow(_userEntityProfile.get_id(), !contain);
                }
                break;
            case R.id.profile_edit_submit:
                _profilePresenter.onSaveClicked();
                break;
            case R.id.profile_linearLayout_followers:
                Intent intent = new Intent(ProfileActivity.this, FollowersActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.profile_edit_email) {
            _profilePresenter.checkEmail();
        }
    }

    @Override
    public UserEntity getUserEntity() {
        return _userEntity;
    }

    @Override
    public void setError(int resId) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        if (coordinatorLayout != null) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, resId, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void follow(boolean isFollow) {
        if (_follow != null) {
            if (isFollow) {
                _follow.setImageDrawable(getDrawable(R.drawable.ic_star_white_24dp));
            }
            else {
                _follow.setImageDrawable(getDrawable(R.drawable.ic_star_border_white_24dp));
            }
        }
        _profilePresenter.getFollowing();
        _profilePresenter.getProfile(false);
    }

    @Override
    public void setUserEntity(UserEntity userEntity) {

        _userEntity = userEntity;
    }

    @Override
    public void setFollowing(List<FollowEntity> followEntities) {
        _followEntities = followEntities;

        if (!_ownUser) {
            boolean contain = false;

            for (FollowEntity followEntity : followEntities) {
                if (Objects.equals(followEntity.get_following().get_id(), _userEntityProfile.get_id())) {
                    contain = true;
                }
            }
            if (_profile_edit != null) {
                _profile_edit.setVisibility(View.GONE);
                _follow = (ImageView) findViewById(R.id.profile_follow);
                _follow.setOnClickListener(this);
                _follow.setVisibility(View.VISIBLE);
                if (contain) {
                    _follow.setImageDrawable(getDrawable(R.drawable.ic_star_white_24dp));
                }
                else {
                    _follow.setImageDrawable(getDrawable(R.drawable.ic_star_border_white_24dp));
                }
            }

        }
    }

    @Override
    public void setFollowers(List<FollowerEntity> followerEntities) {
        _profile_linearLayout_followers.setVisibility(View.VISIBLE);
        int nb = followerEntities != null ? followerEntities.size() : 0;
        _profile_nbFollowers.setText(String.valueOf(nb));
    }

    public void getImageProfile(String url, ImageView photo) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(url, photo, _displayImageOptions);
    }

}
