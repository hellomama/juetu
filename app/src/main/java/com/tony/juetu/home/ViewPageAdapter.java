package com.tony.juetu.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/1/18.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {

    private List<HomeListFragment> fragments;


    public ViewPageAdapter(FragmentManager fm, List<HomeListFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null?0:fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
