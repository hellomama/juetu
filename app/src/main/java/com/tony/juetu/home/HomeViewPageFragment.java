package com.tony.juetu.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tony.juetu.R;
import com.tony.juetu.base.DisplayActivity;
import com.tony.juetu.base.DisplayType;
import com.tony.juetu.utils.PreUtils;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/1/18.
 */

public class HomeViewPageFragment extends SupportFragment implements View.OnClickListener{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<HomeListFragment> mFragments = new ArrayList<>();

    @NonNull
    public static HomeViewPageFragment getInstance()
    {
        return new HomeViewPageFragment();
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
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
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_view_page,container,false);
        initView(view);
        return view;
    }

    private void initView(View aView)
    {
        ImageView category = aView.findViewById(R.id.img_category);
        category.setOnClickListener(this);

        mTabLayout = aView.findViewById(R.id.home_tab_layout);
        mViewPager = aView.findViewById(R.id.home_view_page);
        ArrayList<String> title = PreUtils.getInstance().getList();
        if (title != null&& title.size()>0)
        {
            for (String s: title)
            {
                mFragments.add(HomeListFragment.getInsance(s));
            }
        }else {
            mFragments.add(HomeListFragment.getInsance("Android"));
            mFragments.add(HomeListFragment.getInsance("前端"));
            mFragments.add(HomeListFragment.getInsance("iOS"));
            mFragments.add(HomeListFragment.getInsance("产品"));
            mFragments.add(HomeListFragment.getInsance("设计"));
            mFragments.add(HomeListFragment.getInsance("工具资源"));
            mFragments.add(HomeListFragment.getInsance("阅读"));
            mFragments.add(HomeListFragment.getInsance("后端"));
            mFragments.add(HomeListFragment.getInsance("人工智能"));
        }

        iniLayout();
    }

    private void iniLayout()
    {
        ViewPageAdapter mAdapter = new ViewPageAdapter(getChildFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        initTab();
        mTabLayout.addOnTabSelectedListener(tabSelectedListener);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_category:
                DisplayActivity.startActivity(getActivity(), DisplayType.EAllCategroy.type(),DisplayActivity.EXTRA_RESULT_TYPE);
                break;
                default:break;
        }
    }

    public void updateTab()
    {
        ArrayList<String> title = PreUtils.getInstance().getList();
        mFragments.clear();
        if (title != null&& title.size()>0)
        {
            for (String s: title)
            {
                mFragments.add(HomeListFragment.getInsance(s));
            }
        }else {
            mFragments.add(HomeListFragment.getInsance("Android"));
            mFragments.add(HomeListFragment.getInsance("前端"));
            mFragments.add(HomeListFragment.getInsance("iOS"));
            mFragments.add(HomeListFragment.getInsance("产品"));
            mFragments.add(HomeListFragment.getInsance("设计"));
            mFragments.add(HomeListFragment.getInsance("工具资源"));
            mFragments.add(HomeListFragment.getInsance("阅读"));
            mFragments.add(HomeListFragment.getInsance("后端"));
            mFragments.add(HomeListFragment.getInsance("人工智能"));
        }
        iniLayout();

    }


}
