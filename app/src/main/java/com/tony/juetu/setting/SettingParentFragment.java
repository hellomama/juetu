package com.tony.juetu.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tony.juetu.R;
import com.tony.juetu.base.BaseParentFragment;
import com.tony.juetu.home.HomeViewPageFragment;

/**
 * Created by dev on 5/31/18.
 */

public class SettingParentFragment extends BaseParentFragment {


    public static SettingParentFragment getInstance()
    {
        return new SettingParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_layout,container,false);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(SettingParentFragment.class.getName());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (findChildFragment(LoginFragment.class) == null)
//        {
//            loadRootFragment(R.id.base_container,LoginFragment.getInstance());
//        }
    }
}
