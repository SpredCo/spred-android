package com.eip.roucou_c.spred.Inbox.Tokenfield;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.eip.roucou_c.spred.R;

/**
 * Created by roucou_c on 07/10/2016.
 */
public class TokenTextView extends TextView {

    public TokenTextView(Context context) {
        super(context);
    }

    public TokenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, selected ? R.drawable.ic_close_white_24dp : 0, 0);
    }
}