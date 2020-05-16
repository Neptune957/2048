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

public class SaveActivity extends AppCompatActivity {
    private final int[] gird_background_color={R.drawable.color_0,R.drawable.color_2,R.drawable.color_4,R.drawable.color_8,R.drawable.color_16,R.drawable.color_32,R.drawable.color_64,R.drawable.color_128,R.drawable.color_256,R.drawable.color_512,R.drawable.color_1024,R.drawable.color_2048};
    private final String[] gird_text_color={"#ccc0b3","#776e65","#776e65","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff"};

    private TextView[][] grids_on_display;      //这个grid数组为到时候显示在页面上的TextView数组
    private int[][] grids;                       //这个grid数组为实际在后台参与计算的真正的grid数组,上面的只是个壳

    private TextView scoreText;
    private TextView timeText;
    private TextView modeText;

    private SQLiteDatabase db;
    private Button head_button;
    private Button restore_button;
    private Button delete_button;

    private String who_is_logining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

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
        Cursor cursor1=db.rawQuery("SELECT * FROM WhoIsLogining",null);
        cursor1.moveToNext();
        who_is_logining=cursor1.getString(0);

        head_button=(Button)findViewById(R.id.head);
        restore_button=(Button)findViewById(R.id.restore);
        delete_button=(Button)findViewById(R.id.delete);


        scoreText=(TextView)findViewById(R.id.score);
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
        Cursor cursor=db.rawQuery("SELECT score,time,content,mode FROM Save WHERE userName='"+who_is_logining+"'",null);
        cursor.moveToNext();
        int score=cursor.getInt(0);
        int time=cursor.getInt(1);
        String content=cursor.getString(2).trim();
        String mode=cursor.getString(3).trim();

        if(mode.equals("standard")){
            modeText.setText("标准模式");
        }else if(mode.equals("wild")){
            modeText.setText("狂野模式");
        }else if(mode.equals("racing")){
            modeText.setText("竞速模式");
        }

        scoreText.setText(""+score);
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

        /*
        Intent intent=new Intent();
        intent.putExtra("score",score+"");
        intent.putExtra("time",time+"");
        intent.putExtra("content",content+"");

        if(mode.equals("standard")){
            Toast.makeText(SaveActivity.this, "读取存档成功！" , Toast.LENGTH_SHORT).show();
            intent.setClass(SaveActivity.this,StandardGameActivity.class);
            startActivity(intent);
        }else if(mode.equals("wild")){
            Toast.makeText(SaveActivity.this, "读取存档成功！" , Toast.LENGTH_SHORT).show();
            intent.setClass(SaveActivity.this,WildGameActivity.class);
            startActivity(intent);
        }else if(mode.equals("racing")){
            Toast.makeText(SaveActivity.this, "读取存档成功！" , Toast.LENGTH_SHORT).show();
            intent.setClass(SaveActivity.this,RacingGameActivity.class);
            startActivity(intent);
        }
        */
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
                    intent.setClass(SaveActivity.this,UserpageActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        restore_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    restore_button.setBackgroundResource(R.drawable.restore_button_down_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    restore_button.setBackgroundResource(R.drawable.restore_button_style);

                    Cursor cursor=db.rawQuery("SELECT score,time,content,mode FROM Save WHERE userName='"+who_is_logining+"'",null);
                    cursor.moveToNext();
                    int score=cursor.getInt(0);
                    int time=cursor.getInt(1);
                    String content=cursor.getString(2).trim();
                    String mode=cursor.getString(3).trim();

                    Intent intent=new Intent();
                    intent.putExtra("score",score+"");
                    intent.putExtra("time",time+"");
                    intent.putExtra("content",content+"");

                    if(mode.equals("standard")){
                        Toast.makeText(SaveActivity.this, "读取存档成功！" , Toast.LENGTH_SHORT).show();
                        intent.setClass(SaveActivity.this,StandardGameActivity.class);
                        startActivity(intent);
                    }else if(mode.equals("wild")){
                        Toast.makeText(SaveActivity.this, "读取存档成功！" , Toast.LENGTH_SHORT).show();
                        intent.setClass(SaveActivity.this,WildGameActivity.class);
                        startActivity(intent);
                    }else if(mode.equals("racing")){
                        Toast.makeText(SaveActivity.this, "读取存档成功！" , Toast.LENGTH_SHORT).show();
                        intent.setClass(SaveActivity.this,RacingGameActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        delete_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    delete_button.setBackgroundResource(R.drawable.delete_down_button_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    delete_button.setBackgroundResource(R.drawable.delete_button_style);

                    db.execSQL("DELETE FROM Save WHERE userName='"+who_is_logining+"'");
                    Toast.makeText(SaveActivity.this, "删除存档成功！" , Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent();
                    intent.setClass(SaveActivity.this,UserpageActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
