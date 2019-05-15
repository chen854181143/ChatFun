package com.chenyang.chatfun.presenter;

/**
 * Created by fullcircle on 2017/1/3.
 */

public interface ContactPresenter {
    //初始化
    void initContact();
    //更新联系人
    void updateContact();
    //删除联系人
    void deleteContact(String username);
}
