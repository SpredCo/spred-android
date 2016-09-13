package com.eip.roucou_c.spred;


import com.eip.roucou_c.spred.DAO.Manager;

/**
 * Created by cleme_000 on 25/02/2016.
 */
public class MyService {
//    public final ServiceWeb _serviceWeb;
    protected Manager _manager;
//    public UserEntity _userEntity;

    public MyService(Manager manager, UserEntity userEntity) {
        this._manager = manager;
//        this._userEntity = userEntity;
//        if (_userEntity != null) {
//            this._userEntity.refresh();
//        }
//
//        _serviceWeb = ServiceWeb.getInstance(_manager.get_pContext());
    }

    public MyService(Manager manager) {
        this._manager = manager;
//        this._userEntity = UserEntity.getInstance(_manager);
//        this._userEntity.refresh();
//
//        _serviceWeb = ServiceWeb.getInstance(_manager.get_pContext());
    }
}
