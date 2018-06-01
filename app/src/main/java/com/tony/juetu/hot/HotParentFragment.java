package com.tony.juetu.hot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tony.juetu.R;
import com.tony.juetu.base.BaseParentFragment;

/**
 * Created by dev on 5/31/18.
 */

public class HotParentFragment extends BaseParentFragment {

    public static HotParentFragment getInstance()
    {
        return new HotParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_layout,container,false);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(HotParentFragment.class.getName());
        return view;
    }
}
