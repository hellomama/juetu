package com.tony.juetu.base;

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
    private SupportFragment fragment;


    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context,DisplayActivity.class);
        context.startActivity(intent);
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
