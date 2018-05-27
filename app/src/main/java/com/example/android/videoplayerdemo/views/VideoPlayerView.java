package com.example.android.videoplayerdemo.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class VideoPlayerView extends FrameLayout {
    private Context mContext;
    private FrameLayout mContainer;

    public VideoPlayerView(@NonNull Context context) {
        super(context);
    }


    public VideoPlayerView(
        @NonNull Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public VideoPlayerView(
        @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
