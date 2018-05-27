package com.example.android.videoplayerdemo.interfaces;

public interface OnVideoPlayerEventListener {
    /**
     * 视频初始化成功
     */
    void onPrepared();
    /***
     * 切换视频（指定播放内容）
     */
    void onChange();
    /**
     * 播放
     */
    void onMediaPlayerStart();
    /**
     * 暂停
     */
    void onMediaPlayerPause();
    /**
     * 更新进度
     */
    void onUpdateProgress(int position);
    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

}
