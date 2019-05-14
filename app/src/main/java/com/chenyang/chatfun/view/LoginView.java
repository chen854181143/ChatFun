package com.chenyang.chatfun.view;

/**
 * Created by fullcircle on 2016/12/31.
 */
public interface LoginView {
   /**
    *
    * @param username
    * @param isLogin
    * @param errorMsg
     */
   void onGetLoginState(String username, boolean isLogin, String errorMsg);
}
