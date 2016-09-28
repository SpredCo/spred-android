package com.eip.roucou_c.spred.Home.TabLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eip.roucou_c.spred.R;

/**
 * Created by roucou_c on 22/09/2016.
 */
public class FragmentHome extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_home, container, false);
    }
}
