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
import android.widget.Toast;

public class UserpageActivity extends AppCompatActivity {
    private Button user_button=null;
    private Button balance_button=null;
    private Button history_button=null;
    private Button logout_button =null;
    private Button rank_button=null;
    private Button more_button=null;
    private Button continue_button=null;
    private Button info_button=null;
    private Button head_button=null;

    private String who_is_logining=null;

    private SQLiteDatabase db =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        initDatabase();
        initFields();
        bindListeners();
        initPageInfo();
    }

    private void initFields(){
        user_button=(Button)findViewById(R.id.user);
        balance_button=(Button)findViewById(R.id.balance);
        history_button=(Button)findViewById(R.id.history);
        logout_button =(Button)findViewById(R.id.logout);
        rank_button=(Button)findViewById(R.id.rank);
        more_button=(Button)findViewById(R.id.more);
        continue_button=(Button)findViewById(R.id.continueGame);
        info_button=(Button)findViewById(R.id.info);
        head_button=(Button)findViewById(R.id.head);

        Cursor cursor1=db.rawQuery("SELECT * FROM WhoIsLogining",null);
        cursor1.moveToNext();
        who_is_logining=cursor1.getString(0);
    }

    //初始化页面具体信息
    private void initPageInfo(){
        Cursor cursor2=db.rawQuery("SELECT name FROM Player WHERE userName='"+who_is_logining+"'",null);
        cursor2.moveToNext();
        String nickname=cursor2.getString(0);

        user_button.setText("    "+nickname);
    }

    private void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }


    private void bindListeners(){
        user_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //

                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    Intent intent=new Intent();
                    intent.setClass(UserpageActivity.this,UserInfoActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        balance_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    balance_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    balance_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    balance_button.setBackgroundResource(R.drawable.layer_list);
                    Intent intent=new Intent();
                    intent.setClass(UserpageActivity.this,BalanceActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        history_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    history_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    history_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    history_button.setBackgroundResource(R.drawable.layer_list2);
                    Intent intent=new Intent();
                    intent.setClass(UserpageActivity.this,GameHistoryActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        rank_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    rank_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    rank_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    rank_button.setBackgroundResource(R.drawable.layer_list2);
                    Intent intent=new Intent();
                    intent.setClass(UserpageActivity.this,RankActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        more_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    more_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    more_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    more_button.setBackgroundResource(R.drawable.layer_list2);
                    Intent intent=new Intent();
                    intent.putExtra("origin","user_page");
                    intent.setClass(UserpageActivity.this,RulesActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        continue_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    continue_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    continue_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    continue_button.setBackgroundResource(R.drawable.layer_list2);

                    Cursor cursor=db.rawQuery("SELECT score,time,content,mode FROM Save WHERE userName='"+who_is_logining+"'",null);
                    if(cursor.getCount()==0){
                        Toast.makeText(UserpageActivity.this, "游戏存档为空！" , Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent=new Intent();
                        intent.setClass(UserpageActivity.this,SaveActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        info_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    info_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    info_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    info_button.setBackgroundResource(R.drawable.layer_list);
                    Intent intent=new Intent();
                    intent.setClass(UserpageActivity.this,DeveloperInfoActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        logout_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    logout_button.setBackgroundColor(Color.rgb(232, 94, 108));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    logout_button.setBackgroundColor(Color.rgb(255, 92, 108));
                    db.execSQL("UPDATE WhoIsLogining SET userName=''");

                    Toast.makeText(UserpageActivity.this, "登出成功！" , Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(UserpageActivity.this,WelcomeActivity.class);
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
                    intent.setClass(UserpageActivity.this,WelcomeActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
