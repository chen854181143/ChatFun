package com.chenyang.chatfun.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenyang.chatfun.R;
import com.chenyang.chatfun.utils.StringUtils;

import java.util.List;

/**
 * Created by fullcircle on 2017/1/3.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public List<String> getContacts() {
        return contacts;
    }

    private List<String> contacts;

    public ContactAdapter(List<String> contacts) {
        this.contacts = contacts;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //在这个方法中 创建View对象 并且跟MyViewHolder建立关联
       // View view = View.inflate(parent.getContext(), R.layout.list_contact_item, null);
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //通过viewholder 给展示数据的控件设置数据
        final String contact = contacts.get(position);
        holder.tv_contact.setText(contact);
        holder.tv_section.setText(StringUtils.getFirstChar(contact));
        if(position == 0){
            holder.tv_section.setVisibility(View.VISIBLE);
        }else{
            String current = StringUtils.getFirstChar(contact);
            String last = StringUtils.getFirstChar(contacts.get(position-1));
            if(current.equals(last)){
                //当前条目的首字母跟上个条目一样 不需要展示首字母的textview
                holder.tv_section.setVisibility(View.GONE);
            }else{
                //当前条目的首字母跟上个条目不一样 需要展示首字母的textview
                holder.tv_section.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onclick(v,contact);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onLongClick(v,contact);
                    return true;
                }
                return false;
            }
        });

    }
    public interface OnItemClickListener{
        void onclick(View v, String username);
        boolean onLongClick(View v, String username);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    @Override
    public int getItemCount() {
        return contacts==null?0:contacts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_section;
        TextView tv_contact;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_section = (TextView) itemView.findViewById(R.id.tv_section);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_contact);
        }
    }
}
