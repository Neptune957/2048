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

import java.util.*;

public class ChargeActivity extends AppCompatActivity {
    private Button confirm_button=null;
    private Button head_button=null;
    private SQLiteDatabase db =null;

    private String who_is_logining=null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        initDatabase();
        initFields();
        bindButtons();
    }

    private void initFields(){
        confirm_button=(Button)findViewById(R.id.confirm);
        head_button=(Button)findViewById(R.id.head);

        Cursor cursor1=db.rawQuery("SELECT * FROM WhoIsLogining",null);
        cursor1.moveToNext();
        who_is_logining=cursor1.getString(0);
    }

    private void bindButtons(){
        confirm_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    confirm_button.setBackgroundColor(Color.rgb(5, 109, 214));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    confirm_button.setBackgroundColor(Color.rgb(0, 122, 245));

                    TextView add_field=(TextView)findViewById(R.id.add_field);
                    int add_money=Integer.parseInt(add_field.getText().toString());

                    db.execSQL("UPDATE Player SET balance=balance+"+add_money+" WHERE userName='"+who_is_logining+"'");

                    Cursor cursor2=db.rawQuery("SELECT balance FROM Player WHERE userName='"+who_is_logining+"'",null);
                    cursor2.moveToNext();
                    double new_balance=cursor2.getDouble(0);

                    Calendar c = Calendar.getInstance();
                    int now_year=c.get(Calendar.YEAR);
                    int now_month=c.get(Calendar.MONTH)+1;
                    int now_date=c.get(Calendar.DATE);
                    String stop_day=""+now_year+"-"+now_month+"-"+now_date;

                    db.execSQL("INSERT INTO BalanceHistory VALUES('"+who_is_logining+"','余额充值','余额:"+new_balance+"元','"+stop_day+"','+"+add_money+"元')");

                    Toast.makeText(ChargeActivity.this, "充值成功！" , Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(ChargeActivity.this,BalanceActivity.class);
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
                    intent.setClass(ChargeActivity.this,BalanceActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }


    public void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }
}
