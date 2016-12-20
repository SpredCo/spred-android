package com.eip.roucou_c.spred.Home;

import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TagEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;

import java.util.List;

/**
 * Created by roucou_c on 14/09/2016.
 */
public interface IHomeView {
    void setProfile(UserEntity userEntity);

    void populateSpredCasts(List<SpredCastEntity> spredCastEntities, int state);

    void cancelRefresh();

    void getSpredCasts(int state);

    void getAbo();

    void setAbo(List<FollowEntity> followingUserEntity);

    void getSpredCastsAndShow(String url);

    void getUserAndShow(String objectID);

    void startProfileActivity(UserEntity userEntity);

    void startSpredCastDetailActivity(SpredCastEntity spredCastEntity);

    void startSpredCastByTagActivity(String tag_name);
}

