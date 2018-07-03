package com.tony.juetu.base;

import android.app.Activity;

import com.tony.juetu.home.HomeParentFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 5/31/18.
 */

public abstract class BaseParentFragment extends SupportFragment {

    private OnBackToFirstListener listener;

    public interface OnBackToFirstListener{
        void onBackToFirstFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnBackToFirstListener)
        {
            listener = (OnBackToFirstListener)activity;
        }else {
            throw new RuntimeException(activity.toString()+" must implement OnBackToFirstListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public boolean onBackPressedSupport() {
        if (getChildFragmentManager().getBackStackEntryCount()>1)
        {
            popChild();
        }else {
            if (this instanceof HomeParentFragment)
            {
                _mActivity.finish();
            }else {
                listener.onBackToFirstFragment();
            }
        }
        return true;
    }

    protected void addFragment()
    {

    }
}
