package com.eip.roucou_c.spred.SpredCast.Tokenfield;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.Inbox.Tokenfield.TokenTextView;
import com.eip.roucou_c.spred.R;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by roucou_c on 07/10/2016.
 */
public class ContactsCompletionView extends TokenCompleteTextView<UserEntity> {

    public ContactsCompletionView(Context context) {
        super(context);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(UserEntity userEntity) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
        token.setText("@"+userEntity.get_pseudo());
        return token;
    }

    @Override
    protected UserEntity defaultObject(String completionText) {
        return null;
    }
}