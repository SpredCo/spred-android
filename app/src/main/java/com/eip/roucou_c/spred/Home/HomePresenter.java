package com.eip.roucou_c.spred.Home;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.SignUp.SignUpService;

/**
 * Created by roucou_c on 14/09/2016.
 */
public class HomePresenter {

    private final Manager _manager;
    private final IHomeView _view;
    private final HomeService _homeService;

    public HomePresenter(IHomeView view, Manager manager) {
        _view = view;
        _manager = manager;
        this._homeService = new HomeService(view, manager);
    }

    public void deleteUser() {
        _homeService.deleteUser();
    }
}
