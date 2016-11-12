package com.eip.roucou_c.spred.SpredCast;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.SpredCast.Tokenfield.ContactsCompletionView;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SpredCast.Tokenfield.TagsView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by roucou_c on 07/11/2016.
 */
public class SpredCastActivity extends AppCompatActivity implements ISpredCastView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, DatePickerDialog.OnDateSetListener, TokenCompleteTextView.TokenListener<String>, TextWatcher {

    private Manager _manager;
    private SpredCastPresenter _spredCastPresenter;
    private UserEntity _userEntity;
    private Toolbar _toolbar;
    private String _curentStep;
    private SwipeRefreshLayout _spredCast_swipeRefreshLayout;
    private RecyclerView _spredCast_recycler_view;
    private FloatingActionButton _spredCast_floatActionButton;
    private Menu _menu;
    private MaterialEditText _spredcast_name;
    private MaterialEditText _spredcast_description;
    private TextInputEditText _spredcast_date;
    private Calendar _calendar;
    private TextInputEditText _spredcast_time;
    private TextInputEditText _spredcast_duration;
    private FilteredArrayAdapter<String> _adapterTags;
    private List<String> _tagsList;
    private TagsView _tagView;
    private SwitchCompat _spredcast_isPrivate;
    private ArrayList<String> _followList;
    private ContactsCompletionView _membersView;
    private FilteredArrayAdapter<String> _adapterMembers;
    private MaterialEditText _spredcast_user_capacity;
    private ArrayList<String> _spredcast_membersList;
    private SpredCastsAdapter _spredCast_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _spredCastPresenter = new SpredCastPresenter(this, _manager, tokenEntity);

        _userEntity = (UserEntity) getIntent().getSerializableExtra("userEntity");

        _calendar = Calendar.getInstance();

