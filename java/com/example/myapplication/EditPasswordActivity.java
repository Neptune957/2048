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

public class EditPasswordActivity extends AppCompatActivity {
    private Button confirm_button=null;
    private Button head_button=null;

    private String who_is_logining=null;
    private String old_password=null;

    private SQLiteDatabase db =null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        initDatabase();
        initFields();
        bindListeners();
    }

    private void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }

    private void initFields(){
        Cursor cursor1=db.rawQuery("SELECT * FROM WhoIsLogining",null);
        cursor1.moveToNext();
        who_is_logining=cursor1.getString(0);

        Cursor cursor2=db.rawQuery("SELECT userPassword FROM Player WHERE userName='"+who_is_logining+"'",null);
        cursor2.moveToNext();
        old_password=cursor2.getString(0);

        confirm_button=(Button)findViewById(R.id.confirm);
        head_button=(Button)findViewById(R.id.head);
    }


    private void bindListeners(){
        confirm_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    confirm_button.setBackgroundColor(Color.rgb(5, 109, 214));
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    confirm_button.setBackgroundColor(Color.rgb(0, 122, 245));

                    String old_password_entered=((TextView)findViewById(R.id.oldPassword)).getText().toString().trim();
                    String new_password_entered=((TextView)findViewById(R.id.newPassword)).getText().toString().trim();
                    if(new_password_entered.equals("")||old_password_entered.equals("")){
                        Toast.makeText(EditPasswordActivity.this, "请确认是否填写完毕" , Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(!old_password_entered.equals(old_password)){
                        Toast.makeText(EditPasswordActivity.this, "原密码输入错误" , Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    db.execSQL("UPDATE Player SET userPassword='"+new_password_entered+"' WHERE userName='"+who_is_logining+"'");
                    Toast.makeText(EditPasswordActivity.this, "修改成功！" , Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent();
                    intent.setClass(EditPasswordActivity.this,UserInfoActivity.class);
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
                    intent.setClass(EditPasswordActivity.this,UserInfoActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
