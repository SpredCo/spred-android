package com.eip.roucou_c.spred.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cleme_000 on 03/11/2015.
 */

public abstract class DAOBase {
    protected final static int VERSION = 1;
    protected final static String NOM = "database.db";

    protected SQLiteDatabase _mDb = null;
    protected DatabaseHandler _mHandler = null;

    public DAOBase(Context pContext) {
        this._mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
        this.open();
    }

    public SQLiteDatabase open() {
        _mDb = _mHandler.getWritableDatabase();
        return _mDb;
    }

    public void close() {_mDb.close();
    }

    public SQLiteDatabase getDb() {
        return _mDb;
    }

    public void restartDatabase() {
        this._mHandler.onUpgrade(_mDb, 0, 0);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
