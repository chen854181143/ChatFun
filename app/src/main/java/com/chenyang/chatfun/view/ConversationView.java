package com.chenyang.chatfun.view;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by fullcircle on 2017/1/6.
 */

public interface ConversationView {
    void onGetConversations(List<EMConversation> conversationList);

    void onClearAllUnreadMark();
}
