package com.example.videoplayerlibrary.player;

import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.IOException;

public interface IVideoPlayer {

    void setDisplay(SurfaceHolder var1);

    void setDataSource(String var1) throws IOException;

    void prepareAsync();

    void start();

    void stop();

    void pause();

    boolean isPlaying();

    void seekTo(long var1);

    void release();

    void reset();

    void setVolume(float var1, float var2);

    void setLooping(boolean var1);

    void setSpeed(float var1);

    void setSurface(Surface var1);

    long getCurrentPosition();

    long getDuration();

    long getBufferPercentage();

    void setOnPreparedListener(IVideoPlayer.OnPreparedListener var1);

    void setOnCompletionListener(IVideoPlayer.OnCompletionListener var1);

    void setOnBufferingUpdateListener(IVideoPlayer.OnBufferingUpdateListener var1);

    void setOnSeekCompleteListener(IVideoPlayer.OnSeekCompleteListener var1);

    void setOnVideoSizeChangedListener(IVideoPlayer.OnVideoSizeChangedListener var1);

    void setOnErrorListener(IVideoPlayer.OnErrorListener var1);

    void setOnInfoListener(IVideoPlayer.OnInfoListener var1);


    public interface OnInfoListener {
        boolean onInfo(IVideoPlayer var1, int var2, int var3);
    }


    public interface OnErrorListener {
        boolean onError(IVideoPlayer var1, int var2, int var3);
    }


    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(IVideoPlayer var1, int var2, int var3, int var4, int var5);
    }


    public interface OnSeekCompleteListener {
        void onSeekComplete(IVideoPlayer var1);
    }


    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(IVideoPlayer var1, int var2);
    }


    public interface OnCompletionListener {
        void onCompletion(IVideoPlayer var1);
    }


    public interface OnPreparedListener {
        void onPrepared(IVideoPlayer var1);
    }

}
