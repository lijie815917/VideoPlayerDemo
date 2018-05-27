package com.example.videoplayerlibrary.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by 眼神 on 2018/5/20.
 */

public class AndroidVideoPlayer extends AbstractVideoPlayer {

    private final MediaPlayer mInternalMediaPlayer;
    private final AndroidVideoPlayer.SystemMediaPlayerListenerHolder mInternalListenerAdapter;
    private boolean mIsReleased;
    private Context mContext;

    public AndroidVideoPlayer(Context context) {
        this.mContext = context;
        mInternalMediaPlayer = new MediaPlayer();
        this.mInternalMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mInternalListenerAdapter = new AndroidVideoPlayer.SystemMediaPlayerListenerHolder(this);
        this.attachInternalListeners();
    }


    @Override public void setDisplay(SurfaceHolder holder) {
        if (!this.mIsReleased) {
            this.mInternalMediaPlayer.setDisplay(holder);
        }
    }


    @Override public void setDataSource(String pathUrl) throws IOException {
        this.mInternalMediaPlayer.setDataSource(pathUrl);
    }


    @Override public void prepareAsync() {
        this.mInternalMediaPlayer.prepareAsync();
    }


    @Override public void start() {
        this.mInternalMediaPlayer.start();
    }


    @Override public void stop() {
        this.mInternalMediaPlayer.stop();
    }


    @Override public void pause() {
        this.mInternalMediaPlayer.pause();
    }


    @Override public boolean isPlaying() {
        return this.mInternalMediaPlayer.isPlaying();
    }


    @Override public void seekTo(long msec) {
        this.mInternalMediaPlayer.seekTo((int) msec);
    }


    @Override public void release() {
        this.mIsReleased = true;
        this.mInternalMediaPlayer.release();
        this.resetListeners();
        this.attachInternalListeners();
    }


    @Override public void reset() {
        this.mInternalMediaPlayer.reset();
        this.resetListeners();
        this.attachInternalListeners();
    }


    @Override public void setVolume(float leftVolume, float rightVolume) {
        this.mInternalMediaPlayer.setVolume(leftVolume,rightVolume);
    }


    @Override public void setLooping(boolean looping) {
        this.mInternalMediaPlayer.setLooping(looping);
    }


    @Override public void setSpeed(float var1) {
        //原生没有速度
    }


    @Override public void setSurface(Surface surface) {
         this.mInternalMediaPlayer.setSurface(surface);
    }


    @Override public long getCurrentPosition() {
        return (long)this.mInternalMediaPlayer.getCurrentPosition();
    }


    @Override public long getDuration() {
        return (long)this.mInternalMediaPlayer.getDuration();
    }


    @Override public long getBufferPercentage() {
       return 0;
    }


    private void attachInternalListeners() {
        this.mInternalMediaPlayer.setOnPreparedListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnBufferingUpdateListener( this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnCompletionListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnSeekCompleteListener( this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnVideoSizeChangedListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnErrorListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnInfoListener(this.mInternalListenerAdapter);
    }


    private class SystemMediaPlayerListenerHolder implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener {
        public final WeakReference<AndroidVideoPlayer> mWeakMediaPlayer;


        public SystemMediaPlayerListenerHolder(AndroidVideoPlayer mp) {
            this.mWeakMediaPlayer = new WeakReference(mp);
        }

        @Override public void onBufferingUpdate(MediaPlayer mp, int percent) {
            AndroidVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                AndroidVideoPlayer.this.notifyOnBufferingUpdate(percent);
            }
        }


        @Override public void onCompletion(MediaPlayer mp) {
            AndroidVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                AndroidVideoPlayer.this.notifyOnCompletion();
            }
        }


        @Override public boolean onError(MediaPlayer mp, int what, int extra) {
            AndroidVideoPlayer self = this.mWeakMediaPlayer.get();
            return self != null && AndroidVideoPlayer.this.notifyOnError(what, extra);
        }


        @Override public boolean onInfo(MediaPlayer mp, int what, int extra) {
            AndroidVideoPlayer self = this.mWeakMediaPlayer.get();
            return self != null && AndroidVideoPlayer.this.notifyOnInfo(what, extra);
        }


        @Override public void onPrepared(MediaPlayer mp) {
            AndroidVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                AndroidVideoPlayer.this.notifyOnPrepared();
            }
        }


        @Override public void onSeekComplete(MediaPlayer mp) {
            AndroidVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                AndroidVideoPlayer.this.notifyOnSeekComplete();
            }
        }


        @Override public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            AndroidVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                AndroidVideoPlayer.this.notifyOnVideoSizeChanged(width, height, 1, 1);
            }
        }
    }

}
