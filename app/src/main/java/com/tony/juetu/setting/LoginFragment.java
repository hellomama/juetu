package com.tony.juetu.setting;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tony.juetu.R;
import com.tony.juetu.connection.ConnectService;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by bushi on 2018/6/27.
 */

public class LoginFragment extends SupportFragment implements View.OnClickListener{

    public static LoginFragment getInstance()
    {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }


    private TextInputEditText account,pwd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_login,container,false);
       initView(view);
       return view;
    }

    private void initView(View aRootView)
    {
        account = aRootView.findViewById(R.id.edit_account);
        pwd = aRootView.findViewById(R.id.edit_pwd);
        Button login = aRootView.findViewById(R.id.btn_log_in);

        login.setOnClickListener(this);
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
        intent.putExtra(ConnectService.NAME,account.getText().toString());
        intent.putExtra(ConnectService.PASSWORD,pwd.getText().toString());
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
}
