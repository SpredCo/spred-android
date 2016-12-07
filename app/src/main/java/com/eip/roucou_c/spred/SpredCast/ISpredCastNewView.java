package com.eip.roucou_c.spred.SpredCast;

import com.eip.roucou_c.spred.Entities.UserEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by roucou_c on 19/11/2016.
 */
public interface ISpredCastNewView {
    void populateSearchPseudo(List<UserEntity> userEntities);

    String getSpredCastName();

    String getSpredCastDescription();

    Boolean getSpredCastIsPrivate();

    Date getSpredCastDate();

    Date getSpredCastTime();

    int getSpredCastDuration();

    List<String> getSpredCastTags();

    List<String> getSpredCastMembers();

    void setErrorName(int resId);

    void setErrorDescription(int resId);

    void setErrorMembersList(int resId);

    Calendar getCalendar();

    void setErrorTime(int resId);

    String getSpredCastUserCapacity();

    Boolean getSpredCastIsNow();

    void finishPostSpredCast();
}