        changeStep("spredcast");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Objects.equals(_curentStep, "spredcast_new")) {
            getMenuInflater().inflate(R.menu.validate, menu);
        }
        return true;
    }
    public void changeStep(String step){
        invalidateOptionsMenu();
        _curentStep = step;

        switch (step) {
            case "spredcast":
                setContentView(R.layout.spredcast);

                _spredCast_swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
                _spredCast_swipeRefreshLayout.setOnRefreshListener(this);
                _spredCast_recycler_view = (RecyclerView) findViewById(R.id.spredcast_recycler_view);

                _spredCast_adapter = new SpredCastsAdapter(this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                _spredCast_recycler_view.setLayoutManager(mLayoutManager);
                _spredCast_recycler_view.setItemAnimator(new DefaultItemAnimator());
                _spredCast_recycler_view.setAdapter(_spredCast_adapter);

                _spredCast_floatActionButton = (FloatingActionButton) findViewById(R.id.spredcast_floatActionButton);
                _spredCast_floatActionButton.setOnClickListener(this);

                _spredCastPresenter.getSpredCast();

                break;
            case "spredcast_new":
                setContentView(R.layout.spredcast_new);

                _spredcast_name = (MaterialEditText) findViewById(R.id.spredcast_name);
                _spredcast_description = (MaterialEditText) findViewById(R.id.spredcast_description);

                _spredcast_date = (TextInputEditText) findViewById(R.id.spredcast_date);
                _spredcast_date.setOnClickListener(this);
                _spredcast_date.setText(String.format("%02d", _calendar.get(Calendar.DAY_OF_MONTH))+"/"+ String.format("%02d", _calendar.get(Calendar.MONTH))+"/"+ String.format("%02d", _calendar.get(Calendar.YEAR)));

                _spredcast_time = (TextInputEditText) findViewById(R.id.spredcast_time);
                _spredcast_time.setOnClickListener(this);
                _spredcast_time.setText(String.format("%02d", _calendar.get(Calendar.HOUR_OF_DAY))+"h"+ String.format("%02d", _calendar.get(Calendar.MINUTE)));

                _spredcast_duration = (TextInputEditText) findViewById(R.id.spredcast_duration);
                _spredcast_duration.setOnClickListener(this);
                _spredcast_duration.setText("01h00");

                this.initTag();

                _spredcast_isPrivate = (SwitchCompat) findViewById(R.id.spredcast_isPrivate);
                _spredcast_isPrivate.setOnClickListener(this);
                _spredcast_isPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        _membersView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                });

                _spredcast_user_capacity = (MaterialEditText) findViewById(R.id.spredcast_user_capacity);

                this.initMembers();

                break;
        }
    }

    private void initTag() {
        _tagsList = new ArrayList<>();
        List<String> tagsList = new ArrayList<>();
        tagsList.add("tag1");
        tagsList.add("tag2");
        tagsList.add("tag3");
        tagsList.add("tag4");
        tagsList.add("tag5");
        tagsList.add("tag6");
        _adapterTags = new FilteredArrayAdapter<String>(this, R.layout.tokenfield_single_row, tagsList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.tokenfield_single_row, parent, false);
                }

                String tag = getItem(position);
                ((TextView) convertView.findViewById(R.id.name)).setText(tag);
                (convertView.findViewById(R.id.email)).setVisibility(View.GONE);

                return convertView;
            }

            @Override
            protected boolean keepObject(String tag, String mask) {
                mask = mask.toLowerCase();
                return tag.toLowerCase().startsWith(mask);
            }
        };

        _tagView = (TagsView) findViewById(R.id.spredcast_tags);
        _tagView.setAdapter(_adapterTags);
        _tagView.setSplitChar(';');
        _tagView.setTokenListener(new TokenCompleteTextView.TokenListener<String>() {
            @Override
            public void onTokenAdded(String token) {
                _tagsList.add(token);
            }

            @Override
            public void onTokenRemoved(String token) {
                _tagsList.remove(token);
            }
        });
        _tagView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                _tagView.showDropDown();
            }
        });
        _tagView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        _tagView.setThreshold(0);
        _tagView.allowDuplicates(false);
    }

    private void initMembers() {
        _spredcast_membersList = new ArrayList<>();
        _adapterMembers = new FilteredArrayAdapter<String>(this, R.layout.tokenfield_single_row, new ArrayList<String>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.tokenfield_single_row, parent, false);
                }

                String tag = getItem(position);
                ((TextView) convertView.findViewById(R.id.name)).setText(tag);
                (convertView.findViewById(R.id.email)).setVisibility(View.GONE);

                return convertView;
            }

            @Override
            protected boolean keepObject(String tag, String mask) {
                mask = mask.toLowerCase();
                return tag.toLowerCase().startsWith(mask);
            }
        };

        _membersView = (ContactsCompletionView) findViewById(R.id.spredcast_members);
        _membersView.setAdapter(_adapterMembers);
        _membersView.setSplitChar(';');
        _membersView.setTokenListener(this);
        _membersView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                _membersView.showDropDown();
            }
        });
        _membersView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        _membersView.setThreshold(0);
        _membersView.setVisibility(View.GONE);
        _membersView.addTextChangedListener(this);
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
                if (Objects.equals(_curentStep, "spredcast")) {
                    this.finish();
                }
                else if (Objects.equals(_curentStep, "spredcast_new")) {
                    changeStep("spredcast");
                }
                break;
            case R.id.validate:
                _spredCastPresenter.createSpredCast();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spredcast_floatActionButton:
                changeStep("spredcast_new");
                break;
            case R.id.spredcast_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(SpredCastActivity.this, this, _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH),_calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
            case R.id.spredcast_time:
                new TimePickerDialog(SpredCastActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        _spredcast_time.setText(String.format("%02d", hourOfDay) + "h" + String.format("%02d", minute));
                    }
                }, _calendar.get(Calendar.HOUR_OF_DAY), _calendar.get(Calendar.MINUTE), true).show();
                break;
            case R.id.spredcast_duration:
                new TimePickerDialog(SpredCastActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        _spredcast_duration.setText(String.format("%02d", hourOfDay) + "h" + String.format("%02d", minute));
                    }
                }, _calendar.get(Calendar.HOUR_OF_DAY), _calendar.get(Calendar.MINUTE), true).show();
                break;
            case R.id.spredcast_isPrivate:
                break;

        }
    }

    @Override
    public void onRefresh() {
        _spredCastPresenter.getSpredCast();
    }

    @Override
    public void cancelRefresh() {
        _spredCast_swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _spredcast_date.setText(String.format("%02d", dayOfMonth)+"/"+String.format("%02d", monthOfYear)+"/"+year);
    }

    @Override
    public void onTokenAdded(String token) {
        _spredcast_membersList.add(token);
    }

    @Override
    public void onTokenRemoved(String token) {
        _spredcast_membersList.remove(token);
    }

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

    @Override
    public void populateSearchPseudo(List<UserEntity> userEntities) {
        List<String> tmpMembers = new ArrayList<>();

        if (_spredcast_membersList != null) {
            for (UserEntity userEntity : userEntities) {
                boolean insert = true;
                if (Objects.equals(_userEntity.get_pseudo(), userEntity.get_pseudo())) {
                    insert = false;
                }
                else {
                    for (String member : _spredcast_membersList) {
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
        _adapterMembers.clear();
        _adapterMembers.addAll(tmpMembers);
    }

    @Override
    public String getSpredCastName() {
        return _spredcast_name.getText().toString();
    }

    @Override
    public String getSpredCastDescription() {
        return _spredcast_description.getText().toString();
    }

    @Override
    public Boolean getSpredCastIsPrivate() {
        return _spredcast_isPrivate.isChecked();
    }

    @Override
    public Date getSpredCastDate() {
        String date = _spredcast_date.getText().toString();

        String[] split = date.split("/");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]), 0, 0, 0);
        return calendar.getTime();
    }

    @Override
    public Date getSpredCastTime() {
        String time = _spredcast_time.getText().toString();

        String[] split = time.split("h");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(getSpredCastDate());

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));

        return calendar.getTime();
    }

    @Override
    public int getSpredCastDuration() {
        String duration = _spredcast_duration.getText().toString();

        String[] split = duration.split("h");

        return Integer.parseInt(split[0])*60+Integer.parseInt(split[1]);
    }

    @Override
    public List<String> getSpredCastTags() {
        return _tagsList;
    }

    @Override
    public List<String> getSpredCastMembers() {
        return _spredcast_membersList;
    }

    @Override
    public String getSpredCastUserCapacity() {
        return _spredcast_user_capacity.getText().toString();
    }

    @Override
    public void setErrorName(int resId) {
        _spredcast_name.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorDescription(int resId) {
        _spredcast_description.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorMembersList(int resId) {
        TextInputLayout spredcast_members_textInputLayout = (TextInputLayout) findViewById(R.id.spredcast_members_textInputLayout);

        assert spredcast_members_textInputLayout != null;
        spredcast_members_textInputLayout.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorDate(int resId) {
        TextInputLayout spredcast_date_textInputLayout = (TextInputLayout) findViewById(R.id.spredcast_date_textInputLayout);

        assert spredcast_date_textInputLayout != null;
        spredcast_date_textInputLayout.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public void setErrorTime(int resId) {
        TextInputLayout spredcast_time_textInputLayout = (TextInputLayout) findViewById(R.id.spredcast_time_textInputLayout);

        assert spredcast_time_textInputLayout != null;
        spredcast_time_textInputLayout.setError(resId == 0 ? null : getString(resId));
    }

    @Override
    public Calendar getCalendar() {
        return _calendar;
    }

    @Override
    public void populateSpredCasts(List<SpredCastEntity> spredCastEntities) {
        if (_spredCast_adapter != null) {
            _spredCast_adapter.set_spredCastEntities(spredCastEntities);
            _spredCast_adapter.notifyDataSetChanged();
        }
    }
}