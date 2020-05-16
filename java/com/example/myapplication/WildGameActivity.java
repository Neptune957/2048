package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.AssetsDatabaseManager;
import com.example.myapplication.Config;

import java.util.Calendar;
import java.util.Date;

public class WildGameActivity extends Activity {
    private final int[] gird_background_color={R.drawable.color_0,R.drawable.color_2,R.drawable.color_4,R.drawable.color_8,R.drawable.color_16,R.drawable.color_32,R.drawable.color_64,R.drawable.color_128,R.drawable.color_256,R.drawable.color_512,R.drawable.color_1024,R.drawable.color_2048};
    private final String[] gird_text_color={"#ccc0b3","#776e65","#776e65","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff","#ffffff"};

    private TextView clock_field;
    private TextView score_field;
    private TextView end_tip;
    private TextView title_field;

    private Button[][] grids_on_display;      //这个grid数组为到时候显示在页面上的TextView数组
    private boolean[][] is_gird_selected;
    private int[][] grids;                       //这个grid数组为实际在后台参与计算的真正的grid数组,上面的只是个壳
    private boolean[][] grid_is_new;

    private int empty_grids_amount;

    private Intent intent;

    private Button left_button;
    private Button up_button;
    private Button right_button;
    private Button down_button;
    private Button clear_button;
    private Button save_button;
    private Button confirm_button;
    private Button back_button;
    private Button stop_button;

    private long start_time;
    private long current_time;
    private int score;
    private String date;
    private String who_is_logining;

    private boolean isEnded;
    private boolean is_edit_mode;
    private boolean isSaved;

