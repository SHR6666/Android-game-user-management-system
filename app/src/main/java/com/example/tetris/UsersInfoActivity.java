package com.example.tetris;

import android.content.Intent;
import android.graphics.Typeface;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 停留本界面3秒后跳转到主界面,并显示倒计时和欢迎词
 */
public class UsersInfoActivity extends AppCompatActivity {
    private TextView text_name, text_condition, txtWelcome;
    private String name;
    private static final long DELAY_TIME = 3000;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_info);

        init();
    }

    private void init() {
        Intent intent = getIntent(); //获取传递的数据
        name = intent.getStringExtra("Username");
        text_name = (TextView) findViewById(R.id.text_name);
        text_name.setText(name);
        text_condition = (TextView) findViewById(R.id.text_condition);
        text_condition.setText("欢迎来到俄罗斯方块！" + name + "，请稍等...");


        //倒计时
        task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(UsersInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, DELAY_TIME);

        //TextView的字体更换
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.yan);
        txtWelcome.setTypeface(typeface);
    }
}