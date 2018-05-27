package com.example.android.videoplayerdemo.manager;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.Toast;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.example.android.videoplayerdemo.data.VideoData;
import com.example.android.videoplayerdemo.interfaces.OnVideoPlayerEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 播放视频的控制器(单例)
 */
public class VideoPlayerManage {
    /**
     * 播放的状态（空闲、准备、播放、暂停）
     */
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARED = 1;
    private static final int STATE_STARTED = 2;
    private static final int STATE_PAUSED = 3;
    private int state = STATE_IDLE;
    /**
     * 刷新进度条的时间1s
     */
    private static final long UPDATE_PROGRESS_TIME = 1000;


    private Context mContext;
    private Handler mHandler;
    /**
     * 阿里云播放器
     */
    private AliyunVodPlayer aliPlayer;
    private Boolean isSurfaceDestroy = false;
    /**
     * 视频源
     */
    private List<VideoData> videoData;
    private String videoUri;
    /**
     * 视频播放事件的监听集合
     */
    private final List<OnVideoPlayerEventListener> listeners = new ArrayList<>();

    public VideoPlayerManage() {
    }

    /**
     * 获取控制器
     */
    public static  VideoPlayerManage getPlayerManage(){
        return SingletonHolder.instance;
    }


    private static class SingletonHolder{
        private static VideoPlayerManage instance = new VideoPlayerManage();
    }

    public void init(Context context , SurfaceView surfaceView , String videoPath){
        this.mContext = context;
        //初始化视频播放列表
        // videoData =
        this.videoUri = videoPath;
        //初始化播放器
        aliPlayer = new AliyunVodPlayer(mContext);
        //刷新UI
        mHandler = new Handler(Looper.getMainLooper());
        /**
         * 阿里播放器的监听
         */
        aliPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
            @Override public void onPrepared() {
                aliPlayer.getDuration();
                for (OnVideoPlayerEventListener listener : listeners){
                    listener.onPrepared();
                }
                //准备就绪
                if (isPreparing()){
                    startPlayer();
                }
            }
        });
        aliPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
            @Override public void onError(int i, int i1, String s) {
                Toast.makeText(mContext,"视频出错了！"+s , Toast.LENGTH_LONG);
            }
        });
        aliPlayer.setOnBufferingUpdateListener(new IAliyunVodPlayer.OnBufferingUpdateListener() {
            @Override public void onBufferingUpdate(int percent) {
                for (OnVideoPlayerEventListener listener : listeners){
                 listener.onBufferingUpdate(percent);
                }
            }
        });
        aliPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
            @Override public void onCompletion() {
                //播放完成
                //nextplay()
            }
        });
        aliPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
            @Override public void onSeekComplete() {
                //seek完成
            }
        });

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override public void surfaceCreated(SurfaceHolder holder) {
                holder.setKeepScreenOn(true);
                if (aliPlayer!= null){
                    aliPlayer.setDisplay(holder);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override public void surfaceDestroyed(SurfaceHolder holder) {
                isSurfaceDestroy = true;
            }
        });


    }

    /**
     * 添加监听到集合中
     */
    public void addOnVideoPlayerEventListener(OnVideoPlayerEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 获得播放器
     */
    public AliyunVodPlayer getAliPlayer() {
        return aliPlayer;
    }
    /**
     * 获得状态
     */
    public boolean isIdle(){
        return state == STATE_IDLE;
    }
    public boolean isPreparing(){
        return state == STATE_PREPARED;
    }
    public boolean isPlaying(){
        return state == STATE_STARTED;
    }
    public boolean isPausing(){
        return state == STATE_PAUSED;
    }

    public boolean getSurfaceState(){
        return isSurfaceDestroy;
    }


    /**
     * 准备就绪后播放
     */
    public void startPlayer(){
        if (!isPreparing() && !isPausing()){
            return;
        }
        aliPlayer.start();
        mHandler.removeCallbacks(updateRunnable);
        mHandler.post(updateRunnable);
        for (OnVideoPlayerEventListener listener : listeners){
            listener.onMediaPlayerStart();
        }
        state = STATE_STARTED;
    }


    /**
     * 指定播放
     */
    public void play(){
        // if (videoData.isEmpty()){
        //     return;
        // }
        aliPlayer.reset();
        // aliPlayer.
        //设置视频源
        AliyunLocalSource.AliyunLocalSourceBuilder abs = new AliyunLocalSource.AliyunLocalSourceBuilder();
        abs.setSource(videoUri);
        AliyunLocalSource mLocalSource = abs.build();
        aliPlayer.prepareAsync(mLocalSource);
        for (OnVideoPlayerEventListener listener : listeners){
            listener.onChange();
        }
        state = STATE_STARTED;
    }
    /**
     * 暂停播放
     */
    public void playPause() {
        if (isPreparing()) {
            stopPlayer();
        } else if (isPlaying()) {
            pausePlayer();
        } else if (isPausing()) {
            startPlayer();
        } else {
            play();
        }
    }
    public void pausePlayer() {
        if (!isPlaying()){
            return;
        }
        aliPlayer.pause();
        mHandler.removeCallbacks(updateRunnable);
        for (OnVideoPlayerEventListener listener : listeners){
            listener.onMediaPlayerPause();
        }
        state = STATE_PAUSED;
    }

    /**
     * 结束播放
     */
    public void stopPlayer(){
        if (isIdle()){
            return;
        }
        pausePlayer();
        aliPlayer.reset();
        state = STATE_IDLE;
    }

    public void seekTo(int msec){
        if (isPausing() || isPlaying()){
            aliPlayer.seekTo(msec);
        }
        for (OnVideoPlayerEventListener listener : listeners){
            listener.onUpdateProgress(msec);
        }
    }

    /**
     * 更新页面进度的线程
     */
    private Runnable  updateRunnable = new Runnable() {
        @Override public void run() {
              if (aliPlayer == null){
                  return;
              }
              if (isPlaying()){
                  for (OnVideoPlayerEventListener listener : listeners){
                      listener.onUpdateProgress((int)aliPlayer.getCurrentPosition());
                  }
              }
              mHandler.postDelayed(updateRunnable,UPDATE_PROGRESS_TIME);
        }
    };


}
