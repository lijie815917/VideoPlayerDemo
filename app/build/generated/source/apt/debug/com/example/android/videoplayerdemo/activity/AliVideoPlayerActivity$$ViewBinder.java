// Generated code from Butter Knife. Do not modify!
package com.example.android.videoplayerdemo.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AliVideoPlayerActivity$$ViewBinder<T extends com.example.android.videoplayerdemo.activity.AliVideoPlayerActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165409, "field 'surfaceAli'");
    target.surfaceAli = finder.castView(view, 2131165409, "field 'surfaceAli'");
    view = finder.findRequiredView(source, 2131165333, "field 'ivVideoPlayOperation'");
    target.ivVideoPlayOperation = finder.castView(view, 2131165333, "field 'ivVideoPlayOperation'");
    view = finder.findRequiredView(source, 2131165375, "field 'sbVideoPlayProgressSeekBar'");
    target.sbVideoPlayProgressSeekBar = finder.castView(view, 2131165375, "field 'sbVideoPlayProgressSeekBar'");
    view = finder.findRequiredView(source, 2131165426, "field 'tvVideoPlayCurrentTime'");
    target.tvVideoPlayCurrentTime = finder.castView(view, 2131165426, "field 'tvVideoPlayCurrentTime'");
    view = finder.findRequiredView(source, 2131165427, "field 'tvVideoPlayTotalTime'");
    target.tvVideoPlayTotalTime = finder.castView(view, 2131165427, "field 'tvVideoPlayTotalTime'");
    view = finder.findRequiredView(source, 2131165332, "field 'ivVideoPlayFullScreen'");
    target.ivVideoPlayFullScreen = finder.castView(view, 2131165332, "field 'ivVideoPlayFullScreen'");
    view = finder.findRequiredView(source, 2131165374, "field 'rlVideoPlayOperation'");
    target.rlVideoPlayOperation = finder.castView(view, 2131165374, "field 'rlVideoPlayOperation'");
    view = finder.findRequiredView(source, 2131165365, "field 'relAli'");
    target.relAli = finder.castView(view, 2131165365, "field 'relAli'");
  }

  @Override public void unbind(T target) {
    target.surfaceAli = null;
    target.ivVideoPlayOperation = null;
    target.sbVideoPlayProgressSeekBar = null;
    target.tvVideoPlayCurrentTime = null;
    target.tvVideoPlayTotalTime = null;
    target.ivVideoPlayFullScreen = null;
    target.rlVideoPlayOperation = null;
    target.relAli = null;
  }
}
