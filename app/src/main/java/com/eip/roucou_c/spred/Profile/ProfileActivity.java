package com.eip.roucou_c.spred.Profile;

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
import android.widget.TextView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by roucou_c on 27/09/2016.
 */
public class ProfileActivity extends AppCompatActivity implements IProfileView, View.OnClickListener, View.OnFocusChangeListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _profilePresenter = new ProfilePresenter(this, _manager, tokenEntity);


        _userEntityProfile = (UserEntity) getIntent().getSerializableExtra("userEntityProfile");

        Log.d("followingProfile", String.valueOf(_userEntityProfile.get_following()));

        changeStep("profile");

        if (_userEntityProfile != null) {
            Log.d("_userEntityProfile", _userEntityProfile.get_email());
            _ownUser = false;
            this.populateProfile(_userEntityProfile);
        }
        else {
            _ownUser = true;
        }
        _profilePresenter.getProfile(_ownUser);

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

                Log.d("_ownUser", String.valueOf(_ownUser));
                _profile_edit = (ImageView) findViewById(R.id.profile_edit);
                if (_ownUser) {
                    _profile_edit.setOnClickListener(this);

                    getSupportActionBar().setTitle(getString(R.string.profile_title));
                }
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
                    List<UserEntity> followingList = _userEntity.get_followingUserEntity();
                    boolean contain = false;
                    for (UserEntity userEntity : followingList) {
                        if (Objects.equals(userEntity.get_id(), _userEntityProfile.get_id())) {
                            contain = true;
                        }
                    }
                    _profilePresenter.follow(_userEntityProfile.get_id(), !contain);
                }
                break;
            case R.id.profile_edit_submit:
                _profilePresenter.onSaveClicked();
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
        _profilePresenter.getProfile(false);
    }

    @Override
    public void setUserEntity(UserEntity userEntity) {

        _userEntity = userEntity;

        if (!_ownUser) {
            boolean contain = false;

            for (UserEntity userEntityTmp : userEntity.get_followingUserEntity()) {
                if (Objects.equals(userEntityTmp.get_id(), _userEntityProfile.get_id())) {
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
}
