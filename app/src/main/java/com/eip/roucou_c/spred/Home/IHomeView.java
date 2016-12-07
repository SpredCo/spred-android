package com.eip.roucou_c.spred.Home;

import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;

import java.util.List;

/**
 * Created by roucou_c on 14/09/2016.
 */
public interface IHomeView {
    void setProfile(UserEntity userEntity);

    void populateSpredCasts(List<SpredCastEntity> spredCastEntities);

    void cancelRefresh();

    void getSpredCasts();

    void getAbo();

    void setAbo(List<UserEntity> followingUserEntity);
}
