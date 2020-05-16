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

public class LoginActivity  extends AppCompatActivity {
    private SQLiteDatabase db ;
    private Button head_button;
    private Button login_button;
    private Button register_button;

    private TextView usernameField;
    private TextView passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        login_button=(Button)findViewById(R.id.confirm);
        register_button=(Button)findViewById(R.id.register);
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
                    intent.setClass(LoginActivity.this,WelcomeActivity.class);
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
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    Intent intent=new Intent();
                    intent.setClass(LoginActivity.this,RegisterActivity.class);
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
                    login_button.setBackgroundResource(R.drawable.login_button_down_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    login_button.setBackgroundResource(R.drawable.login_button_style);

                    String username=usernameField.getText().toString().trim();
                    String password=passwordField.getText().toString().trim();

                    if(username.equals("")||password.equals("")){
                        Toast.makeText(LoginActivity.this, "账户名或密码不能为空！" , Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    Cursor cursor=db.rawQuery("SELECT userPassword FROM Player WHERE userName='"+username+"'",null);
                    if(cursor.getCount()==0){
                        Toast.makeText(LoginActivity.this, "账户名不存在！" , Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    cursor.moveToNext();
                    String userPassword=cursor.getString(0).trim();
                    if(!userPassword.equals(password)){
                        Toast.makeText(LoginActivity.this, "密码错误！" , Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    db.execSQL("UPDATE WhoIsLogining SET userName='"+username+"'");
                    Toast.makeText(LoginActivity.this, "登录成功！" , Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent();
                    intent.setClass(LoginActivity.this,WelcomeActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
