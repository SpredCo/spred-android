package com.eip.roucou_c.spred.SpredCast;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TagEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by roucou_c on 07/12/2016.
 */
public class SpredCastDetailsActivity extends AppCompatActivity implements ISpredCastDetailsView{

    private Manager _manager;
    private SpredCastPresenter _spredCastPresenter;
    private SpredCastEntity _spredCast;
    private Toolbar _toolbar;
    private TextView _spredCast_details_name;
    private TextView _spredCast_details_descritpion;
    private TextView _spredCast_details_tags;
    private TextView _spredCast_details_creator_pseudo;
    private TextView _spredCast_details_creator_name;
    private ImageView _spredCast_details_creator_photo;
    private TextView _spredCast_details_date;
    private TextView _spredCast_details_time;
    private TextView _spredCast_details_timelapse;
    private TextView _spredCast_details_members;
    private TextView _spredCast_details_private;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spredcast_details);

        _manager = Manager.getInstance(getApplicationContext());

        TokenEntity tokenEntity = _manager._tokenManager.select();
        _spredCastPresenter = new SpredCastPresenter(null, null, this, null, _manager, tokenEntity);

        _spredCast = (SpredCastEntity) getIntent().getSerializableExtra("spredCast");

        _spredCast_details_name = (TextView) findViewById(R.id.spredcast_details_name);
        _spredCast_details_descritpion = (TextView) findViewById(R.id.spredcast_details_description);
        _spredCast_details_tags = (TextView) findViewById(R.id.spredcast_details_tags);
        _spredCast_details_creator_pseudo = (TextView) findViewById(R.id.spredcast_details_creator_pseudo);
        _spredCast_details_creator_name = (TextView) findViewById(R.id.spredcast_details_creator_name);
        _spredCast_details_creator_photo = (ImageView) findViewById(R.id.spredcast_details_creator_photo);
        _spredCast_details_date = (TextView) findViewById(R.id.spredcast_details_date);
        _spredCast_details_time = (TextView) findViewById(R.id.spredcast_details_time);
        _spredCast_details_timelapse = (TextView) findViewById(R.id.spredcast_details_timelapse);
        _spredCast_details_members = (TextView) findViewById(R.id.spredcast_details_members);
        _spredCast_details_private = (TextView) findViewById(R.id.spredcast_details_private);

        this.populateSpredCast(_spredCast);
    }

    private void populateSpredCast(SpredCastEntity spredCastEntity) {

        _spredCast_details_name.setText(spredCastEntity.get_name());
        _spredCast_details_descritpion.setText(spredCastEntity.get_description());
        UserEntity userEntity = spredCastEntity.get_creator();
        String pseudo = "@"+userEntity.get_pseudo();
        String name = userEntity.get_last_name()+" "+userEntity.get_first_name();

        if (spredCastEntity.get_tags() != null && !spredCastEntity.get_tags().isEmpty()) {
            String tags = "";
            for (TagEntity tagEntity : spredCastEntity.get_tags()) {
                tags += "#" + tagEntity.get_name() + ", ";
            }
            tags = tags.substring(0, tags.length() - 2);
            _spredCast_details_tags.setText(tags);
        }

        _spredCast_details_creator_pseudo.setText(pseudo);
        _spredCast_details_creator_name.setText(name);

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE);

            Date date = format.parse(spredCastEntity.get_date());

            cal.setTime(date);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            _spredCast_details_date.setText(dateFormat.format(date));
            SimpleDateFormat hour_min = new SimpleDateFormat("hh:mm", Locale.FRANCE);
            _spredCast_details_time.setText(hour_min.format(date));

            Date now = new Date();
            long diff = now.getTime() - date.getTime();

            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

            _spredCast_details_timelapse.setText(String.valueOf(minutes) + " min");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String nbMembers = String.valueOf(spredCastEntity.get_members().size())+" personnes participerons";
        _spredCast_details_members.setText(nbMembers);
        if (!spredCastEntity.is_public()) {
            ImageView spredcast_details_drawable_private = (ImageView) findViewById(R.id.spredcast_details_drawable_private);
            spredcast_details_drawable_private.setImageDrawable(getDrawable(R.drawable.ic_lock_outline_black_24dp));
            TextView spredcast_details_private = (TextView) findViewById(R.id.spredcast_details_private);
            spredcast_details_private.setText("Ce SpredCast est privé");
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Détails du SpredCast");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
