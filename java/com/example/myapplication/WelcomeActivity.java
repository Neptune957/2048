package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
    private Button start_button=null;
    private Button restart_button=null;
    private Button homepage_button=null;
    private Button rule_button=null;
    private Button standard_mode_button=null;
    private Button racing_mode_button=null;
    private Button wild_mode_button=null;
    private Button back_button=null;

    private String who_is_logining=null;

    private SQLiteDatabase db =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initDatabase();
        init_fields();
        bind_listeners();

    }

    private void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }

    private void init_fields(){
        Cursor cursor1=db.rawQuery("SELECT * FROM WhoIsLogining",null);
        cursor1.moveToNext();
        who_is_logining=cursor1.getString(0).trim();

        start_button=(Button)findViewById(R.id.start_button);
        restart_button=(Button)findViewById(R.id.restart_button);
        homepage_button=(Button)findViewById(R.id.homepage_button);
        rule_button=(Button)findViewById(R.id.rule_button);
        standard_mode_button=(Button)findViewById(R.id.standard_mode_button);
        standard_mode_button.setVisibility(View.GONE);
        racing_mode_button=(Button)findViewById(R.id.racing_mode_button);
        racing_mode_button.setVisibility(View.GONE);
        wild_mode_button=(Button)findViewById(R.id.wild_mode_button);
        wild_mode_button.setVisibility(View.GONE);
        back_button=(Button)findViewById(R.id.back_button);
        back_button.setVisibility(View.GONE);
    }

    private void bind_listeners(){
        start_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    start_button.setBackgroundResource(R.drawable.welcome_button_down_style);
                    start_button.setTextColor(Color.parseColor("#f6f2ef"));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    if(who_is_logining.equals("")){
                        Toast.makeText(WelcomeActivity.this, "请先登录！" , Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(WelcomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        return false;
                    }

                    start_button.setBackgroundResource(R.drawable.welcome_button_style);
                    start_button.setTextColor(Color.parseColor("#fbfaf8"));

                    start_button.setVisibility(View.GONE);
                    restart_button.setVisibility(View.GONE);
                    homepage_button.setVisibility(View.GONE);
                    rule_button.setVisibility(View.GONE);

                    back_button.setVisibility(View.VISIBLE);
                    standard_mode_button.setVisibility(View.VISIBLE);
                    racing_mode_button.setVisibility(View.VISIBLE);
                    wild_mode_button.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        restart_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    restart_button.setBackgroundResource(R.drawable.welcome_button_down_style);
                    restart_button.setTextColor(Color.parseColor("#f6f2ef"));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    restart_button.setBackgroundResource(R.drawable.welcome_button_style);
                    restart_button.setTextColor(Color.parseColor("#fbfaf8"));

                    if(who_is_logining.equals("")){
                        Toast.makeText(WelcomeActivity.this, "请先登录！" , Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(WelcomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        return false;
                    }

                    Cursor cursor=db.rawQuery("SELECT score,time,content,mode FROM Save WHERE userName='"+who_is_logining+"'",null);
                    if(cursor.getCount()==0){
                        Toast.makeText(WelcomeActivity.this, "游戏存档为空！" , Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent=new Intent();
                        intent.setClass(WelcomeActivity.this,SaveActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        homepage_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    homepage_button.setBackgroundResource(R.drawable.welcome_button_down_style);
                    homepage_button.setTextColor(Color.parseColor("#f6f2ef"));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    homepage_button.setBackgroundResource(R.drawable.welcome_button_style);
                    homepage_button.setTextColor(Color.parseColor("#fbfaf8"));

                    if(who_is_logining.equals("")){
                        Toast.makeText(WelcomeActivity.this, "请先登录！" , Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(WelcomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        return false;
                    }

                    Intent intent=new Intent();
                    intent.setClass(WelcomeActivity.this,UserpageActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        rule_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    rule_button.setBackgroundResource(R.drawable.welcome_button_down_style);
                    rule_button.setTextColor(Color.parseColor("#f6f2ef"));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    rule_button.setBackgroundResource(R.drawable.welcome_button_style);
                    rule_button.setTextColor(Color.parseColor("#fbfaf8"));
                    Intent intent=new Intent();
                    intent.putExtra("origin","welcome_page");
                    intent.setClass(WelcomeActivity.this,RulesActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        standard_mode_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    standard_mode_button.setBackgroundResource(R.drawable.welcome_button_down_style);
                    standard_mode_button.setTextColor(Color.parseColor("#f6f2ef"));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    standard_mode_button.setBackgroundResource(R.drawable.welcome_button_style);
                    standard_mode_button.setTextColor(Color.parseColor("#fbfaf8"));
                    Intent intent=new Intent();
                    intent.setClass(WelcomeActivity.this,StandardGameActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        racing_mode_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    racing_mode_button.setBackgroundResource(R.drawable.welcome_button_down_style);
                    racing_mode_button.setTextColor(Color.parseColor("#f6f2ef"));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    racing_mode_button.setBackgroundResource(R.drawable.welcome_button_style);
                    racing_mode_button.setTextColor(Color.parseColor("#fbfaf8"));
                    Intent intent=new Intent();
                    intent.setClass(WelcomeActivity.this,RacingGameActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        wild_mode_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    wild_mode_button.setBackgroundResource(R.drawable.welcome_button_down_style);
                    wild_mode_button.setTextColor(Color.parseColor("#f6f2ef"));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    wild_mode_button.setBackgroundResource(R.drawable.welcome_button_style);
                    wild_mode_button.setTextColor(Color.parseColor("#fbfaf8"));
                    Intent intent=new Intent();
                    intent.setClass(WelcomeActivity.this,WildGameActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    back_button.setBackgroundResource(R.drawable.welcome_button_down_style);
                    back_button.setTextColor(Color.parseColor("#f6f2ef"));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    back_button.setBackgroundResource(R.drawable.welcome_button_style);
                    back_button.setTextColor(Color.parseColor("#fbfaf8"));

                    start_button.setVisibility(View.VISIBLE);
                    restart_button.setVisibility(View.VISIBLE);
                    homepage_button.setVisibility(View.VISIBLE);
                    rule_button.setVisibility(View.VISIBLE);

                    back_button.setVisibility(View.GONE);
                    standard_mode_button.setVisibility(View.GONE);
                    racing_mode_button.setVisibility(View.GONE);
                    wild_mode_button.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }
}
