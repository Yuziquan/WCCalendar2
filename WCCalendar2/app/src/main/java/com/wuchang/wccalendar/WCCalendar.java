package com.wuchang.wccalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by 余梓权 on 2017/8/3.
 */

public class WCCalendar extends LinearLayout
{
    private TextView Sun;
    private TextView Mon;
    private TextView Tue;
    private TextView Wed;
    private TextView Thurs;
    private TextView Fri;
    private TextView Sat;

    private RelativeLayout calendarHeader;

    private ImageView preMonth;
    private ImageView nextMonth;

    private TextView ymDate;

    private GridView calendarDays;

    //main为日历主体界面的背景布局
    private RelativeLayout main;

    //作为数据查询
    public static Calendar currentDate = Calendar.getInstance();

    private String dateFormatDisplay;

    public WCCalendarListener listener;

    //以便作为农历日期/节日查询
    public LunarCalendar lunarCalendar = new LunarCalendar();

    //thisMonth代表黑色字体日期所在的月份
    public static int thisMonth = currentDate.get(Calendar.MONTH) + 1;

    //创建一个日历主体的监听器接口（最后由MainActivity类实现该接口中的方法）
    public interface WCCalendarListener
    {
        void onItemClick(Date day, View view);
    }


    public WCCalendar(Context context)
    {
        super(context);
    }

