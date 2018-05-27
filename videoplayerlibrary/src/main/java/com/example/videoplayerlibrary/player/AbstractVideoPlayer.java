package com.example.videoplayerlibrary.player;

import com.example.videoplayerlibrary.player.IVideoPlayer;

/**
 * Created by 眼神 on 2018/5/20.
 */

public abstract class AbstractVideoPlayer implements IVideoPlayer {

    private OnPreparedListener onPreparedListener;
    private OnCompletionListener onCompletionListener;
    private OnBufferingUpdateListener onBufferingUpdateListener;
    private OnSeekCompleteListener onSeekCompleteListener;
    private OnVideoSizeChangedListener onVideoSizeChangedListener;
    private OnErrorListener onErrorListener;
    private OnInfoListener onInfoListener;

    public AbstractVideoPlayer() {
    }

    @Override public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    @Override public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    @Override
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener) {
        this.onBufferingUpdateListener = onBufferingUpdateListener;
    }

    @Override public void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener) {
        this.onSeekCompleteListener = onSeekCompleteListener;
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.onVideoSizeChangedListener = onVideoSizeChangedListener;
    }

    @Override public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    @Override public void setOnInfoListener(OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
    }

    public void resetListeners() {
        this.onPreparedListener = null;
        this.onBufferingUpdateListener = null;
        this.onCompletionListener = null;
        this.onSeekCompleteListener = null;
        this.onVideoSizeChangedListener = null;
        this.onErrorListener = null;
        this.onInfoListener = null;
    }

    protected final void notifyOnPrepared() {
        if (this.onPreparedListener != null) {
            this.onPreparedListener.onPrepared(this);
        }

    }

    protected final void notifyOnCompletion() {
        if (this.onCompletionListener != null) {
            this.onCompletionListener.onCompletion(this);
        }

    }

    protected final void notifyOnBufferingUpdate(int percent) {
        if (this.onBufferingUpdateListener != null) {
            this.onBufferingUpdateListener.onBufferingUpdate(this, percent);
        }

    }

    protected final void notifyOnSeekComplete() {
        if (this.onSeekCompleteListener != null) {
            this.onSeekCompleteListener.onSeekComplete(this);
        }

    }

    protected final void notifyOnVideoSizeChanged(int width, int height, int sarNum, int sarDen) {
        if (this.onVideoSizeChangedListener != null) {
            this.onVideoSizeChangedListener.onVideoSizeChanged(this, width, height, sarNum, sarDen);
        }
    }

    protected final boolean notifyOnError(int what, int extra) {
        return this.onErrorListener != null && this.onErrorListener.onError(this, what, extra);
    }

    protected final boolean notifyOnInfo(int what, int extra) {
        return this.onInfoListener != null && this.onInfoListener.onInfo(this, what, extra);
    }


}
