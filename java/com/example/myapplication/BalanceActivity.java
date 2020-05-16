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

public class BalanceActivity extends AppCompatActivity {
    private Button charge_button=null;
    private Button record_button=null;
    private Button head_button=null;

    private String who_is_logining=null;

    private SQLiteDatabase db =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        initDatabase();
        initFields();
        initPageInfo();
        bindListeners();
    }

    private void initFields(){
        charge_button=(Button)findViewById(R.id.charge);
        record_button=(Button)findViewById(R.id.record);
        head_button=(Button)findViewById(R.id.head);

        Cursor cursor1=db.rawQuery("SELECT * FROM WhoIsLogining",null);
        cursor1.moveToNext();
        who_is_logining=cursor1.getString(0);
    }

    public void initPageInfo(){
        Cursor cursor2=db.rawQuery("SELECT balance FROM Player WHERE userName='"+who_is_logining+"'",null);
        cursor2.moveToNext();
        String balance=cursor2.getString(0);

        TextView balance_field=(TextView)findViewById(R.id.balance_field);
        balance_field.setText(balance);
    }

    public void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }

    private void bindListeners(){

        charge_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    charge_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    charge_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    charge_button.setBackgroundResource(R.drawable.layer_list2);
                    Intent intent=new Intent();
                    intent.setClass(BalanceActivity.this,ChargeActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        record_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    record_button.setBackgroundColor(Color.rgb(230, 230, 230));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    record_button.setBackgroundColor(Color.rgb(255, 255, 255));
                    record_button.setBackgroundResource(R.drawable.layer_list2);
                    Intent intent=new Intent();
                    intent.setClass(BalanceActivity.this,BilllistActivity.class);
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
                    intent.setClass(BalanceActivity.this,UserpageActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
