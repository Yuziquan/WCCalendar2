package com.wuchang.wccalendar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements WCCalendar.WCCalendarListener
{
    private FloatingActionButton fab;
    private WCCalendar wccalendar;
    private FloatingActionButton list;
    private View selectedView = null;
    private Date preDate;
    private int theSameMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**实现沉浸式的效果*/
        if(Build.VERSION.SDK_INT >= 21)
        {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        wccalendar = (WCCalendar) findViewById(R.id.wccalendar);
        wccalendar.listener = this;

        /**给左下角的日程列表按钮注册点击事件*/
        list = (FloatingActionButton)findViewById(R.id.notes);
        list.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //跳转到日程列表（List）界面
                Intent intent = new Intent(MainActivity.this, List.class);
                startActivity(intent);
            }
        });
    }


    /**给日历的主体部分的每个子项注册点击事件*/
    @Override
    public void onItemClick(Date day, View view)
    {
        /**恢复之前选中的日期项的颜色设置
        selectedView为之前被选中的子项的view
        preDate为之前被选中的子项所对应的日期
        theSameMonth为之前被选中的子项所在页面的月份（不一定是子项日期所在的月份）*/
        if (selectedView != null)
        {
            selectedView.setBackgroundResource(0);

            //将Date格式的preDate转换为Calendar格式的currentDate
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(preDate);

            //如果之前被选中的子项日期属于所在页面的月份
            if ((preDate.getMonth() + 1) == theSameMonth)
            {
                if (!(currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY))
                {
                    //还原为黑色
                    ((CalendarItemTextView) selectedView).setTextColor(Color.parseColor("#000000"));
                } else
                {
                    //还原为橙色
                    ((CalendarItemTextView) selectedView).setTextColor(Color.parseColor("#FF7F00"));
                }
            }
            ////如果之前被选中的子项日期不属于所在页面的月份
            else
            {
                //还原为灰色
                ((CalendarItemTextView) selectedView).setTextColor(Color.parseColor("#A8A8A8"));
            }
        }

        //更新selectedView 、preDate、theSameMonth
        selectedView = view;
        preDate = day;
        theSameMonth = wccalendar.thisMonth;


        /**设置当前被选中的子项的状态变化*/

        final Date date = day;

        //被选中的子项的背景色设置为半透明红色，字体设置为白色
        view.setBackgroundColor(Color.parseColor("#FF0000"));
        view.getBackground().setAlpha(80);
        ((CalendarItemTextView) view).setTextColor(Color.WHITE);

        //Toast被选中子项的日期
        SimpleDateFormat sdf = new SimpleDateFormat("当前日期为 yyyy / MM / dd");
        Toast.makeText(this, sdf.format(day), Toast.LENGTH_SHORT).show();

        /**当点击（选中）某个子项后，点击右边的日程记录按钮才会得到相应，否则不会响应*/

        /**给右下角的日程记录按钮注册点击事件*/
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /**传递被选中的子项对应日期给notepad，以到达相应的日程记录（notepad）界面*/

                Intent intent = new Intent(MainActivity.this, notepad.class);

                //转换为Calendar对象
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                String time = " *   Be edited in     " + year + " 年 " + month + " 月 " + dayOfMonth + " 日 ";
                intent.putExtra("selectedItem", time);
                startActivity(intent);
            }
        });
    }

}