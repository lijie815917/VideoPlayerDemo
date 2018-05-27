package com.example.videoplayerlibrary.player;

import android.content.Context;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import java.io.IOException;
import java.lang.ref.WeakReference;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;

/**
 * Created by 眼神 on 2018/5/20.
 */

public class AliVideoPlayer extends AbstractVideoPlayer {
    
    private AliyunVodPlayer aliyunVodPlayer;
    private final AliVideoPlayer.AliPlayerListenerHolder mInternalListenerAdapter;
    private boolean mIsReleased;
    private Context mContext;
    AliyunLocalSource.AliyunLocalSourceBuilder abs;


    public AliVideoPlayer(Context context) {
        this.mContext = context;
        aliyunVodPlayer = new AliyunVodPlayer(mContext);
        //设置缓存目录路径。在创建播放器类,并在调用prepare方法之前设置。
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bigeye_save_cache";
        aliyunVodPlayer.setPlayingCache(true, sdDir, 60 * 60 /*单个文件的最大时长, s */, 300 /*缓存目录文件的总大小，MB*/);
        abs = new AliyunLocalSource.AliyunLocalSourceBuilder();
        this.mInternalListenerAdapter = new AliVideoPlayer.AliPlayerListenerHolder(this);
        this.attachInternalListeners();
    }

    @Override public void setDisplay(SurfaceHolder holder) {
        if (!this.mIsReleased) {
            this.aliyunVodPlayer.setDisplay(holder);
        }

    }


    @Override public void setDataSource(String pathUrl) throws IOException {
        abs.setSource(pathUrl);
    }


    @Override public void prepareAsync() {
        this.aliyunVodPlayer.prepareAsync(abs.build());
    }


    @Override public void start() {
        this.aliyunVodPlayer.start();
    }


    @Override public void stop() {
        this.aliyunVodPlayer.stop();
    }


    @Override public void pause() {
        this.aliyunVodPlayer.pause();
    }


    @Override public boolean isPlaying() {
        return this.aliyunVodPlayer.isPlaying();
    }


    @Override public void seekTo(long msec) {
        this.aliyunVodPlayer.seekTo((int) msec);
    }


    @Override public void release() {
        this.mIsReleased = true;
        this.aliyunVodPlayer.release();
        this.resetListeners();
        this.attachInternalListeners();
    }


    @Override public void reset() {
        this.aliyunVodPlayer.reset();
        this.resetListeners();
        this.attachInternalListeners();
    }


    @Override public void setVolume(float leftVolume, float rightVolume) {
        this.aliyunVodPlayer.setVolume((int) (leftVolume + rightVolume) / 2);
    }


    @Override public void setLooping(boolean looping) {
        this.aliyunVodPlayer.setCirclePlay(looping);
    }


    @Override public void setSpeed(float speed) {
        this.aliyunVodPlayer.setPlaySpeed(speed);
    }


    @Override public void setSurface(Surface surface) {
        this.aliyunVodPlayer.setSurface(surface);
    }


    @Override public long getCurrentPosition() {
        return (long)this.aliyunVodPlayer.getCurrentPosition();
    }


    @Override public long getDuration() {
        return (long)this.aliyunVodPlayer.getDuration();
    }


    @Override public long getBufferPercentage() {
        return this.aliyunVodPlayer.getBufferingPosition();
    }
    private void attachInternalListeners() {
        this.aliyunVodPlayer.setOnPreparedListener(this.mInternalListenerAdapter);
        this.aliyunVodPlayer.setOnBufferingUpdateListener( this.mInternalListenerAdapter);
        this.aliyunVodPlayer.setOnCompletionListener( this.mInternalListenerAdapter);
        this.aliyunVodPlayer.setOnSeekCompleteListener( this.mInternalListenerAdapter);
        this.aliyunVodPlayer.setOnVideoSizeChangedListener( this.mInternalListenerAdapter);
        this.aliyunVodPlayer.setOnErrorListener( this.mInternalListenerAdapter);
        this.aliyunVodPlayer.setOnInfoListener( this.mInternalListenerAdapter);
    }


    private class AliPlayerListenerHolder implements IAliyunVodPlayer.OnPreparedListener,
        IAliyunVodPlayer.OnCompletionListener,
        IAliyunVodPlayer.OnBufferingUpdateListener,
        IAliyunVodPlayer.OnSeekCompleteListener,
        IAliyunVodPlayer.OnVideoSizeChangedListener,
        IAliyunVodPlayer.OnErrorListener,
        IAliyunVodPlayer.OnInfoListener {
        public final WeakReference<AliVideoPlayer> mWeakMediaPlayer;


        public AliPlayerListenerHolder(AliVideoPlayer mp) {
            this.mWeakMediaPlayer = new WeakReference(mp);
        }

        @Override public void onBufferingUpdate(int percent) {
            AliVideoPlayer self =  this.mWeakMediaPlayer.get();
            if (self != null) {
                AliVideoPlayer.this.notifyOnBufferingUpdate(percent);
            }
        }


        @Override public void onCompletion() {
            AliVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                AliVideoPlayer.this.notifyOnCompletion();
            }
        }


        @Override public void onError(int what, int extra, String s) {
            AliVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null){
                AliVideoPlayer.this.notifyOnError(what, extra);
            }
        }


        @Override public void onInfo(int what, int extra) {
            AliVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null){
                AliVideoPlayer.this.notifyOnInfo(what, extra);
            }
        }


        @Override public void onPrepared() {
            AliVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                AliVideoPlayer.this.notifyOnPrepared();
            }
        }


        @Override public void onSeekComplete() {
            AliVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                AliVideoPlayer.this.notifyOnSeekComplete();
            }
        }


        @Override public void onVideoSizeChanged(int width, int height) {
            AliVideoPlayer self =  this.mWeakMediaPlayer.get();
            if (self != null) {
                AliVideoPlayer.this.notifyOnVideoSizeChanged(width, height, 1, 1);
            }
        }
    }
}
