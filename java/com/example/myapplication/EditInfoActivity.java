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

public class EditInfoActivity extends AppCompatActivity {
    private Button confirm_button=null;
    private Button head_button=null;

    private SQLiteDatabase db =null;

    private String who_is_logining=null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        initButtons();
        initDatabase();
        bindButtons();
        initPageInfo();
    }

    private void initButtons(){
        confirm_button=(Button)findViewById(R.id.confirm);
        head_button=(Button)findViewById(R.id.head);
    }

    private void initPageInfo(){
        Cursor cursor2=db.rawQuery("SELECT name,email,phone FROM Player WHERE userName='"+who_is_logining+"'",null);
        cursor2.moveToNext();
        String name=cursor2.getString(0);
        String email=cursor2.getString(1);
        String phone=cursor2.getString(2);

        ((TextView)findViewById(R.id.name)).setText(name);
        ((TextView)findViewById(R.id.email)).setText(email);
        ((TextView)findViewById(R.id.phone)).setText(phone);
    }

    private void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);

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

                    String name=((TextView)findViewById(R.id.name)).getText().toString().trim();
                    String email=((TextView)findViewById(R.id.email)).getText().toString().trim();
                    String phone=((TextView)findViewById(R.id.phone)).getText().toString().trim();

                    if(name.equals("")||email.equals("")||phone.equals("")){
                        Toast.makeText(EditInfoActivity.this, "请确认是否填写完毕" , Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    db.execSQL("UPDATE Player SET name='"+name+"',email='"+email+"',phone='"+phone+"' WHERE userName='"+who_is_logining+"'");
                    Toast.makeText(EditInfoActivity.this, "修改成功！" , Toast.LENGTH_SHORT).show();

                    db.execSQL("UPDATE StandardRank SET name='"+name+"' WHERE userName='"+who_is_logining+"'");
                    db.execSQL("UPDATE RacingRank SET name='"+name+"' WHERE userName='"+who_is_logining+"'");
                    db.execSQL("UPDATE WildRank SET name='"+name+"' WHERE userName='"+who_is_logining+"'");

                    Intent intent=new Intent();
                    intent.setClass(EditInfoActivity.this,UserInfoActivity.class);
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
                    intent.setClass(EditInfoActivity.this,UserInfoActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

}
