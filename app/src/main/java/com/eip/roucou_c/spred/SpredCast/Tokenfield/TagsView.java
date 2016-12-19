package com.eip.roucou_c.spred.SpredCast.Tokenfield;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eip.roucou_c.spred.Entities.TagEntity;
import com.eip.roucou_c.spred.Entities.UserEntity;
import com.eip.roucou_c.spred.R;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by roucou_c on 07/10/2016.
 */
public class TagsView extends TokenCompleteTextView<TagEntity> {

    public TagsView(Context context) {
        super(context);
    }

    public TagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(TagEntity tagEntity) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.tag_token, (ViewGroup) getParent(), false);
        token.setText("#"+tagEntity.get_name());
        return token;
    }

    protected TagEntity defaultObject(String completionText) {
        return null;
    }
}