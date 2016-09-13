package com.eip.roucou_c.spred.DAO;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by roucou-c on 07/12/15.
 */
public class GlobalManager extends GlobalDAO {

    public GlobalManager(SQLiteDatabase mDb) {
        super(mDb);

        if (this.select("settings_offline") == null) {
            super.add("settings_offline", "true");
        }
        if (this.select("settings_display_offline") == null) {
            super.add("settings_display_offline", "true");
        }
    }

    public void addGlobal(String key, String value) {
        if (super.select(key) != null) {
            super.modify(key, value);
        }
        else {
            super.add(key, value);
        }
    }

    public void deleteGlobal(String key) {
        super.delete(key);
    }
}
