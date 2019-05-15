package com.chenyang.chatfun.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.chenyang.chatfun.Constant;
import com.chenyang.chatfun.event.ExitEvent;
import com.chenyang.chatfun.utils.ToastUtils;
import com.hyphenate.EMError;
import com.hyphenate.util.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;


/**
 * Created by fullcircle on 2016/12/31.
 */
public class BaseActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private ProgressDialog progressDialog;
    private LocalBroadcastManager localBroadcastManager;
    private MyExitReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        receiver = new MyExitReceiver();
        localBroadcastManager.registerReceiver(receiver,new IntentFilter("com.itheima.finishactivity"));
    }

    protected void startActivity(Class clazz, boolean isFinish) {
        startActivity(new Intent(getApplicationContext(), clazz));
        if (isFinish) {
            finish();
        }
    }

    protected void showToast(String msg) {
        ToastUtils.showToast(getApplicationContext(), msg);
    }

    protected void saveUsernamePwd(String username, String pwd) {
        sp.edit().putString(Constant.SP_KEY_USERNAME, username).putString(Constant.SP_KEY_PASSWORD, pwd).commit();
    }

    protected String getUsername() {
        return sp.getString(Constant.SP_KEY_USERNAME, "");
    }

    protected String getPwd() {
        return sp.getString(Constant.SP_KEY_PASSWORD, "");
    }


    /**
     * 显示进度条对话框
     *
     * @param msg
     */
    protected void showProgressDialog(String msg) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    /**
     * 取消进度条对话框
     */
    protected void cancelProgressDialog() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetExitEvent(ExitEvent exitEvent){
        String msg = null;
        switch (exitEvent.exitType){
            case EMError.USER_REMOVED:
                msg = "您的账号已经被服务端删除......";
                break;
            case EMError.USER_LOGIN_ANOTHER_DEVICE:
                msg = "您的账号于"+ DateUtils.getTimestampString(new Date())+"已经在其它设备登录,请重新登录,如非本人操作" +
                        ",请及时修改密码";
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("下线通知");
        builder.setMessage(msg);
        builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //发广播关闭所有activity  LocalBroadcastManager 通过它发送的广播 只能被自己的应用受到
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
                manager.sendBroadcast(new Intent("com.itheima.finishactivity"));
                //打开登录页面
                startActivity(LoginActivity.class,false);
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( progressDialog!=null){
            progressDialog.dismiss();
        }
        //注销广播接收者
        localBroadcastManager.unregisterReceiver(receiver);
    }

    private class MyExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.itheima.finishactivity")){
                finish();
            }
        }
    }

}
