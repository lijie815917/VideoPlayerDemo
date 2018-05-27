package com.example.videoplayerlibrary.player;

import android.content.Context;
import android.media.AudioManager;
import android.view.Surface;

import android.view.SurfaceHolder;
import java.io.IOException;

import java.lang.ref.WeakReference;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by 眼神 on 2018/5/20.
 */

public class IJKVideoPlayer extends AbstractVideoPlayer{

    private IjkMediaPlayer ijkMediaPlayer;
    private final IJKVideoPlayer.IJKPlayerListenerHolder mInternalListenerAdapter;
    private boolean mIsReleased;
    private Context mContext;


    public IJKVideoPlayer(Context context) {
        this.mContext = context;
        ijkMediaPlayer = new IjkMediaPlayer();
        this.ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        this.ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mInternalListenerAdapter = new IJKVideoPlayer.IJKPlayerListenerHolder(this);
        this.attachInternalListeners();
    }

    @Override public void setDisplay(SurfaceHolder holder) {
        if (!this.mIsReleased) {
            this.ijkMediaPlayer.setDisplay(holder);
        }
    }


    @Override public void setDataSource(String pathUrl) throws IOException {
        this.ijkMediaPlayer.setDataSource(pathUrl);
    }


    @Override public void prepareAsync() {
        this.ijkMediaPlayer.prepareAsync();
    }


    @Override public void start() {
        this.ijkMediaPlayer.start();
    }


    @Override public void stop() {
        this.ijkMediaPlayer.stop();
    }


    @Override public void pause() {
        this.ijkMediaPlayer.pause();
    }


    @Override public boolean isPlaying() {
        return this.ijkMediaPlayer.isPlaying();
    }


    @Override public void seekTo(long msec) {
        this.ijkMediaPlayer.seekTo((int) msec);
    }


    @Override public void release() {
        this.mIsReleased = true;
        this.ijkMediaPlayer.release();
        this.resetListeners();
        this.attachInternalListeners();
    }


    @Override public void reset() {
        this.ijkMediaPlayer.reset();
        this.resetListeners();
        this.attachInternalListeners();
    }


    @Override public void setVolume(float leftVolume, float rightVolume) {
        this.ijkMediaPlayer.setVolume(leftVolume,rightVolume);
    }


    @Override public void setLooping(boolean looping) {
        this.ijkMediaPlayer.setLooping(looping);
    }


    @Override public void setSpeed(float speed) {
        ijkMediaPlayer.setSpeed(speed);
    }


    @Override public void setSurface(Surface surface) {
        this.ijkMediaPlayer.setSurface(surface);
    }


    @Override public long getCurrentPosition() {
        return this.ijkMediaPlayer.getCurrentPosition();
    }


    @Override public long getDuration() {
        return this.ijkMediaPlayer.getDuration();
    }


    @Override public long getBufferPercentage() {
        return this.getBufferPercentage();
    }


    private void attachInternalListeners() {
        this.ijkMediaPlayer.setOnPreparedListener(this.mInternalListenerAdapter);
        this.ijkMediaPlayer.setOnBufferingUpdateListener(this.mInternalListenerAdapter);
        this.ijkMediaPlayer.setOnCompletionListener(this.mInternalListenerAdapter);
        this.ijkMediaPlayer.setOnSeekCompleteListener(this.mInternalListenerAdapter);
        this.ijkMediaPlayer.setOnVideoSizeChangedListener( this.mInternalListenerAdapter);
        this.ijkMediaPlayer.setOnErrorListener( this.mInternalListenerAdapter);
        this.ijkMediaPlayer.setOnInfoListener(this.mInternalListenerAdapter);
    }

    private class IJKPlayerListenerHolder implements IjkMediaPlayer.OnPreparedListener,
        IjkMediaPlayer.OnCompletionListener,
        IjkMediaPlayer.OnBufferingUpdateListener,
        IjkMediaPlayer.OnSeekCompleteListener,
        IjkMediaPlayer.OnVideoSizeChangedListener,
        IjkMediaPlayer.OnErrorListener,
        IjkMediaPlayer.OnInfoListener {
        public final WeakReference<IJKVideoPlayer> mWeakMediaPlayer;


        public IJKPlayerListenerHolder(IJKVideoPlayer mp) {
            this.mWeakMediaPlayer = new WeakReference(mp);
        }

        @Override public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
            IJKVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                IJKVideoPlayer.this.notifyOnBufferingUpdate(percent);
            }
        }


        @Override public void onCompletion(IMediaPlayer iMediaPlayer) {
            IJKVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                IJKVideoPlayer.this.notifyOnCompletion();
            }
        }


        @Override public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
            IJKVideoPlayer self = this.mWeakMediaPlayer.get();
            return self != null && IJKVideoPlayer.this.notifyOnError(what, extra);
        }


        @Override public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
            IJKVideoPlayer self = this.mWeakMediaPlayer.get();
            return self != null && IJKVideoPlayer.this.notifyOnInfo(what, extra);
        }


        @Override public void onPrepared(IMediaPlayer iMediaPlayer) {
            IJKVideoPlayer self =  this.mWeakMediaPlayer.get();
            if (self != null) {
                IJKVideoPlayer.this.notifyOnPrepared();
            }
        }


        @Override public void onSeekComplete(IMediaPlayer iMediaPlayer) {
            IJKVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                IJKVideoPlayer.this.notifyOnSeekComplete();
            }
        }


        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int i2, int i3) {
            IJKVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                IJKVideoPlayer.this.notifyOnVideoSizeChanged(width, height, 1, 1);
            }
        }
    }

}
