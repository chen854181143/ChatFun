package com.chenyang.chatfun.presenter.impl;

import com.chenyang.chatfun.presenter.SplashPresenter;
import com.chenyang.chatfun.view.SplashView;
import com.hyphenate.chat.EMClient;

/**
 * Created by fullcircle on 2016/12/31.
 */

public class SplashPresenterImpl implements SplashPresenter {
    /**
     * view的接口
     */
    private SplashView splashView;

    /**
     * 构造   构造的时候传入view接口的具体实现 通过这个实现 调用View层的业务逻辑
     * @param splashView
     */
    public SplashPresenterImpl(SplashView splashView) {
        this.splashView = splashView;
    }

    @Override
    public void checkLogin() {
        //检测是登录过
       if(EMClient.getInstance().isLoggedInBefore()&&EMClient.getInstance().isConnected()){
          // if(EMClient.getInstance().isConnected()){
               //isLoggedInBefore 之前登陆过    isConnected 已经跟环信的服务器建立了连接
               splashView.onGetLoginState(true);
          // }

       }else{
           splashView.onGetLoginState(false);
       }
        //splashView.onGetLoginState();
    }
}
