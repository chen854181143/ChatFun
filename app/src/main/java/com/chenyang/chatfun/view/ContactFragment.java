package com.chenyang.chatfun.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenyang.chatfun.R;
import com.chenyang.chatfun.adapter.ContactAdapter;
import com.chenyang.chatfun.event.ContactChangeEvent;
import com.chenyang.chatfun.presenter.ContactPresenter;
import com.chenyang.chatfun.presenter.impl.ContactPresenterImpl;
import com.chenyang.chatfun.utils.ToastUtils;
import com.chenyang.chatfun.widget.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by fullcircle on 2017/1/3.
 */
public class ContactFragment extends BaseFragment implements ContactView {

    private ContactLayout contactLayout;
    private ContactPresenter presenter;
    private ContactAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        contactLayout = (ContactLayout) view.findViewById(R.id.contactlayout);
        //创建p层的对象
        presenter = new ContactPresenterImpl(this);
        //初始化联系人数据
        presenter.initContact();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onInitContact(List<String> contact) {
        //获取了初始化数据 需要通过recyclerview进行展示
        //setAdapter
        adapter = new ContactAdapter(contact);
        contactLayout.setAdapter(adapter);
        contactLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.updateContact();
            }
        });
        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onclick(View v, String username) {
                Intent intent = new Intent(getContext(),ChatActivity.class);
                intent.putExtra("contact",username);
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(View v, final String username) {
                Snackbar.make(contactLayout,"真的要删除"+username+"吗?",Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击确定删除联系人
                                presenter.deleteContact(username);
                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public void onUpdateContact(List<String> contact, boolean isUpdateSuccess, String errorMsg) {
        //设置刷新的状态为 不显示
        contactLayout.setRefreshing(false);
        if(isUpdateSuccess){
            //更新adapter中的联系人数据
        adapter.setContacts(contact);
            //刷新界面
        adapter.notifyDataSetChanged();
        }else{
            //更新失败了
            ToastUtils.showToast(getActivity(),"新联系人失败!");
        }
    }

    @Override
    public void onDeleteContact(boolean isDeleteSuccess, String errorMsg) {
        if(isDeleteSuccess){
            ToastUtils.showToast(getContext(),"删除成功");
        }else{
            ToastUtils.showToast(getContext(),"删除失败:"+errorMsg);
        }
    }

    /**
     * POSTING 发布消息的线程和处理消息的线程 是同一个线程
     * Main  在主线程中处理消息
     * ASYNC 处理消息的线程 和发布消息的线程不同一个线程中 如果发布消息在子线程中 会再开一个子线程来处理消息
     * Backgound 在子线程中处理消息 如果发布消息是在子线程发布的就在这个线程中处理
     */
        @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetContactChangeEvent(ContactChangeEvent event){
            //收到这个消息 说明有联系人的变化 需要联网更新数据
        presenter.updateContact();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
