package com.eip.roucou_c.spred.Home;

import com.eip.roucou_c.spred.Entities.FollowEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;

import java.util.List;

/**
 * Created by roucou_c on 07/12/2016.
 */
public interface IHomeAboView {
    void populateAbo(List<FollowEntity> followEntities);
}
