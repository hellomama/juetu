package com.tony.juetu.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tony.juetu.common.Constant;
import com.tony.juetu.R;
import com.tony.juetu.activities.MainActivity;
import com.tony.juetu.xmpp.ConnectService;

import me.yokeyword.fragmentation.SupportFragment;

import static com.tony.juetu.common.Constant.NAME;
import static com.tony.juetu.common.Constant.PASSWORD;

/**
 * Created by bushi on 2018/6/27.
 */

public class LoginFragment extends SupportFragment implements View.OnClickListener{

    public static LoginFragment getInstance()
    {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }


    private static final String TAG = LoginFragment.class.getSimpleName();
    private TextInputEditText account,pwd;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null )
            {
                String action = intent.getAction();
                if (action != null && action.equals(Constant.ACTION_UPDATE_UI))
                {
                    boolean update = intent.getBooleanExtra(Constant.UPDATE,false);
                    if (update)
                    {
                        Intent i = new Intent(_mActivity,MainActivity.class);
                        startActivity(i);
                        _mActivity.finish();
                    }else {
                        Toast.makeText(_mActivity,"connect fail",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_login,container,false);
       initView(view);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initReceiver();
    }

    private void initView(View aRootView)
    {
        account = aRootView.findViewById(R.id.edit_account);
        pwd = aRootView.findViewById(R.id.edit_pwd);
        Button login = aRootView.findViewById(R.id.btn_log_in);

        login.setOnClickListener(this);
    }

    private void initReceiver()
    {
        Log.d(TAG,"init receiver");
        IntentFilter filter = new IntentFilter(Constant.ACTION_UPDATE_UI);
        _mActivity.registerReceiver(receiver,filter);
    }

    @Override
    public void onClick(View v) {
        if (account.getText().length()>0&&pwd.getText().length()>0)
        {
            login();
        }else {
            showError();
        }
    }

    private void login()
    {
        Intent intent = new Intent(_mActivity,ConnectService.class);
        intent.putExtra(NAME,account.getText().toString());
        intent.putExtra(PASSWORD,pwd.getText().toString());
        _mActivity.startService(intent);
    }

    private void showError()
    {
        if (account.getText().length()<=0)
        {
            account.setHintTextColor(getResources().getColor(R.color.COLOR_04));
            account.setHint(getResources().getString(R.string.text_account_invalid));
        }
        if (pwd.getText().length()<=0)
        {
            pwd.setHintTextColor(getResources().getColor(R.color.COLOR_04));
            pwd.setHint(getResources().getString(R.string.text_pwd_invalid));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _mActivity.unregisterReceiver(receiver);
    }
}
