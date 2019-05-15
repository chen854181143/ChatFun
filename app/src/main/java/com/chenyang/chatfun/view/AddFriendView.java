package com.chenyang.chatfun.view;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by fullcircle on 2017/1/4.
 */

public interface AddFriendView {

    void onQuerySuccess(List<AVUser> list, List<String> users, boolean b, String errorMeg);

    void onGetAddFriendResult(boolean b, String message);
}
