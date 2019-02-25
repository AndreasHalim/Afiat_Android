// Generated code from Butter Knife. Do not modify!
package com.example.afiat.screen.achievement;

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

public class AchievementFragment_ViewBinding implements Unbinder {
  private AchievementFragment target;

  @UiThread
  public AchievementFragment_ViewBinding(AchievementFragment target, View source) {
    this.target = target;

    target.tvMaxSteps = Utils.findRequiredViewAsType(source, R.id.tv_max_steps, "field 'tvMaxSteps'", TextView.class);
    target.tvMaxSpeed = Utils.findRequiredViewAsType(source, R.id.tv_max_speed, "field 'tvMaxSpeed'", TextView.class);
    target.tvMaxDistance = Utils.findRequiredViewAsType(source, R.id.tv_max_distance, "field 'tvMaxDistance'", TextView.class);
    target.btnShare = Utils.findRequiredViewAsType(source, R.id.btn_share, "field 'btnShare'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AchievementFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvMaxSteps = null;
    target.tvMaxSpeed = null;
    target.tvMaxDistance = null;
    target.btnShare = null;
  }
}
