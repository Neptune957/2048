package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GameHistoryActivity extends AppCompatActivity {
    private SQLiteDatabase db =null;
    private Button head_button=null;

    private String who_is_logining=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        initDatabase();
        initFields();
        bindButtons();
        initPageInfo();
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

        head_button=(Button)findViewById(R.id.head);
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
                    intent.setClass(GameHistoryActivity.this,UserpageActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    public void initPageInfo(){
        LinearLayout general=(LinearLayout)findViewById(R.id.general);

        Cursor cursor2=db.rawQuery("SELECT * FROM PlayHistory WHERE userName='"+who_is_logining+"'",null);
        if(cursor2.getCount()==0){
            TextView text=new TextView(GameHistoryActivity.this);
            LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text.setLayoutParams(text_params);
            text.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.layer_list2));
            text.setPadding(0,40,0,40);
            text.setText("游戏记录为空，快去玩一盘吧");
            text.setTextSize(20);
            text.setGravity(Gravity.CENTER);
            general.addView(text);
        }else{
            ArrayList<String[]> temp=new ArrayList<>();
            while(cursor2.moveToNext()) {
                String score=cursor2.getInt(1)+"";
                String mode=cursor2.getString(2);
                String time=cursor2.getString(3);
                String date=cursor2.getString(4);
                String[] entry={score,mode,time,date};
                temp.add(entry);
            }
            for(int i=temp.size()-1;i>=0;i--){
                String[] entry=temp.get(i);
                String score=entry[0];
                String mode=entry[1];
                String time=(Long.parseLong(entry[2])/1000/60)+"分"+(Long.parseLong(entry[2])/1000%60)+"秒";
                String date=entry[3];

                LinearLayout layout1 = new LinearLayout(GameHistoryActivity.this);
                LinearLayout.LayoutParams layout1_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout1.setLayoutParams(layout1_params);
                layout1.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.layer_list2));
                layout1.setPadding(20,10,20,10);
                layout1.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout layout2 = new LinearLayout(GameHistoryActivity.this);
                LinearLayout.LayoutParams layout2_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                layout2.setLayoutParams(layout2_params);
                layout2.setOrientation(LinearLayout.VERTICAL);
                layout1.addView(layout2);

                LinearLayout layout3 = new LinearLayout(GameHistoryActivity.this);
                LinearLayout.LayoutParams layout3_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                layout3.setLayoutParams(layout3_params);
                layout3.setOrientation(LinearLayout.VERTICAL);;
                layout1.addView(layout3);

                TextView text1=new TextView(GameHistoryActivity.this);
                LinearLayout.LayoutParams text1_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text1_params.setMargins(0,3,0,3);
                text1.setLayoutParams(text1_params);
                text1.setText("分数: "+score);
                text1.setTextSize(20);
                text1.setTextColor(Color.parseColor("#505050"));
                layout2.addView(text1);

                TextView text2=new TextView(GameHistoryActivity.this);
                LinearLayout.LayoutParams text2_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text2_params.setMargins(0,3,0,3);
                text2.setLayoutParams(text2_params);
                if(mode.equals("standard")){
                    text2.setText("标准模式");
                }else if(mode.equals("racing")){
                    text2.setText("竞速模式");
                }else if(mode.equals("wild")){
                    text2.setText("狂野模式");
                }
                text2.setTextSize(20);
                layout2.addView(text2);

                TextView text3=new TextView(GameHistoryActivity.this);
                LinearLayout.LayoutParams text3_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text3_params.setMargins(0,3,0,3);
                text3.setLayoutParams(text3_params);
                text3.setText(date);
                text3.setGravity(Gravity.RIGHT);
                layout3.addView(text3);

                TextView text4=new TextView(GameHistoryActivity.this);
                LinearLayout.LayoutParams text4_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text4_params.setMargins(0,9,0,3);
                text4.setLayoutParams(text3_params);
                text4.setText(time);
                text4.setGravity(Gravity.RIGHT);
                text4.setTextColor(Color.parseColor("#000000"));
                text4.setTextSize(20);
                text4.setTextColor(Color.parseColor("#404040"));
                layout3.addView(text4);

                general.addView(layout1);
            }
        }
    }
}
