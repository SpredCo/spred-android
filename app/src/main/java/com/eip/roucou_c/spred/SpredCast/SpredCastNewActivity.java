package com.eip.roucou_c.spred.SpredCast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SpredCast.Tokenfield.ContactsCompletionView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by roucou_c on 19/11/2016.
 */
public class SpredCastNewActivity extends AppCompatActivity implements ISpredCastNewView, ISpredCastView{

    private SpredCastPresenter _spredCastPresenter;
    private UserEntity _userEntity;
    private Calendar _calendar;

    PagerAdapter mSectionsPagerAdapter;

    private SpredCastViewPager mViewPager;
    ImageButton mNextBtn;
    Button _cancelBtn, mFinishBtn;

    ImageView zero, one, two;
    ImageView[] indicators;


    int page = 0;   //  to track page position
    /**
     * Step 1
     */
    private String _sredcast_name;
    private String _sredcast_description;
    private List<String> _sredcast_tags;

    /**
     * Step 2
     */
    private boolean _spredcast_now;
    private Date _spredcast_date;
    private Date _spredcast_time;
    private int _spredcast_duration;

    /**
     * Step 3
     */
    private boolean _spredcast_isPrivate;
    private String _spredcast_user_capacity;
    private ArrayList<String> _spredcast_membersList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        Manager _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _spredCastPresenter = new SpredCastPresenter(null, this, _manager, tokenEntity);

        _userEntity = (UserEntity) getIntent().getSerializableExtra("userEntity");

        _calendar = Calendar.getInstance();

        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        mNextBtn = (ImageButton) findViewById(R.id.intro_btn_next);
        _cancelBtn = (Button) findViewById(R.id.intro_btn_cancel);
        mFinishBtn = (Button) findViewById(R.id.intro_btn_finish);

        zero = (ImageView) findViewById(R.id.intro_indicator_0);
        one = (ImageView) findViewById(R.id.intro_indicator_1);
        two = (ImageView) findViewById(R.id.intro_indicator_2);


        indicators = new ImageView[]{zero, one, two};

        mViewPager = (SpredCastViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 2) {
                    PagerAdapter.PlaceholderFragment fragment = mSectionsPagerAdapter.getItem(position);
                    ContactsCompletionView membersView = fragment.get_membersView();
                    membersView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            _spredCastPresenter.searchPartialPseudo(String.valueOf(s).replaceAll(";", "").trim());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }

