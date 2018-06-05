package com.tony.juetu.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tony.juetu.R;
import com.tony.juetu.base.BaseParentFragment;


/**
 * Created by dev on 5/31/18.
 */

public class HomeParentFragment extends BaseParentFragment {


    public static HomeParentFragment getInstance()
    {
        return new HomeParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (findChildFragment(HomeViewPageFragment.class) == null)
        {
            loadRootFragment(R.id.base_container,HomeViewPageFragment.getInstance());
        }
    }
}
