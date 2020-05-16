package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class RulesActivity extends AppCompatActivity {
    private Button head_button=null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        initFields();
        bindListener();
    }

    private void initFields(){
        head_button=(Button)findViewById(R.id.head);
    }

    private void bindListener(){
        head_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    Intent intent=getIntent();
                    String origin=intent.getStringExtra("origin");
                    if(origin.equals("user_page")){
                        intent.setClass(RulesActivity.this,UserpageActivity.class);
                        startActivity(intent);
                    }else{
                        intent.setClass(RulesActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }
}
