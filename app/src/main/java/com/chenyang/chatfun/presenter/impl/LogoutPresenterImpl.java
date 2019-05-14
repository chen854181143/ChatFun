package com.chenyang.chatfun.presenter.impl;

import com.chenyang.chatfun.callback.MyEmCalBack;
import com.chenyang.chatfun.presenter.LogoutPresenter;
import com.chenyang.chatfun.view.PluginView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class LogoutPresenterImpl implements LogoutPresenter {
    private PluginView pluginView;

    public LogoutPresenterImpl(PluginView pluginView) {
        this.pluginView = pluginView;
    }

    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new MyEmCalBack() {
            @Override
            public void success() {
                pluginView.onLogout(true,null);
            }

            @Override
            public void error(int i, String s) {
                pluginView.onLogout(false,s);
            }
        });
    }
}
