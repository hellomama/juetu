package com.tony.juetu.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tony.juetu.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/1/18.
 */

public class HomeListFragment extends SupportFragment {

    private String mTitle;

    public String getTitle()
    {
        if (TextUtils.isEmpty(mTitle))
        {
            init();
        }
        return mTitle;
    }

    public static HomeListFragment getInsance(String aTitle)
    {
        Bundle bundle = new Bundle();
        bundle.putString("title",aTitle);

        HomeListFragment fragment = new HomeListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void init()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mTitle = bundle.getString("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();

        View view = inflater.inflate(R.layout.fragment_base_layout,container,false);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(mTitle);
        return view;
    }
}
