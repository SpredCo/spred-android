package com.eip.roucou_c.spred.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cleme_000 on 03/11/2015.
 */
public class GlobalDAO{
    public static final String TABLE_NAME = "global";
    private final SQLiteDatabase _mDb;

    public static final String KEY = "key";
    public static final String VALUE = "value";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY + " TEXT, " + VALUE + " TEXT);";

    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public GlobalDAO(SQLiteDatabase mDb) {
        this._mDb = mDb;
    }

    public long add(String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY, key);
        contentValues.put(VALUE, value);

        return _mDb.insert(TABLE_NAME, null, contentValues);
    }

    public void delete(String key) {

        _mDb.delete(TABLE_NAME, KEY + " = ?", new String[]{key});
    }

    public void modify(String key, String value) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY, key);
        contentValues.put(VALUE, value);
        _mDb.update(TABLE_NAME, contentValues, KEY + " = ?", new String[]{key});
    }

    public String select(String key) {

        String value = null;

        Cursor c = _mDb.rawQuery("select * from " + TABLE_NAME + " WHERE KEY = ?" , new String[] {key});

        if (c.moveToFirst()) {
            value = c.getString(1);
        }

        c.close();
        return value;
    }
}