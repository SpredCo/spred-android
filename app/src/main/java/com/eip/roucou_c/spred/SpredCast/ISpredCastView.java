package com.eip.roucou_c.spred.SpredCast;

import android.widget.ImageView;

import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by roucou_c on 07/11/2016.
 */
public interface ISpredCastView {

    void populateSpredCasts(List<SpredCastEntity> spredCastEntities);

    void cancelRefresh();

    void startSpredCastDetailActivity(SpredCastEntity spredCastEntity);

    void getImageProfile(String url, ImageView photo_profile);
}
