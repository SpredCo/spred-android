package com.eip.roucou_c.spred.SpredCast;

import android.util.Log;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.SpredCastEntity;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Inbox.InboxService;
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
    private final ISpredCastView _view;
    private final Manager _manager;
    private final SpredCastService _spredCastService;

    public SpredCastPresenter(ISpredCastView view, Manager manager, TokenEntity tokenEntity) {
        _view = view;
        _manager = manager;
        this._spredCastService = new SpredCastService(view, manager, tokenEntity);
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

        String name = _view.getSpredCastName();
        String description = _view.getSpredCastDescription();
        Boolean is_private = _view.getSpredCastIsPrivate();
        Date date = _view.getSpredCastDate();
        Date time = _view.getSpredCastTime();
        int duration = _view.getSpredCastDuration();
        List<String> tagsList = _view.getSpredCastTags();
        List<String> membersList = _view.getSpredCastMembers();
        String user_capacity = _view.getSpredCastUserCapacity();
        Calendar calendar = _view.getCalendar();
        boolean isError = false;

        if (name.isEmpty()) {
            _view.setErrorName(R.string.spredcast_nameError);
            isError = true;
        }
        if (description.isEmpty()) {
            _view.setErrorDescription(R.string.spredcast_descriptionError);
            isError = true;
        }
        if (is_private && membersList.size() == 0) {
            _view.setErrorMembersList(R.string.spredcast_membersError);
            isError = true;
        }
        if (time.before(calendar.getTime())) {
            _view.setErrorTime(R.string.spredcast_timeError);
            isError = true;
        }
        if (!isError) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");

            HashMap<String, Object> params = new HashMap<>();

            params.put("name", name);
            params.put("description", description);
            params.put("is_public", !is_private);
            params.put("date", dateFormat.format(time));
            params.put("tags", tagsList);
            params.put("members", membersList);
            params.put("duration", duration);
            if (!user_capacity.isEmpty()) {
                params.put("user_capacity", Integer.parseInt(user_capacity));
            }

            _spredCastService.postSpredCast(params);
        }
    }
}
