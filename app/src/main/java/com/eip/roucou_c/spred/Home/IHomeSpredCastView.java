package com.eip.roucou_c.spred.Home;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eip.roucou_c.spred.Entities.SpredCastEntity;

import java.util.List;

/**
 * Created by roucou_c on 23/11/2016.
 */
public interface IHomeSpredCastView {

    void populateSpredCasts(List<SpredCastEntity> spredCastEntities);

    void cancelRefresh();

    void getImageProfile(String url, ImageView photo);

    void IsRemind(String id, LinearLayout reminder);
}
