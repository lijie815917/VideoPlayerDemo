// Generated code from Butter Knife. Do not modify!
package com.example.android.videoplayerdemo.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class IJKVideoPlayerActivity$$ViewBinder<T extends com.example.android.videoplayerdemo.activity.IJKVideoPlayerActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165431, "field 'video'");
    target.video = finder.castView(view, 2131165431, "field 'video'");
    view = finder.findRequiredView(source, 2131165364, "field 'rbSysVideo'");
    target.rbSysVideo = finder.castView(view, 2131165364, "field 'rbSysVideo'");
    view = finder.findRequiredView(source, 2131165361, "field 'rbAliVideo'");
    target.rbAliVideo = finder.castView(view, 2131165361, "field 'rbAliVideo'");
    view = finder.findRequiredView(source, 2131165363, "field 'rbIjkVideo'");
    target.rbIjkVideo = finder.castView(view, 2131165363, "field 'rbIjkVideo'");
    view = finder.findRequiredView(source, 2131165362, "field 'rbExoVideo'");
    target.rbExoVideo = finder.castView(view, 2131165362, "field 'rbExoVideo'");
    view = finder.findRequiredView(source, 2131165424, "field 'tvStartTime'");
    target.tvStartTime = finder.castView(view, 2131165424, "field 'tvStartTime'");
    view = finder.findRequiredView(source, 2131165425, "field 'tvStopTime'");
    target.tvStopTime = finder.castView(view, 2131165425, "field 'tvStopTime'");
    view = finder.findRequiredView(source, 2131165252, "field 'btnMirror'");
    target.btnMirror = finder.castView(view, 2131165252, "field 'btnMirror'");
    view = finder.findRequiredView(source, 2131165256, "field 'btnSpeed'");
    target.btnSpeed = finder.castView(view, 2131165256, "field 'btnSpeed'");
    view = finder.findRequiredView(source, 2131165370, "field 'rgVideo'");
    target.rgVideo = finder.castView(view, 2131165370, "field 'rgVideo'");
    view = finder.findRequiredView(source, 2131165253, "field 'btnA'");
    target.btnA = finder.castView(view, 2131165253, "field 'btnA'");
    view = finder.findRequiredView(source, 2131165255, "field 'btnB'");
    target.btnB = finder.castView(view, 2131165255, "field 'btnB'");
    view = finder.findRequiredView(source, 2131165254, "field 'btnAB'");
    target.btnAB = finder.castView(view, 2131165254, "field 'btnAB'");
  }

  @Override public void unbind(T target) {
    target.video = null;
    target.rbSysVideo = null;
    target.rbAliVideo = null;
    target.rbIjkVideo = null;
    target.rbExoVideo = null;
    target.tvStartTime = null;
    target.tvStopTime = null;
    target.btnMirror = null;
    target.btnSpeed = null;
    target.rgVideo = null;
    target.btnA = null;
    target.btnB = null;
    target.btnAB = null;
  }
}
