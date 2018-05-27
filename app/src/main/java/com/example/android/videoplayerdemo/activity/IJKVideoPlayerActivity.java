package com.example.android.videoplayerdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.android.videoplayerdemo.R;
import com.example.videoplayerlibrary.EyeVideoPlayer;
import com.example.videoplayerlibrary.VideoPlayerManager;
import com.example.videoplayerlibrary.controllers.TxVideoPlayerController;
import com.example.videoplayerlibrary.views.Clarity;
import java.util.ArrayList;
import java.util.List;

public class IJKVideoPlayerActivity extends AppCompatActivity
    implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.video)
    EyeVideoPlayer video;
    @Bind(R.id.rb_sys_video)
    RadioButton rbSysVideo;
    @Bind(R.id.rb_ali_video)
    RadioButton rbAliVideo;
    @Bind(R.id.rb_ijk_video)
    RadioButton rbIjkVideo;
    @Bind(R.id.rb_exo_video)
    RadioButton rbExoVideo;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_stop_time)
    TextView tvStopTime;
    @Bind(R.id.btn_Mirror)
    Button btnMirror;
    @Bind(R.id.btn_speed)
    Button btnSpeed;
    @Bind(R.id.rg_video) RadioGroup rgVideo;
    @Bind(R.id.btn_a) Button btnA;
    @Bind(R.id.btn_b) Button btnB;
    @Bind(R.id.btn_ab) Button btnAB;


    private TxVideoPlayerController controller;
    private long aa;
    private long bb;

    private float speed = 1.0f;
    private Boolean isMirror = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijk_video_layout);
        ButterKnife.bind(this);
        initLayout();
        initIJK();
    }


    private void initLayout() {
        rgVideo.setOnCheckedChangeListener(this);
        btnSpeed.setOnClickListener(this);
        btnMirror.setOnClickListener(this);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnAB.setOnClickListener(this);
    }


    private void initIJK() {
        video.setPlayerType(EyeVideoPlayer.TYPE_ALI, this);
        video.isOpenGesture(false);
        controller = new TxVideoPlayerController(this);
        controller.setClarity(getClarites(), 0);
        video.setController(controller);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Mirror://设置镜像
                isMirror = !isMirror;
                video.setMirror(isMirror);
                break;
            case R.id.btn_speed://设置速度
                if (speed >= 2) {
                    speed = 0.50f;
                } else {
                    speed += 0.50f;
                }
                btnSpeed.setText("速度" + speed);
                video.setSpeed(speed);
                break;
            case R.id.btn_a:
                aa = video.getCurrentPosition();
                tvStartTime.setText("=" + aa);
                break;
            case R.id.btn_b:
                bb = video.getCurrentPosition();
                tvStopTime.setText("=" + bb);
                break;
            case R.id.btn_ab:

                break;
        }
    }


    @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_sys_video:
                VideoPlayerManager.instance().releaseVideoPlayer();
                video.setPlayerType(EyeVideoPlayer.TYPE_NATIVE, this);
                break;
            case R.id.rb_ali_video:
                VideoPlayerManager.instance().releaseVideoPlayer();
                video.setPlayerType(EyeVideoPlayer.TYPE_ALI, this);
                break;
            case R.id.rb_ijk_video:
                VideoPlayerManager.instance().releaseVideoPlayer();
                video.setPlayerType(EyeVideoPlayer.TYPE_IJK, this);
                break;
            case R.id.rb_exo_video:
                VideoPlayerManager.instance().releaseVideoPlayer();
                video.setPlayerType(EyeVideoPlayer.TYPE_EXO, this);
                break;

        }
    }


    public List<Clarity> getClarites() {
        List<Clarity> clarities = new ArrayList<>();
        clarities.add(new Clarity("标清", "270P",
            "http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        clarities.add(new Clarity("高清", "480P",
            "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        clarities.add(new Clarity("超清", "720P",
            "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        clarities.add(new Clarity("蓝光", "1080P",
            "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        return clarities;
    }


    @Override protected void onResume() {
        super.onResume();
        video.start();
    }


    @Override protected void onPause() {
        super.onPause();
        video.pause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

}
