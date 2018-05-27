package com.example.android.videoplayerdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.example.android.videoplayerdemo.R;
import com.example.android.videoplayerdemo.interfaces.OnVideoPlayerEventListener;
import com.example.android.videoplayerdemo.manager.VideoPlayerManage;
import com.example.android.videoplayerdemo.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AliVideoPlayerActivity extends AppCompatActivity implements View.OnClickListener,OnVideoPlayerEventListener,SeekBar.OnSeekBarChangeListener{

    @Bind(R.id.surface_ali)
    SurfaceView surfaceAli;
    @Bind(R.id.iv_video_play_operation)
    ImageView ivVideoPlayOperation;
    @Bind(R.id.sb_video_play_progress_seek_bar)
    SeekBar sbVideoPlayProgressSeekBar;
    @Bind(R.id.tv_video_play_current_time)
    TextView tvVideoPlayCurrentTime;
    @Bind(R.id.tv_video_play_total_time)
    TextView tvVideoPlayTotalTime;
    @Bind(R.id.iv_video_play_full_screen)
    ImageView ivVideoPlayFullScreen;
    @Bind(R.id.rl_video_play_operation)
    RelativeLayout rlVideoPlayOperation;
    @Bind(R.id.rel_ali)
    RelativeLayout relAli;

//
    private AliyunVodPlayer mPlayer;
    private VideoPlayerManage playerManage;

    /**
     * 视频播放的进度
     */
    private int progress = 0;

    private String videoUri
            = "https://res.exexm.com/cw_145225549855002";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_video_layout);
        ButterKnife.bind(this);
        init();
    }


    /**
     * 初始化阿里云
     */
    private void init() {
        playerManage = VideoPlayerManage.getPlayerManage();
        playerManage.init(this, surfaceAli, videoUri);
        playerManage.addOnVideoPlayerEventListener(this);
        ivVideoPlayOperation.setOnClickListener(this);
        mPlayer = playerManage.getAliPlayer();
        playerManage.play();

        sbVideoPlayProgressSeekBar.setMax(100);
        sbVideoPlayProgressSeekBar.setOnSeekBarChangeListener(this);
        sbVideoPlayProgressSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    default:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_video_play_operation:
//                playerManage.playPause();
                break;

        }
    }


    @Override
    public void onPrepared() {
        tvVideoPlayTotalTime.setText(TimeUtils.millsecondsToStr((int) mPlayer.getDuration()));
    }

    @Override
    public void onChange() {

    }

    @Override
    public void onMediaPlayerStart() {
        ivVideoPlayOperation.setImageResource(R.drawable.video_play_pause_selector);
    }

    @Override
    public void onMediaPlayerPause() {
        ivVideoPlayOperation.setImageResource(R.drawable.video_play_icon_selector);
    }

    @Override
    public void onUpdateProgress(int position) {
        int pos = (int) (sbVideoPlayProgressSeekBar.getMax() * position / mPlayer.getDuration());
        sbVideoPlayProgressSeekBar.setProgress(pos);
        tvVideoPlayCurrentTime.setText(TimeUtils.millsecondsToStr(position) + "/");
    }

    @Override
    public void onBufferingUpdate(int percent) {
        sbVideoPlayProgressSeekBar.setSecondaryProgress(sbVideoPlayProgressSeekBar.getMax() * 100 / percent);
    }

    /**
     * seek相关监听
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.progress = (int) (progress * mPlayer.getDuration() / sbVideoPlayProgressSeekBar.getMax());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        playerManage.seekTo(progress);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(videoUri) && !playerManage.getSurfaceState()) {
            if (playerManage.isPlaying()) {
                playerManage.startPlayer();
            } else if (!playerManage.isPreparing()) {
                //初始化播放信息

            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
