package com.tony.juetu.activities;

import android.content.Intent;
import android.os.Bundle;

import com.tony.juetu.R;
import com.tony.juetu.setting.LoginFragment;
import com.tony.juetu.utils.PreUtils;

import java.util.HashMap;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by dev on 6/28/18.
 */

public class LoginActivity extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUI();
    }

    private void loadUI()
    {
        HashMap accountInfo = PreUtils.getInstance().getAccount();
        if (accountInfo == null)
        {
          loadRootFragment(R.id.base_container, LoginFragment.getInstance());
        }else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
