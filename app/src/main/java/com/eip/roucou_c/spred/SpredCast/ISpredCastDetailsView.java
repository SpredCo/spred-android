package com.eip.roucou_c.spred.SpredCast;

import com.eip.roucou_c.spred.Entities.ReminderEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;

import java.util.List;

/**
 * Created by roucou_c on 08/12/2016.
 */
public interface ISpredCastDetailsView {
    void setIsRemind(boolean result);

    void setUserEntity(UserEntity userEntity);

    void setReminders(List<ReminderEntity> reminderEntities);

    void spredCastDeleted();
}
