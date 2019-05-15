package com.chenyang.chatfun.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenyang.chatfun.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by fullcircle on 2017/1/6.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<EMMessage> messages;

    public ChatAdapter(List<EMMessage> messages) {
        this.messages = messages;
    }

    public void setMessages(List<EMMessage> messages) {
        this.messages = messages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  null;
        if(viewType == 0){
            //收到的消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_item,parent,false);
        }else{
           //发送的消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_item_send,parent,false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //获取当前条目对应的消息
        EMMessage message = messages.get(position);
        long msgTime = message.getMsgTime();
        //判断是否要显示时间
        if(position == 0){
            //两个时间 间隔是否很近 <30s
           if(DateUtils.isCloseEnough(msgTime,System.currentTimeMillis())) {
               //如果当前消息是刚发送的不显示发送的时间
               holder.tv_time.setVisibility(View.GONE);
               Log.e("ChatActivity", "position" + position + DateUtils.getTimestampString(new Date(msgTime)));
           }else{
               //DateUtils.getTimestampString(new Date(msgTime)) 使用环信的工具类 来格式化时间
               holder.tv_time.setText(DateUtils.getTimestampString(new Date(msgTime)));
               holder.tv_time.setVisibility(View.VISIBLE);
           }
        }else{
            if(DateUtils.isCloseEnough(msgTime,messages.get(position-1).getMsgTime())){
                //跟上一条消息的发送时间比较 获取两条消息的时间间隔
                holder.tv_time.setVisibility(View.GONE);
                Log.e("ChatActivity","gone"+position);

            }else{
                holder.tv_time.setText(DateUtils.getTimestampString(new Date(msgTime)));
                holder.tv_time.setVisibility(View.VISIBLE);
                Log.e("ChatActivity","position"+position+DateUtils.getTimestampString(new Date(msgTime)));
            }
        }

        //显示消息的内容  EMTextMessageBody 文本消息
        EMTextMessageBody body = (EMTextMessageBody)message.getBody();
        //获取消息内容
        String msg = body.getMessage();
        holder.tv_message.setText(msg);

        if(message.direct()== EMMessage.Direct.SEND){
            //如果是发送的消息还要处理发送的状态
            EMMessage.Status status = message.status();
            switch (status){
                case SUCCESS:
                    //隐藏发送状态的图标
                    holder.iv_state.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.iv_state.setImageResource(R.mipmap.msg_error);
                    break;
                case INPROGRESS:
                    holder.iv_state.setImageResource(R.drawable.send_animation);
                    AnimationDrawable drawable = (AnimationDrawable) holder.iv_state.getDrawable();
                    drawable.start();
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        //EMMessage 环信聊天的消息 对应的对象
        EMMessage message = messages.get(position);
        EMMessage.Direct direct = message.direct();
        //Direct 消息的发送方向  send说明是自己发的  receive收到的消息
//        根据direct的值来决定 究竟是发送的消息 还是收到的消息
        return direct== EMMessage.Direct.RECEIVE?0:1;
    }

    @Override
    public int getItemCount() {
        return messages == null?0:messages.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_time;
        TextView tv_message;
        ImageView iv_state;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_state = (ImageView) itemView.findViewById(R.id.iv_state);
        }
    }
}