    public WCCalendar(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context, attrs);
    }


    public WCCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs)
    {
        //绑定控件
        bindControl(context);


        //绑定事件监听器
        bindControlEvents();

        //读取日历顶部日期的自定义格式
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.WCCalendar);
        try
        {
            String format = ta.getString(R.styleable.WCCalendar_dateFormat);
            dateFormatDisplay = format;
            if (dateFormatDisplay == null)
            {
                dateFormatDisplay = "MMM yyyy";
            }
        }
        finally
        {
            //读取之后回收ta
            ta.recycle();
        }

        //渲染日历视图
        renderCalendarView();
    }


    /**找出控件的实例*/
    private void bindControl(Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view, this);

        preMonth = (ImageView) findViewById(R.id.preMonth);
        nextMonth = (ImageView) findViewById(R.id.nextMonth);

        ymDate = (TextView) findViewById(R.id.ymDate);
        calendarDays = (GridView) findViewById(R.id.calendarDays);
        main = (RelativeLayout)findViewById(R.id.MainActivity);

        Sun = (TextView)findViewById(R.id.Sun);
        Mon = (TextView)findViewById(R.id.Mon);
        Tue = (TextView)findViewById(R.id.Tue);
        Wed = (TextView)findViewById(R.id.Wed);
        Thurs = (TextView)findViewById(R.id.Thurs);
        Fri = (TextView)findViewById(R.id.Fri);
        Sat = (TextView)findViewById(R.id.Sat);

        calendarHeader = (RelativeLayout)findViewById(R.id.calendarHeader);
    }



    private void bindControlEvents()
    {
        /**给左上角的翻页按钮注册点击事件*/
        preMonth.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //日历向前翻一个月
                currentDate.add(Calendar.MONTH, -1);

                //一月之前是十二月
                if(thisMonth - 1 == 0) thisMonth = 12;
                else  thisMonth = thisMonth - 1;

                //渲染日历视图
                renderCalendarView();
            }
        });

        /**给右上角的翻页按钮注册点击事件*/
        nextMonth.setOnClickListener(
                new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //日历向后翻一个月
                currentDate.add(Calendar.MONTH, 1);

                //十二月之后是一月
                if(thisMonth + 1 == 13) thisMonth = 1;
                else thisMonth = thisMonth + 1;

                //渲染日历视图
                renderCalendarView();
            }
        });
    }

    /**渲染日历视图*/
    public void renderCalendarView()
    {
        //渲染日历顶部文本
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatDisplay);
        ymDate.setText(sdf.format(currentDate.getTime()));

        //设置背景透明度
        Sun.getBackground().setAlpha(100);
        Mon.getBackground().setAlpha(100);
        Tue.getBackground().setAlpha(100);
        Wed.getBackground().setAlpha(100);
        Thurs.getBackground().setAlpha(100);
        Fri.getBackground().setAlpha(100);
        Sat.getBackground().setAlpha(100);
        calendarHeader.getBackground().setAlpha(100);

        //渲染日历主体

        //准备好要填充当前日历主体界面的子项（均为Date类型）
        ArrayList<Date> cells = new ArrayList<>();

        //新建一个副本
        Calendar currentDate1 = (Calendar) currentDate.clone();

        //将currentDate设置为该月的一号
        currentDate1.set(Calendar.DAY_OF_MONTH, 1);

        //得到此时显示的日历主体界面中位于该月一号前的不属于该月的子项的个数
        int previousDays = currentDate1.get(Calendar.DAY_OF_WEEK) - 1;

        //先移到到日历主体界面最左上角的子项日期
        currentDate1.add(Calendar.DAY_OF_MONTH, -previousDays);

        //至少需要6行（可自行思考）
        int maxCellsSum = 6 * 7;

        //填充42个子项到cells中
        while (cells.size() < maxCellsSum)
        {
            cells.add(currentDate1.getTime());
            currentDate1.add(Calendar.DAY_OF_MONTH, 1);
        }

        //把cells扔给adapter
        calendarDays.setAdapter(new CalendarAdapter(getContext(), cells));

        /**给日历主体的每个子项注册点击事件*/
        calendarDays.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                //使用我们自己定义的onItemClick方法，我们只需获取两个最重要的数据就够了
                listener.onItemClick(((Date) adapterView.getItemAtPosition(position)), view);
            }
        });
    }


    /**实现一个日历的adapter*/
    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        LayoutInflater inflater;

        public CalendarAdapter(@NonNull Context context, ArrayList<Date> calendarDays)
        {
            super(context, R.layout.calendar_item, calendarDays);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            //day为当前被渲染的日期
            Date day = getItem(position);

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.calendar_item, parent, false);
            }

            //新建一个副本
            Calendar currentDate2 = Calendar.getInstance();
            currentDate2.setTime(day);

            /**以下对日历的各个item的字体颜色进行设置，规则为：
              1.若item属于本月且不为星期六/日，设置为黑色
              2.若items属于本月且为星期六/日，设置为橙色
              3.若item不属于本月，统统设置为灰色*/

            if (currentDate2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || currentDate2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            {
                //设置橙色
                ((CalendarItemTextView) convertView).setTextColor(Color.parseColor("#FF7F00"));
            }

            if ((day.getMonth() + 1) == thisMonth)
            {
                if (!(currentDate2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || currentDate2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY))
                {
                    //设置黑色
                    ((CalendarItemTextView) convertView).setTextColor(Color.parseColor("#000000"));
                }
            } else
            {
                //设置灰色
                ((CalendarItemTextView) convertView).setTextColor(Color.parseColor("#A8A8A8"));
            }


            //为每个月份设置相应的背景图
            if (thisMonth == 1)
            {
                main.setBackgroundResource(R.drawable.b1);
                main.getBackground().setAlpha(180);
            } else if (thisMonth == 2)
            {
                main.setBackgroundResource(R.drawable.b2);
                main.getBackground().setAlpha(120);

            } else if (thisMonth == 3)
            {
                main.setBackgroundResource(R.drawable.b3);
                main.getBackground().setAlpha(120);

            } else if (thisMonth == 4)
            {
                main.setBackgroundResource(R.drawable.b4);
                main.getBackground().setAlpha(200);

            } else if (thisMonth == 5)
            {
                main.setBackgroundResource(R.drawable.b5);
                main.getBackground().setAlpha(150);

            } else if (thisMonth == 6)
            {
                main.setBackgroundResource(R.drawable.b6);
                main.getBackground().setAlpha(120);

            } else if (thisMonth == 7)
            {
                main.setBackgroundResource(R.drawable.b7);
                main.getBackground().setAlpha(120);

            } else if (thisMonth == 8)
            {
                main.setBackgroundResource(R.drawable.b8);
                main.getBackground().setAlpha(120);
            } else if (thisMonth == 9)
            {
                main.setBackgroundResource(R.drawable.b9);
                main.getBackground().setAlpha(120);

            } else if (thisMonth == 10)
            {
                main.setBackgroundResource(R.drawable.b10);
                main.getBackground().setAlpha(120);

            } else if (thisMonth == 11)
            {
                main.setBackgroundResource(R.drawable.b11);
                main.getBackground().setAlpha(120);

            } else if (thisMonth == 12)
            {
                main.setBackgroundResource(R.drawable.b12);
                main.getBackground().setAlpha(120);

            }


            //获取当前被渲染的日期的号数
            int curDate = day.getDate();


            //当前被渲染的日期的农历表示

            /**使用
            String LunarDay = lunarCalendar.getLunarDate(day.getYear(), day.getMonth() + 1, day.getDate(), false);
            会报错*/

            //传入false，则成为能显示节假日的版本
            String LunarDay = lunarCalendar.getLunarDate(currentDate2.get(Calendar.YEAR), currentDate2.get(Calendar.MONTH)+1,
                    currentDate2.get(Calendar.DAY_OF_MONTH),false);
            ((CalendarItemTextView) convertView).setText(String.valueOf(curDate) + "\n" + LunarDay);

            //获取真正的当前日期
            Date now = new Date();

            //如果是绘制的是当前日期
            if (day.getDate() == now.getDate() && day.getMonth() == now.getMonth()
                    && day.getYear() == now.getYear())
            {
                //给这个日期子项外围画一个红圈
                ((CalendarItemTextView) convertView).isToday = true;
            }

            return convertView;
        }
    }
}





















