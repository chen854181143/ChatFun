package com.chenyang.chatfun.presenter.impl;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.chenyang.chatfun.db.DBUtils;
import com.chenyang.chatfun.presenter.AddFriendPresenter;
import com.chenyang.chatfun.utils.ThreadUtils;
import com.chenyang.chatfun.view.AddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;


/**
 * Created by fullcircle on 2017/1/4.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {
    private AddFriendView addFriendView;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        this.addFriendView = addFriendView;
    }

    @Override
    public void searchFriend(String keyword) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        //AVQuery 要到LeanCould服务端查询数据
        AVQuery<AVUser> query = new AVQuery<>("_User");
        //模糊查询用户名  以输入的字母开头的都查出来
        query.whereStartsWith("username",keyword)
                //要把自己去掉 查询其它用户
                .whereNotEqualTo("username",currentUser)
                //查询满足条件的所有记录
                .findInBackground(new FindCallback<AVUser>() {
                    @Override
                    public void done(List<AVUser> list, AVException e) {
                        if(e==null&&list!=null&&list.size()>0){
                            //查出数据可以显示
                            //从数据库中拿到已经在通讯录中的好友
                            List<String> users = DBUtils.initContact(currentUser);
                            addFriendView.onQuerySuccess(list,users,true,null);
                            for(AVUser user:list){
                                Log.e("test",user.getUsername());
                            }
                        }else{
                            if(e == null){
                                //查询成功但是没有匹配的数据
                                addFriendView.onQuerySuccess(null,null,false,"没有满足条件的用户");
                            }else{
                                //查询失败
                                addFriendView.onQuerySuccess(null,null,false,e.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void addFriend(final String username) {
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username,"申请添加好友");
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            addFriendView.onGetAddFriendResult(true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            addFriendView.onGetAddFriendResult(true,e.getMessage());
                        }
                    });
                }
            }
        });

    }
}
