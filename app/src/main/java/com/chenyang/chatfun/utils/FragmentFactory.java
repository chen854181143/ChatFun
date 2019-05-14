package com.chenyang.chatfun.utils;


import com.chenyang.chatfun.view.BaseFragment;
import com.chenyang.chatfun.view.ContactFragment;
import com.chenyang.chatfun.view.ConversationFragment;
import com.chenyang.chatfun.view.PlugInFragment;

/**
 * Created by fullcircle on 2017/1/3.
 */
public class FragmentFactory {
    private static ContactFragment contactFragment = null;
    private static ConversationFragment conversationFragment = null;
    private static PlugInFragment plugInFragment = null;

    /**
     * 根据底部导航的索引 获取对应的fragment的实例
     * @param position
     * @return
     */
    public static BaseFragment getFragment(int position){
        BaseFragment fragment = null;
        switch (position){
            case 0:
                if(conversationFragment == null){
                    conversationFragment = new ConversationFragment();
                }
                fragment = conversationFragment;
                break;
            case 1:
                if(contactFragment == null){
                    contactFragment = new ContactFragment();
                }
                fragment = contactFragment;
                break;
            case 2:
                if(plugInFragment == null){
                    plugInFragment = new PlugInFragment();
                }
                fragment = plugInFragment;
                break;
        }
        return fragment;
    }
}
