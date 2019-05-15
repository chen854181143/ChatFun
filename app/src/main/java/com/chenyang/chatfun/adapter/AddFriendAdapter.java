package com.chenyang.chatfun.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.chenyang.chatfun.R;
import com.chenyang.chatfun.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fullcircle on 2017/1/4.
 */
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder> {
    //从数据服务器获取的所有的用户信息
    private List<AVUser> users;
    //从环信服务器获取的好友信息
    private List<String> contacts = new ArrayList<>();

    public void setUsers(List<AVUser> users) {
        this.users = users;
    }

    public void setContacts(List<String> contacts) {
        if(contacts!=null){
        this.contacts = contacts;
        }
    }

    public AddFriendAdapter(List<AVUser> users, List<String> contacts) {
        this.users = users;
        if(contacts != null){
        this.contacts = contacts;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      //  View view = View.inflate(parent.getContext(), R.layout.list_item_addfriend, null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_addfriend,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String username = users.get(position).getUsername();
        holder.tv_username.setText(username);
        String date = StringUtils.getDate(users.get(position).getCreatedAt());
        holder.tv_addTime.setText(date);
        if(contacts.contains(username)){
           //已经是好友了
            holder.btn_add.setText("已经是好友");
            //holder.btn_add.setClickable(false);
            holder.btn_add.setEnabled(false);
        }else{
            //不是好友
            holder.btn_add.setText("添加");
            holder.btn_add.setEnabled(true);
            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onAddFriendClickListener!=null){
                        onAddFriendClickListener.onAddFriendClick(v,username);
                    }
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return users == null? 0:users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_username;
        TextView tv_addTime;
        Button btn_add;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_addTime = (TextView) itemView.findViewById(R.id.tv_regist_time);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);
        }
    }

    public void setOnAddFriendClickListener(AddFriendAdapter.onAddFriendClickListener onAddFriendClickListener) {
        this.onAddFriendClickListener = onAddFriendClickListener;
    }

    private onAddFriendClickListener onAddFriendClickListener;


    public interface onAddFriendClickListener{
        void onAddFriendClick(View v, String username);
    }
}
