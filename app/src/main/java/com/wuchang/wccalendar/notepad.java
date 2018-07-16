package com.wuchang.wccalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class notepad extends AppCompatActivity
{
    private LinearLayout edit;

    private EditText bwTitle1;
    private EditText bwContent1;
    private EditText bwTitle2;
    private EditText bwContent2;
    private EditText bwTitle3;
    private EditText bwContent3;
    private EditText bwTitle4;
    private EditText bwContent4;

    private Button save;
    private Button empty;

    private ImageView back;
    private ImageView delete;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public  String date;

    private String bwt1;
    private String bwc1;
    private String bwt2;
    private String bwc2;
    private String bwt3;
    private String bwc3;
    private String bwt4;
    private String bwc4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad);

        /**实现沉浸式的效果*/
        if(Build.VERSION.SDK_INT >= 21)
        {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Intent intent = getIntent();
        //得到被选中的子项的日期信息
        date = intent.getStringExtra("selectedItem");

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        edit = (LinearLayout) findViewById(R.id.edit);
        bwTitle1 = (EditText) findViewById(R.id.bwTitle1);
        bwContent1 = (EditText) findViewById(R.id.bwContent1);
        bwTitle2 = (EditText) findViewById(R.id.bwTitle2);
        bwContent2 = (EditText) findViewById(R.id.bwContent2);
        bwTitle3 = (EditText) findViewById(R.id.bwTitle3);
        bwContent3 = (EditText) findViewById(R.id.bwContent3);
        bwTitle4 = (EditText) findViewById(R.id.bwTitle4);
        bwContent4 = (EditText) findViewById(R.id.bwContent4);
        save = (Button) findViewById(R.id.save);
        empty = (Button) findViewById(R.id.empty);
        back = (ImageView) findViewById(R.id.back);
        delete = (ImageView) findViewById(R.id.delete);

        //设置背景色的透明度
        edit.getBackground().setAlpha(90);

        /**为SAVE（保存）按钮注册点击事件*/
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bwt1 = bwTitle1.getText().toString();
                bwc1 = bwContent1.getText().toString();
                bwt2 = bwTitle2.getText().toString();
                bwc2 = bwContent2.getText().toString();
                bwt3 = bwTitle3.getText().toString();
                bwc3 = bwContent3.getText().toString();
                bwt4 = bwTitle4.getText().toString();
                bwc4 = bwContent4.getText().toString();

                editor = pref.edit();

                editor.putString(date + "bwt1", bwt1);
                editor.putString(date + "bwc1", bwc1);
                editor.putString(date + "bwt2", bwt2);
                editor.putString(date + "bwc2", bwc2);
                editor.putString(date + "bwt3", bwt3);
                editor.putString(date + "bwc3", bwc3);
                editor.putString(date + "bwt4", bwt4);
                editor.putString(date + "bwc4", bwc4);
                editor.apply();

                //添加到备忘列表（不重复）
                if (List.notes.indexOf(date) == -1)
                {
                    List.notes.add(date);
                }
            }
        });

        /**为EMPTY（清空按钮注册点击事件）*/
        empty.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bwTitle1.setText("");
                bwContent1.setText("");
                bwTitle2.setText("");
                bwContent2.setText("");
                bwTitle3.setText("");
                bwContent3.setText("");
                bwTitle4.setText("");
                bwContent4.setText("");
            }
        });


        bwt1 = pref.getString(date + "bwt1", "");
        bwc1 = pref.getString(date + "bwc1", "");
        bwt2 = pref.getString(date + "bwt2", "");
        bwc2 = pref.getString(date + "bwc2", "");
        bwt3 = pref.getString(date + "bwt3", "");
        bwc3 = pref.getString(date + "bwc3", "");
        bwt4 = pref.getString(date + "bwt4", "");
        bwc4 = pref.getString(date + "bwc4", "");

        bwTitle1.setText(bwt1);
        bwTitle1.setSelection(bwt1.length());

        bwContent1.setText(bwc1);
        bwContent1.setSelection(bwc1.length());

        bwTitle2.setText(bwt2);
        bwTitle2.setSelection(bwt2.length());

        bwContent2.setText(bwc2);
        bwContent2.setSelection(bwc2.length());

        bwTitle3.setText(bwt3);
        bwTitle3.setSelection(bwt3.length());

        bwContent3.setText(bwc3);
        bwContent3.setSelection(bwc3.length());

        bwTitle4.setText(bwt4);
        bwTitle4.setSelection(bwt4.length());

        bwContent4.setText(bwc4);
        bwContent4.setSelection(bwc4.length());


        /**给左上角的返回按钮注册点击事件*/
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //销毁当前活动，回到上一个活动
                finish();
            }
        });

        /**给右上角的删除按钮注册点击事件*/
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                editor = pref.edit();
                //“清空已存数据”
                editor.putString(date + "bwt1", "");
                editor.putString(date + "bwc1", "");
                editor.putString(date + "bwt2", "");
                editor.putString(date + "bwc2", "");
                editor.putString(date + "bwt3", "");
                editor.putString(date + "bwc3", "");
                editor.putString(date + "bwt4", "");
                editor.putString(date + "bwc4", "");
                editor.apply();

                //从notes中删除记录
                List.notes.remove(date);


                //销毁当前活动，回到上一个活动
                finish();
            }
        });
    }
}
