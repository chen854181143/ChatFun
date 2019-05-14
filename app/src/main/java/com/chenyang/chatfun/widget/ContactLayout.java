package com.chenyang.chatfun.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenyang.chatfun.R;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class ContactLayout extends RelativeLayout {

    private RecyclerView recyclerView;
    private TextView tv_float;
    private Slidebar slidebar;

    public ContactLayout(Context context) {
        this(context,null);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.contact_layout, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tv_float = (TextView) findViewById(R.id.tv_float);
        slidebar = (Slidebar) findViewById(R.id.slidebar);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
