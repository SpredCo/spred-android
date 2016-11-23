package com.eip.roucou_c.spred.Home;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.R;
import com.kd.dynamic.calendar.generator.ImageGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by roucou_c on 11/11/2016.
 */
public class SpredCastsAdapter extends RecyclerView.Adapter<SpredCastsAdapter.MyViewHolder> {

    private final int _state;
    private final ImageGenerator _imageGenerator;
    private Context _context;
    private List<SpredCastEntity> _spredCastEntities;
    private IHomeSpredCastView _iHomeSpredCastView;

    public SpredCastsAdapter(IHomeSpredCastView iHomeSpredCastView, int state, Context context) {
        this._iHomeSpredCastView = iHomeSpredCastView;
        this._state = state;
        this._context = context;
        _imageGenerator = new ImageGenerator(context);

        _imageGenerator.setIconSize(60, 60);
        _imageGenerator.setDateSize(25);
        _imageGenerator.setMonthSize(10);
        _imageGenerator.setDatePosition(50);
        _imageGenerator.setMonthPosition(16);
        _imageGenerator.setDateColor(Color.parseColor("#55AA55"));
        _imageGenerator.setMonthColor(Color.WHITE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView _name;
        public TextView _description;
        public TextView _tags;
        public TextView _timelapse;
        public TextView _persons;

        public ImageView _calendar;
        public TextView _time;

        private String _spredcast_id;

        public MyViewHolder(View view) {
            super(view);

            _name = (TextView) view.findViewById(R.id.spredcasts_name);
            _description = (TextView) view.findViewById(R.id.spredcasts_description);
            _tags = (TextView) view.findViewById(R.id.spredcasts_tags);

            _timelapse = (TextView) view.findViewById(R.id.spredcasts_timelapse);
            _persons = (TextView) view.findViewById(R.id.spredcasts_person);

            _calendar = (ImageView) view.findViewById(R.id.spredcast_calendar);
            _time = (TextView) view.findViewById(R.id.spredcasts_time);

            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.spredcasts);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.inbox_conversation:
//                    _IInboxView.conversationSelected(_conversation_id);
//                    break;
//            }
        }
    }

    public void set_spredCastEntities(List<SpredCastEntity> _spredCastEntities) {
        this._spredCastEntities = _spredCastEntities;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (_state == 1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spredcast_single_row_live, parent, false);
        }
        if (_state == 0) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spredcast_single_row_come, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SpredCastEntity spredCastEntity = _spredCastEntities.get(position);
//        if (_state == spredCastEntity.get_state()) {

        if (_state == 1) {
            holder._spredcast_id = spredCastEntity.get_id();
            holder._name.setText(spredCastEntity.get_name());
            holder._description.setText(spredCastEntity.get_description());

            holder._persons.setText(String.valueOf(spredCastEntity.get_members().size()));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE);
            Date date = null;
            try {
                date = format.parse(spredCastEntity.get_date());

                Date now = new Date();
                long diff = now.getTime() - date.getTime();

                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

                holder._timelapse.setText(String.valueOf(minutes) + " min");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (_state == 0) {
            holder._spredcast_id = spredCastEntity.get_id();
            holder._name.setText(spredCastEntity.get_name()+" by @"+spredCastEntity.get_creator().get_pseudo());
            holder._description.setText(spredCastEntity.get_description());

            holder._persons.setText(String.valueOf(spredCastEntity.get_user_capacity() == 0 ? "illimité" : spredCastEntity.get_user_capacity()));

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE);
            Date date = null;

            try {
                date = format.parse(spredCastEntity.get_date());
                cal.setTime(date);
                holder._calendar.setImageBitmap(_imageGenerator.generateDateImage(cal, R.drawable.empty_calendar));

                SimpleDateFormat hour_min = new SimpleDateFormat("hh:mm", Locale.FRANCE);
                holder._time.setText(hour_min.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (spredCastEntity.get_tags() != null && !spredCastEntity.get_tags().isEmpty()) {
            String tags = "";
            for (String tag : spredCastEntity.get_tags()) {
                tags += "#" + tag + " ";
            }
            holder._tags.setText(tags);
            holder._tags.setVisibility(View.VISIBLE);
        } else {
            holder._tags.setVisibility(View.GONE);
        }
//        }
    }

    @Override
    public int getItemCount() {
        int item = 0;
        if (_spredCastEntities != null) {
            for (SpredCastEntity spredCastEntity : _spredCastEntities) {
//                if (_state == spredCastEntity.get_state())
                ++item;
            }
        }
        return item;
    }
}