    private SQLiteDatabase db;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wild_game);

        initDatabase();
        init_fields();
        init_page_info();
        bind_listeners();
        display_grids();
        start_clock();
    }

    private void initDatabase(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db= mg.getDatabase(Config.database_name);
    }

    public void start_clock(){
        new Thread() {
            public void run() {
                while(!isEnded) {
                    try {
                        Thread.sleep(1000);
                        current_time=new Date().getTime();
                        final long minute=(current_time-start_time)/1000/60;
                        final long second=(current_time-start_time)/1000%60;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                clock_field.setText(minute+":"+second);
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void init_fields(){
        intent=getIntent();

        isEnded=false;
        is_edit_mode=false;
        isSaved=false;

        Cursor cursor1=db.rawQuery("SELECT * FROM WhoIsLogining",null);
        cursor1.moveToNext();
        who_is_logining=cursor1.getString(0);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
            }
        };

        current_time=new Date().getTime();

        if(intent.getStringExtra("time")!=null){
            isSaved=true;
            start_time=new Date().getTime()-Long.parseLong(intent.getStringExtra("time"));
        }else{
            start_time=new Date().getTime();
        }

        if(intent.getStringExtra("score")!=null){
            score=Integer.parseInt(intent.getStringExtra("score"));
        }else{
            score=0;
        }

        Calendar c=Calendar.getInstance();
        date=c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);

        clock_field=(TextView)findViewById(R.id.clock);
        score_field=(TextView)findViewById(R.id.score);
        end_tip=(TextView)findViewById(R.id.end_tip);
        title_field=(TextView)findViewById(R.id.title);

        grids_on_display=new Button[4][4];
        grid_is_new=new boolean[4][4];
        empty_grids_amount=16;

        is_gird_selected=new boolean[4][4];
        grids=new int[4][4];
        init_grids();

        left_button=(Button)findViewById(R.id.left_button);
        right_button=(Button)findViewById(R.id.right_button);
        up_button=(Button)findViewById(R.id.up_button);
        down_button=(Button)findViewById(R.id.down_button);
        clear_button=(Button)findViewById(R.id.clear_button);
        save_button=(Button)findViewById(R.id.save_button);
        confirm_button=(Button)findViewById(R.id.confirm_button);
        back_button=(Button)findViewById(R.id.back_button);
        stop_button=(Button)findViewById(R.id.stop_button);

        grids_on_display[0][0]=(Button)findViewById(R.id.grid_00);
        grids_on_display[0][1]=(Button)findViewById(R.id.grid_01);
        grids_on_display[0][2]=(Button)findViewById(R.id.grid_02);
        grids_on_display[0][3]=(Button)findViewById(R.id.grid_03);

        grids_on_display[1][0]=(Button)findViewById(R.id.grid_10);
        grids_on_display[1][1]=(Button)findViewById(R.id.grid_11);
        grids_on_display[1][2]=(Button)findViewById(R.id.grid_12);
        grids_on_display[1][3]=(Button)findViewById(R.id.grid_13);

        grids_on_display[2][0]=(Button)findViewById(R.id.grid_20);
        grids_on_display[2][1]=(Button)findViewById(R.id.grid_21);
        grids_on_display[2][2]=(Button)findViewById(R.id.grid_22);
        grids_on_display[2][3]=(Button)findViewById(R.id.grid_23);

        grids_on_display[3][0]=(Button)findViewById(R.id.grid_30);
        grids_on_display[3][1]=(Button)findViewById(R.id.grid_31);
        grids_on_display[3][2]=(Button)findViewById(R.id.grid_32);
        grids_on_display[3][3]=(Button)findViewById(R.id.grid_33);

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                is_gird_selected[i][j]=false;
            }
        }
    }

    public void init_page_info(){
        final long minute=(current_time-start_time)/1000/60;
        final long second=(current_time-start_time)/1000%60;
        title_field.setText("TIME");
        clock_field.setText(minute+":"+second);
    }

    public void bind_listeners(){
        left_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(isEnded){
                        return false;
                    }else if(is_edit_mode){
                        return false;
                    }
                    end_tip.setText("");
                    grid_is_new=new boolean[4][4];
                    boolean occuring_combination=false;
                    move_grids("left");
                    do {
                        occuring_combination=combine_grids("left");
                    }while(occuring_combination);
                    move_grids("left");

                    add_grid();
                    display_grids();
                }
                return false;
            }
        });

        right_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    //balance_button.setBackgroundResource(R.drawable.layer_list);
                    if(isEnded){
                        return false;
                    }else if(is_edit_mode){
                        return false;
                    }
                    end_tip.setText("");
                    grid_is_new=new boolean[4][4];
                    boolean occuring_combination=false;
                    move_grids("right");
                    do {
                        occuring_combination=combine_grids("right");
                    }while(occuring_combination);
                    move_grids("right");
                    ;
                    add_grid();
                    display_grids();
                }
                return false;
            }
        });

        up_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(isEnded){
                        return false;
                    }else if(is_edit_mode){
                        return false;
                    }
                    end_tip.setText("");
                    grid_is_new=new boolean[4][4];
                    boolean occuring_combination=false;
                    move_grids("up");
                    do {
                        occuring_combination=combine_grids("up");
                    }while(occuring_combination);
                    move_grids("up");

                    add_grid();
                    display_grids();
                }
                return false;
            }
        });

        down_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    if(isEnded){
                        return false;
                    }else if(is_edit_mode){
                        return false;
                    }
                    end_tip.setText("");
                    grid_is_new=new boolean[4][4];
                    boolean occuring_combination=false;
                    move_grids("down");
                    do {
                        occuring_combination=combine_grids("down");
                    }while(occuring_combination);
                    move_grids("down");

                    add_grid();
                    display_grids();
                }
                return false;
            }
        });

        clear_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    clear_button.setBackgroundResource(R.drawable.remove_down_button_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    if(isEnded) {
                        return false;
                    }
                    end_tip.setText("");
                    clear_button.setBackgroundResource(R.drawable.remove_button_style);
                    is_edit_mode=true;

                    clear_button.setVisibility(View.GONE);
                    save_button.setVisibility(View.GONE);
                    stop_button.setVisibility(View.GONE);
                    confirm_button.setVisibility(View.VISIBLE);
                    back_button.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        save_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    save_button.setBackgroundResource(R.drawable.save_down_button_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    save_button.setBackgroundResource(R.drawable.save_button_style);
                    String content="";
                    for(int i=0;i<4;i++) {
                        for (int j = 0; j < 4; j++) {
                            if(i==3&&j==3){
                                content += grids[i][j];
                            }else{
                                content += grids[i][j] + ",";
                            }
                        }
                    }
                    db.execSQL("DELETE FROM Save WHERE userName='"+who_is_logining+"'");
                    db.execSQL("INSERT INTO Save VALUES('"+who_is_logining+"',"+score+","+(current_time-start_time)+",'"+content+"','wild')");
                    Toast.makeText(WildGameActivity.this, "存档成功！" , Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(WildGameActivity.this,WelcomeActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        confirm_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    confirm_button.setBackgroundResource(R.drawable.remove_down_button_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    confirm_button.setBackgroundResource(R.drawable.remove_button_style);

                    if(is_edit_mode){
                        int amount=0;
                        for(int i=0;i<4;i++){
                            for(int j=0;j<4;j++){
                                if(is_gird_selected[i][j]==true){
                                    amount++;
                                }
                            }
                        }
                        Cursor cursor=null;
                        cursor=db.rawQuery("SELECT balance FROM Player WHERE userName='"+who_is_logining+"'",null);
                        cursor.moveToNext();
                        int balance=cursor.getInt(0);
                        if(balance-amount<0){
                            for(int i=0;i<4;i++) {
                                for (int j = 0; j < 4; j++) {
                                    is_gird_selected[i][j] = false;
                                }
                            }
                            end_tip.setText("余额不足,消除失败");
                        }else{
                            for(int i=0;i<4;i++){
                                for(int j=0;j<4;j++){
                                    if(is_gird_selected[i][j]==true){
                                        Animation alpha = new AlphaAnimation(1,0.8f);
                                        alpha.setDuration(200);
                                        grids_on_display[i][j].startAnimation(alpha);

                                        grids[i][j]=0;
                                        empty_grids_amount++;
                                        is_gird_selected[i][j]=false;
                                    }
                                }
                            }
                            Calendar c = Calendar.getInstance();
                            int now_year=c.get(Calendar.YEAR);
                            int now_month=c.get(Calendar.MONTH)+1;
                            int now_date=c.get(Calendar.DATE);
                            String stop_day=""+now_year+"-"+now_month+"-"+now_date;

                            db.execSQL("UPDATE Player SET balance=balance-amount WHERE userName='"+who_is_logining+"'");
                            db.execSQL("INSERT INTO BalanceHistory VALUES('"+who_is_logining+"','游戏消费','余额:"+(balance-amount)+"元','"+stop_day+"','-"+amount+"元')");
                            end_tip.setText("消除成功! 花费"+amount+"元, 剩余余额为"+(balance-amount+"元"));
                        }

                        is_edit_mode=false;
                        clear_button.setVisibility(View.VISIBLE);
                        save_button.setVisibility(View.VISIBLE);
                        stop_button.setVisibility(View.VISIBLE);
                        confirm_button.setVisibility(View.GONE);
                        back_button.setVisibility(View.GONE);

                        display_grids();
                    }else{
                        Intent intent=new Intent();
                        intent.setClass(WildGameActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    back_button.setBackgroundResource(R.drawable.save_down_button_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    back_button.setBackgroundResource(R.drawable.save_button_style);
                    is_edit_mode=false;
                    end_tip.setText("");

                    clear_button.setVisibility(View.VISIBLE);
                    save_button.setVisibility(View.VISIBLE);
                    stop_button.setVisibility(View.VISIBLE);
                    confirm_button.setVisibility(View.GONE);
                    back_button.setVisibility(View.GONE);

                    display_grids();
                }
                return false;
            }
        });

        stop_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //按下按钮时
                    stop_button.setBackgroundResource(R.drawable.stop_button_down_style);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //弹起按钮时
                    stop_button.setBackgroundResource(R.drawable.stop_button_style);
                    if(isEnded) {
                        Intent intent=new Intent();
                        intent.setClass(WildGameActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                        return false;
                    }
                    end_tip.setText("");

                    clear_button.setVisibility(View.GONE);
                    save_button.setVisibility(View.GONE);
                    stop_button.setVisibility(View.GONE);
                    confirm_button.setVisibility(View.VISIBLE);
                    back_button.setVisibility(View.VISIBLE);

                    display_grids();
                }
                return false;
            }
        });


        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                final int temp_i=i;
                final int temp_j=j;
                grids_on_display[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(!is_edit_mode){
                            return false;
                        }
                        if(grids[temp_i][temp_j]==0){
                            return false;
                        }
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            if(!is_gird_selected[temp_i][temp_j]){
                                grids_on_display[temp_i][temp_j].setBackgroundResource(R.drawable.color_selected);
                                grids_on_display[temp_i][temp_j].setTextColor(Color.parseColor("#18d9e2"));
                                is_gird_selected[temp_i][temp_j]=true;
                            }else{
                                int exponent = 0;
                                int temp = grids[temp_i][temp_j];
                                while ((temp /= 2) != 0) {
                                    exponent++;
                                }
                                grids_on_display[temp_i][temp_j].setBackgroundResource(gird_background_color[exponent]);
                                grids_on_display[temp_i][temp_j].setTextColor(Color.parseColor(gird_text_color[exponent]));
                                is_gird_selected[temp_i][temp_j]=false;
                            }
                            //按下按钮时
                        }
                        return false;
                    }
                });
            }
        }
    }

    public boolean exist_combination() {
        if(empty_grids_amount==0) {
            for(int i=0;i<4;i++) {
                for(int j=0;j<3;j++) {
                    if(grids[i][j]==grids[i][j+1]) {
                        return true;
                    }
                }
            }
            for(int j=0;j<4;j++) {
                for(int i=0;i<3;i++) {
                    if(grids[i][j]==grids[i+1][j]) {
                        return true;
                    }
                }
            }
            return false;
        }else {
            return true;
        }
    }

    //参数为方块移动方向的英文。
    public boolean combine_grids(String direction) {
        if(direction.equals("up")) {
            for(int j=0;j<4;j++) {
                for(int i=0;i<3;i++) {
                    if(grids[i][j]==grids[i+1][j]&&grids[i][j]!=0&&grid_is_new[i][j]==false&&grid_is_new[i+1][j]==false) {
                        grids[i][j]*=2;
                        grid_is_new[i][j]=true;
                        grids[i+1][j]=0;
                        empty_grids_amount++;
                        return true;
                    }
                }
            }
        }else if(direction.equals("down")) {
            for(int j=0;j<4;j++) {
                for(int i=3;i>0;i--) {
                    if(grids[i][j]==grids[i-1][j]&&grids[i][j]!=0&&grid_is_new[i][j]==false&&grid_is_new[i-1][j]==false) {
                        grids[i][j]*=2;
                        grid_is_new[i][j]=true;
                        grids[i-1][j]=0;
                        empty_grids_amount++;
                        return true;
                    }
                }
            }
        }else if(direction.equals("left")) {
            for(int i=0;i<4;i++) {
                for(int j=0;j<3;j++) {
                    if(grids[i][j]==grids[i][j+1]&&grids[i][j]!=0&&grid_is_new[i][j]==false&&grid_is_new[i][j+1]==false) {
                        grids[i][j]*=2;
                        grid_is_new[i][j]=true;
                        grids[i][j+1]=0;
                        empty_grids_amount++;
                        return true;
                    }
                }
            }
        }else if(direction.equals("right")) {
            for(int i=0;i<4;i++) {
                for(int j=3;j>0;j--) {
                    if(grids[i][j]==grids[i][j-1]&&grids[i][j]!=0&&grid_is_new[i][j]==false&&grid_is_new[i][j-1]==false) {
                        grids[i][j]*=2;
                        grid_is_new[i][j]=true;
                        grids[i][j-1]=0;
                        empty_grids_amount++;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void add_grid() {
        if(empty_grids_amount==0) {
            if(!exist_combination()){
                if(!isEnded){
                    isEnded=true;
                    last_work();
                }
            };
            return;
        }

        int steps_to_move=(int)(Math.random()*empty_grids_amount)+1;
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                if(grids[i][j]==0) {
                    steps_to_move--;
                    if(steps_to_move==0) {
                        grids[i][j]=((int)(Math.random()*2)+1)*2;
                        empty_grids_amount--;

                        Animation alpha = new AlphaAnimation(0,1);
                        alpha.setDuration(350);
                        grids_on_display[i][j].startAnimation(alpha);

                        return;
                    }
                }
            }
        }
        compute_score();
    }

    public void move_grids(String direction){
        if(direction.equals("down")){
            for(int j=0;j<4;j++) {
                for(int i=0;i<3;i++) {
                    for(int k=0;k<3-i;k++) {
                        if(grids[k+1][j]==0) {
                            grids[k+1][j]=grids[k][j];
                            grids[k][j]=0;
                            if(grid_is_new[k][j]==true){
                                grid_is_new[k][j]=false;
                                grid_is_new[k+1][j]=true;
                            }
                        }
                    }
                }
            }
        }else if(direction.equals("up")){
            for(int j=0;j<4;j++) {
                for(int i=3;i>0;i--) {
                    for(int k=3;k>3-i;k--) {
                        if(grids[k-1][j]==0) {
                            grids[k-1][j]=grids[k][j];
                            grids[k][j]=0;
                            if(grid_is_new[k][j]==true){
                                grid_is_new[k][j]=false;
                                grid_is_new[k-1][j]=true;
                            }
                        }
                    }
                }
            }
        }else if(direction.equals("right")){
            for(int i=0;i<4;i++) {
                for(int j=0;j<3;j++) {
                    for(int k=0;k<3-j;k++) {
                        if(grids[i][k+1]==0) {
                            grids[i][k+1]=grids[i][k];
                            grids[i][k]=0;
                            if(grid_is_new[i][k]==true){
                                grid_is_new[i][k]=false;
                                grid_is_new[i][k+1]=true;
                            }
                        }
                    }
                }
            }
        }else if(direction.equals("left")){
            for(int i=0;i<4;i++) {
                for(int j=3;j>0;j--) {
                    for(int k=3;k>3-j;k--) {
                        if(grids[i][k-1]==0) {
                            grids[i][k-1]=grids[i][k];
                            grids[i][k]=0;
                            if(grid_is_new[i][k]==true){
                                grid_is_new[i][k]=false;
                                grid_is_new[i][k-1]=true;
                            }
                        }
                    }
                }
            }
        }
    }

    public void init_grids() {
        if(intent.getStringExtra("content")!=null){
            String content=intent.getStringExtra("content");
            String[] grids_string=content.split(",");
            int index=0;
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    grids[i][j]=Integer.parseInt(grids_string[index]);
                    if(grids[i][j]!=0){
                        empty_grids_amount--;
                    }
                    index++;
                }
            }
        }else{
            for(int i=0;i<4;i++) {
                for(int j=0;j<4;j++) {
                    grids[i][j]=0;
                }
            }

            int initial_gird1_x=(int)(Math.random()*4);
            int initial_grid2_x=(int)(Math.random()*4);
            while(initial_grid2_x==initial_gird1_x) {
                initial_grid2_x=(int)(Math.random()*4);
            }

            int initial_gird1_y=(int)(Math.random()*4);
            int initial_grid2_y=(int)(Math.random()*4);
            while(initial_grid2_y==initial_gird1_y) {
                initial_grid2_y=(int)(Math.random()*4);
            }

            grids[initial_gird1_x][initial_gird1_y]=((int)(Math.random()*2)+1)*2;
            grids[initial_grid2_x][initial_grid2_y]=((int)(Math.random()*2)+1)*2;

            empty_grids_amount=empty_grids_amount-2;
        }
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
        compute_score();
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(grids[i][j]==2048){
                    isEnded=true;
                    last_work();
                    return;
                }
            }
        }
    }

    public void compute_score(){
        score=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                score+=grids[i][j]*grids[i][j];
            }
        }
        score_field.setText(score+"");
    }

    public void last_work(){
        end_tip.setText("游戏结束");

        Cursor cursor=null;
        cursor=db.rawQuery("SELECT name FROM Player WHERE userName='"+who_is_logining+"'",null);
        cursor.moveToNext();
        String name=cursor.getString(0);

        String content="";
        for(int i=0;i<4;i++) {
            for (int j = 0; j < 4; j++) {
                if(i==3&&j==3){
                    content += grids[i][j];
                }else{
                    content += grids[i][j] + ",";
                }
            }
        }

        db.execSQL("INSERT INTO WildRank VALUES('"+who_is_logining+"','"+name+"',0,"+score+",'"+(current_time-start_time)+"','"+date+"','"+content+"')");
        cursor=db.rawQuery("SELECT userName,content FROM WildRank ORDER BY score DESC",null);
        int index=1;
        while(cursor.moveToNext()){
            String userName=cursor.getString(0);
            String content_temp=cursor.getString(1);
            db.execSQL("UPDATE WildRank SET rank="+index+" WHERE userName='"+userName+"' AND content='"+content_temp+"'");
            index++;
        }

        if((index-1)>9){
            db.execSQL("DELETE FROM WildRank WHERE rank=10");
        }

        db.execSQL("INSERT INTO PlayHistory VALUES('"+who_is_logining+"',"+score+",'wild','"+(current_time-start_time)+"','"+date+"')");
        db.execSQL("UPDATE Player SET times=times+1,minutes=minutes+"+(current_time-start_time)/1000/60+" WHERE userName='"+who_is_logining+"'");
        for(int i=0;i<4;i++) {
            for (int j = 0; j < 4; j++) {
                if(grids[i][j]==2048){
                    db.execSQL("UPDATE Player SET winTimes=winTimes+1 WHERE userName='"+who_is_logining+"'");
                    break;
                }
            }
        }

        if(isSaved){
            db.execSQL("DELETE FROM Save WHERE userName='"+who_is_logining+"'");
        }
    }
}
