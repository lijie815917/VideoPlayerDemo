package com.example.android.videoplayerdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.danikula.videocache.HttpProxyCacheServer;
import com.example.android.videoplayerdemo.R;
import com.example.videoplayerlibrary.EyeVideoPlayer;
import com.example.videoplayerlibrary.VideoPlayerManager;
import com.example.videoplayerlibrary.controllers.AtVideoPlayerController;
import com.example.videoplayerlibrary.controllers.TxVideoPlayerController;

import static com.example.android.videoplayerdemo.app.VideoApp.getProxy;

public class EXOVideoPlayerActivity extends AppCompatActivity {

    @Bind(R.id.video) EyeVideoPlayer video;

    private String videoPath = "http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super";


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_video_layout);
        ButterKnife.bind(this);
        init();
    }


    private void init() {
        HttpProxyCacheServer proxy = getProxy(this);
        String proxyUrl = proxy.getProxyUrl(videoPath);
        video.setPlayerType(EyeVideoPlayer.TYPE_IJK, this);
        video.isOpenGesture(true);
        AtVideoPlayerController controller = new AtVideoPlayerController(this);
        controller.setPathUrl(proxyUrl);
        // controller.setPathUrl("http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super");
        video.setController(controller);
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
