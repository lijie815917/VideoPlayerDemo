package com.example.android.videoplayerdemo.app;

import android.app.Application;
import android.content.Context;
import com.alivc.player.AliVcMediaPlayer;
import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.danikula.videocache.HttpProxyCacheServer;

public class VideoApp extends Application {


    private HttpProxyCacheServer proxyCache;

    @Override public void onCreate() {
        super.onCreate();

        // System.loadLibrary("liblive-openh264");
        // System.loadLibrary("libQuCore");
        // System.loadLibrary("libQuCore-ThirdParty");

        // QupaiHttpFinal.getInstance().initOkHttpFinal();
        // com.aliyun.vod.common.httpfinal.QupaiHttpFinal.getInstance().initOkHttpFinal();
    }

    public static HttpProxyCacheServer getProxy(Context context){
        VideoApp app = (VideoApp) context.getApplicationContext();
        return app.proxyCache == null ? (app.proxyCache = app.newProxy()) : app.proxyCache;
    }
    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
