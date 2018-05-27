package com.example.videoplayerlibrary;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.videoplayerlibrary.player.AliVideoPlayer;
import com.example.videoplayerlibrary.player.ExoVideoPlayer;
import com.example.videoplayerlibrary.player.IJKVideoPlayer;
import com.example.videoplayerlibrary.player.IVideoPlayer;
import com.example.videoplayerlibrary.player.AndroidVideoPlayer;
import com.example.videoplayerlibrary.utils.LogUtil;
import com.example.videoplayerlibrary.utils.NiceUtil;
import com.example.videoplayerlibrary.views.VideoTextureView;
import java.io.IOException;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 播放器主要参考
 * ijkplayer : https://github.com/Bilibili/ijkplayer
 * EXOPlayer : https://github.com/bigeyechou/ExoPlayer
 * NiceVieoPlayer : https://github.com/xiaoyanger0825/NiceVieoPlayer
 * GSYVideoPlayer : https://github.com/bigeyechou/GSYVideoPlayer
 */
public class EyeVideoPlayer extends FrameLayout implements OnVideoPlayerEventListener,
    TextureView.SurfaceTextureListener {
    /**
     * 播放状态
     **/
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_BUFFERING_PLAYING = 5; // 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
    public static final int STATE_BUFFERING_PAUSED = 6;
        // 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
    public static final int STATE_COMPLETED = 7;

    /**
     * 播放模式
     **/
    public static final int MODE_NORMAL = 10;
    public static final int MODE_FULL_SCREEN = 11;
    public static final int MODE_TINY_WINDOW = 12;

    /**
     * 播放器类型
     */
    public static final int TYPE_IJK = 111;
    public static final int TYPE_NATIVE = 222;
    public static final int TYPE_ALI = 333;
    public static final int TYPE_EXO = 444;

    /**
     * 设置默认
     */
    private int mPlayerType = TYPE_ALI;
    private int mCurrentState = STATE_IDLE;
    private int mCurrentMode = MODE_NORMAL;
    /**
     * 是否开启手势
     */
    public boolean isGesture = true;

    private Context mContext;
    private AudioManager mAudioManager;
    //支持原生、Ali、IJK、EXO
    private IVideoPlayer mMediaPlayer;
    private FrameLayout mContainer;
    private VideoTextureView mTextureView;
    private VideoPlayerBaseController mController;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private String mVideoPath;
    private int mBufferPercentage;
    private boolean continueFromLastPosition = true;
    private long skipToPosition;

    public EyeVideoPlayer(@NonNull Context context) {
        this(context, null);
    }


    public EyeVideoPlayer(
        @NonNull Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    private void init() {
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mContainer, params);
    }


    public void setController(VideoPlayerBaseController controller) {
        mContainer.removeView(mContainer);
        mController = controller;
        mController.reset();
        mController.setVideoPlayerView(this);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mController, params);
    }


    /**
     * 设置播放器类型
     */
    public void setPlayerType(int playerType, Context context) {
        mPlayerType = playerType;
        mContext = context;
    }

    /**
     * 设置播放地址
     * @param pathUrl
     */
    @Override public void setVideoPath(String pathUrl) {
        this.mVideoPath = pathUrl;
    }


    @Override public void start() {
        if (mCurrentState == STATE_IDLE) {
            VideoPlayerManager.instance().setCurrentVideoPlayerView(this);
            initAudioManager();
            initMediaPlayer();
            initTextureView();
            addTextureView();
        } else if (mCurrentState == STATE_PAUSED || mCurrentState ==STATE_BUFFERING_PAUSED){
            this.mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }else {
            Log.d("videoLog====", "只有在mCurrentState == STATE_IDLE时才能调用start方法.");
        }
    }


    @Override public void start(long position) {
        skipToPosition = position;
        start();
    }


    @Override public void restart() {
        if (mCurrentState == STATE_PAUSED) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_PLAYING");
        } else if (mCurrentState == STATE_BUFFERING_PAUSED) {
            mMediaPlayer.start();
            mCurrentState = STATE_BUFFERING_PLAYING;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_BUFFERING_PLAYING");
        } else if (mCurrentState == STATE_COMPLETED || mCurrentState == STATE_ERROR) {
            mMediaPlayer.reset();
            openMediaPlayer();
        } else {
            LogUtil.d("在mCurrentState == " + mCurrentState + "时不能调用restart()方法.");
        }
    }


    @Override public void pause() {
        if (mCurrentState == STATE_PLAYING) {
            mMediaPlayer.pause();
            mCurrentState = STATE_PAUSED;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_PAUSED");
        }
        if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mMediaPlayer.pause();
            mCurrentState = STATE_BUFFERING_PAUSED;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_BUFFERING_PAUSED");
        }
    }


    @Override
    public void seekTo(long pos) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(pos);
        }
    }


    @Override
    public void setVolume(int volume) {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }


    @Override public void setSpeed(float speed) {
        if (mMediaPlayer instanceof IJKVideoPlayer || mMediaPlayer instanceof AliVideoPlayer) {
            mMediaPlayer.setSpeed(speed);
        } else {
            Log.d("videoLog====", "当前播放器不能设置播放速度");
        }
    }


    @Override public void continueFromLastPosition(boolean continueFromLastPosition) {
        this.continueFromLastPosition = continueFromLastPosition;
    }


    @Override
    public boolean isIdle() {
        return mCurrentState == STATE_IDLE;
    }


    @Override
    public boolean isPreparing() {
        return mCurrentState == STATE_PREPARING;
    }


    @Override
    public boolean isPrepared() {
        return mCurrentState == STATE_PREPARED;
    }


    @Override
    public boolean isBufferingPlaying() {
        return mCurrentState == STATE_BUFFERING_PLAYING;
    }


    @Override
    public boolean isBufferingPaused() {
        return mCurrentState == STATE_BUFFERING_PAUSED;
    }


    @Override
    public boolean isPlaying() {
        return mCurrentState == STATE_PLAYING;
    }


    @Override
    public boolean isPaused() {
        return mCurrentState == STATE_PAUSED;
    }


    @Override
    public boolean isError() {
        return mCurrentState == STATE_ERROR;
    }


    @Override
    public boolean isCompleted() {
        return mCurrentState == STATE_COMPLETED;
    }


    @Override
    public boolean isFullScreen() {
        return mCurrentMode == MODE_FULL_SCREEN;
    }


    @Override
    public boolean isTinyWindow() {
        return mCurrentMode == MODE_TINY_WINDOW;
    }


    @Override
    public boolean isNormal() {
        return mCurrentMode == MODE_NORMAL;
    }


    @Override public void isOpenGesture(Boolean gesture) {
        this.isGesture =  gesture;
    }


    @Override public boolean isGesture() {
        return isGesture;
    }


    @Override
    public int getMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }


    @Override
    public int getVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }


    @Override
    public long getDuration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }


    @Override public long getCurrentPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }


    @Override public int getBufferPercentage() {
        return mBufferPercentage;
    }


    /**
     * 设置镜像
     */
    @Override public void setMirror(boolean mirror) {
        Matrix matrix = new Matrix();
        if (mirror){
            matrix.setScale(-1, 1, mTextureView.getWidth() / 2, 0);
        }else {
            matrix.setScale(1, 1, mTextureView.getWidth() / 2, 0);
        }
        mTextureView.setTransform(matrix);
        mTextureView.invalidate();
    }


    private void openMediaPlayer() {
        // 屏幕常亮
        mContainer.setKeepScreenOn(true);
        // 设置监听
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        mMediaPlayer.setOnInfoListener(mOnInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        // 设置dataSource
        try {
            mMediaPlayer.setDataSource(mVideoPath);
            if (mSurface == null) {
                mSurface = new Surface(mSurfaceTexture);
            }
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("STATE_PREPARING");
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e("打开播放器发生错误", e);
        }
    }


    private IVideoPlayer.OnPreparedListener mOnPreparedListener
        = new IVideoPlayer.OnPreparedListener() {
        @Override public void onPrepared(IVideoPlayer mp) {
            mCurrentState = STATE_PREPARED;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("onPrepared ——> STATE_PREPARED");
            mp.start();
            // 从上次的保存位置播放
            if (continueFromLastPosition) {
                long savedPlayPosition = NiceUtil.getSavedPlayPosition(mContext, mVideoPath);
                mp.seekTo(savedPlayPosition);
            }
            // 跳到指定位置播放
            if (skipToPosition != 0) {
                mp.seekTo(skipToPosition);
            }
        }
    };
    private IVideoPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener
        = new IVideoPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IVideoPlayer mp, int width, int height, int sar_num, int sar_den) {
            mTextureView.adaptVideoSize(width, height);
            LogUtil.d("onVideoSizeChanged ——> width：" + width + "， height：" + height);
        }

    };
    private IVideoPlayer.OnCompletionListener mOnCompletionListener
        = new IVideoPlayer.OnCompletionListener() {
        @Override public void onCompletion(IVideoPlayer mp) {
            mCurrentState = STATE_COMPLETED;
            mController.onPlayStateChanged(mCurrentState);
            LogUtil.d("onCompletion ——> STATE_COMPLETED");
            // 清除屏幕常亮
            mContainer.setKeepScreenOn(false);
        }
    };
    private IVideoPlayer.OnErrorListener mOnErrorListener
        = new IVideoPlayer.OnErrorListener() {
        @Override public boolean onError(IVideoPlayer mp, int what, int extra) {
            // 直播流播放时去调用mediaPlayer.getDuration会导致-38和-2147483648错误，忽略该错误
            if (what != -38 && what != -2147483648 && extra != -38 && extra != -2147483648) {
                mCurrentState = STATE_ERROR;
                mController.onPlayStateChanged(mCurrentState);
                LogUtil.d("onError ——> STATE_ERROR ———— what：" + what + ", extra: " + extra);
            }
            return true;
        }
    };

    private IVideoPlayer.OnInfoListener mOnInfoListener
        = new IVideoPlayer.OnInfoListener() {
        @Override public boolean onInfo(IVideoPlayer mp, int what, int extra) {
            if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                // 播放器开始渲染
                mCurrentState = STATE_PLAYING;
                mController.onPlayStateChanged(mCurrentState);
                LogUtil.d("onInfo ——> MEDIA_INFO_VIDEO_RENDERING_START：STATE_PLAYING");
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                // MediaPlayer暂时不播放，以缓冲更多的数据
                if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_BUFFERING_PAUSED;
                    LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PAUSED");
                } else {
                    mCurrentState = STATE_BUFFERING_PLAYING;
                    LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PLAYING");
                }
                mController.onPlayStateChanged(mCurrentState);
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                // 填充缓冲区后，MediaPlayer恢复播放/暂停
                if (mCurrentState == STATE_BUFFERING_PLAYING) {
                    mCurrentState = STATE_PLAYING;
                    mController.onPlayStateChanged(mCurrentState);
                    LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PLAYING");
                }
                if (mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_PAUSED;
                    mController.onPlayStateChanged(mCurrentState);
                    LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PAUSED");
                }
            } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
                // 视频旋转了extra度，需要恢复
                if (mTextureView != null) {
                    mTextureView.setRotation(extra);
                    LogUtil.d("视频旋转角度：" + extra);
                }
            } else if (what == IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
                LogUtil.d("视频不能seekTo，为直播视频");
            } else {
                LogUtil.d("onInfo ——> what：" + what);
            }
            return true;
        }
    };

    private IVideoPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener
        = new IVideoPlayer.OnBufferingUpdateListener() {
        @Override public void onBufferingUpdate(IVideoPlayer mp, int percent) {
            mBufferPercentage = percent;
        }

    };

    /**
     * 全屏
     */
    @Override public void enterFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            return;
        }

        // 隐藏ActionBar、状态栏，并横屏
        NiceUtil.hideActionBar(mContext);
        NiceUtil.scanForActivity(mContext)
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ViewGroup contentView = (ViewGroup) NiceUtil.scanForActivity(mContext)
            .findViewById(android.R.id.content);
        if (mCurrentMode == MODE_TINY_WINDOW) {
            contentView.removeView(mContainer);
        } else {
            this.removeView(mContainer);
        }
        LayoutParams params = new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(mContainer, params);

        mCurrentMode = MODE_FULL_SCREEN;
        mController.onPlayModeChanged(mCurrentMode);
    }


    /**
     * 退出全屏
     */
    @Override public boolean exitFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            NiceUtil.showActionBar(mContext);
            NiceUtil.scanForActivity(mContext)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            ViewGroup contentView = (ViewGroup) NiceUtil.scanForActivity(mContext)
                .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mContainer, params);

            mCurrentMode = MODE_NORMAL;
            mController.onPlayModeChanged(mCurrentMode);
            return true;
        }
        return false;
    }


    /**
     * 进入小窗口
     */
    @Override public void enterTinyWindow() {
        if (mCurrentMode == MODE_TINY_WINDOW) {
            return;
        }
        this.removeView(mContainer);

        ViewGroup contentView = (ViewGroup) NiceUtil.scanForActivity(mContext)
            .findViewById(android.R.id.content);
        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            (int) (NiceUtil.getScreenWidth(mContext) * 0.6f),
            (int) (NiceUtil.getScreenWidth(mContext) * 0.6f * 9f / 16f));
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.rightMargin = NiceUtil.dp2px(mContext, 8f);
        params.bottomMargin = NiceUtil.dp2px(mContext, 8f);

        contentView.addView(mContainer, params);

        mCurrentMode = MODE_TINY_WINDOW;
        mController.onPlayModeChanged(mCurrentMode);
    }


    /**
     * 退出小窗口
     */
    @Override public boolean exitTinyWindow() {
        if (mCurrentMode == MODE_TINY_WINDOW) {
            ViewGroup contentView = (ViewGroup) NiceUtil.scanForActivity(mContext)
                .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mContainer, params);

            mCurrentMode = MODE_NORMAL;
            mController.onPlayModeChanged(mCurrentMode);
            return true;
        }
        return false;
    }


    @Override public void releasePlayer() {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(null);
            mAudioManager = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mContainer.removeView(mTextureView);
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCurrentState = STATE_IDLE;
    }


    @Override public void release() {
        // 保存播放位置
        if (isPlaying() || isBufferingPlaying() || isBufferingPaused() || isPaused()) {
            NiceUtil.savePlayPosition(mContext, mVideoPath, getCurrentPosition());
        } else if (isCompleted()) {
            NiceUtil.savePlayPosition(mContext, mVideoPath, 0);
        }
        // 退出全屏或小窗口
        if (isFullScreen()) {
            exitFullScreen();
        }
        if (isTinyWindow()) {
            exitTinyWindow();
        }
        mCurrentMode = MODE_NORMAL;

        // 释放播放器
        releasePlayer();

        // 恢复控制器
        if (mController != null) {
            mController.reset();
        }
        Runtime.getRuntime().gc();
    }


    private void initAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        }
    }


    private void initMediaPlayer() {

        if (mMediaPlayer == null) {
            switch (mPlayerType) {
                case TYPE_NATIVE:
                    mMediaPlayer = new AndroidVideoPlayer(mContext);
                    break;
                case TYPE_IJK:
                    mMediaPlayer = new IJKVideoPlayer(mContext);
                    break;
                case TYPE_ALI:
                    mMediaPlayer = new AliVideoPlayer(mContext);
                    break;
                case TYPE_EXO:
                    mMediaPlayer  =  new ExoVideoPlayer(mContext);
                    break;
                default:
                    mMediaPlayer = new AndroidVideoPlayer(mContext);
                    break;
            }
            // mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }


    private void initTextureView() {
        if (mTextureView == null) {
            mTextureView = new VideoTextureView(mContext);
            mTextureView.setSurfaceTextureListener(this);
        }
    }


    private void addTextureView() {
        mContainer.removeView(mTextureView);
        LayoutParams params = new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER);
        mContainer.addView(mTextureView, 0, params);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture;
            openMediaPlayer();
        } else {
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }


    @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return mSurfaceTexture == null;
    }


    @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
