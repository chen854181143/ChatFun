package com.chenyang.chatfun.presenter.impl;

import com.chenyang.chatfun.presenter.ConversationPresenter;
import com.chenyang.chatfun.view.ConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by fullcircle on 2017/1/6.
 */

public class ConversationPresenterImpl implements ConversationPresenter {
    private ConversationView conversationView;

    public ConversationPresenterImpl(ConversationView conversationView) {
        this.conversationView = conversationView;
    }

    @Override
    public void getConversations() {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        Collection<EMConversation> values = allConversations.values();
        //获取会话的集合
        List<EMConversation> conversationList = new ArrayList<>(values);
        //根据最近收到的消息时间的顺序对会话进行排序
        Collections.sort(conversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                return (int) (o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
            }
        });
        conversationView.onGetConversations(conversationList);
    }

    @Override
    public void clearAllUnreadMark() {
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
        conversationView.onClearAllUnreadMark();
    }
}