            @Override
            public void onPageSelected(int position) {

                page = position;

                updateIndicators(page);

                mNextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setPagingEnabled(false);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePager(page)) {
                    page += 1;
                    mViewPager.setCurrentItem(page, true);
                }
            }
        });
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePager(page)) {
                    _spredCastPresenter.createSpredCast();
                }
            }
        });
        _cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPostSpredCast();
            }
        });
    }

    private boolean validatePager(int page) {
        PagerAdapter.PlaceholderFragment fragment = mSectionsPagerAdapter.getItem(page);

        boolean isError = false;

        try {
            switch (fragment.getStep()) {
                case "1":
                    _sredcast_name = fragment.get_spredcast_name().getText().toString();
                    _sredcast_description = fragment.get_spredcast_description().getText().toString();
                    _sredcast_tags = fragment.get_spredcast_tagsList();
                    isError = _spredCastPresenter.checkNewSpredCastStep1(_sredcast_name, _sredcast_description, _sredcast_tags);
                    break;
                case "2":
                    _spredcast_now = fragment.get_spredcast_now().isChecked();
                    _spredcast_date = fragment.get_spredcast_date();
                    _spredcast_time = fragment.get_spredcast_time();
                    _spredcast_duration = fragment.get_spredcast_duration_int();

                    isError = _spredCastPresenter.checkNewSpredCastStep2(_spredcast_now, _spredcast_date, _spredcast_time, _spredcast_duration);
                    break;
                case "3":
                    _spredcast_isPrivate = fragment.get_spredcast_private().isChecked();
                    _spredcast_user_capacity = fragment.get_spredcast_user_capacity().getText().toString();
                    _spredcast_membersList = fragment.get_spredcast_membersList();

                    isError = _spredCastPresenter.checkNewSpredCastStep3(_spredcast_isPrivate, _spredcast_user_capacity, _spredcast_membersList);
                    break;
            }
        }
        catch (NullPointerException e) {
            return false;

        }
        return isError;
    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    @Override
    public void populateSearchPseudo(List<UserEntity> userEntities) {
        PagerAdapter.PlaceholderFragment fragment = mSectionsPagerAdapter.getItem(page);

        List<String> tmpMembers = new ArrayList<>();

        ArrayList<String> spredcast_membersList = fragment.get_spredcast_membersList();
        if (spredcast_membersList != null) {
            for (UserEntity userEntity : userEntities) {
                boolean insert = true;
                if (Objects.equals(_userEntity.get_pseudo(), userEntity.get_pseudo())) {
                    insert = false;
                }
                else {
                    for (String member : spredcast_membersList) {
                        if (Objects.equals(member, userEntity.get_pseudo())) {
                            insert = false;
                        }
                    }
                    if (insert && !tmpMembers.contains(userEntity.get_pseudo())) {
                        tmpMembers.add(userEntity.get_pseudo());
                    }
                }
            }
        }
        fragment.get_adapterMembers().clear();
        fragment.get_adapterMembers().addAll(tmpMembers);
    }

    @Override
    public void populateSpredCasts(List<SpredCastEntity> spredCastEntities) {

    }

    @Override
    public void cancelRefresh() {

    }

    @Override
    public Boolean getSpredCastIsPrivate() {
        return _spredcast_isPrivate;
    }

    @Override
    public Date getSpredCastDate() {
        return _spredcast_date;
    }

    @Override
    public Date getSpredCastTime() {
        return _spredcast_time;
    }

    @Override
    public int getSpredCastDuration() {
        return _spredcast_duration;
    }

    @Override
    public List<String> getSpredCastMembers() {
        return _spredcast_membersList;
    }

    @Override
    public String getSpredCastUserCapacity() {
        return _spredcast_user_capacity;
    }

    @Override
    public void setErrorName(int resId) {
        PagerAdapter.PlaceholderFragment fragment = mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment.get_spredcast_name() != null) {
            fragment.get_spredcast_name().setError(resId == 0 ? null : getString(resId));
        }
    }

    @Override
    public void setErrorDescription(int resId) {
        PagerAdapter.PlaceholderFragment fragment = mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment.get_spredcast_description() != null) {
            fragment.get_spredcast_description().setError(resId == 0 ? null : getString(resId));
        }
    }

    @Override
    public void setErrorMembersList(int resId) {
        PagerAdapter.PlaceholderFragment fragment = mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());

        View view = fragment.getView();
        if (view != null) {
            TextInputLayout spredcast_members_textInputLayout = (TextInputLayout) view.findViewById(R.id.spredcast_members_textInputLayout);

            spredcast_members_textInputLayout.setError(resId == 0 ? null : getString(resId));
        }
    }

    @Override
    public void setErrorTime(int resId) {
        PagerAdapter.PlaceholderFragment fragment = mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());

        View view = fragment.getView();

        if (view != null) {
            TextInputLayout spredcast_time_textInputLayout = (TextInputLayout) view.findViewById(R.id.spredcast_time_textInputLayout);

            if (spredcast_time_textInputLayout != null) {
                spredcast_time_textInputLayout.setError(resId == 0 ? null : getString(resId));
            }
        }

    }

    @Override
    public Calendar getCalendar() {
        return _calendar;
    }

    @Override
    public String getSpredCastName() {
        return _sredcast_name;
    }

    @Override
    public String getSpredCastDescription() {
        return _sredcast_description;
    }

    @Override
    public List<String> getSpredCastTags() {
        return _sredcast_tags;
    }

    @Override
    public Boolean getSpredCastIsNow() {
        return _spredcast_now;
    }

    @Override
    public void finishPostSpredCast() {
        this.finish();
    }
}
