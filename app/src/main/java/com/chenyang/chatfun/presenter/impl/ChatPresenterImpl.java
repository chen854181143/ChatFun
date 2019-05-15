package com.chenyang.chatfun.presenter.impl;

import com.chenyang.chatfun.callback.MyEmCalBack;
import com.chenyang.chatfun.presenter.ChatPresenter;
import com.chenyang.chatfun.utils.ThreadUtils;
import com.chenyang.chatfun.view.ChatView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fullcircle on 2017/1/4.
 */

public class ChatPresenterImpl implements ChatPresenter {
    private ChatView chatView;

    public ChatPresenterImpl(ChatView chatView) {
        this.chatView = chatView;
    }
    private List<EMMessage> messages = new ArrayList<>();

    @Override
    public List<EMMessage> getChatHistoryMessage(String username) {
     //   EMChatManager emChatManager = EMClient.getInstance().chatManager();
        //conversation 代表了一个会话
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if(conversation !=null){
            //设置所有消息为已读
            conversation.markAllMessagesAsRead();
            //从数据库库获取数据 第一个参数 消息的id 从这条消息开始 取第二个参数传入的数目 这么多条数据
            //先获取最近一条消息
            EMMessage lastMessage = conversation.getLastMessage();
            //获取最近一条消息的消息id
            String msgId = lastMessage.getMsgId();
            //从最近一条数据开始加载 不包括 lastMessage
            int allMsgCount = conversation.getAllMsgCount();
            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(msgId, allMsgCount);
            //清空数据库
            messages.clear();
            messages.addAll(emMessages);
            messages.add(lastMessage);
            //把最近一条消息添加到集合
           // emMessages.add(lastMessage);
            chatView.getHistoryMessage(messages);
        }
      
        return null;
    }

    @Override
    public void sendMessage(String msgText, String contact) {
        //创建一条要发送的消息
        final EMMessage message = EMMessage.createTxtSendMessage(msgText,contact);
        messages.add(message);
        //添加消息发送状态的监听
        message.setMessageStatusCallback(new MyEmCalBack() {
            @Override
            public void success() {
                //发送成功
                chatView.updateList();
            }

            @Override
            public void error(int i, String s) {
                //发送失败
                chatView.updateList();
            }
        });
        //把消息放到数据库中
       // EMClient.getInstance().chatManager().saveMessage(message);
       // getChatHistoryMessage(contact);


        chatView.getHistoryMessage(messages);
        //发消息是联网的操作 需要放到子线程
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });


    }
}
