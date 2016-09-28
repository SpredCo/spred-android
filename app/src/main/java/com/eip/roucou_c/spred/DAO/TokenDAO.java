package com.eip.roucou_c.spred.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eip.roucou_c.spred.Entities.TokenEntity;

/**
 * Created by cleme_000 on 01/03/2016.
 */
public class TokenDAO{
    private final SQLiteDatabase _mDb;
    public static final String TABLE_NAME = "token";

    /**
     * Attribut de l'API
     */
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String TOKEN_TYPE = "token_type";

    /**
     * Attribut locale
     */
    public static final String KEY = "id";
    public static final String EXPIRE_ACCESS_TOKEN = "expire_access_token";
    public static final String EXPIRE_REFRESH_TOKEN = "expire_refresh_token";



    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ACCESS_TOKEN + " TEXT,"
            + REFRESH_TOKEN + " INT,"
            + EXPIRES_IN + " TEXT,"
            + TOKEN_TYPE + " TEXT,"
            + EXPIRE_ACCESS_TOKEN + " TEXT,"
            + EXPIRE_REFRESH_TOKEN + " TEXT);";

    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public TokenDAO(SQLiteDatabase mDb) {
        this._mDb = mDb;
    }

    public long add(TokenEntity tokenEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCESS_TOKEN, tokenEntity.get_access_token());
        contentValues.put(REFRESH_TOKEN, tokenEntity.get_refresh_token());
        contentValues.put(EXPIRES_IN, tokenEntity.get_expire_in());
        contentValues.put(TOKEN_TYPE, tokenEntity.get_token_type());
        contentValues.put(EXPIRE_ACCESS_TOKEN, tokenEntity.get_expire_access_token());
        contentValues.put(EXPIRE_REFRESH_TOKEN, tokenEntity.get_expire_refresh_token());

        return _mDb.insert(TABLE_NAME, null, contentValues);
    }

    public void modify(TokenEntity tokenEntity) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCESS_TOKEN, tokenEntity.get_access_token());
        contentValues.put(REFRESH_TOKEN, tokenEntity.get_refresh_token());
        contentValues.put(EXPIRES_IN, tokenEntity.get_expire_in());
        contentValues.put(TOKEN_TYPE, tokenEntity.get_token_type());
        contentValues.put(EXPIRE_ACCESS_TOKEN, tokenEntity.get_expire_access_token());
        contentValues.put(EXPIRE_REFRESH_TOKEN, tokenEntity.get_expire_refresh_token());

        _mDb.update(TABLE_NAME, contentValues, KEY + " = ?", new String[]{String.valueOf(tokenEntity.get_id())});
    }


    public TokenEntity select() {
        Cursor c = _mDb.rawQuery("select * from " + TABLE_NAME + " LIMIT 1" , null);

        TokenEntity tokenEntity = null;

        if (c.moveToFirst()) {
            tokenEntity = new TokenEntity(c);
        }

        c.close();
        return tokenEntity;
    }

    public void reset(long id) {
        _mDb.execSQL("delete from "+ TABLE_NAME);
    }

    public void delete() {
        _mDb.delete(TABLE_NAME,null,null);
    }
}
