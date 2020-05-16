package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RankDetailActivity extends AppCompatActivity {
    private final int[] gird_background_color={R.drawable.color_0,R.drawable.color_2,R.drawable.color_4,R.drawable.color_8,R.drawable.color_16,R.drawable.color_32,R.drawable.color_64,R.drawable.color_128,R.drawable.color_256,R.drawable.color_512,R.drawable.color_1024,R.drawable.color_2048};
    private final String[] gird_text_color={"#ccc0b3","#776e65","#776e65","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff"};

    private TextView[][] grids_on_display;      //这个grid数组为到时候显示在页面上的TextView数组
    private int[][] grids;                       //这个grid数组为实际在后台参与计算的真正的grid数组,上面的只是个壳

    private TextView nameText=null;
    private TextView dateText=null;
    private TextView scoreText=null;
    private TextView rankText=null;
    private TextView timeText=null;
    private TextView modeText=null;

    private SQLiteDatabase db =null;
    private Button head_button=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_detail);

        initDatabase();
        init_fields();
        initPageInfo();
        bindButtons();
    }

    private void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }

    public void init_fields(){
        head_button=(Button)findViewById(R.id.head);

        nameText=(TextView)findViewById(R.id.name);
        dateText=(TextView)findViewById(R.id.date);
        scoreText=(TextView)findViewById(R.id.score);
        rankText=(TextView)findViewById(R.id.rank);
        timeText=(TextView)findViewById(R.id.time);
        modeText=(TextView)findViewById(R.id.mode);

        grids=new int[4][4];
        grids_on_display=new TextView[4][4];
        grids_on_display[0][0]=(TextView)findViewById(R.id.grid_00);
        grids_on_display[0][1]=(TextView)findViewById(R.id.grid_01);
        grids_on_display[0][2]=(TextView)findViewById(R.id.grid_02);
        grids_on_display[0][3]=(TextView)findViewById(R.id.grid_03);

        grids_on_display[1][0]=(TextView)findViewById(R.id.grid_10);
        grids_on_display[1][1]=(TextView)findViewById(R.id.grid_11);
        grids_on_display[1][2]=(TextView)findViewById(R.id.grid_12);
        grids_on_display[1][3]=(TextView)findViewById(R.id.grid_13);

        grids_on_display[2][0]=(TextView)findViewById(R.id.grid_20);
        grids_on_display[2][1]=(TextView)findViewById(R.id.grid_21);
        grids_on_display[2][2]=(TextView)findViewById(R.id.grid_22);
        grids_on_display[2][3]=(TextView)findViewById(R.id.grid_23);

        grids_on_display[3][0]=(TextView)findViewById(R.id.grid_30);
        grids_on_display[3][1]=(TextView)findViewById(R.id.grid_31);
        grids_on_display[3][2]=(TextView)findViewById(R.id.grid_32);
        grids_on_display[3][3]=(TextView)findViewById(R.id.grid_33);
    }

    public void initPageInfo(){
        Intent intent=getIntent();
        String rank=intent.getStringExtra("rank").trim();
        String mode=intent.getStringExtra("mode").trim();


        Cursor cursor=null;
        if(mode.equals("standard")){
            cursor=db.rawQuery("SELECT score,time,content,date,name FROM StandardRank WHERE rank="+rank,null);
            modeText.setText("标准模式");
        }else if(mode.equals("racing")){
            cursor=db.rawQuery("SELECT score,time,content,date,name FROM RacingRank WHERE rank="+rank,null);
            modeText.setText("竞速模式");
        }else if(mode.equals("wild")){
            cursor=db.rawQuery("SELECT score,time,content,date,name FROM WildRank WHERE rank="+rank,null);
            modeText.setText("狂野模式");
        }
        cursor.moveToNext();
        //Toast.makeText(RankDetailActivity.this,cursor.getCount()+"", Toast.LENGTH_SHORT).show();

        int score=cursor.getInt(0);
        int time=cursor.getInt(1);
        String content=cursor.getString(2);
        String date=cursor.getString(3);
        String name=cursor.getString(4);

        nameText.setText(name);
        dateText.setText(date);
        scoreText.setText(""+score);
        if(rank.equals("1")){
            rankText.setText(rank+"st");
        }else if(rank.equals("2")){
            rankText.setText(rank+"nd");
        }else if(rank.equals("3")){
            rankText.setText(rank+"rd");
        }else{
            rankText.setText(rank+"th");
        }
        timeText.setText(time/1000/60+"分"+time/1000%60+"秒");

        String[] grids_string=content.split(",");
        int index=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                grids[i][j]=Integer.parseInt(grids_string[index]);
                index++;
            }
        }
        display_grids();
    }

    public void display_grids() {
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                int exponent = 0;
                int temp = grids[i][j];
                while ((temp /= 2) != 0) {
                    exponent++;
                }
                grids_on_display[i][j].setBackgroundResource(gird_background_color[exponent]);
                grids_on_display[i][j].setTextColor(Color.parseColor(gird_text_color[exponent]));
                grids_on_display[i][j].setText(grids[i][j] + "");
                if(grids[i][j]>=1024){
                    grids_on_display[i][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
                }
            }
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
                    intent.setClass(RankDetailActivity.this,RankActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
