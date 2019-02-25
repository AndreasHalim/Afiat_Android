// Generated code from Butter Knife. Do not modify!
package com.example.afiat.screen.main;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.afiat.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainFragment_ViewBinding implements Unbinder {
  private MainFragment target;

  @UiThread
  public MainFragment_ViewBinding(MainFragment target, View source) {
    this.target = target;

    target.tvSteps = Utils.findRequiredViewAsType(source, R.id.tv_steps, "field 'tvSteps'", TextView.class);
    target.tvSpeed = Utils.findRequiredViewAsType(source, R.id.tv_speed, "field 'tvSpeed'", TextView.class);
    target.tvDistance = Utils.findRequiredViewAsType(source, R.id.tv_distance, "field 'tvDistance'", TextView.class);
    target.btnToggle = Utils.findRequiredViewAsType(source, R.id.btn_toggle, "field 'btnToggle'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvSteps = null;
    target.tvSpeed = null;
    target.tvDistance = null;
    target.btnToggle = null;
  }
}
