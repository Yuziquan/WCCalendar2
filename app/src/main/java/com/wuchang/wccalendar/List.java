package com.wuchang.wccalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class List extends AppCompatActivity
{
    ArrayAdapter<String> adapter;
    private LinearLayout  list;
    private ImageView back;
    private int notesSize;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

   //用于存储已经记录的日程
    public static ArrayList<String> notes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        /**实现沉浸式的效果*/
        if(Build.VERSION.SDK_INT >= 21)
        {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        notesSize = pref.getInt("notesSize", 0);

        //保证列表的每一项都是不同的日期的日程，避免重复
        for(int i = 0; i < notesSize; i++)
        {
            String date = pref.getString(String.valueOf(i), "");
            if(notes.indexOf(date) == -1)
            {
                notes.add(date);
            }
        }

        //设置list背景为半透明样式
        list = (LinearLayout) findViewById(R.id.list);
        list.getBackground().setAlpha(90);

        /**给左上角的返回按钮注册点击事件*/
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //销毁当前活动，回到上一个活动
                finish();
            }
        });

        adapter = new ArrayAdapter<String>(List.this, android.R.layout.simple_list_item_1, notes);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        /**给列表的每一项注册点击事件，实现点击之后能到达相应的日程记录（notepad）界面*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                String date = notes.get(position);
                Intent intent = new Intent(List.this, notepad.class);
                //传递给notepad.class相应的日期数据，以便以此打开对应日期的日程记录（notepad）界面
                intent.putExtra("selectedItem", date);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onRestart()
    {
        super.onRestart();

        //通知刷新一下日程列表
        adapter.notifyDataSetChanged();
    }

    //活动销毁之前保存好notes中的数据
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        for(int i = 0; i < notes.size(); i++)
        {
            editor.putString(String.valueOf(i), notes.get(i));
        }
        notesSize = notes.size();
        editor.putInt("notesSize", notesSize);
        editor.apply();
    }
}
