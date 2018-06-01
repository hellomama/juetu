package com.tony.juetu.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tony.juetu.R;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/1/18.
 */

public class HomeViewPageFragment extends SupportFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPageAdapter mAdapter;
    private List<HomeListFragment> mFragments = new ArrayList<>();

    public static HomeViewPageFragment getInstance()
    {
        return new HomeViewPageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_view_page,container,false);
        initView(view);
        return view;
    }

    private void initView(View aView)
    {
        mTabLayout = aView.findViewById(R.id.home_tab_layout);
        mViewPager = aView.findViewById(R.id.home_view_page);
        mFragments.add(HomeListFragment.getInsance("首页"));
        mFragments.add(HomeListFragment.getInsance("Android"));
        mFragments.add(HomeListFragment.getInsance("设计"));
        mFragments.add(HomeListFragment.getInsance("人工智能"));
        mFragments.add(HomeListFragment.getInsance("前端"));
        mFragments.add(HomeListFragment.getInsance("iOS"));
        mFragments.add(HomeListFragment.getInsance("产品"));
        mAdapter = new ViewPageAdapter(getChildFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        initTab();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initTab()
    {
        for (int i = 0;i< mFragments.size();i++)
        {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if(tab != null) {
                tab.setCustomView(R.layout.tab_view);
                TextView name = tab.getCustomView().findViewById(R.id.tab_title);
                name.setText(mFragments.get(i).getTitle());
            }
        }
    }
}
