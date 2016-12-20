package com.eip.roucou_c.spred.SpredCast;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.eip.roucou_c.spred.Entities.TagEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SpredCast.Tokenfield.ContactsCompletionView;
import com.eip.roucou_c.spred.SpredCast.Tokenfield.TagsView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by roucou_c on 21/11/2016.
 */
class PagerAdapter extends FragmentPagerAdapter {

    List<PlaceholderFragment> fragmentList = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public PlaceholderFragment getItem(int position) {
        PlaceholderFragment fragment = fragmentList.size() > position ? fragmentList.get(position) : null;

        if (fragment == null) {
            String step = String.valueOf((position+1));
            fragment = PlaceholderFragment.newInstance(step);
            fragmentList.add(fragment);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }

    public void populateTags(List<TagEntity> tagEntities) {
        if (fragmentList.size() != 0) {
            fragmentList.get(0).setTags(tagEntities);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private String step;

        /**
         * Step 1
         */
        private MaterialEditText _spredcast_name;
        private MaterialEditText _spredcast_description;
        private List<TagEntity> _spredcast_tagsEntities;
        private TagsView _tagView;
        private FilteredArrayAdapter<TagEntity> _adapterTags;

        /**
         * Step 2
         */
        private Calendar _calendar;
        private SwitchCompat _spredcast_now;
        private TextInputEditText _spredcast_date;
        private TextInputEditText _spredcast_time;
        private TextInputEditText _spredcast_duration;

        /**
         * Step 3
         */
        private SwitchCompat _spredcast_private;
        private MaterialEditText _spredcast_user_capacity;
        private ArrayList<String> _spredcast_membersList;
        private FilteredArrayAdapter<UserEntity> _adapterMembers;
        private ContactsCompletionView _membersView;
        private ArrayList<TagEntity> _tagsEntities;


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(String step) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.step = step;
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
                    rootView = inflater.inflate(R.layout.spredcast_new_step1, container, false);
                    _spredcast_name = (MaterialEditText) rootView.findViewById(R.id.spredcast_name);
                    _spredcast_description = (MaterialEditText) rootView.findViewById(R.id.spredcast_description);

                    this.initTag(rootView);

                    break;
                case "2":
                    rootView = inflater.inflate(R.layout.spredcast_new_step2, container, false);

                    _calendar = Calendar.getInstance();


                    final LinearLayout spredcast_linearLayout_date_time = (LinearLayout) rootView.findViewById(R.id.spredcast_linearLayout_date_time);

                    _spredcast_now = (SwitchCompat) rootView.findViewById(R.id.spredcast_now);
                    _spredcast_now.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            spredcast_linearLayout_date_time.setVisibility(isChecked ? View.GONE: View.VISIBLE);
                        }
                    });
                    _spredcast_date = (TextInputEditText) rootView.findViewById(R.id.spredcast_date);
                    _spredcast_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    _spredcast_date.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear) + "/" + year);
                                }
                            }, _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH), _calendar.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                            datePickerDialog.show();
                        }
                    });
                    _spredcast_date.setText(String.format("%02d", _calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", _calendar.get(Calendar.MONTH)) + "/" + String.format("%02d", _calendar.get(Calendar.YEAR)));

                    _spredcast_time = (TextInputEditText) rootView.findViewById(R.id.spredcast_time);
                    _spredcast_time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    _spredcast_time.setText(String.format("%02d", hourOfDay) + "h" + String.format("%02d", minute));
                                }
                            }, _calendar.get(Calendar.HOUR_OF_DAY), _calendar.get(Calendar.MINUTE), true).show();
                        }
                    });
                    _spredcast_time.setText(String.format("%02d", _calendar.get(Calendar.HOUR_OF_DAY)) + "h" + String.format("%02d", _calendar.get(Calendar.MINUTE)));

                    _spredcast_duration = (TextInputEditText) rootView.findViewById(R.id.spredcast_duration);
                    _spredcast_duration.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    _spredcast_duration.setText(String.format("%02d", hourOfDay) + "h" + String.format("%02d", minute));
                                }
                            }, _calendar.get(Calendar.HOUR_OF_DAY), _calendar.get(Calendar.MINUTE), true).show();
                        }
                    });
                    _spredcast_duration.setText("01h00");


                    break;
                case "3":
                    rootView = inflater.inflate(R.layout.spredcast_new_step3, container, false);

                    _spredcast_private = (SwitchCompat) rootView.findViewById(R.id.spredcast_isPrivate);
                    _spredcast_user_capacity = (MaterialEditText) rootView.findViewById(R.id.spredcast_user_capacity);

                    this.initMembers(rootView);

                    _spredcast_private.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            _spredcast_user_capacity.setVisibility(isChecked ? View.GONE: View.VISIBLE);
                            _membersView.setVisibility(isChecked ? View.VISIBLE: View.GONE);
                        }
                    });

                    break;
            }

            return rootView;
        }

        private void initTag(View view) {
            _spredcast_tagsEntities = new ArrayList<>();
            _tagsEntities = new ArrayList<>();

            _adapterTags = new FilteredArrayAdapter<TagEntity>(this.getContext(), R.layout.tokenfield_single_row, _tagsEntities) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {

                        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                        convertView = l.inflate(R.layout.tokenfield_single_row, parent, false);
                    }

                    TagEntity tagEntity = getItem(position);
                    ((TextView) convertView.findViewById(R.id.name)).setText(tagEntity.get_name());
                    (convertView.findViewById(R.id.email)).setVisibility(View.GONE);

                    return convertView;
                }

                @Override
                protected boolean keepObject(TagEntity tag, String mask) {
                    mask = mask.toLowerCase();
                    return tag.get_name().startsWith(mask);
                }
            };

            _tagView = (TagsView) view.findViewById(R.id.spredcast_tags);
            _tagView.setAdapter(_adapterTags);
            _tagView.setSplitChar(';');
            _tagView.setTokenListener(new TokenCompleteTextView.TokenListener<TagEntity>() {
                @Override
                public void onTokenAdded(TagEntity tagEntity) {
                    _spredcast_tagsEntities.add(tagEntity);
                }

                @Override
                public void onTokenRemoved(TagEntity tagEntity) {
                    _spredcast_tagsEntities.remove(tagEntity);
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

        private void initMembers(View view) {
            _spredcast_membersList = new ArrayList<>();
            _adapterMembers = new FilteredArrayAdapter<UserEntity>(getContext(), R.layout.tokenfield_single_row, new ArrayList<UserEntity>()) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                        convertView = l.inflate(R.layout.tokenfield_single_row, parent, false);
                    }

                    UserEntity userEntity = getItem(position);
                    ((TextView) convertView.findViewById(R.id.name)).setText("@"+userEntity.get_pseudo());
                    (convertView.findViewById(R.id.email)).setVisibility(View.GONE);

                    return convertView;
                }

                @Override
                protected boolean keepObject(UserEntity userEntity, String mask) {
                    mask = mask.toLowerCase();
                    return userEntity.get_pseudo().toLowerCase().startsWith(mask);
                }
            };

            _membersView = (ContactsCompletionView) view.findViewById(R.id.spredcast_members);
            _membersView.setAdapter(_adapterMembers);
            _membersView.setSplitChar(';');
            _membersView.setTokenListener(new TokenCompleteTextView.TokenListener<UserEntity>() {
                @Override
                public void onTokenAdded(UserEntity userEntity) {
                    _spredcast_membersList.add(userEntity.get_id());
                }

                @Override
                public void onTokenRemoved(UserEntity userEntity) {
                    _spredcast_membersList.remove(userEntity.get_id());
                }
            });
            _membersView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    _membersView.showDropDown();
                }
            });
            _membersView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
            _membersView.setThreshold(0);
            _membersView.setVisibility(View.GONE);

        }

        public String getStep() {
            return step;
        }

        public MaterialEditText get_spredcast_name() {
            return _spredcast_name;
        }

        public MaterialEditText get_spredcast_description() {
            return _spredcast_description;
        }

        public List<TagEntity> get_spredcast_tagsEntities() {
            return _spredcast_tagsEntities;
        }

        public SwitchCompat get_spredcast_now() {
            return _spredcast_now;
        }

        public TextInputEditText get_spredcast_date_string() {
            return _spredcast_date;
        }

        public Date get_spredcast_date() {
            String date = _spredcast_date.getText().toString();

            String[] split = date.split("/");
            Calendar calendar = Calendar.getInstance();

            calendar.set(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]), 0, 0, 0);
            return calendar.getTime();
        }


        public TextInputEditText get_spredcast_time_string() {
            return _spredcast_time;
        }

        public Date get_spredcast_time() {
            String time = _spredcast_time.getText().toString();

            String[] split = time.split("h");
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(get_spredcast_date());

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));

            return calendar.getTime();
        }

        public TextInputEditText get_spredcast_duration() {
            return _spredcast_duration;
        }

        public int get_spredcast_duration_int() {
            String duration = _spredcast_duration.getText().toString();

            String[] split = duration.split("h");

            return Integer.parseInt(split[0])*60+Integer.parseInt(split[1]);
        }

        public SwitchCompat get_spredcast_private() {
            return _spredcast_private;
        }

        public MaterialEditText get_spredcast_user_capacity() {
            return _spredcast_user_capacity;
        }

        public ContactsCompletionView get_membersView() {
            return _membersView;
        }

        public ArrayList<String> get_spredcast_membersList() {
            return _spredcast_membersList;
        }

        public void set_spredcast_membersList(ArrayList<String> spredcast_membersList) {
            this._spredcast_membersList = spredcast_membersList;
        }

        public FilteredArrayAdapter<UserEntity> get_adapterMembers() {
            return _adapterMembers;
        }

        public void setTags(List<TagEntity> tagEntities) {
            _tagsEntities.addAll(tagEntities);
            _adapterTags.addAll(tagEntities);
            _adapterTags.notifyDataSetChanged();
        }
    }
}
