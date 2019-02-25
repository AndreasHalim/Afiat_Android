// Generated code from Butter Knife. Do not modify!
package com.example.afiat;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RegisterActivity_ViewBinding implements Unbinder {
  private RegisterActivity target;

  private View view2131296311;

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegisterActivity_ViewBinding(final RegisterActivity target, View source) {
    this.target = target;

    View view;
    target.editTextEmail = Utils.findRequiredViewAsType(source, R.id.edit_text_email_activity_signup, "field 'editTextEmail'", EditText.class);
    target.editTextPassword = Utils.findRequiredViewAsType(source, R.id.edit_text_password_activity_signup, "field 'editTextPassword'", EditText.class);
    target.relativeLayoutProgress = Utils.findRequiredViewAsType(source, R.id.relative_layout_progress_activity_signup, "field 'relativeLayoutProgress'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.button_next_activity_signup, "method 'onClick'");
    view2131296311 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(Utils.castParam(p0, "doClick", 0, "onClick", 0, Button.class));
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RegisterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.editTextEmail = null;
    target.editTextPassword = null;
    target.relativeLayoutProgress = null;

    view2131296311.setOnClickListener(null);
    view2131296311 = null;
  }
}
