package com.wuchang.wccalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class CalendarItemTextView extends android.support.v7.widget.AppCompatTextView
{
    //isToday用于判断是否该子项对应日期等于当前日期
    public boolean isToday = false;
    private Paint paint = new Paint();


    public CalendarItemTextView(Context context)
    {
        super(context);
    }

    public CalendarItemTextView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initControl();
    }

    public CalendarItemTextView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initControl();
    }

    //先准备好自定义的画笔，设置好样式
    private void initControl()
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth((float)2.6);
        paint.setColor(Color.RED);
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
       //若该子项对应日期等于当前日期
        if(isToday)
        {
            //将canvas的原点移到当前TextView的中心点的位置
            canvas.translate(getWidth() / 2, getHeight() / 2);
            //描绘一个红色圆圈
            canvas.drawCircle(0, 0, getWidth() / 2, paint);
        }
    }

}
