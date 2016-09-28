package com.eip.roucou_c.spred.Home.TabLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by roucou_c on 21/09/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        Fragment frgmt = null;

        switch (position) {
            case 0:
                frgmt = new FragmentHome();
                break;
            case 1:

                frgmt = new FragmentAbo();
                break;
            default:
                break;
        }

        return frgmt;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
