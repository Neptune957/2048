package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.DrawableContainer;
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

public class BilllistActivity extends AppCompatActivity {
    private SQLiteDatabase db =null;
    private Button head_button=null;
    private String who_is_logining=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billlist);

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
                    intent.setClass(BilllistActivity.this,BalanceActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    public void initPageInfo(){
        LinearLayout general=(LinearLayout)findViewById(R.id.general);

        Cursor cursor2=db.rawQuery("SELECT * FROM BalanceHistory WHERE userName='"+who_is_logining+"'",null);
        if(cursor2.getCount()==0){
            TextView text=new TextView(BilllistActivity.this);
            LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text.setLayoutParams(text_params);
            text.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.layer_list2));
            text.setPadding(0,40,0,40);
            text.setText("您当前还未有余额记录");
            text.setTextSize(20);
            text.setGravity(Gravity.CENTER);
            general.addView(text);
        }else{
            //倒序显示余额记录，最新的优先
            ArrayList<String[]> temp=new ArrayList<>();
            while(cursor2.moveToNext()) {
                String reason=cursor2.getString(1);
                String value=cursor2.getString(2);
                String date=cursor2.getString(3);
                String offset=cursor2.getString(4);
                String[] entry={reason,value,date,offset};
                temp.add(entry);
            }
            for(int i=temp.size()-1;i>=0;i--){
                String[] entry=temp.get(i);
                String reason=entry[0];
                String value=entry[1];
                String date=entry[2];
                String offset=entry[3];

                LinearLayout layout1 = new LinearLayout(BilllistActivity.this);
                LinearLayout.LayoutParams layout1_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout1.setLayoutParams(layout1_params);
                layout1.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.layer_list2));
                layout1.setPadding(20,10,20,10);
                layout1.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout layout2 = new LinearLayout(BilllistActivity.this);
                LinearLayout.LayoutParams layout2_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0F);
                layout2.setLayoutParams(layout2_params);
                layout2.setOrientation(LinearLayout.VERTICAL);
                layout1.addView(layout2);

                LinearLayout layout3 = new LinearLayout(BilllistActivity.this);
                LinearLayout.LayoutParams layout3_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0F);
                layout3.setLayoutParams(layout3_params);
                layout3.setOrientation(LinearLayout.VERTICAL);
                layout1.addView(layout3);

                TextView text1=new TextView(BilllistActivity.this);
                LinearLayout.LayoutParams text1_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text1_params.setMargins(0,3,0,3);
                text1.setLayoutParams(text1_params);
                text1.setText(reason);
                text1.setTextSize(20);
                text1.setTextColor(Color.parseColor("#505050"));
                layout2.addView(text1);

                TextView text2=new TextView(BilllistActivity.this);
                LinearLayout.LayoutParams text2_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text2_params.setMargins(0,3,0,3);
                text2.setLayoutParams(text2_params);
                text2.setText(value);
                text2.setTextSize(20);
                layout2.addView(text2);

                TextView text3=new TextView(BilllistActivity.this);
                LinearLayout.LayoutParams text3_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text3_params.setMargins(0,9,0,6);
                text3.setLayoutParams(text3_params);
                text3.setText(date);
                text3.setGravity(Gravity.RIGHT);
                layout3.addView(text3);

                TextView text4=new TextView(BilllistActivity.this);
                LinearLayout.LayoutParams text4_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text4_params.setMargins(0,3,0,3);
                text4.setLayoutParams(text3_params);
                text4.setText(offset);
                text4.setGravity(Gravity.RIGHT);
                text4.setTextColor(Color.parseColor("#000000"));
                text4.setTextSize(20);
                layout3.addView(text4);

                general.addView(layout1);
            }
        }
    }
}

