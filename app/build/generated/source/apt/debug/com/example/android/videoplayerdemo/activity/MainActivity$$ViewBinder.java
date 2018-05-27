// Generated code from Butter Knife. Do not modify!
package com.example.android.videoplayerdemo.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.example.android.videoplayerdemo.activity.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165248, "field 'btn1'");
    target.btn1 = finder.castView(view, 2131165248, "field 'btn1'");
    view = finder.findRequiredView(source, 2131165249, "field 'btn2'");
    target.btn2 = finder.castView(view, 2131165249, "field 'btn2'");
    view = finder.findRequiredView(source, 2131165250, "field 'btn3'");
    target.btn3 = finder.castView(view, 2131165250, "field 'btn3'");
    view = finder.findRequiredView(source, 2131165251, "field 'btn4'");
    target.btn4 = finder.castView(view, 2131165251, "field 'btn4'");
  }

  @Override public void unbind(T target) {
    target.btn1 = null;
    target.btn2 = null;
    target.btn3 = null;
    target.btn4 = null;
  }
}
