package com.eip.roucou_c.spred.SpredCast;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roucou_c on 07/11/2016.
 */
public class SpredCastPresenter {
    private final ISpredCastView _iSpredCastView;
    private final Manager _manager;
    private final SpredCastService _spredCastService;
    private final ISpredCastNewView _iSpredCastNewView;

    public SpredCastPresenter(ISpredCastView iSpredCastView, ISpredCastNewView iSpredCastNewView, Manager manager, TokenEntity tokenEntity) {
        _iSpredCastView = iSpredCastView;
        _iSpredCastNewView = iSpredCastNewView;
        _manager = manager;
        this._spredCastService = new SpredCastService(iSpredCastView, iSpredCastNewView, manager, tokenEntity);
    }

    public void getSpredCast() {
        this._spredCastService.getSpredCast();
    }

    public void searchPartialPseudo(String s) {
        if (!s.isEmpty()) {
            _spredCastService.searchPartialPseudo(s);
        }
    }

    public void createSpredCast() {

        String name = _iSpredCastNewView.getSpredCastName();
        String description = _iSpredCastNewView.getSpredCastDescription();
        Boolean is_private = _iSpredCastNewView.getSpredCastIsPrivate();
        Boolean is_now = _iSpredCastNewView.getSpredCastIsNow();
        Date date = _iSpredCastNewView.getSpredCastDate();
        Date time = _iSpredCastNewView.getSpredCastTime();
        int duration = _iSpredCastNewView.getSpredCastDuration();
        List<String> tagsList = _iSpredCastNewView.getSpredCastTags();
        List<String> membersList = _iSpredCastNewView.getSpredCastMembers();
        String user_capacity = _iSpredCastNewView.getSpredCastUserCapacity();

        boolean isError = false;

        if (!checkNewSpredCastStep1(name, description, tagsList)) {
            isError = true;
        }
        else if (!checkNewSpredCastStep2(is_now, date, time, duration)) {
            isError = true;
        }
        else if (!checkNewSpredCastStep3(is_private, user_capacity, membersList)) {
            isError = true;
        }

        if (!isError) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");

            HashMap<String, Object> params = new HashMap<>();

            params.put("name", name);
            params.put("description", description);
            params.put("is_public", !is_private);
            params.put("date", is_now ? "now" : dateFormat.format(time));
            params.put("tags", tagsList);
            params.put("members", membersList);
            params.put("duration", duration);
            if (!user_capacity.isEmpty()) {
                params.put("user_capacity", Integer.parseInt(user_capacity));
            }

            _spredCastService.postSpredCast(params);
        }
    }

    public boolean checkNewSpredCastStep1(String name, String description, List<String> tags) {

        boolean isError = false;

        if (name.isEmpty()) {
            _iSpredCastNewView.setErrorName(R.string.spredcast_nameError);
            isError = true;
        }
        if (description.isEmpty()) {
            _iSpredCastNewView.setErrorDescription(R.string.spredcast_descriptionError);
            isError = true;
        }
        if (!isError) {
            _iSpredCastNewView.setErrorName(0);
            _iSpredCastNewView.setErrorDescription(0);

        }
        return !isError;
    }
    public boolean checkNewSpredCastStep2(boolean isNow, Date date, Date time, int duration) {
        Calendar calendar = _iSpredCastNewView.getCalendar();

        boolean isError = false;

        if (!isNow && time.before(calendar.getTime())) {
            _iSpredCastNewView.setErrorTime(R.string.spredcast_timeError);
            isError = true;
        }
        if (!isError) {
            _iSpredCastNewView.setErrorTime(0);
        }

        return !isError;
    }

    protected boolean checkNewSpredCastStep3(Boolean isPrivate, String user_capacity, List<String> membersList) {
        boolean isError = false;

        if (isPrivate && membersList.size() == 0) {
            _iSpredCastNewView.setErrorMembersList(R.string.spredcast_membersError);
            isError = true;
        }
        if (!isError) {
            _iSpredCastNewView.setErrorMembersList(0);
        }

        return !isError;
    }

}
