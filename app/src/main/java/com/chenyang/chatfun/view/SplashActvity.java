package com.chenyang.chatfun.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;


import com.chenyang.chatfun.MainActivity;
import com.chenyang.chatfun.R;
import com.chenyang.chatfun.presenter.SplashPresenter;
import com.chenyang.chatfun.presenter.impl.SplashPresenterImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fullcircle on 2016/12/31.
 */

public class SplashActvity extends BaseActivity implements SplashView {
    @InjectView(R.id.iv_splash)
    ImageView ivSplash;
    //声明presenter引用
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        //view层 要持有presenter的引用
        splashPresenter = new SplashPresenterImpl(this);
        //判断是否登录过
        splashPresenter.checkLogin();
    }

    @Override
    public void onGetLoginState(boolean isLogin) {
        if (isLogin) {
            //如果登录过跳到主界面
            startActivity(MainActivity.class, true);
        } else {
            //如果没有登录跳到登录的界面 属性动画 alpha透明度动画 两秒之内从透明变成不透明
            ObjectAnimator alpha = ObjectAnimator.ofFloat(ivSplash, "alpha", 0, 1).setDuration(2000);
            //开始动画
            alpha.start();
            //给透明度动画添加一个监听
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //当动画执行结束开启登陆的activity
                    startActivity(LoginActivity.class, true);
                }
            });

        }
    }
}
