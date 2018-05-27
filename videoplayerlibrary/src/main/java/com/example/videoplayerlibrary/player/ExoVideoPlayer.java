package com.example.videoplayerlibrary.player;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

public class ExoVideoPlayer extends AbstractVideoPlayer{

    private SimpleExoPlayer exoPlayer;
    private final ExoVideoPlayer.ExoPlayerListenerHolder mInternalListenerAdapter;
    private boolean mIsReleased;
    private Context mContext;
    private Handler mainHandler;
    private BandwidthMeter bandwidthMeter;
    private DefaultTrackSelector trackSelector;
    private DefaultLoadControl loadControl;
    private DefaultRenderersFactory rendererFactory;
    private String pathUrl;
    private Uri pathUri;
    private MediaSource mMediaSource;


    public ExoVideoPlayer(Context context) {
        this.mContext = context;
        Looper eventLooper = Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper();
        mainHandler = new Handler(eventLooper);
        // 测量播放带宽，如果不需要可以传null
        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        rendererFactory = new DefaultRenderersFactory(mContext);
        loadControl = new DefaultLoadControl();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(mContext,trackSelector,loadControl);
        this.exoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mInternalListenerAdapter = new ExoVideoPlayer.ExoPlayerListenerHolder(this);
        this.attachInternalListeners();
    }

    @Override public void setDisplay(SurfaceHolder holder) {
        if (!this.mIsReleased) {
            setSurface(holder.getSurface());
        }
    }


    @Override public void setDataSource(String pathUrl) throws IOException {
        this.pathUrl = pathUrl;
        pathUri =  Uri.parse(pathUrl);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
            Util.getUserAgent(mContext, "VideoApp"),
            (TransferListener<? super DataSource>) bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        mMediaSource = new ExtractorMediaSource(pathUri,
            dataSourceFactory, extractorsFactory, null, null);

    }


    @Override public void prepareAsync() {
        this.exoPlayer.prepare(mMediaSource);
        exoPlayer.setPlayWhenReady(false);
    }


    @Override public void start() {
        this.exoPlayer.setPlayWhenReady(true);
    }


    @Override public void stop() {
        this.exoPlayer.release();
    }


    @Override public void pause() {
        this.exoPlayer.setPlayWhenReady(false);;
    }


    @Override public boolean isPlaying() {
        int state = exoPlayer.getPlaybackState();
        switch (state) {
            case Player.STATE_BUFFERING:
            case Player.STATE_READY:
                return exoPlayer.getPlayWhenReady();
            case Player.STATE_IDLE:
            case Player.STATE_ENDED:
            default:
                return false;
        }
    }


    @Override public void seekTo(long msec) {
        this.exoPlayer.seekTo((int) msec);
    }


    @Override public void release() {
        this.mIsReleased = true;
        this.exoPlayer.release();
        this.resetListeners();
        this.attachInternalListeners();
    }


    @Override public void reset() {
        this.exoPlayer.release();
        exoPlayer = null;
        this.resetListeners();
        this.attachInternalListeners();
    }


    @Override public void setVolume(float leftVolume, float rightVolume) {
        this.exoPlayer.setVolume((leftVolume + rightVolume) / 2);
    }


    @Override public void setLooping(boolean looping) {
        // TODO: no support
    }


    @Override public void setSpeed(float var1) {
        // TODO: no support
    }


    @Override public void setSurface(Surface surface) {
        this.exoPlayer.setVideoSurface(surface);
    }


    @Override public long getCurrentPosition() {
        return this.exoPlayer.getCurrentPosition();
    }


    @Override public long getDuration() {
        return this.exoPlayer.getDuration();
    }


    @Override public long getBufferPercentage() {
        return exoPlayer.getBufferedPercentage();
    }

    private void attachInternalListeners() {
         // this.exoPlayer.setVideoListener(mInternalListenerAdapter);
         this.exoPlayer.addListener(mInternalListenerAdapter);
    }

    private class ExoPlayerListenerHolder implements SimpleExoPlayer.VideoListener,SimpleExoPlayer.EventListener{
            public final WeakReference<ExoVideoPlayer> mWeakMediaPlayer;


        public ExoPlayerListenerHolder(ExoVideoPlayer mp) {
            this.mWeakMediaPlayer = new WeakReference(mp);
        }
        //     @Override public boolean onInfo (MediaPlayer mp,int what, int extra){
        //     AndroidVideoPlayer self = this.mWeakMediaPlayer.get();
        //     return self != null && AndroidVideoPlayer.this.notifyOnInfo(what, extra);
        // }


        @Override public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }


        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }


        @Override public void onLoadingChanged(boolean isLoading) {

        }


        @Override public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            ExoVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self == null) {
                return;
            }
            if(playbackState == ExoPlayer.STATE_ENDED) {//播放结束
                ExoVideoPlayer.this.notifyOnCompletion();
            } else if(playbackState == ExoPlayer.STATE_READY) {//准备播放
                    ExoVideoPlayer.this.notifyOnPrepared();
            }
        }


        @Override public void onRepeatModeChanged(int repeatMode) {

        }


        @Override public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }


        @Override public void onPlayerError(ExoPlaybackException error) {
            ExoVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
                ExoVideoPlayer.this.notifyOnError(1,1);
            }

        }


        @Override public void onPositionDiscontinuity(int reason) {

        }


        @Override public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }


        @Override public void onSeekProcessed() {
            ExoVideoPlayer self = this.mWeakMediaPlayer.get();
            if (self != null) {
            ExoVideoPlayer.this.notifyOnSeekComplete();
            }
        }


        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            ExoVideoPlayer self = this.mWeakMediaPlayer.get();
                if (self != null) {
                    ExoVideoPlayer.this.notifyOnVideoSizeChanged(width, height, 1, 1);
                }
        }


        @Override public void onRenderedFirstFrame() {

        }
    }

}
