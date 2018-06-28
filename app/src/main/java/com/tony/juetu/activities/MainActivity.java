package com.tony.juetu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;

import com.tony.juetu.R;
import com.tony.juetu.base.BaseParentFragment;
import com.tony.juetu.base.DisplayActivity;
import com.tony.juetu.home.HomeParentFragment;
import com.tony.juetu.home.HomeViewPageFragment;
import com.tony.juetu.conversation.ConversationParentFragment;
import com.tony.juetu.notification.NotificationParentFragment;
import com.tony.juetu.setting.SettingParentFragment;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends SupportActivity implements BaseParentFragment.OnBackToFirstListener {

    public static final int HOME = 0;
    public static final int HOT = 1;
    public static final int SEARCH = 2;
    public static final int SETTING = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];
    private int preOrder = 0;
    private int presentOrder = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            presentOrder = item.getOrder();
            showHideFragment(mFragments[presentOrder],mFragments[preOrder]);
            preOrder = presentOrder;
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SupportFragment homeFragment = findFragment(HomeParentFragment.class);
        if (homeFragment == null)
        {
            mFragments[HOME] = HomeParentFragment.getInstance();
            mFragments[HOT] = ConversationParentFragment.getInstance();
            mFragments[SEARCH] = NotificationParentFragment.getInstance();
            mFragments[SETTING] = SettingParentFragment.getInstance();

            loadMultipleRootFragment(R.id.container, HOME,
                    mFragments[HOME],
                    mFragments[HOT],
                    mFragments[SEARCH],
                    mFragments[SETTING]);
        }else {
            mFragments[HOME] = homeFragment;
            mFragments[HOT] = findFragment(ConversationParentFragment.class);
            mFragments[SEARCH] = findFragment(NotificationParentFragment.class);
            mFragments[SETTING] = findFragment(SettingParentFragment.class);
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackToFirstFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DisplayActivity.EXTRA_RESULT_TYPE)
        {
            ISupportFragment fragment = mFragments[HOME].getTopChildFragment();;
            if (fragment instanceof HomeViewPageFragment)
            {
                ((HomeViewPageFragment) fragment).updateTab();
                Log.d("Tony","tony");
            }
        }
    }
}