package com.chenyang.chatfun.view;

/**
 * Created by fullcircle on 2016/12/31.
 */

public interface RegistView {
    //当获取到注册的状态之后 做进一步的操作 如果注册成功 跳转到登陆页面 如果注册失败 弹toast

    /**
     * 当获取到注册的状态之后 做进一步的操作 如果注册成功 跳转到登陆页面 如果注册失败 弹toast
     * @param username 用户名
     * @param pwd         密码
     * @param isSuccess  是否注册成功
     * @param errorMsg   错误信息
     */
    void onGetRegistState(String username, String pwd, boolean isSuccess, String errorMsg);
}
