package com.example.android.videoplayerdemo.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aliyun.recorder.AliyunRecorderCreator;
import com.aliyun.recorder.supply.AliyunIRecorder;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.MediaInfo;

public class AliShortVideoPlayerActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        AliyunIRecorder recorder = AliyunRecorderCreator.getRecorderInstance(this);
        glSurfaceView = new GLSurfaceView(this);
        recorder.setDisplayView(glSurfaceView);

        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setVideoWidth(300);
        mediaInfo.setVideoHeight(200);
        mediaInfo.setHWAutoSize(true);//硬编时自适应宽高为16的倍数
        recorder.setMediaInfo(mediaInfo);


    }




}
