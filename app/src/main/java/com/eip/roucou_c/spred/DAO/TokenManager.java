package com.eip.roucou_c.spred.DAO;

import android.database.sqlite.SQLiteDatabase;

import com.eip.roucou_c.spred.Entities.TokenEntity;


/**
 * Created by cleme_000 on 01/03/2016.
 */
public class TokenManager extends TokenDAO {
    public TokenManager(SQLiteDatabase mDb) {
        super(mDb);
    }

    @Override
    public long add(TokenEntity tokenEntity) {
        super.reset(tokenEntity.get_id());
        return super.add(tokenEntity);
    }

    @Override
    public void modify(TokenEntity tokenEntity) {
        super.modify(tokenEntity);
    }

}
