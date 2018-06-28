package com.tony.juetu.conversation;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class ConversationParentFragment extends BaseParentFragment {

    public static ConversationParentFragment getInstance()
    {
        return new ConversationParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_layout,container,false);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(ConversationParentFragment.class.getName());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (findChildFragment(ContactFragment.class) == null)
        {
            loadRootFragment(R.id.base_container,ContactFragment.getInstance());
        }
    }
}
