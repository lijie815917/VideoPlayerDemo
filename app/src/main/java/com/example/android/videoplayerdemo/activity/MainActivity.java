package com.example.android.videoplayerdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.example.android.videoplayerdemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btn_1) Button btn1;
    @Bind(R.id.btn_2) Button btn2;
    @Bind(R.id.btn_3) Button btn3;
    @Bind(R.id.btn_4) Button btn4;
    private AliyunVodPlayer aliyunVodPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }


    private void init() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }


    @Override public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_1:
                startActivity(new Intent(MainActivity.this,AliVideoPlayerActivity.class));
                break;

            case R.id.btn_2:
                startActivity(new Intent(MainActivity.this,EXOVideoPlayerActivity.class));
                break;
            case R.id.btn_3:
                startActivity(new Intent(MainActivity.this,IJKVideoPlayerActivity.class));
                break;
            case R.id.btn_4:
                startActivity(new Intent(MainActivity.this,AliShortVideoPlayerActivity.class));
                break;
        }
    }
}
