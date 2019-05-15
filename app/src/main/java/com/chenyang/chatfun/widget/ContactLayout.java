package com.chenyang.chatfun.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenyang.chatfun.R;
import com.chenyang.chatfun.widget.Slidebar;

/**
 * Created by fullcircle on 2017/1/3.
 */
public class ContactLayout extends RelativeLayout {

    private RecyclerView recyclerView;
    private TextView tv_float;
    private Slidebar slidebar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ContactLayout(Context context) {
        this(context,null);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        //加载xml文件为view对象
        View.inflate(getContext(), R.layout.contact_layout, this);
        //找到列表
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //找到中间显示首字母的textView
        tv_float = (TextView) findViewById(R.id.tv_float);
        //找到侧边快速滑动的菜单条
        slidebar = (Slidebar) findViewById(R.id.slidebar);
        //下拉刷新的控件
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs);
    }

    /**
     * 给recyclerView设置适配器
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void setRefreshing(boolean isRefreshing){
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }
}
