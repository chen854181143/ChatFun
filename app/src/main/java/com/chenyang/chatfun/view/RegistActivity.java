package com.chenyang.chatfun.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chenyang.chatfun.R;
import com.chenyang.chatfun.presenter.RegistPresenter;
import com.chenyang.chatfun.presenter.impl.RegistPresenterImpl;
import com.chenyang.chatfun.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.os.Build.*;

/**
 * Created by fullcircle on 2016/12/31.
 */
public class RegistActivity extends BaseActivity implements RegistView{
    @InjectView(R.id.iv_avatar)
    ImageView ivAvatar;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.til_username)
    TextInputLayout tilUsername;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @InjectView(R.id.btn_regist)
    Button btnRegist;
    private RegistPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //有的手机没有实体按键 是通过虚拟的底部导航实现的 home 返回的效果
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        ButterKnife.inject(this);
        presenter = new RegistPresenterImpl(this);
    }

    @OnClick(R.id.btn_regist)
    public void onClick() {
        //获取用户名密码
        String pwd = etPwd.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        //校验输入是否合法
       if( !StringUtils.CheckUsername(username)){
           tilUsername.setErrorEnabled(true);
           tilUsername.setError("用户名不合法");
           return;
       }else{
           tilUsername.setErrorEnabled(false);
       }

        //校验输入是否合法
        if( !StringUtils.Checkpwd(pwd)){
            tilPwd.setErrorEnabled(true);
            tilPwd.setError("密码不合法");
            return;
        }else{
            tilPwd.setErrorEnabled(false);
        }
        //创建进度条对话框 执行注册的逻辑
        showProgressDialog("正在注册......");
        presenter.registUser(username,pwd);
    }

    @Override
    public void onGetRegistState(String username, String pwd, boolean isSuccess, String errorMsg) {
        //走到这个方法中 说明 已经获取到了注册的结果 隐藏进度对话框
        cancelProgressDialog();
        if(isSuccess){
           //注册成功 可以跳转到登录页面
            showToast("注册成功");
            saveUsernamePwd(username,pwd);
            startActivity(LoginActivity.class,true);
        }else{
           //通过吐司显示注册失败
            showToast("注册失败:"+errorMsg);
        }
    }
}
