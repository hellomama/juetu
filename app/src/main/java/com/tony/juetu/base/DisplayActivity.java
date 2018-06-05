package com.tony.juetu.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.tony.juetu.R;
import com.tony.juetu.home.AllCategoryFragment;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/1/18.
 */

public class DisplayActivity extends SupportActivity {

    public static final String EXTRA_DISPLAY_TYPE = "type";
    public static final int EXTRA_RESULT_TYPE = 1001;
    private SupportFragment fragment;


    public static void startActivity(Activity context, int aType, int code)
    {
        Intent intent = new Intent(context,DisplayActivity.class);
        intent.putExtra(EXTRA_DISPLAY_TYPE,aType);
        context.startActivityForResult(intent,code);
    }

    private void getFragment()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            int type = intent.getIntExtra(EXTRA_DISPLAY_TYPE,0xFFFF);
            switch (DisplayType.getType(type))
            {
                case EAllCategroy:
                    fragment = AllCategoryFragment.getInstance();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        getFragment();
        loadRootFragment(R.id.fragment_container,fragment);
    }
}
