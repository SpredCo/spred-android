package com.eip.roucou_c.spred.SpredCast;

import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TagEntity;

import java.util.List;

/**
 * Created by roucou_c on 19/12/2016.
 */
public interface ISpredCastByTagView {
    void setTag(TagEntity tagEntity);

    void setIsSub(boolean isSub);

    void populateSpredCasts(List<SpredCastEntity> spredCastEntities);

    void cancelRefresh();

}
