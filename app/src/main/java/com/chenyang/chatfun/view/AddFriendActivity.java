package com.chenyang.chatfun.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.chenyang.chatfun.R;
import com.chenyang.chatfun.adapter.AddFriendAdapter;
import com.chenyang.chatfun.presenter.AddFriendPresenter;
import com.chenyang.chatfun.presenter.impl.AddFriendPresenterImpl;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fullcircle on 2017/1/4.
 */

public class AddFriendActivity extends BaseActivity implements AddFriendView {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @InjectView(R.id.rv_addfriend)
    RecyclerView rvAddfriend;
    @InjectView(R.id.iv_nodata)
    ImageView ivNodata;
    private SearchView searchView;
    private AddFriendPresenter presenter;
    private AddFriendAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.inject(this);
        initToolbar();

        presenter = new AddFriendPresenterImpl(this);
    }

    private void initToolbar() {
        tbToolbar.setTitle("");
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_friend, menu);
        //找到包含searchview的菜单项
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        //通过菜单项获取actionView 强转成searchView
        searchView = (SearchView) menuItem.getActionView();
        //设置搜索框的提示
        searchView.setQueryHint("搜索好友");
        //给searchView添加搜索文字变化的监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //按了搜索按钮就会走这个方法
                //创建适配器
                if (adapter == null) {
                    adapter = new AddFriendAdapter(null, null);
                    rvAddfriend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvAddfriend.setAdapter(adapter);
                    adapter.setOnAddFriendClickListener(new AddFriendAdapter.onAddFriendClickListener() {
                        @Override
                        public void onAddFriendClick(View v, String username) {
                            presenter.addFriend(username);
                        }
                    });
                }
                //到服务端查询好友
                presenter.searchFriend(query);
                //隐藏软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(rvAddfriend.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索框的文字有变化就会走这个方法
                if (!TextUtils.isEmpty(newText)) {
                    showToast(newText);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onQuerySuccess(List<AVUser> list, List<String> users, boolean isSuccess, String errorMeg) {
        if (isSuccess) {
            adapter.setContacts(users);
            adapter.setUsers(list);
            adapter.notifyDataSetChanged();
            ivNodata.setVisibility(View.GONE);
            rvAddfriend.setVisibility(View.VISIBLE);
        } else {
            showToast(errorMeg);
            rvAddfriend.setVisibility(View.GONE);
            ivNodata.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetAddFriendResult(boolean b, String message) {
        if (b) {
            showToast("添加好友成功");
        } else {
            showToast(message);
        }
    }
}
