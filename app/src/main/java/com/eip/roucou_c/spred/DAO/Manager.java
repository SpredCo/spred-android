package com.eip.roucou_c.spred.DAO;

import android.content.Context;

/**
 * Created by cleme_000 on 04/03/2016.
 */
public final class Manager extends DAOBase{

    private static volatile Manager instance = null;

    final public GlobalManager _globalManager;
    final public TokenManager _tokenManager;
    private final Context _pContext;


    private Manager(Context pContext) {
        super(pContext);
        this._pContext = pContext;

        this._tokenManager = new TokenManager(this._mDb);
        this._globalManager = new GlobalManager(this._mDb);
    }

    public Context get_pContext() {
        return _pContext;
    }

    public final static Manager getInstance(Context pContext) {
        if (Manager.instance == null) {
            synchronized(Manager.class) {
                if (Manager.instance == null) {
                    Manager.instance = new Manager(pContext);
                }
            }
        }
        return Manager.instance;
    }
}
