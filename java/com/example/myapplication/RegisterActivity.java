package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class RegisterActivity  extends AppCompatActivity {
    private SQLiteDatabase db ;
    private Button head_button;
    private Button login_button;
    private Button register_button;

    private TextView usernameField;
    private TextView passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initDatabase();
        initFields();
        bindButtons();
    }

    private void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }

    private void initFields(){
        head_button=(Button)findViewById(R.id.head);
        register_button=(Button)findViewById(R.id.confirm);
        login_button=(Button)findViewById(R.id.login);
        usernameField=(TextView)findViewById(R.id.userName);
        passwordField=(TextView)findViewById(R.id.password);
    }

    private void bindButtons(){
        head_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    Intent intent=new Intent();
                    intent.setClass(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        login_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    Intent intent=new Intent();
                    intent.setClass(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        register_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    register_button.setBackgroundResource(R.drawable.login_button_down_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    register_button.setBackgroundResource(R.drawable.login_button_style);

                    String username=usernameField.getText().toString().trim();
                    String password=passwordField.getText().toString().trim();

                    if(username.equals("")||password.equals("")){
                        Toast.makeText(RegisterActivity.this, "账户名或密码不能为空！" , Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    Cursor cursor=db.rawQuery("SELECT userPassword FROM Player WHERE userName='"+username+"'",null);
                    if(cursor.getCount()==1){
                        Toast.makeText(RegisterActivity.this, "账户名已存在！" , Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    db.execSQL("INSERT INTO Player VALUES('无','"+username+"','"+password+"',0,0,'无','无',0,0)");
                    Toast.makeText(RegisterActivity.this, "注册成功！" , Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent();
                    intent.setClass(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
