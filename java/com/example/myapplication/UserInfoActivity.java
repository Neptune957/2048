package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends AppCompatActivity {
    private Button edit_info_button=null;
    private Button edit_password_button=null;
    private Button head_button=null;

    private SQLiteDatabase db =null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initButtons();
        initDatabase();
        bindListeners();
        initPageInfo();
    }

    public void initPageInfo(){
        Cursor cursor1=db.rawQuery("SELECT * FROM WhoIsLogining",null);
        cursor1.moveToNext();
        String who_is_logining=cursor1.getString(0);

        Cursor cursor2=db.rawQuery("SELECT name,userName,minutes,phone,email,times,winTimes FROM Player WHERE userName='"+who_is_logining+"'",null);
        cursor2.moveToNext();
        String name=cursor2.getString(0);
        String userName=cursor2.getString(1);
        int minutes=cursor2.getInt(2);
        String phone=cursor2.getString(3);
        String email=cursor2.getString(4);
        int times=cursor2.getInt(5);
        int winTimes=cursor2.getInt(6);

        ((TextView)findViewById(R.id.name)).setText(name);
        ((TextView)findViewById(R.id.userName)).setText(userName);
        ((TextView)findViewById(R.id.minutes)).setText(minutes+"分钟");
        ((TextView)findViewById(R.id.phone)).setText(phone);
        ((TextView)findViewById(R.id.email)).setText(email);
        ((TextView)findViewById(R.id.times)).setText(times+"局");
        ((TextView)findViewById(R.id.winTimes)).setText(winTimes+"局");
    }

    private void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }

    private void initButtons(){
        edit_info_button=(Button)findViewById(R.id.edit_info);
        edit_password_button=(Button)findViewById(R.id.edit_password);
        head_button=(Button)findViewById(R.id.head);
    }

    private void bindListeners(){
        edit_info_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    edit_info_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    edit_info_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    edit_info_button.setBackgroundResource(R.drawable.layer_list2);
                    Intent intent=new Intent();
                    intent.setClass(UserInfoActivity.this,EditInfoActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        edit_password_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    edit_password_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    edit_password_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    edit_password_button.setBackgroundResource(R.drawable.layer_list2);
                    Intent intent=new Intent();
                    intent.setClass(UserInfoActivity.this,EditPasswordActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        head_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    Intent intent=new Intent();
                    intent.setClass(UserInfoActivity.this,UserpageActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
