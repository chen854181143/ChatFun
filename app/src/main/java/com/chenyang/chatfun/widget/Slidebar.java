package com.chenyang.chatfun.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.chenyang.chatfun.R;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class Slidebar extends View {
    private String[] sections = {"搜","A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private Paint paint;
    private int x;
    private int viewHeight;

    public Slidebar(Context context) {
        this(context,null);
    }

    public Slidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.GRAY);
        paint.setTextSize(getResources().getDimension(R.dimen.slide_text_size));
    }

    public Slidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Slidebar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        x = w/2;
        viewHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       for(int i = 0;i<sections.length;i++){
           canvas.drawText(sections[i],x,viewHeight/sections.length *(i+1),paint);
       }
    }
}
