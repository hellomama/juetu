package com.tony.juetu.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;

import com.tony.juetu.R;
import com.tony.juetu.base.BaseParentFragment;
import com.tony.juetu.base.DisplayActivity;
import com.tony.juetu.common.Constant;
import com.tony.juetu.xmpp.ConnectService;
import com.tony.juetu.home.HomeParentFragment;
import com.tony.juetu.home.HomeViewPageFragment;
import com.tony.juetu.conversation.ConversationParentFragment;
import com.tony.juetu.notification.NotificationParentFragment;
import com.tony.juetu.setting.LoginStatusView;
import com.tony.juetu.setting.SettingParentFragment;
import com.tony.juetu.utils.Utils;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

import static com.tony.juetu.common.Constant.ACTION_SEND_RECONNECT;
import static com.tony.juetu.common.Constant.NAME;
import static com.tony.juetu.common.Constant.PASSWORD;

public class MainActivity extends SupportActivity implements BaseParentFragment.OnBackToFirstListener ,LoginStatusView {

    public static final int HOME = 0;
    public static final int HOT = 1;
    public static final int SEARCH = 2;
    public static final int SETTING = 3;
    private static final String TAG = MainActivity.class.getSimpleName();

    public static void start(Context context,String arg1,String arg2)
    {
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra(Constant.EXTRA_DATA,arg1);
        intent.putExtra(Constant.EXTRA_DATA_2,arg2);
        context.startActivity(intent);
    }

    private SupportFragment[] mFragments = new SupportFragment[4];
    private int preOrder = 0;
    private int presentOrder = 0;
    private Snackbar snackbar;
    private String name,password;

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

    private void init()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            name = intent.getStringExtra(Constant.EXTRA_DATA);
            password = intent.getStringExtra(Constant.EXTRA_DATA_2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

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
    protected void onResume() {
        super.onResume();
        checkConnectService();
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

    @Override
    public void onSignIn() {

    }

    @Override
    public void onSignInFail() {

    }

    @Override
    public void onSignInSuccess() {

    }

    private void checkConnectService()
    {
        boolean running = Utils.isServiceWork(this, ConnectService.class);
        if (running)
        {
            //send broadcast
            Log.d(TAG,"start reconnect");
            Intent intent = new Intent();
            intent.setAction(ACTION_SEND_RECONNECT);
            intent.putExtra(NAME,name);
            intent.putExtra(PASSWORD,password);
            sendBroadcast(intent);
        }else {
            // start service
            Log.d(TAG,"start service");
            Intent intent = new Intent(this,ConnectService.class);
            intent.putExtra(NAME,name);
            intent.putExtra(PASSWORD,password);
            startService(intent);
        }
    }
}
