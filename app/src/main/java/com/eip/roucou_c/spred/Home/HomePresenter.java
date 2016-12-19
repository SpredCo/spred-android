package com.eip.roucou_c.spred.Home;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.SignUp.SignUpService;

import java.util.HashMap;

/**
 * Created by roucou_c on 14/09/2016.
 */
class HomePresenter {

    private final Manager _manager;
    private final IHomeView _view;
    private final HomeService _homeService;

    HomePresenter(IHomeView view, Manager manager, TokenEntity tokenEntity) {
        _view = view;
        _manager = manager;
        this._homeService = new HomeService(view, manager, tokenEntity);
    }

    void getProfile() {
        _homeService.getProfile();
    }

    void getSpredCasts(int state) {
        _homeService.getSpredCasts(state);
    }

    void getAbo() {
        _homeService.getAbo();
    }

    void getSpredCastsAndShow(String url) {
        _homeService.getSpredCastsAndShow(url);

    }

    void getUserAndShow(String objectID) {
        _homeService.getUserAndShow(objectID);
    }
}
