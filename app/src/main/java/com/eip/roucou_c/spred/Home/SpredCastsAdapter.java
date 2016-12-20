package com.eip.roucou_c.spred.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TagEntity;
import com.eip.roucou_c.spred.Profile.ProfileActivity;
import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.SpredCast.SpredCastDetailsActivity;
import com.kd.dynamic.calendar.generator.ImageGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by roucou_c on 11/11/2016.
 */
public class SpredCastsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout _spredCast_relativeLayout;
        TextView _name;
        TextView _description;
        TextView _tags;
        TextView _persons;

        String _spredcast_id;

        MyViewHolder(View view) {
            super(view);

            _name = (TextView) view.findViewById(R.id.spredcasts_name);
            _description = (TextView) view.findViewById(R.id.spredcasts_description);
            _tags = (TextView) view.findViewById(R.id.spredcasts_tags);

            _persons = (TextView) view.findViewById(R.id.spredcasts_person);

            _spredCast_relativeLayout = (RelativeLayout) view.findViewById(R.id.spredcasts);
        }

    }

    private class MyViewHolderLive extends MyViewHolder {

        TextView _timelapse;
        ImageView _photo;

        MyViewHolderLive(View view) {
            super(view);
            _timelapse = (TextView) view.findViewById(R.id.spredcasts_timelapse);
            _photo = (ImageView) view.findViewById(R.id.profile_photo);
        }

    }

    private class MyViewHolderCome extends MyViewHolder {
        ImageView _calendar;
        TextView _time;

        MyViewHolderCome(View view) {
            super(view);
            _calendar = (ImageView) view.findViewById(R.id.spredcast_calendar);
            _time = (TextView) view.findViewById(R.id.spredcasts_time);
        }

    }


    public void set_spredCastEntities(List<SpredCastEntity> _spredCastEntities) {
        this._spredCastEntities = _spredCastEntities;
    }

    @Override
    public int getItemViewType(int position) {
        SpredCastEntity spredCastEntity = _spredCastEntities.get(position);

        return spredCastEntity.get_state();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spredcast_single_row_live, parent, false);
            return new MyViewHolderLive(itemView);
        }
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spredcast_single_row_come, parent, false);
            return new MyViewHolderCome(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final SpredCastEntity spredCastEntity = _spredCastEntities.get(position);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE);
        Date date = null;

        if (holder.getItemViewType() == 1) {
            MyViewHolderLive holderLive = (MyViewHolderLive) holder;

            holderLive._spredcast_id = spredCastEntity.get_id();
            holderLive._name.setText(spredCastEntity.get_name());
            holderLive._description.setText(spredCastEntity.get_description());

            holderLive._persons.setText(String.valueOf(spredCastEntity.get_members().size()));

            try {
                date = format.parse(spredCastEntity.get_date());

                Date now = new Date();
                long diff = now.getTime() - date.getTime();

                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

                holderLive._timelapse.setText(String.valueOf(minutes) + " min");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holderLive._photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(_context, ProfileActivity.class);
                    intent.putExtra("userEntityProfile", spredCastEntity.get_creator());
                    _context.startActivity(intent);
                }
            });
        }
        else if (holder.getItemViewType() == 0) {
            MyViewHolderCome holderCome = (MyViewHolderCome) holder;

            holderCome._spredcast_id = spredCastEntity.get_id();
            holderCome._name.setText(spredCastEntity.get_name() + " by @" + spredCastEntity.get_creator().get_pseudo());
            holderCome._description.setText(spredCastEntity.get_description());

            holderCome._persons.setText(String.valueOf(spredCastEntity.get_user_capacity() == 0 ? "illimité" : spredCastEntity.get_user_capacity()));

            Calendar cal = Calendar.getInstance();

            try {
                date = format.parse(spredCastEntity.get_date());
                cal.setTime(date);
                holderCome._calendar.setImageBitmap(_imageGenerator.generateDateImage(cal, R.drawable.empty_calendar));

                SimpleDateFormat hour_min = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                holderCome._time.setText(hour_min.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        if (spredCastEntity.get_tags() != null && !spredCastEntity.get_tags().isEmpty()) {
            String tags = "";
            for (TagEntity tagEntity : spredCastEntity.get_tags()) {
                tags += "#" + tagEntity.get_name() + " ";
            }
            myViewHolder._tags.setText(tags);
            myViewHolder._tags.setVisibility(View.VISIBLE);
        } else {
            myViewHolder._tags.setVisibility(View.GONE);
        }
        myViewHolder._spredCast_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context,SpredCastDetailsActivity.class);
                intent.putExtra("spredCast", spredCastEntity);
                _context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        int item = 0;
        if (_spredCastEntities != null) {
            for (SpredCastEntity spredCastEntity : _spredCastEntities) {
                if (_state == spredCastEntity.get_state() || _state == 2)
                    ++item;
            }
        }
        return item;
    }
}
