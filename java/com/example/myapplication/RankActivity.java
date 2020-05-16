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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {
    private SQLiteDatabase db =null;

    private Button head_button=null;
    private Button standard_button=null;
    private Button racing_button=null;
    private Button wild_button=null;

    private Button[] detail_button_array=null;
    private LinearLayout[] entry_array=null;

    private TextView tip=null;

    private String mode=null;

    private String who_is_logining=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        initDatabase();
        initFields();
        initPageInfo();
        bindButtons();
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
        standard_button=(Button)findViewById(R.id.standardButton);
        racing_button=(Button)findViewById(R.id.racingButton);
        wild_button=(Button)findViewById(R.id.wildButton);

        tip=(TextView)findViewById(R.id.tip);
        mode="standard";

        detail_button_array=new Button[]{(Button)findViewById(R.id.detail),(Button)findViewById(R.id.detail2),(Button)findViewById(R.id.detail3),
                                            (Button)findViewById(R.id.detail4),(Button)findViewById(R.id.detail5),(Button)findViewById(R.id.detail6),
                                            (Button)findViewById(R.id.detail7),(Button)findViewById(R.id.detail8),(Button)findViewById(R.id.detail9)};

        entry_array=new LinearLayout[]{(LinearLayout)findViewById(R.id.l1),(LinearLayout)findViewById(R.id.l2),(LinearLayout)findViewById(R.id.l3),
                                         (LinearLayout)findViewById(R.id.l4), (LinearLayout)findViewById(R.id.l5),(LinearLayout)findViewById(R.id.l6),
                                         (LinearLayout)findViewById(R.id.l7),(LinearLayout)findViewById(R.id.l8),(LinearLayout)findViewById(R.id.l9)};
    }

    public void initPageInfo(){
        for(int i=0;i<9;i++){
            entry_array[i].setVisibility(View.GONE);
        }

        tip.setVisibility(View.GONE);

        Cursor cursor=null;
        if(mode.equals("standard")){
            cursor=db.rawQuery("SELECT userName,name,score,rank FROM StandardRank",null);
        }else if(mode.equals("racing")){
            cursor=db.rawQuery("SELECT userName,name,score,rank FROM RacingRank",null);
        }else if(mode.equals("wild")){
            cursor=db.rawQuery("SELECT userName,name,score,rank FROM WildRank",null);
        }
        ArrayList<String[]> temp=new ArrayList<>();
        while(cursor.moveToNext()) {
            String userName=cursor.getString(0);
            String name=cursor.getString(1);
            String score=cursor.getInt(2)+"";
            String rank=cursor.getInt(3)+"";
            String[] entry={userName,name,score,rank};
            temp.add(entry);
        }
        if(temp.size()==0){
            tip.setVisibility(View.VISIBLE);
            return;
        }
        for(int i=0;i<temp.size();i++){
            String[] entry=temp.get(i);
            String name=entry[1];
            String score=entry[2];
            int rank=Integer.parseInt(entry[3]);
            entry_array[rank-1].setVisibility(View.VISIBLE);
            ((TextView)((LinearLayout)(entry_array[rank-1].getChildAt(2))).getChildAt(0)).setText(name);
            ((TextView)((LinearLayout)(entry_array[rank-1].getChildAt(2))).getChildAt(1)).setText("分数: "+score);
        }
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
                    intent.setClass(RankActivity.this,UserpageActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        standard_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    racing_button.setBackgroundResource(R.drawable.rank_unselected_button);
                    racing_button.setTextColor(Color.parseColor("#606060"));
                    wild_button.setBackgroundResource(R.drawable.rank_unselected_button);
                    wild_button.setTextColor(Color.parseColor("#606060"));

                    standard_button.setBackgroundResource(R.drawable.rank_selected_button);
                    standard_button.setTextColor(Color.parseColor("#ffffff"));

                    mode="standard";
                    bindButtons();
                    initPageInfo();
                }
                return false;
            }
        });

        racing_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    standard_button.setBackgroundResource(R.drawable.rank_unselected_button);
                    standard_button.setTextColor(Color.parseColor("#606060"));
                    wild_button.setBackgroundResource(R.drawable.rank_unselected_button);
                    wild_button.setTextColor(Color.parseColor("#606060"));

                    racing_button.setBackgroundResource(R.drawable.rank_selected_button);
                    racing_button.setTextColor(Color.parseColor("#ffffff"));

                    mode="racing";
                    bindButtons();
                    initPageInfo();
                }
                return false;
            }
        });

        wild_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    racing_button.setBackgroundResource(R.drawable.rank_unselected_button);
                    racing_button.setTextColor(Color.parseColor("#606060"));
                    standard_button.setBackgroundResource(R.drawable.rank_unselected_button);
                    standard_button.setTextColor(Color.parseColor("#606060"));

                    wild_button.setBackgroundResource(R.drawable.rank_selected_button);
                    wild_button.setTextColor(Color.parseColor("#ffffff"));

                    mode="wild";
                    bindButtons();
                    initPageInfo();
                }
                return false;
            }
        });

        for(int i=0;i<9;i++){
            final Button temp= detail_button_array[i];
            final String temp_mode=mode;
            temp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        //按下按钮时
                        temp.setTextColor(Color.parseColor("#ffffff"));
                        temp.setBackgroundResource(R.drawable.rank_detail_down_button);
                        //Toast.makeText(RankActivity.this,  ((TextView)((LinearLayout)((LinearLayout)(temp.getParent())).getChildAt(2)).getChildAt(0)).getText().toString().trim() , Toast.LENGTH_SHORT).show();
                        //Toast.makeText(RankActivity.this,  ((TextView)((LinearLayout)((LinearLayout)(temp.getParent())).getChildAt(2)).getChildAt(1)).getText().toString().trim() , Toast.LENGTH_SHORT).show();
                    }else if(event.getAction() == MotionEvent.ACTION_UP) {
                        //弹起按钮时
                        temp.setTextColor(Color.parseColor("#007af5"));
                        temp.setBackgroundResource(R.drawable.rank_detail_button);

                        Intent intent=new Intent();
                        intent.putExtra("rank",((TextView)((LinearLayout)(temp.getParent())).getChildAt(0)).getText().toString().trim());
                        intent.putExtra("mode",temp_mode);
                        intent.setClass(RankActivity.this,RankDetailActivity.class);
                        startActivity(intent);

                    }
                    return false;
                }
            });
        }
    }
}
