package com.chenyang.chatfun.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chenyang.chatfun.R;
import com.chenyang.chatfun.adapter.ChatAdapter;
import com.chenyang.chatfun.presenter.ChatPresenter;
import com.chenyang.chatfun.presenter.impl.ChatPresenterImpl;
import com.chenyang.chatfun.view.BaseActivity;
import com.hyphenate.chat.EMMessage;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fullcircle on 2017/1/4.
 */

public class ChatActivity extends BaseActivity implements ChatView{

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @InjectView(R.id.rv_chat)
    RecyclerView rvChat;
    @InjectView(R.id.et_message)
    EditText etMessage;
    @InjectView(R.id.btn_send)
    Button btnSend;

    private ChatPresenter presenter;
    private ChatAdapter adapter;
    private String contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        initToolbar();
        contact = getIntent().getStringExtra("contact");
        tvTitle.setText("与"+ contact +"聊天中");
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("ChatActivity",s+"start:::"+start+"before:::"+before+"count:::"+count);
                if(s.length()==0){
                    btnSend.setEnabled(false);
                }else{
                    btnSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        presenter = new ChatPresenterImpl(this);
        //初始化recyclerview
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(null);
        rvChat.setAdapter(adapter);
        presenter.getChatHistoryMessage(contact);
    }

    private void initToolbar() {
        tbToolbar.setTitle("");
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btn_send)
    public void onClick() {
        String msgText = etMessage.getText().toString();
        //通过presenter处理消息发送的逻辑
        presenter.sendMessage(msgText,contact);
        //把edittext清空
        etMessage.setText("");
    }

    @Override
    public void getHistoryMessage(List<EMMessage> emMessages) {
        //给适配器设置数据
        adapter.setMessages(emMessages);
//        刷新界面
        adapter.notifyDataSetChanged();
        if(adapter.getItemCount()>0){
            rvChat.smoothScrollToPosition(adapter.getItemCount()-1);
        }
    }

    @Override
    public void updateList() {
        // 刷新界面
        adapter.notifyDataSetChanged();
        if(adapter.getItemCount()>0){
            rvChat.smoothScrollToPosition(adapter.getItemCount()-1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onGetMessageEvent(List<EMMessage> messages){
        presenter.getChatHistoryMessage(contact);
    }

}
