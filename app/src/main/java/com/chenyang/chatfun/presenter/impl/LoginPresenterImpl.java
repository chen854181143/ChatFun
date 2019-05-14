package com.chenyang.chatfun.presenter.impl;

import com.chenyang.chatfun.callback.MyEmCalBack;
import com.chenyang.chatfun.presenter.LoginPresenter;
import com.chenyang.chatfun.view.LoginView;
import com.hyphenate.chat.EMClient;

/**
 * Created by fullcircle on 2016/12/31.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void login(final String username, final String pwd) {
        EMClient.getInstance().login(username, pwd, new MyEmCalBack() {
            @Override
            public void success() {
                loginView.onGetLoginState(username,true,null);
            }

            @Override
            public void error(int i, String s) {
                loginView.onGetLoginState(username,false,s);
            }
        });

    }
}
