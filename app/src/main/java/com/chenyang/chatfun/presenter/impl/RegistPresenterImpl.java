package com.chenyang.chatfun.presenter.impl;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.chenyang.chatfun.presenter.RegistPresenter;
import com.chenyang.chatfun.utils.ThreadUtils;
import com.chenyang.chatfun.view.RegistView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by fullcircle on 2016/12/31.
 */

public class RegistPresenterImpl implements RegistPresenter {
    private RegistView registView;

    public RegistPresenterImpl(RegistView registView) {
        this.registView = registView;
    }

    @Override
    public void registUser(final String username, final String pwd) {
        //注册的逻辑
        //先去LeanCould注册 注册成功之后 再到环信注册 LeanCould注册的过程就是创建一个AVUser对象 放到三方的数据库中
        final AVUser user = new AVUser();
        //设置用户名密码
        user.setUsername(username);
        user.setPassword(pwd);
        //保存user对象到bmob服务器


        //在子线程中注册用户
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(final AVException e) {
                {
                    //如果 没有异常说明注册成功
                    if(e==null){
                        ThreadUtils.runOnNonUIThread(new Runnable() {
                            @Override
                            public void run() {
                                //注册环信 需要注意 环信的api 联网的操作没有帮助开线程
                                try {
                                    EMClient.getInstance().createAccount(username, pwd);
                                    //说明注册成功
                                    //通知界面跳转
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            registView.onGetRegistState(username,pwd,true,null);
                                        }
                                    });

                                } catch (final HyphenateException e1) {
                                    e1.printStackTrace();
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //如果环信注册失败 删除三方数据库的user
                                            try {
                                                user.delete();
                                            } catch (AVException e2) {
                                                e2.printStackTrace();
                                            }
                                            //通知界面显示注册失败
                                            registView.onGetRegistState(username,pwd,false,e1.getDescription());
                                        }
                                    });
                                }
                            }
                        });

                    }else{
                        //如果有异常说明注册失败 通知界面显示注册失败
                        registView.onGetRegistState(username,pwd,false,e.getMessage());
                    }
                }
            }
        });

//        user.save(new SaveListener() {
//            @Override
//            public void done(Object o, BmobException e) {
//                //如果 没有异常说明注册成功
//                if(e==null){
//                    ThreadUtils.runOnNonUIThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //注册环信
//                            try {
//                                EMClient.getInstance().createAccount(username, pwd);
//                                //说明注册成功
//                                //通知界面跳转
//                                registView.onGetRegistState(username,pwd,true,null);
//                            } catch (final HyphenateException e1) {
//                                e1.printStackTrace();
//                                ThreadUtils.runOnMainThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //如果注册失败 删除user
//                                        user.delete();
//                                        //通知界面显示注册失败
//                                        registView.onGetRegistState(username,pwd,false,e1.getMessage());
//                                    }
//                                });
//                            }
//                        }
//                    });
//
//                }else{
//                //如果有异常说明注册失败 通知界面显示注册失败
//                    registView.onGetRegistState(username,pwd,false,e.getMessage());
//                }
//            }
//        });
        //环信注册成功 用户注册成功
        //环信注册失败 删除bmob账户 注册失败
        //如果bmob注册失败 注册失败
    }



}
